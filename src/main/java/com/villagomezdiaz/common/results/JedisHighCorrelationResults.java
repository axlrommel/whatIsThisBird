package com.villagomezdiaz.common.results;

import com.villagomezdiaz.common.ImageCorrelation;

public class JedisHighCorrelationResults extends JedisCorrelationResults {

	public JedisHighCorrelationResults(String filterName) {
		super(filterName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getCorrelation(ImageCorrelation corr) {
		// TODO Auto-generated method stub
		return corr.getHighColorCorrelation();
	}

	@Override
	public String getClientFilterName() {
		// TODO Auto-generated method stub
		return "light gray";
	}

}
