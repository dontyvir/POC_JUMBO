/*
 * Creado el 26-01-2006
 *
 * Para cambiar la plantilla para este archivo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
package cl.cencosud.jumbo.beans;

import java.util.HashMap;


/**
 * @author rbelmar
 *
 * Para cambiar la plantilla para este comentario de tipo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
public class BeanEstadisticas {

    private int cantidadTotalOP;
    
    private double cumplimientoATiempo;
    private double cumplimientoAtrasosTransporte;
    private double cumplimientoAtrasosSistemas;
    private double cumplimientoAtrasosLocal;
    private double cumplimientoAtrasosCliente;
    private double cumplimientoTotal;

    private HashMap pedidosXComuna;
    private HashMap atrasosXJornada;
    
    private double TipoDespN;  // TIPO DE DESPACHO "NORMAL"
    private double TipoDespE;  // TIPO DE DESPACHO "ECONOMICO"
    private double TipoDespEX; // TIPO DE DESPACHO "EXPRESS"
    private double TipoDespRL; // TIPO DE DESPACHO "RETIRO EN LOCAL"




    
    /**
     * @return Devuelve atrasosXJornada.
     */
    public HashMap getAtrasosXJornada() {
        return atrasosXJornada;
    }
    /**
     * @return Devuelve cantidadTotalOP.
     */
    public int getCantidadTotalOP() {
        return cantidadTotalOP;
    }
    /**
     * @return Devuelve cumplimientoATiempo.
     */
    public double getCumplimientoATiempo() {
        return cumplimientoATiempo;
    }
    /**
     * @return Devuelve cumplimientoAtrasosCliente.
     */
    public double getCumplimientoAtrasosCliente() {
        return cumplimientoAtrasosCliente;
    }
    /**
     * @return Devuelve cumplimientoAtrasosLocal.
     */
    public double getCumplimientoAtrasosLocal() {
        return cumplimientoAtrasosLocal;
    }
    /**
     * @return Devuelve cumplimientoAtrasosSistemas.
     */
    public double getCumplimientoAtrasosSistemas() {
        return cumplimientoAtrasosSistemas;
    }
    /**
     * @return Devuelve cumplimientoAtrasosTransporte.
     */
    public double getCumplimientoAtrasosTransporte() {
        return cumplimientoAtrasosTransporte;
    }
    /**
     * @return Devuelve cumplimientoTotal.
     */
    public double getCumplimientoTotal() {
        return cumplimientoTotal;
    }
    /**
     * @return Devuelve pedidosXComuna.
     */
    public HashMap getPedidosXComuna() {
        return pedidosXComuna;
    }
    /**
     * @return Devuelve tipoDespE.
     */
    public double getTipoDespE() {
        return TipoDespE;
    }
    /**
     * @return Devuelve tipoDespEX.
     */
    public double getTipoDespEX() {
        return TipoDespEX;
    }
    /**
     * @return Devuelve tipoDespN.
     */
    public double getTipoDespN() {
        return TipoDespN;
    }
    /**
     * @return Devuelve tipoDespRL.
     */
    public double getTipoDespRL() {
        return TipoDespRL;
    }
    /**
     * @param atrasosXJornada El atrasosXJornada a establecer.
     */
    public void setAtrasosXJornada(HashMap atrasosXJornada) {
        this.atrasosXJornada = atrasosXJornada;
    }
    /**
     * @param cantidadTotalOP El cantidadTotalOP a establecer.
     */
    public void setCantidadTotalOP(int cantidadTotalOP) {
        this.cantidadTotalOP = cantidadTotalOP;
    }
    /**
     * @param cumplimientoATiempo El cumplimientoATiempo a establecer.
     */
    public void setCumplimientoATiempo(double cumplimientoATiempo) {
        this.cumplimientoATiempo = cumplimientoATiempo;
    }
    /**
     * @param cumplimientoAtrasosCliente El cumplimientoAtrasosCliente a establecer.
     */
    public void setCumplimientoAtrasosCliente(double cumplimientoAtrasosCliente) {
        this.cumplimientoAtrasosCliente = cumplimientoAtrasosCliente;
    }
    /**
     * @param cumplimientoAtrasosLocal El cumplimientoAtrasosLocal a establecer.
     */
    public void setCumplimientoAtrasosLocal(double cumplimientoAtrasosLocal) {
        this.cumplimientoAtrasosLocal = cumplimientoAtrasosLocal;
    }
    /**
     * @param cumplimientoAtrasosSistemas El cumplimientoAtrasosSistemas a establecer.
     */
    public void setCumplimientoAtrasosSistemas(double cumplimientoAtrasosSistemas) {
        this.cumplimientoAtrasosSistemas = cumplimientoAtrasosSistemas;
    }
    /**
     * @param cumplimientoAtrasosTransporte El cumplimientoAtrasosTransporte a establecer.
     */
    public void setCumplimientoAtrasosTransporte(double cumplimientoAtrasosTransporte) {
        this.cumplimientoAtrasosTransporte = cumplimientoAtrasosTransporte;
    }
    /**
     * @param cumplimientoTotal El cumplimientoTotal a establecer.
     */
    public void setCumplimientoTotal(double cumplimientoTotal) {
        this.cumplimientoTotal = cumplimientoTotal;
    }
    /**
     * @param pedidosXComuna El pedidosXComuna a establecer.
     */
    public void setPedidosXComuna(HashMap pedidosXComuna) {
        this.pedidosXComuna = pedidosXComuna;
    }
    /**
     * @param tipoDespE El tipoDespE a establecer.
     */
    public void setTipoDespE(double tipoDespE) {
        TipoDespE = tipoDespE;
    }
    /**
     * @param tipoDespEX El tipoDespEX a establecer.
     */
    public void setTipoDespEX(double tipoDespEX) {
        TipoDespEX = tipoDespEX;
    }
    /**
     * @param tipoDespN El tipoDespN a establecer.
     */
    public void setTipoDespN(double tipoDespN) {
        TipoDespN = tipoDespN;
    }
    /**
     * @param tipoDespRL El tipoDespRL a establecer.
     */
    public void setTipoDespRL(double tipoDespRL) {
        TipoDespRL = tipoDespRL;
    }
}
