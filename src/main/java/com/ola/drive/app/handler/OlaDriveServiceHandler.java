package com.ola.drive.app.handler;

import java.util.List;

import com.ola.drive.app.dao.RideRequestStore;
import com.ola.drive.app.model.RideRequest;
import com.ola.drive.app.model.response.ApiResponse;
import com.ola.drive.app.model.response.Error;

public class OlaDriveServiceHandler {
	private static final OlaDriveServiceHandler INSTANCE = new OlaDriveServiceHandler();

	public static OlaDriveServiceHandler getInstance() {
		return INSTANCE;
	}

	public ApiResponse addCustomerRideRequest(long customerId) {

		// Check if the request has already been picked up
		ApiResponse response = new ApiResponse();
		try {
			boolean isThereAOngoingRide = RideRequestStore.getInstance().checkThereIsNoOngoingRidesForCustomer(customerId);
			if (!isThereAOngoingRide) {
				RideRequest rideRequest = DriveRequestDelegator.getInstance().addCustomerRideRequest(customerId);
				if (rideRequest != null) {
					response.setData(rideRequest);
				} else {
					Error error = new Error("500", "Internal Server error while adding ride request");
					response.setError(error);
				}
			}
		} catch (Exception ex) {
			Error error = new Error("500", "Internal Server error");
			response.setError(error);
		}
		return response;
	}

	public ApiResponse getDriverRidesView(int driverId) {
		ApiResponse response = new ApiResponse();
		try {
			List<RideRequest> rideRequestsDriverView = RideRequestStore.getInstance().getRideRequestsForDriverWithWaitingRequests(driverId);
			response.setData(rideRequestsDriverView);
		} catch (Exception ex) {
			Error error = new Error("500", "Internal Server error while fetching rides for driver");
			response.setError(error);
		}
		return response;
	}

	public ApiResponse getDashboardRidesView() {
		ApiResponse response = new ApiResponse();
		try {
			List<RideRequest> rideRequestsDriverView = RideRequestStore.getInstance().getAllRideRequests();
			response.setData(rideRequestsDriverView);
		} catch (Exception ex) {
			Error error = new Error("500", "Internal Server error while fetching rides for driver");
			response.setError(error);
		}
		return response;
	}
}
