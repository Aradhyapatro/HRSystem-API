package com.HRSystem.Project.Exception;

public class UserNotFoundException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1768193989507029163L;

	public UserNotFoundException(String message) {
		super(message);
	}
}
