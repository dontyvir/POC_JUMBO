/**
 * BackendGatewayServerImplServiceSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf250748.02 v122107130645
 */

package com.transbank.capturatrx.ws;

public class BackendGatewayServerImplServiceSoapBindingStub extends com.ibm.ws.webservices.engine.client.Stub implements com.transbank.capturatrx.ws.BackendGatewayServer {
    public BackendGatewayServerImplServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws com.ibm.ws.webservices.engine.WebServicesFault {
        if (service == null) {
            super.service = new com.ibm.ws.webservices.engine.client.Service();
        }
        else {
            super.service = service;
        }
        super.engine = ((com.ibm.ws.webservices.engine.client.Service) super.service).getEngine();
        initTypeMapping();
        super.cachedEndpoint = endpointURL;
        super.connection = ((com.ibm.ws.webservices.engine.client.Service) super.service).getConnection(endpointURL);
        super.messageContexts = new com.ibm.ws.webservices.engine.MessageContext[1];
    }

    private void initTypeMapping() {
        javax.xml.rpc.encoding.TypeMapping tm = super.getTypeMapping(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
        java.lang.Class javaType = null;
        javax.xml.namespace.QName xmlType = null;
        javax.xml.namespace.QName compQName = null;
        javax.xml.namespace.QName compTypeQName = null;
        com.ibm.ws.webservices.engine.encoding.SerializerFactory sf = null;
        com.ibm.ws.webservices.engine.encoding.DeserializerFactory df = null;
        javaType = com.transbank.capturatrx.ws.TrxInput.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://ws.capturatrx.transbank.com/", "trxInput");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

        javaType = com.transbank.capturatrx.ws.TrxReturn.class;
        xmlType = com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://ws.capturatrx.transbank.com/", "trxReturn");
        sf = com.ibm.ws.webservices.engine.encoding.ser.BaseSerializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanSerializerFactory.class, javaType, xmlType);
        df = com.ibm.ws.webservices.engine.encoding.ser.BaseDeserializerFactory.createFactory(com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializerFactory.class, javaType, xmlType);
        if (sf != null || df != null) {
            tm.register(javaType, xmlType, sf, df);
        }

    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _capturaDiferidaOperation0 = null;
    private static com.ibm.ws.webservices.engine.description.OperationDesc _getcapturaDiferidaOperation0() {
        com.ibm.ws.webservices.engine.description.ParameterDesc[]  _params0 = new com.ibm.ws.webservices.engine.description.ParameterDesc[] {
         new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "arg0"), com.ibm.ws.webservices.engine.description.ParameterDesc.IN, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://ws.capturatrx.transbank.com/", "trxInput"), com.transbank.capturatrx.ws.TrxInput.class, false, false, false, true, true, false), 
          };
        _params0[0].setOption("partQNameString","{http://ws.capturatrx.transbank.com/}trxInput");
        _params0[0].setOption("partName","trxInput");
        _params0[0].setOption("inputPosition","0");
        com.ibm.ws.webservices.engine.description.ParameterDesc  _returnDesc0 = new com.ibm.ws.webservices.engine.description.ParameterDesc(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("", "return"), com.ibm.ws.webservices.engine.description.ParameterDesc.OUT, com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://ws.capturatrx.transbank.com/", "trxReturn"), com.transbank.capturatrx.ws.TrxReturn.class, true, false, false, true, true, false); 
        _returnDesc0.setOption("outputPosition","0");
        _returnDesc0.setOption("partQNameString","{http://ws.capturatrx.transbank.com/}trxReturn");
        _returnDesc0.setOption("partName","trxReturn");
        com.ibm.ws.webservices.engine.description.FaultDesc[]  _faults0 = new com.ibm.ws.webservices.engine.description.FaultDesc[] {
          };
        _capturaDiferidaOperation0 = new com.ibm.ws.webservices.engine.description.OperationDesc("capturaDiferida", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://ws.capturatrx.transbank.com/", "capturaDiferida"), _params0, _returnDesc0, _faults0, "");
        _capturaDiferidaOperation0.setOption("targetNamespace","http://ws.capturatrx.transbank.com/");
        _capturaDiferidaOperation0.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://ws.capturatrx.transbank.com/", "BackendGatewayServerImplService"));
        _capturaDiferidaOperation0.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://ws.capturatrx.transbank.com/", "BackendGatewayServer"));
        _capturaDiferidaOperation0.setOption("ResponseLocalPart","capturaDiferidaResponse");
        _capturaDiferidaOperation0.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://ws.capturatrx.transbank.com/", "capturaDiferidaResponse"));
        _capturaDiferidaOperation0.setOption("ResponseNamespace","http://ws.capturatrx.transbank.com/");
        _capturaDiferidaOperation0.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://ws.capturatrx.transbank.com/", "capturaDiferida"));
        _capturaDiferidaOperation0.setOption("outputName","capturaDiferidaResponse");
        _capturaDiferidaOperation0.setOption("inoutOrderingReq","false");
        _capturaDiferidaOperation0.setOption("inputName","capturaDiferida");
        _capturaDiferidaOperation0.setOption("buildNum","cf250748.02");
        _capturaDiferidaOperation0.setUse(com.ibm.ws.webservices.engine.enum.Use.LITERAL);
        _capturaDiferidaOperation0.setStyle(com.ibm.ws.webservices.engine.enum.Style.WRAPPED);
        return _capturaDiferidaOperation0;

    }

    private int _capturaDiferidaIndex0 = 0;
    private synchronized com.ibm.ws.webservices.engine.client.Stub.Invoke _getcapturaDiferidaInvoke0(Object[] parameters) throws com.ibm.ws.webservices.engine.WebServicesFault  {
        com.ibm.ws.webservices.engine.MessageContext mc = super.messageContexts[_capturaDiferidaIndex0];
        if (mc == null) {
            mc = new com.ibm.ws.webservices.engine.MessageContext(super.engine);
            mc.setOperation(BackendGatewayServerImplServiceSoapBindingStub._capturaDiferidaOperation0);
            mc.setUseSOAPAction(true);
            mc.setSOAPActionURI("");
            mc.setEncodingStyle(com.ibm.ws.webservices.engine.Constants.URI_LITERAL_ENC);
            mc.setProperty(com.ibm.ws.webservices.engine.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
            mc.setProperty(com.ibm.ws.webservices.engine.WebServicesEngine.PROP_DOMULTIREFS, Boolean.FALSE);
            super.primeMessageContext(mc);
            super.messageContexts[_capturaDiferidaIndex0] = mc;
        }
        try {
            mc = (com.ibm.ws.webservices.engine.MessageContext) mc.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw com.ibm.ws.webservices.engine.WebServicesFault.makeFault(cnse);
        }
        return new com.ibm.ws.webservices.engine.client.Stub.Invoke(connection, mc, parameters);
    }

    public com.transbank.capturatrx.ws.TrxReturn capturaDiferida(com.transbank.capturatrx.ws.TrxInput arg0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new com.ibm.ws.webservices.engine.NoEndPointException();
        }
        java.util.Vector _resp = null;
        try {
            _resp = _getcapturaDiferidaInvoke0(new java.lang.Object[] {arg0}).invoke();

        } catch (com.ibm.ws.webservices.engine.WebServicesFault wsf) {
            Exception e = wsf.getUserException();
            throw wsf;
        } 
        try {
            return (com.transbank.capturatrx.ws.TrxReturn) ((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue();
        } catch (java.lang.Exception _exception) {
            return (com.transbank.capturatrx.ws.TrxReturn) super.convert(((com.ibm.ws.webservices.engine.xmlsoap.ext.ParamValue) _resp.get(0)).getValue(), com.transbank.capturatrx.ws.TrxReturn.class);
        }
    }

    private static void _staticInit() {
        _capturaDiferidaOperation0 = _getcapturaDiferidaOperation0();
    }

    static {
       _staticInit();
    }
}
