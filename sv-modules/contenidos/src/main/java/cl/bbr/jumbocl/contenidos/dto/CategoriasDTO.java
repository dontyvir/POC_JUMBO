package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;


/**
 * Clase que muestra informacion de Categoria Web.
 * 
 * @author BBR
 *
 */
public class CategoriasDTO implements Serializable{
	private final static long serialVersionUID = 1;
	
	/**
	 * Id de categoria 
	 */
	private long id_cat;
	
	/**
	 * Id de categortia padre 
	 */
	private long id_cat_padre;
	
	/**
	 * Id de categortia producto
	 */
	private long id_procat;
	/**
	 * Nombre 
	 */
	private String nombre;
	
	/**
	 * Descripcion 
	 */
	private String descripcion;
	
	/**
	 * Estado 
	 */
	private String estado;
	
	/**
	 * Tipo
	 */
	private String tipo;
	
	/**
	 * Orden en el listado
	 */
	private int orden;
	
	/**
	 * Porcentaje de ranking
	 */
	private double porc_ranking;
	
	/**
	 * Banner 
	 */
	private String banner;
	
	/**
	 * Nombre de la categoria padre
	 */
	private String nom_cat_padre;
	
	/**
	 * Fecha de creacion
	 */
	private String fec_crea;
	
	/**
	 * Fecha de modificacion
	 */
	private String fec_mod;
	
	/**
	 * Usuario de modificacion. 
	 */
	private int user_mod;
	
	/**
	 * Indicador si tiene pago
	 */
	private String con_pago;
	
	/**
	 * Nombre del totem de categoria
	 */
	private String totem;
    
    /**
     * Nombre de la imagen de categoria
     */
    private String imagen;
    
    /**
     * Url imagen
     */
    private String url_banner;
	
	
	public CategoriasDTO() {
	}
	
	/**
	 * @param id_cat
	 * @param nombre
	 * @param orden
	 */
	public CategoriasDTO(long id_cat , String nombre , int orden) {
		super();
		this.id_cat = id_cat;
		this.nombre = nombre;
		this.orden = orden;
	}

	/**
	 * @param id_cat
	 * @param id_cat_padre
	 * @param nombre
	 * @param descripcion
	 * @param estado
	 * @param tipo
	 * @param orden
	 * @param porc_ranking
	 * @param banner
	 * @param nom_cat_padre
	 */
	public CategoriasDTO(long id_cat , long id_cat_padre , String nombre , String descripcion , String estado , 
			String tipo , int orden , double porc_ranking , String banner , String nom_cat_padre) {
		super();
		this.id_cat = id_cat;
		this.id_cat_padre = id_cat_padre;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.estado = estado;
		this.tipo = tipo;
		this.orden = orden;
		this.porc_ranking = porc_ranking;
		this.banner = banner;
		this.nom_cat_padre = nom_cat_padre;
	}

	/**
	 * @param id_cat
	 * @param id_cat_padre
	 * @param nombre
	 * @param descripcion
	 * @param estado
	 * @param tipo
	 * @param orden
	 * @param porc_ranking
	 * @param banner
	 * @param nom_cat_padre
	 * @param fec_crea
	 * @param fec_mod
	 * @param user_mod
	 */
	public CategoriasDTO(long id_cat , long id_cat_padre , String nombre , String descripcion , String estado , 
			String tipo , int orden , double porc_ranking , String banner , String nom_cat_padre,
			String fec_crea , String fec_mod , int user_mod) {
		super();
		this.id_cat = id_cat;
		this.id_cat_padre = id_cat_padre;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.estado = estado;
		this.tipo = tipo;
		this.orden = orden;
		this.porc_ranking = porc_ranking;
		this.banner = banner;
		this.nom_cat_padre = nom_cat_padre;
		this.fec_crea = fec_crea;
		this.fec_mod = fec_mod;
		this.user_mod = user_mod;
	}

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
	 * @return Retorna id_cat_padre.
	 */
	public long getId_cat_padre() {
		return id_cat_padre;
	}

	/**
	 * @param id_cat_padre , id_cat_padre a modificar.
	 */
	public void setId_cat_padre(long id_cat_padre) {
		this.id_cat_padre = id_cat_padre;
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
	public int getOrden() {
		return orden;
	}

	/**
	 * @param orden , orden a modificar.
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}

	/**
	 * @return Retorna porc_ranking.
	 */
	public double getPorc_ranking() {
		return porc_ranking;
	}

	/**
	 * @param porc_ranking , porc_ranking a modificar.
	 */
	public void setPorc_ranking(double porc_ranking) {
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
	 * @return Retorna fec_mod.
	 */
	public String getFec_mod() {
		return fec_mod;
	}

	/**
	 * @param fec_mod , fec_mod a modificar.
	 */
	public void setFec_mod(String fec_mod) {
		this.fec_mod = fec_mod;
	}

	/**
	 * @return Retorna user_mod.
	 */
	public int getUser_mod() {
		return user_mod;
	}

	/**
	 * @param user_mod , user_mod a modificar.
	 */
	public void setUser_mod(int user_mod) {
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

	public long getId_procat() {
		return id_procat;
	}

	public void setId_procat(long id_procat) {
		this.id_procat = id_procat;
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
    
	public String getUrl_banner() {
		return url_banner;
	}

	public void setUrl_banner(String url_banner) {
		this.url_banner = url_banner;
	}
	
}
