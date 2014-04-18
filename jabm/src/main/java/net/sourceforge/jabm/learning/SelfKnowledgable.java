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

package net.sourceforge.jabm.learning;

/**
 * Classes implementing this interface indicate that they know if their output
 * is good enough to be used.
 * 
 * @author Jinzhong Niu
 * @version $Revision: 16 $
 */

public interface SelfKnowledgable {

	public boolean goodEnough();

}