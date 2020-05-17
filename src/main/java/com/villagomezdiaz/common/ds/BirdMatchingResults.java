package com.villagomezdiaz.common.ds;

import java.util.Set;

public class BirdMatchingResults {

	private int numBirds;
	private long numSpecies;
	
	private Set<BirdsResults> bResults;

	
	public BirdMatchingResults() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BirdMatchingResults(int numBirds, int numSpecies, Set<BirdsResults> bResults) {
		super();
		this.numBirds = numBirds;
		this.numSpecies = numSpecies;
		this.bResults = bResults;
	}

	public int getNumBirds() {
		return numBirds;
	}

	public void setNumBirds(int numBirds) {
		this.numBirds = numBirds;
	}

	public long getNumSpecies() {
		return numSpecies;
	}

	public void setNumSpecies( long numSpecies) {
		this.numSpecies = numSpecies;
	}

	public Set<BirdsResults> getbResults() {
		return bResults;
	}

	public void setbResults(Set<BirdsResults> bResults) {
		this.bResults = bResults;
	}

	@Override
	public String toString() {
		return "BirdMatchingResults [numBirds=" + numBirds + ", numSpecies=" + numSpecies + ", bResults=" + bResults
				+ "]";
	}
	
}

