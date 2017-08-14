package com.ola.drive.app.handler;

import java.util.ArrayList;
import java.util.List;

import com.ola.drive.app.model.OlaDriver;

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
	private final List<OlaDriver> olaDriversList = new ArrayList<OlaDriver>();
	private static boolean initDone;

	public void initDrivers(int noOfDrivers) {
		for (int i = 0; i < noOfDrivers; i++) {
			int driverId = i + 1;
			OlaDriver driver = new OlaDriver(driverId);
			new Thread(driver).start();
			olaDriversList.add(driver);
		}
	}

	public static DriveRequestDelegator getInstance() {
		return INSTANCE;
	}

	public boolean addCustomerRideRequestForDriver(int requestId, long customerId, int driverId) {
		try {
			if (!initDone) {
				initDrivers(5);
				initDone = true;
			}

			OlaDriver driver = this.olaDriversList.get(driverId - 1);
			return driver.addRideToQueue(requestId, customerId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
}
