/*******************************************************************************
 * TraktSearchAPI.java
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

import com.jakewharton.trakt.ServiceManager;
import com.jakewharton.trakt.entities.MediaBase;
import com.jakewharton.trakt.services.SearchService.ShowsBuilder;
import com.jakewharton.trakt.services.SearchService.MoviesBuilder;
import java.util.ArrayList;
import java.util.List;
import net.seedboxer.core.domain.Configuration;
import net.seedboxer.core.domain.Content;
import net.seedboxer.web.type.api.APIResponse;
import net.seedboxer.web.type.api.TraktAPIResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author The Sultan
 */
@Controller
@RequestMapping("/webservices/search")
public class TraktSearchAPI extends SeedBoxerAPI {

    ServiceManager manager;
    
    public TraktSearchAPI(){
	String username = "thesultan";
	String password = "charly";
	String authKey = "758f2a6188a41c0bb23a6ec463087061";
	manager = new ServiceManager();
	manager.setUseSsl(true);
	manager.setApiKey(authKey);
	manager.setAuthentication(username, password);
    }
    
    
    @RequestMapping(value="movie", method = RequestMethod.GET)
    public @ResponseBody APIResponse searchForMovies(String searchQuery){
	MoviesBuilder moviesBuilder = manager.searchService().movies(searchQuery);
	List<MediaBase> movies = new ArrayList();
	movies.addAll(moviesBuilder.fire());
	return new TraktAPIResponse(movies);
	
    }
    @RequestMapping(value="tvshow", method = RequestMethod.GET)
    public @ResponseBody APIResponse searchForTvShows(String searchQuery){
	ShowsBuilder showsBuilder = manager.searchService().shows(searchQuery);
	List<MediaBase> tvShows = new ArrayList();
	tvShows.addAll(showsBuilder.fire());
	return new TraktAPIResponse(tvShows);
	
    }
}
