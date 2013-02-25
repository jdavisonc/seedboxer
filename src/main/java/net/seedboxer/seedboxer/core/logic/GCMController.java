package net.seedboxer.seedboxer.core.logic;

import net.seedboxer.seedboxer.core.domain.Configuration;
import net.seedboxer.seedboxer.core.domain.User;
import net.seedboxer.seedboxer.core.domain.UserConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.android.gcm.server.Sender;

@Component
public class GCMController {

	public static final int RETRIES = 5;
	
	@Value("${gcm.auth.key}")
	private String gcmAuthKey;
	
	@Value("${gcm.project}")
	private String projectId;
	
	@Autowired
	private UsersController usersController;
	
	public Sender createSender() {
		if (gcmAuthKey != null) {
			return new Sender(gcmAuthKey);
		} else {
			return null;
		}
	}
	
	public String getProjectId() {
		return projectId;
	}
	
	public void registerDevice(User user, String registrationId) {
		usersController.saveUserConf(user, new UserConfiguration(Configuration.NOTIFICATION_GCM_REGISTRATIONID, registrationId));
	}
	
	public void unregisterDevice(User user, String registrationId) {
		usersController.deleteUserConf(user, Configuration.NOTIFICATION_GCM_REGISTRATIONID);
	}

}
