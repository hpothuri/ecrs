package com.novartis.ecrs.ui.bean;

import com.novartis.ecrs.ui.utility.ADFUtils;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import oracle.adf.model.binding.DCIteratorBinding;

import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewObject;

public class CompoundsBean {
    public CompoundsBean() {
    }

    public void searchCompounds(ActionEvent actionEvent) {
        String compoundType = (String)ADFUtils.evaluateEL("#{bindings.CompoundType.inputValue}");
        String compoundCode = (String)ADFUtils.evaluateEL("#{bindings.CompoundCode.inputValue}");
        Integer compoundId = (Integer)ADFUtils.evaluateEL("#{bindings.CompoundId.inputValue}");
        DCIteratorBinding iter = ADFUtils.findIterator("CrsCompoundVOIterator");
        ViewObject compoundVO = iter.getViewObject();
        ViewCriteria vc = compoundVO.getViewCriteriaManager().getViewCriteria("searchCompounds");
        compoundVO.applyViewCriteria(vc);
        compoundVO.setNamedWhereClauseParam("bindCompoundType", compoundType);
        compoundVO.setNamedWhereClauseParam("bindCompoundCode", compoundCode);
        compoundVO.setNamedWhereClauseParam("bindCompoundId", compoundId);
        compoundVO.executeQuery();
    }

    public String editCompound() {
        String returnValue = (String)ADFUtils.invokeEL("#{controllerContext.currentViewPort.taskFlowContext.trainModel.getNext}");
        return returnValue;
    }

    public String createCompound() {
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
