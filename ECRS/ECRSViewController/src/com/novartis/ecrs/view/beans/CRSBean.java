package com.novartis.ecrs.view.beans;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class CRSBean implements Serializable {
    // ******** VERSION **********
    public static final String APP_VERSION = "01.07.00";
    public static final String APP_BUILD = "1";
    public static final String APP_BUILD_DATE = "30-NOV-2015";
    public static final String RESOURCE_BUNDLE_NAME = "eCRS";
    @SuppressWarnings("compatibility:-2309154444791405523")
    private static final long serialVersionUID = -719374587308101131L;
    private final String dbUrlString = "jdbc/EcrsDS";
    // ******** VERSION **********
    public static final Logger logger = Logger.getLogger(CRSBean.class);
    
    // DICTIONARIES
    private static String defaultFilterDictionaryShortName;
    private static String defaultBaseDictionaryShortName;
    private static Hashtable properties;
    private String initialize;
    private String version;

    public CRSBean() {
        super();
        logger.info ("****************************");
        logger.info ("*** LOADING ECRS PROPERTIES ***");
        logger.info ("***                      ***");
        
        loadProperties();
    }
    private Connection getConnection(){
        Connection dbConnection = null;
        if (null != dbConnection){
            return dbConnection;
        }else {
            try {
                InitialContext initialContext = new InitialContext();
                DataSource ds = (DataSource)initialContext.lookup(dbUrlString);
                dbConnection = ds.getConnection();
            } catch (NamingException e) {
                        e.printStackTrace();
            } catch (SQLException e) {
                        e.printStackTrace();
            }
        }
        return dbConnection;
    }
    private void loadProperties() {
            logger.info ("*** LOADING PROPERTIES   ***");
            Boolean propsLoaded = loadPropsFromDB ();            
            if (!propsLoaded) {
                logger.info("!!!! UNABLE TO LOAD CRS PROPERTIES !!!! ");
                return;
            }
            
            this.defaultFilterDictionaryShortName = getProperty("DEFAULT_FILTER_DICTIONARY_SHORT_NAME");
            this.defaultBaseDictionaryShortName = getProperty("DEFAULT_BASE_DICTIONARY_SHORT_NAME");
        }
    private boolean loadPropsFromDB () {
           
       if (properties == null) properties = new Hashtable();
       properties.clear();
       
       String sql = "SELECT * FROM CRS_PROPERTIES";
       Connection conn = getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
    
       try {
           pstmt = conn.prepareCall(sql);
           rs = pstmt.executeQuery();
       } catch (SQLException e) {
           return false;
       }
       try {
            String p, v = null;
            while (rs.next()) {
               p = rs.getString("PROP_NAME");
               v = rs.getString("PROP_VALUE") + "";
               
               logger.info ("*** " + p + "=" + v);
               properties.put(p,v);
            }
       } catch (SQLException e){
                logger.error("Error while loading CRS Properties", e);
            return false;
        } finally{
            try{
                rs.close();
                pstmt.close();
                conn.close();
            }catch (SQLException e){
                logger.error("Error while closing connection", e);
            }
        }
       return true;
       
    }
    public static String getProperty (String property) {
        String retVal = null;
        if (properties == null) return "";
        Object o = properties.get(property);
        if (o!=null) retVal = (String)o;
        return retVal;
        
    }

    public static String getDefaultFilterDictionaryShortName() {
        return defaultFilterDictionaryShortName;
    }

    public static String getDefaultBaseDictionaryShortName() {
        return defaultBaseDictionaryShortName;
    }

    public void setInitialize(String initialize) {
        this.initialize = initialize;
    }

    public String getInitialize() {
        initialize = "APP INIT";
        return initialize;
    }
    public String refreshProperties() {
        loadProperties();
        return null;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        version = "Version: " + APP_VERSION + "\nDate: " + APP_BUILD_DATE;
        return version;
    }
}
