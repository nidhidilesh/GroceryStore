package com.grocery.GroceryStore.exception;

public class ResourceNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException() {
		super("Resource does not exist for the given id");
	}
}
