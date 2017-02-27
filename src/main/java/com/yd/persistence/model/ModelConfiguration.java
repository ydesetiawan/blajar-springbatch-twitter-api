package com.yd.persistence.model;

import com.yd.persistence.repository.model.Docstore;

/**
 * @author edys
 * @version 1.0, May 6, 2014
 * @since
 */
public class ModelConfiguration {

	private String docstoreRelationParty;

	private String docstoreRelationPrincipal;

	private String docstoreRelationType;

	private String docstoreRelationUuid;

	private String partyCode;

	private String principalCode;

	private String action;

	private Docstore principalDocstore;

	private Docstore partyDocstore;

	public ModelConfiguration() {
		super();
	}

	public String getDocstoreRelationParty() {
		return docstoreRelationParty;
	}

	public void setDocstoreRelationParty(String docstoreRelationParty) {
		this.docstoreRelationParty = docstoreRelationParty;
	}

	public String getDocstoreRelationPrincipal() {
		return docstoreRelationPrincipal;
	}

	public void setDocstoreRelationPrincipal(String docstoreRelationPrincipal) {
		this.docstoreRelationPrincipal = docstoreRelationPrincipal;
	}

	public String getDocstoreRelationType() {
		return docstoreRelationType;
	}

	public void setDocstoreRelationType(String docstoreRelationType) {
		this.docstoreRelationType = docstoreRelationType;
	}

	public String getDocstoreRelationUuid() {
		return docstoreRelationUuid;
	}

	public void setDocstoreRelationUuid(String docstoreRelationUuid) {
		this.docstoreRelationUuid = docstoreRelationUuid;
	}

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}

	public String getPrincipalCode() {
		return principalCode;
	}

	public void setPrincipalCode(String principalCode) {
		this.principalCode = principalCode;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Docstore getPrincipalDocstore() {
		return principalDocstore;
	}

	public void setPrincipalDocstore(Docstore principalDocstore) {
		this.principalDocstore = principalDocstore;
	}

	public Docstore getPartyDocstore() {
		return partyDocstore;
	}

	public void setPartyDocstore(Docstore partyDocstore) {
		this.partyDocstore = partyDocstore;
	}

}
