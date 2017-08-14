package com.ola.drive.app.model;

import lombok.Data;

@Data
public class DriveRequest {
	private long customerId;
	private int driverId;
	private int requestId;
}
