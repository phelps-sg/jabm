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

import java.io.Serializable;
import java.util.ArrayList;

import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.event.AbstractModel;
import net.sourceforge.jabm.event.BatchFinishedEvent;
import net.sourceforge.jabm.event.BatchStartingEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.init.SimulationFactory;
import net.sourceforge.jabm.report.Report;

import org.apache.log4j.Logger;

/**
 * The SimulationController is responsible for running a batch of one or more
 * independent Simulation runs. It is responsible for establishing the event
 * listening relationships between the different components of the simulation,
 * and is the container for all of the {@link Report} objects in the simulation.
 * 
 * @author Steve Phelps
 */
public abstract class SimulationController extends AbstractModel 
		implements Runnable, EventScheduler, Serializable {

	/**
	 * The index of the current simulation.
	 */
	protected int batch = 0;
	
	/**
	 * The total number of simulations to run.
	 */
	protected int numSimulations = 1;
	
	/**
	 * The reports that will collect data on the simulations.
	 */
	protected ArrayList<Report> reports = new ArrayList<Report>();
	
	/**
	 * The underlying simulation.
	 */
	protected Simulation simulation;

	/**
	 * The simulationFactory is responsible for initialising
	 * the simulation at the beginning of each run.
	 */
	protected SimulationFactory simulationFactory;
	
	protected boolean listenersInitialised = false;
	
	protected boolean isRunning;
	
	protected int slowSleepInterval = 0;

	/**
	 * A human-readable description of the model that can
	 * be presented to the user of the model, e.g. in a GUI.
	 */
	protected String modelDescription;
	
	static Logger logger = Logger.getLogger(SimulationController.class);
	
	public SimulationController() {
	}

	/**
	 * Run the batch of simulations in sequence.
	 */
	@Override
	public void run() {		
		setListeners();		
		fireEvent(new BatchStartingEvent(this));
		this.isRunning = true;
		for(batch = 0; batch<numSimulations && isRunning(); batch++) {
			if (logger.isInfoEnabled())
				logger.info("Running simulation " + (batch+1) + " of " + numSimulations + "..");
			runSingleSimulation();
			if (logger.isInfoEnabled())
				logger.info("simulation done.");
		}
		fireEvent(new BatchFinishedEvent(this));
		this.isRunning = false;
	}
	
	/**
	 * Run a single simulation.
	 */
	public void runSingleSimulation() {
		constructSimulation();
		simulation.run();
	}

	/**
	 * Query the simulation time in the currently running simulation.
	 */
	@Override
	public SimulationTime getSimulationTime() {
		return simulation.getSimulationTime();
	}
	
	@Override
	public void fireEvent(SimEvent event) {
		super.fireEvent(event);
	}
	
	/**
	 * Terminate all simulations.
	 */
	public void terminate() {
		logger.info("Terminating simulation(s).. ");
		simulation.resume();  // resume if paused
		simulation.terminate();
		isRunning = false;
	}

	/**
	 * Fetch the Population of the current simulation.
	 * @return
	 */
	public Population getPopulation() {
		return simulation.getPopulation();
	}

	public ArrayList<Report> getReports() {
		return reports;
	}

	/**
	 * Configure the reports for this simulation. 
	 * 
	 * @param reports
	 * @see net.sourceforge.jabm.report.Report
	 */
	public void setReports(ArrayList<Report> reports) {
		this.reports = reports;
	}
	
	public void addReport(Report report) {
		reports.add(report);
	}
	
	/**
	 * Fetch the total number of simulations to run in this batch.
	 */
	public int getNumSimulations() {
		return numSimulations;
	}

	/**
	 * Configure the number of independent simulations to run as 
	 * part of a Monte-carlo experiment.
	 * 
	 * @param numSimulations
	 */
	public void setNumSimulations(int numSimulations) {
		this.numSimulations = numSimulations;
	}
	
	public Simulation getSimulation() {
		return this.simulation;
	}
	
	public void setSimulation(Simulation simulation) {
		this.simulation = simulation;
	}

	public SimulationFactory getSimulationFactory() {
		return simulationFactory;
	}

	/**
	 * Configure the initialiser for this simulation.
	 * 
	 * @see net.sourceforge.jabm.init.SimulationFactory
	 * @param simulationInitialiser
	 */
	public void setSimulationFactory(
			SimulationFactory simulationInitialiser) {
		this.simulationFactory = simulationInitialiser;
	}
	
	public int getSlowSleepInterval() {
		return slowSleepInterval;
	}

	public void setSlowSleepInterval(int slowSleepInterval) {
		this.slowSleepInterval = slowSleepInterval;
	}
	
	/**
	 * Slow down the simulation by sleeping for the specified
	 * interval in between simulation steps.
	 * 
	 * @param slowSleepInterval
	 */
	public void slow(int slowSleepInterval) {
		setSlowSleepInterval(slowSleepInterval);
		if (simulation != null) {
			simulation.slow(slowSleepInterval);
		}
	}

	public boolean isRunning() {
		return isRunning;
	}
	
	/**
	 * Establish any listening relationships that are required prior
	 * to launching a new simulation.
	 */
	protected void setListeners() {
		logger.debug("Establishing listeners... ");
		wireReports();
		wireSimulation();
		logger.debug("done.");
	}
	
	/**
	 * Establish the listening relationships for Report objects.
	 */
	protected void wireReports() {
		logger.debug("Wiring reports... ");
		for (Report report : reports) {
			logger.debug("Adding listener " + report);
			this.addListener(report);
		}
		logger.debug("genericListeners = " + genericListeners);
		logger.debug("done.");
	}
	
	/**
	 * Configure the listener relationships for the simulation object. 
	 */
	protected void wireSimulation() {
		logger.debug("Wiring simulation... ");
		this.addListener(simulation.getPopulation());
		for(Agent agent : simulation.getPopulation().getAgents()) {
			agent.setScheduler(this);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("genericListeners = " + this.genericListeners);
			logger.debug("specificListeners = " + this.specificListeners);
		}
		logger.debug("done.");
	}
	
	/**
	 * Remove listeners prior to reconstructing the simulation object.
	 */
	protected void tearDownSimulation() {
		logger.debug("Tearing down simulation... ");
		clearListeners();
		if (logger.isDebugEnabled()) {
			logger.debug("specificListeners = " + specificListeners);
			logger.debug("genericListeners = " + genericListeners);
		}
		logger.debug("done.");
	}

	public String getModelDescription() {
		return modelDescription;
	}
	
	public void setModelDescription(String modelDescription) {
		this.modelDescription = modelDescription;
	}

	protected abstract void constructSimulation();
}
