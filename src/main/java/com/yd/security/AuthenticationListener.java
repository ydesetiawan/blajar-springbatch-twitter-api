package com.yd.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Service;

import com.yd.common.util.AppsUtil;
import com.yd.persistence.repository.UserRepository;
import com.yd.persistence.repository.model.User;

@Service
public class AuthenticationListener implements
        ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private Logger log = LoggerFactory.getLogger(AuthenticationListener.class);

    @Autowired
    UserRepository userRepository;

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        if (!AppsUtil.isSwitchedUser()) {
            log.debug("Updating user last login time");
            User user = userRepository
                    .findOne(AppsUtil.getActiveUserUuid());
            user.setLastLoginTime(new Date());
            userRepository.saveAndFlush(user);
        }
    }

}
