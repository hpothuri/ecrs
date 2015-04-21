package com.novartis.ecrs.ui.bean;


import com.novartis.ecrs.model.constants.ModelConstants;
import com.novartis.ecrs.model.view.CrsContentVORowImpl;
import com.novartis.ecrs.ui.constants.ViewConstants;
import com.novartis.ecrs.ui.utility.ADFUtils;
import com.novartis.ecrs.ui.utility.ExcelExportUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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

import oracle.javatools.resourcebundle.BundleFactory;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import oracle.security.crypto.util.InvalidFormatException;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


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
    private transient RichPopup meddraError;
    private transient RichPopup delConfPopupBinding;
    private transient RichPopup crsPublishPopupBinding;
    private transient RichPopup crsDemoteDraftPopupBinding;
    private List<SelectItem> filterItems;
    private List<SelectItem> meddraItems;
    private List<SelectItem> levelItems;

    public ManageCRSBean() {
        super();
        getUserRole();
        getCrsFlowType();
    }
    
    private String flowType;
    private boolean inboxDisable;
    private String loggedInUserRole;
 //   private String bslFacetName;
    //private String relStatusForAnonymous ;
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
        else {
            String flowType = (String)ADFUtils.evaluateEL("#{pageFlowScope.flowType}");
            if (flowType != null && "C".equalsIgnoreCase(flowType)) {
                Long crsId = (Long)ADFUtils.evaluateEL("#{bindings.CrsId.inputValue}");
                OperationBinding copyOper = ADFUtils.findOperation("copyRoutineDefinition");
                copyOper.getParamsMap().put("crsId", crsId);
                copyOper.execute();
                if (copyOper.getErrors().size() > 0)
                    ADFUtils.showFacesMessage("An internal error has occured. Please try later.",
                                              FacesMessage.SEVERITY_ERROR);
                else {
                    ADFUtils.showPopup(getSuccessPopupBinding());
                    ADFUtils.addPartialTarget(getWorkflowPanelBox());
                }
            } else {
                ADFUtils.showPopup(getSuccessPopupBinding());
                ADFUtils.addPartialTarget(getWorkflowPanelBox());
            }
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
            row.setBslName(ADFContext.getCurrent().getSecurityContext().getUserName());
            row.setStateId(ModelConstants.STATE_DRAFT);
            row.setReviewApproveRequiredFlag(ModelConstants.REVIEW_REQD_YES);
            row.setReleaseStatusFlag(ModelConstants.STATUS_PENDING);
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
                           ModelConstants.STATUS_CURRENT);
        }
        DCBindingContainer bc = ADFUtils.getDCBindingContainer();
        OperationBinding ob = bc.getOperationBinding("filterCRSContent");
        ob.getParamsMap().put("userInRole", loggedInUserRole);
        ob.getParamsMap().put("userName", getUserName());
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
        String returnValue = (String)ADFUtils.invokeEL("#{controllerContext.currentViewPort.taskFlowContext.trainModel.getNext}");
        return returnValue;
    }
    
    public void initRisRel(){
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
    }

    public void onSelectInbox(ValueChangeEvent vce) {
        // Add event code here...
        if (vce!=null) {
            if (vce.getNewValue() != null &&
                !vce.getNewValue().equals(vce.getOldValue()) &&
                (Boolean)vce.getNewValue()) {               
                    setInboxDisable(Boolean.TRUE);                
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

//    /**
//     * @param relStatusForAnonymous
//     */
//    public void setRelStatusForAnonymous(String relStatusForAnonymous) {
//        this.relStatusForAnonymous = relStatusForAnonymous;
//    }
//
//    /**
//     * @return
//     */
//    public String getRelStatusForAnonymous() {
//        return relStatusForAnonymous;
//    }

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
    
    /** DRAFT to REVIEW */
    public void onClickReview(ActionEvent actionEvent) {
        // Add event code here...
        processStateChange(ModelConstants.STATE_REVIEW, getReviewSubmitPopup());       
    }
    
    /** REVIEW to REVIEWED */
    public void onClickReviewed(ActionEvent actionEvent) {
        // Add event code here...
        processStateChange(ModelConstants.STATE_REVIEWED, getCrsReviewedPopup());          
    }
    
    /** REVIEWED to TASL APPROVE */
    public void onClickSubmitForTASL(ActionEvent actionEvent) {
        // Add event code here...
        processStateChange(ModelConstants.STATE_TASLAPPROVE, getReviewSubmitPopup());
    }
    
    /** TASL APPROVE to ML APPROVE */   
    public void onClickTASLApprove(ActionEvent actionEvent) {
        // Add event code here...
        processStateChange(ModelConstants.STATE_MLAPPROVE, getCrsApprovePopup());
    }
    
    /** TASL APPROVE to DRAFT */  
    public void onClickTASLReject(ActionEvent actionEvent) {
        // Add event code here...
        String taslComments = (String)ADFUtils.evaluateEL("#{bindings.TaslRejectComment.inputValue}");
        if (taslComments==null || (taslComments != null && "".equals(taslComments.trim()))){
            ADFUtils.showFacesMessage("Please enter your comments for rejection and click on Reject.",
                                      FacesMessage.SEVERITY_ERROR, getTaslCommentsInputText());                
            return;
        }
        
        // change to DRAFT state
        processStateChange(ModelConstants.STATE_DRAFT, getCrsRejectPopup());
    }
    /** ML APPROVE to APPROVED */ 
    public void onClickMLApprove(ActionEvent actionEvent) {
        // Add event code here...
        processStateChange(ModelConstants.STATE_APPROVED, getCrsApprovePopup());
    }
    
    /** ML APPROVE to DRAFT */ 
    public void onClickMLReject(ActionEvent actionEvent) {
        // Add event code here...
        String mlComments = (String)ADFUtils.evaluateEL("#{bindings.MedicalLeadRejectComment.inputValue}");
        if (mlComments==null || (mlComments != null && "".equals(mlComments.trim()))){
            ADFUtils.showFacesMessage("Please enter your comments for rejection and click on Reject.",
                                      FacesMessage.SEVERITY_ERROR, getMlCommentsInputText());
        return;
        }
        
        // change to DRAFT state
        processStateChange(ModelConstants.STATE_DRAFT, getCrsRejectPopup());
    }
    
     /** BSL DEMOTE - any state to DRAFT */ 
    public void onClickDemoteToDraft(ActionEvent actionEvent) {
        // Add event code here...
        processStateChange(ModelConstants.STATE_DRAFT, getCrsDemoteDraftPopupBinding());        
    }
    
    /** APPROVED to PUBLISHED */ 
    public void onClickPublished(ActionEvent actionEvent) {
        processStateChange(ModelConstants.STATE_PUBLISHED, getCrsPublishPopupBinding());       
    }    
    
    /** ACTIVATED to RETIRED */
    public void onClickRetire(ActionEvent actionEvent) {
        // Add event code here...
        processStateChange(ModelConstants.STATE_RETIRED, getCrsRetirePopup());  
    }

    public void onClickReactivate(ActionEvent actionEvent) {
        // Add event code here....
        processStateChange(ModelConstants.STATE_ACTIVATED, getCrsReactivatePopup());        
    }
    
    private void processStateChange(Integer newState, RichPopup infoPopup) {

        ADFUtils.setEL("#{bindings.StateId.inputValue}", newState);

        OperationBinding oper = ADFUtils.findOperation("Commit");
        oper.execute();
        if (oper.getErrors().size() > 0)
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
        else {
            ADFUtils.showPopup(infoPopup);
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
                String dict = (String)dragRow.getAttribute("DictNm");
                String version = (String)dragRow.getAttribute("Version");
                
                if (dict != null && "NMATMED".equalsIgnoreCase(dict)) {
                    Row rows[] = riskDefVO.getFilteredRows("MeddraDict", "NMATMED");
                    if (rows.length > 0) {
                        ADFUtils.showPopup(meddraError);
                        dragTable.setRowKey(dragCurrentRowKey);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(dragTable);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(dropTable);
                        return DnDAction.NONE;
                    }
                }
                
                Row riskDefRow = riskDefVO.createRow();
                riskDefRow.setAttribute("MeddraCode", code);
                riskDefRow.setAttribute("MeddraLevel", level);
                riskDefRow.setAttribute("MeddraTerm", term);
                riskDefRow.setAttribute("MeddraDict", dict);
                riskDefRow.setAttribute("MeddraVersion", version);
                riskDefRow.setAttribute("MeddraVersionDate", dragRow.getAttribute("Dates"));
                
                if (dict != null && "NMATSMQ".equalsIgnoreCase(dict)) {
                    if (term != null && term.contains("NMQ"))
                        riskDefRow.setAttribute("MeddraExtension", "NMQ");
                    else if (term != null && term.contains("CMQ"))
                        riskDefRow.setAttribute("MeddraExtension", "CMQ");
                    else if (term != null && term.contains("SMQ"))
                        riskDefRow.setAttribute("MeddraExtension", "SMQ");
                } else
                    riskDefRow.setAttribute("MeddraExtension", level);
                
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

            if (ModelConstants.STATUS_CURRENT.equals(crsStatus)) {


            } else if (ModelConstants.STATUS_PENDING.equals(crsStatus)) {
                
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
        
        return isCrsFieldsUpdatable;
    }

    public void processDeleteDialog(DialogEvent dialogEvent) {
        if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome())){
            OperationBinding oper = ADFUtils.findOperation("deleteCrs");
            oper.execute();
            if (oper.getErrors().size() > 0)
                ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
            else
                ADFUtils.navigateToControlFlowCase("home");            
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

    public void setMeddraError(RichPopup meddraError) {
        this.meddraError = meddraError;
    }

    public RichPopup getMeddraError() {
        return meddraError;
    }

    /**
     * @param vce
     */
    public void onChangeIndication(ValueChangeEvent vce) {
        if (vce != null) {
            vce.getComponent().processUpdates(FacesContext.getCurrentInstance());
            if (vce.getNewValue() != null &&
                !vce.getNewValue().equals(vce.getOldValue())) {
                String crsCompCode =
                    (String)ADFUtils.evaluateEL("#{bindings.CrsCompoundCode.inputValue}");
                String compCode =
                    (String)ADFUtils.evaluateEL("#{bindings.CompoundCode.inputValue}");
                String indication =
                    (String)ADFUtils.evaluateEL("#{bindings.Indication.inputValue}");
                if (indication != null) {
                    ADFUtils.setEL("#{bindings.CrsName.inputValue}",
                                   (compCode != null ? compCode :
                                    crsCompCode) + " " + indication);
                }
            }
        }
    }

    /**
     * @param facesContext
     * @param outputStream
     * @throws IOException
     */
    public void exportRiskDefinitions(FacesContext facesContext,
                                      OutputStream outputStream) throws IOException {
        // Add event code here...
        //  _logger.info("Start of CRSReportsBean:onAdminReportItmes()");
        Workbook workbook = null;
        InputStream excelInputStream = ExcelExportUtils.getExcelInpStream();
        try {
            //create sheet
            DCIteratorBinding iter =
                ADFUtils.findIterator("CrsRiskVOIterator");
            RowSetIterator rowSet = null;
            int rowStartIndex = 8;
            int cellStartIndex = 0;
            String emptyValReplace = null;
            String dateCellFormat = "M/dd/yyyy";
            if (iter != null) {
                iter.setRangeSize(-1);
                rowSet = iter.getRowSetIterator();
            }
            workbook = WorkbookFactory.create(excelInputStream);
            LinkedHashMap columnMap = new LinkedHashMap();
            ResourceBundle rsBundle =
                BundleFactory.getBundle("com.novartis.ecrs.model.ECRSModelBundle");
            //Here Key will be ViewObject Attribute
            columnMap.put("SafetyTopicOfInterest",
                          rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskVO.SafetyTopicOfInterest_LABEL"));
            columnMap.put("RiskPurposeSpFlag",
                          rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskVO.RiskPurposeSpFlag_LABEL"));
            columnMap.put("RiskPurposeDsFlag",
                          rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskVO.RiskPurposeDsFlag_LABEL"));
            columnMap.put("RiskPurposeRmFlag",
                          rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskVO.RiskPurposeRmFlag_LABEL"));
            columnMap.put("RiskPurposePsFlag",
                          rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskVO.RiskPurposePsFlag_LABEL"));
            columnMap.put("RiskPurposeIbFlag",
                          rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskVO.RiskPurposeIbFlag_LABEL"));
            columnMap.put("RiskPurposeCdFlag",
                          rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskVO.RiskPurposeCdFlag_LABEL"));
            columnMap.put("RiskPurposeOsFlag",
                          rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskVO.RiskPurposeOsFlag_LABEL"));
            columnMap.put("RiskPurposeMiFlag",
                          rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskVO.RiskPurposeMiFlag_LABEL"));
            columnMap.put("RiskPurposeErFlag",
                          rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskVO.RiskPurposeErFlag_LABEL"));
            columnMap.put("SocTerm",
                          rsBundle.getString("SOC_AS_ASSIGNED_TO_THE_ADR"));
            columnMap.put("DatabaseList",
                          rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskVO.DatabaseId_LABEL"));
            columnMap.put("DataDomain",
                          rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskVO.DataDomain_LABEL"));
            columnMap.put("MeddraTerm", rsBundle.getString("MEDDRA_TERM"));
            columnMap.put("MeddraLevel", rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskDefinitionsVO.MeddraLevel_LABEL"));
            columnMap.put("MeddraQualifier",
                          rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskDefinitionsVO.MeddraQualifier_LABEL"));
            columnMap.put("SearchCriteriaDetails",
                          rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskDefinitionsVO.SearchCriteriaDetails_LABEL"));
            columnMap.put("MqmComment", rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskVO.MqmComment_LABEL"));

            workbook.setMissingCellPolicy(org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK);
            Sheet sheet = workbook.getSheetAt(0);
            ExcelExportUtils.writeExcelSheet(sheet, rowSet, rowStartIndex,
                                             cellStartIndex, columnMap, null,
                                             dateCellFormat, emptyValReplace);
        } catch (InvalidFormatException invalidFormatException) {
            invalidFormatException.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workbook.write(outputStream);
            excelInputStream.close();
            outputStream.close();
        }
    }

    
    public void setDelConfPopupBinding(RichPopup delConfPopupBinding) {
        this.delConfPopupBinding = delConfPopupBinding;
    }

    public RichPopup getDelConfPopupBinding() {
        return delConfPopupBinding;
    }

    public void setCrsPublishPopupBinding(RichPopup publishPopupBinding) {
        this.crsPublishPopupBinding = publishPopupBinding;
    }

    public RichPopup getCrsPublishPopupBinding() {
        return crsPublishPopupBinding;
    }

    public void setCrsDemoteDraftPopupBinding(RichPopup crsDemoteDraftPopupBinding) {
        this.crsDemoteDraftPopupBinding = crsDemoteDraftPopupBinding;
    }

    public RichPopup getCrsDemoteDraftPopupBinding() {
        return crsDemoteDraftPopupBinding;
    }

    public void setFilterItems(List<SelectItem> filterItems) {
        this.filterItems = filterItems;
    }

    public List<SelectItem> getFilterItems() {
        if(filterItems == null){
            filterItems = new ArrayList<SelectItem>();
            SelectItem item1 = new SelectItem("MQ1", "SMQ 1");
            SelectItem item2 = new SelectItem("MQ2", "SMQ 2");
            SelectItem item3 = new SelectItem("MQ3", "SMQ 3");
            SelectItem item4 = new SelectItem("MQ4", "SMQ 4");
            SelectItem item5 = new SelectItem("MQ5", "SMQ 5");
            SelectItem item6 = new SelectItem("NMQ1", "Custom 1");
            SelectItem item7 = new SelectItem("NMQ2", "Custom 2");
            SelectItem item8 = new SelectItem("NMQ3", "Custom 3");
            SelectItem item9 = new SelectItem("NMQ4", "Custom 4");
            SelectItem item10 = new SelectItem("NMQ5", "Custom 5");
            filterItems.add(item1);
            filterItems.add(item2);
            filterItems.add(item3);
            filterItems.add(item4);
            filterItems.add(item5);
            filterItems.add(item6);
            filterItems.add(item7);
            filterItems.add(item8);
            filterItems.add(item9);
            filterItems.add(item10);
        }
        return filterItems;
    }

    public void setMeddraItems(List<SelectItem> meddraItems) {
        this.meddraItems = meddraItems;
    }

    public List<SelectItem> getMeddraItems() {
        if(meddraItems == null){
            meddraItems = new ArrayList<SelectItem>();
            SelectItem item1 = new SelectItem("SOC", "SOC");
            SelectItem item2 = new SelectItem("HLGT", "HLGT");
            SelectItem item3 = new SelectItem("HLT", "HLT");
            SelectItem item4 = new SelectItem("PT", "PT");
            meddraItems.add(item1);
            meddraItems.add(item2);
            meddraItems.add(item3);
            meddraItems.add(item4);
        }
        return meddraItems;
    }

    public void dictionaryVC(ValueChangeEvent valueChangeEvent) {
        if(valueChangeEvent.getNewValue() != null && valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()){
            if("NMATMED".equalsIgnoreCase((String)valueChangeEvent.getNewValue())){
                setLevelItems(getMeddraItems());
            }else{
                setLevelItems(getFilterItems());
            }
        }
    }

    public void setLevelItems(List<SelectItem> levelItems) {
        this.levelItems = levelItems;
    }

    public List<SelectItem> getLevelItems() {
        return levelItems;
    }
}
