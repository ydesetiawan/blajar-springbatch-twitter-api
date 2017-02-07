package com.yd.activiti;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang.StringUtils;

/**
 * @author edys
 * @version 1.0, Jun 20, 2014
 * @since
 */
public class SetJobVariablesForUserTaskListener extends
        SetVariablesForUserTaskListener implements TaskListener {

    private static final long serialVersionUID = 5416638362849240079L;

    private Expression parserMessage;

    private Expression jobSelectedFiles;

    private Expression jobFilePath;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.asyx.xchange3.activiti.SetVariablesForUserTaskListener#notify(org
     * .activiti.engine.delegate.DelegateTask)
     */
    @Override
    public void notify(DelegateTask task) {
        super.notify(task);
        String pm = (String) parserMessage.getValue(task);
        String jsf = (String) jobSelectedFiles.getValue(task);
        String jobPath = (String) jobFilePath.getValue(task);

        Map<String, Object> variables = new HashMap<>();

        variables.put("parserMessage", pm);
        variables.put("jobSelectedFiles", jsf);

        if (StringUtils.isNotBlank(jobPath))
            variables.put("jobFilePath", jobPath);

        task.setVariablesLocal(variables);
    }
}
