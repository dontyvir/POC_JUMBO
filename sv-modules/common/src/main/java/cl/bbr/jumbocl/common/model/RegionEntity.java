package cl.bbr.jumbocl.common.model;

/**
 * @author BBRI
 * 
 */
public class RegionEntity {

	private Long id;
	private String nombre;
	private Long numero;
	
	public RegionEntity() {
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

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}
	
}