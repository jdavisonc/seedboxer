/**
 * 
 */
package com.superdownloader.proeasy.core.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author harley
 *
 */
@Entity
@Table(name="users_configs", uniqueConstraints = {@UniqueConstraint(columnNames={"name", "user_id"})})
public class UserConfiguration {

	@Id
	private long id;

	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	private String name;

	private String value;

	public UserConfiguration() { }

	public UserConfiguration(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
