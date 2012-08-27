/*******************************************************************************
 * TvShow.java
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

package com.seedboxer.seedboxer.sources.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.seedboxer.seedboxer.sources.type.Quality;

/**
 *
 * @author The-Sultan
 */
@Entity
@Table(name= "tv_show")
@PrimaryKeyJoinColumn(name="content_id")
@Component
@Scope("prototype")
public class TvShow extends Content{

	@Column(name="quality")
	private String quality;

	@Column(name="season")
	private Integer season;

	@Column(name="episode")
	private Integer episode;

	public TvShow() { }

	public TvShow(String name, Integer season, Integer episode, Quality quality){
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
				return season == tvShow.getSeason()
						&& episode == tvShow.getEpisode()
						&& this.getQuality() == tvShow.getQuality();

			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		hash = 71 * hash + (quality != null ? quality.hashCode() : 0);
		hash = 71 * hash + (season != null ? season.hashCode() : 0);
		hash = 71 * hash + (episode != null ? episode.hashCode() : 0);
		return hash;
	}


}
