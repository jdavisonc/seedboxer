package com.seedboxer.seedboxer.sources.thirdparty.trakt;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jakewharton.trakt.ServiceManager;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.entities.UserProfile;

public class TraktProcessorIT {

	private static final Logger LOGGER = LoggerFactory.getLogger(TraktProcessorIT.class);
	private static final String AUTH_KEY = "e2c47ad00b7ad043c455cb9a8978a9fb";
	private static final String USERNAME = "harleydavison";

	@Test
	public void shouldReturnValidTVShows() {
		ServiceManager manager = new ServiceManager();
		manager.setApiKey(AUTH_KEY);
		manager.setUseSsl(true);
		manager.setAuthentication(USERNAME, "sp-1212");

		UserProfile profile = manager.userService( ).profile(USERNAME).fire();
		LOGGER.debug("{}", profile.username);

		List<TvShow> watchlistShows = manager.userService().watchlistShows(USERNAME).fire();
		for (TvShow tvShow : watchlistShows) {
			LOGGER.debug("List {}", tvShow.title);
		}
	}

}
