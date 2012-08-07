/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.sources.filters;

import com.superdownloader.proeasy.sources.domain.TvShow;

/**
 *
 * @author Farid
 */
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

    
}
