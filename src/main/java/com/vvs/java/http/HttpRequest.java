package com.vvs.java.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class HttpRequest {

	private static Logger logger = Logger.getLogger(HttpRequest.class);

	List<String> headers = new ArrayList<String>();

	Method method;
	String uri;
	String version;

	public HttpRequest(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String str = reader.readLine();
		parseRequestLine(str);

		while (!str.equals("")) {
			str = reader.readLine();
			parseRequestHeader(str);
		}
	}

	private void parseRequestLine(String str) {
		logger.info(str);
		String[] split = str.split("\\s+");
		try {
			method = Method.valueOf(split[0]);
		} catch (Exception e) {
			method = Method.UNRECOGNIZED;
		}
		uri = split[1];
		version = split[2];
	}

	private void parseRequestHeader(String str) {
		logger.info(str);
		headers.add(str);
	}
}
