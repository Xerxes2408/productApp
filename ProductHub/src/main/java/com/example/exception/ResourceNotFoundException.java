package com.example.exception;

public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String resource, String fieldName, String fieldValue) {
		super(String.format("%s Not Found With %s : %s ", resource, fieldName, fieldValue));

	}

}
