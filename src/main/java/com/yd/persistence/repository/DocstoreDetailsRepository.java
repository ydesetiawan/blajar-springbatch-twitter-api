package com.yd.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yd.persistence.repository.model.DocstoreDetails;

/**
 * @author edys
 * @version 3.0.0, Feb 10, 2014
 * @since 3.0.0
 */
public interface DocstoreDetailsRepository extends
        JpaRepository<DocstoreDetails, String> {

    DocstoreDetails findByCompany_Name(String companyName);

}
