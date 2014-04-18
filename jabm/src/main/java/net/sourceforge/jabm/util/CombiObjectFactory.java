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
package net.sourceforge.jabm.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Required;

@SuppressWarnings("rawtypes")
public class CombiObjectFactory implements ObjectFactory {

	protected ArrayList<Integer> numberOfObjectsPerFactory;
	
	protected ArrayList<ObjectFactory> factories;
	
	protected int currentFactory = 0;
	
	protected int count = 0;
	
	protected int overFlowFactory = -1;
	
	protected boolean overflowed = false;
	
	public CombiObjectFactory(ArrayList<ObjectFactory> factories,
			ArrayList<Integer> numberOfObjectsPerFactory) {
		super();
		this.numberOfObjectsPerFactory = numberOfObjectsPerFactory;
		this.factories = factories;
	}
	
	public CombiObjectFactory() {
	}

	@Override
	public Object getObject() throws BeansException {
		while (count >= numberOfObjectsPerFactory.get(currentFactory) && !overflowed) {
			nextFactory();
		}
		Object result = factories.get(currentFactory).getObject();
		count++;
		return result;
	}
	
	public void nextFactory() {
		currentFactory++;
		if (currentFactory > factories.size()) {
			currentFactory = overFlowFactory;
		}
		count = 0;
	}

	public int getOverFlowFactory() {
		return overFlowFactory;
	}

	public void setOverFlowFactory(int overFlowFactory) {
		this.overFlowFactory = overFlowFactory;
	}

	public ArrayList<Integer> getNumberOfObjectsPerFactory() {
		return numberOfObjectsPerFactory;
	}

	public void setNumberOfObjectsPerFactory(
			ArrayList<Integer> numberOfObjectsPerFactory) {
		this.numberOfObjectsPerFactory = numberOfObjectsPerFactory;
	}

	public ArrayList<ObjectFactory> getFactories() {
		return factories;
	}

	public void setFactories(ArrayList<ObjectFactory> factories) {
		this.factories = factories;
	}

	@Required
	public void setFactoryMap(Map<ObjectFactory, Integer> factoryMap) {
		int n = factoryMap.keySet().size();
		factories = new ArrayList<ObjectFactory>(n);
		numberOfObjectsPerFactory = new ArrayList<Integer>(n);
		for(ObjectFactory factory : factoryMap.keySet()) {
			factories.add(factory);
			numberOfObjectsPerFactory.add(factoryMap.get(factory));
		}
	}
	
	public Map<ObjectFactory, Integer> getFactoryMap() {
		LinkedHashMap<ObjectFactory, Integer> result
			= new LinkedHashMap<ObjectFactory, Integer>();
		for(int i=0; i<factories.size(); i++) {
			result.put(factories.get(i), numberOfObjectsPerFactory.get(i));
		}
		return result;
	}
	
}
