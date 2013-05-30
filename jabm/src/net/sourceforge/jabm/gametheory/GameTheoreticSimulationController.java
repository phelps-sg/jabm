/*
 * JABM - Java Agent-Based Modeling Toolkit
 * Copyright (C) 2011 Steve Phelps
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
package net.sourceforge.jabm.gametheory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.jabm.Population;
import net.sourceforge.jabm.SpringSimulationController;
import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.init.AgentInitialiser;
import net.sourceforge.jabm.init.StrategyInitialiser;
import net.sourceforge.jabm.report.CSVWriter;
import net.sourceforge.jabm.report.PayoffByStrategyReportVariables;
import net.sourceforge.jabm.report.PayoffMap;
import net.sourceforge.jabm.strategy.Strategy;
import net.sourceforge.jabm.util.MutableStringWrapper;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * A simulation controller which can be used to generate a heuristic payoff
 * matrix, as described in the following paper:
 * 
 * ﻿Wellman, M. P. (2006). Methods for Empirical Game-Theoretic Analysis.
 * Twenty-First National Conference on Artificial Intelligence (AAAI-06) (pp.
 * 1152-1155). Boston, Massachusetts.
 * 
 * @author Seve Phelps
 * 
 */
public class GameTheoreticSimulationController extends SpringSimulationController
		implements Serializable {

	protected CompressedPayoffMatrix payoffMatrix;

	protected List<Strategy> strategies;

	protected List<StrategyInitialiser> strategyInitialisers;

	protected PayoffByStrategyReportVariables payoffByStrategy;
	
	protected CompressedPayoffMatrix.Entry currentEntry;

	protected String csvFileName;
	
	protected MutableStringWrapper fileNamePrefix;

	protected String binFileName;

	protected CSVWriter csvOut;
	
	protected ObjectOutputStream binaryOut;

	static Logger logger = Logger.getLogger(GameTheoreticSimulationController.class);

	public GameTheoreticSimulationController() {
	}
	
	public void initialise() {
		strategies = new ArrayList<Strategy>(strategyInitialisers.size());
		for (StrategyInitialiser init : strategyInitialisers) {
			ObjectFactory<Strategy> factory = init.getStrategyFactory();
			strategies.add(factory.getObject());
		}
		int numAgents = getPopulation().getAgents().size();
		payoffMatrix = new CompressedPayoffMatrix(strategies, numAgents);
	}

	public void resize(int numAgents) {
		payoffMatrix = new CompressedPayoffMatrix(strategies, numAgents);
		Population population = getPopulation();
		population.setSize(numAgents);
		population.reset();
	}

	@Override
	public void run() {
		initialiseOutput();
		Iterator<CompressedPayoffMatrix.Entry> i = payoffMatrix
				.compressedEntryIterator();
		while (i.hasNext()) {
			this.currentEntry = i.next();
			logger.info("Computing payoffs for " + this.currentEntry);
			this.payoffByStrategy.setPayoffMap(new PayoffMap(strategies));
			super.run();
			updatePayoffs(this.currentEntry);
			logger.info("done.");
		}
		logger.info("completed payoff matrix.");
		exportPayoffPatrix();
	}
	
	@Override
	protected void constructSimulation() {
		super.constructSimulation();
		initialiseAgents(this.currentEntry);
	}

	public SummaryStatistics getPayoffDistribution(Strategy strategy) {
		return payoffByStrategy.getPayoffMap().getPayoffDistribution(strategy);
	}

	public void updatePayoffs(CompressedPayoffMatrix.Entry entry) {
		try {
			PayoffMap payoffs = payoffByStrategy.getPayoffMap();
			payoffMatrix.setCompressedPayoffs(entry,
					(PayoffMap) payoffs.clone());
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	public void initialiseAgents(CompressedPayoffMatrix.Entry entry) {
		logger.debug("Initialising agents for entry " + entry);
//		TODO: ?
//		initialiseAgents();
		Iterator<Agent> agentIterator = getPopulation()
				.getAgents().iterator();
		for (int s = 0; s < strategies.size(); s++) {
			int n = entry.numAgentsPerStrategy[s];
			if (n == 0) {
				continue;
			}
			Population agents = new Population();
			for (int i = 0; i < n; i++) {
				Agent agent = agentIterator.next();
				Strategy oldStrategy = agent.getStrategy();
				if (oldStrategy != null) {
					oldStrategy.setAgent(null);
					agent.setStrategy(null);
				}
				agents.add(agent);
			}
			AgentInitialiser initialiser = strategyInitialisers.get(s);
			if (logger.isDebugEnabled())
				logger.debug("Initialising " + agents + " with strategy " + s);
			initialiser.initialise(agents);
		}
	}

	public CompressedPayoffMatrix getPayoffMatrix() {
		return payoffMatrix;
	}

	public void setPayoffMatrix(CompressedPayoffMatrix payoffMatrix) {
		this.payoffMatrix = payoffMatrix;
	}

	public List<Strategy> getStrategies() {
		return strategies;
	}

	public Collection<Agent> getAgents() {
		return getPopulation().getAgents();
	}

	public List<StrategyInitialiser> getStrategyInitialisers() {
		return strategyInitialisers;
	}

	@Required
	public void setStrategyInitialisers(
			List<StrategyInitialiser> strategyInitialisers) {
		this.strategyInitialisers = strategyInitialisers;
	}

	@Required
	public PayoffByStrategyReportVariables getPayoffByStrategy() {
		return payoffByStrategy;
	}

	@Required
	public void setPayoffByStrategy(
			PayoffByStrategyReportVariables payoffByStrategy) {
		this.payoffByStrategy = payoffByStrategy;
	}

	public String getCsvFileName() {
		return csvFileName;
	}

	public void setCsvFileName(String csvFileName) {
		this.csvFileName = csvFileName;
	}

	public MutableStringWrapper getFileNamePrefix() {
		return fileNamePrefix;
	}

	public void setFileNamePrefix(MutableStringWrapper fileNamePrefix) {
		this.fileNamePrefix = fileNamePrefix;
	}

	public String getBinFileName() {
		return binFileName;
	}

	public void setBinFileName(String binFileName) {
		this.binFileName = binFileName;
	}

	public void exportPayoffPatrix() {
		try {
			if (csvOut != null) {
				payoffMatrix.export(csvOut);
				csvOut.flush();
			}
			if (this.binaryOut != null) {
				binaryOut.writeObject(payoffMatrix);
				binaryOut.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void initialiseOutput() {
		try {
			if (csvFileName != null) {
				int numStrategies = payoffMatrix.getNumStrategies();
				String fullFileName = this.fileNamePrefix + this.csvFileName;
				this.csvOut = new CSVWriter(new FileOutputStream(
						fullFileName), numStrategies * 4, '\t');
			}
			if (binFileName != null) {
				this.binaryOut = new ObjectOutputStream(
						new FileOutputStream(binFileName));	
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		initialise();
	}
}
