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

//import ec.util.ParamClassLoadException;
//import ec.util.Parameter;
//import ec.util.ParameterDatabase;

import org.apache.log4j.Logger;

/**
 * @author Steve Phelps
 * @version $Revision: 16 $
 */

public abstract class DistributionFactory {

	protected static DistributionFactory currentFactory = new Cummulative();

	public static final String P_DISTFACTORY = "distribution";

	public static final String P_DEF_BASE = "P_DISTFACTORY";

	static Logger logger = Logger.getLogger(DistributionFactory.class);

//	public static void setup(ParameterDatabase parameters, Parameter base) {
//		try {
//			DistributionFactory.currentFactory = (DistributionFactory) parameters
//			    .getInstanceForParameter(base.push(P_DISTFACTORY), new Parameter(
//			        P_DEF_BASE), DistributionFactory.class);
//		} catch (ParamClassLoadException e) {
//			logger.warn(e.getMessage());
//		}
//	}

	public static DistributionFactory getFactory() {
		return currentFactory;
	}

	public abstract Distribution create();

	public abstract Distribution create(String name);

//	public static class Heavyweight extends DistributionFactory {
//
//		public Distribution create() {
//			return new HeavyweightDistribution();
//		}
//
//		public Distribution create(String name) {
//			return new HeavyweightDistribution(name);
//		}
//	}

	public static class Cummulative extends DistributionFactory {

		public Distribution create() {
			return new SummaryStats();
		}

		public Distribution create(String name) {
			return new SummaryStats(name);
		}
	}
}
