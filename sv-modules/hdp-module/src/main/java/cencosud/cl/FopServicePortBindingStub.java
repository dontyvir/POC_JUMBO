/**
 * FopServicePortBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf451234.02 v82812174117
 */

package cencosud.cl;

public class FopServicePortBindingStub extends com.ibm.ws.webservices.engine.client.Stub implements cencosud.cl.FopService {
    public FopServicePortBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws com.ibm.ws.webservices.engine.WebServicesFault {
        if (service == null) {
            super.service = new com.ibm.ws.webservices.engine.client.Service();
        }
        else {
            super.service = service;
        }
        super.engine = ((com.ibm.ws.webservices.engine.client.Service) super.service).getEngine();
        super.messageContexts = new com.ibm.ws.webservices.engine.MessageContext[1];
        java.lang.String theOption = (java.lang.String)super._getProperty("lastStubMapping");
        if (theOption == null || !theOption.equals("cencosud.cl.FopServicePortBinding")) {
                initTypeMapping();
                super._setProperty("lastStubMapping","cencosud.cl.FopServicePortBinding");
        }
        super.cachedEndpoint = endpointURL;
        super.connection = ((com.ibm.ws.webservices.engine.client.Service) super.service).getConnection(endpointURL);
    }

    private void initTypeMapping() {
        javax.xml.rpc.encoding.TypeMapping tm = super.getTypeMapping(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
        java.lang.Class javaType = null;
        javax.xml.namespace.QName xmlType = null;
        javax.xml.namespace.QName compQName = null;
        javax.xml.namespace.QName compTypeQName = null;
        com.ibm.ws.webservices.engine.encoding.SerializerFactory sf = null;
        com.ibm.ws.webservices.engine.encoding.DeserializerFactory df = null;
        javaType = cencosud.cl.WebServiceFaultWrapper.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://cl.cencosud/paymentHub/services", "webServiceFaultWrapper");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _getOperation0 = null;
    private static com.ibm.ws.webservices.engine.description.OperationDesc _getgetOperation0() {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "request"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false, false, true, true, false), 
          };
        _params0[0].setOption("inputPosition","0");
        _params0[0].setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}string");
        _params0[0].setOption("partName","string");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc0 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "return"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, true, false, false, true, true, false); 
        _returnDesc0.setOption("outputPosition","0");
        _returnDesc0.setOption("partQNameString","{http://www.w3.org/2001/XMLSchema}string");
        _returnDesc0.setOption("partName","string");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults0 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
         new com.ibm.ws.webservices.engine.description.FaultDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://cl.cencosud/paymentHub/services", "webServiceFaultWrapper"), com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://cl.cencosud/paymentHub/services", "WebServiceFault"), "cencosud.cl.WebServiceFaultWrapper", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://cl.cencosud/paymentHub/services", "WebServiceFault"), com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://cl.cencosud/paymentHub/services", "webServiceFaultWrapper")), 
          };
        _getOperation0 = new com.ibm.ws.webservices.engine.description.OperationDesc("get", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://cl.cencosud/paymentHub/services", "get"), _params0, _returnDesc0, _faults0, "");
        _getOperation0.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://cl.cencosud/paymentHub/services", "FopService"));
        _getOperation0.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://cl.cencosud/paymentHub/services", "getResponse"));
        _getOperation0.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://cl.cencosud/paymentHub/services", "Fop"));
        _getOperation0.setOption("buildNum","cf451234.02");
        _getOperation0.setOption("ResponseNamespace","http://cl.cencosud/paymentHub/services");
        _getOperation0.setOption("targetNamespace","http://cl.cencosud/paymentHub/services");
        _getOperation0.setOption("ResponseLocalPart","getResponse");
        _getOperation0.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://cl.cencosud/paymentHub/services", "get"));
        _getOperation0.setUse(com.ibm.ws.webservices.engine.enum.Use.LITERAL);
        _getOperation0.setStyle(com.ibm.ws.webservices.engine.enum.Style.WRAPPED);
        return _getOperation0;

    }

    private int _getIndex0 = 0;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getgetInvoke0(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_getIndex0];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(FopServicePortBindingStub._getOperation0);
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.ws.webservices.engine.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
            mc.setProperty(com.ibm.ws.webservices.engine.WebServicesEngine.PROP_DOMULTIREFS, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_getIndex0] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public java.lang.String get(java.lang.String request) throws java.rmi.RemoteException, cencosud.cl.WebServiceFaultWrapper {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getgetInvoke0(new java.lang.Object[] {request}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            Exception e = wsf.getUserException();
            if (e != null) {
                if (e instanceof cencosud.cl.WebServiceFaultWrapper) {
                    throw (cencosud.cl.WebServiceFaultWrapper) e;
                }
            }
            throw wsf;
        } 
        try {
            return (java.lang.String) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue();
        } catch (java.lang.Exception _exception) {
            return (java.lang.String) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), java.lang.String.class);
        }
    }

    private static void _staticInit() {
        _getOperation0 = _getgetOperation0();
    }

    static {
       _staticInit();
    }
}
