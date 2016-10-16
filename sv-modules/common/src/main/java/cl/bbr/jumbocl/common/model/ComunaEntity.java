package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la Base de Datos los datos de una comuna
 * @author bbr
 *
 */
public class ComunaEntity {
	private Long id_comuna;
	private Long id_region;
	private String nombre;
	private String reg_nombre;
	/**
	 * @return Returns the id_comuna.
	 */
	public Long getId_comuna() {
		return id_comuna;
	}
	/**
	 * @param id_comuna The id_comuna to set.
	 */
	public void setId_comuna(Long id_comuna) {
		this.id_comuna = id_comuna;
	}
	/**
	 * @return Returns the id_region.
	 */
	public Long getId_region() {
		return id_region;
	}
	/**
	 * @param id_region The id_region to set.
	 */
	public void setId_region(Long id_region) {
		this.id_region = id_region;
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
	 * @return Return the reg_nombre.
	 */
	public String getReg_nombre() {
		return reg_nombre;
	}
	/**
	 * @param reg_nombre The reg_nombre to set.
	 */
	public void setReg_nombre(String reg_nombre) {
		this.reg_nombre = reg_nombre;
	}
}
