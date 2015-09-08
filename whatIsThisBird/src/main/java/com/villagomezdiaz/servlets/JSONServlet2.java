package com.villagomezdiaz.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.villagomezdiaz.common.ds.BirdAndSource;

@WebServlet(name="/JSONServlet2") 
public class JSONServlet2 extends HttpServlet {
 
    private static final long serialVersionUID = 1L;
 
 
    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("application/json");
		response.setHeader("Cache-Control", "nocache");
        response.setCharacterEncoding("utf-8");

		Gson gson = new Gson();
        response.getWriter().write(gson.toJson(getBirdsAndSources()));
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
