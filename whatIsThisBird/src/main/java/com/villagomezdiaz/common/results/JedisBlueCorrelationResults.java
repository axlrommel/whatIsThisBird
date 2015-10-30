package com.villagomezdiaz.common.results;

import com.villagomezdiaz.common.ImageCorrelation;

public class JedisBlueCorrelationResults extends JedisCorrelationResults {

	public JedisBlueCorrelationResults(String filterName) {
		super(filterName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getCorrelation(ImageCorrelation corr) {
		// TODO Auto-generated method stub
		return corr.getBlueColorCorrelation();
	}

	@Override
	public String getClientFilterName() {
		// TODO Auto-generated method stub
		return "blue";
	}

}
