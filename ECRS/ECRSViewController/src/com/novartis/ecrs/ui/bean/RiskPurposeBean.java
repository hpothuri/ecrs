package com.novartis.ecrs.ui.bean;

import com.novartis.ecrs.ui.utility.ADFUtils;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewObject;

public class RiskPurposeBean {
    public RiskPurposeBean() {
    }

    public String editRiskPurpose() {
        String returnValue = (String)ADFUtils.invokeEL("#{controllerContext.currentViewPort.taskFlowContext.trainModel.getNext}");
        return returnValue;
    }

    public String createRiskPurpose() {
        ADFUtils.invokeEL("#{bindings.CreateInsert.execute}");
        String returnValue = (String)ADFUtils.invokeEL("#{controllerContext.currentViewPort.taskFlowContext.trainModel.getNext}");
        return returnValue;
    }

    public String onSearch() {
        String riskPurposeCode = (String)ADFUtils.evaluateEL("#{bindings.RiskPurposeCode.inputValue}");
        Integer riskPurposeId = (Integer)ADFUtils.evaluateEL("#{bindings.RiskPurposeId.inputValue}");
        DCIteratorBinding iter = ADFUtils.findIterator("CrsRiskPurposesVOIterator");
        ViewObject riskPurposeVO = iter.getViewObject();
        ViewCriteria vc = riskPurposeVO.getViewCriteriaManager().getViewCriteria("searchRiskPurpose");
        riskPurposeVO.applyViewCriteria(vc);
        riskPurposeVO.setNamedWhereClauseParam("bindRiskPurposeCode", riskPurposeCode);
        riskPurposeVO.setNamedWhereClauseParam("bindRiskPurposeId", riskPurposeId);
        riskPurposeVO.executeQuery();
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
