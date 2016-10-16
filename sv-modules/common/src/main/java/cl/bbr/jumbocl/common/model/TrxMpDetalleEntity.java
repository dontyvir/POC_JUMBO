package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la base de datos los datos del detalle de la transacción de un pago
 * @author bbr
 *
 */
public class TrxMpDetalleEntity {
	long id_trxdet;
	long id_detalle;
	long id_trxmp;
	long id_pedido;
	long id_producto;
	String cod_barra;
	double precio;
	String descripcion;
	double cantidad;


	/**
	 * Constructor
	 */
	public TrxMpDetalleEntity() {
	}


	public long getId_trxdet() {
		return id_trxdet;
	}


	public void setId_trxdet(long id_trxdet) {
		this.id_trxdet = id_trxdet;
	}


	/**
	 * @return cantidad
	 */
	public double getCantidad() {
		return cantidad;
	}


	/**
	 * @return cod_barra
	 */
	public String getCod_barra() {
		return cod_barra;
	}


	/**
	 * @return descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}


	/**
	 * @return id_detalle
	 */
	public long getId_detalle() {
		return id_detalle;
	}


	/**
	 * @return id_pedido
	 */
	public long getId_pedido() {
		return id_pedido;
	}


	/**
	 * @return id_producto
	 */
	public long getId_producto() {
		return id_producto;
	}


	/**
	 * @return id_trxmp
	 */
	public long getId_trxmp() {
		return id_trxmp;
	}


	/**
	 * @return precio
	 */
	public double getPrecio() {
		return precio;
	}


	/**
	 * @param cantidad
	 */
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}


	/**
	 * @param cod_barra
	 */
	public void setCod_barra(String cod_barra) {
		this.cod_barra = cod_barra;
	}


	/**
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	/**
	 * @param id_detalle
	 */
	public void setId_detalle(long id_detalle) {
		this.id_detalle = id_detalle;
	}


	/**
	 * @param id_pedido
	 */
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}


	/**
	 * @param id_producto
	 */
	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}


	/**
	 * @param id_trxmp
	 */
	public void setId_trxmp(long id_trxmp) {
		this.id_trxmp = id_trxmp;
	}


	/**
	 * @param precio
	 */
	public void setPrecio(double precio) {
		this.precio = precio;
	}

}
