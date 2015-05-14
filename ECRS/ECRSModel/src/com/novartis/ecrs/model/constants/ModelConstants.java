package com.novartis.ecrs.model.constants;

import java.text.SimpleDateFormat;

import java.util.Calendar;


public class ModelConstants {
    public ModelConstants() {
        super();
    }

    //public static String
    public static final String ROLE_BSL = "CRS_BSL";
    public static final String ROLE_TASL = "CRS_TASL";
    public static final String ROLE_ML = "CRS_ML";
    public static final String ROLE_MQM = "CRS_MQM";
    public static final String ROLE_CRSADMIN = "CRS_ADMIN";

    public static final Integer STATE_DRAFT = 1;
    public static final Integer STATE_REVIEW = 2;
    public static final Integer STATE_REVIEWED = 3;
    public static final Integer STATE_TASLAPPROVE = 4;
    public static final Integer STATE_MLAPPROVE = 5;
    public static final Integer STATE_APPROVED = 6;
    public static final Integer STATE_RETIRED = 7;
    public static final Integer STATE_ACTIVATED = 8;
    public static final Integer STATE_PUBLISHED = 9;

    public static final String STATUS_PENDING = "P";
    public static final String STATUS_CURRENT = "C";

    public static final String REVIEW_REQD_YES = "Y";
    public static final String REVIEW_REQD_NO = "N";

    public static final String BASE_FACET = "BASE";
    public static final String STAGING_FACET = "STAGING";

    public static final String PLSQL_CALL_SUCCESS = "0";
    public static final String PLSQL_CALL_FAILURE = "-1";

    static public String getCustomTimeStamp() {
        String timeStamp =
            new SimpleDateFormat("MM-dd-yyyy hh:mm a").format(Calendar.getInstance().getTime());
        return timeStamp;
    }
}
