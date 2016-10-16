package cl.bbr.jumbocl.contenidos.dto;

/**
 * Clase que muestra informacion de monitor de clientes.
 * 
 * @author BBR
 *
 */
public class MonitorClientesDTO {
	
	/**
	 * Id del cliente
	 */
	private int id_cliente;
	
	/**
	 * Rut
	 */
	private String rut;
	
	/**
	 * Nombre
	 */
	private String nombre;
	
	/**
	 * Apellido paterno
	 */
	private String paterno;
	
	/**
	 * Apellido materno
	 */
	private String materno;
	
	/**
	 * Fecha de nacimiento
	 */
	private String fnac;
	
	/**
	 * Estado
	 */
	private String estado;
	
	/**
	 * @return estado
	 */
	public String getEstado() {
		return estado;
	}
	
	/**
	 * @return fnac
	 */
	public String getFnac() {
		return fnac;
	}
	
	/**
	 * @return id_cliente
	 */
	public int getId_cliente() {
		return id_cliente;
	}
	
	/**
	 * @return materno
	 */
	public String getMaterno() {
		return materno;
	}
	
	/**
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * @return paterno
	 */
	public String getPaterno() {
		return paterno;
	}
	
	/**
	 * @return rut
	 */
	public String getRut() {
		return rut;
	}
	
	/**
	 * @param estado , estado a modificar.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * @param fnac , fnac a modificar.
	 */
	public void setFnac(String fnac) {
		this.fnac = fnac;
	}
	
	/**
	 * @param id_cliente , id_cliente a modificar.
	 */
	public void setId_cliente(int id_cliente) {
		this.id_cliente = id_cliente;
	}
	
	/**
	 * @param materno , materno a modificar.
	 */
	public void setMaterno(String materno) {
		this.materno = materno;
	}
	
	/**
	 * @param nombre , nombre a modificar.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * @param paterno , paterno a modificar.
	 */
	public void setPaterno(String paterno) {
		this.paterno = paterno;
	}
	
	/**
	 * @param rut , rut a modificar.
	 */
	public void setRut(String rut) {
		this.rut = rut;
	}




}
