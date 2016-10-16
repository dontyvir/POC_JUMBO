/**
 * ConsultaCarroCompraSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.tempuri;

public class ConsultaCarroCompraSoapBindingSkeleton implements org.tempuri.ConsultaCarroCompraSoapPort, org.apache.axis.wsdl.Skeleton {
    private org.tempuri.ConsultaCarroCompraSoapPort impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/action/", "ConsultaCarroCompra.Execute"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://tempuri.org/action/", ">ConsultaCarroCompra.Execute"), org.tempuri.ConsultaCarroCompraExecute.class), 
        };
        _oper = new org.apache.axis.description.OperationDesc("execute", _params, new javax.xml.namespace.QName("http://tempuri.org/action/", "ConsultaCarroCompra.ExecuteResponse"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://tempuri.org/action/", ">ConsultaCarroCompra.ExecuteResponse"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://tempuri.org/action/", "ConsultaCarroCompra.Execute"));
        _oper.setSoapAction("http://tempuri.org/action/action/ACONSULTACARROCOMPRA.Execute");
        _myOperationsList.add(_oper);
        if (_myOperations.get("execute") == null) {
            _myOperations.put("execute", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("execute")).add(_oper);
    }

    public ConsultaCarroCompraSoapBindingSkeleton() {
        this.impl = new org.tempuri.ConsultaCarroCompraSoapBindingImpl();
    }

    public ConsultaCarroCompraSoapBindingSkeleton(org.tempuri.ConsultaCarroCompraSoapPort impl) {
        this.impl = impl;
    }
    public org.tempuri.ConsultaCarroCompraExecuteResponse execute(org.tempuri.ConsultaCarroCompraExecute parameters) throws java.rmi.RemoteException
    {
        org.tempuri.ConsultaCarroCompraExecuteResponse ret = impl.execute(parameters);
        return ret;
    }

}
