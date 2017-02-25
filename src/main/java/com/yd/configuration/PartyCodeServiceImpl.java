package com.yd.configuration;

import static com.yd.configuration.LookupConst.KEY_PREFIX_PARTY_CODE_MAPPING;
import static com.yd.configuration.LookupConst.UUID;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yd.persistence.repository.LookupItemRepository;
import com.yd.persistence.repository.model.LookupItem;

/**
 * @author edys
 * @version 1.0, Aug 26, 2014
 * @since
 */
@Component
public class PartyCodeServiceImpl implements PartyCodeService {

	private static Logger log = LoggerFactory
			.getLogger(PartyCodeServiceImpl.class);

	private final LookupItemRepository lookupItemRepository;
	private static final String PREFIX = KEY_PREFIX_PARTY_CODE_MAPPING;

	@Autowired
	public PartyCodeServiceImpl(LookupItemRepository lookupItemRepository) {
		super();
		this.lookupItemRepository = lookupItemRepository;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addConfig(LookupConfig lookupConfig) {
		try {
			LookupItem item = new LookupItem();
			item.setKey(PREFIX + lookupConfig.getPartyCode());
			item.setDateUpdated(new Date());
			Map<String, String> attributes = new HashMap<>();
			attributes.put(UUID, lookupConfig.getUuid());
			item.setAttributes(attributes);
			lookupItemRepository.saveAndFlush(item);
			log.debug("Added {} from {}", new Object[] { PREFIX, lookupConfig });
		} catch (Exception e) {
			String message = "Failed adding lookup" + PREFIX + " for :"
					+ lookupConfig;
			log.debug(message, e);
			throw new IllegalArgumentException(message);
		}
	}

	@Override
	public String getPartyUuid(String partyCode) {
		LookupItem item = findLookupItem(partyCode);
		if (item != null && item.getAttributes() != null) {
			return item.getAttributes().get(UUID);
		}
		log.warn("Could not find uuid of {}", partyCode);
		return null;
	}

	@Override
	@Transactional
	public boolean remove(LookupConfig lookupConfig) {
		try {
			LookupItem item = findLookupItem(lookupConfig.getPartyCode());
			if (item != null) {
				lookupItemRepository.delete(item);
				lookupItemRepository.flush();
			}
			return true;
		} catch (Exception e) {
			String message = "Failed remove lookup" + PREFIX + " for :"
					+ lookupConfig;
			log.debug(message, e);
			throw new IllegalArgumentException(message);
		}
	}

	private LookupItem findLookupItem(String code) {
		return lookupItemRepository.findOne(PREFIX + code);
	}

	@Override
	public LookupConfig find(String code) {
		LookupItem item = findLookupItem(code);
		if (item != null && item.getAttributes() != null) {
			return new LookupConfig(code, item.getAttributes().get(UUID));
		}
		return null;
	}

}
