/*******************************************************************************
 * Content.java
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

package net.seedboxer.seedboxer.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.seedboxer.seedboxer.sources.type.MatchableItem;


/**
 *
 * @author The-Sultan
 */
@Entity
@Table(name= "content")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Content {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name="name")
	private String name;

	@Column(name="history")
	private Boolean history = false;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@Transient
	private MatchableItem matchableItem;

	public Content() { }

	public Content(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MatchableItem getMatchableItem() {
		return matchableItem;
	}

	public void setMatchableItem(MatchableItem matchableItem) {
		this.matchableItem = matchableItem;
	}

	public void setIsHistory(boolean history){
		this.history = history;
	}

	public boolean isHistory(){
		return history;
	}

	public Boolean getHistory() {
		return history;
	}

	public void setHistory(Boolean history) {
		this.history = history;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object object){
		if(this == object) {
			return true;
		}
		if(object instanceof Content){
			Content content = (Content) object;
			if(name.equalsIgnoreCase(content.getName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 43 * hash + (name != null ? name.hashCode() : 0);
		return hash;
	}

}
