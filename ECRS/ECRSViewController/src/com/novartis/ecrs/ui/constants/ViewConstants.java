package com.novartis.ecrs.ui.constants;

import javax.faces.model.SelectItem;

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
    public static final String ANONYMOUS_ROLE = "anonymous";
    
    public static final String MEDDRA_DICTIONARY = "NMATMED";
    public static final String FILTER_DICTIONARY = "NMATSMQ";
    public static final String SMQ = "SMQ";
    public static final String CMQ = "CMQ";
    public static final String NMQ = "NMQ";
    public static final String MQ1 = "MQ1";
    public static final String MQ2 = "MQ2";
    public static final String MQ3 = "MQ3";
    public static final String MQ4 = "MQ4";
    public static final String MQ5 = "MQ5";
    public static final String SMQ1 = "SMQ 1";
    public static final String SMQ2 = "SMQ 2";
    public static final String SMQ3 = "SMQ 3";
    public static final String SMQ4 = "SMQ 4";
    public static final String SMQ5 = "SMQ 5";
    public static final String NMQ1 = "NMQ1";
    public static final String NMQ2 = "NMQ2";
    public static final String NMQ3 = "NMQ3";
    public static final String NMQ4 = "NMQ4";
    public static final String NMQ5 = "NMQ5";   
    public static final String CUSTOM1 = "Custom 1";
    public static final String CUSTOM2 = "Custom 2";
    public static final String CUSTOM3 = "Custom 3";
    public static final String CUSTOM4 = "Custom 4";
    public static final String CUSTOM5 = "Custom 5";
    
    public static final String SOC = "SOC";
    public static final String HLGT = "HLGT";
    public static final String PT = "PT";
    public static final String HLT = "HLT"; 
    
    public static final String PENDING = "Pending";
    public static final String CURRENT = "Current"; 
    
    public static final String STAGING = "STAGING";
    public static final String DOMAIN_OTHER = "OTHER TRIALS";
    
    public static boolean isNotEmpty(String str) {
        return str != null && str.trim().length() > 0;
    }
}
