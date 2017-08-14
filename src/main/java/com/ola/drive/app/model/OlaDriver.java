package com.ola.drive.app.model;

import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private boolean available = true;
	private static int driveServiceTimeMillis = 5 * 60 * 1000;
	
	private static final Logger LOG = LoggerFactory.getLogger(OlaDriver.class);

	private BlockingQueue<RideMessage> rideRequestQueue = new ArrayBlockingQueue<RideMessage>(1000);
	
	public OlaDriver(int driverId) {
		this.driverId = driverId;
	}
	
	public boolean addRideToQueue(int requestId, long customerId) {
		RideMessage msg = new RideMessage(requestId, customerId);
		return this.rideRequestQueue.offer(msg);
	}
	
	@Override
	public void run() {
		try{
            RideMessage msg;
            
            System.out.println("Consumer started for driver : " + this.driverId);
            LOG.info("Consumer started for driver : " + this.driverId);
            //consuming messages until exit message is received
            while (true) {
        		if (rideRequestQueue == null) {
        			throw new Exception("Error. Driver queue is null");
        		}
            	if (available) {
            		msg = rideRequestQueue.take();
            		
            		System.out.println(" Driver " + this.driverId +
            				" is servicing the request Id : " + msg.getRequestId() 
            				+ ": customerId : " + msg.getCustomerId());
            		// Update the corresponding request in database
            		RideRequestStore.getInstance().addDriverToRideRequest(msg.getRequestId(), driverId);
            		
            		this.available = false;
            		Thread.sleep(driveServiceTimeMillis);
            		
            		// Update the corresponding request in database
            		RideRequestStore.getInstance().markRideRequestComplete(msg.getRequestId());

            		this.available = true;
            		System.out.println(" Driver " + this.driverId +
            				" has done servicing the request Id : " + msg.getRequestId() 
            				+ ": customerId : " + msg.getCustomerId());
            	} else {
            		Thread.sleep(100);
            	}
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch (SQLException e) {
        	e.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}

}
