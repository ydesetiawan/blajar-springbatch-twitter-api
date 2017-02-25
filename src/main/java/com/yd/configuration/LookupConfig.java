package com.yd.configuration;

/**
 * @author edys
 * @version 1.0, Aug 5, 2014
 * @since
 */
public class LookupConfig {

	private String uuid;
	private String partyCode;

	public LookupConfig(String partyCode, String uuid) {
		this.partyCode = partyCode;
		this.uuid = uuid;
	}

	public String getPartyCode() {
		return partyCode;
	}

	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}