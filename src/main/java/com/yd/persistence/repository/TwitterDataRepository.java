package com.yd.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yd.persistence.repository.model.TwitterData;

/**
 * @author edys
 * @version 1.0, Jan 21, 2017
 * @since
 */
public interface TwitterDataRepository extends
        JpaRepository<TwitterData, String> {

    List<TwitterData> findAllByOrderByPostingDateAsc();
}
