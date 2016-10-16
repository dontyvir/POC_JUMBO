/**
 * BackendGatewayServerImplServiceInformation.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf250748.02 v122107130645
 */

package com.transbank.capturatrx.ws;

public class BackendGatewayServerImplServiceInformation implements com.ibm.ws.webservices.multiprotocol.ServiceInformation {

    private static java.util.Map operationDescriptions;
    private static java.util.Map typeMappings;

    static {
         initOperationDescriptions();
         initTypeMappings();
    }

    private static void initOperationDescriptions() { 
        operationDescriptions = new java.util.HashMap();

        java.util.Map inner0 = new java.util.HashMap();

        java.util.List list0 = new java.util.ArrayList();
        inner0.put("capturaDiferida", list0);

        com.ibm.ws.webservices.engine.description.OperationDesc capturaDiferida0Op = _capturaDiferida0Op();
        list0.add(capturaDiferida0Op);

        operationDescriptions.put("BackendGatewayServerImplPort",inner0);
        operationDescriptions = java.util.Collections.unmodifiableMap(operationDescriptions);
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _capturaDiferida0Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc capturaDiferida0Op = null;
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
        capturaDiferida0Op = new com.ibm.ws.webservices.engine.description.OperationDesc("capturaDiferida", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://ws.capturatrx.transbank.com/", "capturaDiferida"), _params0, _returnDesc0, _faults0, null);
        capturaDiferida0Op.setOption("targetNamespace","http://ws.capturatrx.transbank.com/");
        capturaDiferida0Op.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://ws.capturatrx.transbank.com/", "BackendGatewayServerImplService"));
        capturaDiferida0Op.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://ws.capturatrx.transbank.com/", "BackendGatewayServer"));
        capturaDiferida0Op.setOption("ResponseLocalPart","capturaDiferidaResponse");
        capturaDiferida0Op.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://ws.capturatrx.transbank.com/", "capturaDiferidaResponse"));
        capturaDiferida0Op.setOption("ResponseNamespace","http://ws.capturatrx.transbank.com/");
        capturaDiferida0Op.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://ws.capturatrx.transbank.com/", "capturaDiferida"));
        capturaDiferida0Op.setOption("outputName","capturaDiferidaResponse");
        capturaDiferida0Op.setOption("inoutOrderingReq","false");
        capturaDiferida0Op.setOption("inputName","capturaDiferida");
        capturaDiferida0Op.setOption("buildNum","cf250748.02");
        capturaDiferida0Op.setStyle(com.ibm.ws.webservices.engine.enum.Style.WRAPPED);
        return capturaDiferida0Op;

    }


    private static void initTypeMappings() {
        typeMappings = new java.util.HashMap();
        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://ws.capturatrx.transbank.com/", "trxInput"),
                         com.transbank.capturatrx.ws.TrxInput.class);

        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://ws.capturatrx.transbank.com/", "trxReturn"),
                         com.transbank.capturatrx.ws.TrxReturn.class);

        typeMappings = java.util.Collections.unmodifiableMap(typeMappings);
    }

    public java.util.Map getTypeMappings() {
        return typeMappings;
    }

    public Class getJavaType(javax.xml.namespace.QName xmlName) {
        return (Class) typeMappings.get(xmlName);
    }

    public java.util.Map getOperationDescriptions(String portName) {
        return (java.util.Map) operationDescriptions.get(portName);
    }

    public java.util.List getOperationDescriptions(String portName, String operationName) {
        java.util.Map map = (java.util.Map) operationDescriptions.get(portName);
        if (map != null) {
            return (java.util.List) map.get(operationName);
        }
        return null;
    }

}
