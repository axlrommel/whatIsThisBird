package com.villagomezdiaz.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import com.jhlabs.image.ImageUtils;
import com.villagomezdiaz.common.ColorBlackBackgroundFilter;
import com.villagomezdiaz.common.ImageCorrelation;
import com.villagomezdiaz.common.ImageStatistics;
import com.villagomezdiaz.common.ds.BirdMatchingResults;
import com.villagomezdiaz.common.ds.BirdsResults;
import com.villagomezdiaz.common.ds.FilterResults;

import redis.clients.jedis.Jedis;

public class ImageComparer {

	static PrintWriter writer = null;
	static final int maxMatches = 10;
	static final double threshold = 0.60;
	private static String outputHttpPath = "http://localhost/images/input/";
	private static String outputSystemPath = "/var/www/html/images/input/";
	
	public static void main(String[] args) {
		ImageComparer ic = new ImageComparer();
		BirdMatchingResults bc = ic.getMatchingResults(args[0]);
		System.out.println(bc.toString());
	}
		
	
	public BirdMatchingResults getMatchingResults(String inputBird) {
		
		BirdMatchingResults results = new BirdMatchingResults();
		Jedis jedis = new Jedis("localhost");
				
		try {
					
			File input = new File(inputBird);
			String fname1 = input.getName();
			File cloneOutput = new File(outputSystemPath + fname1);
			
			 BufferedImage imageIn = ImageIO.read(input);
			 
			 // copy the image
			 Files.copy(input.toPath(), cloneOutput.toPath(), 
					 java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			 
			 //using PosixFilePermission to set file permissions 755
//		     FilePermissions.setFilePermissions(cloneOutput.toPath());
//		        		        
//			 cloneOutput.setReadable(true, true);
//			 cloneOutput.setExecutable(true, true);
			 
			 System.out.println("input: " + outputHttpPath + fname1);
			 
			 ColorBlackBackgroundFilter filter = new ColorBlackBackgroundFilter(50);
			 BufferedImage imageTmp = filter.imageConvertToBlackBackgroundFromAll(imageIn);
			 
			 //write temp image
			 int newHeight = imageTmp.getHeight()*2/3;
			 BufferedImage imageTmp1 = ImageUtils.getSubimage(imageTmp, 0, 0, imageTmp.getWidth(), newHeight);
			 ImageStatistics inputStat = new ImageStatistics(imageTmp1);
			 //System.out.println(inputStat.getTopRedsAsString() + " " + inputStat.getTopGreensAsString() + " " +
			//		 inputStat.getTopBluesAsString());
			 File output = new File(outputSystemPath + "T" + fname1);
			 ImageIO.write(imageTmp1, "jpg", output);
			 
			 Long numSpecies = jedis.scard("birds");
				
			 List<String> list1 = jedis.lrange("images", 0 ,-1);
			 int numImages = list1.size();
		     for(int i=0; i<list1.size(); i++) {
		    	 String redList = "r-" + list1.get(i);
		    	 String greenList = "g-" + list1.get(i);
		    	 String blueList = "b-" + list1.get(i);
		    	 
		    	 List<String> listR = jedis.lrange(redList, 0,-1);
		    	 List<String> listG = jedis.lrange(greenList, 0,-1);
		    	 List<String> listB = jedis.lrange(blueList, 0,-1);
		    	 
				 ImageStatistics fStat = new ImageStatistics(listR.toArray(new String[listR.size()]),
						 listG.toArray(new String[listG.size()]),
						 listB.toArray(new String[listB.size()]));
				 
				 ImageCorrelation corr = new ImageCorrelation(inputStat,fStat);

				 if(corr.getThreeColorCorrelation() > threshold) {
					 jedis.zadd("threecolor-corr", corr.getThreeColorCorrelation(), list1.get(i));
				 }
				 
				 if(corr.getLowColorCorrelation() > threshold) {
					 jedis.zadd("low-corr", corr.getLowColorCorrelation(), list1.get(i));
				 }
				 
				 if(corr.getHighColorCorrelation() > threshold) {
					 jedis.zadd("high-corr", corr.getHighColorCorrelation(), list1.get(i));
				 }
				 
				 if(corr.getRedColorCorrelation() > threshold) {
					 jedis.zadd("red-corr", corr.getRedColorCorrelation(), list1.get(i));
				 }
				 
				 if(corr.getGreenColorCorrelation() > threshold) {
					 jedis.zadd("green-corr", corr.getGreenColorCorrelation(), list1.get(i));
				 }
				 
				 if(corr.getBlueColorCorrelation() > threshold) {
					 jedis.zadd("blue-corr", corr.getBlueColorCorrelation(), list1.get(i));
				 }
				 
				 if(corr.getYellowColorCorrelation() > threshold) {
					 jedis.zadd("yellow-corr", corr.getYellowColorCorrelation(), list1.get(i));
				 }
				 
				 if(corr.getCyanColorCorrelation() > threshold) {
					 jedis.zadd("cyan-corr", corr.getCyanColorCorrelation(), list1.get(i));
				 }
				 
				 if(corr.getMagentaColorCorrelation() > threshold) {
					 jedis.zadd("magenta-corr", corr.getMagentaColorCorrelation(), list1.get(i));
				 }
			 }
		     
		     // let's get the top 6 for each, remove the rest
		     jedis.zremrangeByRank("threecolor-corr", 0, -maxMatches + 1);
		     jedis.zremrangeByRank("low-corr", 0, -maxMatches + 1);
		     jedis.zremrangeByRank("high-corr", 0,  -maxMatches + 1);
		     jedis.zremrangeByRank("red-corr", 0,  -maxMatches + 1);
		     jedis.zremrangeByRank("green-corr", 0,  -maxMatches + 1);
		     jedis.zremrangeByRank("blue-corr", 0,  -maxMatches + 1);
		     jedis.zremrangeByRank("yellow-corr", 0,  -maxMatches + 1);
		     jedis.zremrangeByRank("cyan-corr", 0,  -maxMatches + 1);
		     jedis.zremrangeByRank("magenta-corr", 0,  -maxMatches + 1);
			 
		     // union all, will add the scores if on two or more keys
		     jedis.zunionstore("all-corr", "threecolor-corr","low-corr","high-corr","red-corr","green-corr","blue-corr","yellow-corr",
		    		 "cyan-corr", "magenta-corr");
		     
		     // let's print out the unique ones and its score
		     Set<String> qq = jedis.zrevrange("all-corr", 0, -1);
		     Set<BirdsResults> bResults = new HashSet<BirdsResults>();
		     for(String s : qq) {
		    	 Set<FilterResults> fResults = new HashSet<FilterResults>();
		    	 System.out.println(s + " all-corr " + jedis.zscore("all-corr", s));
		    	 if(jedis.zscore("threecolor-corr",s) != null) {
		    		 FilterResults fr = new FilterResults("threecolor-corr",jedis.zscore("threecolor-corr", s));
		    		 fResults.add(fr);
		    		 System.out.println("\t" + s + " threecolor-corr " + jedis.zscore("threecolor-corr", s));
		    	 }
		    	 if(jedis.zscore("low-corr",s) != null) {
		    		 FilterResults fr = new FilterResults("low-corr",jedis.zscore("low-corr", s));
		    		 fResults.add(fr);
		    		 System.out.println("\t" + s + " low-corr " + jedis.zscore("low-corr", s));
		    	 }
		    	 if(jedis.zscore("high-corr",s) != null) {
		    		 FilterResults fr = new FilterResults("high-corr",jedis.zscore("high-corr", s));
		    		 fResults.add(fr);
		    		 System.out.println("\t" + s + " high-corr " + jedis.zscore("high-corr", s));
		    	 }
		    	 if(jedis.zscore("red-corr",s) != null) {
		    		 FilterResults fr = new FilterResults("red-corr",jedis.zscore("red-corr", s));
		    		 fResults.add(fr);
		    		 System.out.println("\t" + s + " red-corr " + jedis.zscore("red-corr", s));
		    	 }
		    	 if(jedis.zscore("green-corr",s) != null) {
		    		 FilterResults fr = new FilterResults("green-corr",jedis.zscore("green-corr", s));
		    		 fResults.add(fr);
		    		 System.out.println("\t" + s + " green-corr " + jedis.zscore("green-corr", s));
		    	 }
		    	 if(jedis.zscore("blue-corr",s) != null) {
		    		 FilterResults fr = new FilterResults("blue-corr",jedis.zscore("blue-corr", s));
		    		 fResults.add(fr);
		    		 System.out.println("\t" + s + " blue-corr " + jedis.zscore("blue-corr", s));
		    	 }
		    	 if(jedis.zscore("yellow-corr",s) != null) {
		    		 FilterResults fr = new FilterResults("yellow-corr",jedis.zscore("yellow-corr", s));
		    		 fResults.add(fr);
		    		 System.out.println("\t" + s + " yellow-corr " + jedis.zscore("yellow-corr", s));
		    	 }
		    	 if(jedis.zscore("cyan-corr",s) != null) {
		    		 FilterResults fr = new FilterResults("cyan-corr",jedis.zscore("cyan-corr", s));
		    		 fResults.add(fr);
		    		 System.out.println("\t" + s + " cyan-corr " + jedis.zscore("cyan-corr", s));
		    	 }
		    	 if(jedis.zscore("magenta-corr",s) != null) {
		    		 FilterResults fr = new FilterResults("magenta-corr",jedis.zscore("magenta-corr", s));
		    		 fResults.add(fr);
		    		 System.out.println("\t" + s + " magenta-corr " + jedis.zscore("magenta-corr", s));
		    	 }
		    	 
		    	 int p = s.indexOf(".");
		    	 int p1 = s.indexOf("/");
		    	 String birdName = s.substring(p + 1, p1);
		    	 String b = birdName.replaceAll("_", " ");
		    	 BirdsResults br = new BirdsResults(b, s, jedis.zscore("all-corr", s), fResults);
		    	 bResults.add(br);
		     }
		     
		     results.setNumSpecies(numSpecies);
		     results.setNumBirds(numImages);
		     results.setbResults(bResults);
		     
		     // let's remove the keys
		     jedis.del("all-corr", "threecolor-corr","low-corr","high-corr","red-corr","green-corr","blue-corr","yellow-corr",
		    		 "cyan-corr", "magenta-corr");

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			jedis.close();
		}
		return results;
		
	}

}

