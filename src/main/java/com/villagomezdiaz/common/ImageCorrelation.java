package com.villagomezdiaz.common;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class ImageCorrelation {
	
	double lowRedCorr = 0;
	double midRedCorr = 0;
	double highRedCorr = 0;
	double lowGreenCorr = 0;
	double midGreenCorr = 0;
	double highGreenCorr = 0;
	double lowBlueCorr = 0;
	double midBlueCorr = 0;
	double highBlueCorr = 0;
	double redCorr = 0;
	double greenCorr = 0;
	double blueCorr = 0;
	double overallCorr = 0;
	
	
	public ImageCorrelation(ImageStatistics statImg1, ImageStatistics statImg2) {
		super();
		
		PearsonsCorrelation corr = new PearsonsCorrelation();
		
		this.lowRedCorr =  corr.correlation(statImg1.getLowReds(), statImg2.getLowReds());
		this.lowGreenCorr = corr.correlation(statImg1.getLowGreens(), statImg2.getLowGreens());
		this.lowBlueCorr = corr.correlation(statImg1.getLowBlues(), statImg2.getLowBlues());
		
		/*
		this.midRedCorr =  corr.correlation(statImg1.getMidReds(), statImg2.getMidReds());
		this.midGreenCorr = corr.correlation(statImg1.getMidGreens(), statImg2.getMidGreens());
		this.midBlueCorr = corr.correlation(statImg1.getMidBlues(), statImg2.getMidBlues());
		*/
		this.highRedCorr =  corr.correlation(statImg1.getHighReds(), statImg2.getHighReds());
		this.highGreenCorr = corr.correlation(statImg1.getHighGreens(), statImg2.getHighGreens());
		this.highBlueCorr = corr.correlation(statImg1.getHighBlues(), statImg2.getHighBlues());
		
		this.redCorr =  corr.correlation(statImg1.getReds(), statImg2.getReds());
		this.greenCorr = corr.correlation(statImg1.getGreens(), statImg2.getGreens());
		this.blueCorr = corr.correlation(statImg1.getBlues(), statImg2.getBlues());
		
		this.overallCorr = redCorr + greenCorr + blueCorr + lowRedCorr + lowGreenCorr +
				lowBlueCorr + highRedCorr + highGreenCorr + highBlueCorr;		
		
	}
		
	public double getLowColorCorrelation() {
		
		return ((this.lowRedCorr + this.lowGreenCorr + this.lowBlueCorr)/3);
	}

	public double getMidColorCorrelation() {
		
		return ((this.midRedCorr + this.midGreenCorr + this.midBlueCorr)/3);
	}

	public double getHighColorCorrelation() {
		
		return ((this.highRedCorr + this.highGreenCorr + this.highBlueCorr)/3);
	}

	public double getThreeColorCorrelation() {
		
		return ((this.redCorr + this.greenCorr + this.blueCorr)/3);
	}

	public double getRedColorCorrelation() {
			
		return ((this.highRedCorr + this.lowGreenCorr + this.lowBlueCorr)/3);
	}

	public double getGreenColorCorrelation() {
		
		return ((this.lowRedCorr + this.highGreenCorr + this.lowBlueCorr)/3);
	}
	
	public double getBlueColorCorrelation() {
		
		return ((this.lowRedCorr + this.lowGreenCorr + this.highBlueCorr)/3);
	}

	public double getYellowColorCorrelation() {

		return ((this.highRedCorr + this.highGreenCorr + this.lowBlueCorr)/3);
	}
	
	public double getCyanColorCorrelation() {

		return ((this.lowRedCorr + this.highGreenCorr + this.highBlueCorr)/3);
	}
	
	public double getMagentaColorCorrelation() {

		return ((this.highRedCorr + this.lowGreenCorr + this.highBlueCorr)/3);
	}

	public double getOverallCorr() {
		return overallCorr;
	}
	
	
	
}
