package com.yd.persistence.repository.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author edys
 * @version 1.0, Aug 20, 2014
 * @since
 */
@Entity
@Table(name = "lookup_item")
public class LookupItem implements Serializable {

	private static final long serialVersionUID = 2617178254350345205L;

	@Id
	@Column(name = "itemKey", nullable = false, updatable = false)
	private String key;

	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date dateUpdated;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "lookup_item_attributes", joinColumns = { @JoinColumn(name = "itemKey") })
	@MapKeyColumn(name = "attributeKey", columnDefinition = "varchar(255) NOT NULL DEFAULT ''")
	@Column(name = "attributeValue", columnDefinition = "varchar(255) DEFAULT NULL")
	private Map<String, String> attributes;

	public LookupItem() {
		super();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "LookupItem [key=" + key + ", dateUpdated=" + dateUpdated
				+ ", attributes=" + attributes + "]";
	}

}
