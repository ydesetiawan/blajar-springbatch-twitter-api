package com.yd.persistence.model.key;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author edys
 * @version 3.0.0, Feb 6, 2014
 * @since 3.0.0
 */
@Embeddable
public class RoleKey implements Serializable {

	private static final long serialVersionUID = -7629316887859728412L;

	@Column(name = "role", length = 50, nullable = false)
	private String name;

	public RoleKey() {

	}

	public RoleKey(String name) {
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}