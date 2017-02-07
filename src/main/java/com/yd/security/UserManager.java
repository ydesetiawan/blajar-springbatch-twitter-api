package com.yd.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.UserQueryImpl;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.IdentityInfoEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author edys
 * @version 1.0, Feb 8, 2014
 * @since 3.0.0
 */
public class UserManager extends UserEntityManager {

    private static Logger log = Logger.getLogger(UserManager.class);

    @Override
    public Boolean checkPassword(String userId, String password) {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }

    @Override
    public User createNewUser(String userId) {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }

    @Override
    public UserQuery createNewUserQuery() {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }

    @Override
    public void deleteUser(String arg0) {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }

    @Override
    public List<Group> findGroupsByUser(String userId) {
        List<Group> groups = new ArrayList<Group>();
        for (GrantedAuthority authority : SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities()) {
            groups.add(new GroupEntity(authority.getAuthority()));
        }
        log.debug("Found groups for user " + userId + ": " + groups);
        return groups.size() > 0 ? groups : null;
    }

    @Override
    public List<User> findPotentialStarterUsers(String proceDefId) {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }

    @Override
    public UserEntity findUserById(String userId) {
        log.debug("Returning UserEntity for user " + userId);
        return new UserEntity(SecurityContextHolder.getContext()
                .getAuthentication().getName());
    }

    @Override
    public List<User> findUserByQueryCriteria(UserQueryImpl query, Page page) {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }

    @Override
    public long findUserCountByNativeQuery(Map<String, Object> parameterMap) {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }

    @Override
    public long findUserCountByQueryCriteria(UserQueryImpl query) {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }

    @Override
    public IdentityInfoEntity findUserInfoByUserIdAndKey(String userId,
            String key) {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }

    @Override
    public List<String> findUserInfoKeysByUserIdAndType(String userId,
            String type) {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }

    @Override
    public List<User> findUsersByNativeQuery(Map<String, Object> parameterMap,
            int firstResult, int maxResults) {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }

    @Override
    public void insertUser(User user) {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }
}
