package com.villagomezdiaz.common.results;

import com.villagomezdiaz.common.ImageCorrelation;

public class JedisThreeColorCorrelationResults extends JedisCorrelationResults {

	public JedisThreeColorCorrelationResults(String filterName) {
		super(filterName);
		// TODO Auto-generated constructor stub
	}

@Override
public double getCorrelation(ImageCorrelation corr) {
	// TODO Auto-generated method stub
	return corr.getThreeColorCorrelation();
}

@Override
public String getClientFilterName() {
	// TODO Auto-generated method stub
	return "rgb";
}

}
