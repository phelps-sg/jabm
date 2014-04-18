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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

import net.sourceforge.jabm.event.BatchFinishedEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.gametheory.CompressedPayoffMatrix;
import net.sourceforge.jabm.gametheory.GameTheoreticSimulationController;

public class HeuristicPayoffReport implements Report, Serializable {

	protected String csvFileName;
	
	protected String binFileName;	
	
	public HeuristicPayoffReport(String csvFileName, String binFileName) {
		super();
		this.binFileName = binFileName;
		this.csvFileName = csvFileName;
	}
	
	public HeuristicPayoffReport() {
		super();
	}

	public Map<Object, Number> getVariableBindings() {
		// TODO Auto-generated method stub
		return null;
	}

	public void eventOccurred(SimEvent event) {		
		if (event instanceof BatchFinishedEvent) {			
			onBatchFinished((BatchFinishedEvent) event);
		}
	}

	public void onBatchFinished(BatchFinishedEvent event) {
		try {
			GameTheoreticSimulationController simulationController = (GameTheoreticSimulationController) event
					.getSimulationController();
			CompressedPayoffMatrix payoffMatrix = simulationController
					.getPayoffMatrix();
			int numStrategies = payoffMatrix.getNumStrategies();
			CSVWriter csvOut = new CSVWriter(new FileOutputStream(csvFileName),
					numStrategies * 3, '\t');
			payoffMatrix.export(csvOut);
			csvOut.flush();
			if (binFileName != null) {
				ObjectOutputStream binaryOut = new ObjectOutputStream(
						new FileOutputStream(binFileName));
				binaryOut.writeObject(payoffMatrix);
				binaryOut.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getCsvFileName() {
		return csvFileName;
	}

	public void setCsvFileName(String csvFileName) {
		this.csvFileName = csvFileName;
	}

	public String getBinFileName() {
		return binFileName;
	}

	public void setBinFileName(String binFileName) {
		this.binFileName = binFileName;
	}

	@Override
	public String getName() {
		return getClass().toString();
	}
	
}
