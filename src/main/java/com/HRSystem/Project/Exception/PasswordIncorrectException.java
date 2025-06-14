package com.HRSystem.Project.Exception;

public class PasswordIncorrectException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PasswordIncorrectException(String message){
		super(message);
	}
}
