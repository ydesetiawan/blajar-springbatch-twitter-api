package com.yd.configuration;

import static com.yd.configuration.LookupConst.CODE;
import static com.yd.configuration.LookupConst.KEY_PREFIX_PARTY_UUID_MAPPING;

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
 * @version 1.0, Aug 25, 2014
 * @since
 */
@Component
public class PartyUuidServiceImpl implements PartyUuidService {

	private static Logger log = LoggerFactory
			.getLogger(PartyUuidServiceImpl.class);

	private final LookupItemRepository lookupItemRepository;
	private static final String PREFIX = KEY_PREFIX_PARTY_UUID_MAPPING;

	@Autowired
	public PartyUuidServiceImpl(LookupItemRepository lookupItemRepository) {
		super();
		this.lookupItemRepository = lookupItemRepository;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addConfig(LookupConfig config) {
		try {
			LookupItem item = new LookupItem();
			item.setKey(PREFIX + config.getUuid());
			item.setDateUpdated(new Date());
			Map<String, String> attributes = new HashMap<>();
			attributes.put(CODE, config.getPartyCode());
			item.setAttributes(attributes);
			lookupItemRepository.saveAndFlush(item);
			log.debug("Added {} from {}", new Object[] { PREFIX, config });
		} catch (Exception e) {
			String message = "Failed adding lookup" + PREFIX + " for :"
					+ config;
			log.debug(message, e);
			throw new IllegalArgumentException(message);
		}
	}

	@Override
	@Transactional
	public boolean remove(LookupConfig lookupConfig) {
		try {
			LookupItem item = findLookupItem(lookupConfig.getUuid());
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

	@Override
	public String getCode(String uuid) {
		LookupItem item = findLookupItem(uuid);
		if (item != null && item.getAttributes() != null) {
			return item.getAttributes().get(CODE);
		}
		log.warn("Could not find party code of {}", uuid);
		return null;
	}

	private LookupItem findLookupItem(String uuid) {
		return lookupItemRepository.findOne(PREFIX + uuid);
	}

}
