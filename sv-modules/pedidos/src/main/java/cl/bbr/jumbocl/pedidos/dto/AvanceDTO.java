package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class AvanceDTO implements Serializable {

	private double	cant_prod_solicitados; //total de productos
	private double	cant_prod_sin_pickear;
	private double	porc_prod_sin_pickear;
	private double	cant_prod_en_bodega;
	private double	porc_prod_en_bodega;
	//(+) INDRA (+)
	private double  porc_prod_pickeados;
	private double cant_unidad_umbrales;
	private double cant_monto_umbrales;
	private double precio_total;
	private double precio_total_pickeado;
	//(-) INDRA (-)
	
	
    /**
     * @return Devuelve cant_prod_en_bodega.
     */
    public double getCant_prod_en_bodega() {
        return cant_prod_en_bodega;
    }
    /**(+) INDRA (+)
     * @return Devuelve precio total del pedido.
     */
    public double getPrecio_total() {
        return precio_total;
    }
    /**
     * @return Devuelve precio total del pedido.
     */
    public double getPrecio_total_pickeado() {
        return precio_total_pickeado;
    }
   /**
    * @return Devuelve cant_unidad_umbrales.
   */
   public double getCant_unidad_umbrales() {
       return cant_unidad_umbrales;     
    }
   /**
    * @return Devuelve cant_monto_umbrales.
   */
   public double getCant_monto_umbrales() {
       return cant_monto_umbrales;     
    }
    /**
     * @return Devuelve porc_prod_pickeados.
     */
    public double getPorc_prod_pickeados() {
        return porc_prod_pickeados;
    }
    /**(-) INDRA (-)
     * @return Devuelve cant_prod_sin_pickear.
     */
    public double getCant_prod_sin_pickear() {
        return cant_prod_sin_pickear;
    }
    /**
     * @return Devuelve cant_prod_solicitados.
     */
    public double getCant_prod_solicitados() {
        return cant_prod_solicitados;
    }
    /**
     * @return Devuelve porc_prod_en_bodega.
     */
    public double getPorc_prod_en_bodega() {
        return porc_prod_en_bodega;
    }
    /**
     * @return Devuelve porc_prod_sin_pickear.
     */
    public double getPorc_prod_sin_pickear() {
        return porc_prod_sin_pickear;
    }
    /**
     * @param cant_prod_en_bodega El cant_prod_en_bodega a establecer.
     */
    public void setCant_prod_en_bodega(double cant_prod_en_bodega) {
        this.cant_prod_en_bodega = cant_prod_en_bodega;
    }
    /**(+) INDRA (+)
     * @param cant_prod_en_bodega El cant_prod_en_bodega a establecer.
     */
    public void setPrecio_total_pickeado(double precio_total_pickeado) {
        this.precio_total_pickeado = precio_total_pickeado;
    }
    /**
     * @param precio_total del pedido
     */
    public void setPrecio_total(double precio_total) {
        this.precio_total = precio_total;
    }
    /**
     * @param cant_prod_en_bodega El cant_prod_en_bodega a establecer.
     */
    public void setCant_unidad_umbrales(double cant_unidad_umbrales) {
        this.cant_unidad_umbrales = cant_unidad_umbrales;
    }
    /**
     * @param cant_prod_en_bodega El cant_prod_en_bodega a establecer.
     */
    public void setCant_monto_umbrales(double cant_monto_umbrales) {
        this.cant_monto_umbrales = cant_monto_umbrales;
    }
    /**
     * @param cant_prod_en_bodega El cant_prod_en_bodega a establecer.
     */
    public void setPorc_prod_pickeados(double porc_prod_pickeados) {
        this.porc_prod_pickeados = porc_prod_pickeados;
    }
    /**(-) INDRA (-)
     * @param cant_prod_sin_pickear El cant_prod_sin_pickear a establecer.
     */
    public void setCant_prod_sin_pickear(double cant_prod_sin_pickear) {
        this.cant_prod_sin_pickear = cant_prod_sin_pickear;
    }
    /**
     * @param cant_prod_solicitados El cant_prod_solicitados a establecer.
     */
    public void setCant_prod_solicitados(double cant_prod_solicitados) {
        this.cant_prod_solicitados = cant_prod_solicitados;
    }
    /**
     * @param porc_prod_en_bodega El porc_prod_en_bodega a establecer.
     */
    public void setPorc_prod_en_bodega(double porc_prod_en_bodega) {
        this.porc_prod_en_bodega = porc_prod_en_bodega;
    }
    /**
     * @param porc_prod_sin_pickear El porc_prod_sin_pickear a establecer.
     */
    public void setPorc_prod_sin_pickear(double porc_prod_sin_pickear) {
        this.porc_prod_sin_pickear = porc_prod_sin_pickear;
    }
}
