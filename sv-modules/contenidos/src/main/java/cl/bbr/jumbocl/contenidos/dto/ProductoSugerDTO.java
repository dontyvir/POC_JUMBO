package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;

public class ProductoSugerDTO implements Serializable{
	private long id;
	private long id_suger;
	private long id_base;
	private String fec_crea;
	private String estado;
	private String formato;
	private String nombre;
	
	
	/**
	 * @param id
	 * @param id_suger
	 * @param id_base
	 * @param fec_crea
	 * @param estado
	 * @param formato
	 * @param nombre
	 */
	public ProductoSugerDTO(long id, long id_suger, long id_base, String fec_crea, String estado, String formato, String nombre) {
		super();
		this.id = id;
		this.id_suger = id_suger;
		this.id_base = id_base;
		this.fec_crea = fec_crea;
		this.estado = estado;
		this.formato = formato;
		this.nombre = nombre;
	}
	/**
	 * 
	 */
	public ProductoSugerDTO() {
		super();
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
	 * @return Returns the fec_crea.
	 */
	public String getFec_crea() {
		return fec_crea;
	}
	/**
	 * @param fec_crea The fec_crea to set.
	 */
	public void setFec_crea(String fec_crea) {
		this.fec_crea = fec_crea;
	}
	/**
	 * @return Returns the formato.
	 */
	public String getFormato() {
		return formato;
	}
	/**
	 * @param formato The formato to set.
	 */
	public void setFormato(String formato) {
		this.formato = formato;
	}
	/**
	 * @return Returns the id.
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return Returns the id_base.
	 */
	public long getId_base() {
		return id_base;
	}
	/**
	 * @param id_base The id_base to set.
	 */
	public void setId_base(long id_base) {
		this.id_base = id_base;
	}
	/**
	 * @return Returns the id_suger.
	 */
	public long getId_suger() {
		return id_suger;
	}
	/**
	 * @param id_suger The id_suger to set.
	 */
	public void setId_suger(long id_suger) {
		this.id_suger = id_suger;
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
