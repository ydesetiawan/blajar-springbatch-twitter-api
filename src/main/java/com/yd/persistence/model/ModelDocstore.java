package com.yd.persistence.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

import com.yd.persistence.repository.model.Docstore;
import com.yd.persistence.repository.model.DocstoreDetails;

/**
 * @author edys
 * @version 1.0, Jul 16, 2014
 * @since
 */
@Component
public class ModelDocstore {

	private String uuid;

	private String partyCode;

	@NotEmpty(message = "Reference should contain a value.")
	@Size(max = 50, message = "Reference must be lower or equal to 50 character")
	private String reference;

	private DocstoreDetails docstoreDetails;
	private String action;

	@NotEmpty(message = "Company Code should contain a value.")
	@Size(max = 255, message = "Company Code must be lower or equal to 255 character")
	private String companyCode;

	@Size(max = 255, message = "Principal Code must be lower or equal to 255 character")
	private String principalCode;

	public ModelDocstore() {
		super();
	}

	public ModelDocstore(Docstore docstore, String partyCode) {
		this.uuid = docstore.getUuid();
		this.partyCode = partyCode;
		this.reference = docstore.getReference();
		this.docstoreDetails = docstore.getDocstoreDetails();
		this.companyCode = getCompanyCode();
		this.principalCode = getPrincipalCode();
	}
	
	public ModelDocstore(Docstore docstore, String partyCode,String companyCode) {
		this.uuid = docstore.getUuid();
		this.partyCode = partyCode;
		this.reference = docstore.getReference();
		this.docstoreDetails = docstore.getDocstoreDetails();
		this.companyCode = companyCode;
		this.principalCode = getPrincipalCode();
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
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

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
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

}
