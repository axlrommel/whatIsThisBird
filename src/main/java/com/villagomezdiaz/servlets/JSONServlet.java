package com.villagomezdiaz.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.villagomezdiaz.common.BirdLookupRequest;
import com.villagomezdiaz.common.ds.BirdMatchingResults;
import com.villagomezdiaz.utilities.ImageComparer;

 
public class JSONServlet extends HttpServlet {
 
    private static final long serialVersionUID = 1L;
 
 
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
    	// 1. get received JSON data from request
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
        if(br != null){
            json = br.readLine();
        }
 
        // 2. initiate jackson mapper
        ObjectMapper mapper = new ObjectMapper();
 
        // 3. Convert received JSON to BirdLookupRequest
        BirdLookupRequest req = mapper.readValue(json, BirdLookupRequest.class);
 
        // 4. Set response type to JSON
        response.setContentType("application/json");            
 
        // 5. create object to return
        ImageComparer ic = new ImageComparer();
        BirdMatchingResults bmr = ic.getMatchingResults(req.getPath());

 
        // 6. Send BirdMatchingResults as JSON to client
        mapper.writeValue(response.getOutputStream(), bmr);
	}

}
