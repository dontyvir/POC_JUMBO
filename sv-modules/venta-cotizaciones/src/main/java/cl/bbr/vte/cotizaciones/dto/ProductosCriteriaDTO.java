package cl.bbr.vte.cotizaciones.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO para datos de el criterio de la consulta para los productos de una categoría. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class ProductosCriteriaDTO implements Serializable {

	private long id_local;
	private long id_categoria;
	private long id_marca;
	private long ordenamiento;
	private long id_producto;
	private long id_cotizacion;
	private List patrones = null;
	
	/**
	 * Constructor
	 */
	public ProductosCriteriaDTO() {
	}

	
	/**
	 * @return Devuelve id_categoria.
	 */
	public long getId_categoria() {
		return id_categoria;
	}
	/**
	 * @param id_categoria El id_categoria a establecer.
	 */
	public void setId_categoria(long id_categoria) {
		this.id_categoria = id_categoria;
	}
	/**
	 * @return Devuelve id_local.
	 */
	public long getId_local() {
		return id_local;
	}
	/**
	 * @param id_local El id_local a establecer.
	 */
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}
	/**
	 * @return Devuelve id_marca.
	 */
	public long getId_marca() {
		return id_marca;
	}
	/**
	 * @param id_marca El id_marca a establecer.
	 */
	public void setId_marca(long id_marca) {
		this.id_marca = id_marca;
	}
	/**
	 * @return Devuelve ordenamiento.
	 */
	public long getOrdenamiento() {
		return ordenamiento;
	}
	/**
	 * @param ordenamiento El ordenamiento a establecer.
	 */
	public void setOrdenamiento(long ordenamiento) {
		this.ordenamiento = ordenamiento;
	}
	/**
	 * @return Devuelve id_producto.
	 */
	public long getId_producto() {
		return id_producto;
	}
	/**
	 * @param id_producto El id_producto a establecer.
	 */
	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}
	
	
	/**
	 * @return Devuelve id_cotizacion.
	 */
	public long getId_cotizacion() {
		return id_cotizacion;
	}
	/**
	 * @param id_cotizacion El id_cotizacion a establecer.
	 */
	public void setId_cotizacion(long id_cotizacion) {
		this.id_cotizacion = id_cotizacion;
	}
	
	/**
	 * @return Devuelve patrones.
	 */
	public List getPatrones() {
		return patrones;
	}
	/**
	 * @param patrones El patrones a establecer.
	 */
	public void setPatrones(List patrones) {
		this.patrones = patrones;
	}
}