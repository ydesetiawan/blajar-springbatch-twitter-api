/* Copyright (C) 2014 ASYX International B.V. All rights reserved. */
package com.yd.persistence.model;

import com.yd.persistence.repository.model.DocstoreRelation;

/**
 * @author edys
 * @version 1.0, May 12, 2014
 * @since
 */
public class ModelDocstoreRelation {

	private String docstoreRelationUuid;
	private String principalDocstoreReference;
	private String partyDocstoreReference;
	private String partyCode;
	private String principalCode;
	private String relationType;
	private String action;

	public ModelDocstoreRelation() {
		super();
	}

	public ModelDocstoreRelation(DocstoreRelation docstoreRelation,
			String partyCode, String principalCode) {
		super();
		this.partyCode = partyCode;
		this.principalCode = principalCode;
		this.relationType = docstoreRelation.getRelationType();
	}

	public ModelDocstoreRelation(String docstoreRelationUuid) {
		super();
		this.docstoreRelationUuid = docstoreRelationUuid;
	}

	public String getDocstoreRelationUuid() {
		return docstoreRelationUuid;
	}

	public void setDocstoreRelationUuid(String docstoreRelationUuid) {
		this.docstoreRelationUuid = docstoreRelationUuid;
	}

	public String getPrincipalDocstoreReference() {
		return principalDocstoreReference;
	}

	public void setPrincipalDocstoreReference(String principalDocstoreReference) {
		this.principalDocstoreReference = principalDocstoreReference;
	}

	public String getPartyDocstoreReference() {
		return partyDocstoreReference;
	}

	public void setPartyDocstoreReference(String partyDocstoreReference) {
		this.partyDocstoreReference = partyDocstoreReference;
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

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
