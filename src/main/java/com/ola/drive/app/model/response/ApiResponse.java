package com.ola.drive.app.model.response;

import lombok.Data;

/**
 * The response to be sent for all the requests.
 */
@Data
public class ApiResponse {

	private Error error;
	private Object data;
	
	public static ApiResponse buildErrorResponse (String code, String message) {
		ApiResponse apiResponse = new ApiResponse ();
		apiResponse.setError (new Error (code, message));
		return apiResponse;
	}
	public static ApiResponse buildSuccessResponse (Object data) {
		ApiResponse apiResponse = new ApiResponse ();
		apiResponse.setData(data);
		return apiResponse;
	}
	
	public static ApiResponse buildSuccessResponse(Object data, String offset) {
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setData(data);
		return apiResponse;
	}
}