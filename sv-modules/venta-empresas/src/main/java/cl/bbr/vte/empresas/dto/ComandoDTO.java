package cl.bbr.vte.empresas.dto;

/**
 * DTO para datos de los comandos 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class ComandoDTO {
	private long 	id_cmd;
	private String 	nombre;
	private String 	descripcion;
	private String	seguridad;
	private String	activo;
	
	public ComandoDTO(){
		
	}
	
	
	/**
	 * @param id_cmd
	 * @param nombre
	 * @param descripcion
	 */
	public ComandoDTO(long id_cmd, String nombre, String descripcion) {
		super();
		this.id_cmd = id_cmd;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}
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
	
	public String getSeguridad() {
		return seguridad;
	}
	
	public void setSeguridad(String seguridad) {
		this.seguridad = seguridad;
	}
	public String getActivo() {
		return activo;
	}
	public void setActivo(String activo) {
		this.activo = activo;
	}
	
	
}
