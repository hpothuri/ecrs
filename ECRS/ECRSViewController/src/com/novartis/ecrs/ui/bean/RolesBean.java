package com.novartis.ecrs.ui.bean;

import com.novartis.ecrs.ui.utility.ADFUtils;

import javax.faces.application.FacesMessage;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewObject;

public class RolesBean {
    public RolesBean() {
    }

    public String editRole() {
        String returnValue = (String)ADFUtils.invokeEL("#{controllerContext.currentViewPort.taskFlowContext.trainModel.getNext}");
        return returnValue;
    }

    public String createRole() {
        ADFUtils.invokeEL("#{bindings.CreateInsert.execute}");
        String returnValue = (String)ADFUtils.invokeEL("#{controllerContext.currentViewPort.taskFlowContext.trainModel.getNext}");
        return returnValue;
    }

    public String onSearch() {
        String roleName = (String)ADFUtils.evaluateEL("#{bindings.RoleName.inputValue}");
        Integer roleId = (Integer)ADFUtils.evaluateEL("#{bindings.RoleId.inputValue}");
        DCIteratorBinding iter = ADFUtils.findIterator("CrsRolesVOIterator");
        ViewObject rolesVO = iter.getViewObject();
        ViewCriteria vc = rolesVO.getViewCriteriaManager().getViewCriteria("searchRoles");
        rolesVO.applyViewCriteria(vc);
        rolesVO.setNamedWhereClauseParam("bindRoleId", roleId);
        rolesVO.setNamedWhereClauseParam("bindRoleName", roleName);
        rolesVO.executeQuery();
        return null;
    }

    public String onClickBack() {
        ADFUtils.invokeEL("#{bindings.Rollback.execute}");
        ADFUtils.invokeEL("#{bindings.CreateInsert.execute}");
        String returnValue = (String)ADFUtils.invokeEL("#{controllerContext.currentViewPort.taskFlowContext.trainModel.getPrevious}");
        return returnValue;
    }

    public String onSave() {
        ADFUtils.invokeEL("#{bindings.Commit.execute}");
        ADFUtils.showFacesMessage("Saved Successfully.", FacesMessage.SEVERITY_INFO);
        return null;
    }
}
