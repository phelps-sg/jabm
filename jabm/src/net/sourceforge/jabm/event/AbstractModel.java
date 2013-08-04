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
package net.sourceforge.jabm.event;

import java.io.Serializable;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

public abstract class AbstractModel implements Cloneable, Serializable, Model {

	/**
	 * EventListeners that subscribe to all classes of events.
	 */
	protected ConcurrentLinkedQueue<EventListener> genericListeners;

	/**
	 * A map of EventListeners that subscribe to a specific class of events,
	 *  keyed on the class of the event. 
	 */
	@SuppressWarnings("rawtypes")
	protected ConcurrentHashMap<Class, ConcurrentLinkedQueue<EventListener>> specificListeners;
	
	protected Stack<EventListener> deleteQueue;
	
	static Logger logger = Logger.getLogger(AbstractModel.class);

	@SuppressWarnings("rawtypes")
	public AbstractModel() {
		genericListeners = new ConcurrentLinkedQueue<EventListener>();
		specificListeners = new ConcurrentHashMap<Class, ConcurrentLinkedQueue<EventListener>>();
		deleteQueue = new Stack<EventListener>();
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.jabm.event.Model#removeListener(net.sourceforge.jabm.event.EventListener)
	 */
	@Override
	public void removeListener(EventListener listener) {
		deleteQueue.add(listener);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jabm.event.Model#addListener(net.sourceforge.jabm.event.EventListener)
	 */
	@Override
	public void addListener(EventListener listener) {
		if (!genericListeners.contains(listener)) {
			genericListeners.add(listener);
		}
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jabm.event.Model#addListener(java.lang.Class, net.sourceforge.jabm.event.EventListener)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void addListener(Class eventClass, EventListener listener) {
		ConcurrentLinkedQueue<EventListener> listeners = specificListeners.get(eventClass);
		if (listeners == null) {
			listeners = new ConcurrentLinkedQueue<EventListener>();
			specificListeners.put(eventClass, listeners);
		}
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void fireEvent(SimEvent event) {
		processDeleteQueue();
		notifyGenericListeners(event);
		processDeleteQueue();
		notifySpecificListeners(event);
		processDeleteQueue();
	}
	
	@SuppressWarnings("rawtypes")
	protected void processDeleteQueue() {
		while (!deleteQueue.isEmpty()) {
			EventListener listener = deleteQueue.pop();
			genericListeners.remove(listener);
			for(Class key : specificListeners.keySet()) {
				ConcurrentLinkedQueue<EventListener> listeners = specificListeners.get(key);
				@SuppressWarnings("unused")
				boolean removed = listeners.remove(listener);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void notifySpecificListeners(SimEvent event) {
		for (Class eventClass : specificListeners.keySet()) {
			if (eventClass.isInstance(event)) {
				ConcurrentLinkedQueue<EventListener> listeners = specificListeners
						.get(eventClass);
				for (EventListener listener : listeners) {
					if (!deleteQueue.contains(listener)) {
						listener.eventOccurred(event);
					}
				}
			}
		}
	}
	
	public void notifyGenericListeners(SimEvent event) {
		for (EventListener listener : genericListeners) {
			if (!deleteQueue.contains(listener)) {
				listener.eventOccurred(event);
			}
		}
	}
	
	public void clearListeners() {
		specificListeners.clear();
		genericListeners.clear();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
