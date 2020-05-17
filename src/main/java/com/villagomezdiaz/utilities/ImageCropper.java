package com.villagomezdiaz.utilities;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import com.villagomezdiaz.common.DirectoryScanner;

public class ImageCropper {

	private static String pathIn = "/opt/images";
	private static String pathOut = "/opt/croppedImages";
	private static String f1 = "/home/rommel/CUB_200_2011/bounding_boxes.txt";
	private static String f2 = "/home/rommel/CUB_200_2011/images.txt";
	
	public static void main(String[] args) {
		
		try{
			 
			File input = new File(f2);
			BufferedReader reader = new BufferedReader(new FileReader(input));
		    String text = null;
		    HashMap<String, String> mImages = new HashMap<String,String>();

		    while ((text = reader.readLine()) != null) {
		    	String[] values = text.split(" ");
		        if(values.length > 1) {
		        	mImages.put(values[1], values[0]);
		        }
		    }
		    reader.close();

		    input = new File(f1);
		    reader = new BufferedReader(new FileReader(input));
		    HashMap<String, String> mBoxes = new HashMap<String,String>();
		    while ((text = reader.readLine()) != null) {
		    	String[] values = text.split(" ");
		    	
		        if(values.length == 5) {
		        	int x0 = (int) Double.parseDouble(values[1]);
		        	int y0 = (int) Double.parseDouble(values[2]);
		        	int w = (int) Double.parseDouble(values[3]);
		        	int h = (int) Double.parseDouble(values[4]);
		        	
		        	StringBuffer strBuf = new StringBuffer();
		        	strBuf.append(x0);
		        	strBuf.append("|");
		        	strBuf.append(y0);
		        	strBuf.append("|");
		        	strBuf.append(w);
		        	strBuf.append("|");
		        	strBuf.append(h);
		        	
		        	//System.out.println(values[0] + " " + strBuf.toString());
		        	mBoxes.put(values[0], strBuf.toString());
		        }
		    }
		    reader.close();
		    
		    
		    
			 DirectoryScanner dirScanner = new DirectoryScanner();
			 dirScanner.setDirectoryToSearch(pathIn);
			 dirScanner.setFileTypeToSearch("jpg");
			 dirScanner.searchDirectory();
			 List<File> list = dirScanner.getResult();
			 for(File f:list)
			 {
				 String inputPath = f.getAbsolutePath();
				 String imageFName = inputPath.substring(pathIn.length() + 1);
				 String outputPath = pathOut + inputPath.substring(pathIn.length());
				 File output = new File(outputPath);
				 System.out.println(outputPath);
				 
				 String key = (String)mImages.get(imageFName);
				 String box = (String)mBoxes.get(key);
				 String[] values = box.split("\\|");
				 if(values.length == 4) {
					 int x = Integer.parseInt(values[0]);
					 int y = Integer.parseInt(values[1]);
					 int w = Integer.parseInt(values[2]);
					 int h = Integer.parseInt(values[3]);
					 
					 BufferedImage imageIn = ImageIO.read(f);
					 
					 // in case coordinates are incorrect and we are off the picture
					 if(imageIn.getWidth() < x + w) {
						 w = imageIn.getWidth() - x;
					 }
					 if(imageIn.getHeight() < y + h) {
						 h = imageIn.getHeight() - y;
					 }
					 
					 BufferedImage imageOut = imageIn.getSubimage(x, y, w, h);
																 				 
					 String dirPath = output.getParent();
					 File dir = new File(dirPath);
					 dir.mkdirs();
					 ImageIO.write(imageOut, "jpg", output);
					 
					 
				 }
			 }
		}
		catch (Exception e) {
			e.printStackTrace();
		}				 

	}

}
