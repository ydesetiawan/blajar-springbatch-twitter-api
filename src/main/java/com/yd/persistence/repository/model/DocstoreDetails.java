package com.yd.persistence.repository.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author edys
 * @version 3.0.0, Feb 6, 2014
 * @since
 */
@Entity
@Table(name = "docstore_details")
public class DocstoreDetails implements Serializable {

	private static final long serialVersionUID = 3610565776893933886L;

	@Id
	@Column(length = ColumnConst.SIZE_uuid, nullable = false, updatable = false)
	private String uuid;

	@PrimaryKeyJoinColumn
	@OneToOne
	private Docstore docstore;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "name", column = @Column(name = "companyName", length = ColumnConst.SIZE_Adress_Name)),
			@AttributeOverride(name = "country", column = @Column(name = "companyCountry", length = ColumnConst.SIZE_Adress_Country)),
			@AttributeOverride(name = "city", column = @Column(name = "companyCity", length = ColumnConst.SIZE_Adress_City)),
			@AttributeOverride(name = "countryISOCode", column = @Column(name = "companyCountryISO", length = ColumnConst.SIZE_Adress_CountryISOCode)),
			@AttributeOverride(name = "languageISOCode", column = @Column(name = "companyLanguageISO", length = ColumnConst.SIZE_Adress_LanguageISOCode)),
			@AttributeOverride(name = "postalCode", column = @Column(name = "companyPostalCode", length = ColumnConst.SIZE_Adress_PostalCode)),
			@AttributeOverride(name = "street1", column = @Column(name = "companyStreet1", length = ColumnConst.SIZE_Adress_Street)),
			@AttributeOverride(name = "street2", column = @Column(name = "companyStreet2", length = ColumnConst.SIZE_Adress_Street))

	})
	private Address company;

	@Column(length = 80)
	private String companyRegistrationNumber;

	public Address getCompany() {
		if (company == null) {
			company = new Address();
		}
		return company;
	}

	@Transient
	public String getCompanyCity() {
		return hasCompany() ? getCompany().getCity() : null;
	}

	@Transient
	public String getCompanyCountry() {
		return hasCompany() ? getCompany().getCountry() : null;
	}

	@Transient
	public String getCompanyCountryISOCode() {
		return hasCompany() ? getCompany().getCountryISOCode() : null;
	}

	@Transient
	public String getCompanyLanguageISOCode() {
		return hasCompany() ? getCompany().getLanguageISOCode() : null;
	}

	@Transient
	public String getCompanyName() {
		return hasCompany() ? getCompany().getName() : null;
	}

	@Transient
	public String getCompanyPostalCode() {
		return hasCompany() ? getCompany().getPostalCode() : null;
	}

	public String getCompanyRegistrationNumber() {
		return companyRegistrationNumber;
	}

	@Transient
	public String getCompanyStreet1() {
		return hasCompany() ? getCompany().getStreet1() : null;
	}

	@Transient
	public String getCompanyStreet2() {
		return hasCompany() ? getCompany().getStreet2() : null;
	}

	public Docstore getDocstore() {
		return docstore;
	}

	public String getUuid() {
		return uuid;
	}

	@Transient
	public boolean hasCompany() {
		return company != null;
	}

	public void setCompany(Address company) {
		this.company = company;
	}

	public void setCompanyCity(String city) {
		if (hasCompany() || ((city != null) && !city.isEmpty())) {
			getCompany().setCity(city);
		}
	}

	public void setCompanyCountry(String country) {
		if (hasCompany() || ((country != null) && !country.isEmpty())) {
			getCompany().setCountry(country);
		}
	}

	public void setCompanyCountryISOCode(String countryISOCode) {
		if (hasCompany()
				|| ((countryISOCode != null) && !countryISOCode.isEmpty())) {
			getCompany().setCountryISOCode(countryISOCode);
		}
	}

	public void setCompanyLanguageISOCode(String languageISOCode) {
		if (hasCompany()
				|| ((languageISOCode != null) && !languageISOCode.isEmpty())) {
			getCompany().setLanguageISOCode(languageISOCode);
		}
	}

	public void setCompanyName(String name) {
		if (hasCompany() || ((name != null) && !name.isEmpty())) {
			getCompany().setName(name);
		}
	}

	public void setCompanyPostalCode(String postalCode) {
		if (hasCompany() || ((postalCode != null) && !postalCode.isEmpty())) {
			getCompany().setPostalCode(postalCode);
		}
	}

	public void setCompanyRegistrationNumber(String companyRegistrationNumber) {
		this.companyRegistrationNumber = companyRegistrationNumber;
	}

	public void setCompanyStreet1(String street1) {
		if (hasCompany() || ((street1 != null) && !street1.isEmpty())) {
			getCompany().setStreet1(street1);
		}
	}

	public void setCompanyStreet2(String street2) {
		if (hasCompany() || ((street2 != null) && !street2.isEmpty())) {
			getCompany().setStreet2(street2);
		}
	}

	public void setDocstore(Docstore docstore) {
		if ((uuid != null) && (docstore != null)
				&& !uuid.equals(docstore.getUuid()))
			throw new IllegalArgumentException("Docstore uuid differs");
		this.docstore = docstore;
		if ((docstore != null) && (this.uuid == null)
				&& (docstore.getUuid() != null)) {
			setUuid(docstore.getUuid());
		}
	}

	public void setUuid(String uuid) {
		if ((uuid != null) && (docstore != null)
				&& !uuid.equals(docstore.getUuid()))
			throw new IllegalArgumentException("Docstore uuid differs");
		this.uuid = uuid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DocstoreDetails [uuid=" + uuid + "]";
	}
}
