package com.ola.drive.app.service;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import com.google.gson.Gson;
import com.ola.drive.app.handler.OlaDriveServiceHandler;
import com.ola.drive.app.model.DriveRequest;
import com.ola.drive.app.model.response.ApiResponse;

import spark.Spark;

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
		Spark.staticFileLocation("/public");
		port(7777);
		
		get ("ola-drive/ping", (request, response) -> {
			return "pong";
		});
		
		post ("ola-drive/customer/ride", (request, response) -> {
			response.type("application/json");
			DriveRequest requestBody = new Gson().fromJson (request.body (), DriveRequest.class);
			long customerId = requestBody.getCustomerId();
			System.out.println("-------- Customer Id :: " + customerId);
			ApiResponse apiResponse = OlaDriveServiceHandler.getInstance().addCustomerRideRequest(customerId);
			Gson gson = new Gson ();
			return gson.toJson(apiResponse);
		});
		
		post ("ola-drive/driver/ride", (request, response) -> {
			response.type("application/json");
			DriveRequest requestBody = new Gson().fromJson (request.body (), DriveRequest.class);
			long customerId = requestBody.getCustomerId();
			int driverId = requestBody.getDriverId();
			int requestId = requestBody.getRequestId();
			System.out.println("-------- Customer Id :: " + customerId + " :: DriverId :: " + driverId
					+ " :: RequestId :: " + requestId);
			ApiResponse apiResponse = OlaDriveServiceHandler.getInstance().addDriverRideRequestForCustomer(requestId, customerId, driverId);
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
