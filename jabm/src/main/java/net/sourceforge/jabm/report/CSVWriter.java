/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2013 Steve Phelps
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package net.sourceforge.jabm.report;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * A class for writing data to CSV (comma-separated variables) text files.
 * 
 * @author Steve Phelps
 * @version $Revision: 189 $
 */

public class CSVWriter implements Serializable, DataWriter {

	protected transient PrintStream out;

	protected boolean autowrap = true;

	protected int numColumns;

	protected int currentColumn = 0;

	protected char seperator = DEFAULT_SEPERATOR;

	protected boolean append = true;

	static final char DEFAULT_SEPERATOR = '\t';

	static Logger logger = Logger.getLogger(CSVWriter.class);

	public CSVWriter(OutputStream out, int numColumns, char seperator) {
		this.out = new PrintStream(new BufferedOutputStream(out));
		this.numColumns = numColumns;
		this.seperator = seperator;
	}

	public CSVWriter(OutputStream out, char seperator) {
		this.out = new PrintStream(new BufferedOutputStream(out));
		this.autowrap = false;
		this.seperator = seperator;
	}

	public CSVWriter(OutputStream out, int numColumns) {
		this(out, numColumns, DEFAULT_SEPERATOR);
	}

	public CSVWriter(OutputStream out) {
		this(out, DEFAULT_SEPERATOR);
	}

	public CSVWriter() {
	}
//
//	public void setup(ParameterDatabase parameters, Parameter base) {
//		try {
//			String fileName = parameters.getString(base.push(P_FILENAME), null);
//			if (fileName == null) {
//				throw new FileNotFoundException(base.push(P_FILENAME) + " is NOT set!");
//			}
//
//			append = parameters.getBoolean(base.push(P_APPEND), null, append);
//			out = new PrintStream(new BufferedOutputStream(new FileOutputStream(
//			    new File(fileName), append)));
//			autowrap = parameters.getBoolean(base.push(P_AUTOWRAP), null, autowrap);
//			if (autowrap)
//				numColumns = parameters.getIntWithDefault(base.push(P_COLUMNS), null,
//				    numColumns);
//		} catch (FileNotFoundException e) {
//			throw new Error(e);
//		}
//	}

	@SuppressWarnings("rawtypes")
	public void newData(Iterator i) {
		while (i.hasNext()) {
			newData(i.next());
		}
	}

	public void newData(Object[] data) {
		for (int i = 0; i < data.length; i++) {
			newData(data[i]);
		}
	}

	public void newData(Boolean data) {
		if (data.booleanValue()) {
			newData(1);
		} else {
			newData(0);
		}
	}

	public void newData(Integer data) {
		newData(data.intValue());
	}

	public void newData(Double data) {
		newData(data.doubleValue());
	}

	public void newData(Long data) {
		newData(data.longValue());
	}

	public void newData(String data) {
		prepareColumn();
		out.print(data);
		nextColumn();
	}

	public void newData(int data) {
		prepareColumn();
		out.print(data);
		nextColumn();
	}

	public void newData(long data) {
		prepareColumn();
		out.print(data);
		nextColumn();
	}

	public void newData(double data) {
		prepareColumn();
		out.print(data);
		nextColumn();
	}

	public void newData(float data) {
		prepareColumn();
		out.print(data);
		nextColumn();
	}

	public void newData(boolean data) {
		if (data) {
			newData(1);
		} else {
			newData(0);
		}
	}

	public void newData(Object data) {
		if (data instanceof Boolean) {
			newData((Boolean) data);
		} else {
			prepareColumn();
			out.print(data.toString());
			nextColumn();
		}
	}

	public void setAutowrap(boolean autowrap) {
		this.autowrap = autowrap;
	}

	public void setAppend(boolean append) {
		this.append = append;
	}

	public void endRecord() {
		if (autowrap)
			new Error("endRecord() should NOT be invoked when autowrap is enabled.");
		newLine();
	}

	public void flush() {
		out.flush();
	}

	public void close() {
		out.close();
	}

	public void setNumColumns(int numColumns) {
		if (!autowrap)
			new Error(
			    "The number of columns should NOT be set when autowrap is disabled.");
		this.numColumns = numColumns;
	}

	protected void prepareColumn() {
		if (!autowrap)
			if (currentColumn > 0)
				out.print(seperator);
	}

	protected void nextColumn() {
		currentColumn++;
		if (autowrap)
			if (currentColumn < numColumns) {
				out.print(seperator);
			} else {
				newLine();
			}
	}

	private void newLine() {
		out.println();
		currentColumn = 0;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
	    ClassNotFoundException {
	}

}
