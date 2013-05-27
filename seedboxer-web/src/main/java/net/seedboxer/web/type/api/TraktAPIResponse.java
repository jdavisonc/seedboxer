/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.seedboxer.web.type.api;

import com.jakewharton.trakt.entities.MediaBase;
import java.util.ArrayList;
import java.util.List;
import net.seedboxer.web.type.UserConfigInfo;

/**
 *
 * @author The Sultan
 */
public class TraktAPIResponse extends APIResponse {
    
	private final List<MediaBase> searchResult;

	public TraktAPIResponse() {
		searchResult = new ArrayList<MediaBase>();
	}

	public TraktAPIResponse(List<MediaBase> media) {
		this.searchResult = media;
	}

	public List<MediaBase> getSearchResults() {
		return searchResult;
	}
}
