package com.ola.drive.app.model;

import lombok.Data;

@Data
public class RideMessage {
	private int requestId;
	private long customerId;
	
	public RideMessage(long requestId, long customerId) {
		this.customerId = customerId;
	}
}
