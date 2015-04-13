package com.novartis.ecrs.ui.utility;

import java.util.Date;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;

import oracle.binding.BindingContainer;

import oracle.binding.OperationBinding;

import oracle.javatools.resourcebundle.BundleFactory;

import oracle.jbo.domain.Timestamp;

import org.apache.myfaces.trinidad.context.RequestContext;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class ADFUtils {
    public ADFUtils() {
        super();
    }

    /**
     * Programmatic evaluation of EL.
     *
     * @param el EL to evaluate
     * @return Result of the evaluation
     */
    public static Object evaluateEL(String el) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory =
            facesContext.getApplication().getExpressionFactory();
        ValueExpression exp =
            expressionFactory.createValueExpression(elContext, el,
                                                    Object.class);

        return exp.getValue(elContext);
    }

    /**
     * Programmatic invocation of a method that an EL evaluates to.
     * The method must not take any parameters.
     *
     * @param el EL of the method to invoke
     * @return Object that the method returns
     */
    public static Object invokeEL(String el) {

        return invokeEL(el, new Class[0], new Object[0]);
    }

    /**
     * Programmatic invocation of a method that an EL evaluates to.
     *
     * @param el EL of the method to invoke
     * @param paramTypes Array of Class defining the types of the parameters
     * @param params Array of Object defining the values of the parametrs
     * @return Object that the method returns
     */
    public static Object invokeEL(String el, Class[] paramTypes,
                                  Object[] params) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory =
            facesContext.getApplication().getExpressionFactory();
        MethodExpression exp =
            expressionFactory.createMethodExpression(elContext, el,
                                                     Object.class, paramTypes);

        return exp.invoke(elContext, params);
    }

    public static void setEL(String el, Object val) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory =
            facesContext.getApplication().getExpressionFactory();
        ValueExpression exp =
            expressionFactory.createValueExpression(elContext, el,
                                                    Object.class);

        exp.setValue(elContext, val);
    }

    /**
     * Find an iterator binding in the current binding container by name.
     *
     * @param name iterator binding name
     * @return iterator binding
     */
    public static DCIteratorBinding findIterator(String name) {
        DCIteratorBinding iter =
            getDCBindingContainer().findIteratorBinding(name);
        if (iter == null) {
            throw new RuntimeException("Iterator '" + name + "' not found");
        }
        return iter;
    }

    /**
     * Return the Binding Container as a DCBindingContainer.
     * @return current binding container as a DCBindingContainer
     */
    public static DCBindingContainer getDCBindingContainer() {
        return (DCBindingContainer)getBindingContainer();
    }

    /**
     * Return the current page's binding container.
     * @return the current page's binding container
     */
    public static BindingContainer getBindingContainer() {
        BindingContext bctx = BindingContext.getCurrent();
        return bctx.getCurrentBindingsEntry();
    }

    /**
     * Show popup.
     *
     * @param popupId the popup id
     */
    public static void showPopup(String popupId) {
        FacesContext context = null;
        ExtendedRenderKitService extRenderKitSrvc = null;
        StringBuilder script = null;

        context = FacesContext.getCurrentInstance();
        extRenderKitSrvc =
                Service.getRenderKitService(context, ExtendedRenderKitService.class);
        script = new StringBuilder();
        script.append("var popup = AdfPage.PAGE.findComponent('").append(popupId).append("'); ").append("popup.show();");
        extRenderKitSrvc.addScript(context, script.toString());
    }

    /**
     * Show popup.
     *
     * @param popupId the popup id
     */
    public void closePopup(String popupId) {
        FacesContext context = FacesContext.getCurrentInstance();

        ExtendedRenderKitService extRenderKitSrvc =
            Service.getRenderKitService(context,
                                        ExtendedRenderKitService.class);
        extRenderKitSrvc.addScript(context,
                                   "AdfPage.PAGE.findComponent('" + popupId +
                                   "').hide();");
    }

    static public void addPartialTarget(UIComponent component) {

        RequestContext adfContext;

        adfContext = RequestContext.getCurrentInstance();
        adfContext.addPartialTarget(component);
    }

    public static DCBindingContainer findBindingContainerByName(String name) {
        return getBidingContext().findBindingContainer(name);
    }

    public static BindingContext getBidingContext() {
        return BindingContext.getCurrent();
    }

    static public Timestamp getJBOTimeStamp() {
        Date date = new Date(System.currentTimeMillis());
        Timestamp timestampObj =
            new Timestamp(date);
        return timestampObj;
    }


    /**
     * This methods shows the message required on the UI with required severity
     * @param msg -- Message to be shown on the UI.
     */
    public static void showFacesMessage(String msg,
                                        FacesMessage.Severity severity) {
        FacesMessage message = new FacesMessage(severity, msg, "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public static void showFacesMessage(String msg,
                                        FacesMessage.Severity severity,
                                        UIComponent component) {
        FacesMessage message = new FacesMessage(severity, msg, "");
        FacesContext.getCurrentInstance().addMessage(component.getClientId(FacesContext.getCurrentInstance()),
                                                     message);
    }

    public static String getMessageFromBundle(String pkey, String pFile) {
        ResourceBundle rsBundle = BundleFactory.getBundle(pFile);
        String value = rsBundle.getString(pkey);
        return value;
    }


//    public static void main(String a[]){
//        System.out.println("-----"+ getSqlTimeStamp());
//    }
    
        /**
     * Find an operation binding in the current binding container by name.
     * @param name operation binding name
     * @return operation binding
     */
    public static OperationBinding findOperation(String name) {
        OperationBinding op =
            getDCBindingContainer().getOperationBinding(name);
        if (op == null) {
            throw new RuntimeException("Operation '" + name + "' not found");
        }
        return op;
    }

}
