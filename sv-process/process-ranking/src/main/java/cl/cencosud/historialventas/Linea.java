/*
 * Created on 07-may-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.cencosud.historialventas;

/**
 * @author jdroguett
 *
 * Linea de producto de la boleta
 */
public class Linea {
    private long codigoBarra;
    private float cantidad;
    /**
     * @return Returns the cantidad.
     */
    public float getCantidad() {
        return cantidad;
    }
    /**
     * @param cantidad The cantidad to set.
     */
    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }
    /**
     * @return Returns the codigoBarra.
     */
    public long getCodigoBarra() {
        return codigoBarra;
    }
    /**
     * @param codigoBarra The codigoBarra to set.
     */
    public void setCodigoBarra(long codigoBarra) {
        this.codigoBarra = codigoBarra;
        if(codigoBarra > 999999999999l )
            System.out.println("codigoBarra: " + codigoBarra);
    }
    
    public String toString(){
        return "barra: " + codigoBarra + "  cantidad: " + cantidad;
    }
}
