package com.villagomezdiaz.common.results;

import com.villagomezdiaz.common.ImageCorrelation;

public abstract class JedisCorrelationResults {
	private String filterName;

	public JedisCorrelationResults(String filterName) {
		super();
		this.filterName = filterName;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public abstract double getCorrelation(ImageCorrelation corr);
	
	public abstract String getClientFilterName();
	

}
