package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class TPAuditSustitucionDTO implements Serializable{
	
    private long id;
	private long id_auditoria;
    private long id_ronda;
    private long id_dronda;
    private long id_pedido;
    private long id_detalle;
    private long id_dpicking;
    private long id_usuario;
    
	private String cod_barra_orig;
	private String cod_barra_sust;
    private int    cantidad_sust;
    private double precio_sust;
    private String unid_med_sust;
	private String accion;

    
    
    /**
     * @return Devuelve accion.
     */
    public String getAccion() {
        return accion;
    }
    /**
     * @return Devuelve cantidad_sust.
     */
    public int getCantidad_sust() {
        return cantidad_sust;
    }
    /**
     * @return Devuelve cod_barra_orig.
     */
    public String getCod_barra_orig() {
        return cod_barra_orig;
    }
    /**
     * @return Devuelve cod_barra_sust.
     */
    public String getCod_barra_sust() {
        return cod_barra_sust;
    }
    /**
     * @return Devuelve id.
     */
    public long getId() {
        return id;
    }
    /**
     * @return Devuelve id_auditoria.
     */
    public long getId_auditoria() {
        return id_auditoria;
    }
    /**
     * @return Devuelve id_detalle.
     */
    public long getId_detalle() {
        return id_detalle;
    }
    /**
     * @return Devuelve id_dronda.
     */
    public long getId_dronda() {
        return id_dronda;
    }
    /**
     * @return Devuelve id_pedido.
     */
    public long getId_pedido() {
        return id_pedido;
    }
    /**
     * @return Devuelve id_ronda.
     */
    public long getId_ronda() {
        return id_ronda;
    }
    /**
     * @return Devuelve id_usuario.
     */
    public long getId_usuario() {
        return id_usuario;
    }
    /**
     * @return Devuelve precio_sust.
     */
    public double getPrecio_sust() {
        return precio_sust;
    }
    /**
     * @param accion El accion a establecer.
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }
    /**
     * @param cantidad_sust El cantidad_sust a establecer.
     */
    public void setCantidad_sust(int cantidad_sust) {
        this.cantidad_sust = cantidad_sust;
    }
    /**
     * @param cod_barra_orig El cod_barra_orig a establecer.
     */
    public void setCod_barra_orig(String cod_barra_orig) {
        this.cod_barra_orig = cod_barra_orig;
    }
    /**
     * @param cod_barra_sust El cod_barra_sust a establecer.
     */
    public void setCod_barra_sust(String cod_barra_sust) {
        this.cod_barra_sust = cod_barra_sust;
    }
    /**
     * @param id El id a establecer.
     */
    public void setId(long id) {
        this.id = id;
    }
    /**
     * @param id_auditoria El id_auditoria a establecer.
     */
    public void setId_auditoria(long id_auditoria) {
        this.id_auditoria = id_auditoria;
    }
    /**
     * @param id_detalle El id_detalle a establecer.
     */
    public void setId_detalle(long id_detalle) {
        this.id_detalle = id_detalle;
    }
    /**
     * @param id_dronda El id_dronda a establecer.
     */
    public void setId_dronda(long id_dronda) {
        this.id_dronda = id_dronda;
    }
    /**
     * @param id_pedido El id_pedido a establecer.
     */
    public void setId_pedido(long id_pedido) {
        this.id_pedido = id_pedido;
    }
    /**
     * @param id_ronda El id_ronda a establecer.
     */
    public void setId_ronda(long id_ronda) {
        this.id_ronda = id_ronda;
    }
    /**
     * @param id_usuario El id_usuario a establecer.
     */
    public void setId_usuario(long id_usuario) {
        this.id_usuario = id_usuario;
    }
    /**
     * @param precio_sust El precio_sust a establecer.
     */
    public void setPrecio_sust(double precio_sust) {
        this.precio_sust = precio_sust;
    }
    /**
     * @return Devuelve id_dpicking.
     */
    public long getId_dpicking() {
        return id_dpicking;
    }
    /**
     * @param id_dpicking El id_dpicking a establecer.
     */
    public void setId_dpicking(long id_dpicking) {
        this.id_dpicking = id_dpicking;
    }
    /**
     * @return Devuelve unid_med_sust.
     */
    public String getUnid_med_sust() {
        return unid_med_sust;
    }
    /**
     * @param unid_med_sust El unid_med_sust a establecer.
     */
    public void setUnid_med_sust(String unid_med_sust) {
        this.unid_med_sust = unid_med_sust;
    }
}
