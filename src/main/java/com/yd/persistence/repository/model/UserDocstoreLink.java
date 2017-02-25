package com.yd.persistence.repository.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * @author edys
 * @version 3.0.0, Feb 6, 2014
 * @since 3.0.0
 */
@Entity
@Table(name = "user_docstore_link", uniqueConstraints = @UniqueConstraint(columnNames = {
		"user", "docstore" }))
public class UserDocstoreLink implements Serializable {

	private static final long serialVersionUID = -1942123591392838973L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user", nullable = false, updatable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "docstore", nullable = false, updatable = false)
	private Docstore docstore;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastSelectedTime;

	public UserDocstoreLink() {
	}

	public UserDocstoreLink(User user, Docstore docstore) {
		setUser(user);
		setDocstore(docstore);
		setLastSelectedTime(new Date());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Docstore getDocstore() {
		return docstore;
	}

	public void setDocstore(Docstore docstore) {
		this.docstore = docstore;
	}

	public Date getLastSelectedTime() {
		return lastSelectedTime;
	}

	public void setLastSelectedTime(Date lastSelectedTime) {
		this.lastSelectedTime = lastSelectedTime;
	}

	@Override
	public String toString() {
		return "UserDocstoreLink [id=" + id + ", user=" + user + ", docstore="
				+ docstore + ", lastSelectedTime=" + lastSelectedTime + "]";
	}

}
