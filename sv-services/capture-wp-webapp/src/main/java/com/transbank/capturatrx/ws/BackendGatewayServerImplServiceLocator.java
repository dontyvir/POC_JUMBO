/**
 * BackendGatewayServerImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf250748.02 v122107130645
 */

package com.transbank.capturatrx.ws;

public class BackendGatewayServerImplServiceLocator extends com.ibm.ws.webservices.multiprotocol.AgnosticService implements com.ibm.ws.webservices.multiprotocol.GeneratedService, com.transbank.capturatrx.ws.BackendGatewayServerImplService {

    public BackendGatewayServerImplServiceLocator() {
        super(com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
           "http://ws.capturatrx.transbank.com/",
           "BackendGatewayServerImplService"));

        context.setLocatorName("com.transbank.capturatrx.ws.BackendGatewayServerImplServiceLocator");
    }

    public BackendGatewayServerImplServiceLocator(com.ibm.ws.webservices.multiprotocol.ServiceContext ctx) {
        super(ctx);
        context.setLocatorName("com.transbank.capturatrx.ws.BackendGatewayServerImplServiceLocator");
    }

    // Utilizar para obtener la clase de proxy para backendGatewayServerImplPort
    private final java.lang.String backendGatewayServerImplPort_address = "https://200.10.12.198/capturatrx/BackendGatewayServer";

    public java.lang.String getBackendGatewayServerImplPortAddress() {
        if (context.getOverriddingEndpointURIs() == null) {
            return backendGatewayServerImplPort_address;
        }
        String overriddingEndpoint = (String) context.getOverriddingEndpointURIs().get("BackendGatewayServerImplPort");
        if (overriddingEndpoint != null) {
            return overriddingEndpoint;
        }
        else {
            return backendGatewayServerImplPort_address;
        }
    }

    private java.lang.String backendGatewayServerImplPortPortName = "BackendGatewayServerImplPort";

    // The WSDD port name defaults to the port name.
    private java.lang.String backendGatewayServerImplPortWSDDPortName = "BackendGatewayServerImplPort";

    public java.lang.String getBackendGatewayServerImplPortWSDDPortName() {
        return backendGatewayServerImplPortWSDDPortName;
    }

    public void setBackendGatewayServerImplPortWSDDPortName(java.lang.String name) {
        backendGatewayServerImplPortWSDDPortName = name;
    }

    public com.transbank.capturatrx.ws.BackendGatewayServer getBackendGatewayServerImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(getBackendGatewayServerImplPortAddress());
        }
        catch (java.net.MalformedURLException e) {
            return null; // es poco probable ya que URL se ha validado en WSDL2Java
        }
        return getBackendGatewayServerImplPort(endpoint);
    }

    public com.transbank.capturatrx.ws.BackendGatewayServer getBackendGatewayServerImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        com.transbank.capturatrx.ws.BackendGatewayServer _stub =
            (com.transbank.capturatrx.ws.BackendGatewayServer) getStub(
                backendGatewayServerImplPortPortName,
                (String) getPort2NamespaceMap().get(backendGatewayServerImplPortPortName),
                com.transbank.capturatrx.ws.BackendGatewayServer.class,
                "com.transbank.capturatrx.ws.BackendGatewayServerImplServiceSoapBindingStub",
                portAddress.toString());
        if (_stub instanceof com.ibm.ws.webservices.engine.client.Stub) {
            ((com.ibm.ws.webservices.engine.client.Stub) _stub).setPortName(backendGatewayServerImplPortWSDDPortName);
        }
        return _stub;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.transbank.capturatrx.ws.BackendGatewayServer.class.isAssignableFrom(serviceEndpointInterface)) {
                return getBackendGatewayServerImplPort();
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("WSWS3273E: Error: No hay ninguna implementación de apéndice para la interfaz:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        String inputPortName = portName.getLocalPart();
        if ("BackendGatewayServerImplPort".equals(inputPortName)) {
            return getBackendGatewayServerImplPort();
        }
        else  {
            throw new javax.xml.rpc.ServiceException();
        }
    }

    public void setPortNamePrefix(java.lang.String prefix) {
        backendGatewayServerImplPortWSDDPortName = prefix + "/" + backendGatewayServerImplPortPortName;
    }

    public javax.xml.namespace.QName getServiceName() {
        return com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://ws.capturatrx.transbank.com/", "BackendGatewayServerImplService");
    }

    private java.util.Map port2NamespaceMap = null;

    protected synchronized java.util.Map getPort2NamespaceMap() {
        if (port2NamespaceMap == null) {
            port2NamespaceMap = new java.util.HashMap();
            port2NamespaceMap.put(
               "BackendGatewayServerImplPort",
               "http://schemas.xmlsoap.org/wsdl/soap/");
        }
        return port2NamespaceMap;
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            String serviceNamespace = getServiceName().getNamespaceURI();
            for (java.util.Iterator i = getPort2NamespaceMap().keySet().iterator(); i.hasNext(); ) {
                ports.add(
                    com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                        serviceNamespace,
                        (String) i.next()));
            }
        }
        return ports.iterator();
    }

    public javax.xml.rpc.Call[] getCalls(javax.xml.namespace.QName portName) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            throw new javax.xml.rpc.ServiceException("WSWS3062E: Error: portName no debe ser nulo.");
        }
        if  (portName.getLocalPart().equals("BackendGatewayServerImplPort")) {
            return new javax.xml.rpc.Call[] {
                createCall(portName, "capturaDiferida", "capturaDiferida"),
            };
        }
        else {
            throw new javax.xml.rpc.ServiceException("WSWS3062E: Error: portName no debe ser nulo.");
        }
    }
}
