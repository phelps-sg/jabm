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

import org.springframework.beans.factory.FactoryBean;

/**
 * <p>
 * A <a href="http://www.springsource.com/developer/spring">Spring</a> factory
 * bean which generates random Integer values from the specified probability
 * distribution. This can be used to auto-magically configure properties as
 * random variates in a Monte-Carlo simulation.
 * </p>
 * 
 * @author Steve Phelps
 * 
 */
public class RandomIntegerFactoryBean extends AbstractRandomVariateFactoryBean implements
		FactoryBean<Integer> {

	@Override
	public Integer getObject() {
		return new Integer((int) Math.round(distribution.nextDouble()));
	}

	@Override
	public Class<?> getObjectType() {
		return Integer.class;
	}

}
