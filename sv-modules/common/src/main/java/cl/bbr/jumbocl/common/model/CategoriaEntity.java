package cl.bbr.jumbocl.common.model;

import java.sql.Timestamp;


/**
 * Clase que captura desde la Base de Datos los datos de una categoria
 * @author bbr
 *
 */
public class CategoriaEntity {

	private Long id;
	private Long procat_id;
	private Long id_padre;
	private String nombre;
	private String descripcion;
	private String estado;
	private String tipo;
	private Integer orden;
	private Double porc_ranking;
	private String banner;
	private String nom_cat_padre;
	private Timestamp fec_crea;
	private Timestamp fec_mod;
	private Integer user_mod;
	private String con_pago;
	private String totem;
    private String imagen;    
    private String url_imagen;
	
	/**
	 * @return Retorna banner.
	 */
	public String getBanner() {
		return banner;
	}
	/**
	 * @param banner , banner a modificar.
	 */
	public void setBanner(String banner) {
		this.banner = banner;
	}
	/**
	 * @return Retorna descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion , descripcion a modificar.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	 * @return Retorna id_padre.
	 */
	public Long getId_padre() {
		return id_padre;
	}
	/**
	 * @param id_padre , id_padre a modificar.
	 */
	public void setId_padre(Long id_padre) {
		this.id_padre = id_padre;
	}
	/**
	 * @return Retorna nom_cat_padre.
	 */
	public String getNom_cat_padre() {
		return nom_cat_padre;
	}
	/**
	 * @param nom_cat_padre , nom_cat_padre a modificar.
	 */
	public void setNom_cat_padre(String nom_cat_padre) {
		this.nom_cat_padre = nom_cat_padre;
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
	 * @return Retorna orden.
	 */
	public Integer getOrden() {
		return orden;
	}
	/**
	 * @param orden , orden a modificar.
	 */
	public void setOrden(Integer orden) {
		this.orden = orden;
	}
	/**
	 * @return Retorna porc_ranking.
	 */
	public Double getPorc_ranking() {
		return porc_ranking;
	}
	/**
	 * @param porc_ranking , porc_ranking a modificar.
	 */
	public void setPorc_ranking(Double porc_ranking) {
		this.porc_ranking = porc_ranking;
	}
	/**
	 * @return Retorna tipo.
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param tipo , tipo a modificar.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	/**
	 * @return Retorna fec_crea.
	 */
	public Timestamp getFec_crea() {
		return fec_crea;
	}
	/**
	 * @param fec_crea , fec_crea a modificar.
	 */
	public void setFec_crea(Timestamp fec_crea) {
		this.fec_crea = fec_crea;
	}
	/**
	 * @return Retorna fec_mod.
	 */
	public Timestamp getFec_mod() {
		return fec_mod;
	}
	/**
	 * @param fec_mod , fec_mod a modificar.
	 */
	public void setFec_mod(Timestamp fec_mod) {
		this.fec_mod = fec_mod;
	}
	/**
	 * @return Retorna user_mod.
	 */
	public Integer getUser_mod() {
		return user_mod;
	}
	/**
	 * @param user_mod , user_mod a modificar.
	 */
	public void setUser_mod(Integer user_mod) {
		this.user_mod = user_mod;
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
	public Long getProcat_id() {
		return procat_id;
	}
	public void setProcat_id(Long procat_id) {
		this.procat_id = procat_id;
	}
	public String getTotem() {
		return totem;
	}
	public void setTotem(String totem) {
		this.totem = totem;
	}
	
    public String getImagen() {
        return imagen;
    }
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
	public String getUrl_imagen() {
		return url_imagen;
	}
	public void setUrl_imagen(String url_imagen) {
		this.url_imagen = url_imagen;
	}
	
}
