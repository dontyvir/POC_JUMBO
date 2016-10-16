package cl.bbr.jumbocl.pedidos.promos.dto;

public class DetPedidoDTO {
	
	private long id_pedido;
	private long id_detalle;
	private long id_producto;
	private String desc_prod;	
	private double cant_solicitada;
	private double precio_lista;
	private String pesable;
	private String cod_barra;
	private String seccion_sap;
	private int rubro;

	public DetPedidoDTO() {		
	}

	/**
	 * @return Returns the pesable.
	 */
	public String getPesable() {
		return pesable;
	}

	/**
	 * @param pesable The pesable to set.
	 */
	public void setPesable(String pesable) {
		this.pesable = pesable;
	}

	/**
	 * @return Returns the cant_solicitada.
	 */
	public double getCant_solicitada() {
		return cant_solicitada;
	}

	/**
	 * @param cant_solicitada The cant_solicitada to set.
	 */
	public void setCant_solicitada(double cant_solicitada) {
		this.cant_solicitada = cant_solicitada;
	}

	/**
	 * @return Returns the cod_barra.
	 */
	public String getCod_barra() {
		return cod_barra;
	}

	/**
	 * @param cod_barra The cod_barra to set.
	 */
	public void setCod_barra(String cod_barra) {
		this.cod_barra = cod_barra;
	}

	/**
	 * @return Returns the desc_prod.
	 */
	public String getDesc_prod() {
		return desc_prod;
	}

	/**
	 * @param desc_prod The desc_prod to set.
	 */
	public void setDesc_prod(String desc_prod) {
		this.desc_prod = desc_prod;
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
	 * @return Returns the precio_lista.
	 */
	public double getPrecio_lista() {
		return precio_lista;
	}

	/**
	 * @param precio_lista The precio_lista to set.
	 */
	public void setPrecio_lista(double precio_lista) {
		this.precio_lista = precio_lista;
	}

	/**
	 * @return Returns the seccion_sap.
	 */
	public String getSeccion_sap() {
		return seccion_sap;
	}

	/**
	 * @param seccion_sap The seccion_sap to set.
	 */
	public void setSeccion_sap(String seccion_sap) {
		this.seccion_sap = seccion_sap;
	}

	public int getRubro() {
		return rubro;
	}

	public void setRubro(int rubro) {
		this.rubro = rubro;
	}

}
