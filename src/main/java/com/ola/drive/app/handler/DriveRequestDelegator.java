package com.ola.drive.app.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.ola.drive.app.dao.RideRequestStore;
import com.ola.drive.app.model.OlaDriver;
import com.ola.drive.app.model.RideMessage;
import com.ola.drive.app.model.RideRequest;

/**
 * Singleton Controller class which facilitates the publish of ride request to 
 * request queue which will be taken by one among the five drivers 
 * 
 * Serves as the producer offering the incoming request to the blocking queue for which
 * all the available drivers are consumers
 * 
 * Maintains the list of available drivers for real-time updates
 * 
 * @author aarishramesh
 *
 */
public class DriveRequestDelegator {
	private static final DriveRequestDelegator INSTANCE = new DriveRequestDelegator();
	private static final List<OlaDriver> olaDriversList = new ArrayList<OlaDriver>();
	private static boolean initDone;

	private BlockingQueue<RideMessage> rideRequestQueue = new ArrayBlockingQueue<RideMessage>(1000);

	public void initDrivers(int noOfDrivers) {
		try {
			for (int i = 0; i < noOfDrivers; i++) {
				int driverId = i + 1;
				OlaDriver driver = new OlaDriver(driverId, rideRequestQueue);
				new Thread(driver).start();
				olaDriversList.add(driver);
			}
			if (rideRequestQueue.isEmpty()) {
				// Try loading all the non-handled requests from database
				List<RideRequest> waitingRideRequests = RideRequestStore.getInstance().getWaitingRideRequests();
				for (RideRequest waitingRequest : waitingRideRequests) {
					RideMessage msg = new RideMessage(waitingRequest.getRequestId(), waitingRequest.getCustomerId());
					rideRequestQueue.offer(msg);
				}
			}
		} catch (SQLException sq) {
			sq.printStackTrace();
		}
	}

	public static DriveRequestDelegator getInstance() {
		return INSTANCE;
	}

	public RideRequest addCustomerRideRequest(long customerId) {
		RideRequest rideRequest = null;
		try {
			if (!initDone) {
				initDrivers(5);
				initDone = true;
			}

			// Add request to database
			rideRequest = RideRequestStore.getInstance().addRideRequestFromCustomer(customerId);

			if (rideRequest != null) {
				RideMessage msg = new RideMessage(rideRequest.getRequestId(), customerId);
				rideRequestQueue.offer(msg);
			} else {
				throw new Exception("Internal server error. Ride request not added");
			}
			return rideRequest;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return rideRequest;
	}
}
