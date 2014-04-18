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
package net.sourceforge.jabm.report;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Map;

import net.sourceforge.jabm.Population;
import net.sourceforge.jabm.event.BatchFinishedEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.event.SimulationFinishedEvent;

public class TotalFitnessCsvWriter implements Report, Serializable {

	protected transient CSVWriter csvWriter;
	
	public TotalFitnessCsvWriter(String filename) {
		try {
			csvWriter = new CSVWriter(new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void eventOccurred(SimEvent e) {
		if (e instanceof SimulationFinishedEvent) {
			SimulationFinishedEvent event = (SimulationFinishedEvent) e;
			Population population = event.getSimulation()
					.getSimulationController().getPopulation();
			double totalFitness = population.getAgentList().getTotalFitness();
			csvWriter.newData(new String[] { "" + totalFitness });
			csvWriter.endRecord();

			System.out.println("totalFitness = " + totalFitness);
		}
		if (e instanceof BatchFinishedEvent) {
			csvWriter.close();
		}
	}

	@Override
	public Map<Object, Number> getVariableBindings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return getClass().getName();
	}

	
}
