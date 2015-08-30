package com.villagomezdiaz.common;

import java.util.Map;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class ImageCorrelation {

	private boolean debug = false;
	
	private ImageStatistics statImg1;
	private ImageStatistics statImg2;
	
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
	
	
	public ImageCorrelation(ImageStatistics statImg1, ImageStatistics statImg2) {
		super();
		this.statImg1 = statImg1;
		this.statImg2 = statImg2;
		
		PearsonsCorrelation corr = new PearsonsCorrelation();
		
		this.lowRedCorr =  corr.correlation(statImg1.getLowReds(), statImg2.getLowReds());
		this.lowGreenCorr = corr.correlation(statImg1.getLowGreens(), statImg2.getLowGreens());
		this.lowBlueCorr = corr.correlation(statImg1.getLowBlues(), statImg2.getLowBlues());
		
		/*this.midRedCorr =  corr.correlation(statImg1.getMidReds(), statImg2.getMidReds());
		this.midGreenCorr = corr.correlation(statImg1.getMidGreens(), statImg2.getMidGreens());
		this.midBlueCorr = corr.correlation(statImg1.getMidBlues(), statImg2.getMidBlues());*/
		
		this.highRedCorr =  corr.correlation(statImg1.getHighReds(), statImg2.getHighReds());
		this.highGreenCorr = corr.correlation(statImg1.getHighGreens(), statImg2.getHighGreens());
		this.highBlueCorr = corr.correlation(statImg1.getHighBlues(), statImg2.getHighBlues());
		
		this.redCorr =  corr.correlation(statImg1.getReds(), statImg2.getReds());
		this.greenCorr = corr.correlation(statImg1.getGreens(), statImg2.getGreens());
		this.blueCorr = corr.correlation(statImg1.getBlues(), statImg2.getBlues());
		
		
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

	public double getOverallCorrelation() {
		
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
	

	
	public double getRedColorMatching(int colorNum) {
		
		if(colorNum <= 0 || colorNum > 255) {
			return 0;
		}
		
		double retval = (double) getColorMatching(colorNum, OrderedMapDuplicateValues.sortByValue(statImg1.getMapReds()),
				OrderedMapDuplicateValues.sortByValue(statImg2.getMapReds()));
		
		if(this.debug) {
			System.out.println("Red retval: " + retval);
		}
		
		return retval;
	}
	
public double getGreenColorMatching(int colorNum) {
		
		if(colorNum <= 0 || colorNum > 255) {
			return 0;
		}
		
		double retval = (double) getColorMatching(colorNum, OrderedMapDuplicateValues.sortByValue(statImg1.getMapGreens()),
				OrderedMapDuplicateValues.sortByValue(statImg2.getMapGreens()));
		
		if(this.debug) {
			System.out.println("Green retval: " + retval);
		}
		
		return retval;
	}
	
	public double getBlueColorMatching(int colorNum) {
		
		if(colorNum <= 0 || colorNum > 255) {
			return 0;
		}
		
		double retval = (double) getColorMatching(colorNum, OrderedMapDuplicateValues.sortByValue(statImg1.getMapBlues()),
				OrderedMapDuplicateValues.sortByValue(statImg2.getMapBlues()));
		
		if(this.debug) {
			System.out.println("Blue retval: " + retval);
		}
		
		return retval;
	}
	
	private double getColorMatching(int colorNum, Map<Integer, Integer> sorted1Map, Map<Integer, Integer> sorted2Map) {
			
		
		int[] img1TopVals = new int[colorNum];
		int[] img2TopVals = new int[colorNum];
		
		int i = 0;
		
		for (Map.Entry<Integer, Integer> entry : sorted1Map.entrySet()) {
			if(i == colorNum) {
				break;
			}
			if(this.debug) {
//	            System.out.println("Item is:" + entry.getKey() + " with value:"
//	                    + entry.getValue());
			}
			img1TopVals[i++] = entry.getKey().intValue();
        }
		
		i = 0;
		
		for (Map.Entry<Integer, Integer> entry : sorted2Map.entrySet()) {
			if(i == colorNum) {
				break;
			}
			if(this.debug) {
//	            System.out.println("Item is:" + entry.getKey() + " with value:"
//	                    + entry.getValue());
			}
			img2TopVals[i++] = entry.getKey().intValue();
        }
		
		int count = 0;
		
		for(int j = 0; j < colorNum; j++) {
			for(int k = 0; k < colorNum; k++) {
				if(this.debug) {
//					System.out.println("entry 1:" + img1TopVals[j] + " entry 2:"
//	                    + img2TopVals[k]);
				}
				if(img1TopVals[j] == img2TopVals[k]) {
					count++;
				}
			}
		}
		
		
		double retval = ((double)count/((double)colorNum*2));
		
		if(this.debug) {
			System.out.println("found " + count + " total: " + colorNum + " percent match: " + retval);
		}
		
		return retval;
	}
	

	public double getOverallColorMatching(int colorNum) {
		
		return (double)(((double)getRedColorMatching(colorNum) + (double)getGreenColorMatching(colorNum) +
				(double)getBlueColorMatching(colorNum))/3);
	}
	
	public double getOneColorMatching(int colorNum) {
		return getNColorMatching(colorNum,0);
	}
	
	public double getTwoColorMatching(int colorNum) {
		double val1 = getNColorMatching(colorNum,0);
		double val2 = getNColorMatching(colorNum,1);
		//return val2;
		return ((val1*0.6) + (val2*0.4));
	}
	
	public double getThreeColorMatching(int colorNum) {
		double val1 = getNColorMatching(colorNum,0);
		double val2 = getNColorMatching(colorNum,1);
		double val3 = getNColorMatching(colorNum,2);
		
		//return val3;
		return ((val1*0.5) + (val2*0.35) + (val3*.15));
	}
	
	private double getNColorMatching(int colorNum, int index) {
		
		if(colorNum <= 0 || colorNum > 255) {
			return 0.;
		}
		
		double rDiff = statImg1.getTopReds()[index] - statImg2.getTopReds()[index];
		if(rDiff < 0) {
			rDiff = -rDiff;
		}
		
		double gDiff = statImg1.getTopGreens()[index] - statImg2.getTopGreens()[index];
		if(gDiff < 0) {
			gDiff = -gDiff;
		}
		
		double bDiff = statImg1.getTopBlues()[index] - statImg2.getTopBlues()[index];
		if(bDiff < 0) {
			bDiff = -bDiff;
		}
		
		double rnum = 0;
		double gnum = 0;
		double bnum = 0;
			
		if(rDiff < colorNum && statImg1.getTopReds()[index] != -1 && statImg2.getTopReds()[index] != -1) {
			rnum = 1 - (rDiff/colorNum);
		}
		
		if(gDiff < colorNum && statImg1.getTopGreens()[index] != -1 && statImg2.getTopGreens()[index] != -1) {
			gnum = 1 - (gDiff/colorNum);
		}
		
		if(bDiff < colorNum && statImg1.getTopBlues()[index] != -1 && statImg2.getTopBlues()[index] != -1) {
			bnum = 1 - (bDiff/colorNum);
		}
			
		if(this.debug) {
			System.out.println("color max, red: " + rnum + ", green: " + gnum + ", blue: " + bnum);
		}
		
		return (double) ((rnum + gnum + bnum)/3);
		
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	
}
