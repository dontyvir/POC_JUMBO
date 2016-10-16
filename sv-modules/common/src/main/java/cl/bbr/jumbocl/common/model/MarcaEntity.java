package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la base de datos los datos de una marca
 * @author bbr
 *
 */
public class MarcaEntity {
	private Long id;
	private String nombre;
	private String estado;
	private int cant_prods;
	
	/**
	 * @return Retorna estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado , estado a modificar.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Retorna id.
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id , id a modificar.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return Retorna nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre , nombre a modificar.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return Retorna cant_prods.
	 */
	public int getCant_prods() {
		return cant_prods;
	}
	/**
	 * @param cant_prods , cant_prods a modificar.
	 */
	public void setCant_prods(int cant_prods) {
		this.cant_prods = cant_prods;
	}
	
}
