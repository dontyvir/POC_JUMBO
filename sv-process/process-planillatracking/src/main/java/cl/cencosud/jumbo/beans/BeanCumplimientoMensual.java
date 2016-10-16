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
public class BeanCumplimientoMensual {

    private Date fecha;
    private double aTiempo;
    private double atrasosSistemas;
    private double atrasosLocal;
    private double atrasosCliente;
    private double atrasosTransporte;

    
    /**
     * @return Devuelve aTiempo.
     */
    public double getATiempo() {
        return aTiempo;
    }
    /**
     * @return Devuelve atrasosCliente.
     */
    public double getAtrasosCliente() {
        return atrasosCliente;
    }
    /**
     * @return Devuelve atrasosLocal.
     */
    public double getAtrasosLocal() {
        return atrasosLocal;
    }
    /**
     * @return Devuelve atrasosSistemas.
     */
    public double getAtrasosSistemas() {
        return atrasosSistemas;
    }
    /**
     * @return Devuelve atrasosTransporte.
     */
    public double getAtrasosTransporte() {
        return atrasosTransporte;
    }
    /**
     * @return Devuelve fecha.
     */
    public Date getFecha() {
        return fecha;
    }
    /**
     * @return Devuelve total.
     */
    public double getTotal() {
        return (aTiempo + atrasosSistemas + atrasosLocal + atrasosCliente + atrasosTransporte);
    }
    /**
     * @param tiempo El aTiempo a establecer.
     */
    public void setATiempo(double tiempo) {
        aTiempo = tiempo;
    }
    /**
     * @param atrasosCliente El atrasosCliente a establecer.
     */
    public void setAtrasosCliente(double atrasosCliente) {
        this.atrasosCliente = atrasosCliente;
    }
    /**
     * @param atrasosLocal El atrasosLocal a establecer.
     */
    public void setAtrasosLocal(double atrasosLocal) {
        this.atrasosLocal = atrasosLocal;
    }
    /**
     * @param atrasosSistemas El atrasosSistemas a establecer.
     */
    public void setAtrasosSistemas(double atrasosSistemas) {
        this.atrasosSistemas = atrasosSistemas;
    }
    /**
     * @param atrasosTransporte El atrasosTransporte a establecer.
     */
    public void setAtrasosTransporte(double atrasosTransporte) {
        this.atrasosTransporte = atrasosTransporte;
    }
    /**
     * @param fecha El fecha a establecer.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
