package com.acme.test.errors;

import javax.persistence.PersistenceException;

public class AlreadyDeletedException extends PersistenceException{

	public AlreadyDeletedException() {
		// TODO Auto-generated constructor stub
	}

	public AlreadyDeletedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AlreadyDeletedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AlreadyDeletedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}


}
