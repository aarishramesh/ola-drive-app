package com.ola.drive.app.model;

import lombok.Data;

/**
 * Model class representing request to ride from the customer
 * It encompasses all the details corresponding to a ride
 * 
 * @author aarishramesh
 *
 */
@Data
public class RideRequest {
	private int requestId;
	private long customerId;
	private long requestTime;
	private String rideLocaton;
	private int status;
	private int driverId;
	private long startTime;
	private long endTime;
	
	public enum RequestStatus {
		waiting(0), ongoing(1), complete(2);
		
		private RequestStatus (int status) {
			this.status = status;
		}
		private int status;
		
		public int getStatus() {
			return this.status;
		}
	}
	
	public static class RideRequestBuilder {
		private int requestId;
		private long customerId;
		private long requestTime;
		private String rideLocaton;
		private int status;
		private int driverId;
		private long startTime;
		private long endTime;
		
		public RideRequestBuilder setRequestId(int requestId) {
			this.requestId = requestId;
			return this;
		}
		
		public RideRequestBuilder setCustomerId(long customerId) {
			this.customerId = customerId;
			return this;
		}
		
		public RideRequestBuilder setRequestTime(long requestTime) {
			this.requestTime = requestTime;
			return this;
		}
		
		public RideRequestBuilder setRideLocation(String rideLocation) {
			this.rideLocaton = rideLocation;
			return this;
		}
		
		public RideRequestBuilder setStatus(int status) {
			this.status = status;
			return this;
		}

		public RideRequestBuilder setDriverId(int driverId) {
			this.driverId = driverId;
			return this;
		}
		
		public RideRequestBuilder setStartTime(long startTime) {
			this.startTime = startTime;
			return this;
		}
		
		public RideRequestBuilder setEndTime(long endTime) {
			this.endTime = endTime;
			return this;
		}
		
		public RideRequest build() {
			RideRequest rideRequest = new RideRequest();
			rideRequest.setRequestId(this.requestId);
			rideRequest.setCustomerId(this.customerId);
			rideRequest.setRequestTime(this.requestTime);
			rideRequest.setRideLocaton(this.rideLocaton);
			rideRequest.setStatus(this.status);
			rideRequest.setDriverId(this.driverId);
			rideRequest.setStartTime(this.startTime);
			rideRequest.setEndTime(this.endTime);
			return rideRequest;
		}
	}
}
