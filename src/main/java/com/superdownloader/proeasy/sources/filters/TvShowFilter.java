/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.sources.filters;

import com.superdownloader.proeasy.sources.domain.TvShow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Farid
 */
@Component
public class TvShowFilter extends ContentFilter<TvShow>{


    @Override
    protected boolean applyFilter(TvShow userContent, TvShow parsedContent) {
        if(userContent.getName().trim().equalsIgnoreCase(parsedContent.getName().trim())){
            if((userContent.getSeason() != null && userContent.getSeason() == parsedContent.getSeason())
                ||
                userContent.getSeason() == null)
                if((userContent.getEpisode() != null && userContent.getEpisode() == parsedContent.getEpisode())
                ||
                userContent.getSeason() == null)
                    if((userContent.getQuality() != null && userContent.getQuality().equals(parsedContent.getQuality()))
                    ||
                    userContent.getQuality() == null)
                        return true;

        }
        return false;
    }

    @Autowired
    public void setType(TvShow tvshow){
        super.setType(TvShow.class);
    }
    
}
