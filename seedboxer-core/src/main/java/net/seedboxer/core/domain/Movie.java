/*******************************************************************************
 * Movie.java
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
@Table(name = "movie")
@PrimaryKeyJoinColumn(name = "content_id")
@Component
@Scope("prototype")
public class Movie extends Content {

  @Column(name = "quality")
  private String quality;

  @Column(name = "year")
  private Integer year;

  public Movie() {
  }

  public Movie(String name, Integer year, Quality quality) {
    super(name);
    this.year = year;
    this.quality = quality.name();
  }

  public Quality getQuality() {
    return Quality.valueOf(quality);
  }

  public void setQuality(Quality quality) {
    this.quality = quality.name();
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  @Override
  public String toString() {
    return this.getName() + "|" + getYear() + "|" + quality;
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

    Movie movie = (Movie) o;

    if (quality != null ? !quality.equals(movie.quality) : movie.quality != null) {
      return false;
    }
    if (!year.equals(movie.year)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(quality, year, super.hashCode());
  }

}
