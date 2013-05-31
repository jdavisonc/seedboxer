/*******************************************************************************
 * SearchAPI.java
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
package net.seedboxer.web.controller.rs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.seedboxer.web.type.api.APIResponse;
import net.seedboxer.web.type.api.TraktAPIResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jakewharton.trakt.ServiceManager;
import com.jakewharton.trakt.entities.MediaBase;
import com.jakewharton.trakt.services.SearchService.MoviesBuilder;
import com.jakewharton.trakt.services.SearchService.ShowsBuilder;

/**
 * 
 * @author The Sultan
 * @author Jorge Davison (jdavisonc)
 */
@Controller
@RequestMapping("/webservices/search")
public class SearchAPI extends SeedBoxerAPI {

	ServiceManager manager;

	public SearchAPI() {
		String username = "thesultan";
		String password = "charly";
		String authKey = "758f2a6188a41c0bb23a6ec463087061";
		manager = new ServiceManager();
		manager.setUseSsl(true);
		manager.setApiKey(authKey);
		manager.setAuthentication(username, password);
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	APIResponse searchForMovies(String term) {
		MoviesBuilder moviesBuilder = manager.searchService().movies(term);
		List<MediaBase> results = new ArrayList<MediaBase>();
		results.addAll(moviesBuilder.fire());
		ShowsBuilder showsBuilder = manager.searchService().shows(term);
		results.addAll(showsBuilder.fire());
		results = filterResults(term, results);
		return new TraktAPIResponse(results);
	}

	List<MediaBase> filterResults(String term, List<MediaBase> unfilteredResults) {
		List<MediaBase> filteredResults = new ArrayList<MediaBase>();
		Calendar now = Calendar.getInstance();
		Integer currentYear = now.get(Calendar.YEAR);
		for (MediaBase media : unfilteredResults) {
			if (media.year > currentYear - 10) {
				filteredResults.add(media);
			}
		}

		return filteredResults;
	}

}
