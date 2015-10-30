package com.villagomezdiaz.common.results;

import com.villagomezdiaz.common.ImageCorrelation;

public class JedisRedCorrelationResults extends JedisCorrelationResults {

	public JedisRedCorrelationResults(String filterName) {
		super(filterName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getCorrelation(ImageCorrelation corr) {
		// TODO Auto-generated method stub
		return corr.getRedColorCorrelation();
	}

	@Override
	public String getClientFilterName() {
		// TODO Auto-generated method stub
		return "red";
	}

}
