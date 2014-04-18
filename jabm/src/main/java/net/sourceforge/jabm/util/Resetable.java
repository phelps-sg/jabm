/*
 * JASA Java Auction Simulator API
 * Copyright (C) 2013 Steve Phelps
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package net.sourceforge.jabm.util;

/**
 * <p>
 * Classes implementing this interface indicate that their state is resetable.
 * This is often used in JASA to re-initialise objects instead of
 * re-constructing them, with the associated garbage-collection and
 * initialisation performance penalties.
 * </p>
 * 
 * @author Steve Phelps
 * @version $Revision: 16 $
 */

public interface Resetable {

	/**
	 * Reinitialise our state to the original settings.
	 */
	public void reset();

}