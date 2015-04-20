package com.novartis.ecrs.ui.bean;


import com.novartis.ecrs.model.constants.ModelConstants;
import com.novartis.ecrs.model.view.CrsContentVORowImpl;
import com.novartis.ecrs.ui.constants.ViewConstants;
import com.novartis.ecrs.ui.utility.ADFUtils;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.datatransfer.DataFlavor;
import oracle.adf.view.rich.datatransfer.Transferable;
import oracle.adf.view.rich.dnd.DnDAction;
import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.DropEvent;
import oracle.adf.view.rich.event.PopupCanceledEvent;

import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.RowKeySet;


public class ManageCRSBean implements Serializable {
    @SuppressWarnings("compatibility:7725300081501535999")
    private static final long serialVersionUID = 2040469805807166652L;
    private List<SelectItem> designeeList;
    private List<String> selDesigneeList;
    private String selectedCrsName;
    private transient RichPopup successPopupBinding;
    private transient RichPopup riskDefPopup;
    private transient RichTable riskDefTable;
    private transient RichPopup successPopup;
    private List<String> selDatabases;
    private List<SelectItem> databaseList;
    private List<String> selRiskPurposes;
    private transient RichPopup reviewSubmitPopup;
    private transient RichSelectOneChoice crsStateSOC;
    private transient RichSelectOneChoice crsStatusSOC;
    private transient RichPopup crsApprovePopup;
    private transient RichPanelBox workflowPanelBox;
    private transient RichPopup crsRejectPopup;
    private transient RichInputText taslCommentsInputText;
    private transient RichInputText mlCommentsInputText;
    private String dictionary;
    private String level;
    private String term;
    private transient RichPopup hierPopup;
    private boolean crsFieldsUpdatable;
    private transient RichPopup crsRetirePopup;
    private transient RichPopup crsReactivatePopup;
    private transient RichPopup crsReviewedPopup;


    public ManageCRSBean() {
        super();
        getUserRole();
        getCrsFlowType();
    }
    
    private String flowType;
    private boolean inboxDisable;
    private String loggedInUserRole;
 //   private String bslFacetName;
    private String relStatusForAnonymous;
    private String userName;
    
    /**
     * Set the flowType to bean variable and initailize the bsl facet name with 
     * appropriate value.
     */
    public void getCrsFlowType() {
        // Add event code here...
        if (ADFUtils.evaluateEL("#{pageFlowScope.flowType}") != null) {
            flowType =
                    (String)ADFUtils.evaluateEL("#{pageFlowScope.flowType}");
//            if (ViewConstants.FLOW_TYPE_SEARCH.equals(flowType) ||
//                ModelConstants.ROLE_BSL.equals(loggedInUserRole))
//                setBslFacetName("BSL");
//            else
//                setBslFacetName("nonBSL");
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
        } else
            ADFUtils.setEL("#{bindings.Designee.inputValue}",null);

        OperationBinding oper = ADFUtils.findOperation("Commit");
        oper.execute();
        if (oper.getErrors().size() > 0) 
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
        else{
            ADFUtils.showPopup(getSuccessPopupBinding());
            ADFUtils.addPartialTarget(getWorkflowPanelBox());
        }
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
            row.setStateId(ModelConstants.STATE_DRAFT);
            row.setReleaseStatusFlag("P");
            row.setCrsEffectiveDt(ADFUtils.getJBOTimeStamp());
        }
    }

    /**
     * @param designeeList
     */
    public void setDesigneeList(List<SelectItem> designeeList) {
        this.designeeList = designeeList;
    }

    /**
     * @return
     */
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

    /**
     * @param selDesigneeList
     */
    public void setSelDesigneeList(List<String> selDesigneeList) {
        this.selDesigneeList = selDesigneeList;
    }

    /**
     * @return
     */
    public List<String> getSelDesigneeList() {
        return selDesigneeList;
    }

    /**
     * Invokes AMImpl method to filter CRSContent vo with entered search criteria.
     * @param actionEvent
     */
    public void onClickSearch(ActionEvent actionEvent) {
        // TODO get user name
        //set release staus to binding
        if ("anonymous".equals(userName)) {
            ADFUtils.setEL("#{bindings.ReleaseStatus.inputValue}",
                           getRelStatusForAnonymous());
        }
        DCBindingContainer bc = ADFUtils.getDCBindingContainer();
        OperationBinding ob = bc.getOperationBinding("filterCRSContent");
        ob.getParamsMap().put("userInRole", loggedInUserRole);
        ob.getParamsMap().put("userName", "");
        ob.getParamsMap().put("isInboxDisable", isInboxDisable());
        ob.execute();
        if (ob.getErrors().size() > 0)
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.",
                                      FacesMessage.SEVERITY_ERROR);
        //TODO log the error 
    }

    /**
     * Invokes execute empty row set on crs content vo.
     */
    public void invokeEmptyRowSetOnContentVO() {
        // Add event code here...
        DCBindingContainer bc =
            ADFUtils.findBindingContainerByName(ViewConstants.PAGE_DEF_SEARCH);
        DCIteratorBinding iter = bc.findIteratorBinding("CrsContentVOIterator");
        if (iter.getViewObject() != null)
            iter.getViewObject().executeEmptyRowSet();
    }

    /**
     * Custom selection listener to populate crs name and designee list.
     * @param selectionEvent
     */
    public void searchTableSelectionListener(SelectionEvent selectionEvent) {
        // Add event code here...
        ADFUtils.invokeEL("#{bindings.CrsContentVO.collectionModel.makeCurrent}", new Class[] {SelectionEvent.class},
                                 new Object[] { selectionEvent });
        // get the selected row , by this you can get any attribute of that row
        CrsContentVORowImpl selectedRow =
                   (CrsContentVORowImpl)ADFUtils.evaluateEL("#{bindings.CrsContentVOIterator.currentRow}");
        setSelectedCrsName(selectedRow.getCrsName());
        setSelDesigneeList(null);
        List<String> designeeList = new ArrayList<String>();
        if (selectedRow.getDesignee() != null) {
            String[] designeeArray = selectedRow.getDesignee().split("[,]");
            if (designeeArray.length > 0) {
                for (int i = 0; i < designeeArray.length; i++) {
                    designeeList.add(designeeArray[i]);
                }
            }
            setSelDesigneeList(designeeList);
        }
    }

    /**
     * @param selectedCrsName
     */
    public void setSelectedCrsName(String selectedCrsName) {
        this.selectedCrsName = selectedCrsName;
    }

    /**
     * @return
     */
    public String getSelectedCrsName() {
        return selectedCrsName;
    }
    
    /**********************************************************************************************************************/

    /**
     * @return
     */
    public String onClickNext() {
        String crsName = (String)ADFUtils.evaluateEL("#{bindings.CrsName.inputValue}");
        Long crsId = (Long)ADFUtils.evaluateEL("#{bindings.CrsId.inputValue}");
        ADFUtils.setPageFlowScopeValue("crsId", crsId);
        ADFUtils.setPageFlowScopeValue("crsName", crsName);
        Map params = new HashMap<String, Object>();
        params.put("crsId", crsId);
        try {
            ADFUtils.executeAction("initRiskRelation", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String returnValue = (String)ADFUtils.invokeEL("#{controllerContext.currentViewPort.taskFlowContext.trainModel.getNext}");
        return returnValue;
    }

    public void onSelectInbox(ValueChangeEvent vce) {
        // Add event code here...
        if (vce!=null) {
            if (vce.getNewValue() != null &&
                !vce.getNewValue().equals(vce.getOldValue()) &&
                (Boolean)vce.getNewValue()) {
                //if (!ModelConstants.ROLE_BSL.equals(loggedInUserRole)) {
                    setInboxDisable(Boolean.TRUE);
               // }
            } else
                setInboxDisable(Boolean.FALSE);

            ADFUtils.addPartialTarget(vce.getComponent().getParent().getParent());
        }
    }

    /**
     * @param inboxDisable
     */
    public void setInboxDisable(boolean inboxDisable) {
        this.inboxDisable = inboxDisable;
    }

    /**
     * @return
     */
    public boolean isInboxDisable() {
        return inboxDisable;
    }

    /**
     * @param loggedInUserRole
     */
    public void setLoggedInUserRole(String loggedInUserRole) {
        this.loggedInUserRole = loggedInUserRole;
    }

    /**
     * @return
     */
    public String getLoggedInUserRole() {
        return loggedInUserRole;
    }

    /**
     * Set null to trade,generic,indication and isMarketed attributes.
     * @param vce
     */
    public void onCompCodeSelect(ValueChangeEvent vce) {
        // Add event code here...
        if (vce != null) {
            vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
            if (vce.getNewValue() != null &&
                !vce.getNewValue().equals(vce.getOldValue()) &&
                ViewConstants.NON_COMPOUND_ROUTINE.equals(ADFUtils.evaluateEL("#{bindings.CompoundCode.inputValue}"))) {
                ADFUtils.setEL("#{bindings.TradeName.inputValue}", null);
                ADFUtils.setEL("#{bindings.GenericName.inputValue}", null);
                ADFUtils.setEL("#{bindings.Indication.inputValue}", null);
                //TODO make this enable when isMarketedFlag null
                ADFUtils.setEL("#{bindings.IsMarketedFlag.inputValue}", "N");
            }
            //set logged in user name to bsl binding
            if (ViewConstants.FLOW_TYPE_CREATE.equals(flowType) &&
                ModelConstants.ROLE_BSL.equals(loggedInUserRole))
                ADFUtils.setEL("#{bindings.BslName.inputValue}",
                               getUserName());
        }
    }

    /**
     * @param successPopupBinding
     */
    public void setSuccessPopupBinding(RichPopup successPopupBinding) {
        this.successPopupBinding = successPopupBinding;
    }

    /**
     * @return
     */
    public RichPopup getSuccessPopupBinding() {
        return successPopupBinding;
    }

    /**
     * Invoked from constructor and intializes the userRole variable.
     */
    private void getUserRole() {
        if (ADFUtils.evaluateEL("#{sessionBean.userRole}") != null) {
            loggedInUserRole =
                    (String)ADFUtils.evaluateEL("#{sessionBean.userRole}");
        }
        if (ADFUtils.evaluateEL("#{securityContext.userName}") != null) {
            setUserName((String)ADFUtils.evaluateEL("#{securityContext.userName}"));
        }
    }

    public void addRiskDefinition(ActionEvent actionEvent) {
        DCIteratorBinding realtionIter = ADFUtils.findIterator("CrsRiskRelationVOIterator");
        ViewObject relationVO = realtionIter.getViewObject();
        Row relationRow = relationVO.createRow();
        Long crsId = (Long)ADFUtils.getPageFlowScopeValue("crsId");
        relationRow.setAttribute("CrsId", crsId);
        relationVO.insertRow(relationRow);
        relationVO.setCurrentRow(relationRow);
        ADFUtils.setPageFlowScopeValue("popupMode", "Add");
        setSelDatabases(null);
        setSelRiskPurposes(null);
        ADFUtils.showPopup(riskDefPopup);
    }

    public void setRiskDefPopup(RichPopup riskDefPopup) {
        this.riskDefPopup = riskDefPopup;
    }

    public RichPopup getRiskDefPopup() {
        return riskDefPopup;
    }

    public void editRiskDefinition(ActionEvent actionEvent) {
        ADFUtils.setPageFlowScopeValue("popupMode", "Edit");
        Long riskId = (Long)ADFUtils.evaluateEL("#{row.CrsRiskId}");
        String databaseList = (String)ADFUtils.evaluateEL("#{row.DatabaseList}");
        List<String> dbList = new ArrayList<String>();
        if(databaseList != null){
            String split[] = databaseList.split(",");
            for(String db : split){
                dbList.add(db);
            }
        }
        setSelDatabases(dbList);
        String riskPurposeList = (String)ADFUtils.evaluateEL("#{row.RiskPurposeList}");
        List<String> rpList = new ArrayList<String>();
        if(riskPurposeList != null){
            if(riskPurposeList.endsWith(",")){
                riskPurposeList = riskPurposeList.substring(0, riskPurposeList.length()-1);
            }
            String split[] = riskPurposeList.split(",");
            for(String rp : split){
                rpList.add(rp);
            }
        }
        setSelRiskPurposes(rpList);
        
        Map params = new HashMap<String, Object>();
        params.put("rowKey", riskId);
        try {
            ADFUtils.executeAction("setCurrentRiskRelation", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ADFUtils.showPopup(riskDefPopup);
    }

    public void deleteRiskDefinitions(ActionEvent actionEvent) {
        RowKeySet rowKeySet = (RowKeySet)riskDefTable.getSelectedRowKeys();
        CollectionModel cm = (CollectionModel)riskDefTable.getValue();
        for (Object facesTreeRowKey : rowKeySet) {
            cm.setRowKey(facesTreeRowKey);
            JUCtrlHierNodeBinding rowData = (JUCtrlHierNodeBinding)cm.getRowData();
            rowData.getRow().remove();
        }
    }

    public void setRiskDefTable(RichTable riskDefTable) {
        this.riskDefTable = riskDefTable;
    }

    public RichTable getRiskDefTable() {
        return riskDefTable;
    }

    public void saveRiskDefs(ActionEvent actionEvent) {
        if(selDatabases != null && selDatabases.size() > 0){
            String databases = "";
            for(String db : selDatabases){
                databases = databases + "," + db;
            }
            ADFUtils.setEL("#{bindings.DatabaseList.inputValue}", databases.substring(1));
        } else
            ADFUtils.setEL("#{bindings.DatabaseList.inputValue}",null);
        
        if(selRiskPurposes != null && selRiskPurposes.size() > 0){
            String riskPurposes = "";
            for(String riskPurpose : selRiskPurposes){
                riskPurposes = riskPurposes + "," + riskPurpose;
            }
            ADFUtils.setEL("#{bindings.RiskPurposeList.inputValue}", riskPurposes.substring(1));
        } else
            ADFUtils.setEL("#{bindings.RiskPurposeList.inputValue}",null);
        
        OperationBinding oper = ADFUtils.findOperation("Commit");
        oper.execute();
        if (oper.getErrors().size() > 0) 
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
         else
            ADFUtils.showPopup(successPopup);
    }

    public void setSuccessPopup(RichPopup successPopup) {
        this.successPopup = successPopup;
    }

    public RichPopup getSuccessPopup() {
        return successPopup;
    }

    public void onCloseRiskPopup(PopupCanceledEvent popupCanceledEvent) {
        Long crsId = (Long)ADFUtils.getPageFlowScopeValue("crsId");
        OperationBinding oper = ADFUtils.findOperation("initRiskRelation");
        oper.getParamsMap().put("crsId", crsId);
        oper.execute();
        if (oper.getErrors().size() > 0) 
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
        ADFUtils.addPartialTarget(riskDefTable);
    }

//    /**
//     * @param bslFacetName
//     */
//    public void setBslFacetName(String bslFacetName) {
//        this.bslFacetName = bslFacetName;
//    }
//
//    /**
//     * @return
//     */
//    public String getBslFacetName() {
//        return bslFacetName;
//    }

    /**
     * @param relStatusForAnonymous
     */
    public void setRelStatusForAnonymous(String relStatusForAnonymous) {
        this.relStatusForAnonymous = relStatusForAnonymous;
    }

    /**
     * @return
     */
    public String getRelStatusForAnonymous() {
        return relStatusForAnonymous;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return
     */
    public String getUserName() {
        return userName;
    }

    public void setSelDatabases(List<String> selDatabases) {
        this.selDatabases = selDatabases;
    }

    public List<String> getSelDatabases() {
        return selDatabases;
    }

    public void setDatabaseList(List<SelectItem> databaseList) {
        this.databaseList = databaseList;
    }

    public List<SelectItem> getDatabaseList() {
        if(databaseList == null){
            databaseList = new ArrayList<SelectItem>();
            DCBindingContainer bc = ADFUtils.getDCBindingContainer();
            OperationBinding ob = bc.getOperationBinding("fetchDatabases");
            List<String> databases = (List<String>)ob.execute();
            if(databases != null && databases.size() > 0){
                for(String database : databases){
                    SelectItem item = new SelectItem(database, database);
                    databaseList.add(item);
                }
            }
        }
        return databaseList;
    }

    public void setSelRiskPurposes(List<String> selRiskPurposes) {
        this.selRiskPurposes = selRiskPurposes;
    }

    public List<String> getSelRiskPurposes() {
        return selRiskPurposes;
    }

    public void setReviewSubmitPopup(RichPopup reviewSubmitPopup) {
        this.reviewSubmitPopup = reviewSubmitPopup;
    }

    public RichPopup getReviewSubmitPopup() {
        return reviewSubmitPopup;
    }

    public void setCrsStateSOC(RichSelectOneChoice crsStateSOC) {
        this.crsStateSOC = crsStateSOC;
    }

    public RichSelectOneChoice getCrsStateSOC() {
        return crsStateSOC;
    }

    public void setCrsStatusSOC(RichSelectOneChoice crsStatusSOC) {
        this.crsStatusSOC = crsStatusSOC;
    }

    public RichSelectOneChoice getCrsStatusSOC() {
        return crsStatusSOC;
    }
    
    public void onClickReview(ActionEvent actionEvent) {
        // Add event code here...
        ADFUtils.setEL("#{bindings.StateId.inputValue}", ModelConstants.STATE_REVIEW);
        OperationBinding oper = ADFUtils.findOperation("Commit");
        oper.execute();
        if (oper.getErrors().size() > 0) 
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
        else{
            ADFUtils.showPopup(getReviewSubmitPopup());
            ADFUtils.addPartialTarget(getCrsStateSOC());
        }
    }

    public void onClickApprove(ActionEvent actionEvent) {
        // Add event code here...

        if (ADFContext.getCurrent().getSecurityContext().isUserInRole(ModelConstants.ROLE_TASL)) 
            ADFUtils.setEL("#{bindings.StateId.inputValue}", ModelConstants.STATE_MLAPPROVE);  
        else if (ADFContext.getCurrent().getSecurityContext().isUserInRole(ModelConstants.ROLE_ML)) 
            ADFUtils.setEL("#{bindings.StateId.inputValue}", ModelConstants.STATE_APPROVED);        

        OperationBinding oper = ADFUtils.findOperation("Commit");
        oper.execute();
        if (oper.getErrors().size() > 0) 
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
        else{
            ADFUtils.showPopup(getCrsApprovePopup());
            ADFUtils.addPartialTarget(getCrsStateSOC());
            ADFUtils.addPartialTarget(getWorkflowPanelBox());
        }
    }


    public void setCrsApprovePopup(RichPopup crsApprovePopup) {
        this.crsApprovePopup = crsApprovePopup;
    }

    public RichPopup getCrsApprovePopup() {
        return crsApprovePopup;
    }

    public void setWorkflowPanelBox(RichPanelBox workflowPanelBox) {
        this.workflowPanelBox = workflowPanelBox;
    }

    public RichPanelBox getWorkflowPanelBox() {
        return workflowPanelBox;
    }

    public void onClickSubmitForTASL(ActionEvent actionEvent) {
        // Add event code here...
        if (ADFContext.getCurrent().getSecurityContext().isUserInRole(ModelConstants.ROLE_BSL)) {
            
            ADFUtils.setEL("#{bindings.StateId.inputValue}", ModelConstants.STATE_TASLAPPROVE);
            
            OperationBinding oper = ADFUtils.findOperation("Commit");
            oper.execute();
            if (oper.getErrors().size() > 0)
                ADFUtils.showFacesMessage("An internal error has occured. Please try later.",
                                          FacesMessage.SEVERITY_ERROR);
            else {
                ADFUtils.showPopup(getCrsApprovePopup());
                ADFUtils.addPartialTarget(getCrsStateSOC());
                ADFUtils.addPartialTarget(getWorkflowPanelBox());
            }
        }
    }

    public void onClickReject(ActionEvent actionEvent) {
        // Add event code here...
        if (ADFContext.getCurrent().getSecurityContext().isUserInRole(ModelConstants.ROLE_TASL)) {
            String taslComments = (String)ADFUtils.evaluateEL("#{bindings.TaslRejectComment.inputValue}");
            if (taslComments==null || (taslComments != null && "".equals(taslComments.trim()))){
                ADFUtils.showFacesMessage("Please enter your comments for rejection and click on Reject.",
                                          FacesMessage.SEVERITY_ERROR, getTaslCommentsInputText());                
                return;
            }
        }

        if (ADFContext.getCurrent().getSecurityContext().isUserInRole(ModelConstants.ROLE_ML)) {
            String mlComments = (String)ADFUtils.evaluateEL("#{bindings.MedicalLeadRejectComment.inputValue}");
            if (mlComments==null || (mlComments != null && "".equals(mlComments.trim()))){
                ADFUtils.showFacesMessage("Please enter your comments for rejection and click on Reject.",
                                          FacesMessage.SEVERITY_ERROR, getMlCommentsInputText());
            return;
            }
        }

        ADFUtils.setEL("#{bindings.StateId.inputValue}", ModelConstants.STATE_DRAFT);

        OperationBinding oper = ADFUtils.findOperation("Commit");
        oper.execute();
        if (oper.getErrors().size() > 0)
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
        else {
            ADFUtils.showPopup(getCrsApprovePopup());
            ADFUtils.addPartialTarget(getCrsStateSOC());
            ADFUtils.addPartialTarget(getWorkflowPanelBox());
        }

    }
    
    public void onClickDemoteToDraft(ActionEvent actionEvent) {
        // Add event code here...
        ADFUtils.setEL("#{bindings.StateId.inputValue}", ModelConstants.STATE_DRAFT);

        OperationBinding oper = ADFUtils.findOperation("Commit");
        oper.execute();
        if (oper.getErrors().size() > 0)
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
        else {
            ADFUtils.showPopup(getCrsApprovePopup());
            ADFUtils.addPartialTarget(getCrsStateSOC());
            ADFUtils.addPartialTarget(getWorkflowPanelBox());
        }
    }
    
    public void onClickReviewed(ActionEvent actionEvent) {
        // Add event code here...
        ADFUtils.setEL("#{bindings.StateId.inputValue}", ModelConstants.STATE_REVIEWED);

        OperationBinding oper = ADFUtils.findOperation("Commit");
        oper.execute();
        if (oper.getErrors().size() > 0)
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
        else {
            ADFUtils.showPopup(getCrsReviewedPopup());
            ADFUtils.addPartialTarget(getCrsStateSOC());
            ADFUtils.addPartialTarget(getWorkflowPanelBox());
        }
    }
    
    public void onClickRetire(ActionEvent actionEvent) {
        // Add event code here...
        ADFUtils.setEL("#{bindings.StateId.inputValue}", ModelConstants.STATE_RETIRED);

        OperationBinding oper = ADFUtils.findOperation("Commit");
        oper.execute();
        if (oper.getErrors().size() > 0)
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
        else {
            ADFUtils.showPopup(getCrsRetirePopup());
            ADFUtils.addPartialTarget(getCrsStateSOC());
            ADFUtils.addPartialTarget(getWorkflowPanelBox());
        }
        
    }

    public void onClickReactivate(ActionEvent actionEvent) {
        // Add event code here....
        ADFUtils.setEL("#{bindings.StateId.inputValue}", ModelConstants.STATE_ACTIVATED);

        OperationBinding oper = ADFUtils.findOperation("Commit");
        oper.execute();
        if (oper.getErrors().size() > 0)
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
        else {
            ADFUtils.showPopup(getCrsReactivatePopup());
            ADFUtils.addPartialTarget(getCrsStateSOC());
            ADFUtils.addPartialTarget(getWorkflowPanelBox());
        }
    }

    public void setCrsRejectPopup(RichPopup crsRejectPopup) {
        this.crsRejectPopup = crsRejectPopup;
    }

    public RichPopup getCrsRejectPopup() {
        return crsRejectPopup;
    }

    public void setTaslCommentsInputText(RichInputText taslCommentsInputText) {
        this.taslCommentsInputText = taslCommentsInputText;
    }

    public RichInputText getTaslCommentsInputText() {
        return taslCommentsInputText;
    }

    public void setMlCommentsInputText(RichInputText mlCommentsInputText) {
        this.mlCommentsInputText = mlCommentsInputText;
    }

    public RichInputText getMlCommentsInputText() {
        return mlCommentsInputText;
    }

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public String getDictionary() {
        return dictionary;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    public void searchHierarchy(ActionEvent actionEvent) {
        DCIteratorBinding iter = ADFUtils.findIterator("HierarchySearchVOIterator");
        ViewObject hierVO = iter.getViewObject();
        hierVO.setNamedWhereClauseParam("pTerm", term != null ? term : null);
        hierVO.setNamedWhereClauseParam("pLevel", level != null ? level : null);
        hierVO.setNamedWhereClauseParam("pDict", dictionary != null ? dictionary : null);
        hierVO.executeQuery();
    }

    public void onClickHierarchySearch(ActionEvent actionEvent) {
        DCIteratorBinding iter = ADFUtils.findIterator("HierarchySearchVOIterator");
        ViewObject hierVO = iter.getViewObject();
        hierVO.executeEmptyRowSet();
        setTerm(null);
        setLevel(null);
        setDictionary(null);
        ADFUtils.showPopup(hierPopup);
    }

    public void setHierPopup(RichPopup hierPopup) {
        this.hierPopup = hierPopup;
    }

    public RichPopup getHierPopup() {
        return hierPopup;
    }

    public DnDAction dragDropListener(DropEvent dropEvent) {
        
        DCIteratorBinding riskDefIter = ADFUtils.findIterator("CrsRiskDefinitionsVOIterator");
        ViewObject riskDefVO = riskDefIter.getViewObject();
        
        RichTable dragTable = (RichTable)dropEvent.getDragComponent();
        RichTable dropTable = (RichTable)dropEvent.getDropComponent();
        String dragNodeVO = null;
        Transferable t = dropEvent.getTransferable();
        DataFlavor<RowKeySet> df = DataFlavor.getDataFlavor(RowKeySet.class, "copyRows");
        RowKeySet rks = t.getData(df);
        Iterator iter = rks.iterator();
        
        Object dragCurrentRowKey = dragTable.getRowKey();
        Row dragRow = null;
        while (iter.hasNext()) {
            List key = (List)iter.next();
            dragTable.setRowKey(key);
            JUCtrlHierNodeBinding rowBinding = (JUCtrlHierNodeBinding)dragTable.getRowData();
            dragRow = rowBinding.getRow();
            dragNodeVO = dragRow.getStructureDef().getDefName();
            if (!("HierarchySearchVO".equalsIgnoreCase(dragNodeVO))) {
                FacesMessage msg =
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Drag and Drop a row from the Hierachy Search Results.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return DnDAction.NONE;
            }
            else{
                String term = (String)dragRow.getAttribute("Mqterm");
                String code = (String)dragRow.getAttribute("Mqcode");
                String level = (String)dragRow.getAttribute("Mqlevel");
                String qual = (String)dragRow.getAttribute("Mqcrtev");
                Row riskDefRow = riskDefVO.createRow();
                riskDefRow.setAttribute("MeddraCode", code);
                riskDefRow.setAttribute("MeddraLevel", level);
                riskDefRow.setAttribute("MeddraTerm", term);
                if(qual != null && qual.contains("Y_BROAD"))
                    riskDefRow.setAttribute("MeddraQualifier", "BROAD");
                else if(qual != null && qual.contains("Y_NARROW"))
                    riskDefRow.setAttribute("MeddraQualifier", "NARROW");
                else
                    riskDefRow.setAttribute("MeddraQualifier", "CHILD NARROW");
//                meddra_qualifier IN ('BROAD','NARROW','CHILD NARROW')
                riskDefVO.insertRow(riskDefRow);
            }
        }
//        Object currentRowKey = dropTable.getRowKey();
//        List dropRowKey = (List)dropEvent.getDropSite();
//        if (dropRowKey == null) {
//            return DnDAction.NONE;
//        }
//        dropTable.setRowKey(dropRowKey);
//        JUCtrlHierNodeBinding dropNode = (JUCtrlHierNodeBinding)dropTable.getRowData();
//        Row dropRow = dropNode.getRow();
//        String dropNodeVO = dropRow.getStructureDef().getDefName();
//        if ("CrsRiskDefinitionsVO".equalsIgnoreCase(dropNodeVO)) {
//            Long deptId = (Long)dropRow.getAttribute("DepartmentId");
//            Long oldDeptId = (Long)dragRow.getAttribute("DepartmentId");
//            if (oldDeptId != deptId) {
//                dragRow.setAttribute("DepartmentId", deptId);
//                executeOperation("Commit");
//            } else {
//                FacesMessage msg =
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Drop an employee on to a different department, he is already from this department.");
//                FacesContext.getCurrentInstance().addMessage(null, msg);
//                return DnDAction.NONE;
//            }
//        } else {
//            FacesMessage msg =
//                new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Drop an employee on to a department to move from department to department.");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            return DnDAction.NONE;
//        }
//        dropTable.setRowKey(currentRowKey);
        dragTable.setRowKey(dragCurrentRowKey);
        AdfFacesContext.getCurrentInstance().addPartialTarget(dragTable);
        AdfFacesContext.getCurrentInstance().addPartialTarget(dropTable);
        return DnDAction.COPY;
    }


    public void setCrsFieldsUpdatable(boolean crsFieldsUpdatable) {
        this.crsFieldsUpdatable = crsFieldsUpdatable;
    }

    public boolean isCrsFieldsUpdatable() {
        
        boolean isCrsFieldsUpdatable = false;
        Integer crsState = (Integer)ADFUtils.evaluateEL("#{bindings.StateId.inputValue}");
        String crsStatus = (String)ADFUtils.evaluateEL("#{bindings.ReleaseStatusFlag.inputValue}");
        
        // SEARCH FLOW - always read only
        if(ViewConstants.FLOW_TYPE_SEARCH.equals(getFlowType())){
            isCrsFieldsUpdatable = false;
            return isCrsFieldsUpdatable;
        }
            
        //  BSL LOGIN
        if (ADFContext.getCurrent().getSecurityContext().isUserInRole(ModelConstants.ROLE_BSL)) {

            if (ModelConstants.STATUS_PENDING.equals(crsStatus)) {


            } else if (ModelConstants.STATUS_CURRENT.equals(crsStatus)) {
                
                if(ModelConstants.STATE_DRAFT.equals(crsState) ||
                    ModelConstants.STATE_REVIEWED.equals(crsState) ||
                    ModelConstants.STATE_APPROVED.equals(crsState) ||
                    ModelConstants.STATE_PUBLISHED.equals(crsState) )
                    isCrsFieldsUpdatable = true;                                
            }
        }
        
        // ADMIN LOGIN - admin can update any CRS in any state
       else if (ADFContext.getCurrent().getSecurityContext().isUserInRole(ModelConstants.ROLE_CRSADMIN))
            isCrsFieldsUpdatable = true; 
        
        
        return crsFieldsUpdatable;
    }

    public void processDeleteDialog(DialogEvent dialogEvent) {
        // Add event code here...
        if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome())){
            
            // remove risk relations and risk defs
            
            //remove crs content
            ADFUtils.findIterator("CrsContentVOIterator").getCurrentRow().remove();
            
            //commit
            
            //navigate back to home page
            
            
        }
    }


    public void setCrsRetirePopup(RichPopup crsRetirePopup) {
        this.crsRetirePopup = crsRetirePopup;
    }

    public RichPopup getCrsRetirePopup() {
        return crsRetirePopup;
    }

    public void setCrsReactivatePopup(RichPopup crsReactivatePopup) {
        this.crsReactivatePopup = crsReactivatePopup;
    }

    public RichPopup getCrsReactivatePopup() {
        return crsReactivatePopup;
    }

    public void setCrsReviewedPopup(RichPopup crsReviewedPopup) {
        this.crsReviewedPopup = crsReviewedPopup;
    }

    public RichPopup getCrsReviewedPopup() {
        return crsReviewedPopup;
    }
}
