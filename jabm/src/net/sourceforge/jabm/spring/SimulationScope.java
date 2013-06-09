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
package net.sourceforge.jabm.spring;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

/**
 * <p>
 * A custom Spring bean scope used to manage objects which persist
 * throughout a single simulation, but are re-instantiated on each new 
 * independent simulation run.
 * </p>
 * @author Steve Phelps
 */
public class SimulationScope implements Scope {

	/**
	 * A mapping from bean names to objects that are bound in this scope.
	 */
	protected HashMap<String, Object> boundObjects;
	
	protected int simulationId = 1;
	
	protected static SimulationScope singletonInstance;

	static Logger logger = Logger.getLogger(SimulationScope.class);

	/**
	 * The string that appears in the scope attribute of a bean definition.
	 */
	public static final String ATTRIBUTE_VALUE = "simulation";
	
	
	public SimulationScope() {
		boundObjects = new HashMap<String, Object>();
	}
	
	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		if (logger.isDebugEnabled()) logger.debug("Looking up bean " + name);
		Object result = boundObjects.get(name);
		if (result == null) {
			result = objectFactory.getObject();
			boundObjects.put(name, result);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Returning " + result + " with hash "
					+ result.hashCode() + " for " + name);
		}
		return result;
	}

	@Override
	public Object remove(String name) {
		return boundObjects.remove(name);
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object resolveContextualObject(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getConversationId() {
		return simulationId + "";
	}

	/**
	 * Invoked to indicate that a new simulation has begun and that a fresh
	 * scope is required.
	 */
	public void startNewSimulation() {
		boundObjects.clear();
		simulationId++;
	}

	public static SimulationScope getSingletonInstance() {
		if (singletonInstance == null) {
			singletonInstance = new SimulationScope();
		} 
		return singletonInstance;
	}

}
