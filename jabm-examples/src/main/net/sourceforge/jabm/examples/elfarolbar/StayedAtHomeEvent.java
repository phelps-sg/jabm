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
package net.sourceforge.jabm.examples.elfarolbar;

import net.sourceforge.jabm.event.SimEvent;

/**
 * An event that is fired whenever an agent decides to stay at home
 * instead of attending the bar.
 * 
 * @author Steve Phelps
 *
 */
public class StayedAtHomeEvent extends SimEvent {

	protected PatronAgent agent;

	public StayedAtHomeEvent(PatronAgent agent) {
		super();
		this.agent = agent;
	}
	
}
