package com.novartis.ecrs.ui.bean;

import com.novartis.ecrs.ui.utility.ADFUtils;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewObject;

public class DomainsBean {
    public DomainsBean() {
    }
    
    public void searchDomains(ActionEvent actionEvent) {
        String domainName = (String)ADFUtils.evaluateEL("#{bindings.DomainName.inputValue}");
        Integer domainId = (Integer)ADFUtils.evaluateEL("#{bindings.DomainId.inputValue}");
        DCIteratorBinding iter = ADFUtils.findIterator("CrsDomainsVOIterator");
        ViewObject compoundVO = iter.getViewObject();
        ViewCriteria vc = compoundVO.getViewCriteriaManager().getViewCriteria("searchDomains");
        compoundVO.applyViewCriteria(vc);
        compoundVO.setNamedWhereClauseParam("bindDomainName", domainName);
        compoundVO.setNamedWhereClauseParam("bindDomainId", domainId);
        compoundVO.executeQuery();
    }

    public String editDomain() {
        String returnValue = (String)ADFUtils.invokeEL("#{controllerContext.currentViewPort.taskFlowContext.trainModel.getNext}");
        return returnValue;
    }

    public String createDomain() {
        ADFUtils.invokeEL("#{bindings.CreateInsert.execute}");
        String returnValue = (String)ADFUtils.invokeEL("#{controllerContext.currentViewPort.taskFlowContext.trainModel.getNext}");
        return returnValue;
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
