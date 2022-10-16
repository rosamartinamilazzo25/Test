package com.acme.test.errors;

import javax.persistence.PersistenceException;

public class AlreadyInsertedException extends PersistenceException {

	public AlreadyInsertedException() {
		// TODO Auto-generated constructor stub
	}

	public AlreadyInsertedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AlreadyInsertedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AlreadyInsertedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}


}
