package com.villagomezdiaz.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
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
import com.villagomezdiaz.common.results.JedisBlueCorrelationResults;
import com.villagomezdiaz.common.results.JedisCorrelationResults;
import com.villagomezdiaz.common.results.JedisCyanCorrelationResults;
import com.villagomezdiaz.common.results.JedisGreenCorrelationResults;
import com.villagomezdiaz.common.results.JedisHighCorrelationResults;
import com.villagomezdiaz.common.results.JedisLowCorrelationResults;
import com.villagomezdiaz.common.results.JedisMagentaCorrelationResults;
import com.villagomezdiaz.common.results.JedisRedCorrelationResults;
import com.villagomezdiaz.common.results.JedisThreeColorCorrelationResults;
import com.villagomezdiaz.common.results.JedisYellowCorrelationResults;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ZParams;

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
		
	
	public ImageComparer() {
		super();
		// TODO Auto-generated constructor stub
	}


	public BirdMatchingResults getMatchingResults (String inputBird) {
		
		BirdMatchingResults results = new BirdMatchingResults();
		
		try {
		Jedis jedis = new Jedis("localhost");
		File input = new File(inputBird);
		String fname1 = input.getName();
		File cloneOutput = new File(outputSystemPath + fname1);
		BufferedImage imageIn = ImageIO.read(input);
		
		 // copy the image
		 Files.copy(input.toPath(), cloneOutput.toPath(), 
				 java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		 
		 //using PosixFilePermission to set file permissions 755
//	     FilePermissions.setFilePermissions(cloneOutput.toPath());
//	        		        
//		 cloneOutput.setReadable(true, true);
//		 cloneOutput.setExecutable(true, true);
		 
		 System.out.println("input: " + outputHttpPath + fname1);
		 
		 results = getMatchingResults(imageIn, jedis, fname1);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return results;
		
	}
	
	public BirdMatchingResults getMatchingResults(BufferedImage imageIn, 
			Jedis jedis, String filename) throws Exception {
		
		 BirdMatchingResults results = new BirdMatchingResults();		
			 
		 ColorBlackBackgroundFilter filter = new ColorBlackBackgroundFilter(50);
		 BufferedImage imageTmp = filter.imageConvertToBlackBackgroundFromAll(imageIn);
		 
		 //write temp image
		 int newHeight = imageTmp.getHeight()*2/3;
		 BufferedImage imageTmp1 = ImageUtils.getSubimage(imageTmp, 0, 0, imageTmp.getWidth(), newHeight);
		 ImageStatistics inputStat = new ImageStatistics(imageTmp1);
		 //System.out.println(inputStat.getTopRedsAsString() + " " + inputStat.getTopGreensAsString() + " " +
		//		 inputStat.getTopBluesAsString());
		 ImageIO.write(imageTmp1, "jpg", new File(outputSystemPath + "T" + filename));
		 
		 Long numSpecies = jedis.scard("birds");
		 
		 //let's create the filters
		 ArrayList<JedisCorrelationResults> filters = initFilters();
		 			
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

			 for( JedisCorrelationResults jf : filters) {
				 if(jf.getCorrelation(corr) >= threshold) {
					 jedis.zadd(jf.getFilterName(), jf.getCorrelation(corr),list1.get(i));
				 }
		     }
		 }
	     
	     // let's get the top 10 for each, remove the rest
	     	    
	     for( JedisCorrelationResults jf : filters) {
	    	 jedis.zremrangeByRank(jf.getFilterName(), 0, -maxMatches + 1);
	    	 //jedis.zunionstore("all-corr",z,jf.getFilterName());
	     }
	     
	     
	     //can't do unionstore in the for loop to aggregate, we need to do it here, let's get the max
	     ZParams z = new ZParams();
	     z.aggregate(ZParams.Aggregate.MAX);
	     jedis.zunionstore("all-corr", z, "threecolor-corr", "high-corr", "low-corr", "red-corr", "green-corr",
	    		 "blue-corr","yellow-corr", "cyan-corr", "magenta-corr");
	     
	     // let's print out the unique ones and its score
	     Set<String> qq = jedis.zrevrange("all-corr", 0, -1);
	     Set<BirdsResults> bResults = new HashSet<BirdsResults>();
	     for(String s : qq) {
	    	 Set<FilterResults> fResults = new HashSet<FilterResults>();
	    	 System.out.println(s + " all-corr " + jedis.zscore("all-corr", s));
	    	 for( JedisCorrelationResults jf : filters) {
	    		 if(jedis.zscore(jf.getFilterName(),s) != null) {
	    			 FilterResults fr = new FilterResults(jf.getClientFilterName(), jedis.zscore(jf.getFilterName(),s));
	    			 fResults.add(fr);
	    			 System.out.println("\t" + s + " " + jf.getClientFilterName() + " " 
	    					 + jedis.zscore(jf.getFilterName(), s));
	    		 }
	    	 }
	    	 
	    	 int p = s.indexOf(".");
	    	 int p1 = s.indexOf("/");
	    	 String birdName = s.substring(p + 1, p1).replaceAll("_", " ");
	    	 BirdsResults br = new BirdsResults(birdName, s, jedis.zscore("all-corr", s), fResults);
	    	 bResults.add(br);
	     }
	     
	     results.setNumSpecies(numSpecies);
	     results.setNumBirds(numImages);
	     results.setbResults(bResults);
	     
	     // let's remove the temporary keys
	     for( JedisCorrelationResults jf : filters) {
	    	 jedis.del(jf.getFilterName());
	     }
	     jedis.del("all-corr");

		return results;
		
	}

	private ArrayList<JedisCorrelationResults> initFilters() {
		ArrayList<JedisCorrelationResults> correlationResults = new ArrayList<JedisCorrelationResults>();
		 
		 correlationResults.add(new JedisThreeColorCorrelationResults("threecolor-corr"));

		 correlationResults.add(new JedisLowCorrelationResults("low-corr"));
		 
		 correlationResults.add(new JedisHighCorrelationResults("high-corr"));

		 correlationResults.add(new JedisRedCorrelationResults("red-corr"));

		 correlationResults.add(new JedisGreenCorrelationResults("green-corr"));

		 correlationResults.add(new JedisBlueCorrelationResults("blue-corr"));

		 correlationResults.add(new JedisYellowCorrelationResults("yellow-corr"));

		 correlationResults.add(new JedisCyanCorrelationResults("cyan-corr"));

		 correlationResults.add(new JedisMagentaCorrelationResults("magenta-corr"));
		 
		 return correlationResults;
	}
}

