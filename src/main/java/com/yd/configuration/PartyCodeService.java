package com.yd.configuration;

/**
 * @author edys
 * @version 1.0, Aug 8, 2014
 * @since
 */
public interface PartyCodeService {

	void addConfig(LookupConfig LookupConfig);

	String getPartyUuid(String partyCode);

	boolean remove(LookupConfig LookupConfig);

	LookupConfig find(String partyCode);
}
