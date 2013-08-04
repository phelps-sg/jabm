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
package net.sourceforge.jabm;

import net.sourceforge.jabm.init.SpringSimulationFactory;
import net.sourceforge.jabm.spring.SimulationScope;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * <p>
 * <code>SpringSimulationController</code> is responsible for running
 * one or more independent JABM simulations which are configured using the 
 * <a href="http://www.springsource.com/developer/spring">Spring Framework</a>.
 * </p>
 * 
 * <p>
 * It is responsible for running one or more Monte-carlo simulations; that is,
 * any object implementing the <code>Simulation</code> interface.
 * Typically a simulation will be run several hundred times with different
 * realisations of random variables.  In order to ensure that each sample
 * is independent, the underlying simulation is initialised as a freshly
 * constructed bean from the Spring factory for every run.
 * </p>
 * 
 * <p>
 * A simulation model is constructed using 
 * <a href="http://martinfowler.com/articles/injection.html">dependency injection</a> 
 * by creating a 
 * <a href="http://unmaintainable.wordpress.com/2007/11/01/configuration-with-spring-beans/">
 * Spring beans configuration file</a> which specifies which classes to use 
 * in the simulation and the values of any attributes (parameters).  
 * The Spring configuration file is specified using the system property 
 * <code>jabm.config</code>.  
 * </p>
 * 
 * @author Steve Phelps
 * @see Simulation
 *
 */
public class SpringSimulationController extends SimulationController 
		implements BeanFactoryAware, InitializingBean {

	/**
	 * The name of the bean representing the </code>Simulation</code>.
	 */
	protected String simulationBeanName;
	
	/**
	 * The container for beans with scope="simulation".
	 */
	protected SimulationScope simulationScope;

	protected BeanFactory beanFactory;
	
	protected boolean simulationInitialised = false;
	
	static Logger logger = Logger.getLogger(SpringSimulationController.class);
	
	@Override
	protected void tearDownSimulation() {
		super.tearDownSimulation();
		simulationScope.startNewSimulation();
	}
	
	@Override
	protected void constructSimulation() {
		logger.debug("Constructing simulation... ");
		// Tear down any existing simulation object so that we can run afresh.
		tearDownSimulation();
		// First wire the reports so that they can listen to events fired
		//  during the initialisation phase.
		wireReports();
		// Now construct a fresh simulation.
		this.simulation = simulationFactory.initialise(this);
		logger.debug("simulation = " + simulation);
		// Now establish the remaining listeners
		wireSimulation();
		// All done
		this.simulationInitialised = true;
	}

	public String getSimulationBeanName() {
		return simulationBeanName;
	}

	/**
	 * The name of bean representing the Simulation to be run
	 * as part of this experiment.
	 * 
	 * @see Simulation
	 * @param simulationBean
	 */
	@Required
	public void setSimulationBeanName(String simulationBean) {
		this.simulationBeanName = simulationBean;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}
	
	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.simulation = (Simulation) beanFactory.getBean(simulationBeanName);
		this.simulationScope = SimulationScope.getSingletonInstance();
		if (this.simulationFactory == null) {
			this.simulationFactory = new SpringSimulationFactory();
		}
	}
	
}
