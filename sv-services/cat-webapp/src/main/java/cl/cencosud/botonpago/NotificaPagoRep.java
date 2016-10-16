/**
 * NotificaPagoRep.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package cl.cencosud.botonpago;

public class NotificaPagoRep  implements java.io.Serializable {
    private int codigoResp;
    private java.lang.String glosaResp;

    public NotificaPagoRep() {
    }

    public int getCodigoResp() {
        return codigoResp;
    }

    public void setCodigoResp(int codigoResp) {
        this.codigoResp = codigoResp;
    }

    public java.lang.String getGlosaResp() {
        return glosaResp;
    }

    public void setGlosaResp(java.lang.String glosaResp) {
        this.glosaResp = glosaResp;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof NotificaPagoRep)) return false;
        NotificaPagoRep other = (NotificaPagoRep) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            codigoResp == other.getCodigoResp() &&
            ((glosaResp==null && other.getGlosaResp()==null) || 
             (glosaResp!=null &&
              glosaResp.equals(other.getGlosaResp())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += getCodigoResp();
        if (getGlosaResp() != null) {
            _hashCode += getGlosaResp().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NotificaPagoRep.class);

    static {
        org.apache.axis.description.FieldDesc field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("codigoResp");
        field.setXmlName(new javax.xml.namespace.QName("http://botonpago.cencosud.cl", "codigoResp"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("glosaResp");
        field.setXmlName(new javax.xml.namespace.QName("http://botonpago.cencosud.cl", "glosaResp"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
    };

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
