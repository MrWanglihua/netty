package com.gupaoedu.vip.bio.tomcat.servlet;


import com.gupaoedu.vip.bio.tomcat.http.GPRequest;
import com.gupaoedu.vip.bio.tomcat.http.GPResponse;
import com.gupaoedu.vip.bio.tomcat.http.GPServlet;

public class FirstServlet extends GPServlet {

	public void doGet(GPRequest request, GPResponse response) throws Exception {
		this.doPost(request, response);
	}

	public void doPost(GPRequest request, GPResponse response) throws Exception {
		response.write("This is First Serlvet");
	}

}
