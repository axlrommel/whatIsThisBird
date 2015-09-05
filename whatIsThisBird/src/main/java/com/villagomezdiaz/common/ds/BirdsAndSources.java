package com.villagomezdiaz.common.ds;

import java.util.List;

public class BirdsAndSources {
	private List<String> sources;
	
	private List<String> birds;

	public List<String> getSources() {
		return sources;
	}

	public void setSources(List<String> sources) {
		this.sources = sources;
	}

	public List<String> getBirds() {
		return birds;
	}

	public void setBirds(List<String> birds) {
		this.birds = birds;
	}

	@Override
	public String toString() {
		return "BirdsAndSources [sources=" + sources + ", birds=" + birds + "]";
	}

	
}
