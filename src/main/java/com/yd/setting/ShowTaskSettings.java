package com.yd.setting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author edys
 * @version 1.0, Jun 11, 2015
 * @since
 */
@Component
public class ShowTaskSettings {

    private static final String HIDE_ALL_TASK_NOT_IN_ROLE = "hideAllTaskNotInRole";

    private static final boolean DEFAULT_HIDE_ALL_TASK = false;

    @Autowired
    private Environment env;

    public Boolean getShowHideTask() {
        return env.getProperty(HIDE_ALL_TASK_NOT_IN_ROLE, Boolean.class,
                DEFAULT_HIDE_ALL_TASK);
    }

}
