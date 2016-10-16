package cl.bbr.boc.dto;

import java.io.Serializable;

/**
 * Fecha de creación: 27/10/2011
 *
 * Clase de serializacion que se encarga de guardar los datos para mostrar en el detalle de reporte de categorizacion erronea
 */
public class DetalleCategorizacionErroneaDTO implements Serializable {

	/**
	 * id de producto erroneo
	 */
	private String id_producto;
	
	/**
	 * id de categoria erronea
	 */
	private String id_categoria;
	
//	20121008 SBERNAL
	/**
	 * orden erronea
	 */
	private String orden;
	
//	-20121008 SBERNAL
	/**
	 * Mensaje de error
	 */
	private String mensajeError;

	
	/**
	 * @return Devuelve id_categoria.
	 */
	public String getId_categoria() {
		return id_categoria;
	}
	/**
	 * @param id_categoria El id_categoria a establecer.
	 */
	public void setId_categoria(String id_categoria) {
		this.id_categoria = id_categoria;
	}
	/**
	 * @return Devuelve id_producto.
	 */
	public String getId_producto() {
		return id_producto;
	}
	/**
	 * @param id_producto El id_producto a establecer.
	 */
	public void setId_producto(String id_producto) {
		this.id_producto = id_producto;
	}
	/**
	 * @return Devuelve mensajeError.
	 */
	public String getMensajeError() {
		return mensajeError;
	}
	/**
	 * @param mensajeError El mensajeError a establecer.
	 */
	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	
//	20121008 SBERNAL
	/**
	 * @param orden El orden a establecer.
	 */
	public void setOrden(String orden) {
		this.orden = orden;
	}
	/**
	 * @return Devuelve orden.
	 */
	public String getOrden() {
		return orden;
	}
//	-20121008 SBERNAL
}
