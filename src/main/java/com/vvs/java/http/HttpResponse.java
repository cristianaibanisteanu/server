package com.vvs.java.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class HttpResponse {

	private static Logger logger = Logger.getLogger(HttpResponse.class);

	public static final String VERSION = "HTTP/1.0";

	List<String> headers = new ArrayList<String>();

	byte[] body;

	public HttpResponse(HttpRequest req) throws IOException {

		switch (req.method) {
			case HEAD:
				fillHeaders(Status._200);
				break;
			case GET:
				try {
					File file = new File("." + req.uri);
					if (file.isDirectory()) {
					    fillHeaders(Status._200);
					    
						headers.add(ContentType.HTML.toString());
						StringBuilder result = new StringBuilder("<html><head><title>VVS Project");
						result.append(req.uri);
						result.append("</title></head><body><h1>Html Page");
						result.append(req.uri);
						result.append("</h1><hr><pre>");

						File[] files = file.listFiles();
						for (File subfile : files) {
							result.append(" <a href=\"" + subfile.getPath() + "\">" + subfile.getPath() + "</a>\n");
						}
						result.append("<hr></pre></body></html>");
						fillResponse(result.toString());
					} else if (file.exists()) {
					    fillHeaders(Status._200);
						setContentType(req.uri, headers);
						fillResponse(getBytes(file));
					} else {
						logger.info("File not found:" + req.uri);
						fillHeaders(Status._404);
						fillResponse(Status._404.toString());
					}
				} catch (Exception e) {
					logger.error("Response Error", e);
					fillHeaders(Status._400);
					fillResponse(Status._400.toString());
				}

				break;
			case UNRECOGNIZED:
				fillHeaders(Status._400);
				fillResponse(Status._400.toString());
				break;
			default:
				fillHeaders(Status._501);
				fillResponse(Status._501.toString());
		}

	}

	private byte[] getBytes(File file) throws IOException {
		int length = (int) file.length();
		byte[] array = new byte[length];
		InputStream in = new FileInputStream(file);
		int offset = 0;
		while (offset < length) {
			int count = in.read(array, offset, (length - offset));
			offset += count;
		}
		in.close();
		return array;
	}

	private void fillHeaders(Status status) {
		headers.add(HttpResponse.VERSION + " " + status.toString());
		headers.add("Connection: close");
		headers.add("Server: WebServer");
	}

	private void fillResponse(String response) {
		body = response.getBytes();
	}

	private void fillResponse(byte[] response) {
		body = response;
	}

	public void write(OutputStream os) throws IOException {
		DataOutputStream output = new DataOutputStream(os);
		for (String header : headers) {
			output.writeBytes(header + "\r\n");
		}
		output.writeBytes("\r\n");
		if (body != null) {
			output.write(body);
		}
		output.writeBytes("\r\n");
		output.flush();
	}

	private void setContentType(String uri, List<String> list) {
		try {
			String ext = uri.substring(uri.indexOf(".") + 1);
			list.add(ContentType.valueOf(ext.toUpperCase()).toString());
		} catch (Exception e) {
			logger.error("ContentType not found: " + e, e);
		}
	}
}
