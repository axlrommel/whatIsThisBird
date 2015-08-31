package com.villagomezdiaz.utilities;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import com.jhlabs.image.ImageUtils;
import com.villagomezdiaz.common.ColorBlackBackgroundFilter;
import com.villagomezdiaz.common.ImageCorrelation;
import com.villagomezdiaz.common.ImageStatistics;

import redis.clients.jedis.Jedis;

public class ImageComparer {

	static PrintWriter writer = null;
	static final int maxMatches = 7;
	
	public static void main(String[] args) {
		
		String outputHttpPath = "http://localhost/images/input/";
		String outputSystemPath = "/var/www/html/images/input/";
		
		Jedis jedis = new Jedis("localhost");
		
		try {
					
			File input = new File(args[0]);
			String fname1 = input.getName();
			File cloneOutput = new File(outputSystemPath + fname1);
			
			 BufferedImage imageIn = ImageIO.read(input);
			 
			 // copy the image
			 Files.copy(input.toPath(), cloneOutput.toPath(), REPLACE_EXISTING);
			 
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
			 
			 List<String> list1 = jedis.lrange("images", 0 ,-1);
		     for(int i=0; i<list1.size(); i++) {
		    	 String redList = "rr-" + list1.get(i);
		    	 String greenList = "gg-" + list1.get(i);
		    	 String blueList = "bb-" + list1.get(i);
		    	 
		    	 List<String> listR = jedis.lrange(redList, 0,-1);
		    	 List<String> listG = jedis.lrange(greenList, 0,-1);
		    	 List<String> listB = jedis.lrange(blueList, 0,-1);
		    	 
				 ImageStatistics fStat = new ImageStatistics(listR.toArray(new String[listR.size()]),
						 listG.toArray(new String[listG.size()]),
						 listB.toArray(new String[listB.size()]));
				 
				 ImageCorrelation corr = new ImageCorrelation(inputStat,fStat);

				 if(corr.getThreeColorCorrelation() > 0.6) {
					 jedis.zadd("threecolor-corr", corr.getThreeColorCorrelation(), list1.get(i));
				 }
				 
				 if(corr.getLowColorCorrelation() > 0.6) {
					 jedis.zadd("low-corr", corr.getLowColorCorrelation(), list1.get(i));
				 }
				 
				 if(corr.getHighColorCorrelation() > 0.6) {
					 jedis.zadd("high-corr", corr.getHighColorCorrelation(), list1.get(i));
				 }
				 
				 if(corr.getRedColorCorrelation() > 0.6) {
					 jedis.zadd("red-corr", corr.getRedColorCorrelation(), list1.get(i));
				 }
				 
				 if(corr.getGreenColorCorrelation() > 0.6) {
					 jedis.zadd("green-corr", corr.getGreenColorCorrelation(), list1.get(i));
				 }
				 
				 if(corr.getBlueColorCorrelation() > 0.6) {
					 jedis.zadd("blue-corr", corr.getBlueColorCorrelation(), list1.get(i));
				 }
				 
				 if(corr.getYellowColorCorrelation() > 0.6) {
					 jedis.zadd("yellow-corr", corr.getYellowColorCorrelation(), list1.get(i));
				 }
				 
				 if(corr.getCyanColorCorrelation() > 0.6) {
					 jedis.zadd("cyan-corr", corr.getCyanColorCorrelation(), list1.get(i));
				 }
				 
				 if(corr.getMagentaColorCorrelation() > 0.6) {
					 jedis.zadd("magenta-corr", corr.getMagentaColorCorrelation(), list1.get(i));
				 }
			 }
		     
		     // let's get the top 6 for each, remove the rest
		     jedis.zremrangeByRank("threecolor-corr", 0, -6);
		     jedis.zremrangeByRank("low-corr", 0, -6);
		     jedis.zremrangeByRank("high-corr", 0, -6);
		     jedis.zremrangeByRank("red-corr", 0, -6);
		     jedis.zremrangeByRank("green-corr", 0, -6);
		     jedis.zremrangeByRank("blue-corr", 0, -6);
		     jedis.zremrangeByRank("yellow-corr", 0, -6);
		     jedis.zremrangeByRank("cyan-corr", 0, -6);
		     jedis.zremrangeByRank("magenta-corr", 0, -6);
			 
		     // union all, will add the scores if on two or more keys
		     jedis.zunionstore("all-corr", "threecolor-corr","low-corr","high-corr","red-corr","green-corr","blue-corr","yellow-corr",
		    		 "cyan-corr", "magenta-corr");
		     
		     // let's print out the unique ones and its score
		     Set<String> qq = jedis.zrevrange("all-corr", 0, -1);
		     for(String s : qq) {
		    	 System.out.println(s + " " + jedis.zscore("all-corr", s));
		     }
		     
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

	}
	
	

}

