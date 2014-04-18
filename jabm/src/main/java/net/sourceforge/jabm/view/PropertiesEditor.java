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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * A GUI component to allow editing of Properties objects.
 * 
 * @author Steve Phelps
 */
public class PropertiesEditor extends JTable {

	protected Properties properties;
	
	public PropertiesEditor(Properties properties) {
		super();
		this.properties = properties;
		PropertiesModel model = new PropertiesModel();
		setModel(model);
	}
	
	public void propertiesChanged() {
		PropertiesModel model = (PropertiesModel) this.getModel();
		model.initialise();
		model.fireTableDataChanged();
	}
	

	/**
	 * Load new properties from the specified input stream and update
	 * the view.
	 * 
	 * @param fileInputStream
	 * @throws IOException
	 */
	public void load(FileInputStream fileInputStream) throws IOException {
		properties.clear();
		properties
				.load(fileInputStream);
		propertiesChanged();
	}
	
	
	/**
	 * The underlying model for the properties model.
	 *  
	 * @author Steve Phelps
	 */
	class PropertiesModel extends AbstractTableModel implements TableModel {

		List<String> propertyNames = new ArrayList<String>();
		
		List<String> values = new ArrayList<String>();
		
		public PropertiesModel() {
			super();
			initialise();
		}
		
		public void initialise() {
			int size = properties.keySet().size();
			this.propertyNames = new ArrayList<String>(size);
			this.values = new ArrayList<String>(size);
			for(Object propertyName : properties.keySet()) {
				propertyNames.add(propertyName.toString());
			}
			Collections.sort(propertyNames);
			for(String propertyName : propertyNames) {
				values.add(properties.get(propertyName).toString());
			}
		}

		@Override
		public int getRowCount() {
			return properties.size();
		}

		@Override
		public int getColumnCount() {
			return 2;
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			switch (columnIndex) {
			case 0:
				return "Name";
			case 1:
				return "Value";
			}
			return null;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return !isPropertyName(columnIndex);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (isPropertyName(columnIndex)) {
				return propertyNames.get(rowIndex);
			} else {
				return values.get(rowIndex);
			}
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			super.setValueAt(aValue, rowIndex, columnIndex);
			if (!isPropertyName(columnIndex)) {
				values.set(rowIndex, aValue.toString());
				properties.setProperty(propertyNames.get(rowIndex),
						aValue.toString());
			}
			fireTableCellUpdated(rowIndex, columnIndex);
		}
		
		public boolean isPropertyName(int columnIndex) {
			return columnIndex == 0;
		}
		
	}

}
