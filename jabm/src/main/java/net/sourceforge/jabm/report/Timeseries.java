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

import net.sourceforge.jabm.event.Model;

/**
 * A set of report variables representing a time series.
 * 
 * @author Steve Phelps
 */
public interface Timeseries extends ReportVariables, Model {

	public Number getX(int seriesIndex, int itemIndex);

	public Number getY(int seriesIndex, int itemIndex);

	public int size(int seriesIndex);

	public int getNumberOfSeries();

	List<Object> getyVariableNames();

}