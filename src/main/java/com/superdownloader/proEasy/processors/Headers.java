package com.superdownloader.proEasy.processors;

public class Headers {

	public static final String SEPARATOR = "_";

	public static final String PREFIX = "proeasy" + SEPARATOR;

	public static final String USERNAME = PREFIX + "username";

	public static final String FILES = PREFIX + "files";
	public static final String FILES_NAME = PREFIX + "filesName";

	public static final String START_TIME = PREFIX + "start";
	public static final String END_TIME = PREFIX + "end";

	public static final String FTP_PREFIX = PREFIX + "ftp" + SEPARATOR;
	public static final String FTP_USERNAME = FTP_PREFIX + "username";
	public static final String FTP_PASSWORD = FTP_PREFIX +"password";
	public static final String FTP_URL = FTP_PREFIX +"url";
	public static final String FTP_REMOTE_DIR = FTP_PREFIX + "remoteDir";
	public static final String FTP_SENT = FTP_PREFIX + "sent";

	public static final String SSH_PREFIX = PREFIX + "ssh" + SEPARATOR;
	public static final String SSH_USERNAME = SSH_PREFIX + "username";
	public static final String SSH_PASSWORD = SSH_PREFIX + "password";
	public static final String SSH_URL = SSH_PREFIX + "url";
	public static final String SSH_CMD = SSH_PREFIX + "cmd";

	public static final String NOTIFICATION_PREFIX = PREFIX + "notification" + SEPARATOR;

	public static final String NOTIFICATION_EMAIL = NOTIFICATION_PREFIX + "email";
	public static final String NOTIFICATION_EMAIL_EMAIL = NOTIFICATION_EMAIL + SEPARATOR + "email";

	public static final String NOTIFICATION_C2DM = NOTIFICATION_PREFIX + "c2dm";
	public static final String NOTIFICATION_C2DM_DEVICEID = NOTIFICATION_C2DM + SEPARATOR + "deviceId";
	public static final String NOTIFICATION_C2DM_REGISTRATIONID = NOTIFICATION_C2DM + SEPARATOR + "registrationId";

}
