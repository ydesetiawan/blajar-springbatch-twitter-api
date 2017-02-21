package com.yd.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.yd.persistence.repository.model.User;

/**
 * @author edys
 * @version 1.0, Jan 20, 2017
 * @since
 */
public interface UserRepository extends JpaRepository<User, String> {

	User findByUsername(String username);
	
	List<User> findByUsernameStartingWith(String username);

    Page<User> findByUsernameStartingWith(String username, Pageable pageable);

}
