package com.yd.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yd.persistence.repository.model.User;
import com.yd.persistence.repository.model.UserRole;

/**
 * @author edys
 * @version 1.0, Jan 20, 2017
 * @since
 */
public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    @Query("select ur from UserRole ur where ur.user = ?1")
    public List<UserRole> findByUser(User user);

    @Query("SELECT ur FROM UserRole ur WHERE ur.role IN (?1) GROUP BY ur.user")
    public List<UserRole> findByRoleNames(List<String> list);

}
