package com.yd.persistence.services;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yd.persistence.repository.DocstoreDetailsRepository;
import com.yd.persistence.repository.DocstoreRepository;
import com.yd.persistence.repository.model.Docstore;
import com.yd.persistence.repository.model.DocstoreDetails;

/**
 * @author edys
 * @version 1.0, Apr 1, 2015
 * @since
 */
@Service
@Transactional(TxType.REQUIRES_NEW)
public class DocstoreService {

    @Autowired
    private DocstoreRepository docstoreRepository;
    @Autowired
    private DocstoreDetailsRepository docstoreDetailsRepository;

    public void saveDocstoreAndDetails(Docstore docstore,
            DocstoreDetails docstoreDetails) {
        docstoreRepository.save(docstore);
        docstoreDetailsRepository.save(docstoreDetails);
    }
}