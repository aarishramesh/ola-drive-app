package com.ola.drive.app.service;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import com.google.gson.Gson;

/**
 * Application entry class for Ola Drive service which provides REST based apis for 
 * 	actions like book a ride, list all rides for a driver, list all ride requests 
 *  for dashboard
 * 
 * @author aarishramesh
 *
 */
public class OlaDriveService {
	public static void main(String[] args) {
		port(7777);
		
		get ("ola-drive/ping", (request, response) -> {
			return "pong";
		});
		
		post ("ola-drive/ride/customer/:id", (request, response) -> {
			response.type("application/json");
			
			Gson gson = new Gson ();
			return gson.toJson("");
		});
		
		get("ola-drive/driver/:id/ride", (request, response) -> {
			Gson gson = new Gson ();
			return gson.toJson("");
		});
		
		get("ola-drive/ride/dashboard", (request, response) -> {
			Gson gson = new Gson ();
			return gson.toJson("");
		});
	}
}
