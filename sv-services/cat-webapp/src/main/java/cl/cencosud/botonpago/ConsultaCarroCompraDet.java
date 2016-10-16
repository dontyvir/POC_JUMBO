/**
 * ConsultaCarroCompraDet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package cl.cencosud.botonpago;

public class ConsultaCarroCompraDet  implements java.io.Serializable {
    private int secuencia;
    private double monto;
    private java.lang.String nemoProveedor;
    private java.lang.String glosaServicio;

    public ConsultaCarroCompraDet() {
    }

    public int getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(int secuencia) {
        this.secuencia = secuencia;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public java.lang.String getNemoProveedor() {
        return nemoProveedor;
    }

    public void setNemoProveedor(java.lang.String nemoProveedor) {
        this.nemoProveedor = nemoProveedor;
    }

    public java.lang.String getGlosaServicio() {
        return glosaServicio;
    }

    public void setGlosaServicio(java.lang.String glosaServicio) {
        this.glosaServicio = glosaServicio;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultaCarroCompraDet)) return false;
        ConsultaCarroCompraDet other = (ConsultaCarroCompraDet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            secuencia == other.getSecuencia() &&
            monto == other.getMonto() &&
            ((nemoProveedor==null && other.getNemoProveedor()==null) || 
             (nemoProveedor!=null &&
              nemoProveedor.equals(other.getNemoProveedor()))) &&
            ((glosaServicio==null && other.getGlosaServicio()==null) || 
             (glosaServicio!=null &&
              glosaServicio.equals(other.getGlosaServicio())));
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
        _hashCode += getSecuencia();
        _hashCode += new Double(getMonto()).hashCode();
        if (getNemoProveedor() != null) {
            _hashCode += getNemoProveedor().hashCode();
        }
        if (getGlosaServicio() != null) {
            _hashCode += getGlosaServicio().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultaCarroCompraDet.class);

    static {
        org.apache.axis.description.FieldDesc field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("secuencia");
        field.setXmlName(new javax.xml.namespace.QName("http://botonpago.cencosud.cl", "secuencia"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("monto");
        field.setXmlName(new javax.xml.namespace.QName("http://botonpago.cencosud.cl", "monto"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("nemoProveedor");
        field.setXmlName(new javax.xml.namespace.QName("http://botonpago.cencosud.cl", "nemoProveedor"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("glosaServicio");
        field.setXmlName(new javax.xml.namespace.QName("http://botonpago.cencosud.cl", "glosaServicio"));
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
