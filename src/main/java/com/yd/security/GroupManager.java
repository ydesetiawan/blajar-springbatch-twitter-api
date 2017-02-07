package com.yd.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author edys
 * @version 1.0, Feb 8, 2014
 * @since 3.0.0
 */
public class GroupManager extends GroupEntityManager {

    private static Logger log = Logger.getLogger(GroupManager.class);

    @Override
    public Group createNewGroup(String groupId) {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }

    @Override
    public GroupQuery createNewGroupQuery() {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }

    @Override
    public void deleteGroup(String groupId) {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }

    @Override
    public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }

    @Override
    public long findGroupCountByNativeQuery(Map<String, Object> parameterMap) {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }

    @Override
    public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }

    @Override
    public List<Group> findGroupsByNativeQuery(
            Map<String, Object> parameterMap, int firstResult, int maxResults) {
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
    public void insertGroup(Group group) {
        throw new UnsupportedOperationException("Managed by Spring Security");
    }
}
