package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class DetallePickingDTO implements Serializable{
	private long id_dpicking;
	private long id_detalle;
	private long id_bp;
	private long id_producto;
	private long id_pedido;
	private String cBarra;
	private String descripcion;
	private double precio;
	private double cant_pick;
	private String sustituto;
	private int posicion;
	private String uni_med;
	private String cod_bin;
	private String cod_prod;
	//	---------- mod_ene09 - ini------------------------
	private String auditado;
	
	private double precio_picking_ori;
	private double total_pickeado;
	
	/**
	 * @return Returns the auditado.
	 */
	public String getAuditado() {
		return auditado;
	}
	/**
	 * @param auditado The auditado to set.
	 */
	public void setAuditado(String auditado) {
		this.auditado = auditado;
	}
	//	---------- mod_ene09 - fin------------------------
	public String getCod_bin() {
		return cod_bin;
	}
	public String getCod_prod() {
		return cod_prod;
	}
	public String getUni_med() {
		return uni_med;
	}
	public void setCod_bin(String cod_bin) {
		this.cod_bin = cod_bin;
	}
	public void setCod_prod(String cod_prod) {
		this.cod_prod = cod_prod;
	}
	public void setUni_med(String uni_med) {
		this.uni_med = uni_med;
	}
	/**
	 * @param id_dpicking
	 * @param id_detalle
	 * @param id_bp
	 * @param id_producto
	 * @param id_pedido
	 * @param descripcion
	 * @param precio
	 * @param cant_pick
	 * @param sustituto
	 */
	public DetallePickingDTO(long id_dpicking, long id_detalle, long id_bp, long id_producto, long id_pedido, String cBarra, String descripcion, double precio, double cant_pick, String sustituto) {
		super();
		this.id_dpicking = id_dpicking;
		this.id_detalle = id_detalle;
		this.id_bp = id_bp;
		this.id_producto = id_producto;
		this.id_pedido = id_pedido;
		this.cBarra = cBarra;
		this.descripcion = descripcion;
		this.precio = precio;
		this.cant_pick = cant_pick;
		this.sustituto = sustituto;
	}
	public DetallePickingDTO() {
	
	}
	/**
	 * @return Returns the cant_pick.
	 */
	public double getCant_pick() {
		return cant_pick;
	}
	/**
	 * @param cant_pick The cant_pick to set.
	 */
	public void setCant_pick(double cant_pick) {
		this.cant_pick = cant_pick;
	}
	/**
	 * @return Returns the cBarra.
	 */
	public String getCBarra() {
		return cBarra;
	}
	/**
	 * @param barra The cBarra to set.
	 */
	public void setCBarra(String barra) {
		cBarra = barra;
	}
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return Returns the id_bp.
	 */
	public long getId_bp() {
		return id_bp;
	}
	/**
	 * @param id_bp The id_bp to set.
	 */
	public void setId_bp(long id_bp) {
		this.id_bp = id_bp;
	}
	/**
	 * @return Returns the id_detalle.
	 */
	public long getId_detalle() {
		return id_detalle;
	}
	/**
	 * @param id_detalle The id_detalle to set.
	 */
	public void setId_detalle(long id_detalle) {
		this.id_detalle = id_detalle;
	}
	/**
	 * @return Returns the id_dpicking.
	 */
	public long getId_dpicking() {
		return id_dpicking;
	}
	/**
	 * @param id_dpicking The id_dpicking to set.
	 */
	public void setId_dpicking(long id_dpicking) {
		this.id_dpicking = id_dpicking;
	}
	/**
	 * @return Returns the id_pedido.
	 */
	public long getId_pedido() {
		return id_pedido;
	}
	/**
	 * @param id_pedido The id_pedido to set.
	 */
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	/**
	 * @return Returns the id_producto.
	 */
	public long getId_producto() {
		return id_producto;
	}
	/**
	 * @param id_producto The id_producto to set.
	 */
	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}
	/**
	 * @return Returns the precio.
	 */
	public double getPrecio() {
		return precio;
	}
	/**
	 * @param precio The precio to set.
	 */
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	/**
	 * @return Returns the sustituto.
	 */
	public String getSustituto() {
		return sustituto;
	}
	/**
	 * @param sustituto The sustituto to set.
	 */
	public void setSustituto(String sustituto) {
		this.sustituto = sustituto;
	}
	public int getPosicion() {
		return posicion;
	}
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
	
	/**
	 * @return el precio_picking_ori
	 */
	public double getPrecio_picking_ori() {
		return precio_picking_ori;
	}
	/**
	 * @param precio_picking_ori el precio_picking_ori a establecer
	 */
	public void setPrecio_picking_ori(double precio_picking_ori) {
		this.precio_picking_ori = precio_picking_ori;
	}
	
	/**
	 * @return el total_pickeado
	 */
	public double getTotal_pickeado() {
		return total_pickeado;
	}
	/**
	 * @param total_pickeado el total_pickeado a establecer
	 */
	public void setTotal_pickeado(double total_pickeado) {
		this.total_pickeado = total_pickeado;
	}
	
	public String toString() {
		return "DetallePickingDTO [id_detalle=" + id_detalle + ", id_producto="
				+ id_producto + ", cBarra=" + cBarra + ", precio=" + precio
				+ ", cant_pick=" + cant_pick + ", sustituto=" + sustituto + "]";
	}
	
}
