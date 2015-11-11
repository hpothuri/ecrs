package com.novartis.ecrs.view;

import com.novartis.ecrs.model.constants.ModelConstants;
import com.novartis.ecrs.ui.bean.ManageCRSBean;
import com.novartis.ecrs.ui.utility.ADFUtils;
import com.novartis.ecrs.view.beans.SessionBean;

import java.io.Serializable;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;

import oracle.adf.model.binding.DCBindingContainer;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.layout.RichPanelGroupLayout;

import oracle.binding.OperationBinding;

import oracle.javatools.resourcebundle.BundleFactory;

import org.apache.log4j.Logger;

public class FreezeMedDRABean implements Serializable {

    @SuppressWarnings("compatibility:5871946036701436342")
    private static final long serialVersionUID = -3593612141973470337L;
    public static final Logger logger = Logger.getLogger(FreezeMedDRABean.class);
    private transient ResourceBundle uiBundle =
        BundleFactory.getBundle("com.novartis.ecrs.view.ECRSViewControllerBundle");
    private Boolean freezeMedDRAFlag = Boolean.FALSE;
    private String freezeMedDRA;
    
    public FreezeMedDRABean() {
        super();
    }
    
    public void setFreezeMedDRAFlag(Boolean freezeMedDRAFlag) {
        this.freezeMedDRAFlag = freezeMedDRAFlag;
    }

    public Boolean getFreezeMedDRAFlag() {
        return freezeMedDRAFlag;
    }

    public void setFreezeMedDRA(String freezeMedDRA) {
        this.freezeMedDRA = freezeMedDRA;
    }

    public String getFreezeMedDRA() {
        return freezeMedDRA;
    }
    public void updateFreezeMedDRAFlag(){
        logger.info("Begin updateMedDRAFreezeFlag...");
        logger.info("freezeMedDRA..." + this.freezeMedDRA);
        DCBindingContainer bc = ADFUtils.getDCBindingContainer();
        OperationBinding ob = bc.getOperationBinding("updateMedDRAFreezeFlag");
        ob.getParamsMap().put("freezeFlag", this.freezeMedDRA);
        String retVal = (String) ob.execute();
        if (ob.getErrors().size() > 0) {
            ADFUtils.showFacesMessage(uiBundle.getString("INTERNAL_ERROR"),
                                      FacesMessage.SEVERITY_ERROR);
        }
        if (ModelConstants.PLSQL_CALL_SUCCESS.equalsIgnoreCase(retVal)){
            if (null != this.freezeMedDRA && this.freezeMedDRA.equalsIgnoreCase("Y")){
                this.freezeMedDRAFlag = Boolean.TRUE;
            }
            ADFUtils.showFacesMessage("Freeze Flag Saved Successfully.", FacesMessage.SEVERITY_INFO);
        }
        SessionBean sessionBean = (SessionBean) ADFUtils.getSessionScopeValue("sessionBean");
        if (null != sessionBean){
                sessionBean.setFreezeMedDRAFlag(freezeMedDRAFlag);
        }
        logger.info("End updateMedDRAFreezeFlag...");
    }
   
    public String getFreezeMedDRAFlagFromDB(){
        logger.info("Begin getFreezeMedDRAFlagFromDB...");
        String retVal = "N";
        DCBindingContainer bc = ADFUtils.getDCBindingContainer();
        OperationBinding ob = bc.getOperationBinding("getMedDRAFreezeFlag");
        retVal = (String) ob.execute();
        if (null != retVal){
            this.setFreezeMedDRA(retVal);
        } else {
            this.setFreezeMedDRA("N");
        }
        
        logger.info("freezeMedDRA..." + this.freezeMedDRA);
        logger.info("End getFreezeMedDRAFlagFromDB...");
        return retVal;
    }
}
