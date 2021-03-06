package com.novartis.ecrs.model.view;

import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.ViewRowImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Tue May 19 11:22:39 IST 2015
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class UserFullNameVORowImpl extends ViewRowImpl {
    /**
     * AttributesEnum: generated enum for identifying attributes and accessors. Do not modify.
     */
    public enum AttributesEnum {
        FullName {
            public Object get(UserFullNameVORowImpl obj) {
                return obj.getFullName();
            }

            public void put(UserFullNameVORowImpl obj, Object value) {
                obj.setFullName((String)value);
            }
        }
        ,
        UserName {
            public Object get(UserFullNameVORowImpl obj) {
                return obj.getUserName();
            }

            public void put(UserFullNameVORowImpl obj, Object value) {
                obj.setUserName((String)value);
            }
        }
        ;
        private static AttributesEnum[] vals = null;
        private static int firstIndex = 0;

        public abstract Object get(UserFullNameVORowImpl object);

        public abstract void put(UserFullNameVORowImpl object, Object value);

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
    public static final int FULLNAME = AttributesEnum.FullName.index();
    public static final int USERNAME = AttributesEnum.UserName.index();

    /**
     * This is the default constructor (do not remove).
     */
    public UserFullNameVORowImpl() {
    }

    /**
     * Gets the attribute value for the calculated attribute FullName.
     * @return the FullName
     */
    public String getFullName() {
        return (String) getAttributeInternal(FULLNAME);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute FullName.
     * @param value value to set the  FullName
     */
    public void setFullName(String value) {
        setAttributeInternal(FULLNAME, value);
    }

    /**
     * Gets the attribute value for the calculated attribute UserName.
     * @return the UserName
     */
    public String getUserName() {
        return (String) getAttributeInternal(USERNAME);
    }

    /**
     * Sets <code>value</code> as the attribute value for the calculated attribute UserName.
     * @param value value to set the  UserName
     */
    public void setUserName(String value) {
        setAttributeInternal(USERNAME, value);
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
}
