package com.yd.persistence.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author edys
 * @version 1.0, Apr 23, 2014
 * @since
 */
@Entity
@Table(name = "docstore_relation", uniqueConstraints = @UniqueConstraint(columnNames = {
		ColumnConst.NAME_PrincipalDocstore, ColumnConst.NAME_PartyDocstore,
		"relationType" }, name = "docstoreRelation"))
public class DocstoreRelation implements Serializable {

	private static final long serialVersionUID = -1566118493305728206L;

	@Id
	@GeneratedValue(generator = "UUIDGenerator")
	@GenericGenerator(name = "UUIDGenerator", strategy = "com.yd.common.util.UUIDGenerator")
	@Column(length = ColumnConst.SIZE_uuid, nullable = false, updatable = false)
	private String uuid;

	@ManyToOne
	@JoinColumn(name = ColumnConst.NAME_PrincipalDocstore, nullable = false, updatable = false, foreignKey = @ForeignKey(name = "XCHNG_DocstoreRelationFKPrincipal_docstore"))
	private Docstore principalDocstore;

	@ManyToOne
	@JoinColumn(name = ColumnConst.NAME_PartyDocstore, nullable = false, updatable = false, foreignKey = @ForeignKey(name = "XCHNG_DocstoreRelationFKParty_docstore"))
	private Docstore partyDocstore;

	@Column(length = ColumnConst.SIZE_name, nullable = false, updatable = false)
	private String relationType;

	public DocstoreRelation() {
	}

	public DocstoreRelation(Docstore principalDocstore, Docstore partyDocstore,
			String relationType) {
		super();
		this.principalDocstore = principalDocstore;
		this.partyDocstore = partyDocstore;
		this.relationType = relationType;
	}

	public Docstore getPartyDocstore() {
		return partyDocstore;
	}

	public Docstore getPrincipalDocstore() {
		return principalDocstore;
	}

	public String getRelationType() {
		return relationType;
	}

	public String getUuid() {
		return uuid;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(uuid).append(principalDocstore)
				.append(partyDocstore).append(relationType).toHashCode();
	}

	public void setPartyDocstore(Docstore partyDocstore) {
		this.partyDocstore = partyDocstore;
	}

	public void setPrincipalDocstore(Docstore principalDocstore) {
		this.principalDocstore = principalDocstore;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "DocstoreRelation [uuid=" + uuid + ", principalDocstore="
				+ principalDocstore + ", partyDocstore=" + partyDocstore
				+ ", relationType=" + relationType + "]";
	}

}
