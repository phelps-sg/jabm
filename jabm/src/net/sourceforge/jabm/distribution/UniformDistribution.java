package net.sourceforge.jabm.distribution;

import org.springframework.beans.factory.annotation.Required;

import cern.jet.random.Uniform;

public class UniformDistribution extends AbstractDelegatedDistribution {

	protected double min;
	
	protected double max;
	
	public double getMin() {
		return min;
	}

	@Required
	public void setMin(double min) {
		this.min = min;
		reinitialise();
	}

	public double getMax() {
		return max;
	}

	@Required
	public void setMax(double max) {
		this.max = max;
		reinitialise();
	}

	@Override
	public void initialise() {
		this.delegate = new Uniform(min, max, prng);
	}
	
}
