package com.ola.drive.app.service;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import com.google.gson.Gson;
import com.ola.drive.app.handler.OlaDriveServiceHandler;
import com.ola.drive.model.response.ApiResponse;

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
		
		post ("ola-drive/customer/:id/ride", (request, response) -> {
			response.type("application/json");
			String customerIdStr = request.params(":id");
			int customerId = Integer.parseInt(customerIdStr);
			ApiResponse apiResponse = OlaDriveServiceHandler.getInstance().addCustomerRideRequest(customerId);
			Gson gson = new Gson ();
			return gson.toJson(apiResponse);
		});
		
		get("ola-drive/driver/:id/ride", (request, response) -> {
			response.type("application/json");
			String driverIdStr = request.params(":id");
			int driverId = Integer.parseInt(driverIdStr);
			ApiResponse apiResponse = OlaDriveServiceHandler.getInstance().getDriverRidesView(driverId);
			Gson gson = new Gson ();
			return gson.toJson(apiResponse);
		});
		
		get("ola-drive/ride/dashboard", (request, response) -> {
			response.type("application/json");
			ApiResponse apiResponse = OlaDriveServiceHandler.getInstance().getDashboardRidesView();
			Gson gson = new Gson ();
			return gson.toJson(apiResponse);
		});
	}
}
