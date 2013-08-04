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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.math3.stat.Frequency;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.xy.XYDataset;

public class FrequencyByTimeDataset extends AbstractDataset implements XYDataset, Serializable {

	protected ArrayList<Frequency> frequencyByTime;
	
	protected int t = 0;
	
	protected HashMap<Integer,Comparable<?>> categories;
	
	protected int categoryCount = 0;
	
	public FrequencyByTimeDataset() {
		super();
		frequencyByTime = new ArrayList<Frequency>();
		categories = new HashMap<Integer,Comparable<?>>();
	}
	
	public void addNewSample(Frequency f) {
		frequencyByTime.add(f);
		updateCategories(f);
		t++;
		fireDatasetChanged();
	}
	
	public void updateCategories(Frequency f) {
		@SuppressWarnings("rawtypes")
		Iterator<Comparable<?>> it = f.valuesIterator();
		while (it.hasNext()) {
			Comparable<?> category = it.next();
			registerCategory(category);			
		}
	}
	
	public void registerCategory(Comparable<?> category) {
		if (!categories.containsValue(category)) {
			categories.put(categoryCount++, category);
		}	
	}
	
	public void fireDatasetChanged() {
		super.fireDatasetChanged();
	}

	public DomainOrder getDomainOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getItemCount(int series) {
		return t;
	}

	public Number getX(int series, int item) {
		return item;
	}

	public double getXValue(int series, int item) {
		return item;
	}

	public Number getY(int series, int item) {
		Comparable<?> category = categories.get(series);
		assert category != null;
		return frequencyByTime.get(item).getCount(category);
	}

	public double getYValue(int series, int item) {
		return getY(series, item).doubleValue();
	}

	public int getSeriesCount() {
		return categoryCount;
	}

	@SuppressWarnings("rawtypes")
	public Comparable getSeriesKey(int series) {
		Object category = categories.get(series);
		return category.toString();
	}

	@SuppressWarnings("rawtypes")
	public int indexOf(Comparable seriesKey) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int size() {
		return t;
	}
	
	public void initialiseCategories(Collection<Comparable<?>> initCategories) {
		for (Comparable<?> category : initCategories) {
			registerCategory(category);
		}
	}
	
}
