package com.villagomezdiaz.common;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.jhlabs.image.EdgeFilter;
import com.jhlabs.image.ImageUtils;
import com.jhlabs.image.LaplaceFilter;


public class ColorBlackBackgroundFilter {
	
	
	// threshold  is 0 or 255
	private int threshold = 0;

	public ColorBlackBackgroundFilter(int threshold) {
		super();
		this.threshold = threshold;
	}

	public BufferedImage imageConvertToBlackBackgroundFromCenter(BufferedImage image) {
		
		LaplaceFilter lapFilter = new LaplaceFilter();
		
		BufferedImage imageTmp = null;
		BufferedImage imageOut = ImageUtils.cloneImage(image);
		imageTmp = lapFilter.filter(image, imageTmp);
		int halfWidth= 0;
		
		if((imageTmp.getWidth()% 2 ) == 1) {
			halfWidth = (imageTmp.getWidth() - 1)/2;			
		}
		else {
			halfWidth = (imageTmp.getWidth())/2;
		}
		
		
		// first pass, start in the middle and move to the right
		
		int[] colorBlack = new int[]{0, 0, 0};
		for(int i = 0; i < imageTmp.getHeight(); i++) {
			boolean foundEdge = false;
			for(int j = halfWidth; j < imageTmp.getWidth(); j++) {
				if(foundEdge) {
					ImageUtils.setRGB(imageOut, j, i, 1, 1, colorBlack);
				}
				else {
					Color c = new Color(imageTmp.getRGB(j, i));
					if(c.getRed() >= threshold || c.getGreen() >= threshold || c.getBlue() >= threshold) {
						foundEdge = true;
					}
				}
			}
		}
		
		// second pass, start in the middle and move to the left
		for(int i = 0; i < imageTmp.getHeight(); i++) {
			boolean foundEdge = false;
			for(int j = halfWidth -1 ; j >= 0; j--) {
				if(foundEdge) {
					ImageUtils.setRGB(imageOut, j, i, 1, 1, colorBlack);
				}
				else {
					Color c = new Color(imageTmp.getRGB(j, i));
					if(c.getRed() >= threshold || c.getGreen() >= threshold || c.getBlue() >= threshold) {
						foundEdge = true;
					}
				}
			}
		}
		
		return imageOut;
	}
	
public BufferedImage imageConvertToBlackBackground(BufferedImage image) {
		
		EdgeFilter edgeFilter = new EdgeFilter();
		
		BufferedImage imageTmp = null;
		BufferedImage imageOut = ImageUtils.cloneImage(image);
		imageTmp = edgeFilter.filter(image, imageTmp);
		
		// first pass, let's get rid of the background from the left
		
		int[] colorBlack = new int[]{0, 0, 0};
		for(int i = 0; i < imageTmp.getHeight(); i++) {
			for(int j = 0; j < imageTmp.getWidth(); j ++) {
				Color c = new Color(imageTmp.getRGB(j, i));
				if(c.getRed() < threshold && c.getGreen() < threshold && c.getBlue() < threshold) {
					ImageUtils.setRGB(imageOut, j, i, 1, 1, colorBlack);
				}
				else {
					break;
				}
			}
		}
		
		//now let's do it from the right
		for(int i = 0; i < imageTmp.getHeight(); i++) {
			for(int j = imageTmp.getWidth() -1 ; j >= 0; j --) {
				Color c = new Color(imageTmp.getRGB(j, i));
				if(c.getRed() < threshold && c.getGreen() < threshold && c.getBlue() < threshold) {
					ImageUtils.setRGB(imageOut, j, i, 1, 1, colorBlack);
				}
				else {
					break;
				}
			}
		}
		
		return imageOut;
	}

public BufferedImage imageConvertToBlackBackgroundFromAll(BufferedImage image) {
	
	EdgeFilter edgeFilter = new EdgeFilter();
	
	BufferedImage imageTmp = null;
	BufferedImage imageOut = ImageUtils.cloneImage(image);
	imageTmp = edgeFilter.filter(image, imageTmp);
	
	// first pass, let's get rid of the background from the left
	
	int[] colorBlack = new int[]{0, 0, 0};
	for(int i = 0; i < imageTmp.getHeight(); i++) {
		for(int j = 0; j < imageTmp.getWidth(); j ++) {
			Color c = new Color(imageTmp.getRGB(j, i));
			if(c.getRed() < threshold && c.getGreen() < threshold && c.getBlue() < threshold) {
				ImageUtils.setRGB(imageOut, j, i, 1, 1, colorBlack);
			}
			else {
				break;
			}
		}
	}
	
	//now let's do it from the right
	for(int i = 0; i < imageTmp.getHeight(); i++) {
		for(int j = imageTmp.getWidth() -1 ; j >= 0; j --) {
			Color c = new Color(imageTmp.getRGB(j, i));
			if(c.getRed() < threshold && c.getGreen() < threshold && c.getBlue() < threshold) {
				ImageUtils.setRGB(imageOut, j, i, 1, 1, colorBlack);
			}
			else {
				break;
			}
		}
	}
	
	for(int j = 0; j < imageTmp.getWidth(); j++) {
		for(int i = 0; i < imageTmp.getHeight(); i ++) {
			Color c = new Color(imageTmp.getRGB(j, i));
			if(c.getRed() < threshold && c.getGreen() < threshold && c.getBlue() < threshold) {
				ImageUtils.setRGB(imageOut, j, i, 1, 1, colorBlack);
			}
			else {
				break;
			}
		}
	}
	
	//now let's do it from the right
	for(int j = 0; j < imageTmp.getWidth(); j++) {
		for(int i = imageTmp.getHeight() -1 ; i >= 0; i --) {
			Color c = new Color(imageTmp.getRGB(j, i));
			if(c.getRed() < threshold && c.getGreen() < threshold && c.getBlue() < threshold) {
				ImageUtils.setRGB(imageOut, j, i, 1, 1, colorBlack);
			}
			else {
				break;
			}
		}
	}
	
	return imageOut;
}

public BufferedImage imageConvertToBlackBackgroundFromTop(BufferedImage image) {
	
	EdgeFilter edgeFilter = new EdgeFilter();
	
	BufferedImage imageTmp = null;
	BufferedImage imageOut = ImageUtils.cloneImage(image);
	imageTmp = edgeFilter.filter(image, imageTmp);
	
	// first pass, let's get rid of the background from the left
	
	int[] colorBlack = new int[]{0, 0, 0};
	for(int j = 0; j < imageTmp.getWidth(); j++) {
		for(int i = 0; i < imageTmp.getHeight(); i ++) {
			Color c = new Color(imageTmp.getRGB(j, i));
			if(c.getRed() < threshold && c.getGreen() < threshold && c.getBlue() < threshold) {
				ImageUtils.setRGB(imageOut, j, i, 1, 1, colorBlack);
			}
			else {
				break;
			}
		}
	}
	
	//now let's do it from the right
	for(int j = 0; j < imageTmp.getWidth(); j++) {
		for(int i = imageTmp.getHeight() -1 ; i >= 0; i --) {
			Color c = new Color(imageTmp.getRGB(j, i));
			if(c.getRed() < threshold && c.getGreen() < threshold && c.getBlue() < threshold) {
				ImageUtils.setRGB(imageOut, j, i, 1, 1, colorBlack);
			}
			else {
				break;
			}
		}
	}
	
	return imageOut;
}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	
	
	
	

}
