package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la base de datos los datos de un tipo de calle
 * @author BBRI
 * 
 */
public class TipoCalleEntity {

	private Long id;
	private String nombre;
	private String estado;

	/**
	 * Constructor
	 */
	public TipoCalleEntity() {

	}

	/**
	 * @return estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}