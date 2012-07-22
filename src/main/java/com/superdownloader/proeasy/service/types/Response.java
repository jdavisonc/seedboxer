package com.superdownloader.proeasy.service.types;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
public class Response {

	public enum ResponseStatus { OK, ERROR; }

	private String message;

	private ResponseStatus status;

	public Response() { }

	public Response(String message, ResponseStatus status) {
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public static Response createSuccessfulResponse(String message) {
		return new Response(message, ResponseStatus.OK);
	}

	public static Response createSuccessfulResponse() {
		return createSuccessfulResponse(null);
	}

	public static Response createErrorResponse(String message) {
		return new Response(message, ResponseStatus.ERROR);
	}

}
