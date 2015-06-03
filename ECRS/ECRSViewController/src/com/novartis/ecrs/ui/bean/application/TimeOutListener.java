package com.novartis.ecrs.ui.bean.application;

import com.novartis.ecrs.ui.bean.ManageCRSBean;

import java.io.IOException;

import java.io.PrintWriter;

import java.util.Date;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.faces.event.ActionEvent;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.adf.share.logging.ADFLogger;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import weblogic.servlet.security.ServletAuthentication;

public class TimeOutListener implements Filter {

    private FilterConfig filterConfig = null;
    private String initParam = null;
    private static final Logger logger = Logger.getLogger(TimeOutListener.class);
    private String timeoutPage = "faces/Home";

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        logger.info("Init TimeOutListener");
        this.filterConfig = filterConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
                                                                                                           ServletException {
        logger.info("inside doFilter");
        if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
            HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            HttpServletResponse httpServletResponse =
                (HttpServletResponse)response; 

            if (isSessionControlRequiredForThisResource(httpServletRequest)) { 
                if (isSessionInvalid(httpServletRequest)) {
                    String timeoutUrl =
                        ((HttpServletRequest)httpServletRequest).getContextPath() + "/" + getTimeoutPage();

                    logger.info("session is invalid! redirecting to timeoutpage : " + timeoutUrl);
                    httpServletResponse.sendRedirect(timeoutUrl);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    /** * * session shouldn't be checked for some pages. For example: for timeout page.. * Since we're redirecting to timeout page from this filter, * if we don't disable session control for it, filter will again redirect to it * and this will be result with an infinite loop... */
    private boolean isSessionControlRequiredForThisResource(HttpServletRequest httpServletRequest) {
        String requestPath = httpServletRequest.getRequestURI();
        logger.info("Request URI - "+requestPath);
        boolean controlRequired = !StringUtils.contains(requestPath, getTimeoutPage());
        return controlRequired;
    }

    private boolean isSessionInvalid(HttpServletRequest httpServletRequest) {  
        logger.info("httpServletRequest.getRequestedSessionId() -"+httpServletRequest.getRequestedSessionId());
        logger.info("httpServletRequest.isRequestedSessionIdValid() -"+httpServletRequest.isRequestedSessionIdValid());
        logger.info("httpServletRequest.getSession().getId() -"+httpServletRequest.getSession().getId());
        logger.info("httpServletRequest.getSession().getId() equal to httpServletRequest.getRequestedSessionId() -"+httpServletRequest.getSession().getId().equals(httpServletRequest.getRequestedSessionId()));
        logger.info("httpServletRequest.getUserPrincipal()"+httpServletRequest.getUserPrincipal());
                
        boolean sessionInValid =
            (httpServletRequest.getRequestedSessionId() != null) && !httpServletRequest.isRequestedSessionIdValid();        
        
        // chk if anonymous role and current page is a protected one
        if(!sessionInValid)
        sessionInValid =
                httpServletRequest.getUserPrincipal() == null && !(httpServletRequest.getRequestURI().contains("BrowseSearch") ||
                                                                   httpServletRequest.getRequestURI().contains("MedDRAComponentsReport") ||
                                                                   httpServletRequest.getRequestURI().contains("RiskDefinitionsSafetyTopicReport"));
        
        logger.info("is session invalid - "+ sessionInValid);
        return sessionInValid;
    }


    public void setTimeoutPage(String timeoutPage) {
        this.timeoutPage = timeoutPage;
    }

    public String getTimeoutPage() {
        return timeoutPage;
    }
}

