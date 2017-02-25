package com.yd.persistence.services;

import java.util.List;

import com.yd.persistence.repository.model.DocstoreRelation;

/**
 * @author edys
 * @version 1.0, Apr 28, 2014
 * @since
 */
public interface DocstoreRelationService {

    List<DocstoreRelation> findAllByPartyDocstoreAndRelationType(
            String partyDocstoreUuid, String relationType);
    DocstoreRelation findSupplierRelationToBuyer(String supplierDocstoreUuid);
}
