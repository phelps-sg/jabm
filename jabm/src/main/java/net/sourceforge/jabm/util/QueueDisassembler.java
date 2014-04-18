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

import java.util.Iterator;

/**
 * An iterator that destructively iterates over a PriorityQueue, that is each
 * item that is returned is removed from the top of the heap.
 * 
 * @author Steve Phelps
 * @version $Revision: 104 $
 */

@SuppressWarnings("rawtypes")
public class QueueDisassembler implements Iterator {

	private PriorityQueue queue;

	public QueueDisassembler(PriorityQueue queue) {
		this.queue = queue;
	}

	public boolean hasNext() {
		return !queue.isEmpty();
	}

	public Object next() {
		return queue.removeFirst();
	}

	public void remove() {
	}

}