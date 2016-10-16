/**
 * NotificaPagoLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.tempuri;

public class NotificaPagoLocator extends org.apache.axis.client.Service implements org.tempuri.NotificaPago {

    // Use to get a proxy class for NotificaPagoSoapPort
    //private final java.lang.String NotificaPagoSoapPort_address = "https://www.cencosud.com/wscencosud/service/anotificapago";
    private final java.lang.String NotificaPagoSoapPort_address = "http://localhost:9080/WS_CAT/services/NotificaPagoSoapPort";
    
    //http://localhost:9080/WS_CAT/services/ConsultaCarroCompraSoapPort
    
    public java.lang.String getNotificaPagoSoapPortAddress() {
        return NotificaPagoSoapPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String NotificaPagoSoapPortWSDDServiceName = "NotificaPagoSoapPort";

    public java.lang.String getNotificaPagoSoapPortWSDDServiceName() {
        return NotificaPagoSoapPortWSDDServiceName;
    }

    public void setNotificaPagoSoapPortWSDDServiceName(java.lang.String name) {
        NotificaPagoSoapPortWSDDServiceName = name;
    }

    public org.tempuri.NotificaPagoSoapPort getNotificaPagoSoapPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NotificaPagoSoapPort_address);
        }
        catch (java.net.MalformedURLException e) {
            return null; // unlikely as URL was validated in WSDL2Java
        }
        return getNotificaPagoSoapPort(endpoint);
    }

    public org.tempuri.NotificaPagoSoapPort getNotificaPagoSoapPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tempuri.NotificaPagoSoapBindingStub _stub = new org.tempuri.NotificaPagoSoapBindingStub(portAddress, this);
            _stub.setPortName(getNotificaPagoSoapPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.tempuri.NotificaPagoSoapPort.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tempuri.NotificaPagoSoapBindingStub _stub = new org.tempuri.NotificaPagoSoapBindingStub(new java.net.URL(NotificaPagoSoapPort_address), this);
                _stub.setPortName(getNotificaPagoSoapPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        java.rmi.Remote _stub = getPort(serviceEndpointInterface);
        ((org.apache.axis.client.Stub) _stub).setPortName(portName);
        return _stub;
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/action/", "NotificaPago");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("NotificaPagoSoapPort"));
        }
        return ports.iterator();
    }

}
