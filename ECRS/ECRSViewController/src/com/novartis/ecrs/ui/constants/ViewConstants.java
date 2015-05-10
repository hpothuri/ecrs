package com.novartis.ecrs.ui.constants;

public class ViewConstants {
    public ViewConstants() {
        super();
    }
    
    public static final String COMP_TYPE_NON_COMPOUND = "NON-COMPOUND";
    public static final String PAGE_DEF_SEARCH = "com_novartis_ecrs_view_searchCRSPageDef";
    public static final String PAGE_DEF_CREATE = "com_novartis_ecrs_view_createCRSPageDef";
    public static final String NON_COMPOUND_ROUTINE = "ROUTINE";
    public static final String FLOW_TYPE_CREATE = "C";
    public static final String FLOW_TYPE_UPDATE = "U";
    public static final String FLOW_TYPE_SEARCH = "S";
    public static final String REASON_DEFAULT_VALUE = "Initial Version";
    public static final String REL_STATUS_PENDING = "P";
    
    public static boolean isNotEmpty(String str) {
        return str != null && str.trim().length() > 0;
    }
}
