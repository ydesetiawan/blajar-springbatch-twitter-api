package com.yd.security;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;
import org.apache.log4j.Logger;

/**
 * @author edys
 * @version 1.0, Feb 8, 2014
 * @since 3.0.0
 */
public class SpringSecurityGroupManagerFactory implements SessionFactory {

    private static Logger log = Logger
            .getLogger("SpringSecurityGroupManagerFactory");

    @Override
    public Class<?> getSessionType() {
        return GroupIdentityManager.class;
    }

    @Override
    public Session openSession() {
        log.debug("Returning GroupManager");
        return new GroupManager();
    }
}
