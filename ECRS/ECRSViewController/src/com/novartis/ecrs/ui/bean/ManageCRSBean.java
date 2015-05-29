package com.novartis.ecrs.ui.bean;


import com.novartis.ecrs.model.constants.ModelConstants;
import com.novartis.ecrs.model.lov.UserRoleVORowImpl;
import com.novartis.ecrs.model.view.CrsContentVORowImpl;
import com.novartis.ecrs.model.view.HierarchyChildVORowImpl;
import com.novartis.ecrs.model.view.base.CrsContentBaseVORowImpl;
import com.novartis.ecrs.ui.constants.ViewConstants;
import com.novartis.ecrs.ui.utility.ADFUtils;
import com.novartis.ecrs.ui.utility.ExcelExportUtils;
import com.novartis.ecrs.view.beans.SessionBean;

import org.apache.log4j.Logger;

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
import oracle.jbo.server.ViewObjectImpl;
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
    public static final Logger logger = Logger.getLogger(ManageCRSBean.class);
    private transient RichPanelGroupLayout riskDefPopupPanel;
    private transient RichTable stagingTable;
    private transient ResourceBundle uiBundle =
        BundleFactory.getBundle("com.novartis.ecrs.view.ECRSViewControllerBundle");

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
            ADFUtils.showFacesMessage(uiBundle.getString("INTERNAL_ERROR"), FacesMessage.SEVERITY_ERROR);
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
            if (row.getCompoundType() != null &&
                  ModelConstants.COMPOUND_TYPE_COMPOUND.equalsIgnoreCase(row.getCompoundType())) {
                OperationBinding copyOper = bc.getOperationBinding("copyRoutineDefinition");
                copyOper.getParamsMap().put("crsId", crsId);
                copyOper.execute();
            }
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
        logger.info("--Start-ManageCRSBean:onClickSearch--");
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
        if (ViewConstants.ANONYMOUS_ROLE.equalsIgnoreCase(userName)) {
            ADFUtils.setEL("#{bindings.State.inputValue}",
                           ModelConstants.STATE_ACTIVATED);
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
            ADFUtils.showFacesMessage(uiBundle.getString("INTERNAL_ERROR"),
                                      FacesMessage.SEVERITY_ERROR);
        //TODO log the error 
        logger.info("--End-ManageCRSBean:onClickSearch--");
    }

    /**
     * Invokes execute empty row set on crs content vo.
     */
    public void invokeEmptyRowSetOnContentVO() {
        logger.info("--Start-ManageCRSBean:invokeEmptyRowSetOnContentVO--");
        DCBindingContainer bc =
            ADFUtils.findBindingContainerByName(ViewConstants.PAGE_DEF_SEARCH);
        DCIteratorBinding searchIter =  bc.findIteratorBinding("ECrsSearchVOIterator");
        //Mode is Update and role is BSL,settting CompoundType to COMPOUND
        if (searchIter != null && searchIter.getCurrentRow() != null &&
            ViewConstants.FLOW_TYPE_UPDATE.equals(getFlowType()) &&
            ModelConstants.ROLE_BSL.equals(getLoggedInUserRole())) {
            searchIter.getCurrentRow().setAttribute("CompoundType",
                                                    ModelConstants.COMPOUND_TYPE_COMPOUND);
            logger.info("Update mode and role is BSL,settting CompoundType to COMPOUND");
        }
        DCIteratorBinding iter = bc.findIteratorBinding("CrsContentVOIterator");
        if (iter.getViewObject() != null)
            iter.getViewObject().executeEmptyRowSet();
        DCIteratorBinding baseIter = bc.findIteratorBinding("CrsContentBaseVOIterator");
        if (baseIter.getViewObject() != null)
            baseIter.getViewObject().executeEmptyRowSet();
        logger.info("--End-ManageCRSBean:invokeEmptyRowSetOnContentVO--");
    }

    /**
     * Custom selection listener to populate crs name and designee list.
     * @param selectionEvent
     */
    public void searchTableSelectionListener(SelectionEvent selectionEvent) {
        logger.info("--Start-ManageCRSBean:searchTableSelectionListener--");
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
//        if (ModelConstants.COMPOUND_TYPE_NON_COMPOUND.equals(selectedRow.getCrsCompoundType())) {
//            setNonCompoundSelected(Boolean.TRUE);
//        } else
//            setNonCompoundSelected(Boolean.FALSE);
        logger.info("--End-ManageCRSBean:searchTableSelectionListener--");
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

    /**
     * @return
     */
    public String onClickNext() {
        logger.info("Navigating to next train stop");
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
        logger.info("Init risk Relation : current Crs ID :: "+crsId);
        logger.info("Init risk Relation : Base or Staging :: "+getBaseOrStaging());
        try {
            logger.info("Calling AM method initRiskRelation");
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
        logger.info("Start-ManageCRSBean:onCompCodeSelect()");
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
                ADFUtils.setEL("#{bindings.ReviewApproveRequiredFlag1.inputValue}",
                               "N");
                ADFUtils.setEL("#{bindings.MedicalLeadName.inputValue}", null);
                setSelDesigneeList(null);
                setNonCompoundSelected(Boolean.TRUE);
                logger.info("--------------Selected non compound value----------");
            }
            String crsCompCode = (String)ADFUtils.evaluateEL("#{bindings.CrsCompoundCode.inputValue}");
            String compCode = (String)ADFUtils.evaluateEL("#{bindings.CompoundCode.inputValue}");
            String indication = (String)ADFUtils.evaluateEL("#{bindings.Indication.inputValue}");
            ADFUtils.setEL("#{bindings.CrsName.inputValue}",
                           (compCode != null ? compCode : crsCompCode) + (indication != null ? (" "+indication) : ""));
            //ResetUtils.reset(vce.getComponent().getParent());
            ADFUtils.addPartialTarget(vce.getComponent().getParent());
        }
        logger.info("End-ManageCRSBean:onCompCodeSelect()");
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
            logger.info("loggedInUser role----------"+loggedInUserRole);
        }
        if (ADFUtils.evaluateEL("#{securityContext.userName}") != null) {
            setUserName(ADFUtils.evaluateEL("#{securityContext.userName}").toString().toUpperCase());
            logger.info("user name from security context-------" +
                        ADFUtils.evaluateEL("#{securityContext.userName}"));
        }
    }

    public void addRiskDefinition(ActionEvent actionEvent) {
        DCIteratorBinding realtionIter = ADFUtils.findIterator("CrsRiskRelationVOIterator");
        ViewObject relationVO = realtionIter.getViewObject();
        Row relationRow = relationVO.createRow();
        Long crsId = (Long)ADFUtils.getPageFlowScopeValue("crsId");
        logger.info("AddRiskDefinition crsId "+crsId);
        relationRow.setAttribute("CrsId", crsId);
        relationVO.insertRow(relationRow);
        relationVO.setCurrentRow(relationRow);
        logger.info("Popup mode is add, opens blank risk defintion popup.");
        ADFUtils.setPageFlowScopeValue("popupMode", "Add");
        setSelDatabases(null);
        setSelRiskPurposes(null);
        if(savedSuccessMessage != null){
            savedSuccessMessage.setVisible(Boolean.FALSE);
            ResetUtils.reset(savedSuccessMessage);
        }
        if(riskDefPopupPanel != null)
            ResetUtils.reset(riskDefPopupPanel);
        ADFUtils.showPopup(riskDefPopup);
    }

    public void setRiskDefPopup(RichPopup riskDefPopup) {
        this.riskDefPopup = riskDefPopup;
    }

    public RichPopup getRiskDefPopup() {
        return riskDefPopup;
    }

    public void editRiskDefinition(ActionEvent actionEvent) {
        //Added because, when coming from copy current flow, the new ID is not there in the EO and giving error while setting current row.
        DCIteratorBinding relationIter = ADFUtils.findIterator("CrsRiskRelationVOIterator");
        relationIter.executeQuery();
        
        logger.info("Editing Risk definition, popup mode edit.");
        ADFUtils.setPageFlowScopeValue("popupMode", "Edit");
        Long riskId = (Long)ADFUtils.evaluateEL("#{row.CrsRiskId}");
        logger.info("Current crs risk id "+riskId);
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
        logger.info("Selected risk purpose list :: "+rpList);
        setSelRiskPurposes(rpList);
        
        Map params = new HashMap<String, Object>();
        params.put("rowKey", riskId);
        try {
            ADFUtils.executeAction("setCurrentRiskRelation", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(riskDefPopupPanel != null)
            ResetUtils.reset(riskDefPopupPanel);
        if(savedSuccessMessage != null){
            savedSuccessMessage.setVisible(Boolean.FALSE);
            ResetUtils.reset(savedSuccessMessage);
        }
        ADFUtils.showPopup(riskDefPopup);
    }

    public void deleteRiskDefinitions(ActionEvent actionEvent) {
        logger.info("Deleting Selected Risk Definitions");
        DCIteratorBinding riskDefIter = ADFUtils.findIterator("CrsRiskDefinitionsVOIterator");
        ViewObject riskDefVO = riskDefIter.getViewObject();
        Row[] rows= riskDefVO.getFilteredRows("SelectAttr", Boolean.TRUE);
        for(Row row : rows){
            row.remove();
        }
//        RowKeySet rowKeySet = (RowKeySet)riskDefTable.getSelectedRowKeys();
//        CollectionModel cm = (CollectionModel)riskDefTable.getValue();
//        for (Object facesTreeRowKey : rowKeySet) {
//            cm.setRowKey(facesTreeRowKey);
//            JUCtrlHierNodeBinding rowData = (JUCtrlHierNodeBinding)cm.getRowData();
//            rowData.getRow().remove();
//        }
    }

    public void setRiskDefTable(RichTable riskDefTable) {
        this.riskDefTable = riskDefTable;
    }

    public RichTable getRiskDefTable() {
        return riskDefTable;
    }

    public void saveRiskDefs(ActionEvent actionEvent) {

        String riskPurposes = "";
        if(selRiskPurposes != null && selRiskPurposes.size() > 0){
            for(String riskPurpose : selRiskPurposes){
                riskPurposes = riskPurposes + "," + riskPurpose;
            }
            ADFUtils.setEL("#{bindings.RiskPurposeList.inputValue}", riskPurposes.substring(1));
        } else{
            ADFUtils.addMessage(FacesMessage.SEVERITY_WARN, "Please select at least one Risk Purpose.");
            ADFUtils.setEL("#{bindings.RiskPurposeList.inputValue}",null);
            return;
        }
        logger.info("Selected risk purposes : "+selRiskPurposes);
        
        Long crsId = (Long)ADFUtils.getPageFlowScopeValue("crsId");  
        String riskPurposeList = riskPurposes.substring(1);
        String safetyTopic = (String)ADFUtils.evaluateEL("#{bindings.SafetyTopicOfInterest.inputValue}");
        Map params1 = new HashMap<String, Object>();
        logger.info("Validating it this CRS "+crsId+" has this safety topic already.");
        params1.put("crsId", crsId);
        params1.put("safetyTopic", safetyTopic);
        params1.put("rpList", riskPurposeList);
        params1.put("crsRiskId", ADFUtils.evaluateEL("#{bindings.CrsRiskId.inputValue}"));
        try {
            logger.info("Calling model method validateSafetyTopic");
            Boolean invalid = (Boolean)ADFUtils.executeAction("validateSafetyTopic", params1);
            if(invalid){
                ADFUtils.showFacesMessage(uiBundle.getString("STOI_UNIQUE_ERROR"), FacesMessage.SEVERITY_ERROR);
                return;
            }
        } catch (Exception e) {
            logger.error("Exception occured in validateSafetyTopic()"+e);
        }
        
//        if(selDatabases != null && selDatabases.size() > 0){
//            String databases = "";
//            for(String db : selDatabases){
//                databases = databases + "," + db;
//            }
//            ADFUtils.setEL("#{bindings.DatabaseList.inputValue}", databases.substring(1));
//        } else
//            ADFUtils.setEL("#{bindings.DatabaseList.inputValue}",null);
        
        Integer domain = (Integer)ADFUtils.evaluateEL("#{bindings.DomainId.attributeValue}");
        if(domain != null && (domain == 1)){
            DCIteratorBinding iter = ADFUtils.findIterator("CrsRiskDefinitionsVOIterator");
            ViewObject riskDefVO = iter.getViewObject();
            if(riskDefVO.getEstimatedRowCount() == 0){
                ADFUtils.showFacesMessage(uiBundle.getString("MEDDRA_MANDATE_ERROR"), FacesMessage.SEVERITY_ERROR);
                return;
            }
        }
        
        logger.info("Saving risk defs.");
        String soc = (String)ADFUtils.evaluateEL("#{bindings.SocTerm.inputValue}");
        if(soc == null || "".equals(soc)){
            ADFUtils.addMessage(FacesMessage.SEVERITY_WARN, uiBundle.getString("SOC_MANDATE_ERROR"));
            return;
        }
        String stoi = (String)ADFUtils.evaluateEL("#{bindings.SafetyTopicOfInterest.inputValue}");
        if(stoi == null || "".equals(stoi)){
            ADFUtils.addMessage(FacesMessage.SEVERITY_WARN, uiBundle.getString("STOI_MANDATE_ERROR"));
            return;
        }
        Integer domainId = (Integer)ADFUtils.evaluateEL("#{bindings.DomainId.inputValue}");
        if(domainId == null || "".equals(domainId)){
            ADFUtils.addMessage(FacesMessage.SEVERITY_WARN, uiBundle.getString("DATA_DOMAIN_MANDATE_ERROR"));
            return;
        }
        
        OperationBinding oper = ADFUtils.findOperation("Commit");
        oper.execute();
        if (oper.getErrors().size() > 0) {
            ADFUtils.showFacesMessage(uiBundle.getString("INTERNAL_ERROR"), FacesMessage.SEVERITY_ERROR);
            if(savedSuccessMessage != null){
                savedSuccessMessage.setVisible(Boolean.FALSE);
                ADFUtils.addPartialTarget(savedSuccessMessage);
                ResetUtils.reset(savedSuccessMessage);
            }
            if(copySuccessMessage != null){
                copySuccessMessage.setVisible(Boolean.FALSE);
                ADFUtils.addPartialTarget(copySuccessMessage);
                ResetUtils.reset(savedSuccessMessage);
            }
        }
        else{
//            ADFUtils.showPopup(successPopup);
            if(savedSuccessMessage != null){
                savedSuccessMessage.setVisible(Boolean.TRUE);
                ADFUtils.addPartialTarget(savedSuccessMessage);
                ResetUtils.reset(savedSuccessMessage);
            }
            if(copySuccessMessage != null){
                copySuccessMessage.setVisible(Boolean.TRUE);
                ADFUtils.addPartialTarget(copySuccessMessage);
                ResetUtils.reset(copySuccessMessage);
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
        logger.info("Closing risk defintions popup");
        Long crsId = (Long)ADFUtils.getPageFlowScopeValue("crsId");
        OperationBinding oper = ADFUtils.findOperation("initRiskRelation");
        logger.info("Reexecuting the table with crsId : "+crsId+" state : "+getBaseOrStaging());
        oper.getParamsMap().put("crsId", crsId);
        oper.getParamsMap().put("status", getBaseOrStaging());
        oper.execute();
        if (oper.getErrors().size() > 0) 
            ADFUtils.showFacesMessage(uiBundle.getString("INTERNAL_ERROR"), FacesMessage.SEVERITY_ERROR);
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

    /**
     * @param selDatabases
     */
    public void setSelDatabases(List<String> selDatabases) {
        this.selDatabases = selDatabases;
    }

    /**
     * @return
     */
    public List<String> getSelDatabases() {
        return selDatabases;
    }

    /**
     * @param databaseList
     */
    public void setDatabaseList(List<SelectItem> databaseList) {
        this.databaseList = databaseList;
    }

    /**
     * @return
     */
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
            logger.info("databaseList -->"+databaseList);
        }
        return databaseList;
    }

    /**
     * @param selRiskPurposes
     */
    public void setSelRiskPurposes(List<String> selRiskPurposes) {
        this.selRiskPurposes = selRiskPurposes;
    }

    /**
     * @return
     */
    public List<String> getSelRiskPurposes() {
        return selRiskPurposes;
    }

    /**
     * @param reviewSubmitPopup
     */
    public void setReviewSubmitPopup(RichPopup reviewSubmitPopup) {
        this.reviewSubmitPopup = reviewSubmitPopup;
    }

    /**
     * @return
     */
    public RichPopup getReviewSubmitPopup() {
        return reviewSubmitPopup;
    }

    /**
     * @param crsStateSOC
     */
    public void setCrsStateSOC(RichSelectOneChoice crsStateSOC) {
        this.crsStateSOC = crsStateSOC;
    }

    /**
     * @return
     */
    public RichSelectOneChoice getCrsStateSOC() {
        return crsStateSOC;
    }

    /**
     * @param crsStatusSOC
     */
    public void setCrsStatusSOC(RichSelectOneChoice crsStatusSOC) {
        this.crsStatusSOC = crsStatusSOC;
    }

    /**
     * @return
     */
    public RichSelectOneChoice getCrsStatusSOC() {
        return crsStatusSOC;
    }
    
    /** DRAFT to REVIEW */
     public void processReviewDialog(DialogEvent dialogEvent) {
         logger.info("--------processing Review action---------");
         if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome()))
            processStateChange(ModelConstants.STATE_REVIEW, getReviewSubmitPopup());

    }
       
    /** REVIEW to REVIEWED */
     public void processReviewedDialog(DialogEvent dialogEvent) {
         logger.info("--------processing Reviewed action---------");
         if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome()))
             processStateChange(ModelConstants.STATE_REVIEWED, getCrsReviewedPopup());    
     }
       
    /** REVIEWED to TASL APPROVE */
     public void processTaslReviewSubmit(DialogEvent dialogEvent) {
         logger.info("--------processing TaslReviewSubmit action---------");
         if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome()))
             processStateChange(ModelConstants.STATE_TASLAPPROVE, getSubmitApprovalPopup());
     }
       
    /** TASL APPROVE to ML APPROVE */ 
     public void processTaslApprove(DialogEvent dialogEvent) {
         logger.info("--------processing TaslApprove action---------");
         if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome())) 
             processStateChange(ModelConstants.STATE_MLAPPROVE, getCrsApprovePopup());
     }
      
    /** TASL APPROVE to DRAFT */ 
     public void processTaslReject(DialogEvent dialogEvent) {
        logger.info("--------processing TaslReject action---------");
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
         logger.info("--------processing MLApprove action---------");
         if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome()))
             processStateChange(ModelConstants.STATE_APPROVED, getCrsApprovePopup());
     }
       
    /** ML APPROVE to DRAFT */ 
     public void processMLReject(DialogEvent dialogEvent) {
         logger.info("--------processing MLReject action---------");
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
         logger.info("--------processing DemoteToDraft action---------");
         if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome()))
             processStateChange(ModelConstants.STATE_DRAFT, getCrsDemoteDraftPopupBinding());
    }

    /** APPROVED to PUBLISHED */ 
     public void processPublishDialog(DialogEvent dialogEvent) {
         logger.info("--------processing Publish action---------");
        if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome())) {
            OperationBinding op = ADFUtils.findOperation("activateCrs");
            Map params = op.getParamsMap();
            params.put("pCRSId", ADFUtils.evaluateEL("#{bindings.CrsId.inputValue}"));
            params.put("pReasonForChange", getReasonForChange());
            String msg = (String)op.execute();

            if (op.getErrors() != null && op.getErrors().size() > 0) {
                ADFUtils.showFacesMessage(uiBundle.getString("INTERNAL_ERROR"),
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
        logger.info("--------processing StateChange ---------");
        ADFUtils.setEL("#{bindings.StateId.inputValue}", newState);

        OperationBinding oper = ADFUtils.findOperation("Commit");
        oper.execute();
        if (oper.getErrors().size() > 0)
            ADFUtils.showFacesMessage(uiBundle.getString("INTERNAL_ERROR"), FacesMessage.SEVERITY_ERROR);
        else {
            ADFUtils.showPopup(infoPopup);
            ADFUtils.addPartialTarget(getCrsStateSOC());
            ADFUtils.addPartialTarget(getWorkflowPanelBox());
        }
    }

    /**
     * @param crsApprovePopup
     */
    public void setCrsApprovePopup(RichPopup crsApprovePopup) {
        this.crsApprovePopup = crsApprovePopup;
    }

    /**
     * @return
     */
    public RichPopup getCrsApprovePopup() {
        return crsApprovePopup;
    }

    /**
     * @param workflowPanelBox
     */
    public void setWorkflowPanelBox(RichPanelBox workflowPanelBox) {
        this.workflowPanelBox = workflowPanelBox;
    }

    /**
     * @return
     */
    public RichPanelBox getWorkflowPanelBox() {
        return workflowPanelBox;
    }

    /**
     * @param crsRejectPopup
     */
    public void setCrsRejectPopup(RichPopup crsRejectPopup) {
        this.crsRejectPopup = crsRejectPopup;
    }

    /**
     * @return
     */
    public RichPopup getCrsRejectPopup() {
        return crsRejectPopup;
    }

    /**
     * @param taslCommentsInputText
     */
    public void setTaslCommentsInputText(RichInputText taslCommentsInputText) {
        this.taslCommentsInputText = taslCommentsInputText;
    }

    /**
     * @return
     */
    public RichInputText getTaslCommentsInputText() {
        return taslCommentsInputText;
    }

    /**
     * @param mlCommentsInputText
     */
    public void setMlCommentsInputText(RichInputText mlCommentsInputText) {
        this.mlCommentsInputText = mlCommentsInputText;
    }

    /**
     * @return
     */
    public RichInputText getMlCommentsInputText() {
        return mlCommentsInputText;
    }

    /**
     * @param dictionary
     */
    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * @return
     */
    public String getDictionary() {
        return dictionary;
    }

    /**
     * @param level
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * @return
     */
    public String getLevel() {
        return level;
    }

    /**
     * @param term
     */
    public void setTerm(String term) {
        this.term = term;
    }

    /**
     * @return
     */
    public String getTerm() {
        return term;
    }

    public void searchHierarchy(ActionEvent actionEvent) {
        logger.info("Querying Hierarchy search");
        DCIteratorBinding iter = ADFUtils.findIterator("HierarchySearchVOIterator");
        ViewObject hierVO = iter.getViewObject();
        logger.info("Entered search criteria : term : "+term+" level : "+level+" dictionary : "+dictionary);
        hierVO.setNamedWhereClauseParam("pTerm", term != null ? term : null);
        hierVO.setNamedWhereClauseParam("pLevel", level != null ? level : null);
        hierVO.setNamedWhereClauseParam("pDict", dictionary != null ? dictionary : null);
        hierVO.executeQuery();
    }

    public void onClickHierarchySearch(ActionEvent actionEvent) {
        clickHierarchy();
        logger.info("Opening the blank hierarchy popup, aligning to the right of risk definition popup.");
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        hints.add(RichPopup.PopupHints.HintTypes.HINT_ALIGN_ID, this.getHiddenPopupAlign());
        hints.add(RichPopup.PopupHints.HintTypes.HINT_ALIGN, RichPopup.PopupHints.AlignTypes.ALIGN_END_BEFORE);
        hierPopup.show(hints);
//        ADFUtils.showPopup(hierPopup);
    }
    
    public void onClickCopyHierarchySearch(ActionEvent actionEvent) {
        clickHierarchy();
        logger.info("Opening the blank hierarchy popup, aligning to the right of copy risk definition popup.");
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        hints.add(RichPopup.PopupHints.HintTypes.HINT_ALIGN_ID, this.getCopyRiskDefTable());
        hints.add(RichPopup.PopupHints.HintTypes.HINT_ALIGN, RichPopup.PopupHints.AlignTypes.ALIGN_END_AFTER);
        hierPopup.show(hints);
    }
    
    private void clickHierarchy(){
        logger.info("-------clickHierarchy ---------");
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

    /**
     * @param hierPopup
     */
    public void setHierPopup(RichPopup hierPopup) {
        this.hierPopup = hierPopup;
    }

    /**
     * @return
     */
    public RichPopup getHierPopup() {
        return hierPopup;
    }

    public DnDAction dragDropListener(DropEvent dropEvent) {
        logger.info("Performed drag and drop.");
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
                logger.info("Dragged child hierarchy row. (second level tree table)");
                HierarchyChildUIBean selRow = (HierarchyChildUIBean)dragTable.getRowData();
                if(selRow.getLevelName().equals(new Long(0))){
                    logger.info("Dragged parent hierarchy row in tree. Diallowed");
                    ADFUtils.showPopup(parentError);
                    dragTable.setRowKey(dragCurrentRowKey);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(dragTable);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(dropTable);
                    return DnDAction.NONE;
                }
                else{
                    
                    Row filterRow[] = riskDefVO.getFilteredRows("TmsDictContentId", selRow.getTmsDictContentId());
                    if(filterRow.length > 0){
                        ADFUtils.showFacesMessage(uiBundle.getString("TERM_UNIQUE_ERROR"), FacesMessage.SEVERITY_ERROR);
                        dragTable.setRowKey(dragCurrentRowKey);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(dragTable);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(dropTable);
                        return DnDAction.NONE;
                    }
                    
                    String version = selRow.getDictContentAltCode();
                    String dict = selRow.getDictShortName();
                    if (dict != null && ViewConstants.MEDDRA_DICTIONARY.equalsIgnoreCase(dict)) {
                        Row rows[] = riskDefVO.getFilteredRows("MeddraDict", ViewConstants.MEDDRA_DICTIONARY);
                        if (rows.length > 0) {
                            ADFUtils.showPopup(meddraError);
                            dragTable.setRowKey(dragCurrentRowKey);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(dragTable);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(dropTable);
                            return DnDAction.NONE;
                        }
                        Row rows1[] = riskDefVO.getFilteredRows("MeddraDict", ViewConstants.FILTER_DICTIONARY);
                        if (rows1.length > 0) {
                            ADFUtils.showFacesMessage(uiBundle.getString("MEDDRA_FILTER_COMBO_ERROR"), FacesMessage.SEVERITY_ERROR);
                            dragTable.setRowKey(dragCurrentRowKey);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(dragTable);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(dropTable);
                            return DnDAction.NONE;
                        }
                    }
                    else if(dict != null && ViewConstants.FILTER_DICTIONARY.equalsIgnoreCase(dict)){
                        Row rows[] = riskDefVO.getFilteredRows("MeddraDict", ViewConstants.MEDDRA_DICTIONARY);
                        if (rows.length > 0) {
                            ADFUtils.showFacesMessage(uiBundle.getString("MEDDRA_FILTER_COMBO_ERROR"), FacesMessage.SEVERITY_ERROR);
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

                    if (dict != null && ViewConstants.FILTER_DICTIONARY.equalsIgnoreCase(dict)) {
                        if (selRow.getTerm() != null && selRow.getTerm().contains(ViewConstants.NMQ))
                            riskDefRow.setAttribute("MeddraExtension", ViewConstants.NMQ);
                        else if (selRow.getTerm() != null && selRow.getTerm().contains(ViewConstants.CMQ))
                            riskDefRow.setAttribute("MeddraExtension", ViewConstants.CMQ);
                        else if (selRow.getTerm() != null && selRow.getTerm().contains(ViewConstants.SMQ))
                            riskDefRow.setAttribute("MeddraExtension", ViewConstants.SMQ);
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
                logger.info("Dragged hierarchy row from 1st table");
                JUCtrlHierNodeBinding rowBinding = (JUCtrlHierNodeBinding)dragTable.getRowData();
                dragRow = rowBinding.getRow();
                dragNodeVO = dragRow.getStructureDef().getDefName();
                if ("HierarchySearchVO".equalsIgnoreCase(dragNodeVO)) {
                    
                    Row filterRow[] = riskDefVO.getFilteredRows("TmsDictContentId", dragRow.getAttribute("DictContentId"));
                    if(filterRow.length > 0){
                        ADFUtils.showFacesMessage(uiBundle.getString("TERM_UNIQUE_ERROR"), FacesMessage.SEVERITY_ERROR);
                        dragTable.setRowKey(dragCurrentRowKey);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(dragTable);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(dropTable);
                        return DnDAction.NONE;
                    }
                    
                    String term = (String)dragRow.getAttribute("Mqterm");
                    String code = (String)dragRow.getAttribute("Mqcode");
                    String level = (String)dragRow.getAttribute("Mqlevel");
                    String qual = (String)dragRow.getAttribute("Mqcrtev");
                    String dict = (String)dragRow.getAttribute("DictNm");
                    String version = (String)dragRow.getAttribute("DictVersion");

                    if (dict != null && ViewConstants.MEDDRA_DICTIONARY.equalsIgnoreCase(dict)) {
                        Row rows[] = riskDefVO.getFilteredRows("MeddraDict", ViewConstants.MEDDRA_DICTIONARY);
                        if (rows.length > 0) {
                            logger.info("Dragged another meddra row, disallowed");
                            ADFUtils.showPopup(meddraError);
                            dragTable.setRowKey(dragCurrentRowKey);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(dragTable);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(dropTable);
                            return DnDAction.NONE;
                        }
                        Row rows1[] = riskDefVO.getFilteredRows("MeddraDict", ViewConstants.FILTER_DICTIONARY);
                        if (rows1.length > 0) {
                            ADFUtils.showFacesMessage(uiBundle.getString("MEDDRA_FILTER_COMBO_ERROR"), FacesMessage.SEVERITY_ERROR);
                            dragTable.setRowKey(dragCurrentRowKey);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(dragTable);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(dropTable);
                            return DnDAction.NONE;
                        }
                    }
                    else if(dict != null && ViewConstants.FILTER_DICTIONARY.equalsIgnoreCase(dict)){
                        Row rows[] = riskDefVO.getFilteredRows("MeddraDict", ViewConstants.MEDDRA_DICTIONARY);
                        if (rows.length > 0) {
                            ADFUtils.showFacesMessage(uiBundle.getString("MEDDRA_FILTER_COMBO_ERROR"), FacesMessage.SEVERITY_ERROR);
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
                    if (dict != null && ViewConstants.FILTER_DICTIONARY.equalsIgnoreCase(dict)) {
                        if (term != null && term.contains(ViewConstants.NMQ))
                            riskDefRow.setAttribute("MeddraExtension", ViewConstants.NMQ);
                        else if (term != null && term.contains(ViewConstants.CMQ))
                            riskDefRow.setAttribute("MeddraExtension", ViewConstants.CMQ);
                        else if (term != null && term.contains(ViewConstants.SMQ))
                            riskDefRow.setAttribute("MeddraExtension", ViewConstants.SMQ);
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


    /**
     * @param crsFieldsUpdatable
     */
    public void setCrsFieldsUpdatable(boolean crsFieldsUpdatable) {
        this.crsFieldsUpdatable = crsFieldsUpdatable;
    }

    /**
     * @return
     */
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
            logger.info("--Entering : isCrsFieldsUpdatable: BSL Loggedin block-------------");
            if (ModelConstants.STATUS_CURRENT.equals(crsStatus)) {


            } else if (ModelConstants.STATUS_PENDING.equals(crsStatus)) {
                
                if(ModelConstants.STATE_DRAFT.equals(crsState) ||
                    ModelConstants.STATE_REVIEWED.equals(crsState) ||
                    ModelConstants.STATE_APPROVED.equals(crsState) ||
                    ModelConstants.STATE_PUBLISHED.equals(crsState) )
                    isCrsFieldsUpdatable = true;                                
            }
            logger.info("--End : isCrsFieldsUpdatable:  BSL Loggedin block-------------");
        }
        
        // ADMIN LOGIN - admin can update any CRS in any state
       else if (ADFContext.getCurrent().getSecurityContext().isUserInRole(ModelConstants.ROLE_CRSADMIN))
            isCrsFieldsUpdatable = true;
        
        logger.info("--isCrsFieldsUpdatable --->"+isCrsFieldsUpdatable);
        return isCrsFieldsUpdatable;
    }

    public void processDeleteDialog(DialogEvent dialogEvent) {
        logger.info("Showing delete confirmation popup.");
        if(DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome())){
            OperationBinding oper = ADFUtils.findOperation("deleteCrs");
            oper.execute();
            if (oper.getErrors().size() > 0)
                ADFUtils.showFacesMessage(uiBundle.getString("INTERNAL_ERROR"), FacesMessage.SEVERITY_ERROR);
            else{
                String returnValue = (String)ADFUtils.invokeEL("#{controllerContext.currentViewPort.taskFlowContext.trainModel.getPrevious}");
                if(returnValue == null){
                    ADFUtils.navigateToControlFlowCase("home");
                }else{
                    ADFUtils.navigateToControlFlowCase(returnValue);
                }
            }
        }
    }


    /**
     * @param crsRetirePopup
     */
    public void setCrsRetirePopup(RichPopup crsRetirePopup) {
        this.crsRetirePopup = crsRetirePopup;
    }

    /**
     * @return
     */
    public RichPopup getCrsRetirePopup() {
        return crsRetirePopup;
    }

    /**
     * @param crsReactivatePopup
     */
    public void setCrsReactivatePopup(RichPopup crsReactivatePopup) {
        this.crsReactivatePopup = crsReactivatePopup;
    }

    /**
     * @return
     */
    public RichPopup getCrsReactivatePopup() {
        return crsReactivatePopup;
    }

    /**
     * @param crsReviewedPopup
     */
    public void setCrsReviewedPopup(RichPopup crsReviewedPopup) {
        this.crsReviewedPopup = crsReviewedPopup;
    }

    /**
     * @return
     */
    public RichPopup getCrsReviewedPopup() {
        return crsReviewedPopup;
    }

    /**
     * @param meddraError
     */
    public void setMeddraError(RichPopup meddraError) {
        this.meddraError = meddraError;
    }

    /**
     * @return
     */
    public RichPopup getMeddraError() {
        return meddraError;
    }

    /**
     * This method is used to frame the crs name with the
     * selected compound code and indication
     * @param vce
     */
    public void onChangeIndication(ValueChangeEvent vce) {
        if (vce != null) {
            logger.info("-- onChangeIndication : append commpound and indication--->");
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
        logger.info("Start of CRSReportsBean:onAdminReportItmes()");
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
            int rowStartIndex = 14;
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
            columnMap.put("NonMeddraComponentComment", rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskRelationVO.NonMeddraComponentComment_LABEL"));
            //BSL, LoggedinUser in Designee, Admin,MQM
            String bsl = "";
            String designee = "";
            if (ModelConstants.BASE_FACET.equals(getBaseOrStaging())) {
                bsl =
(String)ADFUtils.evaluateEL("#{bindings.BslNameBase.inputValue}");

            } else
                bsl =
(String)ADFUtils.evaluateEL("#{bindings.BslName.inputValue}");

            if (ModelConstants.BASE_FACET.equals(getBaseOrStaging())) {
                designee =
                        (String)ADFUtils.evaluateEL("#{bindings.DesigneeBase.inputValue}");
            } else
                designee =
                        (String)ADFUtils.evaluateEL("#{bindings.Designee.inputValue}");

            if (getUserName() != null && getUserName().equals(bsl) ||
                ViewConstants.isNotEmpty(designee) &&
                designee.contains(getUserName()) ||
                ModelConstants.ROLE_CRSADMIN.equals(loggedInUserRole) ||
                ModelConstants.ROLE_MQM.equals(loggedInUserRole)) {
                columnMap.put("MqmComment",
                              rsBundle.getString("com.novartis.ecrs.model.view.CrsRiskVO.MqmComment_LABEL"));
            }
            workbook.setMissingCellPolicy(org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK);
            Sheet sheet = workbook.getSheetAt(0);
            writeHeaderData(sheet,0,4,7,10);
            ExcelExportUtils.writeExcelSheet(sheet, rowSet, rowStartIndex,
                                             cellStartIndex, columnMap, null,
                                             dateCellFormat, emptyValReplace,imageInputStream);
            //write image to sheet
            //ExcelExportUtils.writeImageTOExcel(sheet,imageInputStream);
            
        } catch (InvalidFormatException invalidFormatException) {
            logger.error("Exception occured in onAdminReportItmes()"+invalidFormatException);
        } catch (IOException ioe) {
            logger.error("Exception occured in onAdminReportItmes()"+ioe);
        } catch (Exception e) {
            logger.error("Exception occured in onAdminReportItmes()"+e);
        } finally {
            workbook.write(outputStream);
            excelInputStream.close();
            outputStream.close(); 
        }
        logger.info("End of CRSReportsBean:onAdminReportItmes()");
    }


    /**
     * @param delConfPopupBinding
     */
    public void setDelConfPopupBinding(RichPopup delConfPopupBinding) {
        this.delConfPopupBinding = delConfPopupBinding;
    }

    /**
     * @return
     */
    public RichPopup getDelConfPopupBinding() {
        return delConfPopupBinding;
    }

    /**
     * @param publishPopupBinding
     */
    public void setCrsPublishPopupBinding(RichPopup publishPopupBinding) {
        this.crsPublishPopupBinding = publishPopupBinding;
    }

    /**
     * @return
     */
    public RichPopup getCrsPublishPopupBinding() {
        return crsPublishPopupBinding;
    }

    /**
     * @param crsDemoteDraftPopupBinding
     */
    public void setCrsDemoteDraftPopupBinding(RichPopup crsDemoteDraftPopupBinding) {
        this.crsDemoteDraftPopupBinding = crsDemoteDraftPopupBinding;
    }

    /**
     * @return
     */
    public RichPopup getCrsDemoteDraftPopupBinding() {
        return crsDemoteDraftPopupBinding;
    }

    /**
     * @param filterItems
     */
    public void setFilterItems(List<SelectItem> filterItems) {
        this.filterItems = filterItems;
    }

    /**
     * @return
     */
    public List<SelectItem> getFilterItems() {
        logger.info("Getting list of values for Filter in hierarchy search.");
        if(filterItems == null){
            filterItems = new ArrayList<SelectItem>();
            SelectItem item1 = new SelectItem(ViewConstants.MQ1, ViewConstants.SMQ1);
            SelectItem item2 = new SelectItem(ViewConstants.MQ2, ViewConstants.SMQ2);
            SelectItem item3 = new SelectItem(ViewConstants.MQ3, ViewConstants.SMQ3);
            SelectItem item4 = new SelectItem(ViewConstants.MQ4, ViewConstants.SMQ4);
            SelectItem item5 = new SelectItem(ViewConstants.MQ5, ViewConstants.SMQ5);
            SelectItem item6 = new SelectItem(ViewConstants.NMQ1, ViewConstants.CUSTOM1);
            SelectItem item7 = new SelectItem(ViewConstants.NMQ2, ViewConstants.CUSTOM2);
            SelectItem item8 = new SelectItem(ViewConstants.NMQ3, ViewConstants.CUSTOM3);
            SelectItem item9 = new SelectItem(ViewConstants.NMQ4, ViewConstants.CUSTOM4);
            SelectItem item10 = new SelectItem(ViewConstants.NMQ5, ViewConstants.CUSTOM5);
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

    /**
     * @param meddraItems
     */
    public void setMeddraItems(List<SelectItem> meddraItems) {
        this.meddraItems = meddraItems;
    }

    /**
     * @return
     */
    public List<SelectItem> getMeddraItems() {
        logger.info("Fetching list of values for MEDDRA LOV in hierarchy search");
        if(meddraItems == null){
            meddraItems = new ArrayList<SelectItem>();
            SelectItem item1 = new SelectItem(ViewConstants.SOC, ViewConstants.SOC);
            SelectItem item2 = new SelectItem(ViewConstants.HLGT, ViewConstants.HLGT);
            SelectItem item3 = new SelectItem(ViewConstants.HLT, ViewConstants.HLT);
            SelectItem item4 = new SelectItem(ViewConstants.PT, ViewConstants.PT);
            meddraItems.add(item1);
            meddraItems.add(item2);
            meddraItems.add(item3);
            meddraItems.add(item4);
        }
        return meddraItems;
    }

    public void dictionaryVC(ValueChangeEvent valueChangeEvent) {
        logger.info("Refreshing Level LOV based on the dictionary selected");
        if(valueChangeEvent.getNewValue() != null && valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()){
            if(ViewConstants.MEDDRA_DICTIONARY.equalsIgnoreCase((String)valueChangeEvent.getNewValue())){
                setLevelItems(getMeddraItems());
            }else{
                setLevelItems(getFilterItems());
            }
        }
    }

    /**
     * @param levelItems
     */
    public void setLevelItems(List<SelectItem> levelItems) {
        this.levelItems = levelItems;
    }

    /**
     * @return
     */
    public List<SelectItem> getLevelItems() {
        return levelItems;
    }

    /**
     * @param actionEvent
     */
    public void onCancelCrsRiskPopup(ActionEvent actionEvent) {
        logger.info("Closing CrsRisk Popup, rolling back any unsaved changes.");
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
        logger.info("CrsId : "+crsId+" state: "+getBaseOrStaging());
        oper1.execute();
        if (oper1.getErrors().size() > 0) 
            ADFUtils.showFacesMessage(uiBundle.getString("INTERNAL_ERROR"), FacesMessage.SEVERITY_ERROR);
        if(riskDefTable != null)
            ADFUtils.addPartialTarget(riskDefTable);
        if(riskDefPopup != null)
            riskDefPopup.hide();
        if(copyPopup != null)
            copyPopup.hide();
    }

    /**
     * @param contentId
     */
    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    /**
     * @return
     */
    public String getContentId() {
        return contentId;
    }

    /**
     * @param childScope
     */
    public void setChildScope(String childScope) {
        this.childScope = childScope;
    }

    /**
     * @return
     */
    public String getChildScope() {
        return childScope;
    }

    /**
     * @param actionEvent
     */
    public void executeHierarchyChild(ActionEvent actionEvent) {
        DCIteratorBinding childIter = ADFUtils.findIterator("HierarchyChildVOIterator");
        ViewObject childVO = childIter.getViewObject();
        logger.info("Executing hierachy child for selected content ID");
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

    /**
     * @param childTreeTable
     */
    public void setChildTreeTable(RichTreeTable childTreeTable) {
        this.childTreeTable = childTreeTable;
    }

    /**
     * @return
     */
    public RichTreeTable getChildTreeTable() {
        return childTreeTable;
    }

    /**
     * @param parentError
     */
    public void setParentError(RichPopup parentError) {
        this.parentError = parentError;
    }

    /**
     * @return
     */
    public RichPopup getParentError() {
        return parentError;
    }
    
    public void searchCrs(ActionEvent actionEvent) {
        String stoi = getSafetyTopicOfInterest();
        logger.info("Searching exisitng current safety topic of interests with entered value : "+stoi);
        DCIteratorBinding iter = ADFUtils.findIterator("CopyCrsRiskVOIterator");
        ViewObject crsSearchVO = iter.getViewObject();
        crsSearchVO.setWhereClause("SAFETY_TOPIC_OF_INTEREST like '"+stoi+"'");
        System.err.println(crsSearchVO.getQuery());
        crsSearchVO.executeQuery();
    }

    /**
     * @param safetyTopicOfInterest
     */
    public void setSafetyTopicOfInterest(String safetyTopicOfInterest) {
        this.safetyTopicOfInterest = safetyTopicOfInterest;
    }

    /**
     * @return
     */
    public String getSafetyTopicOfInterest() {
        return safetyTopicOfInterest;
    }

    /**
     * @param actionEvent
     */
    public void copyCrsRiskRelation(ActionEvent actionEvent) {
        
        Long crsId = (Long)ADFUtils.getPageFlowScopeValue("crsId");  
        String riskPurposeList = (String)ADFUtils.evaluateEL("#{copyRow.RiskPurposeList}");
        String safetyTopic = (String)ADFUtils.evaluateEL("#{copyRow.SafetyTopicOfInterest}");
        Map params1 = new HashMap<String, Object>();
        logger.info("Validating it this CRS "+crsId+" has this safety topic already.");
        params1.put("crsId", crsId);
        params1.put("safetyTopic", safetyTopic);
        params1.put("rpList", riskPurposeList);
//        params1.put("crsRiskId", ADFUtils.evaluateEL("#{bindings.CrsRiskId.inputValue}"));
        try {
            logger.info("Calling model method validateSafetyTopic");
            Boolean invalid = (Boolean)ADFUtils.executeAction("validateSafetyTopic", params1);
            if(invalid){
                ADFUtils.showFacesMessage(uiBundle.getString("STOI_UNIQUE_ERROR"), FacesMessage.SEVERITY_ERROR);
                return;
            }
        } catch (Exception e) {
            logger.error("Exception occured in validateSafetyTopic()"+e);
        }
        
//        DCBindingContainer dcbind =(DCBindingContainer)getBindings();
//        Boolean dirty = dcbind.getDataControl().isTransactionModified();
//        if(dirty){
//            ADFUtils.showPopup(pendingPopup);
//            return;
//        }
        
        ADFUtils.setPageFlowScopeValue("popupMode", "Edit");
        Long riskId = (Long)ADFUtils.evaluateEL("#{copyRow.CrsRiskId}");
        
//        String databaseList = (String)ADFUtils.evaluateEL("#{copyRow.DatabaseList}");
//        List<String> dbList = new ArrayList<String>();
//        if(databaseList != null){
//            String split[] = databaseList.split(",");
//            for(String db : split){
//                dbList.add(db.trim());
//            }
//        }
//        setSelDatabases(dbList);       
        logger.info("Copying crsRiskRelations from crsId : "+crsId+" riskID : "+riskId);
        
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
        logger.info("Selected risk purposes "+rpList);
//        if(copyDBListBinding != null)
//            ResetUtils.reset(copyDBListBinding);
        if(copyRPListBinding != null)
            ResetUtils.reset(copyRPListBinding);
        Map params = new HashMap<String, Object>();
        logger.info("Copying from source risk Id : "+riskId+ " destination crsId "+crsId);
        params.put("srcRiskId", riskId);
        params.put("destCrsId", crsId);
        try {
            logger.info("Calling model method copyCurrentRiskRelation");
            ADFUtils.executeAction("copyCurrentRiskRelation", params);
        } catch (Exception e) {
            logger.error("Exception occured in copyCrsRiskRelation()"+e);
        }
        copyPanel.setVisible(true);
        ADFUtils.addPartialTarget(copyPanel);
    }

    /**
     * @return
     */
    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    /**
     * @param copyPopup
     */
    public void setCopyPopup(RichPopup copyPopup) {
        this.copyPopup = copyPopup;
    }

    /**
     * @return
     */
    public RichPopup getCopyPopup() {
        return copyPopup;
    }

    /**
     * @param copyPanel
     */
    public void setCopyPanel(RichPanelGroupLayout copyPanel) {
        this.copyPanel = copyPanel;
    }

    /**
     * @return
     */
    public RichPanelGroupLayout getCopyPanel() {
        return copyPanel;
    }

    /**
     * @param pendingPopup
     */
    public void setPendingPopup(RichPopup pendingPopup) {
        this.pendingPopup = pendingPopup;
    }

    /**
     * @return
     */
    public RichPopup getPendingPopup() {
        return pendingPopup;
    }

    /**
     * @param actionEvent
     */
    public void onClickYes(ActionEvent actionEvent) {
        logger.info("On click yes on the pending changes popup, Rolling back");
        OperationBinding oper = ADFUtils.findOperation("Rollback");
        oper.execute();
        if (oper.getErrors().size() > 0) 
            ADFUtils.showFacesMessage(uiBundle.getString("INTERNAL_ERROR"), FacesMessage.SEVERITY_ERROR);
         else
            copyCrsRiskRelation(actionEvent);
    }

    /**
     * @param actionEvent
     */
    public void onClickCopy(ActionEvent actionEvent) {
        logger.info("Opening the blank copy Risk Defintions popup");
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

    /**
     * @param actionEvent
     */
    public void deleteCopiedRiskDefs(ActionEvent actionEvent) {
        logger.info("Delete selected risk definitions in copy popup.");
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

    /**
     * @param stoiBinding
     */
    public void setStoiBinding(RichInputText stoiBinding) {
        this.stoiBinding = stoiBinding;
    }

    /**
     * @return
     */
    public RichInputText getStoiBinding() {
        return stoiBinding;
    }

    /**
     * @param copyDBListBinding
     */
    public void setCopyDBListBinding(RichSelectManyChoice copyDBListBinding) {
        this.copyDBListBinding = copyDBListBinding;
    }

    /**
     * @return
     */
    public RichSelectManyChoice getCopyDBListBinding() {
        return copyDBListBinding;
    }

    /**
     * @param copyRPListBinding
     */
    public void setCopyRPListBinding(RichSelectManyChoice copyRPListBinding) {
        this.copyRPListBinding = copyRPListBinding;
    }

    /**
     * @return
     */
    public RichSelectManyChoice getCopyRPListBinding() {
        return copyRPListBinding;
    }

    /**
     * @param searchSwitherBinding
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
        logger.info("Start- ManageCRSBean:baseContentVOSelectionListener--");
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
        logger.info("End- ManageCRSBean:baseContentVOSelectionListener--");
    }

    public void refreshRepository(ActionEvent actionEvent) {
        Map params = new HashMap<String, Object>();
        params.put("crsId", ADFUtils.getPageFlowScopeValue("crsId"));
        logger.info("Executing refreshRepository function call for crsID :: "+ADFUtils.getPageFlowScopeValue("crsId"));
        try {
            ADFUtils.executeAction("refreshRepository", params);
            setRepoRefreshed(Boolean.TRUE);
            ADFUtils.addPartialTarget(riskDefTable);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    /**
     * @param repoRefreshed
     */
    public void setRepoRefreshed(Boolean repoRefreshed) {
        this.repoRefreshed = repoRefreshed;
    }

    /**
     * @return
     */
    public Boolean getRepoRefreshed() {
        return repoRefreshed;
    }


    /**
     * @return
     */
    public String initializeCreateUpdateCRS() {
        // chk if there exists a CRS in staging table with the same as selected CRS in base table
        if (!ViewConstants.ANONYMOUS_ROLE.equalsIgnoreCase(userName) &&
            ModelConstants.BASE_FACET.equals(getBaseOrStaging()) &&
            ViewConstants.FLOW_TYPE_UPDATE.equals(getFlowType())) {
            setReasonForChange(null);
            ADFUtils.showPopup(getModifyReasonChngPopup());
            return "toSearch";
        } else
            return "createUpdateCRS";
    }

    /**
     * @param baseOrStaging
     */
    public void setBaseOrStaging(String baseOrStaging) {
        this.baseOrStaging = baseOrStaging;
    }

    /**
     * @return
     */
    public String getBaseOrStaging() {
        return baseOrStaging;
    }

    /**
     * @param hierChildTreeModel
     */
    public void setHierChildTreeModel(ChildPropertyTreeModel hierChildTreeModel) {
        this.hierChildTreeModel = hierChildTreeModel;
    }

    /**
     * @return
     */
    public ChildPropertyTreeModel getHierChildTreeModel() {
        return hierChildTreeModel;
    }

    /**
     * @param hierChildList
     */
    public void setHierChildList(List<HierarchyChildUIBean> hierChildList) {
        this.hierChildList = hierChildList;
    }

    /**
     * @return
     */
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

    /**
     * @param reasonChangePopup
     */
    public void setReasonChangePopup(RichDialog reasonChangePopup) {
        this.reasonChangePopup = reasonChangePopup;
    }

    /**
     * @return
     */
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
            logger.info("Start- ManageCRSBean:retireConfirmDialogListener--");
            CrsContentBaseVORowImpl row =
                (CrsContentBaseVORowImpl)ADFUtils.evaluateEL("#{bindings.CrsContentBaseVOIterator.currentRow}");
            if (row.getCrsId() != null) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("pCRSId", row.getCrsId());
                params.put("pReasonForChange", getReasonForChange());
                try {
                    String msg =
                        (String)ADFUtils.executeAction("retireCrs", params);
                    logger.info("Model result message--"+msg);
                    if (!ModelConstants.PLSQL_CALL_SUCCESS.equals(msg)) {
                        ADFUtils.setEL("#{pageFlowScope.plsqlerror}", msg);
                        ADFUtils.showPopup(getErrorPLSqlPopup());
                        return;
                    }
                    onClickSearch(new ActionEvent((UIComponent)dialogEvent.getSource()));
                    getSearchBaseTableBinding().resetStampState();
                    ADFUtils.addPartialTarget(getSearchBaseTableBinding());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            logger.info("End- ManageCRSBean:retireConfirmDialogListener--");
        }
    }

    /**
     * @param dialogEvent
     */
    public void reactivateConfirmDialogListener(DialogEvent dialogEvent) {
        // Add event code here...
        if (DialogEvent.Outcome.yes.equals(dialogEvent.getOutcome())) {
            logger.info("Start- ManageCRSBean:reactivateConfirmDialogListener--");
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
            logger.info("End- ManageCRSBean:reactivateConfirmDialogListener--");
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
        logger.info("Start- ManageCRSBean:onClickModifyCrs--");
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
            logger.info("isCrsFound --"+isCrsFound);
            if (op.getErrors() != null && op.getErrors().size() > 0) {
                logger.info("Error occured from findByCrsFromStg,Navigating to search page --");
                ADFUtils.showFacesMessage(uiBundle.getString("INTERNAL_ERROR"),
                                          FacesMessage.SEVERITY_ERROR);
                setBaseOrStaging(ModelConstants.BASE_FACET);
                ADFUtils.closeDialog(getModifyReasonChngPopup());
                return "navToSearch";
            } else {
                logger.info("If isCrsFound true Navigating to search page ---");
                // if found - show faces message that the CRS already in update process
                if (isCrsFound) {
                    ADFUtils.showFacesMessage("The selected CRS is already in update process",
                                              FacesMessage.SEVERITY_INFO);
                    setBaseOrStaging(ModelConstants.BASE_FACET);
                    ADFUtils.closeDialog(getModifyReasonChngPopup());
                    return "navToSearch";
                } else {
                    logger.info("If isCrsFound false invoke modifyCrs ---");
                    // if NOT found - call MODIDY_CRS
                    String resultMsg = null;
                    op =
 ADFUtils.getOperBindFromPageDef(ViewConstants.PAGE_DEF_SEARCH, "modifyCrs");
                    op.getParamsMap().put("pCRSId", crsId);
                    op.getParamsMap().put("pReasonForChange", getReasonForChange());
                    resultMsg = (String)op.execute();

                    if (op.getErrors() != null && op.getErrors().size() > 0) {
                        logger.info("If error from invoke modifyCrs,navigate to search page ---");
                        ADFUtils.setEL("#{pageFlowScope.plsqlerror}", resultMsg);
                        ADFUtils.showPopup(getErrorPLSqlPopup());
                        setBaseOrStaging(ModelConstants.BASE_FACET);
                        ADFUtils.closeDialog(getModifyReasonChngPopup());
                        return "navToSearch";
                    } else {
                        logger.info("If succes from modifyCrs,set crsid to current row ---");
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
                                ADFUtils.showFacesMessage(uiBundle.getString("INTERNAL_ERROR"),
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
            logger.info(" current row null ,navigating to search page ---");
            ADFUtils.showFacesMessage(uiBundle.getString("NAV_ERROR"),
                                      FacesMessage.SEVERITY_INFO);
            setBaseOrStaging(ModelConstants.BASE_FACET);
            ADFUtils.closeDialog(getModifyReasonChngPopup());
            return "navToSearch";
        }
        //default nav to createUpdate
        //return "createUpdate";
    }

    /**
     * @return
     */
    public String cancelModifyCrs() {
        // Add event code here...
        ADFUtils.closeDialog(getModifyReasonChngPopup());
        return "navToSearch";
    }

    /**
     * @param actionEvent
     */
    public void refreshRepoInPopup(ActionEvent actionEvent) {
        logger.info(" Start-ManageCRSBean :refreshRepoInPopup ---");
        Map params = new HashMap<String, Object>();
        params.put("crsId", ADFUtils.getPageFlowScopeValue("crsId"));
        logger.info("Executing refreshRepository function call for crsId :: "+ADFUtils.getPageFlowScopeValue("crsId"));
        try {
            ADFUtils.executeAction("refreshRepository", params);
            setRepoRefreshed(Boolean.TRUE);
            ADFUtils.addPartialTarget(riskDefTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info(" End-ManageCRSBean :refreshRepoInPopup ---");
    }

    /**
     * @return
     */
    public boolean isCrsFieldsUpdatable1() {
        return crsFieldsUpdatable;
    }

    /**
     * @param filterBy1
     */
    public void setFilterBy1(String filterBy1) {
        this.filterBy1 = filterBy1;
    }

    /**
     * @return
     */
    public String getFilterBy1() {
        return filterBy1;
    }

    /**
     * @param filterBy2
     */
    public void setFilterBy2(String filterBy2) {
        this.filterBy2 = filterBy2;
    }

    /**
     * @return
     */
    public String getFilterBy2() {
        return filterBy2;
    }

    /**
     * @param filterBy3
     */
    public void setFilterBy3(String filterBy3) {
        this.filterBy3 = filterBy3;
    }

    /**
     * @return
     */
    public String getFilterBy3() {
        return filterBy3;
    }

    /**
     * @param filterValue1
     */
    public void setFilterValue1(String filterValue1) {
        this.filterValue1 = filterValue1;
    }

    /**
     * @return
     */
    public String getFilterValue1() {
        return filterValue1;
    }

    /**
     * @param filterValue2
     */
    public void setFilterValue2(String filterValue2) {
        this.filterValue2 = filterValue2;
    }

    /**
     * @return
     */
    public String getFilterValue2() {
        return filterValue2;
    }

    /**
     * @param filterValue3
     */
    public void setFilterValue3(String filterValue3) {
        this.filterValue3 = filterValue3;
    }

    /**
     * @return
     */
    public String getFilterValue3() {
        return filterValue3;
    }

    /**
     * @param filterCri1
     */
    public void setFilterCri1(String filterCri1) {
        this.filterCri1 = filterCri1;
    }

    /**
     * @return
     */
    public String getFilterCri1() {
        return filterCri1;
    }

    /**
     * @param filterCri2
     */
    public void setFilterCri2(String filterCri2) {
        this.filterCri2 = filterCri2;
    }

    /**
     * @return
     */
    public String getFilterCri2() {
        return filterCri2;
    }

    /**
     * @param actionEvent
     */
    public void onOkFilter(ActionEvent actionEvent) {
        logger.info("Performing advanced filter on the table");
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

    /**
     * Fetches the dictionary version from session
     */
    public void initManageCrs(){
        logger.info("Initalizing CRS taskflow, fetching the dictionary version from session");
        String dictVersion = (String)ADFUtils.getSessionScopeValue("dictVersion");
        if(dictVersion == null){
            OperationBinding oper = ADFUtils.findOperation("fetchDictionaryVersion");
            dictVersion = (String)oper.execute();  
            ADFUtils.setSessionScopeValue("dictVersion", dictVersion);
            logger.info("Dict Version is :: "+dictVersion);
        }
    }

    /**
     * @param advancedFilterPopup
     */
    public void setAdvancedFilterPopup(RichPopup advancedFilterPopup) {
        this.advancedFilterPopup = advancedFilterPopup;
    }

    /**
     * @return
     */
    public RichPopup getAdvancedFilterPopup() {
        return advancedFilterPopup;
    }

    /**
     * @param actionEvent
     */
    public void clearFilters(ActionEvent actionEvent) {
        logger.info("Clearing advanced filters");
        Long crsId = (Long)ADFUtils.getPageFlowScopeValue("crsId");
        OperationBinding oper = ADFUtils.findOperation("initRiskRelation");
        oper.getParamsMap().put("crsId", crsId);
        oper.getParamsMap().put("status", getBaseOrStaging());
        oper.execute();
        if (oper.getErrors().size() > 0) 
            ADFUtils.showFacesMessage(uiBundle.getString("INTERNAL_ERROR"), FacesMessage.SEVERITY_ERROR);
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

    /**
     * @param currReleaseStatus
     */
    public void setCurrReleaseStatus(String currReleaseStatus) {
        this.currReleaseStatus = currReleaseStatus;
    }

    /**
     * @return
     */
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
     * This method write header data to risk definition and PT report.
     * @param sheet
     */
    private void writeHeaderData(Sheet sheet,int firstPalletStartIndx,
                                 int firstPalletEndIndx,
                                 int secondPalletStartIndx,
                                 int secondPalletEndIndx) {
        //        Excel report Header data to include
        int count = 6;
        //invoke prepareStatesMap to get state names
        if (statesMap == null || (statesMap != null && statesMap.size() == 0))
            prepareStatesMap();
        
        //  •1 CRS Name
        org.apache.poi.ss.usermodel.Row row1 = sheet.createRow(count);
        Cell cell11 = row1.createCell((short)firstPalletStartIndx);
        cell11.setCellValue("CRS Name :" +
                            ADFUtils.evaluateEL("#{pageFlowScope.crsName}"));
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell11.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, firstPalletStartIndx, firstPalletEndIndx));
        //•1 CRS ID
        //CRS ID
        Cell cell12 = row1.createCell((short)secondPalletStartIndx);
        cell12.setCellValue("CRS ID : " +
                            (Long)ADFUtils.getPageFlowScopeValue("crsId"));
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell12.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, secondPalletStartIndx, secondPalletEndIndx));
        count++;
        //        • 2 Dictionary Version
        org.apache.poi.ss.usermodel.Row row2 = sheet.createRow(count);
        //dictionary version
        Cell cell21 = row2.createCell((short)firstPalletStartIndx);
        cell21.setCellValue("Dictionary Version: " +
                            ADFUtils.evaluateEL("#{sessionScope.dictVersion}"));
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell21.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, firstPalletStartIndx, firstPalletEndIndx));
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
        Cell cell22 = row2.createCell((short)secondPalletStartIndx);
        // TODO
        cell22.setCellValue("Status: " + status);
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell22.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, secondPalletStartIndx, secondPalletEndIndx));
        count++;

        //•3 Date and time the report is run
        //Report time
        org.apache.poi.ss.usermodel.Row row3 = sheet.createRow(count);
        Cell cell31 = row3.createCell((short)firstPalletStartIndx);
        cell31.setCellValue("Downloaded Time: " +
                            ModelConstants.getCustomTimeStamp());
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell31.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, firstPalletStartIndx, firstPalletEndIndx));
        //•3 Release Status (CURRENT or PENDING)
        String relFlag =
            (String)ADFUtils.evaluateEL("#{bindings.ReleaseStatusFlag.inputValue}");
        String relstatus = "";
        if ("P".equals(relFlag))
            relstatus = ViewConstants.PENDING;
        else
            relstatus = ViewConstants.CURRENT;
        Cell cell32 = row3.createCell((short)secondPalletStartIndx);
        cell32.setCellValue("Release Status: " + relstatus);
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell32.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, secondPalletStartIndx, secondPalletEndIndx));
        sheet.setColumnWidth(4, 6000);
        count++;
        //        •4 State (only displays the value for PENDING CRSs
        //        •4 BSL
        //BSL
        org.apache.poi.ss.usermodel.Row row4 = sheet.createRow(count);
       
        Cell cell41 = row4.createCell((short)firstPalletStartIndx);
        int stateIdstg = 0;
        if (ModelConstants.STAGING_FACET.equals(getBaseOrStaging())) {
            stateIdstg =
                (Integer)ADFUtils.evaluateEL("#{bindings.StateId.inputValue}");
        }
        cell41.setCellValue("State:  " + (statesMap.get(stateIdstg) == null ?
                            "" : statesMap.get(stateIdstg)));
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell41.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, firstPalletStartIndx, firstPalletEndIndx));
        
        Cell cell42 = row4.createCell((short)secondPalletStartIndx);
        String bsl = null;
        if (ModelConstants.BASE_FACET.equals(getBaseOrStaging())) {
            bsl =
(String)ADFUtils.evaluateEL("#{bindings.BslNameBase.inputValue}");
        } else
            bsl =
(String)ADFUtils.evaluateEL("#{bindings.BslName.inputValue}");
        bsl = getFullNamesForAccName(bsl);
        cell42.setCellValue("BSL: " + bsl);
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell42.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, secondPalletStartIndx, secondPalletEndIndx));
        count++;
        //        •5 TASL
        //        •6 Medical Lead
        //TASL
        org.apache.poi.ss.usermodel.Row row5 = sheet.createRow(count);
        Cell cell51 = row5.createCell((short)firstPalletStartIndx);
        String tasl = null;
        if (ModelConstants.BASE_FACET.equals(getBaseOrStaging())) {
            tasl =
(String)ADFUtils.evaluateEL("#{bindings.TaslNameBase.inputValue}");
        } else
            tasl =
(String)ADFUtils.evaluateEL("#{bindings.TaslName.inputValue}");
        tasl = getFullNamesForAccName(tasl);
        cell51.setCellValue("TASL :  " + tasl);
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell51.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, firstPalletStartIndx, firstPalletEndIndx));
        //ML name
        Cell cell52 = row5.createCell((short)secondPalletStartIndx);
        String medLLead = null;
        if (ModelConstants.BASE_FACET.equals(getBaseOrStaging())) {
            medLLead =
                    (String)ADFUtils.evaluateEL("#{bindings.MedicalLeadNameBase.inputValue}");
        } else
            medLLead =
                    (String)ADFUtils.evaluateEL("#{bindings.MedicalLeadName.inputValue}");
        medLLead = getFullNamesForAccName(medLLead);
        cell52.setCellValue("Medical Lead: " + medLLead);
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell52.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, secondPalletStartIndx, secondPalletEndIndx));
        count++;
        org.apache.poi.ss.usermodel.Row row6 = sheet.createRow(count);
        String designee = "";
        if (ModelConstants.BASE_FACET.equals(getBaseOrStaging())) {
            designee =
                    (String)ADFUtils.evaluateEL("#{bindings.DesigneeBase.inputValue}");
        } else
            designee =
                    (String)ADFUtils.evaluateEL("#{bindings.Designee.inputValue}");
        designee = getFullNamesForAccName(designee);
        Cell cell61 = row6.createCell((short)firstPalletStartIndx);
        cell61.setCellValue("Designee: " + designee);
        ExcelExportUtils.setHeaderCellStyle(sheet, count,
                                            cell61.getColumnIndex(), false,
                                            CellStyle.ALIGN_LEFT);
        sheet.addMergedRegion(new CellRangeAddress(count, count, firstPalletStartIndx, firstPalletEndIndx));
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
     * This method set state null or activated based on the flow type.
     * @param vce
     */
    public void onChangeReleaseStatus(ValueChangeEvent vce) {
        // Add event code here...
        if (vce != null && !vce.getNewValue().equals(vce.getOldValue())) {
//            if (ViewConstants.FLOW_TYPE_SEARCH.equals(getFlowType())) {
//                if (ModelConstants.STATUS_PENDING.equals((String)vce.getNewValue())) {
//                    ADFUtils.setEL("#{bindings.State.inputValue}", null);
//                } else {
//                    ADFUtils.setEL("#{bindings.State.inputValue}",
//                                   ModelConstants.STATE_ACTIVATED);
//                }
//            }
            ADFUtils.addPartialTarget(stateSwitcherBinding);
        }
    }

    /**
     * This method exports PT report for the current CRS id.
     * @param facesContext
     * @param outputStream
     * @throws IOException
     */
    public void exportPTReport(FacesContext facesContext,
                               OutputStream outputStream) {
        // Add event code here...
        logger.info("Start of CRSReportsBean:exportPTReport()");
        Workbook workbook = null;
        DCIteratorBinding iter =
            ADFUtils.findIterator("PTReportVOIterator");
        if(iter!=null&& iter.getViewObject()!=null){
            ViewObjectImpl vo = (ViewObjectImpl)iter.getViewObject();
            String wrClause = "CRS_ID = "+ADFUtils.getPageFlowScopeValue("crsId");
            vo.setWhereClause(wrClause);
            vo.executeQuery();
        }
        ExcelExportUtils excUtils = new ExcelExportUtils();
        InputStream excelInputStream = excUtils.getExcelInpStream();
        InputStream imageInputStream = excUtils.getImageInpStream();
            try {
                //create sheet
                RowSetIterator rowSet = null;
                int rowStartIndex = 14;
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
                    BundleFactory.getBundle("com.novartis.ecrs.view.ECRSViewControllerBundle");
                //Here Key will be ViewObject Attribute
                columnMap.put("SafetyTopicOfInterest",
                              rsBundle.getString("SAFETY_TOPIC_OF_INTEREST"));
                columnMap.put("PtName", rsBundle.getString("PT_NAME"));
                columnMap.put("PtCode", rsBundle.getString("PT_CODE"));
                workbook.setMissingCellPolicy(org.apache.poi.ss.usermodel.Row.CREATE_NULL_AS_BLANK);
                Sheet sheet = workbook.getSheetAt(0);
                writeHeaderData(sheet,0,1,3,4);
                ExcelExportUtils.writeExcelSheet(sheet, rowSet, rowStartIndex,
                                                 cellStartIndex, columnMap, null,
                                                 dateCellFormat, emptyValReplace,
                                                 imageInputStream);

            } catch (IOException ioe) {
                // TODO: Add catch code
                ioe.printStackTrace();
            } catch (InvalidFormatException ife) {
                // TODO: Add catch code
                ife.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                workbook.write(outputStream);
                excelInputStream.close();
                outputStream.close();
            } catch (IOException ioe) {
                // TODO: Add catch code
                ADFUtils.showFacesMessage(ioe.getMessage(), FacesMessage.SEVERITY_ERROR);
            }
            }
        logger.info("End of CRSReportsBean:exportPTReport()");
    }

    /**
     * This method returns fullname from input account name of the user.
     * @param accName
     * @return
     */
    private String getFullNamesForAccName(String accName){
        //invoke AMImpl with bsl,tasl,ml,designee acc names as keys
        DCIteratorBinding iter = ADFUtils.findIterator("UserFullNameIterator");
        String fullName = "";
        if(iter!=null && iter.getViewObject()!=null){
            ViewObjectImpl vo = (ViewObjectImpl)iter.getViewObject();
            vo.setWhereClause("user_name = '"+ accName+"'");
            vo.executeQuery();
            if(vo.first()!=null)
               fullName = (String) vo.first().getAttribute("FullName");
            logger.info("FullName for AccountName :-"+accName+" is -"+fullName);
        }
        return fullName;
    }

    /**
     * @param riskDefPopupPanel
     */
    public void setRiskDefPopupPanel(RichPanelGroupLayout riskDefPopupPanel) {
        this.riskDefPopupPanel = riskDefPopupPanel;
    }

    /**
     * @return
     */
    public RichPanelGroupLayout getRiskDefPopupPanel() {
        return riskDefPopupPanel;
    }

    public void deleteSafetyTopicOfInterest(ActionEvent actionEvent) {
        DCIteratorBinding relationIter = ADFUtils.findIterator("CrsRiskRelationVOIterator");
        DCIteratorBinding definitionIter = ADFUtils.findIterator("CrsRiskDefinitionsVOIterator");
        ViewObject definitionVO = definitionIter.getViewObject();
        Row[] defRows = definitionVO.getAllRowsInRange();
        for(Row row : defRows)
            row.remove();
        relationIter.getCurrentRow().remove();
        OperationBinding oper = ADFUtils.findOperation("Commit");
        oper.execute();
        if (oper.getErrors().size() > 0)
            ADFUtils.showFacesMessage(uiBundle.getString("INTERNAL_ERROR"), FacesMessage.SEVERITY_ERROR);
        riskDefPopup.hide();
        Long crsId = (Long)ADFUtils.getPageFlowScopeValue("crsId");
        Map params = new HashMap<String, Object>();
        params.put("crsId", crsId);
        params.put("status", ViewConstants.STAGING);
        logger.info("Init risk Relation : current Crs ID :: "+crsId);
        logger.info("Init risk Relation : Base or Staging :: "+getBaseOrStaging());
        try {
            logger.info("Calling AM method initRiskRelation");
            ADFUtils.executeAction("initRiskRelation", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ADFUtils.addPartialTarget(stagingTable);
    }

    public void setStagingTable(RichTable stagingTable) {
        this.stagingTable = stagingTable;
    }

    public RichTable getStagingTable() {
        return stagingTable;
    }
}
