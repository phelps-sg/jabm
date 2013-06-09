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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.jabm.spring.BeanFactorySingleton;
import net.sourceforge.jabm.util.SystemProperties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

/**
 * <p>
 * The main application class for JABM experiments when running in headless mode, 
 * e.g. on a cluster (to run using a GUI see {@link net.sourceforge.jabm.DesktopSimulationManager}).  
 * </p>  
 * 
 * <p>
 * Example usage:<br>
 * </p>
 * 
 * <p>
 *  <code>java -ea -server
 *  -Djabm.config=config/elfarolbar.xml  net.sourceforge.jabm.SimulationManager</code>
 *  </p>
 *   
 * <p>
 * System parameters:
 * </p>
 * 
 * <p>
 * <table border="1">
 * <tr>
 * <td><code>jabm.config</code></td> <td>The name of the Spring configuration file defining the simulation model</td>
 * </tr>
 * <tr>
 * <td><code>jabm.propertyfile</code></td> <td>The name of a properties file which overrides the values specified in the beans configuration</td>
 * </tr>
 * <tr>
 * <td><code>jabm.varfile</code></td> <td>The name of a properties file which specifies the ranges of independent variables to be used for a multiple treatment factor experiment</td>
 * </tr>
 * <td><code>jabm.configonly</code></td>  <td>If set to true in combintation with jabm.varfile then create experiment property files without running any simulations.  This is useful for running experiments on clusters.
 * </table>
 * 
 * @see DesktopSimulationManager
 * 
 * @author Steve Phelps
 */
public class SimulationManager implements Runnable {
	
	/**
	 * The file name of the .properties file to use for the experiment(s).
	 */
	protected String propFile;

	/**
	 * The (optional) file name of the variables file. The variables file
	 * specifies a parameter sweep experiment in which the specified properties
	 * are systematically varied over the specified ranges of values.
	 */
	protected String varFile;

	/**
	 * The (optional) base directory in which subdirectories will be created
	 * for experiments involving paramater sweeps.
	 */
	protected String baseDirName;

	/**
	 * If this option is set, then the simulation manager will create
	 * configuration files for experiments without actually running them.
	 */
	protected boolean configOnly;

	protected boolean generateSeeds;

	protected int seedMask;
	
	static Logger logger = Logger.getLogger(SimulationManager.class);

	/**
	 * The name of the spring bean representing the SimulationController
	 * which will be used to run the experiments.
	 */
	public static final String SIMULATION_CONTROLLER_BEAN 
		= "simulationController";

	
	public SimulationManager() {
		SystemProperties systemProperties = SystemProperties
				.jabsConfiguration();
		propFile = systemProperties
				.getProperty(SystemProperties.PROPERTY_PROPFILE);
		varFile = systemProperties.getProperty(
				SystemProperties.PROPERTY_VARFILE);
		baseDirName = systemProperties.getProperty(
				SystemProperties.PROPERTY_BASE_DIR_NAME, "data");	
		configOnly = Boolean.parseBoolean(systemProperties.getProperty(
				SystemProperties.PROPERTY_CONFIG_ONLY, "false"));
		generateSeeds = Boolean.parseBoolean(systemProperties.getProperty(
					SystemProperties.PROPERTY_SEEDS, "false"));
		seedMask = Integer.parseInt(systemProperties.getProperty(
							SystemProperties.PROPERTY_SEED_MASK, "-1"));
	}
	
	@Override
	public void run() {
		if (propFile != null) {
			runSingleExperiment(propFile);
		} else {
			if (varFile != null) {
				setup(varFile, baseDirName, generateSeeds,
												seedMask);
			} else {
				if (configOnly) {
					setup(baseDirName, generateSeeds,
													seedMask);
				} else {
					runSingleExperiment();
				}
			}
		}
	}

	public void launch(Runnable controller) {
		logger.info("Starting...");
		long start = System.currentTimeMillis();
		controller.run();
		long finish = System.currentTimeMillis();
		long duration = finish - start;
		logger.info("all done.");
		logger.info("completed simulation(s) in " + duration + "ms.");
	}
	
	public SimulationController getSimulationController() {
		return (SimulationController) BeanFactorySingleton
				.getBean(SIMULATION_CONTROLLER_BEAN);
	}
	
	public void runSingleExperiment() {		
		launch(getSimulationController());
	}
	
	public void runSingleExperiment(Properties properties) {
		SimulationExperiment experiment = new SimulationExperiment(
				properties);
		launch(experiment);
	}

	public void runSingleExperiment(String propFile) {
		try {
			logger.info("Running single experiment with properties " 
							+ propFile);
			Properties properties = new Properties();
			properties.load(new FileInputStream(propFile));
			runSingleExperiment(properties);
			return;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setup(String baseDirName, boolean generateSeeds,
								int seedMask) {
		logger.info("Creating experiment with no variable bindings...");
		logger.debug("baseDirName = " + baseDirName);
		BeanFactory beanFactory = BeanFactorySingleton.getBeanFactory();
		Map<String, String> emptyBindings = new HashMap<String, String>();
		SimulationExperiment experiment = new SimulationExperiment(beanFactory,
				baseDirName, 0, emptyBindings, generateSeeds, seedMask);
		experiment.createPropertyFile();
		logger.info("done.");
	}

	public void setup(String varFile, String baseDirName, 
								boolean generateSeeds, int seedMask) {
		logger.debug("baseDirName = " + baseDirName);
		long start = System.currentTimeMillis();
		logger.info("Configuring experiments from " + varFile + " ...");
		int experimentNumber = 0;

		BeanFactory beanFactory = BeanFactorySingleton.getBeanFactory();
		VariableBindingsIterator iterator = new VariableBindingsIterator(
				varFile, beanFactory);
		
		while (iterator.hasNext()) {
			Map<String, String> bindings = iterator.next();
			SimulationExperiment experiment = new SimulationExperiment(
					beanFactory, baseDirName, experimentNumber++, bindings,
					generateSeeds, seedMask);
			experiment.createPropertyFile();
		}
		long finish = System.currentTimeMillis();
		long duration = finish - start;
		logger.info("all done.");
		logger.info("Completed in " + duration + "ms.");
	}

	public static void main(String[] args) {
		SimulationManager manager = new SimulationManager();
		manager.run();
	}
	
}
