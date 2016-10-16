/**
 * ConsultaCarroCompraRep.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package cl.cencosud.botonpago;

public class ConsultaCarroCompraRep  implements java.io.Serializable {
    private int codigoResp;
    private java.lang.String glosaResp;
    private java.lang.String idCarroCompra;
    private ConsultaCarroCompraDet[] detalleCarroCompra;

    public ConsultaCarroCompraRep() {
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

    public java.lang.String getIdCarroCompra() {
        return idCarroCompra;
    }

    public void setIdCarroCompra(java.lang.String idCarroCompra) {
        this.idCarroCompra = idCarroCompra;
    }

    public ConsultaCarroCompraDet[] getDetalleCarroCompra() {
        return detalleCarroCompra;
    }

    public void setDetalleCarroCompra(ConsultaCarroCompraDet[] detalleCarroCompra) {
        this.detalleCarroCompra = detalleCarroCompra;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultaCarroCompraRep)) return false;
        ConsultaCarroCompraRep other = (ConsultaCarroCompraRep) obj;
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
              glosaResp.equals(other.getGlosaResp()))) &&
            ((idCarroCompra==null && other.getIdCarroCompra()==null) || 
             (idCarroCompra!=null &&
              idCarroCompra.equals(other.getIdCarroCompra()))) &&
            ((detalleCarroCompra==null && other.getDetalleCarroCompra()==null) || 
             (detalleCarroCompra!=null &&
              detalleCarroCompra.equals(other.getDetalleCarroCompra())));
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
        if (getIdCarroCompra() != null) {
            _hashCode += getIdCarroCompra().hashCode();
        }
        if (getDetalleCarroCompra() != null) {
            _hashCode += getDetalleCarroCompra().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultaCarroCompraRep.class);

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
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("idCarroCompra");
        field.setXmlName(new javax.xml.namespace.QName("http://botonpago.cencosud.cl", "idCarroCompra"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("detalleCarroCompra");
        field.setXmlName(new javax.xml.namespace.QName("http://botonpago.cencosud.cl", "detalleCarroCompra"));
        field.setXmlType(new javax.xml.namespace.QName("http://botonpago.cencosud.cl", "ArrayOfConsultaCarroCompraDet"));
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
