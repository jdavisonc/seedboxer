/*******************************************************************************
 * APIResponse.java
 *
 * Copyright (c) 2012 SeedBoxer Team.
 *
 * This file is part of SeedBoxer.
 *
 * SeedBoxer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SeedBoxer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SeedBoxer.  If not, see <http ://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.seedboxer.web.type;

public class APIResponse {

	public enum ResponseStatus { SUCCESS, FAILURE; }

	private String message;

	private ResponseStatus status;

	public APIResponse() { }

	public APIResponse(String message, ResponseStatus status) {
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

	public static APIResponse createSuccessfulResponse(String message) {
		return new APIResponse(message, ResponseStatus.SUCCESS);
	}

	public static APIResponse createSuccessfulResponse() {
		return createSuccessfulResponse(null);
	}

	public static APIResponse createErrorResponse(String message) {
		return new APIResponse(message, ResponseStatus.FAILURE);
	}

}
