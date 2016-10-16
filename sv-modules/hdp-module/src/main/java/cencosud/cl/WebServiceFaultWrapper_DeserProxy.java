/**
 * WebServiceFaultWrapper_DeserProxy.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * cf451234.02 v82812174117
 */

package cencosud.cl;

public class WebServiceFaultWrapper_DeserProxy  extends java.lang.Exception  {
    private java.lang.String xmlError;

    public WebServiceFaultWrapper_DeserProxy() {
    }

    public java.lang.String getXmlError() {
        return xmlError;
    }

    public void setXmlError(java.lang.String xmlError) {
        this.xmlError = xmlError;
    }

    public java.lang.Object convert() {
      cencosud.cl.WebServiceFaultWrapper _e;
      _e = new cencosud.cl.WebServiceFaultWrapper(
        getXmlError());
      return _e;
    }
}
