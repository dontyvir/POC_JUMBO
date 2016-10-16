/**
 * FopLocator.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf451234.02 v82812174117
 */

package cencosud.cl;

public class FopLocator extends com.ibm.ws.webservices.multiprotocol.AgnosticService implements com.ibm.ws.webservices.multiprotocol.GeneratedService, cencosud.cl.Fop {

    public FopLocator() {
        super(com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
           "http://cl.cencosud/paymentHub/services",
           "Fop"));

        context.setLocatorName("cencosud.cl.FopLocator");
    }

    public FopLocator(com.ibm.ws.webservices.multiprotocol.ServiceContext ctx) {
        super(ctx);
        context.setLocatorName("cencosud.cl.FopLocator");
    }

    // Utilizar para obtener la clase de proxy para fopServicePort
    private final java.lang.String fopServicePort_address = "http://pagos.qa.cencosud.com:80/payment_hub/services/soap/v2/FopService";

    public java.lang.String getFopServicePortAddress() {
        if (context.getOverriddingEndpointURIs() == null) {
            return fopServicePort_address;
        }
        String overriddingEndpoint = (String) context.getOverriddingEndpointURIs().get("FopServicePort");
        if (overriddingEndpoint != null) {
            return overriddingEndpoint;
        }
        else {
            return fopServicePort_address;
        }
    }

    private java.lang.String fopServicePortPortName = "FopServicePort";

    // The WSDD port name defaults to the port name.
    private java.lang.String fopServicePortWSDDPortName = "FopServicePort";

    public java.lang.String getFopServicePortWSDDPortName() {
        return fopServicePortWSDDPortName;
    }

    public void setFopServicePortWSDDPortName(java.lang.String name) {
        fopServicePortWSDDPortName = name;
    }

    public cencosud.cl.FopService getFopServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(getFopServicePortAddress());
        }
        catch (java.net.MalformedURLException e) {
            return null; // es poco probable ya que URL se ha validado en WSDL2Java
        }
        return getFopServicePort(endpoint);
    }

    public cencosud.cl.FopService getFopServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        cencosud.cl.FopService _stub =
            (cencosud.cl.FopService) getStub(
                fopServicePortPortName,
                (String) getPort2NamespaceMap().get(fopServicePortPortName),
                cencosud.cl.FopService.class,
                "cencosud.cl.FopServicePortBindingStub",
                portAddress.toString());
        if (_stub instanceof com.ibm.ws.webservices.engine.client.Stub) {
            ((com.ibm.ws.webservices.engine.client.Stub) _stub).setPortName(fopServicePortWSDDPortName);
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
            if (cencosud.cl.FopService.class.isAssignableFrom(serviceEndpointInterface)) {
                return getFopServicePort();
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
        if ("FopServicePort".equals(inputPortName)) {
            return getFopServicePort();
        }
        else  {
            throw new javax.xml.rpc.ServiceException();
        }
    }

    public void setPortNamePrefix(java.lang.String prefix) {
        fopServicePortWSDDPortName = prefix + "/" + fopServicePortPortName;
    }

    public javax.xml.namespace.QName getServiceName() {
        return com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://cl.cencosud/paymentHub/services", "Fop");
    }

    private java.util.Map port2NamespaceMap = null;

    protected synchronized java.util.Map getPort2NamespaceMap() {
        if (port2NamespaceMap == null) {
            port2NamespaceMap = new java.util.HashMap();
            port2NamespaceMap.put(
               "FopServicePort",
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
        if  (portName.getLocalPart().equals("FopServicePort")) {
            return new javax.xml.rpc.Call[] {
                createCall(portName, "get", "null"),
            };
        }
        else {
            throw new javax.xml.rpc.ServiceException("WSWS3062E: Error: portName no debe ser nulo.");
        }
    }
}
