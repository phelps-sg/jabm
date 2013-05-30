package net.sourceforge.jabm.distribution;

import java.io.File;

import org.apache.commons.math3.random.RandomGenerator;
import org.springframework.beans.factory.InitializingBean;

import cern.jet.random.AbstractContinousDistribution;
import cern.jet.random.engine.RandomEngine;

public class EmpiricalDistribution extends AbstractContinousDistribution 
		implements InitializingBean {

	protected org.apache.commons.math3.random.EmpiricalDistribution delegate;
	
	protected String dataFileName;
	
	protected int binCount;
	
	protected RandomEngine prng;
	
	@Override
	public double nextDouble() {
		return delegate.getNextValue();
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}
	
	public int getBinCount() {
		return binCount;
	}

	public void setBinCount(int binCount) {
		this.binCount = binCount;
	}
	
	public RandomEngine getPrng() {
		return prng;
	}

	public void setPrng(RandomEngine prng) {
		this.prng = prng;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		RandomGenerator generator = new RandomGeneratorAdaptor(prng);
		delegate = 
				new org.apache.commons.math3.random.EmpiricalDistribution(binCount, generator);
		delegate.load(new File(dataFileName));
	}
	
}
