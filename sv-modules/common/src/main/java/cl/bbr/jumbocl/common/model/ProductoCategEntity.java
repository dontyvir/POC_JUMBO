package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la base de datos los datos de un producto por categoria
 * @author bbr
 *
 */
public class ProductoCategEntity {
	private Long id;
	private Long id_cat;
	private Long id_prod;
	private String estado;
	private Integer orden;
	private String con_pago;
	private String nom_prod; 
	private String tipo_prod;
	private String nom_marca;
	
	
	/**
	 * @param id_prod
	 * @param nom_prod
	 * @param orden
	 */
	public ProductoCategEntity(Long id_prod, String nom_prod, Integer orden) {
		super();
		this.id_prod = id_prod;
		this.nom_prod = nom_prod;
		this.orden = orden;
	}
	/**
	 * Constructor
	 */
	public ProductoCategEntity() {
		super();

	}
	/**
	 * @return Returns the con_pago.
	 */
	public String getCon_pago() {
		return con_pago;
	}
	/**
	 * @param con_pago The con_pago to set.
	 */
	public void setCon_pago(String con_pago) {
		this.con_pago = con_pago;
	}
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
	 * @return Returns the id_cat.
	 */
	public Long getId_cat() {
		return id_cat;
	}
	/**
	 * @param id_cat The id_cat to set.
	 */
	public void setId_cat(Long id_cat) {
		this.id_cat = id_cat;
	}
	/**
	 * @return Returns the id_prod.
	 */
	public Long getId_prod() {
		return id_prod;
	}
	/**
	 * @param id_prod The id_prod to set.
	 */
	public void setId_prod(Long id_prod) {
		this.id_prod = id_prod;
	}
	/**
	 * @return Returns the nom_prod.
	 */
	public String getNom_prod() {
		return nom_prod;
	}
	/**
	 * @param nom_prod The nom_prod to set.
	 */
	public void setNom_prod(String nom_prod) {
		this.nom_prod = nom_prod;
	}
	/**
	 * @return Returns the orden.
	 */
	public Integer getOrden() {
		return orden;
	}
	/**
	 * @param orden The orden to set.
	 */
	public void setOrden(Integer orden) {
		this.orden = orden;
	}
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
	 * @param nom_marca
	 */
	public void setNom_marca(String nom_marca) {
		this.nom_marca = nom_marca;
	}
	/**
	 * @param tipo_prod
	 */
	public void setTipo_prod(String tipo_prod) {
		this.tipo_prod = tipo_prod;
	}
}