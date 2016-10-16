package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la Base de Datos los datos de un Detalle de picking
 * @author bbr
 *
 */
public class DetallePickingEntity {
	private long id_dpicking;
	private long id_detalle;
	private long id_bp;
	private long id_producto;
	private long id_pedido;
	private String cBarra;
	private String descripcion;
	private Double precio;
	private Double cant_pick;
	private String sustituto;
	private String uni_med;
	private String cod_bin;
	private String cod_prod;
	
	/**
	 * @return Returns the cod_bin.
	 */
	public String getCod_bin() {
		return cod_bin;
	}
	/**
	 * @return Returns the cod_prod.
	 */
	public String getCod_prod() {
		return cod_prod;
	}
	/**
	 * @return Returns the uni_med.
	 */
	public String getUni_med() {
		return uni_med;
	}
	/**
	 * @param cod_bin
	 */
	public void setCod_bin(String cod_bin) {
		this.cod_bin = cod_bin;
	}
	/**
	 * @param cod_prod
	 */
	public void setCod_prod(String cod_prod) {
		this.cod_prod = cod_prod;
	}
	/**
	 * @param uni_med
	 */
	public void setUni_med(String uni_med) {
		this.uni_med = uni_med;
	}
	/**
	 * @return Returns the cant_pick.
	 */
	public Double getCant_pick() {
		return cant_pick;
	}
	/**
	 * @param cant_pick The cant_pick to set.
	 */
	public void setCant_pick(Double cant_pick) {
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
	public Double getPrecio() {
		return precio;
	}
	/**
	 * @param precio The precio to set.
	 */
	public void setPrecio(Double precio) {
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
	
	
}
