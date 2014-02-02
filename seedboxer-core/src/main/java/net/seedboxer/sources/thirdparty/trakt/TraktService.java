/*******************************************************************************
 * TraktService.java
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
package net.seedboxer.sources.thirdparty.trakt;

import java.util.List;

import com.jakewharton.trakt.ServiceManager;
import com.jakewharton.trakt.entities.Movie;
import com.jakewharton.trakt.entities.TvShow;

/**
 * @author Jorge Davison (jdavisonc)
 *
 */
public class TraktService {
	
	private final ServiceManager serviceManager;

	public TraktService(String authKey, String username, String password) {
		serviceManager = new ServiceManager();
		serviceManager.setUseSsl(true);
		serviceManager.setApiKey(authKey);
		serviceManager.setAuthentication(username, password);
	}
	
	public TraktService(String authKey) {
		serviceManager = new ServiceManager();
		serviceManager.setUseSsl(true);
		serviceManager.setApiKey(authKey);
	}
	
	public String searchTvShow(String show) {
		List<TvShow> shows = serviceManager.searchService().shows(show).fire();
		if (!shows.isEmpty()) {
			return shows.get(0).url;
		}
		return null;
	}
	
	public String searchMovie(String movie) {
		List<Movie> movies = serviceManager.searchService().movies(movie).fire();
		if (!movies.isEmpty()) {
			return movies.get(0).url;
		}
		return null;
	}	

}
