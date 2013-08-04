/*
 * JABM - Java Agent-Based Modeling Toolkit
 * Copyright (C) 2013 Steve Phelps
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */
package net.sourceforge.jabm.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import net.sourceforge.jabm.EventScheduler;
import net.sourceforge.jabm.Simulation;
import net.sourceforge.jabm.SimulationController;
import net.sourceforge.jabm.SpringSimulationController;
import net.sourceforge.jabm.event.RandomVariateInitialisedEvent;
import net.sourceforge.jabm.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;

import bsh.EvalError;
import bsh.Interpreter;
import cern.jet.random.engine.RandomEngine;

/**
 * <p>
 * A simulation factory which initialises the values of pre-specified
 * Spring bean properties by drawing randomly from the specified probability
 * distribution.  The bean properties are designated as random variates 
 * by creating a configuration file which specifies the mapping between
 * each variate and its corresponding distribution.
 * </p>  
 * 
 * <p>
 * Currently the following distributions are supported.
 * </p>
 * 
 * <table border="1">
 * <tr>
 * <td><code>N</code></td> <td>Normal distribution</td>
 * </tr>
 * <tr>
 * <td><code>U</code></td> <td>Uniform distribution</td>
 * </tr>
 * </table>
 * 
 * <p>
 * The parameters of the distribution are specified in brackets after
 * the abbreviation.  For example the following configuration:
 * </p>
 * 
 *  <code>
 *  agent.epsilon = U(0.2, 0.5)<br>
 *  agent.wealth = N(200.0, 10.0)<br>
 *  </code>
 *  
 * <p>
 * specifies that the agent's epsilon parameter is uniformly distributed 
 * between 0.2 and 0.5 and the agent's wealth is normally distributed with
 * mean 200 and standard deviation 10. 
 * </p>
 *  
 * @author Steve Phelps
 * @see java.util.Properties
 * @see cern.jet.random.AbstractDistribution
 *
 */
public class RandomVariateInitialiser extends SpringSimulationFactory
		implements InitializingBean {

	protected Object configFileNamePrefix = "";
	
	protected String configFileName = "config/random-variate.properties";

	protected RandomEngine prng;
	
	protected HashMap<String,String> expressionBindings;

	protected EventScheduler eventScheduler;
	
	static Logger logger = 
		Logger.getLogger(RandomVariateInitialiser.class);
	
	public RandomVariateInitialiser() {
		super();
	}

	@Override
	public Simulation initialise(SimulationController simulationController) {
		this.eventScheduler = simulationController;
		java.util.Properties variateBindings = new java.util.Properties();
		for(String variable : expressionBindings.keySet()) {
			Number value = evaluate(expressionBindings.get(variable), 
										variateBindings);
			variateBindings.put(variable, value + "");
			logger.info(variable + " = " + value);
			fireEvent(variable, value);
		}
		PropertyOverrideConfigurer configurer = 
			new PropertyOverrideConfigurer();
		configurer.setProperties(variateBindings);
		configurer.setLocalOverride(true);
		configurer
				.postProcessBeanFactory((ConfigurableListableBeanFactory) 
						((SpringSimulationController) simulationController)
						.getBeanFactory());
		return super.initialise(simulationController);
	}
	
	public void parseConfigFile() throws FileNotFoundException, IOException {
		expressionBindings = new LinkedHashMap<String, String>();
		File f = new File(configFileNamePrefix + configFileName);
		Properties properties = new Properties();
		properties.load(new FileInputStream(f));
		for(Object property : properties.keySet()) {
			String expression = (String) properties.get(property);
			expressionBindings.put(property.toString(), expression);
		}
	}
	
	public Number evaluate(String expression, 
								java.util.Properties variateBindings) {
		try {
			Interpreter bsh = initialiseBeanShell(variateBindings);
			logger.debug("Evalating " + expression);
			return (Number) bsh.eval(expression);
		} catch (EvalError e) {
			throw new RuntimeException(e);
		}
	}
	
	public Interpreter initialiseBeanShell(java.util.Properties variateBindings)
			throws EvalError {
		
		Interpreter bsh = new Interpreter();
		
		bsh.set("prng", this.prng);

		bsh.eval("double N(double mean, double std) "
				+ "{ return new cern.jet.random.Normal(mean,std,prng).nextDouble(); }");

		bsh.eval("double U(double min, double max) "
				+ "{ return new cern.jet.random.Uniform(min,max,prng).nextDouble(); }");

		for (Object variate : variateBindings.keySet()) {
			String var = variate.toString();
			bsh.set(var,
					Double.parseDouble(variateBindings.get(var).toString()));
		}
		return bsh;
	}

	protected void fireEvent(String variableName, Number value) {
		if (eventScheduler != null) {
			eventScheduler.fireEvent(new RandomVariateInitialisedEvent(
					variableName, value.doubleValue()));
		}
	}

	public String getConfigFileName() {
		return configFileName;
	}

	/**
	 * The name of the configuration file which specifies which
	 * bean properties are to be treated as random variates and their 
	 * corresponding distribution.  This file is in the standard 
	 * format for a Properties file.
	 * 
	 * @param configFileName
	 * @see java.util.Properties
	 */
	@Required
	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}

	public RandomEngine getPrng() {
		return prng;
	}

	/**
	 * The pseudo-random number generator to be used for drawing all
	 * variates drawn by this initialiser.
	 * 
	 * @param prng
	 */
	@Required
	public void setPrng(RandomEngine prng) {
		this.prng = prng;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		parseConfigFile();
	}

	public EventScheduler getEventScheduler() {
		return eventScheduler;
	}

	/**
	 * If this property is set then the initialiser will fire
	 * a RandomVariateInitialisedEvent each time a 
	 * random variate is initialised.
	 * 
	 * @param eventScheduler  The scheduler used to manage the event queue.
	 * 
	 * @see net.sourceforge.jabm.event.RandomVariateInitialisedEvent
	 * @see net.sourceforge.jabm.EventScheduler
	 */
	public void setEventScheduler(EventScheduler eventScheduler) {
		this.eventScheduler = eventScheduler;
	}

	public Object getConfigFileNamePrefix() {
		return configFileNamePrefix;
	}

	public void setConfigFileNamePrefix(Object configFileNamePrefix) {
		this.configFileNamePrefix = configFileNamePrefix;
	}
	
}
