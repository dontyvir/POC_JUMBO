/*
 * Created on 20-may-2008
 *
 */
package cl.cencosud.ultimascompras;

import java.util.Date;

/**
 * @author jdroguett
 *  
 */
public class DetalleCompra {
    private long codigoBarra;

    private float cantidad;

    private Date fecha;

    /**
     * @return Returns the cantidad.
     */
    public float getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad
     *            The cantidad to set.
     */
    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return Returns the fecha.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha
     *            The fecha to set.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return Returns the codigoBarra.
     */
    public long getCodigoBarra() {
        return codigoBarra;
    }

    /**
     * @param codigoBarra
     *            The codigoBarra to set.
     */
    public void setCodigoBarra(long codigoBarra) {
        this.codigoBarra = codigoBarra;
    }
}
