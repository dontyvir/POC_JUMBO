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
public class BeanPedidosComunaDiario {

    private Date fecha;
    private double cant_pedidos;



    /**
     * @return Devuelve cant_pedidos.
     */
    public double getCant_pedidos() {
        return cant_pedidos;
    }
    /**
     * @return Devuelve fecha.
     */
    public Date getFecha() {
        return fecha;
    }
    /**
     * @param cant_pedidos El cant_pedidos a establecer.
     */
    public void setCant_pedidos(double cant_pedidos) {
        this.cant_pedidos = cant_pedidos;
    }
    /**
     * @param fecha El fecha a establecer.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
