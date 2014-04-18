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

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import net.sourceforge.jabm.event.Model;
import net.sourceforge.jabm.event.ReportVariablesChangedEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.report.ReportVariables;
import net.sourceforge.jabm.report.ReportWithGUI;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.springframework.beans.factory.InitializingBean;

@SuppressWarnings("rawtypes")
public class BarChart  
		implements CategoryDataset, ReportWithGUI, Serializable, InitializingBean {

	protected ReportVariables reportVariables;
	
	protected Map<Object, Number> variableBindings
		= new LinkedHashMap<Object, Number>();
	
	protected LinkedList<Object> variableNames
		= new LinkedList<Object>();
	
	protected LinkedList<DatasetChangeListener> listeners
		= new LinkedList<DatasetChangeListener>();
	
	protected String chartTitle;
	
	protected String valueAxisLabel;

	protected ChartPanel chartPanel;
	
	static Logger logger = Logger.getLogger(BarChart.class);
	
	public BarChart()
			throws HeadlessException {
		super();
	}
	
	public void afterPropertiesSet() {
		String name = chartTitle;
		if (name == null) {
			name = reportVariables.getName();
		}
//		series.initialise(null);
		JFreeChart chart = ChartFactory.createBarChart(//title, categoryAxisLabel, valueAxisLabel, dataset, orientation, legend, tooltips, urls)(
	            name,       // chart title
	            "t",               // domain axis label
	            valueAxisLabel,                  // range axis label
	            this,                  // data
	            PlotOrientation.VERTICAL,
	            true,                     // include legend
	            true,                     // tooltips?
	            false                     // URLs?
	        );
		chartPanel = new ChartPanel(chart, false);
        chartPanel.setPreferredSize(new Dimension(500, 270));
//        getContentPane().add(chartPanel);
//        pack();
//        computeVariableNames();
//        setTitle("JABM: " + name);
//        setVisible(true);
        ((Model) reportVariables).addListener(this);
	}

	public Map<Object, Number> getVariableBindings() {
		return reportVariables.getVariableBindings();
	}
	
	public void computeVariableNames() {
		this.variableBindings = reportVariables.getVariableBindings();
		this.variableBindings.remove(reportVariables.getName() + ".t");
		this.variableNames = new LinkedList<Object>();
		this.variableNames.addAll(variableBindings.keySet());
	}

	public void compute(ReportVariablesChangedEvent event) {
		computeVariableNames();
		final Dataset eventOriginator = this;
		try {
			SwingUtilities.invokeAndWait( new Runnable() {
				public void run() {
					for (DatasetChangeListener listener : listeners) {
						listener.datasetChanged(new DatasetChangeEvent(
								eventOriginator, eventOriginator));
					}
				}
			});
		} catch (InterruptedException e) {
			logger.warn(e);
			Thread.currentThread().interrupt();
		} catch (InvocationTargetException e) {
			logger.warn(e);
		}
	}

	public void dispose(SimEvent event) {
		reportVariables.dispose(event);
	}

	public void initialise(SimEvent event) {
		reportVariables.initialise(event);
	}


	public void addChangeListener(DatasetChangeListener arg0) {
		listeners.add(arg0);
	}
	
	public void removeChangeListener(DatasetChangeListener arg0) {
		listeners.remove(arg0);
	}



	public void eventOccurred(SimEvent event) {
//		series.eventOccurred(event);
		if (event instanceof ReportVariablesChangedEvent) {
			compute((ReportVariablesChangedEvent) event);
		}
	}

	public String getChartTitle() {
		return chartTitle;
	}

	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}

	public String getValueAxisLabel() {
		return valueAxisLabel;
	}

	public void setValueAxisLabel(String valueAxisLabel) {
		this.valueAxisLabel = valueAxisLabel;
	}

	@Override
	public Comparable getRowKey(int row) {
		Comparable result = variableNames.get(row).toString();
		return result;
	}

	@Override
	public int getRowIndex(Comparable key) {
		return variableNames.indexOf(key);
	}

	@Override
	public List getRowKeys() {
		return variableNames;
	}

	@Override
	public Comparable getColumnKey(int column) {
		return "1";
	}

	@Override
	public int getColumnIndex(Comparable key) {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List getColumnKeys() {
		List result = new LinkedList();
		result.add("1");
		return result;
	}

	@Override
	public Number getValue(Comparable rowKey, Comparable columnKey) {
		Number result = variableBindings.get(rowKey);
		return result;
	}

	@Override
	public int getRowCount() {
		int result = variableBindings.size();
		return result;
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public Number getValue(int row, int column) {
		if (variableBindings == null) {
			return null;
		}
		String variableName = variableNames.get(row).toString();
		return getValue(variableName, null);
	}

	@Override
	public DatasetGroup getGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGroup(DatasetGroup group) {
		// TODO Auto-generated method stub
		
	}

	public ReportVariables getReportVariables() {
		return reportVariables;
	}

	public void setReportVariables(ReportVariables reportVariables) {
		this.reportVariables = reportVariables;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JComponent getComponent() {
		return chartPanel;
	}
	
	
}
