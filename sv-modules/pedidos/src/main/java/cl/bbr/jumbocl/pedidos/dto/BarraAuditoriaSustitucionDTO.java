package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class BarraAuditoriaSustitucionDTO implements Serializable {

	private String cod_barra;
	private String tip_codbar;
	private String unid_med;
    private long   id_producto;
    private String descripcion;
    private double precio;
    
    	
    /**
     * @return Devuelve cod_barra.
     */
    public String getCod_barra() {
        return cod_barra;
    }
    /**
     * @return Devuelve descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @return Devuelve id_producto.
     */
    public long getId_producto() {
        return id_producto;
    }
    /**
     * @return Devuelve precio.
     */
    public double getPrecio() {
        return precio;
    }
    /**
     * @return Devuelve tip_codbar.
     */
    public String getTip_codbar() {
        return tip_codbar;
    }
    /**
     * @return Devuelve unid_med.
     */
    public String getUnid_med() {
        return unid_med;
    }
    /**
     * @param cod_barra El cod_barra a establecer.
     */
    public void setCod_barra(String cod_barra) {
        this.cod_barra = cod_barra;
    }
    /**
     * @param descripcion El descripcion a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * @param id_producto El id_producto a establecer.
     */
    public void setId_producto(long id_producto) {
        this.id_producto = id_producto;
    }
    /**
     * @param precio El precio a establecer.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    /**
     * @param tip_codbar El tip_codbar a establecer.
     */
    public void setTip_codbar(String tip_codbar) {
        this.tip_codbar = tip_codbar;
    }
    /**
     * @param unid_med El unid_med a establecer.
     */
    public void setUnid_med(String unid_med) {
        this.unid_med = unid_med;
    }
}
