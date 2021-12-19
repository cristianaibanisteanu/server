package com.vvs.java.http;

public enum Status {
	_200("200 OK"),
	_400("400 Bad Request"),
	_404("404 Not Found"),
	_501("501 Not Implemented");

	private final String status;

	Status(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return status;
	}
}
