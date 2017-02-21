package com.yd.persistence.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.yd.persistence.model.key.RoleKey;

/**
 * @author edys
 * @version 3.0.0, Feb 6, 2014
 * @since 3.0.0
 */
@Entity
@Table(name = "role")
public class Role implements Serializable {

	private static final long serialVersionUID = 8830881515739834534L;

	@EmbeddedId
	private RoleKey id;

	@Column(nullable = false)
	private boolean permissionRole = true;

	public Role() {
		super();
	}

	public Role(String name) {
		id = new RoleKey(name);
	}

	@Transient
	public String getName() {
		return getId().getName();
	}

	public RoleKey getId() {
		return id;
	}

	public void setId(RoleKey id) {
		this.id = id;
	}

	public boolean isPermissionRole() {
		return permissionRole;
	}

	public void setPermissionRole(boolean permissionRole) {
		this.permissionRole = permissionRole;
	}

}
