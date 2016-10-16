/**
 * ConsultaCarroCompraLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.tempuri;

public class ConsultaCarroCompraLocator extends org.apache.axis.client.Service implements org.tempuri.ConsultaCarroCompra {

    // Use to get a proxy class for ConsultaCarroCompraSoapPort
    private final java.lang.String ConsultaCarroCompraSoapPort_address = "http://localhost:9080/WS_CAT/services/ConsultaCarroCompraSoapPort";

    public java.lang.String getConsultaCarroCompraSoapPortAddress() {
        return ConsultaCarroCompraSoapPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ConsultaCarroCompraSoapPortWSDDServiceName = "ConsultaCarroCompraSoapPort";

    public java.lang.String getConsultaCarroCompraSoapPortWSDDServiceName() {
        return ConsultaCarroCompraSoapPortWSDDServiceName;
    }

    public void setConsultaCarroCompraSoapPortWSDDServiceName(java.lang.String name) {
        ConsultaCarroCompraSoapPortWSDDServiceName = name;
    }

    public org.tempuri.ConsultaCarroCompraSoapPort getConsultaCarroCompraSoapPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ConsultaCarroCompraSoapPort_address);
        }
        catch (java.net.MalformedURLException e) {
            return null; // unlikely as URL was validated in WSDL2Java
        }
        return getConsultaCarroCompraSoapPort(endpoint);
    }

    public org.tempuri.ConsultaCarroCompraSoapPort getConsultaCarroCompraSoapPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tempuri.ConsultaCarroCompraSoapBindingStub _stub = new org.tempuri.ConsultaCarroCompraSoapBindingStub(portAddress, this);
            _stub.setPortName(getConsultaCarroCompraSoapPortWSDDServiceName());
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
            if (org.tempuri.ConsultaCarroCompraSoapPort.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tempuri.ConsultaCarroCompraSoapBindingStub _stub = new org.tempuri.ConsultaCarroCompraSoapBindingStub(new java.net.URL(ConsultaCarroCompraSoapPort_address), this);
                _stub.setPortName(getConsultaCarroCompraSoapPortWSDDServiceName());
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
        return new javax.xml.namespace.QName("http://tempuri.org/action/", "ConsultaCarroCompra");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("ConsultaCarroCompraSoapPort"));
        }
        return ports.iterator();
    }

}
