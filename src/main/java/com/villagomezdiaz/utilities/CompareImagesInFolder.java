package com.villagomezdiaz.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import com.villagomezdiaz.common.DirectoryScanner;
import com.villagomezdiaz.common.ImageCorrelation;
import com.villagomezdiaz.common.ImageStatistics;

public class CompareImagesInFolder {

	
	/**
	 * 
	 * @param args
	 * args[0] = directory with images that need to be compared
	 * args[1] = results file
	 */
	public static void main(String[] args) {
		String path = args[0];
		
		PrintWriter writer = null;
		ArrayList<String> fNames = new ArrayList<String>();
		HashMap<String, Double> mImages = new HashMap<String,Double>();
		
		try {
			writer = new PrintWriter(args[1], "UTF-8");
			DirectoryScanner dirScanner = new DirectoryScanner();
			 dirScanner.setDirectoryToSearch(path);
			 dirScanner.setFileTypeToSearch("jpg");
			 dirScanner.searchDirectory();
			 List<File> list = dirScanner.getResult();
			 for(File f:list)
			 {
				 fNames.add(f.getAbsolutePath());
				 mImages.put(f.getAbsolutePath(), new Double(0));
			 }
			 
			 for(int i = 0; i < fNames.size(); i++) {
				 for(int j = 0; j < fNames.size(); j++) {
					 if(i < j) { // don't compare twice
						 File compareOne = new File(fNames.get(i));
						 File compareTwo = new File(fNames.get(j));
						 
						 BufferedImage imageOne = ImageIO.read(compareOne);
						 BufferedImage imageTwo = ImageIO.read(compareTwo);
						 ImageStatistics stat1 = new ImageStatistics(imageOne);
						 ImageStatistics stat2 = new ImageStatistics(imageTwo);
						 ImageCorrelation corr = new ImageCorrelation(stat1,stat2);
						 System.out.println(fNames.get(i) + " " + fNames.get(j));
						 Double q = (Double)mImages.get(fNames.get(i));
						 mImages.put(fNames.get(i), new Double(q.doubleValue() + 
								 corr.getOverallCorr()));
			
						 q = (Double)mImages.get(fNames.get(j));
						 mImages.put(fNames.get(j), new Double(q.doubleValue() + 
								 corr.getOverallCorr()));
					 }
				 }
			 }
			 Iterator<String> keySetIterator = mImages.keySet().iterator();

			 while(keySetIterator.hasNext()){
			   String key = keySetIterator.next();
			   writer.println(key + "|" + mImages.get(key).doubleValue());
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			writer.close();
		}
		// TODO Auto-generated method stub

	}

}
