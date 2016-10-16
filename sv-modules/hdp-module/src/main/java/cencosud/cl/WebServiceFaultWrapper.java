/**
 * WebServiceFaultWrapper.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf451234.02 v82812174117
 */

package cencosud.cl;

public class WebServiceFaultWrapper  extends java.lang.Exception  {
    private java.lang.String xmlError;

    public WebServiceFaultWrapper(
           java.lang.String xmlError) {
        this.xmlError = xmlError;
    }

    public java.lang.String getXmlError() {
        return xmlError;
    }

}
