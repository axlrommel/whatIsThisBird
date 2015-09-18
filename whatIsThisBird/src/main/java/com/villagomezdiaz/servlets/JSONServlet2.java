package com.villagomezdiaz.servlets;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;
import com.villagomezdiaz.common.ds.BirdAndSource;
import com.villagomezdiaz.utilities.ImageComparer;

@WebServlet(name="/JSONServlet2") 
public class JSONServlet2 extends HttpServlet {
 
    private static final long serialVersionUID = 1L;
 
 
    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
    	
    	BufferedReader br = req.getReader();
    	String line = br.readLine();
    	String encodedImage = null;
    	ImageComparer comparer = new ImageComparer();
    	BufferedImage image = null;
    	// string=data%3Aimage%2Fjpeg%3Bbase64%2C%2F9j%2F4AAQSkZJRgA
    	// how it should be:
    	// "data:image/jpeg;base64,/9j/4AAQSkZ..."
    	
    	String[] sp = line.split("&",4);
    	if(sp.length == 4) { //let's get the payload
    		String[] sp1 = sp[3].split("=", 2);
    		String d1 = new String(java.net.URLDecoder.decode(sp1[1], "UTF-8"));
    		int index = d1.indexOf(",");
    		encodedImage = d1.substring(index + 1);
    		PrintWriter out = new PrintWriter("/home/rommel/Downloads/file1.html");
    		out.println("<!DOCTYPE html>\n<html>\n<body>\n<h1>Heading</h1>\n<p>Paragraph.</p>");
    		out.println("<img src=\"" + d1 + "\">");
    		out.println("</body>\n</html>");
    		out.close();
    		
    	}
    	
    	if( encodedImage != null) {
    		byte[] byteArray = Base64.decodeBase64(encodedImage);
    		InputStream in = new ByteArrayInputStream(byteArray);
    		image = ImageIO.read(in);
    		//ImageIO.write(image, "jpg", new File("/home/rommel/Downloads/file1.jpeg"));

    	}
    	
    	
        /* other method
    	int len = req.getContentLength();
    	ServletInputStream is = req.getInputStream();
    	OutputStream os = new FileOutputStream(new File("/home/rommel/Downloads/file1.jpg"));
    	int read = 0;
		byte[] bytes = new byte[len];

		while ((read = is.read(bytes)) != -1) {
			os.write(bytes, 0, read);
		}
    	os.close();
    	is.close();
    	
    	*/
    	
    	response.setContentType("application/json");
		response.setHeader("Cache-Control", "nocache");
        response.setCharacterEncoding("utf-8");

		Gson gson = new Gson();
        response.getWriter().write(gson.toJson(
        		comparer.getMatchingResults(
        				image, "newOne.jpg")));
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
 
    	response.setContentType("application/json");
		response.setHeader("Cache-Control", "nocache");
        response.setCharacterEncoding("utf-8");

		Gson gson = new Gson();
        response.getWriter().write(gson.toJson(getBirdsAndSources()));
        
	}
    
    private BirdAndSource getBirdsAndSources() {
    	
    	BirdAndSource result = new BirdAndSource("http://www.caltech.com","cardinal");
    	
    	return result;
    		
    	
    }

}
