package cl.bbr.jumbocl.contenidos.dto;

/**
 * Clase que muestra informacion de relacion entre Productos y Categorias.
 * 
 * @author BBR
 *
 */
public class ProductoCategDTO {
	
	/**
	 * Id de la relacion
	 */
	private long id;
	
	/**
	 * Id de categoria
	 */
	private long id_cat;
	
	/**
	 * Id Producto
	 */
	private long id_prod;
	
	/**
	 * Estado
	 */
	private String estado;
	
	/**
	 * Orden
	 */
	private int orden;
	
	/**
	 * Con pago
	 */
	private String con_pago;
	
	/**
	 * Nombre de producto
	 */
	private String nom_prod;
	
	/**
	 * Tipo de Producto
	 */
	private String tipo_prod;
	
	/**
	 * Nombre de marca
	 */
	private String nom_marca;
	
	/**
	 * @return nom_marca
	 */
	public String getNom_marca() {
		return nom_marca;
	}
	
	/**
	 * @return tipo_prod
	 */
	public String getTipo_prod() {
		return tipo_prod;
	}
	
	/**
	 * @param nom_marca , nom_marca a modificar.
	 */
	public void setNom_marca(String nom_marca) {
		this.nom_marca = nom_marca;
	}
	
	/**
	 * @param tipo_prod , tipo_prod a modificar.
	 */
	public void setTipo_prod(String tipo_prod) {
		this.tipo_prod = tipo_prod;
	}
	
	/**
	 * @param id_prod
	 * @param nom_prod
	 * @param orden
	 */
	public ProductoCategDTO(long id_prod , String nom_prod , int orden) {
		super();
		this.id_prod = id_prod;
		this.nom_prod = nom_prod;
		this.orden = orden;
	}
	
	/**
	 * 
	 */
	public ProductoCategDTO() {
		super();
	}
	
	/**
	 * @return Retorna con_pago.
	 */
	public String getCon_pago() {
		return con_pago;
	}
	/**
	 * @param con_pago , con_pago a modificar.
	 */
	public void setCon_pago(String con_pago) {
		this.con_pago = con_pago;
	}
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
	public long getId() {
		return id;
	}
	/**
	 * @param id , id a modificar.
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return Retorna id_cat.
	 */
	public long getId_cat() {
		return id_cat;
	}
	/**
	 * @param id_cat , id_cat a modificar.
	 */
	public void setId_cat(long id_cat) {
		this.id_cat = id_cat;
	}
	/**
	 * @return Retorna id_prod.
	 */
	public long getId_prod() {
		return id_prod;
	}
	/**
	 * @param id_prod , id_prod a modificar.
	 */
	public void setId_prod(long id_prod) {
		this.id_prod = id_prod;
	}
	/**
	 * @return Retorna nom_prod.
	 */
	public String getNom_prod() {
		return nom_prod;
	}
	/**
	 * @param nom_prod , nom_prod a modificar.
	 */
	public void setNom_prod(String nom_prod) {
		this.nom_prod = nom_prod;
	}
	/**
	 * @return Retorna orden.
	 */
	public int getOrden() {
		return orden;
	}
	/**
	 * @param orden , orden a modificar.
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	} 

	
}
