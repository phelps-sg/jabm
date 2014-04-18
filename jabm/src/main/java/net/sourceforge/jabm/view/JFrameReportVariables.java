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
package net.sourceforge.jabm.view;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.report.ReportVariables;
import net.sourceforge.jabm.report.ReportWithGUI;

import org.springframework.beans.factory.InitializingBean;

/**
 * JFrameReportVariables automatically record other ReportVariables to a 
 * swing JTable on a window whenever they are computed.
 * 
 * @author Steve Phelps
 *
 */
public class JFrameReportVariables 
		implements ReportWithGUI, ReportVariables, TableModel, InitializingBean {

	protected LinkedList<TableModelListener> listeners 
		= new LinkedList<TableModelListener>();
	
	/**
	 * This report collects the data that we will write.
	 */
	protected ReportVariables reportVariables;
	
	protected JTable table;
	
	Map<Object, Number> variableBindings;
	
	DecimalFormat format
		= new DecimalFormat("#00000.00");
	
	public JFrameReportVariables() {
		super();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		table = new JTable(this);
//		this.setModel(this);
//		this.setPreferredSize(new Dimension(400, 200));
	}


	@Override
	public void compute(SimEvent event) {
		this.variableBindings = getVariableBindings();
//		reportVariables.compute(event);
		notifyTableChanged();
	}

	@Override
	public Map<Object, Number> getVariableBindings() {
		return reportVariables.getVariableBindings();
	}

	@Override
	public void eventOccurred(SimEvent event) {
//		reportVariables.eventOccurred(event);
	}

	@Override
	public void dispose(SimEvent event) {
//		reportVariables.dispose(event);
	}
	
	@Override
	public void initialise(SimEvent event) {
//		reportVariables.initialise(event);
	}

	public ReportVariables getReportVariables() {
		return reportVariables;
	}

	public void setReportVariables(ReportVariables reportVariables) {
		this.reportVariables = reportVariables;
	}

	public DecimalFormat getFormat() {
		return format;
	}

	public void setFormat(DecimalFormat format) {
		this.format = format;
	}

	@Override
	public int getRowCount() {
		if (variableBindings == null) {
			return 0;
		} else {
			return this.variableBindings.size();
		}
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == 0) {
			return "variable";
		} else {
			return "value";
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		LinkedList<Object> variables = new LinkedList<Object>();
		variables.addAll(variableBindings.keySet());
		Object variable = variables.get(rowIndex);
		if (columnIndex == 0) {
			return variable.toString();
		} else {
			return format.format(variableBindings.get(variable).doubleValue());
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}
	
	public void notifyTableChanged() {
		for(TableModelListener l : listeners) {
			l.tableChanged(new TableModelEvent(this));
		}
		
	}

	public String getName() {
		return getClass().getName() + "." + reportVariables.getName();
	}

	@Override
	public JComponent getComponent() {
		return table;
	}
}
