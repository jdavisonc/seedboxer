/**
 * 
 */
package com.superdownloader.proeasy.core.type;

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
@Table(name="configurations", uniqueConstraints = {@UniqueConstraint(columnNames={"name"})})
public class UserConfiguration {

	@Id
	private long id;

	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	private String name;

	private String value;

	public UserConfiguration() { }

	public UserConfiguration(User user, String name, String value) {
		this.user = user;
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

}
