package cl.cencosud.beans;

/**
 * @author jdroguett
 *
 */
public class Inventario {
    private int mixLocal;
    private String bloqueoCompra;
    private int costoPromedio;
    
    /**
     * @return Returns the bloqueoCompra.
     */
    public String getBloqueoCompra() {
        /*Si mixlocal es cero bloqueoCompra debe decir SI*/
        return this.mixLocal == 0 ? "SI" : bloqueoCompra;
    }
    /**
     * @param bloqueoCompra The bloqueoCompra to set.
     */
    public void setBloqueoCompra(String bloqueoCompra) {
        this.bloqueoCompra = bloqueoCompra;
    }
    /**
     * @return Returns the mixLocal.
     */
    public int getMixLocal() {
        return mixLocal;
    }
    /**
     * @param mixLocal The mixLocal to set.
     */
    public void setMixLocal(int mixLocal) {
        this.mixLocal = mixLocal;
    }
    public int getCostoPromedio() {
        return costoPromedio;
    }
    public void setCostoPromedio(int costoPromedio) {
        this.costoPromedio = costoPromedio;
    }
}
