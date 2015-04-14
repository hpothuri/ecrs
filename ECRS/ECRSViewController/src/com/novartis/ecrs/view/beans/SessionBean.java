package com.novartis.ecrs.view.beans;

import oracle.adf.share.ADFContext;
import oracle.adf.share.security.SecurityContext;

public class SessionBean {
    public SessionBean() {
        super();
    }
    
    private String userRole;

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserRole() {
        if(userRole == null){
            SecurityContext secCtx =
                ADFContext.getCurrent().getSecurityContext();
            String[] roles = secCtx.getUserRoles();
            if(roles != null){
                for(String role : roles){
                    if("ADMIN".equalsIgnoreCase(role) || "BSL".equalsIgnoreCase(role) || "MQM".equalsIgnoreCase(role) || "TASL".equalsIgnoreCase(role) || "ML".equalsIgnoreCase(role)){
                        userRole = role;
                        break;
                    }
                }
            }
        }
        return userRole;
    }
}
