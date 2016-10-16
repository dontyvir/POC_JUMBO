package cl.bbr.jumbocl.common.model;

import java.sql.Timestamp;

public class CampanaEntity {
	private long 	id_campana;
	private String 	nombre;
	private String 	descripcion;
	private String 	estado;
	private Timestamp fec_creacion;
	
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
	 * @return Returns the id_campana.
	 */
	public long getId_campana() {
		return id_campana;
	}
	/**
	 * @param id_campana The id_campana to set.
	 */
	public void setId_campana(long id_campana) {
		this.id_campana = id_campana;
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
	
	
}
