package com.novartis.ecrs.ui.bean;


import com.novartis.ecrs.model.view.CrsContentVORowImpl;
import com.novartis.ecrs.ui.constants.ViewConstants;
import com.novartis.ecrs.ui.utility.ADFUtils;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;

import oracle.binding.OperationBinding;


public class ManageCRSBean implements Serializable {
    @SuppressWarnings("compatibility:7725300081501535999")
    private static final long serialVersionUID = 2040469805807166652L;
    private List<SelectItem> designeeList;
    private List<String> selDesigneeList;

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
        if(selDesigneeList != null && selDesigneeList.size() > 0){
            String designees = "";
            for(String des : selDesigneeList){
                designees = designees + "," + des;
            }
            ADFUtils.setEL("#{bindings.Designee.inputValue}", designees.substring(1));
        }
        //TODO check with Donna
        //if compound type is non-compound then set marketed flag to 'N' 
        if(ADFUtils.evaluateEL("#{bindings.CompoundType.inputValue}")!=null ){
            String compType = (String)ADFUtils.evaluateEL("#{bindings.CompoundType.inputValue}");
            if(ViewConstants.COMP_TYPE_NON_COMPOUND.equals(compType))
                ADFUtils.setEL("#{bindings.IsMarketedFlag.inputValue}", "N");
        }
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

    public void setDesigneeList(List<SelectItem> designeeList) {
        this.designeeList = designeeList;
    }

    public List<SelectItem> getDesigneeList() {
        if(designeeList == null){
            designeeList = new ArrayList<SelectItem>();
            DCBindingContainer bc = ADFUtils.getDCBindingContainer();
            OperationBinding ob = bc.getOperationBinding("fetchDesignees");
            List<String> designees = (List<String>)ob.execute();
            if(designees != null && designees.size() > 0){
                for(String designee : designees){
                    SelectItem item = new SelectItem(designee, designee);
                    designeeList.add(item);
                }
            }
        }
        return designeeList;
    }

    public void setSelDesigneeList(List<String> selDesigneeList) {
        this.selDesigneeList = selDesigneeList;
    }

    public List<String> getSelDesigneeList() {
        return selDesigneeList;
    }
}
