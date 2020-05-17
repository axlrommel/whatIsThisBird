package com.villagomezdiaz.common.results;

import com.villagomezdiaz.common.ImageCorrelation;

public class JedisYellowCorrelationResults extends JedisCorrelationResults {

	public JedisYellowCorrelationResults(String filterName) {
		super(filterName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getCorrelation(ImageCorrelation corr) {
		// TODO Auto-generated method stub
		return corr.getYellowColorCorrelation();
	}

	@Override
	public String getClientFilterName() {
		// TODO Auto-generated method stub
		return "yellow";
	}

}
