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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.jabm.spring.BeanFactorySingleton;
import net.sourceforge.jabm.spring.PropertyOverrideWithReferencesConfigurer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;

import cern.jet.random.engine.RandomSeedGenerator;

/**
 * A simulation experiment represents a simulation and a corresponding treatment:
 * that is, a set of parameter bindings represented as Java Properties.
 * 
 * @author Steve Phelps
 *
 */
public class SimulationExperiment implements Runnable {

	/**
	 * The SimulationController for conducting this experiment.
	 */
	protected SimulationController model;

	/**
	 * The properties specified here represent parameter bindings
	 * which override those specified in the original model configuration.
	 */
	protected Properties properties;

	/**
	 * The path name of the directory where report data will be written to.
	 */
	protected String directoryName;
	
	/**
	 * The Spring BeanFactory instance which is used to construct the
	 * model.
	 */
	protected BeanFactory beanFactory;
	
	protected static RandomSeedGenerator prngSeedGenerator = 
			new RandomSeedGenerator();

	static Logger logger = Logger.getLogger(SimulationExperiment.class);

	public SimulationExperiment(BeanFactory beanFactory,
			String baseDirName, int experimentNumber,
			Map<String, String> variableBindings,
			boolean generateSeeds, int seedMask) {
		super();
		logger.info("Initialising experiment " + (experimentNumber+1) + " ..");
		this.beanFactory = beanFactory;
		properties = new Properties();
		for (String variable : variableBindings.keySet()) {
			properties.put(variable, variableBindings.get(variable));
		}
		logger.info("Using properties: " + properties);
		
		directoryName = baseDirName + "/experiment" + (experimentNumber + 1)
							+ "/";
		properties.put("fileNamePrefix.value", directoryName);
		
		if (generateSeeds) {
			int seed = prngSeedGenerator.nextSeed();
			if (seedMask > 0) {
				seed = seed | ((seedMask & 0x0ffff) << 48);  
			}
			properties.put("prng.seed", seed + "");
		}
		
		initialise();
		logger.info("initialisation done.");
	}

	public SimulationExperiment(Properties properties) {
		this.properties = properties;
		this.directoryName = properties.getProperty("fileNamePrefix.value");
		this.beanFactory = BeanFactorySingleton.getBeanFactory();
		initialise();
	}

	/**
	 * Initialise the model by constructing it from the bean factory
	 * and then applying the parameter bindings specified by the properties
	 * attribute using Spring's properties post-processing feature.
	 */
	public void initialise() {
		PropertyOverrideConfigurer configurer = 
			new PropertyOverrideWithReferencesConfigurer();
		configurer.setProperties(properties);
		configurer.postProcessBeanFactory(
				(ConfigurableListableBeanFactory) beanFactory);
		this.model = (SimulationController) beanFactory
				.getBean("simulationController");
	}

	/**
	 * Run this experiment.
	 */
	public void run() {
		logProperties();
		model.run();
	}
	
	/**
	 * Print a summary of the parameter bindings for this experiment
	 * to a log.
	 */
	public void logProperties() {
		if (properties != null && properties.size() > 0) {
			logger.info("Using properties " + properties);
		}
	}

	/**
	 * Save the parameter bindings of this experiment to a property file.
	 * The property file is called 'experiment.properties' and is saved in 
	 * the same directory as the report data.
	 */
	public void createPropertyFile() {
		File propertyFile = null;
		try {
			File directory = new File(directoryName);
			directory.mkdir();
			propertyFile = new File(directoryName + "experiment.properties");
			if (!propertyFile.exists()) {
				logger.debug("Creating property file " + propertyFile);
				propertyFile.createNewFile();
			}
			FileOutputStream propertyFileOutputStream = 
				new FileOutputStream(propertyFile, false);
			logger.info("Saving properties in " + propertyFile);
			properties.store(propertyFileOutputStream, this.getClass()
					.toString());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			logger.error(propertyFile.toString() + ": " + e);
			throw new RuntimeException(e);
		}
	}
	
}
