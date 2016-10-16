package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la Base de Datos los datos de un comando
 * @author bbr
 *
 */
public class ComandosEntity {
	private long 	id_cmd;
	private String 	nombre;
	private String 	descripcion;
	private String	seguridad;
	private String	activo;
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
	 * @return Returns the id_cmd.
	 */
	public long getId_cmd() {
		return id_cmd;
	}
	/**
	 * @param id_cmd The id_cmd to set.
	 */
	public void setId_cmd(long id_cmd) {
		this.id_cmd = id_cmd;
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
	 * @return Returns the seguridad. 
	 */
	public String getSeguridad() {
		return seguridad;
	}
	/**
	 * @param seguridad The seguridad to set.
	 */
	public void setSeguridad(String seguridad) {
		this.seguridad = seguridad;
	}
	/**
	 * @return Returns the activo
	 */
	public String getActivo() {
		return activo;
	}
	/**
	 * @param activo The activo to set.
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}
	
	
	
}
