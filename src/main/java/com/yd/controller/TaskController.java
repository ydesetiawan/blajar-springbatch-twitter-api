package com.yd.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yd.batch.JobLauncherDetails;
import com.yd.common.util.AppsUtil;
import com.yd.persistence.model.User;
import com.yd.persistence.model.UserRole;
import com.yd.persistence.repository.UserRepository;
import com.yd.persistence.repository.UserRoleRepository;
import com.yd.setting.ShowTaskSettings;

/**
 * @author edys
 * @version 1.0, Feb 8, 2014
 * @since 3.0.0
 */
@Controller
public class TaskController {

    private static final String AUTHENTICATOR_CODE = "authenticatorCode";
    private static final String REJECT = "reject";
    private static final String TASK_ACTIONS = "task_actions";
    private static final String FORM_EDITABLE = "form_editable";
    private static final String BUSINESS_KEY = "businessKey";
    private static final String ADMIN = "admin";
    private static final String ALERT_SUCCESS = "alertSuccess";
    private static final String ACTION = "action";
    private static final String FORM_DOCUMENT = "form_document";
    private static final String FORM_KEY = "form_key";
    private static final String SELECTED_ACTION = "selectedAction";
    private static final String REDIRECT_TASKS = "redirect:/tasks";
    private static final String REDIRECT_DOCUMENTS = "redirect:/documents";
    private static final String OPTIONAL = "optional";
    private static final String ACCEPT = "accept";
    private static Logger log = Logger.getLogger(TaskController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShowTaskSettings showTaskSettings;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private Environment env;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FormService formService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;

    @RequestMapping("/task/{taskId}/accept")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String acceptTask(
            @PathVariable("taskId") String taskId,
            @RequestParam(value = "redirect", required = false) String redirectActivity,
            @RequestParam(value = AUTHENTICATOR_CODE, required = false) String authenticatorCode,
            RedirectAttributes redirect, ModelMap model) {
        String userName = AppsUtil.getName();
        Task task = taskService.createTaskQuery().taskCandidateUser(userName)
                .taskId(taskId).singleResult();

        if (task == null) {
            return returnNullTask(redirect);
        }

        task.setDescription(ACCEPT);
        taskService.saveTask(task);
        boolean optional = (boolean) taskService.getVariable(task.getId(),
                OPTIONAL);

        return processCompleteTask(taskId, ACCEPT, redirectActivity, redirect,
                userName, task, optional);
    }

    @RequestMapping("/task/{taskId}/cancel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String cancelTask(
            @PathVariable("taskId") String taskId,
            @RequestParam(value = AUTHENTICATOR_CODE, required = false) String authenticatorCode,
            RedirectAttributes redirect, ModelMap model) {
        String userName = AppsUtil.getName();
        Task task = taskService.createTaskQuery().taskCandidateUser(userName)
                .taskId(taskId).singleResult();

        if (task == null) {
            return returnNullTask(redirect);
        }

        boolean optional = (boolean) taskService.getVariable(task.getId(),
                OPTIONAL);
        if (optional)
            return REDIRECT_DOCUMENTS;

        completeTask(taskId, "cancel", redirect, userName, task);

        return REDIRECT_TASKS;
    }

    @RequestMapping("/task/{taskId}/complete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String completeTask(
            @RequestParam Map<String, String> params,
            @PathVariable("taskId") String taskId,
            @RequestParam(value = "redirect", required = false) String redirectActivity,
            @RequestParam(value = AUTHENTICATOR_CODE, required = false) String authenticatorCode,
            RedirectAttributes redirect, ModelMap model) {

        String action = "complete";
        String userName = AppsUtil.getName();
        Task task = taskService.createTaskQuery().taskCandidateUser(userName)
                .taskId(taskId).singleResult();
        if (task == null) {
            return returnNullTask(redirect);
        }

        Map<String, Object> taskVars = taskService.getVariables(task.getId());
        String formDocument = (String) taskVars.get(FORM_DOCUMENT);

        boolean optional = (boolean) taskService.getVariable(task.getId(),
                OPTIONAL);

        Map<String, Object> args = new HashMap<>();
        args.put(ACTION, action);
        args.put("uuid", formDocument);
        args.put("requestParams", params);

        return processCompleteTask(taskId, action, redirectActivity, redirect,
                userName, task, optional);
    }

    /**
     * @param id
     * @param action
     * @param redirect
     * @param userName
     * @param task
     */
    private void completeTask(String id, String action,
            RedirectAttributes redirect, String userName, Task task) {
        if (action != null) {
            Map<String, Object> variables = new HashMap<>();
            variables.put(SELECTED_ACTION, action);
            taskService.claim(id, userName);
            taskService.setOwner(id, userName);
            taskService.complete(id, variables);
        } else {
            taskService.complete(id);
        }
        redirect.addFlashAttribute(ALERT_SUCCESS,
                "Completed task '" + task.getName() + "' for " + userName);

    }

    private void constructRedirectActivity(String redirectActivity,
            ModelMap model) {
        if (redirectActivity.contains(",")) {
            // parse multiple options
            List<String> redirectActivities = Arrays.asList(redirectActivity
                    .split(","));
            for (String redirectOption : redirectActivities) {
                if (StringUtils.startsWith(redirectOption, "accept:")) {
                    constructRedirectActivityModel(model, redirectOption,
                            "accept:", "acceptRedirect");
                } else if (StringUtils.startsWith(redirectOption, "reject:")) {
                    constructRedirectActivityModel(model, redirectOption,
                            "reject:", "rejectRedirect");
                } else if (StringUtils.startsWith(redirectOption, "complete:")) {
                    constructRedirectActivityModel(model, redirectOption,
                            "complete:", "completeRedirect");
                } else if (StringUtils.startsWith(redirectOption, "cancel:")) {
                    constructRedirectActivityModel(model, redirectOption,
                            "cancel:", "cancelRedirect");
                } else if (StringUtils.startsWith(redirectOption, "settle:")) {
                    constructRedirectActivityModel(model, redirectOption,
                            "settle:", "settleRedirect");
                } else if (StringUtils.startsWith(redirectOption, "revise:")) {
                    constructRedirectActivityModel(model, redirectOption,
                            "revise:", "reviseRedirect");
                } else if (StringUtils.startsWith(redirectOption, "request:")) {
                    constructRedirectActivityModel(model, redirectOption,
                            "request:", "requestRedirect");
                }
            }
        } else
            model.put("completeRedirect", redirectActivity);
    }

    private void constructRedirectActivityModel(ModelMap model,
            String redirectOption, String separator, String modelKey) {
        String chompRedirectActivity = StringUtils.substringAfter(
                redirectOption, separator);
        if (StringUtils.isNotBlank(chompRedirectActivity))
            model.put(modelKey, chompRedirectActivity);
    }

    /**
     * @param model
     * @param task
     */
    private void constructUserTaskRedirect(ModelMap model, Task task) {
        Map<String, Object> executionVars = runtimeService.getVariables(task
                .getExecutionId());
        String redirectUserTask = (String) executionVars
                .get("redirect_usertask");
        if (redirectUserTask != null && redirectUserTask.contains("|")) {
            String[] redirectUserTaskArr = redirectUserTask.split(Pattern
                    .quote("|"));
            String taskFrom = redirectUserTaskArr[0];
            String taskTo = redirectUserTaskArr[1];
            if (taskFrom.equals(task.getTaskDefinitionKey())) {
                constructRedirectActivity(taskTo, model);
            }
        }
    }

    @RequestMapping("/task/{taskId}/delete")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteTask(@PathVariable("taskId") String taskId,
            RedirectAttributes redirect) {
        String userName = AppsUtil.getName();
        Task task = taskService.createTaskQuery().taskCandidateUser(userName)
                .taskId(taskId).singleResult();

        if (task == null) {
            return returnNullTask(redirect);
        }

        completeTask(taskId, "delete", redirect, userName, task);
        redirect.addFlashAttribute(ALERT_SUCCESS,
                "Waiting for few minutes to process deleting Document ...");
        redirect.addFlashAttribute("isDeleteCpo", true);
        return REDIRECT_DOCUMENTS;

    }

    @RequestMapping("/task/{taskId}/edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String editTask(ModelMap model,
            @PathVariable("taskId") String taskId, RedirectAttributes redirect) {
        String userName = AppsUtil.getName();
        Task task = taskService.createTaskQuery().taskCandidateUser(userName)
                .taskId(taskId).singleResult();

        if (task == null) {
            return returnNullTask(redirect);
        }

        return processTask(taskId, model, "edit");
    }

    /**
     * @param id
     * @param action
     * @param redirectActivity
     * @param redirect
     * @param userName
     * @param task
     */
    private String processCompleteTask(String id, String action,
            String redirectActivity, RedirectAttributes redirect,
            String userName, Task task, boolean optional) {
        completeTask(id, action, redirect, userName, task);

        if (StringUtils.isNotBlank(redirectActivity)) {
            String taskName = redirectActivity;
            String act = "";
            if (redirectActivity.contains("/")) {
                String[] taskArr = redirectActivity.split(Pattern.quote("/"));
                taskName = taskArr[0];
                act = taskArr[1];
            }
            // wait for backend processing to complete
            int count = 0;
            while (true) {
                Execution execution = runtimeService.createExecutionQuery()
                        .processInstanceId(task.getProcessInstanceId())
                        .activityId(taskName).singleResult();
                if (execution != null) {
                    Task redirectTask = taskService
                            .createTaskQuery()
                            .taskCandidateUser(
                                    AppsUtil.getAuthentication().getName())
                            .executionId(execution.getId()).singleResult();
                    if (redirectTask != null) {
                        if (!"".equals(act)) {
                            return "redirect:/task/" + redirectTask.getId()
                                    + "/" + act;
                        } else {
                            return "redirect:/task/" + redirectTask.getId();
                        }
                    }
                }
                if (++count > 10) {
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    log.debug("error when processCompleteTask", e);
                    break;
                }
            }
        }
        if (optional)
            return REDIRECT_DOCUMENTS;

        return REDIRECT_TASKS;
    }

    @SuppressWarnings("unchecked")
    private String processTask(String taskId, ModelMap model, String action) {
        Task task;
        User user = AppsUtil.getActiveUser();
        task = taskService.createTaskQuery().taskId(taskId)
                .taskCandidateUser(user.getUsername()).singleResult();
        Map<String, Object> taskVars = taskService.getVariables(task.getId());
        String businessKey = (String) taskVars.get(BUSINESS_KEY);
        model.put(BUSINESS_KEY, businessKey);
        String formKey = (String) taskVars.get(FORM_KEY);
        String formDocument = (String) taskVars.get(FORM_DOCUMENT);
        Boolean formEditable = (Boolean) taskVars.get(FORM_EDITABLE);
        String redirectActivity = (String) taskVars.get("redirect_activity");

        Map<String, String> taskActions = null;
        for (FormProperty prop : formService.getTaskFormData(task.getId())
                .getFormProperties()) {
            if (TASK_ACTIONS.equals(prop.getId())) {
                taskActions = (Map<String, String>) prop.getType()
                        .getInformation("values");
            }
        }

        model.put("task", task);
        model.put(FORM_KEY, formKey);
        model.put(FORM_DOCUMENT, formDocument);
        model.put(FORM_EDITABLE, formEditable);
        if (taskActions != null) {
            model.put(TASK_ACTIONS, taskActions);
        }
        model.put(ACTION, action);
        if (redirectActivity != null)
            constructRedirectActivity(redirectActivity, model);

        constructUserTaskRedirect(model, task);

        return "task";
    }

    @RequestMapping("/task/{taskId}/reject")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String rejectTask(
            @PathVariable("taskId") String taskId,
            @RequestParam(value = "redirect", required = false) String redirectActivity,
            @RequestParam(value = AUTHENTICATOR_CODE, required = false) String authenticatorCode,
            RedirectAttributes redirect, ModelMap model) {
        String userName = AppsUtil.getName();
        Task task = taskService.createTaskQuery().taskCandidateUser(userName)
                .taskId(taskId).singleResult();

        if (task == null) {
            return returnNullTask(redirect);
        }
        task.setDescription(REJECT);
        taskService.saveTask(task);

        boolean optional = (boolean) taskService.getVariable(task.getId(),
                OPTIONAL);

        return processCompleteTask(taskId, REJECT, redirectActivity, redirect,
                userName, task, optional);
    }

    private String returnNullTask(RedirectAttributes redirect) {
        redirect.addFlashAttribute("alertWarning",
                "Task doesn't exists or already claimed.");
        return REDIRECT_TASKS;
    }

    @RequestMapping("/task/{taskId}")
    public String task(ModelMap model, @PathVariable("taskId") String taskId,
            @RequestParam(value = ACTION,
                    required = false,
                    defaultValue = "view") String action,
            RedirectAttributes redirect) {
        String userName = AppsUtil.getName();
        Task task = taskService.createTaskQuery().taskCandidateUser(userName)
                .taskId(taskId).singleResult();

        if (task == null) {
            return returnNullTask(redirect);
        }

        return processTask(taskId, model, action);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("/task/job/{taskId}")
    public String taskJob(ModelMap model,
            @PathVariable("taskId") String taskId,
            @RequestParam(value = ACTION,
                    required = false,
                    defaultValue = "view") String action) {
        Task task;
        User user = AppsUtil.getActiveUser();
        task = taskService.createTaskQuery().taskId(taskId)
                .taskCandidateUser(user.getUsername()).singleResult();

        String businessKey = (String) taskService.getVariable(task.getId(),
                BUSINESS_KEY);
        model.put(BUSINESS_KEY, businessKey);
        String formKey = (String) taskService.getVariable(task.getId(),
                FORM_KEY);
        String formDocument = (String) taskService.getVariable(task.getId(),
                FORM_DOCUMENT);
        Boolean formEditable = (Boolean) taskService.getVariable(task.getId(),
                FORM_EDITABLE);
        String redirectActivity = (String) taskService.getVariable(
                task.getId(), "redirect_activity");

        String parserMessage = (String) taskService.getVariable(task.getId(),
                "parserMessage");

        Long jobId = (Long) taskService.getVariable(task.getId(),
                JobLauncherDetails.JOB_DELEGATOR);

        Map<String, String> taskActions = null;
        for (FormProperty prop : formService.getTaskFormData(task.getId())
                .getFormProperties()) {
            if (TASK_ACTIONS.equals(prop.getId())) {
                taskActions = (Map<String, String>) prop.getType()
                        .getInformation("values");
            }
        }

        model.put("task", task);
        model.put(FORM_KEY, formKey);
        model.put(FORM_DOCUMENT, formDocument);
        model.put(FORM_EDITABLE, formEditable);
        model.put("parserMessage", parserMessage);
        model.put("jobId", jobId);
        if (taskActions != null) {
            model.put(TASK_ACTIONS, taskActions);
        }
        model.put(ACTION, action);
        if (redirectActivity != null)
            constructRedirectActivity(redirectActivity, model);

        return "taskJob";
    }

    @RequestMapping("/tasks")
    public String tasks(ModelMap model, @RequestParam(required = false,
            defaultValue = "0") int start, @RequestParam(value = BUSINESS_KEY,
            required = false) String businessKey) {
        User user = AppsUtil.getActiveUser();
        TaskQuery taskQuery;
        if (ADMIN.equalsIgnoreCase(user.getUsername())) {
            // admin user can see any task
            if (!showTaskSettings.getShowHideTask()) {
                if (StringUtils.isBlank(businessKey)) {
                    taskQuery = taskService.createTaskQuery()
                            .taskVariableValueNotEquals(OPTIONAL, true)
                            .orderByTaskCreateTime().desc();
                } else {
                    taskQuery = taskService
                            .createTaskQuery()
                            .includeProcessVariables()
                            .processVariableValueLike(BUSINESS_KEY,
                                    businessKey + "%").orderByTaskCreateTime()
                            .desc();
                }
            } else {
                List<UserRole> userRoles = userRoleRepository.findByUser(user);
                List<String> listRole = new ArrayList<>();

                for (UserRole userRole : userRoles) {
                    listRole.add("ROLE_" + userRole.getRole());
                }
                if (StringUtils.isBlank(businessKey)) {
                    taskQuery = taskService.createTaskQuery()
                            .taskCandidateGroupIn(listRole)
                            .taskVariableValueNotEquals(OPTIONAL, true)
                            .orderByTaskCreateTime().desc();
                } else {
                    taskQuery = taskService
                            .createTaskQuery()
                            .includeProcessVariables()
                            .taskCandidateGroupIn(listRole)
                            .processVariableValueLike(BUSINESS_KEY,
                                    businessKey + "%")
                            .taskVariableValueNotEquals(OPTIONAL, true)
                            .orderByTaskCreateTime().desc();
                }

            }

        } else {
            if (StringUtils.isBlank(businessKey))
                taskQuery = taskService.createTaskQuery()
                        .taskCandidateUser(user.getUsername())
                        .taskVariableValueNotEquals(OPTIONAL, true)
                        .orderByTaskCreateTime().desc();
            else {
                taskQuery = taskService
                        .createTaskQuery()
                        .includeProcessVariables()
                        .processVariableValueLike(BUSINESS_KEY,
                                businessKey + "%")
                        .taskCandidateUser(user.getUsername())
                        .taskVariableValueNotEquals(OPTIONAL, true)
                        .orderByTaskCreateTime().desc();
            }
        }
        long count = taskQuery.count();

        List<Task> tasks = taskQuery.listPage(start, 10);

        Map<String, String> businessKeys = new HashMap<>();
        for (Task task : tasks) {
            businessKeys.put(task.getId(), (String) taskService.getVariable(
                    task.getId(), BUSINESS_KEY));
        }
        model.put("businessKeys", businessKeys);
        model.put("start", start);
        model.put("count", count);
        model.put("tasks", tasks);
        return "tasks";
    }

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String updateTask(ModelMap model,
            @RequestParam Map<String, String> params,
            @RequestParam String taskId, RedirectAttributes redirect) {
        String uuid = params.get(FORM_DOCUMENT);
        String userName = AppsUtil.getName();
        Task task = taskService.createTaskQuery().taskCandidateUser(userName)
                .taskId(taskId).singleResult();

        if (task == null) {
            return returnNullTask(redirect);
        }

        Map<String, Object> args = new HashMap<>();
        args.put(ACTION, "update");
        args.put("uuid", uuid);
        args.put("requestParams", params);

        return processTask(taskId, model, "edit");
    }

}
