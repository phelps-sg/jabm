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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sourceforge.jabm.event.AbstractModel;
import net.sourceforge.jabm.event.ReportVariablesChangedEvent;
import net.sourceforge.jabm.event.SimEvent;

import org.springframework.beans.factory.InitializingBean;

public class CombiSeriesReportVariables extends AbstractModel implements
		Timeseries, InitializingBean {

	protected List<Timeseries> seriesList;
	
	protected String name;
	
	@Override
	public void compute(SimEvent event) {
		for(Timeseries series: seriesList) {
			series.compute(event);
		}
	}

	@Override
	public void dispose(SimEvent event) {
		for(Timeseries series : seriesList) {
			series.dispose(event);
		}
	}

	@Override
	public void initialise(SimEvent event) {
		for(Timeseries series : seriesList) {
			series.initialise(event);
		}
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Map<Object, Number> getVariableBindings() {
		LinkedHashMap<Object, Number> result = new LinkedHashMap<Object, Number>();
		for(Timeseries series : seriesList) {
			result.putAll(series.getVariableBindings());
		}
		return result;
 	}

	@Override
	public void eventOccurred(SimEvent event) {
		if (event instanceof ReportVariablesChangedEvent) {
			fireEvent(event);
		} else {
			for(Timeseries series : seriesList) {
				series.eventOccurred(event);
			}
		}
	}

	@Override
	public Number getX(int seriesIndex, int itemIndex) {
		return seriesList.get(seriesIndex).getX(0, itemIndex);
	}

	@Override
	public Number getY(int seriesIndex, int itemIndex) {
		return seriesList.get(seriesIndex).getY(0, itemIndex);
	}

	@Override
	public int size(int seriesIndex) {
//		Timeseries series = seriesList.get(seriesIndex);
//		int n = series.getNumberOfSeries();
//		int result = 0;
//		for(int i=0; i<n; i++) {
//			result += series.size(i);
//		}
//		return result;
		return seriesList.get(seriesIndex).size(0);
	}

	@Override
	public int getNumberOfSeries() {
		return seriesList.size();
	}

	public List<Timeseries> getSeriesList() {
		return seriesList;
	}

	public void setSeriesList(List<Timeseries> seriesList) {
		this.seriesList = seriesList;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for(Timeseries series : seriesList) {
			series.addListener(this);
		}
	}

	@Override
	public List<Object> getyVariableNames() {
		LinkedList<Object> result = new LinkedList<Object>();
		for(Timeseries series : seriesList) {
			result.addAll(series.getyVariableNames());
		}
		return result;
	}
		
	
	
	
}
