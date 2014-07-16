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

package net.seedboxer.core.domain;

import com.google.common.base.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import net.seedboxer.core.type.Quality;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * @author The-Sultan
 */
@Entity
@Table(name = "tv_show")
@PrimaryKeyJoinColumn(name = "content_id")
@Component
@Scope("prototype")
public class TvShow extends Content {

  @Column(name = "quality")
  private String quality;

  @Column(name = "season")
  private Integer season;

  @Column(name = "episode")
  private Integer episode;

  public TvShow() {
  }

  public TvShow(String name, Integer season, Integer episode, Quality quality) {
    super(name);
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
  public String toString() {
    return getName() + "|S" + season + "|E" + episode + "|" + quality;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    TvShow tvShow = (TvShow) o;

    if (episode != null ? !episode.equals(tvShow.episode) : tvShow.episode != null) {
      return false;
    }
    if (!quality.equals(tvShow.quality)) {
      return false;
    }
    if (season != null ? !season.equals(tvShow.season) : tvShow.season != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(quality, season, episode, super.hashCode());
  }


}
