package net.sourceforge.jabm.distribution;

import org.springframework.beans.factory.annotation.Required;

import cern.jet.random.Normal;

public class NormalDistribution extends AbstractDelegatedDistribution {
	
	protected double mean = 0.0;
	
	protected double stdev = 1.0;

	public double getMean() {
		return mean;
	}

	@Required
	public void setMean(double mean) {
		this.mean = mean;
		reinitialise();
	}

	public double getStdev() {
		return stdev;
	}

	@Required
	public void setStdev(double stdev) {
		this.stdev = stdev;
		reinitialise();
	}
	
	@Override
	public void initialise() {
		delegate = new Normal(mean, stdev, prng);
	}
	
}
