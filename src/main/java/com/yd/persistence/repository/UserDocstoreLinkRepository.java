package com.yd.persistence.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yd.persistence.repository.model.Docstore;
import com.yd.persistence.repository.model.User;
import com.yd.persistence.repository.model.UserDocstoreLink;

/**
 * @author edys
 * @version 3.0.0, Feb 10, 2014
 * @since 3.0.0
 */
public interface UserDocstoreLinkRepository extends
		JpaRepository<UserDocstoreLink, Long> {

	public Collection<UserDocstoreLink> findByDocstore(Docstore docstore);

	@Query("select udl from UserDocstoreLink udl where udl.user = ?1")
	List<UserDocstoreLink> findByUser(User user);

	UserDocstoreLink findByUserAndDocstore(User user, Docstore docstore);

}
