package com.villagomezdiaz.servlets;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@WebServlet(name="/InfoServlet") 
public class InfoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
    private static JedisPool pool = null;
 
 
    @Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		pool = new JedisPool(new JedisPoolConfig(),"localhost");
		
	}
    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
    	
    	response.setContentType("text/plain; charset=ISO-8859-2");
    	Jedis jedis = null;
        
        try {
        	jedis = pool.getResource();
        	StringBuffer listOfBirds = new StringBuffer();
        	Set<String> list1 = jedis.smembers("birds");

        	for(String l:list1) {
        		listOfBirds.append(l + "\n");
        	}
        	response.getWriter().write(listOfBirds.toString());
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        finally {
        	pool.returnResourceObject(jedis);
        }
    }

}
