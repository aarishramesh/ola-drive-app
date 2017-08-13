package com.ola.drive.app.model.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Error {

	private String code;
	private String message;
	private String source;
	private List<Error> errors = new ArrayList<>();
	
	public Error (String code, String message){
		this.code = code;
		this.message = message;
	}
}
