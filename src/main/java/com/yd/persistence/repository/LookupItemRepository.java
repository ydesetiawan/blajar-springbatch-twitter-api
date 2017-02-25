/* Copyright (C) 2014 ASYX International B.V. All rights reserved. */
package com.yd.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yd.persistence.repository.model.LookupItem;

/**
 * @author alifr
 * @version 1.0, Aug 20, 2014
 * @since
 */
public interface LookupItemRepository extends JpaRepository<LookupItem, String> {

}
