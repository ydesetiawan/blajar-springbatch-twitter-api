package com.yd.activiti;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.BaseEntityEventListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author edys
 * @version 1.0, Jul 9, 2014
 * @since 3.1.1
 */
@Component
public class ActivitiEventListener extends BaseEntityEventListener {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(ActivitiEventListener.class);

    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    protected void onCreate(ActivitiEvent event) {
        processEvent(event);
    }

    @Override
    protected void onDelete(ActivitiEvent event) {
        processEvent(event);
    }

    @Override
    protected void onEntityEvent(ActivitiEvent event) {
        processEvent(event);
    }

    @Override
    protected void onInitialized(ActivitiEvent event) {
        processEvent(event);
    }

    @Override
    protected void onUpdate(ActivitiEvent event) {
        processEvent(event);
    }

    private void processEvent(ActivitiEvent event) {
        if (!(event instanceof ActivitiEntityEvent)) {
            return;
        }
        ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
        if (entityEvent.getEntity() instanceof ProcessInstance) {
            processProcessInstanceEvent(entityEvent);
        } else if (entityEvent.getEntity() instanceof Task) {
            processTaskEvent(entityEvent);
        }
    }

    private void processProcessInstanceEvent(ActivitiEntityEvent entityEvent) {
        ProcessInstance processInstance = (ProcessInstance) entityEvent
                .getEntity();
        if (processInstance.getBusinessKey() == null) {
            return;
        }

    }

    private void processTaskEvent(ActivitiEntityEvent entityEvent) {
        Task task = (Task) entityEvent.getEntity();
        if (task.getName() == null) {
            return;
        }

    }
}
