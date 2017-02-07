package com.yd.activiti;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.el.Expression;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author edys
 * @version 1.0, Feb 8, 2014
 * @since 3.0.0
 */
public class SetVariablesForUserTaskListener implements TaskListener {

    private static final long serialVersionUID = -6451344788318693122L;

    @SuppressWarnings("unused")
    private static Logger log = Logger
            .getLogger("SetVariablesForUserTaskListener");

    private Expression docstoreRole;

    private Expression formDocument;

    private Expression formKey;

    private Expression formEditable;

    private Expression redirectActivity;

    private Expression verifyAuthenticatorCode;

    private Expression registerDocstore;

    private Expression relationType;

    private Expression perspective;

    private Expression optional;

    @Override
    public void notify(DelegateTask task) {
        @SuppressWarnings("unchecked")
        Map<String, UUID> associateDocstores = (Map<String, UUID>) task
                .getVariable("associate_docstores");

        String docstoreRoleValue = (String) docstoreRole.getValue(task);
        String formDocumentValue = (String) formDocument.getValue(task);
        String registerDocstoreValue = null;
        if (registerDocstore != null) {
            registerDocstoreValue = (String) registerDocstore.getValue(task);
        }
        String relationTypeValue = null;
        if (relationType != null) {
            relationTypeValue = (String) relationType.getValue(task);
        }
        String perspectiveValue = null;
        if (perspective != null) {
            perspectiveValue = (String) perspective.getValue(task);
        }
        String formKeyValue = (String) formKey.getValue(task);
        String[] docstoreNames = StringUtils.split(docstoreRoleValue, "|");
        boolean formEditableValue = Boolean.parseBoolean((String) formEditable
                .getValue(task));

        boolean optionalValue = false;
        if (optional != null)
            optionalValue = Boolean.parseBoolean((String) optional
                    .getValue(task));

        Map<String, Object> variables = new HashMap<>();
        for (String name : docstoreNames) {
            if (associateDocstores.containsKey(name)) {
                variables.put("docstore" + StringUtils.capitalize(name),
                        associateDocstores.get(name));
            } else {
                throw new IllegalArgumentException("Role " + name
                        + " does not exist in associate docstores: "
                        + associateDocstores);
            }
        }

        variables.put("associate_docstores", associateDocstores);
        variables.put("form_document", formDocumentValue);
        variables.put("form_key", formKeyValue);
        variables.put("form_editable", formEditableValue);
        variables.put("register_docstore", registerDocstoreValue);
        variables.put("relation_type", relationTypeValue);
        variables.put("perspective", perspectiveValue);
        variables.put("optional", optionalValue);
        if (redirectActivity != null) {
            String redirectActivityValue = (String) redirectActivity
                    .getValue(task);
            variables.put("redirect_activity", redirectActivityValue);
        }
        if (verifyAuthenticatorCode != null) {
            String verifyAuthenticatorCodeValue = (String) verifyAuthenticatorCode
                    .getValue(task);
            variables.put("verify_authenticator_code",
                    verifyAuthenticatorCodeValue);
        }
        task.setVariablesLocal(variables);
    }

    public void setDocstoreRole(Expression docstoreRole) {
        this.docstoreRole = docstoreRole;
    }

    public void setFormDocument(Expression formDocument) {
        this.formDocument = formDocument;
    }

    public void setFormEditable(Expression formEditable) {
        this.formEditable = formEditable;
    }

    public void setFormKey(Expression formKey) {
        this.formKey = formKey;
    }

    /**
     * @param perspective
     *            the perspective to set
     */
    public void setPerspective(Expression perspective) {
        this.perspective = perspective;
    }

    public void setRedirectActivity(Expression redirectActivity) {
        this.redirectActivity = redirectActivity;
    }

    /**
     * @param registerDocstore
     *            the registerDocstore to set
     */
    public void setRegisterDocstore(Expression registerDocstore) {
        this.registerDocstore = registerDocstore;
    }

    /**
     * @param relationType
     *            the relationType to set
     */
    public void setRelationType(Expression relationType) {
        this.relationType = relationType;
    }

    public void setVerifyAuthenticatorCode(Expression verifyAuthenticatorCode) {
        this.verifyAuthenticatorCode = verifyAuthenticatorCode;
    }

}
