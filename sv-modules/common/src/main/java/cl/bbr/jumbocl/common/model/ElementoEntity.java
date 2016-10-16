package cl.bbr.jumbocl.common.model;

import java.sql.Timestamp;

public class ElementoEntity {
	private long 	id_elemento;
	private long 	id_tipo_elem;
	private Timestamp fec_creacion;
	private String 	nombre;
	private String 	descripcion;
	private String 	estado;
	private String 	url_destino;
	private long	clicks;
	
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
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Returns the fec_creacion.
	 */
	public Timestamp getFec_creacion() {
		return fec_creacion;
	}
	/**
	 * @param fec_creacion The fec_creacion to set.
	 */
	public void setFec_creacion(Timestamp fec_creacion) {
		this.fec_creacion = fec_creacion;
	}
	/**
	 * @return Returns the id_elemento.
	 */
	public long getId_elemento() {
		return id_elemento;
	}
	/**
	 * @param id_elemento The id_elemento to set.
	 */
	public void setId_elemento(long id_elemento) {
		this.id_elemento = id_elemento;
	}
	/**
	 * @return Returns the id_tipo_elem.
	 */
	public long getId_tipo_elem() {
		return id_tipo_elem;
	}
	/**
	 * @param id_tipo_elem The id_tipo_elem to set.
	 */
	public void setId_tipo_elem(long id_tipo_elem) {
		this.id_tipo_elem = id_tipo_elem;
	}
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return Returns the url_destino.
	 */
	public String getUrl_destino() {
		return url_destino;
	}
	/**
	 * @param url_destino The url_destino to set.
	 */
	public void setUrl_destino(String url_destino) {
		this.url_destino = url_destino;
	}
	/**
	 * @return Returns the clicks.
	 */
	public long getClicks() {
		return clicks;
	}
	/**
	 * @param clicks The clicks to set.
	 */
	public void setClicks(long clicks) {
		this.clicks = clicks;
	}
	
}
