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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>
 * A <a href="http://www.springsource.com/developer/spring">Spring</a>
 * factory bean which returns a seed based on the current time and the IP 
 * address of the host.  
 * </p>
 * 
 * @author Steve Phelps
 * 
 */
public class PRNGSeedFactoryBean implements FactoryBean<Integer> {
	
	protected Random metaPrng = new Random();
	
	static Logger logger = Logger.getLogger(PRNGSeedFactoryBean.class);
	
	@Override
	public Integer getObject()  {
		try {
			int time = new java.util.Date().hashCode();
			int ipAddress = InetAddress.getLocalHost().hashCode();
			int rand = metaPrng.nextInt();
			int seed = (ipAddress & 0xffff) |
						((time & 0x00ff) << 32) | (rand & 0x0f000000);
			logger.info("seed = " + seed);
			return seed;
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Class<?> getObjectType() {
		return Integer.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
	
}
