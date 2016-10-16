/*
 * Creado el 26-01-2006
 *
 * Para cambiar la plantilla para este archivo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
package cl.cencosud.jumbo.beans;

import java.util.Date;

/**
 * @author rbelmar
 *
 * Para cambiar la plantilla para este comentario de tipo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
public class BeanTipoDespachoMensual {

    private Date fecha;
    private double tipoDespN;
    private double tipoDespE;
    private double tipoDespEX;


    /**
     * @return Devuelve fecha.
     */
    public Date getFecha() {
        return fecha;
    }
    /**
     * @return Devuelve tipoDespE.
     */
    public double getTipoDespE() {
        return tipoDespE;
    }
    /**
     * @return Devuelve tipoDespEX.
     */
    public double getTipoDespEX() {
        return tipoDespEX;
    }
    /**
     * @return Devuelve tipoDespN.
     */
    public double getTipoDespN() {
        return tipoDespN;
    }
    /**
     * @return Devuelve totalDespachos.
     */
    public double getTotalDespachos() {
        return (tipoDespN + tipoDespE + tipoDespEX);
    }
    /**
     * @param fecha El fecha a establecer.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    /**
     * @param tipoDespE El tipoDespE a establecer.
     */
    public void setTipoDespE(double tipoDespE) {
        this.tipoDespE = tipoDespE;
    }
    /**
     * @param tipoDespEX El tipoDespEX a establecer.
     */
    public void setTipoDespEX(double tipoDespEX) {
        this.tipoDespEX = tipoDespEX;
    }
    /**
     * @param tipoDespN El tipoDespN a establecer.
     */
    public void setTipoDespN(double tipoDespN) {
        this.tipoDespN = tipoDespN;
    }

}
