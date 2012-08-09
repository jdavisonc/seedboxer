/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.superdownloader.proeasy.sources.domain;

import com.superdownloader.proeasy.sources.type.Quality;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Farid
 */
@Entity
@Table(name= "TV_SHOW")
@PrimaryKeyJoinColumn(name="CONTENT_ID")
@Component
@Scope("prototype")
public class TvShow extends Content{

    @Column(name="QUALITY")
    private String quality;
    
    @Column(name="SEASON")
    private Integer season;
    
    @Column(name="EPISODE")
    private Integer episode;

    public TvShow() {
    }
    
    public TvShow(String name, int season, int episode, Quality quality){
        super.setName(name);
        this.quality = quality.name();
        this.season = season;
        this.episode = episode;
        
    }

    public Integer getEpisode() {
        return episode;
    }

    public void setEpisode(Integer episode) {
        this.episode = episode;
    }

    public Quality getQuality() {
        return Quality.valueOf(quality);
    }

    public void setQuality(Quality quality) {
        this.quality = quality.name();
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }
    
    
    @Override
    public String toString(){
        return this.getName()+ "|S"+ season + "|E" + episode + this.getQuality();
    }

    @Override
    public boolean equals(Object object){
        boolean superEquals = super.equals(object);
        if(superEquals){
            if(object.getClass() == this.getClass()){
                TvShow tvShow = (TvShow) object;
                return this.season == tvShow.getSeason() 
                    && this.episode == tvShow.getEpisode() 
                    && this.getQuality() == tvShow.getQuality();
                        
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 71 * hash + (this.quality != null ? this.quality.hashCode() : 0);
        hash = 71 * hash + (this.season != null ? this.season.hashCode() : 0);
        hash = 71 * hash + (this.episode != null ? this.episode.hashCode() : 0);
        return hash;
    }
    
    
}
