package com.yd.configuration;

/**
 * @author edys
 * @version 1.0, Aug 8, 2014
 * @since
 */
public interface PartyUuidService {

	void addConfig(LookupConfig lookupConfig);

	boolean remove(LookupConfig lookupConfig);

	String getCode(String uuid);
}
