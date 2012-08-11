/*******************************************************************************
 * C2DMNotification.java
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
package com.superdownloader.proeasy.mule.processor.notification;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.superdownloader.proeasy.mule.processor.Headers;
import com.superdownloader.proeasy.mule.util.C2DMUtils;

/**
 * @author harley
 *
 */
@Component(value = "c2dmNotification")
public class C2DMNotification extends Notification {

	private static final Logger LOGGER = LoggerFactory.getLogger(C2DMNotification.class);

	@Value("${proeasy.c2dm.username}")
	private String c2dmUsername;

	@Value("${proeasy.c2dm.password}")
	private String c2dmPassword;

	private String authToken = null;

	@Override
	protected void processSuccessNotification(Message msg) {
		sendMessage(msg, "OK");
	}

	@Override
	protected void processFailNotification(Message msg, Throwable t) {
		sendMessage(msg, "FAILED");
	}

	@SuppressWarnings("unchecked")
	private void sendMessage(Message msg, String sucess) {
		String registrationId = (String) msg.getHeader(Headers.NOTIFICATION_C2DM_REGISTRATIONID);
		String name = ((List<String>) msg.getHeader(Headers.FILES_NAME)).get(0);
		String message = sucess + ";Files " + name + "...";
		try {
			int responseCode = C2DMUtils.sendMessage(authToken, registrationId, message);
			if (responseCode == 401) { // Invalid login, relogin
				register();
				C2DMUtils.sendMessage(authToken, registrationId, message);
			}
		} catch (IOException e) {
			LOGGER.error("Error sending notification to device", e);
		}
	}

	@PostConstruct
	private void register() {
		try {
			authToken = C2DMUtils.register(c2dmUsername, c2dmPassword);
		} catch (IOException e) {
			LOGGER.error("Error registering server to Google", e);
		}
	}

}
