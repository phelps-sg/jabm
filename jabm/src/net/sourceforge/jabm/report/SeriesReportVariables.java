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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.jabm.event.AbstractModel;
import net.sourceforge.jabm.event.ReportVariablesChangedEvent;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.view.TimeSeriesChart;

import org.springframework.beans.factory.InitializingBean;

/**
 * A report which incrementally records values from an underlying
 * {@link ReportVariables} object to an array each time it is computed. This can
 * be used as the model for a, e.g. {@link TimeSeriesChart}.
 * 
 * @author Steve Phelps
 * 
 */
public class SeriesReportVariables extends AbstractModel 
		implements Serializable, InitializingBean, Timeseries {

	protected ArrayList<ArrayList<Number>> xData;
	
	protected ArrayList<ArrayList<Number>> yData;
	
	protected XYReportVariables reportVariables;
	
	protected int n;
	
	@Override
	public Map<Object, Number> getVariableBindings() {
		return reportVariables.getVariableBindings();
	}
	
	@Override
	public List<Object> getyVariableNames() {
		return reportVariables.getyVariableNames();
	}

	public String getxVariableName() {
		return reportVariables.getxVariableName();
	}

	@Override
	public void eventOccurred(SimEvent event) {
	}

	@Override
	public void compute(SimEvent event) {
//		this.reportVariables.compute(event); 
		this.n = reportVariables.getNumberOfSeries();
		for(int i=0; i<n; i++) {
			xData.get(i).add(reportVariables.getX(i));
			yData.get(i).add(reportVariables.getY(i));
		}
		fireEvent(new ReportVariablesChangedEvent(this));
	}

	@Override
	public void dispose(SimEvent event) {
//		this.reportVariables.dispose(event);
	}

	@Override
	public void initialise(SimEvent event) {
//		this.reportVariables.initialise(event);
		initialiseSeries();
	}

	public void initialiseSeries() {
		this.n = reportVariables.getNumberOfSeries();
		this.xData = new ArrayList<ArrayList<Number>>(n);
		this.yData = new ArrayList<ArrayList<Number>>(n);
		for(int i=0; i<n; i++) {
			xData.add(new ArrayList<Number>());
			yData.add(new ArrayList<Number>());
		}
		fireEvent(new ReportVariablesChangedEvent(this));
	}

	@Override
	public String getName() {
		return reportVariables.getName();
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jabm.report.Timeseries#getX(int, int)
	 */
	@Override
	public Number getX(int seriesIndex, int itemIndex) {
		return xData.get(seriesIndex).get(itemIndex);
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jabm.report.Timeseries#getY(int, int)
	 */
	@Override
	public Number getY(int seriesIndex, int itemIndex) {
		return yData.get(seriesIndex).get(itemIndex);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jabm.report.Timeseries#size()
	 */
	@Override
	public int size(int seriesIndex) {
		if (xData == null) {
			return 0;
		} else {
			return xData.get(seriesIndex).size();
		}
	}

	public XYReportVariables getReportVariables() {
		return reportVariables;
	}

	public void setReportVariables(XYReportVariables reportVariables) {
		this.reportVariables = reportVariables;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jabm.report.Timeseries#getNumberOfSeries()
	 */
	@Override
	public int getNumberOfSeries() {
		return n;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initialiseSeries();
	}
	
}
