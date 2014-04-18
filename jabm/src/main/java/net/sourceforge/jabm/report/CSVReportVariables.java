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
import java.util.Map;

import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.util.MutableStringWrapper;

/**
 * CSVReportVariables automatically record other ReportVariables to a 
 * comma separated variables (CSV) file whenever they are computed.
 * 
 * @author Steve Phelps
 *
 */
public class CSVReportVariables implements ReportVariables {

	protected String fileNameSuffix;
	
	protected Object fileNamePrefix;
	
	protected Object fileNameExtension = ".csv";
	
	protected CSVWriter writer;
	
	/**
	 * This report collects the data that we will write.
	 */
	protected ReportVariables reportVariables;
	
	protected int fileNumber = 0;
	
	protected boolean autoNumbering = true;
	
	protected boolean passThrough = false;
	
	public CSVReportVariables(String fileNameSuffix,
			Object fileNamePrefix, ReportVariables reportVariables) {
		super();
		this.fileNameSuffix = fileNameSuffix;
		this.fileNamePrefix = fileNamePrefix;
		this.reportVariables = reportVariables;
	}

	public CSVReportVariables(String fileNameSuffix,
			ReportVariables reportVariables) {
		this(fileNameSuffix, new MutableStringWrapper(""), reportVariables);
	}
	
	public CSVReportVariables() {
		this("", null);
	}

	@Override
	public void compute(SimEvent event) {
		if (writer == null) {
			createWriter();
		}
		if (passThrough) {
			reportVariables.compute(event);
		}
		Map<Object, Number> variables = getVariableBindings();
		@SuppressWarnings("unused")
		int columns = variables.keySet().size();
		for(Object variable : variables.keySet()) {
			Number value = variables.get(variable);
			writer.newData(value);
		}
		writer.endRecord();
		writer.flush();
	}
	
	public void createWriter() {
		try {
			String fileName = getFileName();
			writer = new CSVWriter(new FileOutputStream(fileName));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getFileName() {
		return "" + fileNamePrefix
		 + fileNameSuffix + getNumberingSuffix() + fileNameExtension;
	}
	
	public String getNumberingSuffix() {
		if (autoNumbering) {
			return Integer.toString(fileNumber + 1);
		} else {
			return "";
		}
	}

	@Override
	public Map<Object, Number> getVariableBindings() {
		return reportVariables.getVariableBindings();
	}

	public String getFileNameSuffix() {
		return fileNameSuffix;
	}

	public void setFileNameSuffix(String fileNameSuffix) {
		this.fileNameSuffix = fileNameSuffix;
	}

	public Object getFileNamePrefix() {
		return fileNamePrefix;
	}

	public void setFileNamePrefix(Object fileNamePrefix) {
		this.fileNamePrefix = fileNamePrefix;
	}

	@Override
	public void eventOccurred(SimEvent event) {
		if (passThrough) {
			reportVariables.eventOccurred(event);
		}
	}

	@Override
	public void dispose(SimEvent event) {
		if (passThrough) {
			reportVariables.dispose(event);
		}
		if (writer != null) {
			writer.close();
		}
		fileNumber++;
	}
	
	@Override
	public void initialise(SimEvent event) {
		createWriter();
		if (passThrough) {
			reportVariables.initialise(event);
		}
	}

	public ReportVariables getReportVariables() {
		return reportVariables;
	}

	public void setReportVariables(ReportVariables reportVariables) {
		this.reportVariables = reportVariables;
	}

	public boolean isAutoNumbering() {
		return autoNumbering;
	}

	public void setAutoNumbering(boolean autoNumbering) {
		this.autoNumbering = autoNumbering;
	}

	public Object getFileNameExtension() {
		return fileNameExtension;
	}

	public void setFileNameExtension(Object fileNameExtension) {
		this.fileNameExtension = fileNameExtension;
	}

	public boolean isPassThrough() {
		return passThrough;
	}

	public void setPassThrough(boolean passThrough) {
		this.passThrough = passThrough;
	}

	public String getName() {
		return getClass().getName() + "." + reportVariables.getName();
	}
}
