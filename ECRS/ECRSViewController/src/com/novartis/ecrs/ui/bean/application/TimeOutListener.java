package com.novartis.ecrs.ui.bean.application;

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

import weblogic.servlet.security.ServletAuthentication;

public class TimeOutListener implements Filter {

    private FilterConfig filterConfig = null;
    private String initParam = null;


    /**
     * {@inheritDoc}
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
//        logger.info("Init SessionExpiredFilter.");
        this.filterConfig = filterConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        filterConfig = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain) throws IOException,
                                                         ServletException {
//        logger.fine("Check if the session is valid.");
        final HttpServletRequest httpServletRequest =
            (HttpServletRequest)request;
        final String adfCtrlState = request.getParameter("_adf.ctrl-state");
        final String requestedSession =
            httpServletRequest.getRequestedSessionId();
        final String currentWebSession =
            httpServletRequest.getSession().getId();

//        logger.fine("Filter info: \n" +
//                " adfCtrolState: " + adfCtrlState + "\n" +
//                " requestedSession: " + requestedSession + "\n" +
//                " currentWebSession " + currentWebSession);

        if (requestedSession != null && !currentWebSession.equalsIgnoreCase(requestedSession)) {
//            logger.info("Inside session invalid");
//            Long inMillis = System.currentTimeMillis();
//            Date date = new Date(inMillis);
//            logger.info("current time---------->"+date);
//            String serverRelativeUrl = httpServletRequest.getContextPath();
//            serverRelativeUrl = serverRelativeUrl.substring(0, serverRelativeUrl.lastIndexOf("/"));
//            serverRelativeUrl = serverRelativeUrl + initParam;   
//            logger.info("Server relative URL :: "+serverRelativeUrl);
//            ((HttpServletResponse)response).sendRedirect(serverRelativeUrl);
            logout();
        } else {
            chain.doFilter(request, response);
        }
    }
    
    private void logout() {

        FacesContext fctx = FacesContext.getCurrentInstance();

        ExternalContext ectx = fctx.getExternalContext();

    //        accessLogger.info("LOGING OFF USER: " + ectx.getRemoteUser());

        String url = ectx.getRequestContextPath() + "/adfAuthentication?logout=true&end_url=/faces/Home";
        try {
            ectx.redirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fctx.responseComplete();

    }
}

