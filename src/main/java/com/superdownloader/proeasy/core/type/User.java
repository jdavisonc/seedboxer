/**
 * 
 */
package com.superdownloader.proeasy.core.type;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author harley
 *
 */
@Entity
@Table(name="users", uniqueConstraints = {@UniqueConstraint(columnNames={"username"})})
public class User {

	@Id
	private long id;

	private String username;

	private String password;

	@OneToMany(mappedBy="user")
	@ElementCollection
	@MapKeyColumn(name="name")
	private Map<String, UserConfiguration> config;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<String, UserConfiguration> getConfig() {
		return config;
	}

	public void setConfig(Map<String, UserConfiguration> config) {
		this.config = config;
	}

	public void addConfig(String name, String value) {
		config.put(name, new UserConfiguration(this, name, value));
	}

	public void removeConfig(String name) {
		config.remove(name);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + "]";
	}

}
