package com.yd.persistence.repository.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * @author edys
 * @version 3.0.0, Feb 6, 2014
 * @since 3.0.0
 */
@Entity
@Table(name = "docstore")
public class Docstore implements Serializable {

	private static final long serialVersionUID = 6967463326519134024L;

	@Id
	@Column(length = ColumnConst.SIZE_uuid, nullable = false, updatable = false)
	private String uuid;

	@Column(name = ColumnConst.NAME_Docstore_reference, length = ColumnConst.SIZE_Docstore_reference, nullable = false)
	private String reference;

	@PrimaryKeyJoinColumn
	@OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private DocstoreDetails docstoreDetails;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public DocstoreDetails getDocstoreDetails() {
		return docstoreDetails;
	}

	public void setDocstoreDetails(DocstoreDetails docstoreDetails) {
		this.docstoreDetails = docstoreDetails;
	}

	@Override
	public String toString() {
		return "Docstore [uuid=" + uuid + ", reference=" + reference + "]";
	}
}
