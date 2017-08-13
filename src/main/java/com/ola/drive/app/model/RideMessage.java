package com.ola.drive.app.model;

import lombok.Data;

@Data
public class RideMessage {
	private int requestId;
	private long customerId;
	
	public RideMessage(int requestId, long customerId) {
		this.requestId = requestId;
		this.customerId = customerId;
	}
}
