package com.villagomezdiaz.common.ds;

import java.util.Set;

public class BirdsResults {

	private String birdName;
	private String path;
	private double overallScore;
	private Set<FilterResults> fResults;
	
	
	public BirdsResults() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BirdsResults(String birdName, String path, double overallScore, Set<FilterResults> fResults) {
		super();
		this.birdName = birdName;
		this.path = path;
		this.overallScore = overallScore;
		this.fResults = fResults;
	}
	
	public String getBirdName() {
		return birdName;
	}
	public void setBirdName(String birdName) {
		this.birdName = birdName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public double getOverallScore() {
		return overallScore;
	}
	public void setOverallScore(double overallScore) {
		this.overallScore = overallScore;
	}
	public Set<FilterResults> getfResults() {
		return fResults;
	}
	public void setfResults(Set<FilterResults> fResults) {
		this.fResults = fResults;
	}

	@Override
	public String toString() {
		return "BirdsResults [birdName=" + birdName + ", path=" + path + ", overallScore=" + overallScore
				+ ", fResults=" + fResults + "]";
	}
	
}
