package com.xthst.ims.exceptions;

public class ResourceNotFoundException extends RuntimeException{
	
	public static final String PRODUCT = "Product";
	public static final String TRANSACTION = "Transaction";
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
	
	public ResourceNotFoundException(String resourceType, Long id) {
		super(String.format("%s with ID %d is not found.", resourceType, id));
	}
}
