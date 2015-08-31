package com.villagomezdiaz.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import com.jhlabs.image.ImageUtils;
import com.villagomezdiaz.common.ColorBlackBackgroundFilter;
import com.villagomezdiaz.common.DirectoryScanner;

public class BackgroundBlacker {

	private static String pathIn = "/opt/croppedImages";
	private static String pathOut = "/opt/backBlack";
	
	public static void main(String[] args) {
		final int almostBlack = 50;
		try {
			DirectoryScanner dirScanner = new DirectoryScanner();
			 dirScanner.setDirectoryToSearch(pathIn);
			 dirScanner.setFileTypeToSearch("jpg");
			 dirScanner.searchDirectory();
			 ColorBlackBackgroundFilter filter = new ColorBlackBackgroundFilter(almostBlack);
			 List<File> list = dirScanner.getResult();
			 for(File f:list)
			 {
				 String inputPath = f.getAbsolutePath();
				 BufferedImage imageIn = ImageIO.read(f);
				 
				 BufferedImage imageTmp = filter.imageConvertToBlackBackgroundFromAll(imageIn);
				 
				 int newHeight = (int)(imageIn.getHeight()*2/3);
				 BufferedImage imageOut = ImageUtils.getSubimage(imageTmp, 0, 0, imageIn.getWidth(), newHeight);
				 
				 String outputPath = pathOut + inputPath.substring(pathIn.length());
				 File output = new File(outputPath);
				 String dirPath = output.getParent();
				 File dir = new File(dirPath);
				 dir.mkdirs();
				 ImageIO.write(imageOut, "jpg", output);
				 
				 
			 }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			
		}
		// TODO Auto-generated method stub

	}

}
