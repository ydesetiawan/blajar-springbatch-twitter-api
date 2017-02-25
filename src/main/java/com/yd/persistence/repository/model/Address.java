package com.yd.persistence.repository.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * @author edys
 * @version 3.0.0, Feb 6, 2014
 * @since
 */
@Embeddable
public class Address implements Serializable {

	private static final long serialVersionUID = 8236304318769778227L;

	private String name;

	private String city;

	private String countryISOCode;

	private String languageISOCode;

	private String postalCode;

	private String street1;

	private String street2;

	private String country;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountryISOCode() {
		return countryISOCode;
	}

	public void setCountryISOCode(String countryISOCode) {
		this.countryISOCode = countryISOCode;
	}

	public String getLanguageISOCode() {
		return languageISOCode;
	}

	public void setLanguageISOCode(String languageISOCode) {
		this.languageISOCode = languageISOCode;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "Address [name=" + name + ", city=" + city + ", countryISOCode="
				+ countryISOCode + ", languageISOCode=" + languageISOCode
				+ ", postalCode=" + postalCode + ", street1=" + street1
				+ ", street2=" + street2 + ", country=" + country + "]";
	}

}
