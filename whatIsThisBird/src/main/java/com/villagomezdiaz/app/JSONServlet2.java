package com.villagomezdiaz.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.villagomezdiaz.common.ds.BirdAndSource;
import com.villagomezdiaz.common.ds.BirdsAndSources;

 
public class JSONServlet2 extends HttpServlet {
 
    private static final long serialVersionUID = 1L;
 
 
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
 
        // 2. initiate jackson mapper
        ObjectMapper mapper = new ObjectMapper();
 
 
        // 4. Set response type to JSON
        response.setContentType("application/json");            
 
        
        String buttonID = request.getParameter("button-id");
        switch (buttonID) {
        case "info":
        	mapper.writeValue(response.getOutputStream(), getBirdsAndSources());
        	break;
        }
	}
    
    private BirdAndSource getBirdsAndSources() {
    	
    	BirdAndSource result = new BirdAndSource("http://www.caltech.com","cardinal");
    	
    	return result;
    		
    	
    }

}
