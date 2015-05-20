package com.novartis.ecrs.model.entity;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowIterator;
import oracle.jbo.RowSet;
import oracle.jbo.RowSetIterator;
import oracle.jbo.domain.Timestamp;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.EntityDefImpl;
import oracle.jbo.server.EntityImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Mon Apr 13 13:40:01 IST 2015
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class CrsContentEOImpl extends EntityImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. Do not modify.
     */
    public enum AttributesEnum {
        CrsId {
            public Object get(CrsContentEOImpl obj) {
                return obj.getCrsId();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setCrsId((Long)value);
            }
        }
        ,
        CrsName {
            public Object get(CrsContentEOImpl obj) {
                return obj.getCrsName();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setCrsName((String)value);
            }
        }
        ,
        StateId {
            public Object get(CrsContentEOImpl obj) {
                return obj.getStateId();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setStateId((Integer)value);
            }
        }
        ,
        CompoundId {
            public Object get(CrsContentEOImpl obj) {
                return obj.getCompoundId();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setCompoundId((Integer)value);
            }
        }
        ,
        GenericName {
            public Object get(CrsContentEOImpl obj) {
                return obj.getGenericName();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setGenericName((String)value);
            }
        }
        ,
        TradeName {
            public Object get(CrsContentEOImpl obj) {
                return obj.getTradeName();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setTradeName((String)value);
            }
        }
        ,
        Indication {
            public Object get(CrsContentEOImpl obj) {
                return obj.getIndication();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setIndication((String)value);
            }
        }
        ,
        IsMarketedFlag {
            public Object get(CrsContentEOImpl obj) {
                return obj.getIsMarketedFlag();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setIsMarketedFlag((String)value);
            }
        }
        ,
        ReleaseStatusFlag {
            public Object get(CrsContentEOImpl obj) {
                return obj.getReleaseStatusFlag();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setReleaseStatusFlag((String)value);
            }
        }
        ,
        BslName {
            public Object get(CrsContentEOImpl obj) {
                return obj.getBslName();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setBslName((String)value);
            }
        }
        ,
        Designee {
            public Object get(CrsContentEOImpl obj) {
                return obj.getDesignee();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setDesignee((String)value);
            }
        }
        ,
        MqmComment {
            public Object get(CrsContentEOImpl obj) {
                return obj.getMqmComment();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setMqmComment((String)value);
            }
        }
        ,
        TaslName {
            public Object get(CrsContentEOImpl obj) {
                return obj.getTaslName();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setTaslName((String)value);
            }
        }
        ,
        TaslRejectComment {
            public Object get(CrsContentEOImpl obj) {
                return obj.getTaslRejectComment();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setTaslRejectComment((String)value);
            }
        }
        ,
        MedicalLeadName {
            public Object get(CrsContentEOImpl obj) {
                return obj.getMedicalLeadName();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setMedicalLeadName((String)value);
            }
        }
        ,
        MedicalLeadRejectComment {
            public Object get(CrsContentEOImpl obj) {
                return obj.getMedicalLeadRejectComment();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setMedicalLeadRejectComment((String)value);
            }
        }
        ,
        CrsEffectiveDt {
            public Object get(CrsContentEOImpl obj) {
                return obj.getCrsEffectiveDt();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setCrsEffectiveDt((Timestamp)value);
            }
        }
        ,
        ReviewApproveRequiredFlag {
            public Object get(CrsContentEOImpl obj) {
                return obj.getReviewApproveRequiredFlag();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setReviewApproveRequiredFlag((String)value);
            }
        }
        ,
        UiVersionNumber {
            public Object get(CrsContentEOImpl obj) {
                return obj.getUiVersionNumber();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        ReasonForChange {
            public Object get(CrsContentEOImpl obj) {
                return obj.getReasonForChange();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setReasonForChange((String)value);
            }
        }
        ,
        CreatedBy {
            public Object get(CrsContentEOImpl obj) {
                return obj.getCreatedBy();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        CreationTs {
            public Object get(CrsContentEOImpl obj) {
                return obj.getCreationTs();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        ModifiedBy {
            public Object get(CrsContentEOImpl obj) {
                return obj.getModifiedBy();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        ModificationTs {
            public Object get(CrsContentEOImpl obj) {
                return obj.getModificationTs();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        CrsStateEO {
            public Object get(CrsContentEOImpl obj) {
                return obj.getCrsStateEO();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setCrsStateEO((CrsStateEOImpl)value);
            }
        }
        ,
        CrsCompoundEO {
            public Object get(CrsContentEOImpl obj) {
                return obj.getCrsCompoundEO();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setCrsCompoundEO((CrsCompoundEOImpl)value);
            }
        }
        ,
        CrsRiskRelationsEO {
            public Object get(CrsContentEOImpl obj) {
                return obj.getCrsRiskRelationsEO();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        CrsContentStagingVA {
            public Object get(CrsContentEOImpl obj) {
                return obj.getCrsContentStagingVA();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ,
        CrsContentBaseVA {
            public Object get(CrsContentEOImpl obj) {
                return obj.getCrsContentBaseVA();
            }

            public void put(CrsContentEOImpl obj, Object value) {
                obj.setAttributeInternal(index(), value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static int firstIndex = 0;

        public abstract Object get(CrsContentEOImpl object);

        public abstract void put(CrsContentEOImpl object, Object value);

        public int index() {
            return AttributesEnum.firstIndex() + ordinal();
        }

        public static int firstIndex() {
            return firstIndex;
        }

        public static int count() {
            return AttributesEnum.firstIndex() +
                AttributesEnum.staticValues().length;
        }

        public static AttributesEnum[] staticValues() {
            if (vals == null) {
                vals = AttributesEnum.values();
            }
            return vals;
        }
    }


    public static final int CRSID = AttributesEnum.CrsId.index();
    public static final int CRSNAME = AttributesEnum.CrsName.index();
    public static final int STATEID = AttributesEnum.StateId.index();
    public static final int COMPOUNDID = AttributesEnum.CompoundId.index();
    public static final int GENERICNAME = AttributesEnum.GenericName.index();
    public static final int TRADENAME = AttributesEnum.TradeName.index();
    public static final int INDICATION = AttributesEnum.Indication.index();
    public static final int ISMARKETEDFLAG = AttributesEnum.IsMarketedFlag.index();
    public static final int RELEASESTATUSFLAG = AttributesEnum.ReleaseStatusFlag.index();
    public static final int BSLNAME = AttributesEnum.BslName.index();
    public static final int DESIGNEE = AttributesEnum.Designee.index();
    public static final int MQMCOMMENT = AttributesEnum.MqmComment.index();
    public static final int TASLNAME = AttributesEnum.TaslName.index();
    public static final int TASLREJECTCOMMENT = AttributesEnum.TaslRejectComment.index();
    public static final int MEDICALLEADNAME = AttributesEnum.MedicalLeadName.index();
    public static final int MEDICALLEADREJECTCOMMENT = AttributesEnum.MedicalLeadRejectComment.index();
    public static final int CRSEFFECTIVEDT = AttributesEnum.CrsEffectiveDt.index();
    public static final int REVIEWAPPROVEREQUIREDFLAG = AttributesEnum.ReviewApproveRequiredFlag.index();
    public static final int UIVERSIONNUMBER = AttributesEnum.UiVersionNumber.index();
    public static final int REASONFORCHANGE = AttributesEnum.ReasonForChange.index();
    public static final int CREATEDBY = AttributesEnum.CreatedBy.index();
    public static final int CREATIONTS = AttributesEnum.CreationTs.index();
    public static final int MODIFIEDBY = AttributesEnum.ModifiedBy.index();
    public static final int MODIFICATIONTS = AttributesEnum.ModificationTs.index();
    public static final int CRSSTATEEO = AttributesEnum.CrsStateEO.index();
    public static final int CRSCOMPOUNDEO = AttributesEnum.CrsCompoundEO.index();
    public static final int CRSRISKRELATIONSEO = AttributesEnum.CrsRiskRelationsEO.index();
    public static final int CRSCONTENTSTAGINGVA = AttributesEnum.CrsContentStagingVA.index();
    public static final int CRSCONTENTBASEVA = AttributesEnum.CrsContentBaseVA.index();

    /**
     * This is the default constructor (do not remove).
     */
    public CrsContentEOImpl() {
    }


    /**
     * @return the definition object for this instance class.
     */
    public static synchronized EntityDefImpl getDefinitionObject() {
        return EntityDefImpl.findDefObject("com.novartis.ecrs.model.entity.CrsContentEO");
    }

    /**
     * Gets the attribute value for CrsId, using the alias name CrsId.
     * @return the value of CrsId
     */
    public Long getCrsId() {
        return (Long)getAttributeInternal(CRSID);
    }

    /**
     * Sets <code>value</code> as the attribute value for CrsId.
     * @param value value to set the CrsId
     */
    public void setCrsId(Long value) {
        setAttributeInternal(CRSID, value);
    }

    /**
     * Gets the attribute value for CrsName, using the alias name CrsName.
     * @return the value of CrsName
     */
    public String getCrsName() {
        return (String)getAttributeInternal(CRSNAME);
    }

    /**
     * Sets <code>value</code> as the attribute value for CrsName.
     * @param value value to set the CrsName
     */
    public void setCrsName(String value) {
        setAttributeInternal(CRSNAME, value);
    }

    /**
     * Gets the attribute value for StateId, using the alias name StateId.
     * @return the value of StateId
     */
    public Integer getStateId() {
        return (Integer)getAttributeInternal(STATEID);
    }

    /**
     * Sets <code>value</code> as the attribute value for StateId.
     * @param value value to set the StateId
     */
    public void setStateId(Integer value) {
        setAttributeInternal(STATEID, value);
    }

    /**
     * Gets the attribute value for CompoundId, using the alias name CompoundId.
     * @return the value of CompoundId
     */
    public Integer getCompoundId() {
        return (Integer)getAttributeInternal(COMPOUNDID);
    }

    /**
     * Sets <code>value</code> as the attribute value for CompoundId.
     * @param value value to set the CompoundId
     */
    public void setCompoundId(Integer value) {
        setAttributeInternal(COMPOUNDID, value);
    }

    /**
     * Gets the attribute value for GenericName, using the alias name GenericName.
     * @return the value of GenericName
     */
    public String getGenericName() {
        return (String)getAttributeInternal(GENERICNAME);
    }

    /**
     * Sets <code>value</code> as the attribute value for GenericName.
     * @param value value to set the GenericName
     */
    public void setGenericName(String value) {
        setAttributeInternal(GENERICNAME, value);
    }

    /**
     * Gets the attribute value for TradeName, using the alias name TradeName.
     * @return the value of TradeName
     */
    public String getTradeName() {
        return (String)getAttributeInternal(TRADENAME);
    }

    /**
     * Sets <code>value</code> as the attribute value for TradeName.
     * @param value value to set the TradeName
     */
    public void setTradeName(String value) {
        setAttributeInternal(TRADENAME, value);
    }

    /**
     * Gets the attribute value for Indication, using the alias name Indication.
     * @return the value of Indication
     */
    public String getIndication() {
        return (String)getAttributeInternal(INDICATION);
    }

    /**
     * Sets <code>value</code> as the attribute value for Indication.
     * @param value value to set the Indication
     */
    public void setIndication(String value) {
        setAttributeInternal(INDICATION, value);
    }

    /**
     * Gets the attribute value for IsMarketedFlag, using the alias name IsMarketedFlag.
     * @return the value of IsMarketedFlag
     */
    public String getIsMarketedFlag() {
        return (String)getAttributeInternal(ISMARKETEDFLAG);
    }

    /**
     * Sets <code>value</code> as the attribute value for IsMarketedFlag.
     * @param value value to set the IsMarketedFlag
     */
    public void setIsMarketedFlag(String value) {
        setAttributeInternal(ISMARKETEDFLAG, value);
    }

    /**
     * Gets the attribute value for ReleaseStatusFlag, using the alias name ReleaseStatusFlag.
     * @return the value of ReleaseStatusFlag
     */
    public String getReleaseStatusFlag() {
        return (String)getAttributeInternal(RELEASESTATUSFLAG);
    }

    /**
     * Sets <code>value</code> as the attribute value for ReleaseStatusFlag.
     * @param value value to set the ReleaseStatusFlag
     */
    public void setReleaseStatusFlag(String value) {
        setAttributeInternal(RELEASESTATUSFLAG, value);
    }

    /**
     * Gets the attribute value for BslName, using the alias name BslName.
     * @return the value of BslName
     */
    public String getBslName() {
        return (String)getAttributeInternal(BSLNAME);
    }

    /**
     * Sets <code>value</code> as the attribute value for BslName.
     * @param value value to set the BslName
     */
    public void setBslName(String value) {
        setAttributeInternal(BSLNAME, value);
    }

    /**
     * Gets the attribute value for Designee, using the alias name Designee.
     * @return the value of Designee
     */
    public String getDesignee() {
        return (String)getAttributeInternal(DESIGNEE);
    }

    /**
     * Sets <code>value</code> as the attribute value for Designee.
     * @param value value to set the Designee
     */
    public void setDesignee(String value) {
        setAttributeInternal(DESIGNEE, value);
    }

    /**
     * Gets the attribute value for MqmComment, using the alias name MqmComment.
     * @return the value of MqmComment
     */
    public String getMqmComment() {
        return (String)getAttributeInternal(MQMCOMMENT);
    }

    /**
     * Sets <code>value</code> as the attribute value for MqmComment.
     * @param value value to set the MqmComment
     */
    public void setMqmComment(String value) {
        setAttributeInternal(MQMCOMMENT, value);
    }

    /**
     * Gets the attribute value for TaslName, using the alias name TaslName.
     * @return the value of TaslName
     */
    public String getTaslName() {
        return (String)getAttributeInternal(TASLNAME);
    }

    /**
     * Sets <code>value</code> as the attribute value for TaslName.
     * @param value value to set the TaslName
     */
    public void setTaslName(String value) {
        setAttributeInternal(TASLNAME, value);
    }

    /**
     * Gets the attribute value for TaslRejectComment, using the alias name TaslRejectComment.
     * @return the value of TaslRejectComment
     */
    public String getTaslRejectComment() {
        return (String)getAttributeInternal(TASLREJECTCOMMENT);
    }

    /**
     * Sets <code>value</code> as the attribute value for TaslRejectComment.
     * @param value value to set the TaslRejectComment
     */
    public void setTaslRejectComment(String value) {
        setAttributeInternal(TASLREJECTCOMMENT, value);
    }

    /**
     * Gets the attribute value for MedicalLeadName, using the alias name MedicalLeadName.
     * @return the value of MedicalLeadName
     */
    public String getMedicalLeadName() {
        return (String)getAttributeInternal(MEDICALLEADNAME);
    }

    /**
     * Sets <code>value</code> as the attribute value for MedicalLeadName.
     * @param value value to set the MedicalLeadName
     */
    public void setMedicalLeadName(String value) {
        setAttributeInternal(MEDICALLEADNAME, value);
    }

    /**
     * Gets the attribute value for MedicalLeadRejectComment, using the alias name MedicalLeadRejectComment.
     * @return the value of MedicalLeadRejectComment
     */
    public String getMedicalLeadRejectComment() {
        return (String)getAttributeInternal(MEDICALLEADREJECTCOMMENT);
    }

    /**
     * Sets <code>value</code> as the attribute value for MedicalLeadRejectComment.
     * @param value value to set the MedicalLeadRejectComment
     */
    public void setMedicalLeadRejectComment(String value) {
        setAttributeInternal(MEDICALLEADREJECTCOMMENT, value);
    }

    /**
     * Gets the attribute value for CrsEffectiveDt, using the alias name CrsEffectiveDt.
     * @return the value of CrsEffectiveDt
     */
    public Timestamp getCrsEffectiveDt() {
        return (Timestamp)getAttributeInternal(CRSEFFECTIVEDT);
    }

    /**
     * Sets <code>value</code> as the attribute value for CrsEffectiveDt.
     * @param value value to set the CrsEffectiveDt
     */
    public void setCrsEffectiveDt(Timestamp value) {
        setAttributeInternal(CRSEFFECTIVEDT, value);
    }

    /**
     * Gets the attribute value for ReviewApproveRequiredFlag, using the alias name ReviewApproveRequiredFlag.
     * @return the value of ReviewApproveRequiredFlag
     */
    public String getReviewApproveRequiredFlag() {
        return (String)getAttributeInternal(REVIEWAPPROVEREQUIREDFLAG);
    }

    /**
     * Sets <code>value</code> as the attribute value for ReviewApproveRequiredFlag.
     * @param value value to set the ReviewApproveRequiredFlag
     */
    public void setReviewApproveRequiredFlag(String value) {
        setAttributeInternal(REVIEWAPPROVEREQUIREDFLAG, value);
    }

    /**
     * Gets the attribute value for UiVersionNumber, using the alias name UiVersionNumber.
     * @return the value of UiVersionNumber
     */
    public Integer getUiVersionNumber() {
        return (Integer)getAttributeInternal(UIVERSIONNUMBER);
    }


    /**
     * Gets the attribute value for ReasonForChange, using the alias name ReasonForChange.
     * @return the value of ReasonForChange
     */
    public String getReasonForChange() {
        return (String)getAttributeInternal(REASONFORCHANGE);
    }

    /**
     * Sets <code>value</code> as the attribute value for ReasonForChange.
     * @param value value to set the ReasonForChange
     */
    public void setReasonForChange(String value) {
        setAttributeInternal(REASONFORCHANGE, value);
    }

    /**
     * Gets the attribute value for CreatedBy, using the alias name CreatedBy.
     * @return the value of CreatedBy
     */
    public String getCreatedBy() {
        return (String)getAttributeInternal(CREATEDBY);
    }


    /**
     * Gets the attribute value for CreationTs, using the alias name CreationTs.
     * @return the value of CreationTs
     */
    public Timestamp getCreationTs() {
        return (Timestamp)getAttributeInternal(CREATIONTS);
    }


    /**
     * Gets the attribute value for ModifiedBy, using the alias name ModifiedBy.
     * @return the value of ModifiedBy
     */
    public String getModifiedBy() {
        return (String)getAttributeInternal(MODIFIEDBY);
    }


    /**
     * Gets the attribute value for ModificationTs, using the alias name ModificationTs.
     * @return the value of ModificationTs
     */
    public Timestamp getModificationTs() {
        return (Timestamp)getAttributeInternal(MODIFICATIONTS);
    }


    /**
     * getAttrInvokeAccessor: generated method. Do not modify.
     * @param index the index identifying the attribute
     * @param attrDef the attribute

     * @return the attribute value
     * @throws Exception
     */
    protected Object getAttrInvokeAccessor(int index,
                                           AttributeDefImpl attrDef) throws Exception {
        if ((index >= AttributesEnum.firstIndex()) && (index < AttributesEnum.count())) {
            return AttributesEnum.staticValues()[index - AttributesEnum.firstIndex()].get(this);
        }
        return super.getAttrInvokeAccessor(index, attrDef);
    }

    /**
     * setAttrInvokeAccessor: generated method. Do not modify.
     * @param index the index identifying the attribute
     * @param value the value to assign to the attribute
     * @param attrDef the attribute

     * @throws Exception
     */
    protected void setAttrInvokeAccessor(int index, Object value,
                                         AttributeDefImpl attrDef) throws Exception {
        if ((index >= AttributesEnum.firstIndex()) && (index < AttributesEnum.count())) {
            AttributesEnum.staticValues()[index - AttributesEnum.firstIndex()].put(this, value);
            return;
        }
        super.setAttrInvokeAccessor(index, value, attrDef);
    }

    /**
     * @return the associated entity oracle.jbo.server.EntityImpl.
     */
    public CrsStateEOImpl getCrsStateEO() {
        return (CrsStateEOImpl)getAttributeInternal(CRSSTATEEO);
    }

    /**
     * Sets <code>value</code> as the associated entity oracle.jbo.server.EntityImpl.
     */
    public void setCrsStateEO(CrsStateEOImpl value) {
        setAttributeInternal(CRSSTATEEO, value);
    }

    /**
     * @return the associated entity CrsCompoundEOImpl.
     */
    public CrsCompoundEOImpl getCrsCompoundEO() {
        return (CrsCompoundEOImpl)getAttributeInternal(CRSCOMPOUNDEO);
    }

    /**
     * Sets <code>value</code> as the associated entity CrsCompoundEOImpl.
     */
    public void setCrsCompoundEO(CrsCompoundEOImpl value) {
        setAttributeInternal(CRSCOMPOUNDEO, value);
    }

    /**
     * @return the associated entity oracle.jbo.RowIterator.
     */
    public RowIterator getCrsRiskRelationsEO() {
        return (RowIterator)getAttributeInternal(CRSRISKRELATIONSEO);
    }


    /**
     * Gets the view accessor <code>RowSet</code> CrsContentStagingVA.
     */
    public RowSet getCrsContentStagingVA() {
        return (RowSet)getAttributeInternal(CRSCONTENTSTAGINGVA);
    }

    /**
     * Gets the view accessor <code>RowSet</code> CrsContentBaseVA.
     */
    public RowSet getCrsContentBaseVA() {
        return (RowSet)getAttributeInternal(CRSCONTENTBASEVA);
    }

    /**
     * @param crsId key constituent

     * @return a Key object based on given key constituents.
     */
    public static Key createPrimaryKey(Long crsId) {
        return new Key(new Object[]{crsId});
    }

    /**
     * Validation method for CrsContentEO.
     */
    public boolean validateDuplicateCheckOnCrs() {
        //search for crs name in staging and base accessors if found return true
        RowSet stgRwSet = this.getCrsContentStagingVA();
        //System.out.println("getCrsName()-->" + getCrsName());
        RowSetIterator iter = stgRwSet.createRowSetIterator(null);
        try {
            if (iter != null) {
                iter.reset();
                while (iter.hasNext()) {
                    Row rw = iter.next();
                    if (getCrsName().equals(rw.getAttribute("CrsName")) &&
                        !getCrsId().equals(rw.getAttribute("CrsId"))) {
                        return false;
                    }
                }

            }
        } finally {
            if (iter != null)
                iter.closeRowSetIterator();
        }
        RowSet baseRwSet = this.getCrsContentBaseVA();
        baseRwSet.setNamedWhereClauseParam("pCrsName", getCrsName());
        baseRwSet.executeQuery();
        if (baseRwSet.first() != null &&
            baseRwSet.first().getAttribute("CrsName").equals(getCrsName())) {
//            System.out.println("baseRwSet.first().getAttribute(\"CrsName\")-->" +
//                               baseRwSet.first().getAttribute("CrsName"));
            baseRwSet.setNamedWhereClauseParam("pCrsName", null);
            baseRwSet.executeQuery();
            return false;
        }
        return true;
    }

    //    public void doDML(int operation, TransactionEvent e) {
//        if (operation == DML_INSERT)
//            this.setCrsId((new SequenceImpl("CRS_CONTENT_SEQ",
//                                                          getDBTransaction()).getSequenceNumber()).longValue());
//        super.doDML(operation, e);
//    }
}
