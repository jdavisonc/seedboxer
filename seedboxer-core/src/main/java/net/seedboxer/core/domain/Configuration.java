/*******************************************************************************
 * Configuration.java
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

package net.seedboxer.core.domain;

import java.util.List;

import com.google.common.collect.ImmutableList.Builder;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class Configuration {

	public static final String USER = "User";
	public static final String USER_ID = "UserId";

	public static final String DOWNLOAD_ID = "DownloadId";
	public static final String DOWNLOAD_TOTAL_PIECES = "DownloadTotalPieces";

	public static final String FILES = "Files";
	public static final String FILES_NAME = "FilesName";

	public static final String START_TIME = "Start";
	public static final String END_TIME = "End";

	public static final String TRANSFER_USERNAME = "TransferUsername";
	public static final String TRANSFER_PASSWORD = "TransferPassword";
	public static final String TRANSFER_URL = "TransferUrl";
	public static final String TRANSFER_REMOTE_DIR = "TransferRemoteDir";
	public static final String TRANSFER_SENT = "TransferSent";

	public static final String SSH_USERNAME = "SshUsername";
	public static final String SSH_PASSWORD = "SshPassword";
	public static final String SSH_URL = "SshUrl";
	public static final String SSH_CMD = "SshCmd";

	public static final String NOTIFICATION_EMAIL = "NotificationEmail";
	public static final String NOTIFICATION_GCM = "NotificationGCM";

	public static final String NOTIFICATION_GCM_DEVICEID = "NotificationGCMDeviceId";
	public static final String NOTIFICATION_GCM_REGISTRATIONID = "NotificationGCMRegistrationId";

	public static final String THIRD_PARTY = "ThirdParty";

	public static final String IMDB_LIST = "ImdbList";
	public static final String IMDB_AUTHOR = "ImdbAuthor";
	public static final String IMDB_CONTENT_QUALITY = "ImdbContentQuality";

	public static final String TRAKT_USERNAME = "TraktUsername";
	public static final String TRAKT_PASSWORD = "TraktPassword";
	public static final String TRAKT_AUTH_KEY = "TraktAuthKey";
	public static final String TRAKT_CONTENT_QUALITY = "TraktContentQuality";
	
	public static final List<String> values = new Builder<String>()
			.add(USER)
			.add(USER_ID)
			.add(DOWNLOAD_ID)
			.add(FILES)
			.add(FILES_NAME)
			.add(START_TIME)
			.add(END_TIME)
			.add(TRANSFER_USERNAME)
			.add(TRANSFER_PASSWORD)
			.add(TRANSFER_URL)
			.add(TRANSFER_REMOTE_DIR)
			.add(TRANSFER_SENT)
			.add(SSH_USERNAME)
			.add(SSH_PASSWORD)
			.add(SSH_URL)
			.add(SSH_CMD)
			.add(NOTIFICATION_EMAIL)
			.add(NOTIFICATION_GCM)
			.add(NOTIFICATION_GCM_DEVICEID)
			.add(NOTIFICATION_GCM_REGISTRATIONID)
			.add(THIRD_PARTY)
			.add(IMDB_LIST)
			.add(IMDB_AUTHOR)
			.add(IMDB_CONTENT_QUALITY)
			.add(TRAKT_USERNAME)
			.add(TRAKT_PASSWORD)
			.add(TRAKT_AUTH_KEY)
			.add(TRAKT_CONTENT_QUALITY)
			.build();

}
