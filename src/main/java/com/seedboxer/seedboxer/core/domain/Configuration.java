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

package com.seedboxer.seedboxer.core.domain;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class Configuration {

	public static final String USER = "User";
	public static final String USER_ID = "UserId";

	public static final String DOWNLOAD_ID = "DownloadId";

	public static final String FILES = "Files";
	public static final String FILES_NAME = "FilesName";

	public static final String START_TIME = "Start";
	public static final String END_TIME = "End";

	public static final String FTP_PREFIX = "Ftp";
	public static final String FTP_USERNAME = FTP_PREFIX + "Username";
	public static final String FTP_PASSWORD = FTP_PREFIX +"Password";
	public static final String FTP_URL = FTP_PREFIX + "Url";
	public static final String FTP_REMOTE_DIR = FTP_PREFIX + "RemoteDir";
	public static final String FTP_SENT = FTP_PREFIX + "Sent";

	public static final String SSH_PREFIX = "Ssh";
	public static final String SSH_USERNAME = SSH_PREFIX + "Username";
	public static final String SSH_PASSWORD = SSH_PREFIX + "Password";
	public static final String SSH_URL = SSH_PREFIX + "Url";
	public static final String SSH_CMD = SSH_PREFIX + "Cmd";

	public static final String NOTIFICATION_PREFIX = "Notification";

	public static final String NOTIFICATION_EMAIL = NOTIFICATION_PREFIX + "Email";
	public static final String NOTIFICATION_EMAIL_EMAIL = NOTIFICATION_EMAIL + "Email";

	public static final String NOTIFICATION_C2DM = NOTIFICATION_PREFIX + "C2dm";
	public static final String NOTIFICATION_C2DM_DEVICEID = NOTIFICATION_C2DM + "DeviceId";
	public static final String NOTIFICATION_C2DM_REGISTRATIONID = NOTIFICATION_C2DM + "RegistrationId";

	public static final String THIRD_PARTY = "ThirdParty";

	public static final String IMDB = "Imdb";
	public static final String IMDB_LIST = "ImdbList";
	public static final String IMDB_AUTHOR = "ImdbAuthor";
	public static final String IMDB_CONTENT_QUALITY = "ImdbContentQuality";

}
