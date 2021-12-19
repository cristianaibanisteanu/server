package com.vvs.java.http;

public enum Method {
	GET("GET"),
	HEAD("HEAD"),
	UNRECOGNIZED(null);

	private final String method;

	Method(String method) {
		this.method = method;
	}
}
