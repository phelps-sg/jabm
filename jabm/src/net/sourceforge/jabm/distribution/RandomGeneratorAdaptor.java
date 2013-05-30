package net.sourceforge.jabm.distribution;

import org.apache.commons.math3.random.RandomGenerator;

import cern.jet.random.Normal;
import cern.jet.random.engine.RandomEngine;

public class RandomGeneratorAdaptor implements RandomGenerator {

	protected RandomEngine prng;
	
	public RandomGeneratorAdaptor(RandomEngine prng) {
		super();
		this.prng = prng;
	}
	
	public RandomGeneratorAdaptor() {
		this(null);
	}

	@Override
	public boolean nextBoolean() {
		return prng.raw() < 0.5;
	}

	@Override
	public void nextBytes(byte[] arg0) {
		throw new RuntimeException("unimplemented");
	}

	@Override
	public double nextDouble() {
		return prng.nextDouble();
	}

	@Override
	public float nextFloat() {
		return prng.nextFloat();
	}

	@Override
	public double nextGaussian() {
		return new Normal(0, 1, prng).nextDouble();
	}

	@Override
	public int nextInt() {
		return prng.nextInt();
	}

	@Override
	public int nextInt(int arg0) {
		throw new RuntimeException("unimplemented");
	}

	@Override
	public long nextLong() {
		return prng.nextLong();
	}

	@Override
	public void setSeed(int arg0) {
		throw new RuntimeException("unimplemented");
	}

	@Override
	public void setSeed(int[] arg0) {
		throw new RuntimeException("unimplemented");
	}

	@Override
	public void setSeed(long arg0) {
		throw new RuntimeException("unimplemented");
	}

	public RandomEngine getPrng() {
		return prng;
	}

	public void setPrng(RandomEngine prng) {
		this.prng = prng;
	}
	
	

}
