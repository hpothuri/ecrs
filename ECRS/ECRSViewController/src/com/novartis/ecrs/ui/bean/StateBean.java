package com.novartis.ecrs.ui.bean;

import com.novartis.ecrs.ui.utility.ADFUtils;

import javax.faces.application.FacesMessage;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewObject;

public class StateBean {
    public StateBean() {
    }

    public String editState() {
        String returnValue = (String)ADFUtils.invokeEL("#{controllerContext.currentViewPort.taskFlowContext.trainModel.getNext}");
        return returnValue;
    }

    public String createState() {
        ADFUtils.invokeEL("#{bindings.CreateInsert.execute}");
        String returnValue = (String)ADFUtils.invokeEL("#{controllerContext.currentViewPort.taskFlowContext.trainModel.getNext}");
        return returnValue;
    }

    public String onSearch() {
        String stateName = (String)ADFUtils.evaluateEL("#{bindings.StateName.inputValue}");
        Integer stateId = (Integer)ADFUtils.evaluateEL("#{bindings.StateId.inputValue}");
        DCIteratorBinding iter = ADFUtils.findIterator("CrsStateVOIterator");
        ViewObject statesVO = iter.getViewObject();
        ViewCriteria vc = statesVO.getViewCriteriaManager().getViewCriteria("searchStates");
        statesVO.applyViewCriteria(vc);
        statesVO.setNamedWhereClauseParam("bindStateId", stateId);
        statesVO.setNamedWhereClauseParam("bindStateName", stateName);
        statesVO.executeQuery();
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
