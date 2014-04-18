package net.sourceforge.jabm.distribution;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import cern.jet.random.AbstractContinousDistribution;
import cern.jet.random.engine.RandomEngine;

public abstract class AbstractDelegatedDistribution
		extends AbstractContinousDistribution implements InitializingBean {

	protected RandomEngine prng;
	
	protected AbstractContinousDistribution delegate;
	
	protected boolean initialised = false;
	
	@Override
	public double nextDouble() {
		return delegate.nextDouble();
	}

	public RandomEngine getPrng() {
		return prng;
	}

	@Required
	public void setPrng(RandomEngine prng) {
		this.prng = prng;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initialise();
		this.initialised = true;
	}

	/**
	 * Initialise the delegate provided that the bean has
	 * already been configured.  This should be called by
	 * setters after any moments are changed in case
	 * we are being configured by a PropertyOverrideConfigurator.
	 */
	public void reinitialise() {
		if (initialised) {
			initialise();
		}
	}
	
	public abstract void initialise();
	
}
