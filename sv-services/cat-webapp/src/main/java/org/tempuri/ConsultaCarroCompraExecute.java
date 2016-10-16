/**
 * ConsultaCarroCompraExecute.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.tempuri;

public class ConsultaCarroCompraExecute  implements java.io.Serializable {
    private java.lang.String idcarrocompra;
    private cl.cencosud.botonpago.ConsultaCarroCompraRep consultacarrocompra;

    public ConsultaCarroCompraExecute() {
    }

    public java.lang.String getIdcarrocompra() {
        return idcarrocompra;
    }

    public void setIdcarrocompra(java.lang.String idcarrocompra) {
        this.idcarrocompra = idcarrocompra;
    }

    public cl.cencosud.botonpago.ConsultaCarroCompraRep getConsultacarrocompra() {
        return consultacarrocompra;
    }

    public void setConsultacarrocompra(cl.cencosud.botonpago.ConsultaCarroCompraRep consultacarrocompra) {
        this.consultacarrocompra = consultacarrocompra;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultaCarroCompraExecute)) return false;
        ConsultaCarroCompraExecute other = (ConsultaCarroCompraExecute) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((idcarrocompra==null && other.getIdcarrocompra()==null) || 
             (idcarrocompra!=null &&
              idcarrocompra.equals(other.getIdcarrocompra()))) &&
            ((consultacarrocompra==null && other.getConsultacarrocompra()==null) || 
             (consultacarrocompra!=null &&
              consultacarrocompra.equals(other.getConsultacarrocompra())));
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
        if (getIdcarrocompra() != null) {
            _hashCode += getIdcarrocompra().hashCode();
        }
        if (getConsultacarrocompra() != null) {
            _hashCode += getConsultacarrocompra().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultaCarroCompraExecute.class);

    static {
        org.apache.axis.description.FieldDesc field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("idcarrocompra");
        field.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/action/", "Idcarrocompra"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("consultacarrocompra");
        field.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/action/", "Consultacarrocompra"));
        field.setXmlType(new javax.xml.namespace.QName("http://botonpago.cencosud.cl", "ConsultaCarroCompraRep"));
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
