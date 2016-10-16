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
public class BeanPedidosComunaMensual {

    private String comuna;
    private double totalPedidos;
    private HashMap pedidosDiarios;



    /**
     * @return Devuelve comuna.
     */
    public String getComuna() {
        return comuna;
    }
    /**
     * @return Devuelve pedidosDiarios.
     */
    public HashMap getPedidosDiarios() {
        return pedidosDiarios;
    }
    /**
     * @return Devuelve totalPedidos.
     */
    public double getTotalPedidos() {
        return totalPedidos;
    }
    /**
     * @param comuna El comuna a establecer.
     */
    public void setComuna(String comuna) {
        this.comuna = comuna;
    }
    /**
     * @param pedidosDiarios El pedidosDiarios a establecer.
     */
    public void setPedidosDiarios(HashMap pedidosDiarios) {
        this.pedidosDiarios = pedidosDiarios;
    }
    /**
     * @param totalPedidos El totalPedidos a establecer.
     */
    public void setTotalPedidos(double totalPedidos) {
        this.totalPedidos = totalPedidos;
    }
}
