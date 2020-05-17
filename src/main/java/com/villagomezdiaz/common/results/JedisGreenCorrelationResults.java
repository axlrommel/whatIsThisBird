package com.villagomezdiaz.common.results;

import com.villagomezdiaz.common.ImageCorrelation;

public class JedisGreenCorrelationResults extends JedisCorrelationResults {

	public JedisGreenCorrelationResults(String filterName) {
		super(filterName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getCorrelation(ImageCorrelation corr) {
		// TODO Auto-generated method stub
		return corr.getGreenColorCorrelation();
	}

	@Override
	public String getClientFilterName() {
		// TODO Auto-generated method stub
		return "green";
	}

}
