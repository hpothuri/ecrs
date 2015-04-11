package com.novartis.ecrs.ui.bean;


import com.novartis.ecrs.model.view.CrsContentVORowImpl;
import com.novartis.ecrs.ui.utility.ADFUtils;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;

import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;

import oracle.binding.OperationBinding;


public class ManageCRSBean implements Serializable {
    @SuppressWarnings("compatibility:7725300081501535999")
    private static final long serialVersionUID = 2040469805807166652L;

    public ManageCRSBean() {
        super();
    }
    
    private String flowType;

    /**
     */
    public void getCrsFlowType() {
        // Add event code here...
        if (ADFUtils.evaluateEL("#{pageFlowScope.flowType}") != null) {
            flowType =
                    (String)ADFUtils.evaluateEL("#{pageFlowScope.flowType}");
        }
    }

    /**
     * @param flowType
     */
    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    /**
     * @return
     */
    public String getFlowType() {
        return flowType;
    }

    /**
     * Invoke commit operation of DB.
     * @param actionEvent
     */
    public void onClickCreateSave(ActionEvent actionEvent) {
        // Add event code here...
        ADFUtils.invokeEL("#{bindings.Commit.execute}");
        ADFUtils.showFacesMessage("Record saved successfully.", FacesMessage.SEVERITY_INFO);
    }

    /**
     * Creates a row in CRS_CONTENT table.
     */
    public void createCrsRow() {
        // Add event code here...
        DCBindingContainer bc =  ADFUtils.findBindingContainerByName("com_novartis_ecrs_view_createCRSPageDef");
        OperationBinding ob =  bc.getOperationBinding("CreateInsert");
        ob.execute();

        DCIteratorBinding iter = bc.findIteratorBinding("CrsContentVOIterator");

        if (iter != null && iter.getCurrentRow() != null){
            CrsContentVORowImpl row = (CrsContentVORowImpl)iter.getCurrentRow();
            row.setStateId(1);
            row.setReleaseStatusFlag("P");
            row.setCrsEffectiveDt(ADFUtils.getJBOTimeStamp());
        }
    }
}
