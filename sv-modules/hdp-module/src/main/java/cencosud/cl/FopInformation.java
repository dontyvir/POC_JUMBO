/**
 * FopInformation.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf451234.02 v82812174117
 */

package cencosud.cl;

public class FopInformation implements com.ibm.ws.webservices.multiprotocol.ServiceInformation {
	org.jboss.ws.common.
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
        inner0.put("get", list0);

        //com.ibm.ws.webservices.engine.description.OperationDesc get0Op = _get0Op();
         get0Op = _get0Op();
        list0.add(get0Op);

        operationDescriptions.put("FopServicePort",inner0);
        operationDescriptions = java.util.Collections.unmodifiableMap(operationDescriptions);
    }

    private static com.ibm.ws.webservices.engine.description.OperationDesc _get0Op() {
        com.ibm.ws.webservices.engine.description.OperationDesc get0Op = null;
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
        get0Op = new com.ibm.ws.webservices.engine.description.OperationDesc("get", com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://cl.cencosud/paymentHub/services", "get"), _params0, _returnDesc0, _faults0, null);
        get0Op.setOption("portTypeQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://cl.cencosud/paymentHub/services", "FopService"));
        get0Op.setOption("outputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://cl.cencosud/paymentHub/services", "getResponse"));
        get0Op.setOption("ServiceQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://cl.cencosud/paymentHub/services", "Fop"));
        get0Op.setOption("buildNum","cf451234.02");
        get0Op.setOption("ResponseNamespace","http://cl.cencosud/paymentHub/services");
        get0Op.setOption("targetNamespace","http://cl.cencosud/paymentHub/services");
        get0Op.setOption("ResponseLocalPart","getResponse");
        get0Op.setOption("inputMessageQName",com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://cl.cencosud/paymentHub/services", "get"));
        get0Op.setStyle(com.ibm.ws.webservices.engine.enum.Style.WRAPPED);
        return get0Op;

    }


    private static void initTypeMappings() {
        typeMappings = new java.util.HashMap();
        typeMappings.put(com.ibm.ws.webservices.engine.utils.QNameTable.createQName("http://cl.cencosud/paymentHub/services", "webServiceFaultWrapper"),
                         cencosud.cl.WebServiceFaultWrapper.class);

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
