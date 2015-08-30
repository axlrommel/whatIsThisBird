package com.villagomezdiaz.common;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.jhlabs.image.Histogram;
import com.jhlabs.image.ImageUtils;

public class ImageStatistics {
	
	private static int POPULATION_DIFFERENCE = 50;
	private static int COLOR_SHIFT_LIGHT_DARK = 200;
	private double[] topReds = {-1,-1,-1};
	private double[] topGreens = {-1,-1,-1};
	private double[] topBlues = {-1,-1,-1};
	
	// 0 - 255
	private double[] reds = new double[255];
	private double[] greens = new double[255];
	private double[] blues = new double[255];
	
	// 0 - 200 approx
	private double[] lowReds = new double[COLOR_SHIFT_LIGHT_DARK];
	private double[] lowGreens = new double[COLOR_SHIFT_LIGHT_DARK];
	private double[] lowBlues = new double[COLOR_SHIFT_LIGHT_DARK];
	
	// 22 - 222 approx
	private double[] midReds = new double[COLOR_SHIFT_LIGHT_DARK];
	private double[] midGreens = new double[COLOR_SHIFT_LIGHT_DARK];
	private double[] midBlues = new double[COLOR_SHIFT_LIGHT_DARK];
	
	// 55 - 255 approx
	private double[] highReds = new double[COLOR_SHIFT_LIGHT_DARK];
	private double[] highGreens = new double[COLOR_SHIFT_LIGHT_DARK];
	private double[] highBlues = new double[COLOR_SHIFT_LIGHT_DARK];
	
	private HashMap<Integer, Integer> mapReds = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> mapGreens = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> mapBlues = new HashMap<Integer, Integer>();

	public ImageStatistics(BufferedImage img) {
		
		super();
		int imageHeight = img.getHeight();
		int imageWidth = img.getWidth();
		
		int[] outPixels = ImageUtils.getRGB(img,0,0,imageWidth,imageHeight,null);
		Histogram hist = new Histogram(outPixels, imageWidth, imageHeight, 0, imageWidth);
			
		//ignore i = 0;
		for(int i = 0; i < 255; i++) {
			reds[i] = (double)hist.getFrequency(Histogram.RED,i+1);
			greens[i] = (double)hist.getFrequency(Histogram.GREEN,i+1);
			blues[i] = (double)hist.getFrequency(Histogram.BLUE,i+1);
			
			mapReds.put(new Integer(i), new Integer((int) reds[i]));
			mapGreens.put(new Integer(i), new Integer((int) greens[i]));
			mapBlues.put(new Integer(i), new Integer((int) blues[i]));
			
		}
		initRestOfValues();
		
	}
	
	public ImageStatistics(String[] r, String[] g, String[] b) {
	
		for(int i = 0; i < 255; i++) {
			reds[i] = Double.valueOf(r[i]).doubleValue();
			greens[i] = Double.valueOf(g[i]).doubleValue();
			blues[i] = Double.valueOf(b[i]).doubleValue();
			
			mapReds.put(new Integer(i), new Integer((int) reds[i]));
			mapGreens.put(new Integer(i), new Integer((int) greens[i]));
			mapBlues.put(new Integer(i), new Integer((int) blues[i]));
			
		}
		
		initRestOfValues();
	}
	
	public ImageStatistics(double[] r, double[] g, double[] b) {
	
		for(int i = 0; i < 255; i++) {
			reds[i] = r[i];
			greens[i] = g[i];
			blues[i] = b[i];
			
			mapReds.put(new Integer(i), new Integer((int) reds[i]));
			mapGreens.put(new Integer(i), new Integer((int) greens[i]));
			mapBlues.put(new Integer(i), new Integer((int) blues[i]));
			
		}
		
		initRestOfValues();
	}
	private void initRestOfValues() {
		
		int startMids = (int) ((255 - COLOR_SHIFT_LIGHT_DARK)/2);
		
		for(int i = 0; i < COLOR_SHIFT_LIGHT_DARK; i++) {
			lowReds[i] = reds[i];
			lowGreens[i] = greens[i];
			lowBlues[i] = blues[i];
			highReds[i] = reds[i+255-COLOR_SHIFT_LIGHT_DARK];
			highGreens[i] = greens[i+255-COLOR_SHIFT_LIGHT_DARK];
			highBlues[i] = blues[i+255-COLOR_SHIFT_LIGHT_DARK];
			midReds[i] = reds[startMids];
			midGreens[i] = greens[startMids];
			midBlues[i] = blues[startMids];
			startMids++;
			
		}
		
		topReds = getTopThree(mapReds);
		topGreens = getTopThree(mapGreens);
		topBlues = getTopThree(mapBlues);
		
		
	}
	
	/**
	 * let's return the max for each population
	 * @param populations
	 * @param list
	 * @return
	 */
	private double[] getTopThree(HashMap<Integer, Integer> map) {
		
		int numPopulations = 3; //top three
		
		double[] retval = {-1,-1,-1};
		
		// let's order the map
		Map<Integer, Integer> sorted1Map = OrderedMapDuplicateValues.sortByValue(map);
		
		int i = 0;
		int max1 = -1;
		int max2 = -1;
		int max3 = -1;
		for (Map.Entry<Integer, Integer> entry : sorted1Map.entrySet()) {
			if(i == 0) { // highest freq
				max1 = entry.getKey().intValue();
				retval[i++] = max1;
			}
			else if (max2 == -1){
				int val = entry.getKey().intValue();
				if(val - max1 > POPULATION_DIFFERENCE || max1 - val > POPULATION_DIFFERENCE) {
					max2 = val;
					retval[i++] = max2;
				}
			}
			else if (max3 == -1){
				int val = entry.getKey().intValue();
				if((val - max1 > POPULATION_DIFFERENCE || max1 - val > POPULATION_DIFFERENCE) &&
						(val - max2 > POPULATION_DIFFERENCE || max2 - val > POPULATION_DIFFERENCE)){
					max3 = val;
					retval[i++] = max3;
				}
			}
			
			if(i == numPopulations || max3 != -1) {
				break;
			}
        }
		
		return retval;
	}

	public HashMap<Integer, Integer> getMapReds() {
		return mapReds;
	}

	public HashMap<Integer, Integer> getMapGreens() {
		return mapGreens;
	}

	public HashMap<Integer, Integer> getMapBlues() {
		return mapBlues;
	}
	
	public double[] getReds() {
		return reds;
	}

	public double[] getGreens() {
		return greens;
	}

	public double[] getBlues() {
		return blues;
	}

	public double[] getTopReds() {
		return topReds;
	}

	public String getTopRedsAsString() {
		StringBuffer tmp = new StringBuffer("[");
		tmp.append(String.valueOf(topReds[0]));
		tmp.append(",");
		tmp.append(String.valueOf(topReds[1]));
		tmp.append(",");
		tmp.append(String.valueOf(topReds[2]));
		tmp.append("]");
		return tmp.toString();
		
	}
	
	public double[] getTopGreens() {
		return topGreens;
	}

	public String getTopGreensAsString() {
		StringBuffer tmp = new StringBuffer("[");
		tmp.append(String.valueOf(topGreens[0]));
		tmp.append(",");
		tmp.append(String.valueOf(topGreens[1]));
		tmp.append(",");
		tmp.append(String.valueOf(topGreens[2]));
		tmp.append("]");
		return tmp.toString();
		
	}
	
	public double[] getTopBlues() {
		return topBlues;
	}
	
	public String getTopBluesAsString() {
		StringBuffer tmp = new StringBuffer("[");
		tmp.append(String.valueOf(topBlues[0]));
		tmp.append(",");
		tmp.append(String.valueOf(topBlues[1]));
		tmp.append(",");
		tmp.append(String.valueOf(topBlues[2]));
		tmp.append("]");
		return tmp.toString();
		
	}

	public double[] getLowReds() {
		return lowReds;
	}

	public double[] getLowGreens() {
		return lowGreens;
	}

	public double[] getLowBlues() {
		return lowBlues;
	}

	public double[] getHighReds() {
		return highReds;
	}

	public double[] getHighGreens() {
		return highGreens;
	}

	public double[] getHighBlues() {
		return highBlues;
	}

	public double[] getMidReds() {
		return midReds;
	}

	public double[] getMidGreens() {
		return midGreens;
	}

	public double[] getMidBlues() {
		return midBlues;
	}
	
	
}
