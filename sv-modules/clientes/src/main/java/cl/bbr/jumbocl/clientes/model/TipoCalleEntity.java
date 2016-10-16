package cl.bbr.jumbocl.clientes.model;

/**
 * @author BBRI
 * 
 */
public class TipoCalleEntity {

	private Long id;
	private String nombre;
	private Character estado;

	/**
	 * Constructor
	 */
	public TipoCalleEntity() {

	}

	public Character getEstado() {
		return estado;
	}

	public void setEstado(Character estado) {
		this.estado = estado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}