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

import java.io.Serializable;

/**
 * <p>
 * This is an extension of Java's Number class that provides methods for
 * performing untyped polymorphic arithmetic. For example, an UntypedDouble can
 * be added to an UntypedInteger without having to know the class of each
 * operand.
 * </p>
 * 
 * @author Steve Phelps
 * @version $Revision: 104 $
 * 
 */

@SuppressWarnings("rawtypes")
public abstract class UntypedNumber extends Number implements Comparable,
		Serializable {

	public abstract UntypedNumber multiply(UntypedNumber other);

	public abstract UntypedNumber add(UntypedNumber other);

	public abstract UntypedNumber subtract(UntypedNumber other);

	public abstract UntypedNumber divide(UntypedNumber other);

}