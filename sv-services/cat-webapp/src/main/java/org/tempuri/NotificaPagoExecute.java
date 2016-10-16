/**
 * NotificaPagoExecute.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.tempuri;

public class NotificaPagoExecute  implements java.io.Serializable {
    private java.lang.String idcarrocompra;
    private java.lang.String tipoautorizacion;
    private java.lang.String codigoautorizacion;
    private java.util.Calendar fechahoraautorizacion;
    private java.lang.String numerocuotas;
    private java.lang.String montocuota;
    private java.lang.String montooperacion;
    private cl.cencosud.botonpago.NotificaPagoRep notificapago;

    public NotificaPagoExecute() {
    }

    public java.lang.String getIdcarrocompra() {
        return idcarrocompra;
    }

    public void setIdcarrocompra(java.lang.String idcarrocompra) {
        this.idcarrocompra = idcarrocompra;
    }

    public java.lang.String getTipoautorizacion() {
        return tipoautorizacion;
    }

    public void setTipoautorizacion(java.lang.String tipoautorizacion) {
        this.tipoautorizacion = tipoautorizacion;
    }

    public java.lang.String getCodigoautorizacion() {
        return codigoautorizacion;
    }

    public void setCodigoautorizacion(java.lang.String codigoautorizacion) {
        this.codigoautorizacion = codigoautorizacion;
    }

    public java.util.Calendar getFechahoraautorizacion() {
        return fechahoraautorizacion;
    }

    public void setFechahoraautorizacion(java.util.Calendar fechahoraautorizacion) {
        this.fechahoraautorizacion = fechahoraautorizacion;
    }

    public java.lang.String getNumerocuotas() {
        return numerocuotas;
    }

    public void setNumerocuotas(java.lang.String numerocuotas) {
        this.numerocuotas = numerocuotas;
    }

    public java.lang.String getMontocuota() {
        return montocuota;
    }

    public void setMontocuota(java.lang.String montocuota) {
        this.montocuota = montocuota;
    }

    public java.lang.String getMontooperacion() {
        return montooperacion;
    }

    public void setMontooperacion(java.lang.String montooperacion) {
        this.montooperacion = montooperacion;
    }

    public cl.cencosud.botonpago.NotificaPagoRep getNotificapago() {
        return notificapago;
    }

    public void setNotificapago(cl.cencosud.botonpago.NotificaPagoRep notificapago) {
        this.notificapago = notificapago;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof NotificaPagoExecute)) return false;
        NotificaPagoExecute other = (NotificaPagoExecute) obj;
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
            ((tipoautorizacion==null && other.getTipoautorizacion()==null) || 
             (tipoautorizacion!=null &&
              tipoautorizacion.equals(other.getTipoautorizacion()))) &&
            ((codigoautorizacion==null && other.getCodigoautorizacion()==null) || 
             (codigoautorizacion!=null &&
              codigoautorizacion.equals(other.getCodigoautorizacion()))) &&
            ((fechahoraautorizacion==null && other.getFechahoraautorizacion()==null) || 
             (fechahoraautorizacion!=null &&
              fechahoraautorizacion.equals(other.getFechahoraautorizacion()))) &&
            ((numerocuotas==null && other.getNumerocuotas()==null) || 
             (numerocuotas!=null &&
              numerocuotas.equals(other.getNumerocuotas()))) &&
            ((montocuota==null && other.getMontocuota()==null) || 
             (montocuota!=null &&
              montocuota.equals(other.getMontocuota()))) &&
            ((montooperacion==null && other.getMontooperacion()==null) || 
             (montooperacion!=null &&
              montooperacion.equals(other.getMontooperacion()))) &&
            ((notificapago==null && other.getNotificapago()==null) || 
             (notificapago!=null &&
              notificapago.equals(other.getNotificapago())));
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
        if (getTipoautorizacion() != null) {
            _hashCode += getTipoautorizacion().hashCode();
        }
        if (getCodigoautorizacion() != null) {
            _hashCode += getCodigoautorizacion().hashCode();
        }
        if (getFechahoraautorizacion() != null) {
            _hashCode += getFechahoraautorizacion().hashCode();
        }
        if (getNumerocuotas() != null) {
            _hashCode += getNumerocuotas().hashCode();
        }
        if (getMontocuota() != null) {
            _hashCode += getMontocuota().hashCode();
        }
        if (getMontooperacion() != null) {
            _hashCode += getMontooperacion().hashCode();
        }
        if (getNotificapago() != null) {
            _hashCode += getNotificapago().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NotificaPagoExecute.class);

    static {
        org.apache.axis.description.FieldDesc field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("idcarrocompra");
        field.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/action/", "Idcarrocompra"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("tipoautorizacion");
        field.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/action/", "Tipoautorizacion"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("codigoautorizacion");
        field.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/action/", "Codigoautorizacion"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("fechahoraautorizacion");
        field.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/action/", "Fechahoraautorizacion"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("numerocuotas");
        field.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/action/", "Numerocuotas"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("montocuota");
        field.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/action/", "Montocuota"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("montooperacion");
        field.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/action/", "Montooperacion"));
        field.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(field);
        field = new org.apache.axis.description.ElementDesc();
        field.setFieldName("notificapago");
        field.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/action/", "Notificapago"));
        field.setXmlType(new javax.xml.namespace.QName("http://botonpago.cencosud.cl", "NotificaPagoRep"));
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
