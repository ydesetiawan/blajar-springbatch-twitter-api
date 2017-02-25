package com.yd.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.yd.persistence.repository.model.Docstore;

/**
 * @author edys
 * @version 3.0.0, Feb 10, 2014
 * @since 3.0.0
 */
public interface DocstoreRepository extends JpaRepository<Docstore, String> {

    List<Docstore> findByReferenceStartingWith(String reference);

    List<Docstore> findByReference(String reference);

    Page<Docstore> findByReferenceStartingWith(String reference,
            Pageable pageable);

    List<Docstore> findAllByOrderByReferenceAsc();

}
