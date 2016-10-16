package cl.bbr.jumbocl.common.model;

import java.sql.Timestamp;

/**
 * Clase que captura desde la base de datos los datos de un producto sugerido
 * @author bbr
 *
 */
public class ProductoSugerEntity {
	private Long id;
	private Long id_suger;
	private Long id_base;
	private Timestamp fec_crea;
	private String estado;
	private String formato;
	private String nombre;
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
	public Timestamp getFec_crea() {
		return fec_crea;
	}
	/**
	 * @param fec_crea The fec_crea to set.
	 */
	public void setFec_crea(Timestamp fec_crea) {
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
	public Long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return Returns the id_base.
	 */
	public Long getId_base() {
		return id_base;
	}
	/**
	 * @param id_base The id_base to set.
	 */
	public void setId_base(Long id_base) {
		this.id_base = id_base;
	}
	/**
	 * @return Returns the id_suger.
	 */
	public Long getId_suger() {
		return id_suger;
	}
	/**
	 * @param id_suger The id_suger to set.
	 */
	public void setId_suger(Long id_suger) {
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
