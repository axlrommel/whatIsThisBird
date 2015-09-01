package com.villagomezdiaz.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import com.villagomezdiaz.common.DirectoryScanner;
import com.villagomezdiaz.common.ImageStatistics;

import redis.clients.jedis.Jedis;

public class JedisImageListPopulate {

	private static String imageDir = "/opt/blackendImagesAll";
	
	public static void main(String[] args) {
		//Connecting to Redis server on localhost
	      Jedis jedis = new Jedis("localhost");
	      System.out.println("Connection to server sucessful");
	   // scan dir
	      try {
			 DirectoryScanner dirScanner = new DirectoryScanner();
			 dirScanner.setDirectoryToSearch(imageDir);
			 dirScanner.setFileTypeToSearch("jpg");
			 dirScanner.searchDirectory();
			 List<File> list = dirScanner.getResult();
			 for(File f:list)
			 {
				 int pos = f.getParent().lastIndexOf("/");
				 String dir = f.getParent().substring(pos + 1);
				 String fname = dir + "/" + f.getName();
				 int p = fname.indexOf("/");
		    	 int p1 = fname.indexOf(".");
		    	 String bird = fname.substring(p1 + 1, p).replaceAll("_", " ");
		    	 
		    	 jedis.sadd("birds", bird);
				 jedis.rpush("images", fname);
				 String rName = "r-" + fname;
				 String gName = "g-" + fname;
				 String bName = "b-" + fname;
				 BufferedImage comp1 = ImageIO.read(f);
				 ImageStatistics fStat = new ImageStatistics(comp1);
				 double[] r = fStat.getReds();
				 double[] g = fStat.getGreens();
				 double[] b = fStat.getBlues();
				 for(int i = 0; i < 255; i++) {
					 
					 jedis.rpush(rName, String.valueOf(r[i]));
					 jedis.rpush(gName, String.valueOf(g[i]));
					 jedis.rpush(bName, String.valueOf(b[i]));
					 
//					 jedis.rpush("r" + rName, String.valueOf(r[i]));
//					 byte[] rqb = ByteBuffer.allocate(Double.BYTES).putDouble(r[i]).array();
//					 jedis.rpush(rName.getBytes(),rqb);
//					 
//					 jedis.rpush("g" + gName, String.valueOf(g[i]));
//					 byte[] gqb = ByteBuffer.allocate(Double.BYTES).putDouble(g[i]).array();
//					 jedis.rpush(gName.getBytes(),gqb);
//					 
//					 jedis.rpush("b" + bName, String.valueOf(b[i]));
//					 byte[] bqb = ByteBuffer.allocate(Double.BYTES).putDouble(b[i]).array();
//					 jedis.rpush(bName.getBytes(),bqb);
					 
				 }
				 
			 }
			 
	      
	     // Get the stored data and print it
			/*List<String> list1 = jedis.lrange("images", 0, 5);
			for (int i = 0; i < list1.size(); i++) {
				System.out.println("Stored string in redis:: " + list1.get(i));
				String redList = "r-" + list1.get(i);

				// List<byte[]> list2 = jedis.lrange(redList.getBytes(),0,5);
				List<String> list2 = jedis.lrange(redList, 0, 5);
				for (int j = 0; j < list2.size(); j++) {
					System.out.println(list2.get(j));
					// System.out.println(ByteBuffer.wrap(list2.get(j)).getDouble());
					// + " " + list3.get(j));
				}

			}*/
	     
	     
	      }
	      catch (Exception e) {
	    	  e.printStackTrace();
	      }
	      
	      finally {
	    	  jedis.close();
	      }

	}

}
