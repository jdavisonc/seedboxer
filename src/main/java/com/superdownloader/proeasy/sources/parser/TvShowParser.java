/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.sources.parser;

import com.superdownloader.proeasy.sources.domain.TvShow;
import com.superdownloader.proeasy.sources.type.Quality;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

/**
 *
 * @author Farid
 */
@Component
public class TvShowParser extends ContentParser<TvShow>{

    private final String QUALITY_HD = "720p";
    private final String QUALITY_FULLHD = "1080p";
    private final Pattern pattern = Pattern.compile(
        "(.*?)[\\.\\s_-]+S?(\\d{1,2})[Ex]{1}(\\d{2})[\\.\\s_-]+(\\d{3,4}p)?(.*)[\\.\\s_-]+(.*)"

    );
    
    @Override
    public TvShow parse(String input) {
        Matcher matcher = pattern.matcher(input);
        if(matcher.matches()){
            String name = matcher.group(1).trim();
            Integer season = Integer.valueOf(matcher.group(2));
            Integer episode = Integer.valueOf(matcher.group(3));
            
            String quality = matcher.group(4);
            Quality qualityEnum = Quality.STANDARD;
            if(quality != null){
                if(quality.equals(QUALITY_HD))
                    qualityEnum = Quality.HD;
                else if(quality.equals(QUALITY_FULLHD))
                    qualityEnum = Quality.FULLHD;
            }
            
                   
            return new TvShow(name, season, episode, qualityEnum);
        }
        else
            return null;
    }
    
}
