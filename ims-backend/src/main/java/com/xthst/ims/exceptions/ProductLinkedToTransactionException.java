package com.xthst.ims.exceptions;

public class ProductLinkedToTransactionException extends RuntimeException{
	
	public ProductLinkedToTransactionException() {
		super(String.format("Cannot delete. Product is referenced by transaction(s)"));
	}
}
