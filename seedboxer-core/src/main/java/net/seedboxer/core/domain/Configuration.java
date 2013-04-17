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

	public static final String NOTIFICATION_GCM = NOTIFICATION_PREFIX + "GCM";
	public static final String NOTIFICATION_GCM_DEVICEID = NOTIFICATION_GCM + "DeviceId";
	public static final String NOTIFICATION_GCM_REGISTRATIONID = NOTIFICATION_GCM + "RegistrationId";

	public static final String THIRD_PARTY = "ThirdParty";

	public static final String IMDB_PREFIX = "Imdb";
	public static final String IMDB_LIST = IMDB_PREFIX + "List";
	public static final String IMDB_AUTHOR = IMDB_PREFIX + "Author";
	public static final String IMDB_CONTENT_QUALITY = IMDB_PREFIX + "ContentQuality";

	public static final String TRAKT_PREFIX = "Trakt";
	public static final String TRAKT_USERNAME = TRAKT_PREFIX + "Username";
	public static final String TRAKT_PASSWORD = TRAKT_PREFIX + "Password";
	public static final String TRAKT_AUTH_KEY = TRAKT_PREFIX + "AuthKey";
	public static final String TRAKT_CONTENT_QUALITY = TRAKT_PREFIX + "ContentQuality";

}
