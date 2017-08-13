package com.ola.drive.app.dao;

import com.ola.drive.app.model.RideRequest;

/**
 * DAO layer class for accessing RideRequest database
 * 
 * @author aarishramesh
 *
 */
public class RideRequestStore {
	private static final RideRequestStore INSTANCE = new RideRequestStore();
	
	public static RideRequestStore getInstance() {
		return INSTANCE;
	}
	
	public RideRequest addRideRequestFromCustomer(long customerId) {
		// Logic to insert ride request into database
		
		RideRequest rideRequest = new RideRequest.RideRequestBuilder().setCustomerId(customerId)
				.setRequestTime(System.currentTimeMillis())
				.build();
		return rideRequest;
	}
	
	public void addDriverToRideRequest(int driverId) {
		// update driverId and status
	}
	
	public void markRideRequestComplete(long requestId) {
		// update status to done
	}
}
