package cl.bbr.jumbocl.contenidos.dto;

/**
 * Clase que muestra informacion del log de producto.
 * 
 * @author BBR
 *
 */
public class CatalogacionMasivaLogDTO {
	
	/**
	 * Id del log del producto
	 */
	private long id;
	
	/**
	 * Codigo del producto
	 */
	private long cod_prod;
	
	/**
	 * Codigo del producto Sap
	 */
	private long cod_prod_bo;
	
	/**
	 * Fecha de creación del producto
	 */
	private String fec_crea;
	
	/**
	 * Usuario
	 */
	private String usuario;
	
	/**
	 * Texto
	 */
	private String texto;
	
	/**
	 * Constructor inicial.
	 */
	public CatalogacionMasivaLogDTO() {
		super();
	}

	/**
	 * @param id
	 * @param cod_prod
	 * @param fec_crea
	 * @param usuario
	 * @param texto
	 */
	public CatalogacionMasivaLogDTO(long id , long cod_prod , String fec_crea , String usuario , String texto) {
		super();
		this.id = id;
		this.cod_prod = cod_prod;
		this.fec_crea = fec_crea;
		this.usuario = usuario;
		this.texto = texto;
	}
	/**
	 * @return Retorna cod_prod.
	 */
	public long getCod_prod() {
		return cod_prod;
	}
	/**
	 * @param cod_prod , cod_prod a modificar.
	 */
	public void setCod_prod(long cod_prod) {
		this.cod_prod = cod_prod;
	}
	/**
	 * @return Retorna fec_crea.
	 */
	public String getFec_crea() {
		return fec_crea;
	}
	/**
	 * @param fec_crea , fec_crea a modificar.
	 */
	public void setFec_crea(String fec_crea) {
		this.fec_crea = fec_crea;
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
	 * @return Retorna texto.
	 */
	public String getTexto() {
		return texto;
	}
	/**
	 * @param texto , texto a modificar.
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}
	/**
	 * @return Retorna usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario , usuario a modificar.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return Retorna cod_prod_bo.
	 */
	public long getCod_prod_bo() {
		return cod_prod_bo;
	}
	/**
	 * @param cod_prod_bo , cod_prod_bo a modificar.
	 */
	public void setCod_prod_bo(long cod_prod_bo) {
		this.cod_prod_bo = cod_prod_bo;
	}
}
