package com.yd.persistence.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yd.persistence.repository.DocstoreRelationRepository;
import com.yd.persistence.repository.model.DocstoreRelation;
import com.yd.persistence.repository.model.DocstoreRelationType;

/**
 * @author edys
 * @version 1.0, Apr 28, 2014
 * @since
 */
@Component
public class DocstoreRelationServiceImpl implements DocstoreRelationService {

	@Autowired
	private DocstoreRelationRepository docstoreRelationRepository;

	@Override
	public List<DocstoreRelation> findAllByPartyDocstoreAndRelationType(
			String partyDocstoreUuid, String relationType) {
		return docstoreRelationRepository
				.findAllByPartyDocstoreAndRelationType(partyDocstoreUuid,
						relationType);
	}

	@Override
	public DocstoreRelation findSupplierRelationToBuyer(
			String supplierDocstoreUuid) {
		return docstoreRelationRepository.findByPartyDocstoreAndRelationType(
				supplierDocstoreUuid, DocstoreRelationType.BUYER_SUPPLIER);
	}

}
