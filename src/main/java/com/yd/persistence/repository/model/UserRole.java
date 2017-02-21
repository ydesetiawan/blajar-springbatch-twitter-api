package com.yd.persistence.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author edys
 * @version 1.0, Jan 20, 2017
 * @since
 */
@Entity
@Table(name = "user_role", uniqueConstraints = @UniqueConstraint(columnNames = {
		"role", "user" }))
public class UserRole implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1920583714607590726L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(length = 36, nullable = false, updatable = false)
	private String uuid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "UserRoleFK_user"))
	private User user;

	@ManyToOne
	@JoinColumns(value = { @JoinColumn(name = ColumnConst.REF_Role, referencedColumnName = ColumnConst.REF_Role, nullable = false) }, foreignKey = @ForeignKey(name = "UserRoleFK_role"))
	private Role role;

	public UserRole() {
		//
	}

	public UserRole(User user, Role role) {
		this.user = user;
		this.role = role;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
