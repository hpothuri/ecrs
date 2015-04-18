package com.novartis.ecrs.model.constants;

public class ModelConstants {
    public ModelConstants() {
        super();
    }
    
    //public static String 
    public static final String ROLE_BSL = "BSL";
    public static final String ROLE_TASL = "TASL";
    public static final String ROLE_ML = "ML";
    public static final String ROLE_MQM = "MQM";
    
    public static final Integer STATE_DRAFT = 1;
    public static final Integer STATE_REVIEW = 2;
    public static final Integer STATE_REVIEWED = 3;
    public static final Integer STATE_TASLAPPROVE = 4;
    public static final Integer STATE_MLAPPROVE = 5;
    public static final Integer STATE_APPROVED = 6;
    public static final Integer STATE_RETIRED = 7;
    public static final Integer STATE_ACTIVATED = 8;
    public static final Integer STATE_PUBLISHED = 9;
}
