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
package net.sourceforge.jabm.evolution;

import java.util.List;

import net.sourceforge.jabm.agent.AgentList;

/**
 * <p>
 * A combination of different breeders which forms a pipeline for producing the
 * next generation.
 * </p>
 * 
 * @author Steve Phelps
 */
public class CombiBreeder implements Breeder {

	/**
	 * The breeding pipeline.
	 */
	protected List<Breeder> breedingPipeline;
	
	@Override
	public AgentList reproduce(AgentList currentGeneration) {
		AgentList nextGeneration = currentGeneration;
		for(Breeder breeder : breedingPipeline) {
			nextGeneration = breeder.reproduce(nextGeneration);
		}
		return nextGeneration;
	}

	public List<Breeder> getBreedingPipeline() {
		return breedingPipeline;
	}

	public void setBreedingPipeline(List<Breeder> breedingPipeline) {
		this.breedingPipeline = breedingPipeline;
	}

}
