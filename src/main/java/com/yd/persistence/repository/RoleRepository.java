package com.yd.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yd.persistence.model.key.RoleKey;
import com.yd.persistence.repository.model.Role;

/**
 * @author edys
 * @version 3.0.0, Feb 10, 2014
 * @since 3.0.0
 */
public interface RoleRepository extends JpaRepository<Role, RoleKey> {

    @Query("SELECT r FROM Role r WHERE r.id.name = :name")
    Role findByRoleName(@Param("name") String name);
    
    @Query("SELECT r FROM Role r WHERE r.id.name IN :roles")
    List<Role> findByRoles(@Param("roles") List<String> roleNames);

}
