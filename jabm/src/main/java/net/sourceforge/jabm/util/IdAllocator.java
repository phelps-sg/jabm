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
 * Utility class for handing out unique ids.
 * 
 * A using class wishing to assign unique ids to each of its instances should
 * declare a static member variable:
 * 
 * <code>
 *   static IdAllocator idAllocator = new IdAllocator();
 * </code>
 * 
 * In its constructor it should use something like:
 * 
 * <code>
 *   id = idAllocator.nextId();
 * </code>
 * 
 * @author Steve Phelps
 * @version $Revision: 16 $
 */

public class IdAllocator {

	protected long nextId = 0;

	public IdAllocator() {
	}

	public synchronized long nextId() {
		return nextId++;
	}

}