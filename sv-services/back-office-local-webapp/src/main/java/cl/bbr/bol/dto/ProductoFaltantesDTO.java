/*
 * Creado el 07-nov-2012
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.bol.dto;

public class ProductoFaltantesDTO {

	
	/**
	 * idProducto
	 */
	private long idProducto;
	/**
	 * sectorPicking
	 */
	private String sectorPicking;
	/**
	 * descripcion
	 */
	private String descripcion;
	
	/**
	 * idJornada
	 */
	private long idJornada;
	/**
	 * presencia
	 */
	private long presenciaProductosEnJornada;
	/**
	 * opsTotales
	 */
	private long opsTotalesPorJornada;
	/**
	 * porcentajePresencia
	 */
	private String porcentajePresencia;
	/**
	 * cantidadProductos
	 */
	private String cantidadProductos;
	
	
	
	/**
	 * @return Devuelve cantidadProductos.
	 */
	public String getCantidadProductos() {
		return cantidadProductos;
	}
	/**
	 * @param cantidadProductos El cantidadProductos a establecer.
	 */
	public void setCantidadProductos(String cantidadProductos) {
		this.cantidadProductos = cantidadProductos;
	}
	/**
	 * @return Devuelve descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion El descripcion a establecer.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return Devuelve idJornada.
	 */
	public long getIdJornada() {
		return idJornada;
	}
	/**
	 * @param idJornada El idJornada a establecer.
	 */
	public void setIdJornada(long idJornada) {
		this.idJornada = idJornada;
	}
	/**
	 * @return Devuelve idProducto.
	 */
	public long getIdProducto() {
		return idProducto;
	}
	/**
	 * @param idProducto El idProducto a establecer.
	 */
	public void setIdProducto(long idProducto) {
		this.idProducto = idProducto;
	}
	/**
	 * @return Devuelve opsTotalesPorJornada.
	 */
	public long getOpsTotalesPorJornada() {
		return opsTotalesPorJornada;
	}
	/**
	 * @param opsTotalesPorJornada El opsTotalesPorJornada a establecer.
	 */
	public void setOpsTotalesPorJornada(long opsTotalesPorJornada) {
		this.opsTotalesPorJornada = opsTotalesPorJornada;
	}
	/**
	 * @return Devuelve porcentajePresencia.
	 */
	public String getPorcentajePresencia() {
		return porcentajePresencia;
	}
	/**
	 * @param porcentajePresencia El porcentajePresencia a establecer.
	 */
	public void setPorcentajePresencia(String porcentajePresencia) {
		this.porcentajePresencia = porcentajePresencia;
	}
	/**
	 * @return Devuelve presenciaProductosEnJornada.
	 */
	public long getPresenciaProductosEnJornada() {
		return presenciaProductosEnJornada;
	}
	/**
	 * @param presenciaProductosEnJornada El presenciaProductosEnJornada a establecer.
	 */
	public void setPresenciaProductosEnJornada(long presenciaProductosEnJornada) {
		this.presenciaProductosEnJornada = presenciaProductosEnJornada;
	}
	/**
	 * @return Devuelve sectorPicking.
	 */
	public String getSectorPicking() {
		return sectorPicking;
	}
	/**
	 * @param sectorPicking El sectorPicking a establecer.
	 */
	public void setSectorPicking(String sectorPicking) {
		this.sectorPicking = sectorPicking;
	}
}
