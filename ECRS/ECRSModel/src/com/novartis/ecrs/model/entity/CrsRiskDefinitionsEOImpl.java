package com.novartis.ecrs.model.entity;

import oracle.jbo.Key;
import oracle.jbo.domain.Timestamp;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.EntityDefImpl;
import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.SequenceImpl;
import oracle.jbo.server.TransactionEvent;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Thu Apr 16 09:15:43 IST 2015
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class CrsRiskDefinitionsEOImpl extends EntityImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. Do not modify.
     */
    public enum AttributesEnum {
        CrsRiskDefnId {
            public Object get(CrsRiskDefinitionsEOImpl obj) {
                return obj.getCrsRiskDefnId();
            }

            public void put(CrsRiskDefinitionsEOImpl obj, Object value) {
                obj.setCrsRiskDefnId((Long)value);
            }
        }
        ,
        CrsRiskId {
            public Object get(CrsRiskDefinitionsEOImpl obj) {
                return obj.getCrsRiskId();
            }

            public void put(CrsRiskDefinitionsEOImpl obj, Object value) {
                obj.setCrsRiskId((Long)value);
            }
        }
        ,
        CrsQualifier {
            public Object get(CrsRiskDefinitionsEOImpl obj) {
                return obj.getCrsQualifier();
            }

            public void put(CrsRiskDefinitionsEOImpl obj, Object value) {
                obj.setCrsQualifier((String)value);
            }
        }
        ,
        MeddraLevel {
            public Object get(CrsRiskDefinitionsEOImpl obj) {
                return obj.getMeddraLevel();
            }

            public void put(CrsRiskDefinitionsEOImpl obj, Object value) {
                obj.setMeddraLevel((String)value);
            }
        }
        ,
        MeddraCode {
            public Object get(CrsRiskDefinitionsEOImpl obj) {
                return obj.getMeddraCode();
            }

            public void put(CrsRiskDefinitionsEOImpl obj, Object value) {
                obj.setMeddraCode((String)value);
            }
        }
        ,
        MeddraTerm {
            public Object get(CrsRiskDefinitionsEOImpl obj) {
                return obj.getMeddraTerm();
            }

            public void put(CrsRiskDefinitionsEOImpl obj, Object value) {
                obj.setMeddraTerm((String)value);
            }
        }
        ,
        MeddraVersion {
            public Object get(CrsRiskDefinitionsEOImpl obj) {
                return obj.getMeddraVersion();
            }

            public void put(CrsRiskDefinitionsEOImpl obj, Object value) {
                obj.setMeddraVersion((String)value);
            }
        }
        ,
        MeddraVersionDate {
            public Object get(CrsRiskDefinitionsEOImpl obj) {
                return obj.getMeddraVersionDate();
            }

            public void put(CrsRiskDefinitionsEOImpl obj, Object value) {
                obj.setMeddraVersionDate((Timestamp)value);
            }
        }
        ,
        SearchCriteriaDetails {
            public Object get(CrsRiskDefinitionsEOImpl obj) {
                return obj.getSearchCriteriaDetails();
            }

            public void put(CrsRiskDefinitionsEOImpl obj, Object value) {
                obj.setSearchCriteriaDetails((String)value);
            }
        }
        ,
        TmsDictContentId {
            public Object get(CrsRiskDefinitionsEOImpl obj) {
                return obj.getTmsDictContentId();
            }

            public void put(CrsRiskDefinitionsEOImpl obj, Object value) {
                obj.setTmsDictContentId((Long)value);
            }
        }
        ,
        TmsDictContentEntryTs {
            public Object get(CrsRiskDefinitionsEOImpl obj) {
                return obj.getTmsDictContentEntryTs();
            }

            public void put(CrsRiskDefinitionsEOImpl obj, Object value) {
                obj.setTmsDictContentEntryTs((Timestamp)value);
            }
        }
        ,
        TmsEndTs {
            public Object get(CrsRiskDefinitionsEOImpl obj) {
                return obj.getTmsEndTs();
            }

            public void put(CrsRiskDefinitionsEOImpl obj, Object value) {
                obj.setTmsEndTs((Timestamp)value);
            }
        }
        ,
        TmsUpdateFlag {
            public Object get(CrsRiskDefinitionsEOImpl obj) {
                return obj.getTmsUpdateFlag();
            }

            public void put(CrsRiskDefinitionsEOImpl obj, Object value) {
                obj.setTmsUpdateFlag((String)value);
            }
        }
        ,
        TmsUpdateFlagDt {
            public Object get(CrsRiskDefinitionsEOImpl obj) {
                return obj.getTmsUpdateFlagDt();
            }

            public void put(CrsRiskDefinitionsEOImpl obj, Object value) {
                obj.setTmsUpdateFlagDt((Timestamp)value);
            }
        }
        ,
        MeddraQualifier {
            public Object get(CrsRiskDefinitionsEOImpl obj) {
                return obj.getMeddraQualifier();
            }

            public void put(CrsRiskDefinitionsEOImpl obj, Object value) {
                obj.setMeddraQualifier((String)value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static int firstIndex = 0;

        public abstract Object get(CrsRiskDefinitionsEOImpl object);

        public abstract void put(CrsRiskDefinitionsEOImpl object, Object value);

        public int index() {
            return AttributesEnum.firstIndex() + ordinal();
        }

        public static int firstIndex() {
            return firstIndex;
        }

        public static int count() {
            return AttributesEnum.firstIndex() + AttributesEnum.staticValues().length;
        }

        public static AttributesEnum[] staticValues() {
            if (vals == null) {
                vals = AttributesEnum.values();
            }
            return vals;
        }
    }
    public static final int CRSRISKDEFNID = AttributesEnum.CrsRiskDefnId.index();
    public static final int CRSRISKID = AttributesEnum.CrsRiskId.index();
    public static final int CRSQUALIFIER = AttributesEnum.CrsQualifier.index();
    public static final int MEDDRALEVEL = AttributesEnum.MeddraLevel.index();
    public static final int MEDDRACODE = AttributesEnum.MeddraCode.index();
    public static final int MEDDRATERM = AttributesEnum.MeddraTerm.index();
    public static final int MEDDRAVERSION = AttributesEnum.MeddraVersion.index();
    public static final int MEDDRAVERSIONDATE = AttributesEnum.MeddraVersionDate.index();
    public static final int SEARCHCRITERIADETAILS = AttributesEnum.SearchCriteriaDetails.index();
    public static final int TMSDICTCONTENTID = AttributesEnum.TmsDictContentId.index();
    public static final int TMSDICTCONTENTENTRYTS = AttributesEnum.TmsDictContentEntryTs.index();
    public static final int TMSENDTS = AttributesEnum.TmsEndTs.index();
    public static final int TMSUPDATEFLAG = AttributesEnum.TmsUpdateFlag.index();
    public static final int TMSUPDATEFLAGDT = AttributesEnum.TmsUpdateFlagDt.index();
    public static final int MEDDRAQUALIFIER = AttributesEnum.MeddraQualifier.index();

    /**
     * This is the default constructor (do not remove).
     */
    public CrsRiskDefinitionsEOImpl() {
    }

    /**
     * Gets the attribute value for CrsRiskDefnId, using the alias name CrsRiskDefnId.
     * @return the value of CrsRiskDefnId
     */
    public Long getCrsRiskDefnId() {
        return (Long)getAttributeInternal(CRSRISKDEFNID);
    }

    /**
     * Sets <code>value</code> as the attribute value for CrsRiskDefnId.
     * @param value value to set the CrsRiskDefnId
     */
    public void setCrsRiskDefnId(Long value) {
        setAttributeInternal(CRSRISKDEFNID, value);
    }

    /**
     * Gets the attribute value for CrsRiskId, using the alias name CrsRiskId.
     * @return the value of CrsRiskId
     */
    public Long getCrsRiskId() {
        return (Long)getAttributeInternal(CRSRISKID);
    }

    /**
     * Sets <code>value</code> as the attribute value for CrsRiskId.
     * @param value value to set the CrsRiskId
     */
    public void setCrsRiskId(Long value) {
        setAttributeInternal(CRSRISKID, value);
    }

    /**
     * Gets the attribute value for CrsQualifier, using the alias name CrsQualifier.
     * @return the value of CrsQualifier
     */
    public String getCrsQualifier() {
        return (String)getAttributeInternal(CRSQUALIFIER);
    }

    /**
     * Sets <code>value</code> as the attribute value for CrsQualifier.
     * @param value value to set the CrsQualifier
     */
    public void setCrsQualifier(String value) {
        setAttributeInternal(CRSQUALIFIER, value);
    }

    /**
     * Gets the attribute value for MeddraLevel, using the alias name MeddraLevel.
     * @return the value of MeddraLevel
     */
    public String getMeddraLevel() {
        return (String)getAttributeInternal(MEDDRALEVEL);
    }

    /**
     * Sets <code>value</code> as the attribute value for MeddraLevel.
     * @param value value to set the MeddraLevel
     */
    public void setMeddraLevel(String value) {
        setAttributeInternal(MEDDRALEVEL, value);
    }

    /**
     * Gets the attribute value for MeddraCode, using the alias name MeddraCode.
     * @return the value of MeddraCode
     */
    public String getMeddraCode() {
        return (String)getAttributeInternal(MEDDRACODE);
    }

    /**
     * Sets <code>value</code> as the attribute value for MeddraCode.
     * @param value value to set the MeddraCode
     */
    public void setMeddraCode(String value) {
        setAttributeInternal(MEDDRACODE, value);
    }

    /**
     * Gets the attribute value for MeddraTerm, using the alias name MeddraTerm.
     * @return the value of MeddraTerm
     */
    public String getMeddraTerm() {
        return (String)getAttributeInternal(MEDDRATERM);
    }

    /**
     * Sets <code>value</code> as the attribute value for MeddraTerm.
     * @param value value to set the MeddraTerm
     */
    public void setMeddraTerm(String value) {
        setAttributeInternal(MEDDRATERM, value);
    }

    /**
     * Gets the attribute value for MeddraVersion, using the alias name MeddraVersion.
     * @return the value of MeddraVersion
     */
    public String getMeddraVersion() {
        return (String)getAttributeInternal(MEDDRAVERSION);
    }

    /**
     * Sets <code>value</code> as the attribute value for MeddraVersion.
     * @param value value to set the MeddraVersion
     */
    public void setMeddraVersion(String value) {
        setAttributeInternal(MEDDRAVERSION, value);
    }

    /**
     * Gets the attribute value for MeddraVersionDate, using the alias name MeddraVersionDate.
     * @return the value of MeddraVersionDate
     */
    public Timestamp getMeddraVersionDate() {
        return (Timestamp)getAttributeInternal(MEDDRAVERSIONDATE);
    }

    /**
     * Sets <code>value</code> as the attribute value for MeddraVersionDate.
     * @param value value to set the MeddraVersionDate
     */
    public void setMeddraVersionDate(Timestamp value) {
        setAttributeInternal(MEDDRAVERSIONDATE, value);
    }

    /**
     * Gets the attribute value for SearchCriteriaDetails, using the alias name SearchCriteriaDetails.
     * @return the value of SearchCriteriaDetails
     */
    public String getSearchCriteriaDetails() {
        return (String)getAttributeInternal(SEARCHCRITERIADETAILS);
    }

    /**
     * Sets <code>value</code> as the attribute value for SearchCriteriaDetails.
     * @param value value to set the SearchCriteriaDetails
     */
    public void setSearchCriteriaDetails(String value) {
        setAttributeInternal(SEARCHCRITERIADETAILS, value);
    }

    /**
     * Gets the attribute value for TmsDictContentId, using the alias name TmsDictContentId.
     * @return the value of TmsDictContentId
     */
    public Long getTmsDictContentId() {
        return (Long)getAttributeInternal(TMSDICTCONTENTID);
    }

    /**
     * Sets <code>value</code> as the attribute value for TmsDictContentId.
     * @param value value to set the TmsDictContentId
     */
    public void setTmsDictContentId(Long value) {
        setAttributeInternal(TMSDICTCONTENTID, value);
    }

    /**
     * Gets the attribute value for TmsDictContentEntryTs, using the alias name TmsDictContentEntryTs.
     * @return the value of TmsDictContentEntryTs
     */
    public Timestamp getTmsDictContentEntryTs() {
        return (Timestamp)getAttributeInternal(TMSDICTCONTENTENTRYTS);
    }

    /**
     * Sets <code>value</code> as the attribute value for TmsDictContentEntryTs.
     * @param value value to set the TmsDictContentEntryTs
     */
    public void setTmsDictContentEntryTs(Timestamp value) {
        setAttributeInternal(TMSDICTCONTENTENTRYTS, value);
    }

    /**
     * Gets the attribute value for TmsEndTs, using the alias name TmsEndTs.
     * @return the value of TmsEndTs
     */
    public Timestamp getTmsEndTs() {
        return (Timestamp)getAttributeInternal(TMSENDTS);
    }

    /**
     * Sets <code>value</code> as the attribute value for TmsEndTs.
     * @param value value to set the TmsEndTs
     */
    public void setTmsEndTs(Timestamp value) {
        setAttributeInternal(TMSENDTS, value);
    }

    /**
     * Gets the attribute value for TmsUpdateFlag, using the alias name TmsUpdateFlag.
     * @return the value of TmsUpdateFlag
     */
    public String getTmsUpdateFlag() {
        return (String)getAttributeInternal(TMSUPDATEFLAG);
    }

    /**
     * Sets <code>value</code> as the attribute value for TmsUpdateFlag.
     * @param value value to set the TmsUpdateFlag
     */
    public void setTmsUpdateFlag(String value) {
        setAttributeInternal(TMSUPDATEFLAG, value);
    }

    /**
     * Gets the attribute value for TmsUpdateFlagDt, using the alias name TmsUpdateFlagDt.
     * @return the value of TmsUpdateFlagDt
     */
    public Timestamp getTmsUpdateFlagDt() {
        return (Timestamp)getAttributeInternal(TMSUPDATEFLAGDT);
    }

    /**
     * Sets <code>value</code> as the attribute value for TmsUpdateFlagDt.
     * @param value value to set the TmsUpdateFlagDt
     */
    public void setTmsUpdateFlagDt(Timestamp value) {
        setAttributeInternal(TMSUPDATEFLAGDT, value);
    }

    /**
     * Gets the attribute value for MeddraQualifier, using the alias name MeddraQualifier.
     * @return the value of MeddraQualifier
     */
    public String getMeddraQualifier() {
        return (String)getAttributeInternal(MEDDRAQUALIFIER);
    }

    /**
     * Sets <code>value</code> as the attribute value for MeddraQualifier.
     * @param value value to set the MeddraQualifier
     */
    public void setMeddraQualifier(String value) {
        setAttributeInternal(MEDDRAQUALIFIER, value);
    }

    /**
     * getAttrInvokeAccessor: generated method. Do not modify.
     * @param index the index identifying the attribute
     * @param attrDef the attribute

     * @return the attribute value
     * @throws Exception
     */
    protected Object getAttrInvokeAccessor(int index, AttributeDefImpl attrDef) throws Exception {
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
    protected void setAttrInvokeAccessor(int index, Object value, AttributeDefImpl attrDef) throws Exception {
        if ((index >= AttributesEnum.firstIndex()) && (index < AttributesEnum.count())) {
            AttributesEnum.staticValues()[index - AttributesEnum.firstIndex()].put(this, value);
            return;
        }
        super.setAttrInvokeAccessor(index, value, attrDef);
    }

    /**
     * @param crsRiskDefnId key constituent

     * @return a Key object based on given key constituents.
     */
    public static Key createPrimaryKey(Long crsRiskDefnId) {
        return new Key(new Object[]{crsRiskDefnId});
    }

    /**
     * @return the definition object for this instance class.
     */
    public static synchronized EntityDefImpl getDefinitionObject() {
        return EntityDefImpl.findDefObject("com.novartis.ecrs.model.entity.CrsRiskDefinitionsEO");
    }
}