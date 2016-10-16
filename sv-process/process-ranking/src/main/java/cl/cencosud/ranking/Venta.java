/*
 * Created on 25-abr-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.cencosud.ranking;

/**
 * @author JDroguett
 *
 */
public class Venta {
    private long codigoBarra;
    
    /**
     * Cantidad de boletas donde apararece el producto
     */
    private int boletas; 

    public Venta(long codigoBarra, int boletas){
        this.codigoBarra = codigoBarra;
        this.boletas = boletas;
    }
    
    /**
     * @return Returns the boletas.
     */
    public int getBoletas() {
        return boletas;
    }
    /**
     * @param boletas The boletas to set.
     */
    public void setBoletas(int boletas) {
        this.boletas = boletas;
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
    }
}
