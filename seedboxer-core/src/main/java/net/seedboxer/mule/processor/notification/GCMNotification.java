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
package net.seedboxer.mule.processor.notification;

import java.io.IOException;
import java.util.List;

import net.seedboxer.core.domain.Configuration;
import net.seedboxer.core.logic.GCMController;

import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
@Component(value = "gcmNotification")
public class GCMNotification extends Notification {

	private static final Logger LOGGER = LoggerFactory.getLogger(GCMNotification.class);

	@Autowired
	private GCMController gcmController;

	@Override
	protected void processSuccessNotification(Message msg) {
		sendMessage(msg, "OK");
	}

	@Override
	protected void processFailNotification(Message msg, Throwable t) {
		sendMessage(msg, "FAILED");
	}

	@SuppressWarnings("unchecked")
	private void sendMessage(Message msg, String success) {
		String registrationId = (String) msg.getHeader(Configuration.NOTIFICATION_GCM_REGISTRATIONID);
		String name = ((List<String>) msg.getHeader(Configuration.FILES_NAME)).get(0);
		try {
			if (registrationId == null) {
				LOGGER.error("GCM Notification failed, device not registered for the user");
				return ;
			}
			gcmController.send(createPushNotification(success, name), registrationId);
		} catch (IOException e) {
			LOGGER.error("Error sending notification to device", e);
		}
	}

	private com.google.android.gcm.server.Message createPushNotification(String success, String name) {
		com.google.android.gcm.server.Message pushNotif =
				new com.google.android.gcm.server.Message
				.Builder()
				.addData("success", success)
				.addData("file", name)
				.build();

		return pushNotif;
	}

}
