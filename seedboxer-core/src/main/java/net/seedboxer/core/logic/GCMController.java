package net.seedboxer.core.logic;

import java.io.IOException;

import javax.annotation.PostConstruct;

import net.seedboxer.core.domain.Configuration;
import net.seedboxer.core.domain.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;

@Component
public class GCMController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GCMController.class);

	public static final int RETRIES = 5;
	
	@Value("${gcm.auth.key}")
	private String gcmAuthKey;
	
	@Value("${gcm.projectId}")
	private String projectId;
	
	@Autowired
	private UsersManager usersController;
	
	private Sender sender;
		
	@PostConstruct
	public void init() {
		if (gcmAuthKey != null) {
			sender = new Sender(gcmAuthKey);
		}
	}
	
	public String getProjectId() {
		return projectId;
	}
	
	public void registerDevice(User user, String registrationId) {
		usersController.saveUserConf(user, Configuration.NOTIFICATION_GCM_REGISTRATIONID, registrationId);
	}
	
	public void unregisterDevice(User user, String registrationId) {
		usersController.deleteUserConf(user, Configuration.NOTIFICATION_GCM_REGISTRATIONID);
	}
	
	public void send(Message message, String registrationId) throws IOException {
		if (sender == null) {
			LOGGER.error("GCM Notification failed, key doesn't provided");
			return ;
		}
		
		sender.send(message, registrationId, RETRIES);
	}

}
