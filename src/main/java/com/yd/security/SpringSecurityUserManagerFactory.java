package com.yd.security;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;
import org.apache.log4j.Logger;

/**
 * @author edys
 * @version 1.0, Feb 8, 2014
 * @since 3.0.0
 */
public class SpringSecurityUserManagerFactory implements SessionFactory {

    private static Logger log = Logger
            .getLogger("SpringSecurityUserManagerFactory");

    @Override
    public Class<?> getSessionType() {
        return UserIdentityManager.class;
    }

    @Override
    public Session openSession() {
        log.debug("Returning UserManager");
        return new UserManager();
    }
}
