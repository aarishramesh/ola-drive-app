package com.ola.drive.app.model;

import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.ola.drive.app.dao.RideRequestStore;

import lombok.Data;

/**
 * Model class representing OlaDriver. It is open to extension so further inherits can be made
 * based on the type of vehicle they drive
 * 
 * It is assumed from the requirements that Driver is always up for the ride
 * 
 * @author aarishramesh
 *
 */

@Data
public class OlaDriver implements Runnable {

	private int driverId;
	private boolean available;
	private static int driveServiceTimeMillis = 5 * 60 * 1000;
	
	private BlockingQueue<RideMessage> rideRequestQueue = new ArrayBlockingQueue<RideMessage>(500);
	
	public OlaDriver(int driverId, BlockingQueue<RideMessage> rideRequestQueue) {
		this.driverId = driverId;
		this.rideRequestQueue = rideRequestQueue;
	}
	
	@Override
	public void run() {
		try{
            RideMessage msg;
            //consuming messages until exit message is received
            while (true) {
            	if (available) {
            		msg = rideRequestQueue.take();
            		
            		// Update the corresponding request in database
            		RideRequestStore.getInstance().addDriverToRideRequest(msg.getRequestId(), driverId);
            		
            		Thread.sleep(driveServiceTimeMillis);
            		
            		// Update the corresponding request in database
            		RideRequestStore.getInstance().markRideRequestComplete(msg.getRequestId());

            		this.available = true;
            	} else {
            		Thread.sleep(300);
            	}
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}

}
