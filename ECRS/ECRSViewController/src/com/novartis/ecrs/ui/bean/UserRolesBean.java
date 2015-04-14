package com.novartis.ecrs.ui.bean;

import com.novartis.ecrs.ui.utility.ADFUtils;

import javax.faces.application.FacesMessage;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewObject;

public class UserRolesBean {
    public UserRolesBean() {
    }

    public String editUserRole() {
        String returnValue = (String)ADFUtils.invokeEL("#{controllerContext.currentViewPort.taskFlowContext.trainModel.getNext}");
        return returnValue;
    }

    public String createUserRole() {
        ADFUtils.invokeEL("#{bindings.CreateInsert.execute}");
        String returnValue = (String)ADFUtils.invokeEL("#{controllerContext.currentViewPort.taskFlowContext.trainModel.getNext}");
        return returnValue;
    }

    public String onSearch() {
        String userName = (String)ADFUtils.evaluateEL("#{bindings.UserName.inputValue}");
        Integer roleId = (Integer)ADFUtils.evaluateEL("#{bindings.RoleId.inputValue}");
        DCIteratorBinding iter = ADFUtils.findIterator("CrsUserRolesVOIterator");
        ViewObject userRolesVO = iter.getViewObject();
        ViewCriteria vc = userRolesVO.getViewCriteriaManager().getViewCriteria("searchUserRoles");
        userRolesVO.applyViewCriteria(vc);
        userRolesVO.setNamedWhereClauseParam("bindRoleId", roleId);
        userRolesVO.setNamedWhereClauseParam("bindUserName", userName);
        userRolesVO.executeQuery();
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
