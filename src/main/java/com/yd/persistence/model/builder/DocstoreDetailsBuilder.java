package com.yd.persistence.model.builder;

import com.yd.persistence.repository.model.Address;
import com.yd.persistence.repository.model.Docstore;
import com.yd.persistence.repository.model.DocstoreDetails;

/**
 * @author edys
 * @version 1.0, Feb 16, 2014
 * @since 3.0.0
 */
public class DocstoreDetailsBuilder {

	private String uuid;
	private Address company;
	private Docstore docstore;
	private String companyRegistrationNumber;

	public DocstoreDetails build() {
		DocstoreDetails docstoreDetails = new DocstoreDetails();
		docstoreDetails.setCompany(company);
		docstoreDetails.setDocstore(docstore);
		docstoreDetails.setUuid(uuid);
		docstoreDetails.setCompanyRegistrationNumber(companyRegistrationNumber);
		return docstoreDetails;
	}

	public DocstoreDetailsBuilder withCompany(Address company) {
		this.company = company;
		return this;
	}

	public DocstoreDetailsBuilder withCompanyRegistrationNumber(
			String companyRegistrationNumber) {
		this.companyRegistrationNumber = companyRegistrationNumber;
		return this;
	}

	public DocstoreDetailsBuilder withDocstore(Docstore docstore) {
		this.docstore = docstore;
		return this;
	}

	public DocstoreDetailsBuilder withUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}
}
