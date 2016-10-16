/**
 * TrxInput_Deser.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf250748.02 v122107130645
 */

package com.transbank.capturatrx.ws;

public class TrxInput_Deser extends com.ibm.ws.webservices.engine.encoding.ser.BeanDeserializer {
    /**
     * Constructor
     */
    public TrxInput_Deser(
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType, 
           com.ibm.ws.webservices.engine.description.TypeDesc _typeDesc) {
        super(_javaType, _xmlType, _typeDesc);
    }
    /**
     * Create instance of java bean
     */
    public void createValue() {
        value = new com.transbank.capturatrx.ws.TrxInput();
    }
    protected boolean tryElementSetFromString(javax.xml.namespace.QName qName, java.lang.String strValue) {
        if (qName==QName_0_0) {
          ((TrxInput)value).setCodAut(strValue);
          return true;}
        else if (qName==QName_0_1) {
          ((TrxInput)value).setCodCred(strValue);
          return true;}
        else if (qName==QName_0_2) {
          ((TrxInput)value).setFechaAut(strValue);
          return true;}
        else if (qName==QName_0_3) {
          ((TrxInput)value).setFinTarjeta(strValue);
          return true;}
        else if (qName==QName_0_4) {
          ((TrxInput)value).setMontoCaptura(com.ibm.ws.webservices.engine.encoding.ser.SimpleDeserializer.parsedouble(strValue));
          return true;}
        else if (qName==QName_0_5) {
          ((TrxInput)value).setOrdenPedido(strValue);
          return true;}
        return false;
    }
    protected boolean tryAttributeSetFromString(javax.xml.namespace.QName qName, java.lang.String strValue) {
        return false;
    }
    protected boolean tryElementSetFromObject(javax.xml.namespace.QName qName, java.lang.Object objValue) {
        if (objValue == null) {
          return true;
        }
        return false;
    }
    protected boolean tryElementSetFromList(javax.xml.namespace.QName qName, java.util.List listValue) {
        return false;
    }
    private final static javax.xml.namespace.QName QName_0_5 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "ordenPedido");
    private final static javax.xml.namespace.QName QName_0_0 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "codAut");
    private final static javax.xml.namespace.QName QName_0_1 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "codCred");
    private final static javax.xml.namespace.QName QName_0_2 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "fechaAut");
    private final static javax.xml.namespace.QName QName_0_4 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "montoCaptura");
    private final static javax.xml.namespace.QName QName_0_3 = 
           com.ibm.ws.webservices.engine.utils.QNameTable.createQName(
                  "",
                  "finTarjeta");
}
