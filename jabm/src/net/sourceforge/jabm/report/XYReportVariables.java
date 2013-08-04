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

import java.util.List;

/**
 * A set of ReportVariables which have one independent variable and several
 * dependent variables. The independent variable is typically a time-stamp value
 * and these variables are typically reported in time series form.
 * 
 * @author sphelps
 */
public interface XYReportVariables extends ReportVariables {

	//TODO seriesIndex redundant here.
	public Number getX(int seriesIndex);

	/**
	 * @param seriesIndex The index of the dependent variable.
	 * @return The value of the dependent variable in the given series.
	 */
	public Number getY(int seriesIndex);
	
	/**
	 * @return The number of independent variables.
	 */
	public int getNumberOfSeries();

	/**
	 * @return The names of the dependent variables.
	 */
	public List<Object> getyVariableNames();

	/**
	 * @return The name of the independent variable.
	 */
	public String getxVariableName();
	
}
