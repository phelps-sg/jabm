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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.stat.Frequency;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.AbstractDataset;

@SuppressWarnings("rawtypes")
public class CategoryDatasetFrequencyAdaptor extends AbstractDataset implements
		CategoryDataset, Serializable {

	protected Frequency frequency;
	
	public static final String ROW = "frequency";
	
	protected HashMap<String,Integer> columnMap;
	
	protected HashMap<String,Comparable<?>> objectMap;
	
	protected ArrayList<String> columns;
	
	public CategoryDatasetFrequencyAdaptor(Frequency frequency) {
		super();
		this.frequency = frequency;
		updateCategories();
	}
	
	public void updateCategories() {
		columnMap = new HashMap<String,Integer>();
		objectMap = new HashMap<String,Comparable<?>>();
		columns = new ArrayList<String>();
		Iterator<Comparable<?>> it = frequency.valuesIterator();
		int i = 0;
		while (it.hasNext()) {
			Comparable<?> value = it.next();
			objectMap.put(value.toString(), value);
			columns.add(i, value.toString());
			columnMap.put(value.toString(), i);
			i++;
		}
	}
	
	public void clear() {
		frequency.clear();
		columnMap.clear();
		objectMap.clear();
		columns.clear();
	}
	
	public void fireDatasetChanged() {
		super.fireDatasetChanged();
	}

	public int getColumnIndex(Comparable key) {
		return columnMap.get(key);
	}

	public Comparable getColumnKey(int column) {
		return columns.get(column);
	}

	public List getColumnKeys() {
		return columns;
	}

	public int getRowIndex(Comparable key) {
		return 0;
	}

	public Comparable getRowKey(int row) {
		return ROW;
	}

	@SuppressWarnings("unchecked")
	public List getRowKeys() {
		ArrayList result = new ArrayList();
		result.add(ROW);
		return result;
	}

	public Number getValue(Comparable rowKey, Comparable columnKey) {
		Integer column = (Integer) columnKey;
		String columnName = columns.get(column);
		return frequency.getCount(objectMap.get(columnName));
	}

	public int getColumnCount() {
		return columns.size();
	}

	public int getRowCount() {
		return 1;
	}

	public Number getValue(int row, int column) {
		return getValue(null, column);
	}

	
}
