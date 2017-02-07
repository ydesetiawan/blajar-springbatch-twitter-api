package com.yd.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.Expression;
import org.apache.log4j.Logger;

/**
 * @author edys
 * @version 1.0, Feb 10, 2014
 * @since
 */
public class SetCandidateGroupTaskListener implements TaskListener {

    private static final long serialVersionUID = -5197697364793300866L;

    private static Logger log = Logger
            .getLogger("SetCandidateGroupTaskListener");

    private Expression parameter;

    private Expression prefix;

    public Expression getParameter() {
        return parameter;
    }

    public Expression getPrefix() {
        return prefix;
    }

    @Override
    public void notify(DelegateTask task) {
        String prefixValue = (String) prefix.getValue(task);
        String parameterValue = (String) parameter.getValue(task);
        String candidateGroup = prefixValue + parameterValue;
        task.addCandidateGroup(candidateGroup);
        log.debug("Candidate group set to: '" + candidateGroup + "' for task "
                + task.getId());
    }

    public void setParameter(Expression parameter) {
        this.parameter = parameter;
    }

    public void setPrefix(Expression prefix) {
        this.prefix = prefix;
    }

}
