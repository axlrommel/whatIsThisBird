package com.villagomezdiaz.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.villagomezdiaz.common.ds.BirdAndSource;

@WebServlet(name="/JSONServlet2") 
public class JSONServlet2 extends HttpServlet {
 
    private static final long serialVersionUID = 1L;
 
 
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
 
        // 2. initiate jackson mapper
        ObjectMapper mapper = new ObjectMapper();
 
 
        // 4. Set response type to JSON
        response.setContentType("application/json");            
 
        
        mapper.writeValue(response.getOutputStream(), getBirdsAndSources());
        
	}
    
    private BirdAndSource getBirdsAndSources() {
    	
    	BirdAndSource result = new BirdAndSource("http://www.caltech.com","cardinal");
    	
    	return result;
    		
    	
    }

}
