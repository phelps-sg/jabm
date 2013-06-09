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
package net.sourceforge.jabm;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.jabm.util.UntypedDouble;
import net.sourceforge.jabm.util.UntypedLong;
import net.sourceforge.jabm.util.UntypedNumber;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

class VariableBindingsIterator implements Iterator<Map<String,String>> {

	protected Map<String, VariableRange> variableRanges;
	
	protected ArrayList<String> variables;
	
	protected Bindings bindings;
	
	protected int currentVariableIndex;
	
	protected BeanFactory beanFactory;

	protected boolean hasNext;
	
	public static final String RANGE_REGEXP =
		"([0-9\\.]+)\\:([0-9\\.]+)\\:([0-9\\.]+)";
	
	static Pattern rangePattern = Pattern.compile(RANGE_REGEXP);
	
	static Logger logger = Logger.getLogger(VariableBindingsIterator.class);
	
	
	public VariableBindingsIterator(String varFile, 
										BeanFactory beanFactory) {
		try {
			this.beanFactory = beanFactory;
			parseVarFile(varFile);
			bindings = initialBindings();
			currentVariableIndex = variables.size() - 1;
			hasNext = variables.size() > 0;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Bindings initialBindings() {
		Bindings result = new Bindings();
		for(String variable : variables) {
			UntypedNumber initialValue = 
				variableRanges.get(variable).getMin();
			result.set(variable, initialValue);
		}
		return result;
	}
		
	@SuppressWarnings("rawtypes")
	public void parseVarFile(String varFile) throws IOException {
		Properties properties = new Properties();
		variableRanges = new HashMap<String, VariableRange>();
		properties.load(new FileInputStream(varFile));
		for(Object variable : properties.keySet()) {
			Class type = getType(variable.toString());
			VariableRange range = 
				parseVariableRange(properties.get(variable).toString(), type);
			logger.debug("range = " + range);
			variableRanges.put(variable.toString(), range);
		}
		variables = new ArrayList<String>(variableRanges.keySet());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Class getType(String beanProperty) {
		int dot = beanProperty.indexOf('.');
		String beanName = beanProperty.substring(0, dot);
		String attributeName = beanProperty.substring(dot+1);
		Class beanType = beanFactory.getType(beanName);
		String getterName = "get" + 
			Character.toUpperCase(attributeName.charAt(0)) + 
				attributeName.substring(1);
		try {
			Method method = beanType.getMethod(getterName, new Class[] {});
			Type returnType = method.getGenericReturnType();
			if (returnType instanceof Class) {
				Class result = (Class) returnType;
				return result;
			} else {
				throw new IllegalArgumentException(beanProperty);
			}
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public VariableRange parseVariableRange(String rangeSpecification,
											 Class type) {
		Matcher matcher = rangePattern.matcher(rangeSpecification);
		if (matcher.matches()) {
			VariableRange result = new VariableRange();
			String minStr = matcher.group(1);
			String incrStr = matcher.group(2);
			String maxStr = matcher.group(3);
			if (type.isAssignableFrom(int.class) || type.isAssignableFrom(long.class)) {
				result.setMin(new UntypedLong(Long.parseLong(minStr)));
				result.setIncrement(new UntypedLong(Long.parseLong(incrStr)));
				result.setMax(new UntypedLong(Long.parseLong(maxStr)));
			} else if (type.isAssignableFrom(double.class) || type.isAssignableFrom(float.class)) {
				result.setMin(new UntypedDouble(Double.parseDouble(minStr)));
				result.setIncrement(new UntypedDouble(Double.parseDouble(incrStr)));
				result.setMax(new UntypedDouble(Double.parseDouble(maxStr)));
			} else {
				throw new IllegalArgumentException(type.toString());
			}
			return result;
		}
		throw new IllegalArgumentException(
				"Could not parse range specification: " + 
					rangeSpecification);
	}
	
	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public Map<String, String> next() {

		Map<String, String> result = bindings.getStringBindings();

		logger.debug(variableName(currentVariableIndex) + " = "
				+ bindings.get(currentVariableIndex));

		if (lessThanMaximumValue(currentVariableIndex)) {
			increment(currentVariableIndex);
		} else {
			// Back
			while (currentVariableIndex >= 0
					&& !lessThanMaximumValue(currentVariableIndex)) {
				currentVariableIndex--;
			}
			if (currentVariableIndex < 0) {
				hasNext = false;
				return result;
			}
			increment(currentVariableIndex);
			for (int i = currentVariableIndex + 1; i < variables.size(); i++) {
				bindings.set(variableName(i), getMin(i));
			}
			currentVariableIndex = variables.size() - 1;
		}

		return result;
	}

	@Override
	public void remove() {
		throw new RuntimeException("Unsupported method");
	}
	
	public String variableName(int variableIndex) {
		return variables.get(variableIndex);
	}
	
	public void increment(int variableIndex) {
		bindings.increment(variableName(variableIndex));
	}
	
	public boolean lessThanMaximumValue(int variableIndex) {
		String variableName = variableName(variableIndex);
		double value = bindings.get(variableName).doubleValue();
		double max = variableRanges.get(variableName).getMax().doubleValue();
		return value < (max - 10E-9);
	}
	
	public UntypedNumber getMin(int variableIndex) {
		String variableName = variableName(variableIndex);
		return variableRanges.get(variableName).getMin();
	}

	class VariableRange {
		
		public UntypedNumber min;
		
		public UntypedNumber max;
		
		public UntypedNumber increment;

		public VariableRange(UntypedNumber min, UntypedNumber max, 
								UntypedNumber increment) {
			super();
			this.min = min;
			this.max = max;
			this.increment = increment;
		}
		
		public VariableRange() {
			super();
		}

		public UntypedNumber getMin() {
			return min;
		}

		public void setMin(UntypedNumber min) {
			this.min = min;
		}

		public UntypedNumber getMax() {
			return max;
		}

		public void setMax(UntypedNumber max) {
			this.max = max;
		}

		public UntypedNumber getIncrement() {
			return increment;
		}

		public void setIncrement(UntypedNumber increment) {
			this.increment = increment;
		}
		
		public String toString() {
			return min + ":" + increment + ":" + max;
		}
	}

	class Bindings {

		Map<String, UntypedNumber> variableMap;
		
		public Bindings() {
			variableMap = new HashMap<String, UntypedNumber>();
		}
		
		public void set(String variable, UntypedNumber value) {
			variableMap.put(variable, value);
		}
		
		public void increment(String variable) {
			UntypedNumber currentValue = variableMap.get(variable);
			UntypedNumber newValue = 
				currentValue.add(variableRanges.get(variable).getIncrement());
			set(variable, newValue);
		}
		
		public UntypedNumber getNextValue(String variable) {
			UntypedNumber currentValue = variableMap.get(variable);
			UntypedNumber newValue = 
				currentValue.add(variableRanges.get(variable).getIncrement());
			return newValue;
		}
		
		public UntypedNumber get(String variable) {
			return variableMap.get(variable);
		}
		
		public UntypedNumber get(int variableIndex) {
			String variableName = variables.get(variableIndex);
			return get(variableName);
		}
		
		public Set<String> getVariables() {
			return variableMap.keySet();
		}
		
		public Map<String, String> getStringBindings() {
			HashMap<String, String> result = new HashMap<String, String>();
			for(String variable : variableMap.keySet()) {
				result.put(variable, variableMap.get(variable).toString());
			}
			return result;
		}
	}
	
}