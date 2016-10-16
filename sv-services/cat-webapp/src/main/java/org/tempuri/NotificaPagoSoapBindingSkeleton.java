/**
 * NotificaPagoSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.tempuri;

public class NotificaPagoSoapBindingSkeleton implements org.tempuri.NotificaPagoSoapPort, org.apache.axis.wsdl.Skeleton {
    private org.tempuri.NotificaPagoSoapPort impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/action/", "NotificaPago.Execute"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://tempuri.org/action/", ">NotificaPago.Execute"), org.tempuri.NotificaPagoExecute.class), 
        };
        _oper = new org.apache.axis.description.OperationDesc("execute", _params, new javax.xml.namespace.QName("http://tempuri.org/action/", "NotificaPago.ExecuteResponse"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/action/", ">NotificaPago.ExecuteResponse"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/action/", "NotificaPago.Execute"));
        _oper.setSoapAction("http://tempuri.org/action/action/ANOTIFICAPAGO.Execute");
        _myOperationsList.add(_oper);
        if (_myOperations.get("execute") == null) {
            _myOperations.put("execute", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("execute")).add(_oper);
    }

    public NotificaPagoSoapBindingSkeleton() {
        this.impl = new org.tempuri.NotificaPagoSoapBindingImpl();
    }

    public NotificaPagoSoapBindingSkeleton(org.tempuri.NotificaPagoSoapPort impl) {
        this.impl = impl;
    }
    public org.tempuri.NotificaPagoExecuteResponse execute(org.tempuri.NotificaPagoExecute parameters) throws java.rmi.RemoteException
    {
        org.tempuri.NotificaPagoExecuteResponse ret = impl.execute(parameters);
        return ret;
    }

}
