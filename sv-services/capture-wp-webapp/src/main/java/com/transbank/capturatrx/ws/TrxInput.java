/**
 * TrxInput.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * o0444.10 v11404193627
 */

package com.transbank.capturatrx.ws;

public class TrxInput  {
    private java.lang.String codAut;
    private java.lang.String codCred;
    private java.lang.String fechaAut;
    private java.lang.String finTarjeta;
    private double montoCaptura;
    private java.lang.String ordenPedido;

    public TrxInput() {
    }

    public java.lang.String getCodAut() {
        return codAut;
    }

    public void setCodAut(java.lang.String codAut) {
        this.codAut = codAut;
    }

    public java.lang.String getCodCred() {
        return codCred;
    }

    public void setCodCred(java.lang.String codCred) {
        this.codCred = codCred;
    }

    public java.lang.String getFechaAut() {
        return fechaAut;
    }

    public void setFechaAut(java.lang.String fechaAut) {
        this.fechaAut = fechaAut;
    }

    public java.lang.String getFinTarjeta() {
        return finTarjeta;
    }

    public void setFinTarjeta(java.lang.String finTarjeta) {
        this.finTarjeta = finTarjeta;
    }

    public double getMontoCaptura() {
        return montoCaptura;
    }

    public void setMontoCaptura(double montoCaptura) {
        this.montoCaptura = montoCaptura;
    }

    public java.lang.String getOrdenPedido() {
        return ordenPedido;
    }

    public void setOrdenPedido(java.lang.String ordenPedido) {
        this.ordenPedido = ordenPedido;
    }

}
