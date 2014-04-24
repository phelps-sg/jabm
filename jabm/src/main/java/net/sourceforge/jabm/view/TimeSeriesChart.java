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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import net.sourceforge.jabm.event.ReportVariablesChangedEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.report.ReportWithGUI;
import net.sourceforge.jabm.report.Timeseries;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYDataset;
import org.springframework.beans.factory.InitializingBean;

/**
 * A graphical report that renders an underlying time series ({@link Timeseries}
 * ) as a JFreeChart time series chart.  This report listens for events from
 * the underlying Timeseries object and notifies its chart whenever the series
 * is updated.
 * 
 * @author Steve Phelps
 */
public class TimeSeriesChart implements XYDataset, ReportWithGUI, Serializable,
		InitializingBean {

	/**
	 * The underlying time series which is the model for our chart.
	 */
	protected Timeseries series;

	protected Map<Object, Number> variableBindings = new LinkedHashMap<Object, Number>();

	protected LinkedList<Object> variableNames = new LinkedList<Object>();

	protected LinkedList<DatasetChangeListener> listeners = new LinkedList<DatasetChangeListener>();

	protected String chartTitle;

	protected String rangeAxisLabel = "";

	/**
	 * The JFreeChart ChartPanel which contains the actual swing component
	 * for the chart.
	 */
	protected ChartPanel chartPanel;

	static Logger logger = Logger.getLogger(TimeSeriesChart.class);

	
	public TimeSeriesChart() throws HeadlessException {
		super();
	}

	@Override
	public void afterPropertiesSet() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initialiseGUI();
			}
		});
	}

	public void initialiseGUI() {
		String name = chartTitle;
		if (name == null) {
			name = series.getName();
		}
		computeVariableNames();
		// series.initialise(null);
		JFreeChart chart = ChartFactory.createTimeSeriesChart(name, // chart
																	// title
				"t", // domain axis label
				rangeAxisLabel, // range axis label
				this, // data
				true, // include legend
				true, // tooltips?
				false // URLs?
				);
		chartPanel = new ChartPanel(chart, true);
		chartPanel.setPreferredSize(new Dimension(500, 270));
		computeVariableNames();
		series.addListener(this);
	}

	public Map<Object, Number> getVariableBindings() {
		return series.getVariableBindings();
	}

	public void computeVariableNames() {
		this.variableNames = new LinkedList<Object>(series.getyVariableNames());
	}

	/**
	 * When this report is computed it notifies its listeners (typically
	 * the JFreeChart swing component) that its data set has changed.
	 */
	public void compute(ReportVariablesChangedEvent event) {
		final Dataset eventOriginator = this;
		for (DatasetChangeListener listener : listeners) {
			listener.datasetChanged(new DatasetChangeEvent(eventOriginator,
					eventOriginator));
		}
	}

	public void dispose(SimEvent event) {
		series.dispose(event);
	}

	public void initialise(SimEvent event) {
		series.initialise(event);
	}

	public int getSeriesCount() {
		int n = series.getNumberOfSeries();
		return n;
	}

	@SuppressWarnings("rawtypes")
	public Comparable getSeriesKey(int seriesIndex) {
		// if (variableNames.size() == 0) {
		// //TODO
		// return "";
		// }
		String result = this.variableNames.get(seriesIndex).toString();
		return result;
	}

	@SuppressWarnings("rawtypes")
	public int indexOf(Comparable seriesKey) {
		int result = variableNames.indexOf(seriesKey);
		return result;
	}

	public DatasetGroup getGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addChangeListener(DatasetChangeListener arg0) {
		listeners.add(arg0);
	}

	public void removeChangeListener(DatasetChangeListener arg0) {
		listeners.remove(arg0);
	}

	public void setGroup(DatasetGroup arg0) {
	}

	public DomainOrder getDomainOrder() {
		return null;
	}

	public int getItemCount(int seriesIndex) {
		int result = series.size(seriesIndex);
		return result;
	}

	public Number getX(int seriesIndex, int itemIndex) {
		return series.getX(seriesIndex, itemIndex);
	}

	public double getXValue(int seriesIndex, int itemIndex) {
		double result = getX(seriesIndex, itemIndex).doubleValue();
		return result;
	}

	public Number getY(int seriesIndex, int itemIndex) {
		return series.getY(seriesIndex, itemIndex);
	}

	public double getYValue(int seriesIndex, int itemIndex) {
		return getY(seriesIndex, itemIndex).doubleValue();
	}

	public void eventOccurred(SimEvent event) {
		// series.eventOccurred(event);
		if (event instanceof ReportVariablesChangedEvent) {
			compute((ReportVariablesChangedEvent) event);
		}
	}

	public Timeseries getSeries() {
		return series;
	}

	public void setSeries(Timeseries series) {
		this.series = series;
	}

	public String getChartTitle() {
		return chartTitle;
	}

	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}

	public String getRangeAxisLabel() {
		return rangeAxisLabel;
	}

	public void setRangeAxisLabel(String rangeAxisLabel) {
		this.rangeAxisLabel = rangeAxisLabel;
	}

	@Override
	public String getName() {
		return chartTitle;
	}

	@Override
	public JComponent getComponent() {
		return chartPanel;
	}

}
