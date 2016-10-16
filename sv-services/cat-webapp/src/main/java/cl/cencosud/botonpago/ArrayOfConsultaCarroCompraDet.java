/**
 * ArrayOfConsultaCarroCompraDet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package cl.cencosud.botonpago;

public class ArrayOfConsultaCarroCompraDet  implements java.io.Serializable {
    private cl.cencosud.botonpago.ConsultaCarroCompraDet[] consultaCarroCompraDet;

    public ArrayOfConsultaCarroCompraDet() {
    }

    public cl.cencosud.botonpago.ConsultaCarroCompraDet[] getConsultaCarroCompraDet() {
        return consultaCarroCompraDet;
    }

    public void setConsultaCarroCompraDet(cl.cencosud.botonpago.ConsultaCarroCompraDet[] consultaCarroCompraDet) {
        this.consultaCarroCompraDet = consultaCarroCompraDet;
    }

    public cl.cencosud.botonpago.ConsultaCarroCompraDet getConsultaCarroCompraDet(int i) {
        return consultaCarroCompraDet[i];
    }

    public void setConsultaCarroCompraDet(int i, cl.cencosud.botonpago.ConsultaCarroCompraDet value) {
        this.consultaCarroCompraDet[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfConsultaCarroCompraDet)) return false;
        ArrayOfConsultaCarroCompraDet other = (ArrayOfConsultaCarroCompraDet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((consultaCarroCompraDet==null && other.getConsultaCarroCompraDet()==null) || 
             (consultaCarroCompraDet!=null &&
              java.util.Arrays.equals(consultaCarroCompraDet, other.getConsultaCarroCompraDet())));
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
        if (getConsultaCarroCompraDet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getConsultaCarroCompraDet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getConsultaCarroCompraDet(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArrayOfConsultaCarroCompraDet.class);

    static {
        org.apache.axis.description.FieldDesc field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("consultaCarroCompraDet");
        field.setXmlName(new javax.xml.namespace.QName("http://botonpago.cencosud.cl", "ConsultaCarroCompraDet"));
        field.setMinOccursIs0(true);
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
