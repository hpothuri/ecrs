package com.novartis.ecrs.ui.bean;


import com.novartis.ecrs.model.constants.ModelConstants;
import com.novartis.ecrs.model.lov.UserRoleVORowImpl;
import com.novartis.ecrs.model.view.CrsContentVORowImpl;
import com.novartis.ecrs.model.view.HierarchyChildVORowImpl;
import com.novartis.ecrs.model.view.base.CrsContentBaseVORowImpl;
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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichDialog;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectManyChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichPanelBox;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;
import oracle.adf.view.rich.component.rich.layout.RichPanelLabelAndMessage;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.datatransfer.DataFlavor;
import oracle.adf.view.rich.datatransfer.Transferable;
import oracle.adf.view.rich.dnd.DnDAction;
import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.DropEvent;
import oracle.adf.view.rich.event.PopupCanceledEvent;
import oracle.adf.view.rich.util.ResetUtils;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.javatools.resourcebundle.BundleFactory;

import oracle.jbo.Row;
import oracle.jbo.RowIterator;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import oracle.security.crypto.util.InvalidFormatException;

import org.apache.myfaces.trinidad.component.UIXCollection;
import org.apache.myfaces.trinidad.component.UIXSwitcher;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.ChildPropertyTreeModel;
import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;


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
    private String contentId;
    private String childScope;
    private transient RichTreeTable childTreeTable;
    private transient RichPopup parentError;
    private String safetyTopicOfInterest;
    private transient RichPopup copyPopup;
    private transient RichPanelGroupLayout copyPanel;
    private transient RichPopup pendingPopup;
    private transient RichTable copyRiskDefTable;
    private transient RichPanelLabelAndMessage savedSuccessMessage;
    private transient RichPanelLabelAndMessage copySuccessMessage;
    private transient RichOutputText hiddenPopupAlign;
    private transient RichInputText stoiBinding;
    private transient RichSelectManyChoice copyDBListBinding;
    private transient RichSelectManyChoice copyRPListBinding;
    private transient UIXSwitcher searchSwitherBinding;
    private Boolean repoRefreshed;
    private String baseOrStaging=ModelConstants.BASE_FACET;
    private ChildPropertyTreeModel hierChildTreeModel;
    private List<HierarchyChildUIBean> hierChildList;
    private transient RichTable searchBaseTableBinding;
    private transient RichDialog reasonChangePopup;
    private transient RichPopup modifyReasonChngPopup;
    private transient RichInputText retireReactvteReasonPopup;
    private transient RichPopup reactivatePopupBinding;
    private transient RichPopup retirePopupBinding;
    private String reasonForChange;
    private transient RichPopup errorPLSqlPopup;
    private String filterBy1;
    private String filterBy2;
    private String filterBy3;
    private String filterValue1;
    private String filterValue2;
    private String filterValue3;
    private String filterCri1="OR";
    private String filterCri2="OR";
    private transient RichPopup advancedFilterPopup;
    private transient RichTable searchStagingTableBinding;
    private String currReleaseStatus = ModelConstants.STATUS_PENDING;
    private transient RichPopup publishPopupBinding;
    private transient RichPopup submitApprovalPopup;
    private Map<Integer,String> statesMap = null;
    private boolean nonCompoundSelected;
    private transient UIXSwitcher stateSwitcherBinding;

    public ManageCRSBean() {
        super();
        getUserRole();
        getCrsFlowType();
        // save flow type to session - CompoundVO uses flowType as bind param
        ADFUtils.setSessionScopeValue("flowType", flowType);
        if (ViewConstants.FLOW_TYPE_CREATE.equals(flowType) ||
            ViewConstants.FLOW_TYPE_UPDATE.equals(flowType)) {
            setBaseOrStaging(ModelConstants.STAGING_FACET);
        } else
            setBaseOrStaging(ModelConstants.BASE_FACET);
    }
    
    private String flowType;
    private boolean inboxDisable;
    private String loggedInUserRole;
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
//        ADFUtils.setEL("#{bindings.CrsName.inputValue}", "ROUTINE");
        OperationBinding oper = ADFUtils.findOperation("Commit");
        oper.execute();
        if (oper.getErrors().size() > 0)
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
        else {
//            String flowType = (String)ADFUtils.evaluateEL("#{pageFlowScope.flowType}");
//            if (flowType != null && "C".equalsIgnoreCase(flowType)) {
//                Long crsId = (Long)ADFUtils.evaluateEL("#{bindings.CrsId.inputValue}");
//                OperationBinding copyOper = ADFUtils.findOperation("copyRoutineDefinition");
//                copyOper.getParamsMap().put("crsId", crsId);
//                copyOper.execute();
//                if (copyOper.getErrors().size() > 0)
//                    ADFUtils.showFacesMessage("An internal error has occured. Please try later.",
//                                              FacesMessage.SEVERITY_ERROR);
//                else {
//                    ADFUtils.showPopup(getSuccessPopupBinding());
//                    ADFUtils.addPartialTarget(getWorkflowPanelBox());
//                }
//            } else {
                ADFUtils.showPopup(getSuccessPopupBinding());
                ADFUtils.addPartialTarget(getWorkflowPanelBox());
//            }
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
            row.setBslName(ADFContext.getCurrent().getSecurityContext().getUserName().toUpperCase());
            row.setStateId(ModelConstants.STATE_DRAFT);
            row.setReviewApproveRequiredFlag(ModelConstants.REVIEW_REQD_YES);
            row.setReleaseStatusFlag(ModelConstants.STATUS_PENDING);
            row.setCrsEffectiveDt(ADFUtils.getJBOTimeStamp());
            
            Long crsId = row.getCrsId();
            OperationBinding copyOper = bc.getOperationBinding("copyRoutineDefinition");
            copyOper.getParamsMap().put("crsId", crsId);
            copyOper.execute();
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
            List<UserRoleVORowImpl> designees = (List<UserRoleVORowImpl>)ob.execute();
            if(designees != null && designees.size() > 0){
                for(UserRoleVORowImpl designee : designees){
                    SelectItem item = new SelectItem(designee.getUserName(), designee.getFullName());
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
        String releaseStatus = "";
        if ("anonymous".equalsIgnoreCase(userName))
            releaseStatus = ModelConstants.STATUS_CURRENT;
        else if (ViewConstants.FLOW_TYPE_UPDATE.equals(getFlowType()) &&
                 (ModelConstants.ROLE_ML.equals(loggedInUserRole) ||
                  ModelConstants.ROLE_MQM.equals(loggedInUserRole) ||
                  ModelConstants.ROLE_TASL.equals(loggedInUserRole)))
            releaseStatus = ModelConstants.STATUS_PENDING;
        else
            releaseStatus = getCurrReleaseStatus();
 
        //set releaseStatus variable to current row attribute
        ADFUtils.setEL("#{bindings.ReleaseStatus.inputValue}",
                       releaseStatus);
        
        //If mode is B&S ,and release status is 'CURRENT' set state to activated 
        if(ViewConstants.FLOW_TYPE_SEARCH.equals(flowType) &&
        ModelConstants.STATUS_CURRENT.equals(releaseStatus)){
            ADFUtils.setEL("#{bindings.State.inputValue}", ModelConstants.STATE_ACTIVATED);
        }

        //if mode is U and logged in is BSL, set Compound Type to COMPOUND
        if (ViewConstants.FLOW_TYPE_UPDATE.equals(flowType)
            && ModelConstants.ROLE_BSL.equals(loggedInUserRole)) {
            ADFUtils.setEL("#{bindings.CompoundType.inputValue}", ModelConstants.COMPOUND_TYPE_COMPOUND);
        }
        
        DCBindingContainer bc = ADFUtils.getDCBindingContainer();
        OperationBinding ob = bc.getOperationBinding("filterCRSContent");
        ob.getParamsMap().put("userInRole", loggedInUserRole);
        ob.getParamsMap().put("userName", getUserName());
        ob.getParamsMap().put("isInboxDisable", isInboxDisable());
        ob.getParamsMap().put("flowType", getFlowType());
        ob.execute();
        
        if (ModelConstants.STATUS_PENDING.equals(ADFUtils.evaluateEL("#{bindings.ReleaseStatus.inputValue}"))) {
            setBaseOrStaging(ModelConstants.STAGING_FACET);
        } else{
            setBaseOrStaging(ModelConstants.BASE_FACET);
            getSearchBaseTableBinding().resetStampState();
            ADFUtils.addPartialTarget(getSearchBaseTableBinding());
        }
        ADFUtils.addPartialTarget(getSearchSwitherBinding());
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
        DCIteratorBinding baseIter = bc.findIteratorBinding("CrsContentBaseVOIterator");
        if (baseIter.getViewObject() != null)
            baseIter.getViewObject().executeEmptyRowSet();
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
        if (ModelConstants.COMPOUND_TYPE_NON_COMPOUND.equals(selectedRow.getCrsCompoundType())) {
            setNonCompoundSelected(Boolean.TRUE);
        } else
            setNonCompoundSelected(Boolean.FALSE);
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
        if(crsId == null){
            crsName = (String)ADFUtils.evaluateEL("#{bindings.CrsNameBase.inputValue}");
            crsId = (Long)ADFUtils.evaluateEL("#{bindings.CrsIdBase.inputValue}");
        }
        ADFUtils.setPageFlowScopeValue("crsId", crsId);
        ADFUtils.setPageFlowScopeValue("crsName", crsName);
        Map params = new HashMap<String, Object>();
        params.put("crsId", crsId);
        params.put("status", getBaseOrStaging());
        try {
            ADFUtils.executeAction("initRiskRelation", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setRepoRefreshed(Boolean.FALSE);
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
            setNonCompoundSelected(Boolean.FALSE);
            if (vce.getNewValue() != null && !vce.getNewValue().equals(vce.getOldValue()) &&
                ModelConstants.COMPOUND_TYPE_NON_COMPOUND.equals(ADFUtils.evaluateEL("#{bindings.CompoundType.inputValue}"))) {
                ADFUtils.setEL("#{bindings.TradeName.inputValue}", null);
                ADFUtils.setEL("#{bindings.GenericName.inputValue}", null);
                ADFUtils.setEL("#{bindings.Indication.inputValue}", null);
                //TODO make this enable when isMarketedFlag null
                ADFUtils.setEL("#{bindings.IsMarketedFlag.inputValue}", "N");
                ADFUtils.setEL("#{bindings.BslName.inputValue}", null);
                ADFUtils.setEL("#{bindings.TaslName.inputValue}", null);
                ADFUtils.setEL("#{bindings.MedicalLeadName.inputValue}", null);
                setNonCompoundSelected(Boolean.TRUE);
            }
            String crsCompCode = (String)ADFUtils.evaluateEL("#{bindings.CrsCompoundCode.inputValue}");
            String compCode = (String)ADFUtils.evaluateEL("#{bindings.CompoundCode.inputValue}");
            String indication = (String)ADFUtils.evaluateEL("#{bindings.Indication.inputValue}");
            ADFUtils.setEL("#{bindings.CrsName.inputValue}",
                           (compCode != null ? compCode : crsCompCode) + (indication != null ? (" "+indication) : ""));
            //ResetUtils.reset(vce.getComponent().getParent());
            ADFUtils.addPartialTarget(vce.getComponent().getParent());
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
            setUserName(ADFUtils.evaluateEL("#{securityContext.userName}").toString().toUpperCase());
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
        if(savedSuccessMessage != null){
            savedSuccessMessage.setVisible(Boolean.FALSE);
        }
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
//        String databaseList = (String)ADFUtils.evaluateEL("#{row.DatabaseList}");
//        List<String> dbList = new ArrayList<String>();
//        if(databaseList != null){
//            String split[] = databaseList.split(",");
//            for(String db : split){
//                dbList.add(db);
//            }
//        }
//        setSelDatabases(dbList);
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
//        if(selDatabases != null && selDatabases.size() > 0){
//            String databases = "";
//            for(String db : selDatabases){
//                databases = databases + "," + db;
//            }
//            ADFUtils.setEL("#{bindings.DatabaseList.inputValue}", databases.substring(1));
//        } else
//            ADFUtils.setEL("#{bindings.DatabaseList.inputValue}",null);
        
        String soc = (String)ADFUtils.evaluateEL("#{bindings.SocTerm.inputValue}");
        if(soc == null || "".equals(soc)){
            ADFUtils.addMessage(FacesMessage.SEVERITY_WARN, "Please select SOC.");
            return;
        }
        String stoi = (String)ADFUtils.evaluateEL("#{bindings.SafetyTopicOfInterest.inputValue}");
        if(stoi == null || "".equals(stoi)){
            ADFUtils.addMessage(FacesMessage.SEVERITY_WARN, "Please enter Safety Topic of Interest.");
            return;
        }
        Integer domainId = (Integer)ADFUtils.evaluateEL("#{bindings.DomainId.inputValue}");
        if(domainId == null || "".equals(domainId)){
            ADFUtils.addMessage(FacesMessage.SEVERITY_WARN, "Please select Data domain.");
            return;
        }
        if(selRiskPurposes != null && selRiskPurposes.size() > 0){
            String riskPurposes = "";
            for(String riskPurpose : selRiskPurposes){
                riskPurposes = riskPurposes + "," + riskPurpose;
            }
            ADFUtils.setEL("#{bindings.RiskPurposeList.inputValue}", riskPurposes.substring(1));
        } else{
            ADFUtils.addMessage(FacesMessage.SEVERITY_WARN, "Please select atleast one Risk Purpose.");
            ADFUtils.setEL("#{bindings.RiskPurposeList.inputValue}",null);
            return;
        }
        
        OperationBinding oper = ADFUtils.findOperation("Commit");
        oper.execute();
        if (oper.getErrors().size() > 0) {
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
            if(savedSuccessMessage != null){
                savedSuccessMessage.setVisible(Boolean.FALSE);
                ADFUtils.addPartialTarget(savedSuccessMessage);
            }
            if(copySuccessMessage != null){
                copySuccessMessage.setVisible(Boolean.FALSE);
                ADFUtils.addPartialTarget(copySuccessMessage);
            }
        }
        else{
//            ADFUtils.showPopup(successPopup);
            if(savedSuccessMessage != null){
                savedSuccessMessage.setVisible(Boolean.TRUE);
                ADFUtils.addPartialTarget(savedSuccessMessage);
            }
            if(copySuccessMessage != null){
                copySuccessMessage.setVisible(Boolean.TRUE);
                ADFUtils.addPartialTarget(copySuccessMessage);
            }
        }
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
        oper.getParamsMap().put("status", getBaseOrStaging());
        oper.execute();
        if (oper.getErrors().size() > 0) 
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
        ADFUtils.addPartialTarget(riskDefTable);
        setRepoRefreshed(Boolean.FALSE);
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
    
    /** DRAFT to REVIEW */
     public void processReviewDialog(DialogEvent dialogEvent) {
         // Add event code here...
         if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome()))
            processStateChange(ModelConstants.STATE_REVIEW, getReviewSubmitPopup());

    }
       
    /** REVIEW to REVIEWED */
     public void processReviewedDialog(DialogEvent dialogEvent) {
         // Add event code here...
         if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome()))
             processStateChange(ModelConstants.STATE_REVIEWED, getCrsReviewedPopup());    
     }
       
    /** REVIEWED to TASL APPROVE */
     public void processTaslReviewSubmit(DialogEvent dialogEvent) {
         // Add event code here...
         if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome()))
             processStateChange(ModelConstants.STATE_TASLAPPROVE, getSubmitApprovalPopup());
     }
       
    /** TASL APPROVE to ML APPROVE */ 
     public void processTaslApprove(DialogEvent dialogEvent) {
         // Add event code here...
         if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome())) 
             processStateChange(ModelConstants.STATE_MLAPPROVE, getCrsApprovePopup());
     }
      
    /** TASL APPROVE to DRAFT */ 
     public void processTaslReject(DialogEvent dialogEvent) {
         // Add event code here...
        if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome())) {
            String taslComments = (String)ADFUtils.evaluateEL("#{bindings.TaslRejectComment.inputValue}");
            if (taslComments==null || (taslComments != null && "".equals(taslComments.trim()))){
                ADFUtils.showFacesMessage("Please enter your comments for rejection.",
                                          FacesMessage.SEVERITY_ERROR, getTaslCommentsInputText());                
                return;
            }
            
            // change to DRAFT state
            processStateChange(ModelConstants.STATE_DRAFT, getCrsRejectPopup());
        }
     }
   
    /** ML APPROVE to APPROVED */ 
     public void processMLApprove(DialogEvent dialogEvent) {
         // Add event code here...
         if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome()))
             processStateChange(ModelConstants.STATE_APPROVED, getCrsApprovePopup());
     }
       
    /** ML APPROVE to DRAFT */ 
     public void processMLReject(DialogEvent dialogEvent) {
         // Add event code here...
        if (DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome())) {
            String mlComments = (String)ADFUtils.evaluateEL("#{bindings.MedicalLeadRejectComment.inputValue}");
            if (mlComments == null || (mlComments != null && "".equals(mlComments.trim()))) {
                ADFUtils.showFacesMessage("Please enter your comments for rejection.",
                                          FacesMessage.SEVERITY_ERROR, getMlCommentsInputText());
                return;
            }

            // change to DRAFT state
            processStateChange(ModelConstants.STATE_DRAFT, getCrsRejectPopup());
        }
     }
       
     /** BSL DEMOTE - any state to DRAFT */
    public void processDemoteToDraftDialog(DialogEvent dialogEvent) {
        // Add event code here...
         if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome()))
             processStateChange(ModelConstants.STATE_DRAFT, getCrsDemoteDraftPopupBinding());
    }

    /** APPROVED to PUBLISHED */ 
     public void processPublishDialog(DialogEvent dialogEvent) {
         // Add event code here...
        if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome())) {
            OperationBinding op = ADFUtils.findOperation("activateCrs");
            Map params = op.getParamsMap();
            params.put("pCRSId", ADFUtils.evaluateEL("#{bindings.CrsId.inputValue}"));
            params.put("pReasonForChange", getReasonForChange());
            String msg = (String)op.execute();

            if (op.getErrors() != null && op.getErrors().size() > 0) {
                ADFUtils.showFacesMessage("An internal error has occured. Please contact the Administrator",
                                          FacesMessage.SEVERITY_ERROR);
            } else {

                // if NOT a success
                if (!ModelConstants.PLSQL_CALL_SUCCESS.equals(msg)) {
                    ADFUtils.showFacesMessage("<html> <body> <p> An internal error has occured. Please contact the Administrator </p> <p>"+msg+"</p> </body> </html>",
                                              FacesMessage.SEVERITY_ERROR);
                    // if success - show popup which on ack takes user to search page
                } else
                    ADFUtils.showPopup(getCrsPublishPopupBinding());
            }
        }
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
        clickHierarchy();
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        hints.add(RichPopup.PopupHints.HintTypes.HINT_ALIGN_ID, this.getHiddenPopupAlign());
        hints.add(RichPopup.PopupHints.HintTypes.HINT_ALIGN, RichPopup.PopupHints.AlignTypes.ALIGN_END_BEFORE);
        hierPopup.show(hints);
//        ADFUtils.showPopup(hierPopup);
    }
    
    public void onClickCopyHierarchySearch(ActionEvent actionEvent) {
        clickHierarchy();
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        hints.add(RichPopup.PopupHints.HintTypes.HINT_ALIGN_ID, this.getCopyRiskDefTable());
        hints.add(RichPopup.PopupHints.HintTypes.HINT_ALIGN, RichPopup.PopupHints.AlignTypes.ALIGN_END_AFTER);
        hierPopup.show(hints);
    }
    
    private void clickHierarchy(){
        DCIteratorBinding iter = ADFUtils.findIterator("HierarchySearchVOIterator");
        ViewObject hierVO = iter.getViewObject();
        hierVO.executeEmptyRowSet();
        DCIteratorBinding childIter = ADFUtils.findIterator("HierarchyChildVOIterator");
        ViewObject childVO = childIter.getViewObject();
        childVO.executeEmptyRowSet();
        setTerm(null);
        setLevel(null);
        setDictionary(null);
        setContentId(null);
        if(childTreeTable != null)
            childTreeTable.setVisible(false);
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

        UIXCollection dragTable = (UIXCollection)dropEvent.getDragComponent();
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
            Object dataObj = dragTable.getRowData();
            if (dataObj instanceof HierarchyChildUIBean) {
                HierarchyChildUIBean selRow = (HierarchyChildUIBean)dragTable.getRowData();
                if(selRow.getLevelName().equals(new Long(0))){
                    ADFUtils.showPopup(parentError);
                    dragTable.setRowKey(dragCurrentRowKey);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(dragTable);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(dropTable);
                    return DnDAction.NONE;
                }
                else{
                    String version = selRow.getDictContentAltCode();
                    String dict = selRow.getDictShortName();
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
                    riskDefRow.setAttribute("MeddraCode", selRow.getDictContentCode());
                    riskDefRow.setAttribute("MeddraLevel", selRow.getLevelName());
                    riskDefRow.setAttribute("MeddraTerm", selRow.getTerm());
                    riskDefRow.setAttribute("MeddraDict", selRow.getDictShortName());
                    riskDefRow.setAttribute("MeddraVersion", ADFUtils.getPageFlowScopeValue("childVersion"));
                    riskDefRow.setAttribute("MeddraVersionDate", ADFUtils.getPageFlowScopeValue("childDate"));

                    if (dict != null && "NMATSMQ".equalsIgnoreCase(dict)) {
                        if (selRow.getTerm() != null && selRow.getTerm().contains("NMQ"))
                            riskDefRow.setAttribute("MeddraExtension", "NMQ");
                        else if (selRow.getTerm() != null && selRow.getTerm().contains("CMQ"))
                            riskDefRow.setAttribute("MeddraExtension", "CMQ");
                        else if (selRow.getTerm() != null && selRow.getTerm().contains("SMQ"))
                            riskDefRow.setAttribute("MeddraExtension", "SMQ");
                        else
                            riskDefRow.setAttribute("MeddraExtension", selRow.getLevelName());
                    } else
                        riskDefRow.setAttribute("MeddraExtension", selRow.getLevelName());


//                    riskDefRow.setAttribute("MeddraQualifier", getChildScope());
                    riskDefRow.setAttribute("TmsDictContentEntryTs", selRow.getTmsDictContentEntryTs());
                    riskDefRow.setAttribute("TmsDictContentId", selRow.getTmsDictContentId());
                    riskDefRow.setAttribute("TmsEndTs", selRow.getTmsEndTs());
                    riskDefRow.setAttribute("MeddraQualifier", selRow.getQual());
                    riskDefRow.setAttribute("MeddraQualifierUpdFlag", selRow.getQualFlag());
                    riskDefRow.setAttribute("CrsQualifier", selRow.getQual());
                    riskDefVO.insertRow(riskDefRow);
                }
            } else {
                JUCtrlHierNodeBinding rowBinding = (JUCtrlHierNodeBinding)dragTable.getRowData();
                dragRow = rowBinding.getRow();
                dragNodeVO = dragRow.getStructureDef().getDefName();
                if ("HierarchySearchVO".equalsIgnoreCase(dragNodeVO)) {
                    String term = (String)dragRow.getAttribute("Mqterm");
                    String code = (String)dragRow.getAttribute("Mqcode");
                    String level = (String)dragRow.getAttribute("Mqlevel");
                    String qual = (String)dragRow.getAttribute("Mqcrtev");
                    String dict = (String)dragRow.getAttribute("DictNm");
                    String version = (String)dragRow.getAttribute("DictVersion");

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
                    riskDefRow.setAttribute("MeddraVersionDate", dragRow.getAttribute("DictVersionDate"));
                    riskDefRow.setAttribute("TmsDictContentEntryTs", dragRow.getAttribute("DictContentEntryTs"));
                    riskDefRow.setAttribute("TmsDictContentId", dragRow.getAttribute("DictContentId"));
                    riskDefRow.setAttribute("TmsEndTs", dragRow.getAttribute("EndTs"));
                    if (dict != null && "NMATSMQ".equalsIgnoreCase(dict)) {
                        if (term != null && term.contains("NMQ"))
                            riskDefRow.setAttribute("MeddraExtension", "NMQ");
                        else if (term != null && term.contains("CMQ"))
                            riskDefRow.setAttribute("MeddraExtension", "CMQ");
                        else if (term != null && term.contains("SMQ"))
                            riskDefRow.setAttribute("MeddraExtension", "SMQ");
                    } else
                        riskDefRow.setAttribute("MeddraExtension", level);

               
                    riskDefRow.setAttribute("MeddraQualifier", dragRow.getAttribute("Qual"));
                    riskDefRow.setAttribute("CrsQualifier", dragRow.getAttribute("Qual"));
                    //                meddra_qualifier IN ('BROAD','NARROW','CHILD NARROW')
                    
                    riskDefRow.setAttribute("MeddraQualifierUpdFlag", dragRow.getAttribute("QualFlag"));
                
                    riskDefVO.insertRow(riskDefRow);
                }
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
        ExcelExportUtils excUtils = new ExcelExportUtils();
        InputStream excelInputStream = excUtils.getExcelInpStream();
        InputStream imageInputStream = excUtils.getImageInpStream();
        try {
            //create sheet
            DCIteratorBinding iter = null;
            if (ModelConstants.BASE_FACET.equals(getBaseOrStaging())) {
                iter = ADFUtils.findIterator("CrsRiskBaseVOIterator");
            } else
                iter = ADFUtils.findIterator("CrsRiskVOIterator");

            RowSetIterator rowSet = null;
            int rowStartIndex = 12;
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
//            columnMap.put("DatabaseList",
//                          rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskVO.DatabaseId_LABEL"));
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
            writeHeaderData(sheet);
            ExcelExportUtils.writeExcelSheet(sheet, rowSet, rowStartIndex,
                                             cellStartIndex, columnMap, null,
                                             dateCellFormat, emptyValReplace,imageInputStream);
            //write image to sheet
            //ExcelExportUtils.writeImageTOExcel(sheet,imageInputStream);
            
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

    public void onCancelCrsRiskPopup(ActionEvent actionEvent) {
        DCIteratorBinding iter = ADFUtils.findIterator("CrsRiskDefinitionsVOIterator");
        ViewObject riskDefVO = iter.getViewObject();
        Row currRow = riskDefVO.getCurrentRow();
        if(currRow != null){
            currRow.refresh(Row.REFRESH_WITH_DB_FORGET_CHANGES | Row.REFRESH_UNDO_CHANGES);
        }
        
        
//        OperationBinding oper = ADFUtils.findOperation("Rollback");
//        oper.execute();
//        if (oper.getErrors().size() > 0) 
//            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
        Long crsId = (Long)ADFUtils.getPageFlowScopeValue("crsId");
        OperationBinding oper1 = ADFUtils.findOperation("initRiskRelation");
        oper1.getParamsMap().put("crsId", crsId);
        oper1.getParamsMap().put("status", getBaseOrStaging());
        oper1.execute();
        if (oper1.getErrors().size() > 0) 
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
        if(riskDefTable != null)
            ADFUtils.addPartialTarget(riskDefTable);
        if(riskDefPopup != null)
            riskDefPopup.hide();
        if(copyPopup != null)
            copyPopup.hide();
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setChildScope(String childScope) {
        this.childScope = childScope;
    }

    public String getChildScope() {
        return childScope;
    }

    public void executeHierarchyChild(ActionEvent actionEvent) {
        DCIteratorBinding childIter = ADFUtils.findIterator("HierarchyChildVOIterator");
        ViewObject childVO = childIter.getViewObject();
        childVO.setNamedWhereClauseParam("bContentId", ADFUtils.evaluateEL("#{row.ContentId}"));
        childVO.executeQuery();
        if(childVO.getEstimatedRowCount() > 0){
            HierarchyChildUIBean parRow = new HierarchyChildUIBean(childVO.first());
            childVO.setCurrentRow(childVO.first());
            HierarchyChildVORowImpl parVORow = (HierarchyChildVORowImpl)childVO.first();
            RowIterator rs = parVORow.getHierarchyChildDetailVO();
            List<HierarchyChildUIBean> childRows = new ArrayList<HierarchyChildUIBean>();
            while(rs.hasNext()){
                Row childRow = rs.next();
                childRows.add(new HierarchyChildUIBean(childRow));                
            }
            parRow.setChildren(childRows);
            hierChildList = new ArrayList<HierarchyChildUIBean>();
            hierChildList.add(parRow);
        }
        hierChildTreeModel = new ChildPropertyTreeModel(hierChildList, "children");
        getChildTreeTable().setVisible(Boolean.TRUE);
        
        ADFUtils.setPageFlowScopeValue("childVersion", ADFUtils.evaluateEL("#{row.DictVersion}"));
        ADFUtils.setPageFlowScopeValue("childDate", ADFUtils.evaluateEL("#{row.DictVersionDate}"));
        
        ADFUtils.addPartialTarget(getChildTreeTable());
    }

    public void setChildTreeTable(RichTreeTable childTreeTable) {
        this.childTreeTable = childTreeTable;
    }

    public RichTreeTable getChildTreeTable() {
        return childTreeTable;
    }

    public void setParentError(RichPopup parentError) {
        this.parentError = parentError;
    }

    public RichPopup getParentError() {
        return parentError;
    }
    
    public void searchCrs(ActionEvent actionEvent) {
        String stoi = getSafetyTopicOfInterest();
        DCIteratorBinding iter = ADFUtils.findIterator("CopyCrsRiskVOIterator");
        ViewObject crsSearchVO = iter.getViewObject();
        crsSearchVO.setWhereClause("SAFETY_TOPIC_OF_INTEREST like '"+stoi+"'");
        System.err.println(crsSearchVO.getQuery());
        crsSearchVO.executeQuery();
    }

    public void setSafetyTopicOfInterest(String safetyTopicOfInterest) {
        this.safetyTopicOfInterest = safetyTopicOfInterest;
    }

    public String getSafetyTopicOfInterest() {
        return safetyTopicOfInterest;
    }
    
    public void copyCrsRiskRelation(ActionEvent actionEvent) {
    
//        DCBindingContainer dcbind =(DCBindingContainer)getBindings();
//        Boolean dirty = dcbind.getDataControl().isTransactionModified();
//        if(dirty){
//            ADFUtils.showPopup(pendingPopup);
//            return;
//        }
        
        ADFUtils.setPageFlowScopeValue("popupMode", "Edit");
        Long riskId = (Long)ADFUtils.evaluateEL("#{copyRow.CrsRiskId}");
        Long crsId = (Long)ADFUtils.getPageFlowScopeValue("crsId");
//        String databaseList = (String)ADFUtils.evaluateEL("#{copyRow.DatabaseList}");
//        List<String> dbList = new ArrayList<String>();
//        if(databaseList != null){
//            String split[] = databaseList.split(",");
//            for(String db : split){
//                dbList.add(db.trim());
//            }
//        }
//        setSelDatabases(dbList);
        String riskPurposeList = (String)ADFUtils.evaluateEL("#{copyRow.RiskPurposeList}");
        List<String> rpList = new ArrayList<String>();
        if(riskPurposeList != null){
            if(riskPurposeList.endsWith(",")){
                riskPurposeList = riskPurposeList.substring(0, riskPurposeList.length()-1);
            }
            String split[] = riskPurposeList.split(",");
            for(String rp : split){
                rpList.add(rp.trim());
            }
        }
        setSelRiskPurposes(rpList);
//        if(copyDBListBinding != null)
//            ResetUtils.reset(copyDBListBinding);
        if(copyRPListBinding != null)
            ResetUtils.reset(copyRPListBinding);
        Map params = new HashMap<String, Object>();
        params.put("srcRiskId", riskId);
        params.put("destCrsId", crsId);
        try {
            ADFUtils.executeAction("copyCurrentRiskRelation", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        copyPanel.setVisible(true);
        ADFUtils.addPartialTarget(copyPanel);
    }
    
    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public void setCopyPopup(RichPopup copyPopup) {
        this.copyPopup = copyPopup;
    }

    public RichPopup getCopyPopup() {
        return copyPopup;
    }
    
    public void setCopyPanel(RichPanelGroupLayout copyPanel) {
        this.copyPanel = copyPanel;
    }

    public RichPanelGroupLayout getCopyPanel() {
        return copyPanel;
    }

    public void setPendingPopup(RichPopup pendingPopup) {
        this.pendingPopup = pendingPopup;
    }

    public RichPopup getPendingPopup() {
        return pendingPopup;
    }

    public void onClickYes(ActionEvent actionEvent) {
        OperationBinding oper = ADFUtils.findOperation("Rollback");
        oper.execute();
        if (oper.getErrors().size() > 0) 
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
         else
            copyCrsRiskRelation(actionEvent);
    }
    
    public void onClickCopy(ActionEvent actionEvent) {
        setSafetyTopicOfInterest(null);
        if(stoiBinding != null)
            ResetUtils.reset(stoiBinding);
        setSelDatabases(null);
        setSelDesigneeList(null);
        DCIteratorBinding iter = ADFUtils.findIterator("CopyCrsRiskVOIterator");
        ViewObject crsSearchVO = iter.getViewObject();
        crsSearchVO.executeEmptyRowSet();
        ADFUtils.showPopup(copyPopup);
        if(copyPanel != null)
            copyPanel.setVisible(Boolean.FALSE);
        if(copySuccessMessage != null)
            copySuccessMessage.setVisible(Boolean.FALSE);
    }

    public void deleteCopiedRiskDefs(ActionEvent actionEvent) {
        RowKeySet rowKeySet = (RowKeySet)copyRiskDefTable.getSelectedRowKeys();
        CollectionModel cm = (CollectionModel)copyRiskDefTable.getValue();
        for (Object facesTreeRowKey : rowKeySet) {
            cm.setRowKey(facesTreeRowKey);
            JUCtrlHierNodeBinding rowData = (JUCtrlHierNodeBinding)cm.getRowData();
            rowData.getRow().remove();
        }
    }

    /**
     * @param copyRiskDefTable
     */
    public void setCopyRiskDefTable(RichTable copyRiskDefTable) {
        this.copyRiskDefTable = copyRiskDefTable;
    }

    /**
     * @return
     */
    public RichTable getCopyRiskDefTable() {
        return copyRiskDefTable;
    }

    /**
     * @param savedSuccessMessage
     */
    public void setSavedSuccessMessage(RichPanelLabelAndMessage savedSuccessMessage) {
        this.savedSuccessMessage = savedSuccessMessage;
    }

    /**
     * @return
     */
    public RichPanelLabelAndMessage getSavedSuccessMessage() {
        return savedSuccessMessage;
    }

    /**
     * @param copySuccessMessage
     */
    public void setCopySuccessMessage(RichPanelLabelAndMessage copySuccessMessage) {
        this.copySuccessMessage = copySuccessMessage;
    }

    /**
     * @return
     */
    public RichPanelLabelAndMessage getCopySuccessMessage() {
        return copySuccessMessage;
    }

    /**
     * @param hiddenPopupAlign
     */
    public void setHiddenPopupAlign(RichOutputText hiddenPopupAlign) {
        this.hiddenPopupAlign = hiddenPopupAlign;
    }

    /**
     * @return
     */
    public RichOutputText getHiddenPopupAlign() {
        return hiddenPopupAlign;
    }

    public void setStoiBinding(RichInputText stoiBinding) {
        this.stoiBinding = stoiBinding;
    }

    public RichInputText getStoiBinding() {
        return stoiBinding;
    }

    public void setCopyDBListBinding(RichSelectManyChoice copyDBListBinding) {
        this.copyDBListBinding = copyDBListBinding;
    }

    public RichSelectManyChoice getCopyDBListBinding() {
        return copyDBListBinding;
    }

    public void setCopyRPListBinding(RichSelectManyChoice copyRPListBinding) {
        this.copyRPListBinding = copyRPListBinding;
    }

    public RichSelectManyChoice getCopyRPListBinding() {
        return copyRPListBinding;
    }

    /**
     * @param searchFacetBinding
     */
    public void setSearchSwitherBinding(UIXSwitcher searchSwitherBinding) {
        this.searchSwitherBinding = searchSwitherBinding;
    }

    /**
     * @return
     */
    public UIXSwitcher getSearchSwitherBinding() {
        return searchSwitherBinding;
    }

    /**
     * @param selectionEvent
     */
    public void baseContentVOSelectionListener(SelectionEvent selectionEvent) {
        // Add event code here...
        ADFUtils.invokeEL("#{bindings.CrsContentBaseVO.collectionModel.makeCurrent}", new Class[] {SelectionEvent.class},
                                 new Object[] { selectionEvent });
        // get the selected row , by this you can get any attribute of that row
        CrsContentBaseVORowImpl selectedRow =
                   (CrsContentBaseVORowImpl)ADFUtils.evaluateEL("#{bindings.CrsContentBaseVOIterator.currentRow}");
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

    public void refreshRepository(ActionEvent actionEvent) {
        Map params = new HashMap<String, Object>();
        params.put("crsId", ADFUtils.getPageFlowScopeValue("crsId"));
        try {
            ADFUtils.executeAction("refreshRepository", params);
            setRepoRefreshed(Boolean.TRUE);
            ADFUtils.addPartialTarget(riskDefTable);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    public void setRepoRefreshed(Boolean repoRefreshed) {
        this.repoRefreshed = repoRefreshed;
    }

    public Boolean getRepoRefreshed() {
        return repoRefreshed;
    }


    public String initializeCreateUpdateCRS() {
        // chk if there exists a CRS in staging table with the same as selected CRS in base table
        if (!"anonymous".equals(userName) &&
            ModelConstants.BASE_FACET.equals(getBaseOrStaging()) &&
            ViewConstants.FLOW_TYPE_UPDATE.equals(getFlowType())) {
            setReasonForChange(null);
            ADFUtils.showPopup(getModifyReasonChngPopup());
            return "toSearch";
        } else
            return "createUpdateCRS";
    }

    public void setBaseOrStaging(String baseOrStaging) {
        this.baseOrStaging = baseOrStaging;
    }

    public String getBaseOrStaging() {
        return baseOrStaging;
    }

    public void setHierChildTreeModel(ChildPropertyTreeModel hierChildTreeModel) {
        this.hierChildTreeModel = hierChildTreeModel;
    }

    public ChildPropertyTreeModel getHierChildTreeModel() {
        return hierChildTreeModel;
    }

    public void setHierChildList(List<HierarchyChildUIBean> hierChildList) {
        this.hierChildList = hierChildList;
    }

    public List<HierarchyChildUIBean> getHierChildList() {
        return hierChildList;
    }

    /**
     * @param actionEvent
     */
    public void reactivateCRS(ActionEvent actionEvent) {
        // Add event code here...
        setReasonForChange(null);
        ADFUtils.showPopup(getReactivatePopupBinding());
    }

    /**
     * @param actionEvent
     */
    public void retireCRS(ActionEvent actionEvent) {
        // Add event code here...
        setReasonForChange(null);
        ADFUtils.showPopup(getRetirePopupBinding());
    }

    /**
     * @param searchBaseTableBinding
     */
    public void setSearchBaseTableBinding(RichTable searchBaseTableBinding) {
        this.searchBaseTableBinding = searchBaseTableBinding;
    }

    /**
     * @return
     */
    public RichTable getSearchBaseTableBinding() {
        return searchBaseTableBinding;
    }

    public void setReasonChangePopup(RichDialog reasonChangePopup) {
        this.reasonChangePopup = reasonChangePopup;
    }

    public RichDialog getReasonChangePopup() {
        return reasonChangePopup;
    }

    /**
     * @param modifyReasonChngPopup
     */
    public void setModifyReasonChngPopup(RichPopup modifyReasonChngPopup) {
        this.modifyReasonChngPopup = modifyReasonChngPopup;
    }

    /**
     * @return
     */
    public RichPopup getModifyReasonChngPopup() {
        return modifyReasonChngPopup;
    }

    /**
     * @param retireReactvteReasonPopup
     */
    public void setRetireReactvteReasonPopup(RichInputText retireReactvteReasonPopup) {
        this.retireReactvteReasonPopup = retireReactvteReasonPopup;
    }

    /**
     * @return
     */
    public RichInputText getRetireReactvteReasonPopup() {
        return retireReactvteReasonPopup;
    }

    /**
     * @param dialogEvent
     */
    public void retireConfirmDialogListener(DialogEvent dialogEvent) {
        // Add event code here...
        if (DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome())) {
            CrsContentBaseVORowImpl row =
                (CrsContentBaseVORowImpl)ADFUtils.evaluateEL("#{bindings.CrsContentBaseVOIterator.currentRow}");
            if (row.getCrsId() != null) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("pCRSId", row.getCrsId());
                params.put("pReasonForChange", getReasonForChange());
                try {
                    String msg =
                        (String)ADFUtils.executeAction("retireCrs", params);
                    if (!ModelConstants.PLSQL_CALL_SUCCESS.equals(msg)) {
                        ADFUtils.setEL("#{pageFlowScope.plsqlerror}", msg);
                        ADFUtils.showPopup(getErrorPLSqlPopup());
                    }
                    onClickSearch(new ActionEvent((UIComponent)dialogEvent.getSource()));
                    getSearchBaseTableBinding().resetStampState();
                    ADFUtils.addPartialTarget(getSearchBaseTableBinding());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param dialogEvent
     */
    public void reactivateConfirmDialogListener(DialogEvent dialogEvent) {
        // Add event code here...
        if (DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome())) {
            CrsContentBaseVORowImpl row =
                (CrsContentBaseVORowImpl)ADFUtils.evaluateEL("#{bindings.CrsContentBaseVOIterator.currentRow}");
            if (row.getCrsId() != null) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("pCRSId", row.getCrsId());
                params.put("pReasonForChange", getReasonForChange());
                try {
                    String msg =
                        (String)ADFUtils.executeAction("reactivateCrs",
                                                       params);
                    if (!ModelConstants.PLSQL_CALL_SUCCESS.equals(msg)) {
                        ADFUtils.setEL("#{pageFlowScope.plsqlerror}", msg);
                        ADFUtils.showPopup(getErrorPLSqlPopup());
                    }
                    onClickSearch(new ActionEvent((UIComponent)dialogEvent.getSource()));
                    getSearchBaseTableBinding().resetStampState();
                    ADFUtils.addPartialTarget(getSearchBaseTableBinding());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param reactivatePopupBinding
     */
    public void setReactivatePopupBinding(RichPopup reactivatePopupBinding) {
        this.reactivatePopupBinding = reactivatePopupBinding;
    }

    /**
     * @return
     */
    public RichPopup getReactivatePopupBinding() {
        return reactivatePopupBinding;
    }

    /**
     * @param retirePopupBinding
     */
    public void setRetirePopupBinding(RichPopup retirePopupBinding) {
        this.retirePopupBinding = retirePopupBinding;
    }

    /**
     * @return
     */
    public RichPopup getRetirePopupBinding() {
        return retirePopupBinding;
    }

    /**
     * @param reasonForChange
     */
    public void setReasonForChange(String reasonForChange) {
        this.reasonForChange = reasonForChange;
    }

    /**
     * @return
     */
    public String getReasonForChange() {
        return reasonForChange;
    }

    /**
     * @param errorPLSqlPopup
     */
    public void setErrorPLSqlPopup(RichPopup errorPLSqlPopup) {
        this.errorPLSqlPopup = errorPLSqlPopup;
    }

    /**
     * @return
     */
    public RichPopup getErrorPLSqlPopup() {
        return errorPLSqlPopup;
    }

    public String onClickModifyCrs() {
        // Add event code here...
        DCBindingContainer bc =
            ADFUtils.findBindingContainerByName(ViewConstants.PAGE_DEF_SEARCH);
        DCIteratorBinding iter =
            bc.findIteratorBinding("CrsContentBaseVOIterator");
        Long crsId = null;
        if (iter.getCurrentRow() != null) {
            crsId = (Long)iter.getCurrentRow().getAttribute("CrsId");

            //invoke vc on stg table with this crs id
            boolean isCrsFound = Boolean.FALSE;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("pCrsId", crsId);
            OperationBinding op =
                ADFUtils.getOperBindFromPageDef(ViewConstants.PAGE_DEF_SEARCH,
                                                "findByCrsFromStg");
            op.getParamsMap().put("pCrsId", crsId);
            isCrsFound = (Boolean)op.execute();

            if (op.getErrors() != null && op.getErrors().size() > 0) {
                ADFUtils.showFacesMessage("An Internal Error occured,Please try again later.",
                                          FacesMessage.SEVERITY_ERROR);
                setBaseOrStaging(ModelConstants.BASE_FACET);
                ADFUtils.closeDialog(getModifyReasonChngPopup());
                return "navToSearch";
            } else {

                // if found - show faces message that the CRS already in update process
                if (isCrsFound) {
                    ADFUtils.showFacesMessage("The selected CRS is already in update process",
                                              FacesMessage.SEVERITY_INFO);
                    setBaseOrStaging(ModelConstants.BASE_FACET);
                    ADFUtils.closeDialog(getModifyReasonChngPopup());
                    return "navToSearch";
                } else {
                    // if NOT found - call MODIDY_CRS
                    String resultMsg = null;
                    op =
 ADFUtils.getOperBindFromPageDef(ViewConstants.PAGE_DEF_SEARCH, "modifyCrs");
                    op.getParamsMap().put("pCRSId", crsId);
                    op.getParamsMap().put("pReasonForChange", getReasonForChange());
                    resultMsg = (String)op.execute();

                    if (op.getErrors() != null && op.getErrors().size() > 0) {
                        ADFUtils.setEL("#{pageFlowScope.plsqlerror}", resultMsg);
                        ADFUtils.showPopup(getErrorPLSqlPopup());
                        setBaseOrStaging(ModelConstants.BASE_FACET);
                        ADFUtils.closeDialog(getModifyReasonChngPopup());
                        return "navToSearch";
                    } else {
                        // if PL/SQL call return value is success - set current row of staging table to CRS ID
                        if (ModelConstants.PLSQL_CALL_SUCCESS.equals(resultMsg)) {
                            //set the staging table to current crs id
                            bc.findIteratorBinding("CrsContentVOIterator").getViewObject().applyViewCriteria(null);
                            bc.findIteratorBinding("CrsContentVOIterator").executeQuery();
                            ADFUtils.setEL("#{pageFlowScope.crsId}", crsId);

                            // page should navigate to add details page
                            op =
 ADFUtils.getOperBindFromPageDef(ViewConstants.PAGE_DEF_CREATE,
                                 "setCurrentRowWithKeyValue");
                            op.execute();
                            if (op.getErrors() != null &&
                                op.getErrors().size() > 0) {
                                ADFUtils.showFacesMessage("An Internal Error occured,Please try again later.",
                                                          FacesMessage.SEVERITY_ERROR);
                                setBaseOrStaging(ModelConstants.BASE_FACET);
                                ADFUtils.closeDialog(getModifyReasonChngPopup());
                                return "navToSearch";
                            } else {
                                // set mode to staging
                                setBaseOrStaging(ModelConstants.STAGING_FACET);
                                ADFUtils.closeDialog(getModifyReasonChngPopup());
                                return "navToCreate";
                            }
                        } else {
                            setBaseOrStaging(ModelConstants.BASE_FACET);
                            ADFUtils.setEL("#{pageFlowScope.plsqlerror}", resultMsg);
                            ADFUtils.showPopup(getErrorPLSqlPopup());
                            ADFUtils.closeDialog(getModifyReasonChngPopup());
                            return "navToSearch";
                        }
                    }
                }
            }
        } else {
            ADFUtils.showFacesMessage("Please select a row to update before navigating.",
                                      FacesMessage.SEVERITY_INFO);
            setBaseOrStaging(ModelConstants.BASE_FACET);
            ADFUtils.closeDialog(getModifyReasonChngPopup());
            return "navToSearch";
        }
        //default nav to createUpdate
        //return "createUpdate";
    }

    public String cancelModifyCrs() {
        // Add event code here...
        ADFUtils.closeDialog(getModifyReasonChngPopup());
        return "navToSearch";
    }

    public void refreshRepoInPopup(ActionEvent actionEvent) {
        Map params = new HashMap<String, Object>();
        params.put("crsId", ADFUtils.getPageFlowScopeValue("crsId"));
        try {
            ADFUtils.executeAction("refreshRepository", params);
            setRepoRefreshed(Boolean.TRUE);
            ADFUtils.addPartialTarget(riskDefTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isCrsFieldsUpdatable1() {
        return crsFieldsUpdatable;
    }

    public void setFilterBy1(String filterBy1) {
        this.filterBy1 = filterBy1;
    }

    public String getFilterBy1() {
        return filterBy1;
    }

    public void setFilterBy2(String filterBy2) {
        this.filterBy2 = filterBy2;
    }

    public String getFilterBy2() {
        return filterBy2;
    }

    public void setFilterBy3(String filterBy3) {
        this.filterBy3 = filterBy3;
    }

    public String getFilterBy3() {
        return filterBy3;
    }

    public void setFilterValue1(String filterValue1) {
        this.filterValue1 = filterValue1;
    }

    public String getFilterValue1() {
        return filterValue1;
    }

    public void setFilterValue2(String filterValue2) {
        this.filterValue2 = filterValue2;
    }

    public String getFilterValue2() {
        return filterValue2;
    }

    public void setFilterValue3(String filterValue3) {
        this.filterValue3 = filterValue3;
    }

    public String getFilterValue3() {
        return filterValue3;
    }

    public void setFilterCri1(String filterCri1) {
        this.filterCri1 = filterCri1;
    }

    public String getFilterCri1() {
        return filterCri1;
    }

    public void setFilterCri2(String filterCri2) {
        this.filterCri2 = filterCri2;
    }

    public String getFilterCri2() {
        return filterCri2;
    }

    public void onOkFilter(ActionEvent actionEvent) {
        DCIteratorBinding iter = ADFUtils.findIterator("CrsRiskVOIterator");
        ViewObject riskVO = iter.getViewObject();
        Long crsId = (Long)ADFUtils.getPageFlowScopeValue("crsId");
        StringBuilder whereClause = new StringBuilder("CRS_ID = "+crsId);
        if(filterBy1 != null && filterValue1 != null)
            whereClause.append(" AND ("+filterBy1 + " LIKE '"+filterValue1+"' ");
        if(filterBy2 != null && filterValue2 != null)
            whereClause.append(filterCri1 + " " + filterBy2 + " LIKE '"+filterValue2+"' ");
        if(filterBy3 != null && filterValue3 != null)
            whereClause.append(filterCri2 + " " + filterBy3 + " LIKE '"+filterValue3+"' ");  
        if(filterBy1 != null && filterValue1 != null)
            whereClause.append(")");
        riskVO.setWhereClause(whereClause.toString());
        System.err.println(riskVO.getQuery());
        riskVO.executeQuery();
        advancedFilterPopup.hide();
    }
    
    public void initManageCrs(){
       
        String dictVersion = (String)ADFUtils.getSessionScopeValue("dictVersion");
        if(dictVersion == null){
            OperationBinding oper = ADFUtils.findOperation("fetchDictionaryVersion");
            dictVersion = (String)oper.execute();  
            ADFUtils.setSessionScopeValue("dictVersion", dictVersion);
        }
    }

    public void setAdvancedFilterPopup(RichPopup advancedFilterPopup) {
        this.advancedFilterPopup = advancedFilterPopup;
    }

    public RichPopup getAdvancedFilterPopup() {
        return advancedFilterPopup;
    }

    public void clearFilters(ActionEvent actionEvent) {
        Long crsId = (Long)ADFUtils.getPageFlowScopeValue("crsId");
        OperationBinding oper = ADFUtils.findOperation("initRiskRelation");
        oper.getParamsMap().put("crsId", crsId);
        oper.getParamsMap().put("status", getBaseOrStaging());
        oper.execute();
        if (oper.getErrors().size() > 0) 
            ADFUtils.showFacesMessage("An internal error has occured. Please try later.", FacesMessage.SEVERITY_ERROR);
        setFilterBy1(null);
        setFilterBy2(null);
        setFilterBy3(null);
        setFilterValue1(null);
        setFilterValue2(null);
        setFilterValue3(null);
        setFilterCri1("OR");
        setFilterCri2("OR");
    }

    /**
     * Reload the iteractors on search page.
     */
    public void reloadSearchResults() {
        // Add event code here...
        ADFUtils.closeDialog(getCrsPublishPopupBinding());
        DCBindingContainer bc =
            ADFUtils.findBindingContainerByName(ViewConstants.PAGE_DEF_SEARCH);
        if (bc != null) {
            DCIteratorBinding stgIter =
                bc.findIteratorBinding("CrsContentVOIterator");
            if (stgIter != null) {
                stgIter.executeQuery();
            }
            DCIteratorBinding baseIter =
                bc.findIteratorBinding("CrsContentBaseVOIterator");
            if (baseIter != null) {
                baseIter.executeQuery();
            }
            if (getSearchBaseTableBinding() != null)
                getSearchBaseTableBinding().resetStampState();
            if (getSearchStagingTableBinding() != null)
                getSearchStagingTableBinding().resetStampState();
        }
    }

    /**
     * @param searchStagingTableBinding
     */
    public void setSearchStagingTableBinding(RichTable searchStagingTableBinding) {
        this.searchStagingTableBinding = searchStagingTableBinding;
    }

    /**
     * @return
     */
    public RichTable getSearchStagingTableBinding() {
        return searchStagingTableBinding;
    }

    public void setCurrReleaseStatus(String currReleaseStatus) {
        this.currReleaseStatus = currReleaseStatus;
    }

    public String getCurrReleaseStatus() {
        return currReleaseStatus;
    }

    /**
     * This methodd default reason for change to 'initial version'
     * and shows the pubish popup
     * @param actionEvent
     */
    public void onClickPublish(ActionEvent actionEvent) {
        //default reason for change to initial version
        reasonForChange = ViewConstants.REASON_DEFAULT_VALUE;
        ADFUtils.showPopup(getPublishPopupBinding());
    }

    /**
     * @param publishPopupBinding
     */
    public void setPublishPopupBinding(RichPopup publishPopupBinding) {
        this.publishPopupBinding = publishPopupBinding;
    }

    /**
     * @return
     */
    public RichPopup getPublishPopupBinding() {
        return publishPopupBinding;
    }

    /**
     * @param submitApprovalPopup
     */
    public void setSubmitApprovalPopup(RichPopup submitApprovalPopup) {
        this.submitApprovalPopup = submitApprovalPopup;
    }

    /**
     * @return
     */
    public RichPopup getSubmitApprovalPopup() {
        return submitApprovalPopup;
    }

    /**
     * This method write header data to risk definition report.
     * @param sheet
     */
    private void writeHeaderData(Sheet sheet) {
        //        Excel report Header data to include
        int count = 6;
        //invoke prepareStatesMap to get state names
        if (statesMap == null || (statesMap != null && statesMap.size() == 0))
            prepareStatesMap();
        
        //  •1 CRS Name
        org.apache.poi.ss.usermodel.Row row1 = sheet.createRow(count);
        Cell cell11 = row1.createCell((short)0);
        cell11.setCellValue("CRS Name :" +
                            ADFUtils.evaluateEL("#{pageFlowScope.crsName}"));
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell11.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, 0, 4));
        //•1 CRS ID
        //CRS ID
        Cell cell12 = row1.createCell((short)7);
        cell12.setCellValue("CRS ID : " +
                            (Long)ADFUtils.getPageFlowScopeValue("crsId"));
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell12.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, 7, 10));
        count++;
        //        • 2 Dictionary Version
        org.apache.poi.ss.usermodel.Row row2 = sheet.createRow(count);
        //dictionary version
        Cell cell21 = row2.createCell((short)0);
        cell21.setCellValue("Dictionary Version: " +
                            ADFUtils.evaluateEL("#{sessionScope.dictVersion}"));
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell21.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, 0, 4));
        //Status //        • 2 Status (Active or Inactive)
        String status = "";
        if (ModelConstants.BASE_FACET.equals(getBaseOrStaging())) {
            int stateIdBase =
                (Integer)ADFUtils.evaluateEL("#{bindings.StateIdBase.inputValue}");
            if (stateIdBase == ModelConstants.STATE_RETIRED.intValue())
                status = ModelConstants.CRS_INACTIVE;
            else
                status = ModelConstants.CRS_ACTIVE;
        }
        Cell cell22 = row2.createCell((short)7);
        // TODO
        cell22.setCellValue("Status: " + status);
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell22.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, 7, 10));
        count++;

        //•3 Date and time the report is run
        //Report time
        org.apache.poi.ss.usermodel.Row row3 = sheet.createRow(count);
        Cell cell31 = row3.createCell((short)0);
        cell31.setCellValue("Downloaded Time: " +
                            ModelConstants.getCustomTimeStamp());
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell31.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, 0, 4));
        //•3 Release Status (CURRENT or PENDING)
        String relFlag =
            (String)ADFUtils.evaluateEL("#{bindings.ReleaseStatusFlag.inputValue}");
        String relstatus = "";
        if ("P".equals(relFlag))
            relstatus = "Pending";
        else
            relstatus = "Current";
        Cell cell32 = row3.createCell((short)7);
        cell32.setCellValue("Release Status: " + relstatus);
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell32.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, 7, 10));
        sheet.setColumnWidth(4, 6000);
        count++;
        //        •4 State (only displays the value for PENDING CRSs
        //        •4 BSL
        //BSL
        org.apache.poi.ss.usermodel.Row row4 = sheet.createRow(count);
       
        Cell cell41 = row4.createCell((short)0);
        if (ModelConstants.STAGING_FACET.equals(getBaseOrStaging())) {
            int stateIdstg =
                (Integer)ADFUtils.evaluateEL("#{bindings.StateId.inputValue}");
            cell41.setCellValue("State:  " + statesMap.get(stateIdstg));
        }
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell41.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, 0, 4));
        
        Cell cell42 = row4.createCell((short)7);
        String bsl = null;
        if (ModelConstants.BASE_FACET.equals(getBaseOrStaging())) {
            bsl =
(String)ADFUtils.evaluateEL("#{bindings.BslNameBase.inputValue}");
        } else
            bsl =
(String)ADFUtils.evaluateEL("#{bindings.BslName.inputValue}");
        cell42.setCellValue("BSL: " + bsl);
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell42.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, 7, 10));
        count++;
        //        •5 TASL
        //        •6 Medical Lead
        //TASL
        org.apache.poi.ss.usermodel.Row row5 = sheet.createRow(count);
        Cell cell51 = row5.createCell((short)0);
        String tasl = null;
        if (ModelConstants.BASE_FACET.equals(getBaseOrStaging())) {
            tasl =
(String)ADFUtils.evaluateEL("#{bindings.TaslNameBase.inputValue}");
        } else
            tasl =
(String)ADFUtils.evaluateEL("#{bindings.TaslName.inputValue}");
        cell51.setCellValue("TASL :  " + tasl);
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell51.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, 0, 4));
        //ML name
        Cell cell52 = row5.createCell((short)7);
        String medLLead = null;
        if (ModelConstants.BASE_FACET.equals(getBaseOrStaging())) {
            medLLead =
                    (String)ADFUtils.evaluateEL("#{bindings.MedicalLeadNameBase.inputValue}");
        } else
            medLLead =
                    (String)ADFUtils.evaluateEL("#{bindings.MedicalLeadName.inputValue}");
        cell51.setCellValue("TASL :  " + tasl);
        cell52.setCellValue("Medical Lead: " + medLLead);
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell52.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, 7, 10));
        count++;
    }

    /**
     * This method is used to prepare map with stateid and stateName
     */
    private void prepareStatesMap() {
        statesMap = new HashMap<Integer, String>();
        DCIteratorBinding iter = ADFUtils.findIterator("CrsStateVOIterator");
        if (iter != null) {
            Row[] rows = iter.getAllRowsInRange();
            Integer stateId = null;
            String stateName = null;
            for (Row row : rows) {
                if (row != null) {
                    stateId = (Integer)row.getAttribute("StateId");
                    stateName = (String)row.getAttribute("StateName");
                    statesMap.put(stateId, stateName);
                }
            }
        }
    }

    /**
     * @param nonCompoundSelected
     */
    public void setNonCompoundSelected(boolean nonCompoundSelected) {
        this.nonCompoundSelected = nonCompoundSelected;
    }

    /**
     * @return
     */
    public boolean isNonCompoundSelected() {
        return nonCompoundSelected;
    }

    /**
     * @param stateSwitcherBinding
     */
    public void setStateSwitcherBinding(UIXSwitcher stateSwitcherBinding) {
        this.stateSwitcherBinding = stateSwitcherBinding;
    }

    /**
     * @return
     */
    public UIXSwitcher getStateSwitcherBinding() {
        return stateSwitcherBinding;
    }

    /**
     * @param vce
     */
    public void onChangeReleaseStatus(ValueChangeEvent vce) {
        // Add event code here...
        if (vce != null && !vce.getNewValue().equals(vce.getOldValue())) {
            if (ViewConstants.FLOW_TYPE_SEARCH.equals(getFlowType())) {
                if (ModelConstants.STATUS_PENDING.equals((String)vce.getNewValue())) {
                    ADFUtils.setEL("#{bindings.State.inputValue}", null);
                } else {
                    ADFUtils.setEL("#{bindings.State.inputValue}",
                                   ModelConstants.STATE_ACTIVATED);
                }
            }
            ADFUtils.addPartialTarget(stateSwitcherBinding);
        }
    }
}
