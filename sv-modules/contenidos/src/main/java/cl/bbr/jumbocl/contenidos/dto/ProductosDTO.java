package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;
import java.util.List;

public class ProductosDTO implements Serializable{
// Ficha
	private long id;
	private long id_padre;
	private String tipre;
	private String cod_sap;
	private long uni_id;
	private long mar_id;
	private String estado;
	private String tipo;
	private String desc_corta;
	private String desc_larga;
	private String img_mini_ficha;
	private String img_ficha;
	
	/**banner para cualquier producto*/
	private String banner_prod;
	private String desc_banner_prod;
	private String color_banner_prod;
	
	private double unidad_medidad;
	private String uni_med_desc;
	private String valor_difer;
	private int rank_ventas;
	private String fec_crea;
	private String fec_mod;
	private long user_mod;
	private String generico;
	private double inter_valor;
	private double inter_max;
	private String adm_coment;
	private String es_prep;
	
	private String nom_marca;
	
	private long id_bo;
	
	private String es_pesable;
	
	private boolean evitarPubDes;
	
	private int motivoDesId;
    
    private double pilaPorcion;
    private long idPilaUnidad;
    
// Variable para flag publicado en Grability
    private boolean publicadoGrability;
	
// Categorias
	List lista_categorias;	
// Items
	List lista_items;	
// Sugeridos
	List lista_sugeridos;
	
	private String esParticionable;
	private Integer particion;
	
	
	/**
	 * @param id
	 * @param id_padre
	 * @param tipre
	 * @param cod_sap
	 * @param uni_id
	 * @param mar_id
	 * @param estado
	 * @param tipo
	 * @param desc_corta
	 * @param desc_larga
	 * @param img_mini_ficha
	 * @param img_ficha
	 * @param unidad_medidad
	 * @param valor_difer
	 * @param rank_ventas
	 * @param fec_crea
	 * @param fec_mod
	 * @param user_mod
	 * @param generico
	 * @param inter_valor
	 * @param inter_max
	 * @param adm_coment
	 * @param es_prep
	 * @param nom_marca
	 */
	public ProductosDTO(long id, long id_padre, String tipre, String cod_sap, long uni_id, long mar_id, 
			String estado, String tipo, String desc_corta, String desc_larga, String img_mini_ficha, 
			String img_ficha, double unidad_medidad, String valor_difer, int rank_ventas, String fec_crea, 
			String fec_mod, long user_mod, String generico, double inter_valor, double inter_max, String adm_coment, 
			String es_prep, String nom_marca) {
		super();
		this.id = id;
		this.id_padre = id_padre;
		this.tipre = tipre;
		this.cod_sap = cod_sap;
		this.uni_id = uni_id;
		this.mar_id = mar_id;
		this.estado = estado;
		this.tipo = tipo;
		this.desc_corta = desc_corta;
		this.desc_larga = desc_larga;
		this.img_mini_ficha = img_mini_ficha;
		this.img_ficha = img_ficha;
		this.unidad_medidad = unidad_medidad;
		this.valor_difer = valor_difer;
		this.rank_ventas = rank_ventas;
		this.fec_crea = fec_crea;
		this.fec_mod = fec_mod;
		this.user_mod = user_mod;
		this.generico = generico;
		this.inter_valor = inter_valor;
		this.inter_max = inter_max;
		this.adm_coment = adm_coment;
		this.es_prep = es_prep;
		this.nom_marca = nom_marca;
	}
	public ProductosDTO(){
		
	}
/**
	 * @param id
	 * @param estado
	 * @param desc_corta
	 * @param desc_larga
	 * @param generico
	 */
	public ProductosDTO(long id, String estado, String desc_corta, String desc_larga, String generico) {
		super();
		this.id = id;
		this.estado = estado;
		this.desc_corta = desc_corta;
		this.desc_larga = desc_larga;
		this.generico = generico;
	}
/**
	 * @param id
	 * @param id_padre
	 * @param tipre
	 * @param cod_sap
	 * @param uni_id
	 * @param mar_id
	 * @param estado
	 * @param tipo
	 * @param desc_corta
	 * @param desc_larga
	 * @param img_mini_ficha
	 * @param img_ficha
	 * @param unidad_medidad
	 * @param valor_difer
	 * @param rank_ventas
	 * @param fec_crea
	 * @param fec_mod
	 * @param user_mod
	 * @param generico
	 */
	public ProductosDTO(long id, long id_padre, String tipre, String cod_sap, long uni_id, long mar_id, String estado, 
			String tipo, String desc_corta, String desc_larga, String img_mini_ficha, String img_ficha, 
			double unidad_medidad, String valor_difer, int rank_ventas, String fec_crea, String fec_mod, 
			long user_mod, String generico, String nom_marca, String adm_coment, String es_prep, String es_pesable,
			double int_val, double int_max, List lista_items, List lista_sugeridos) {
		super();
		this.id = id;
		this.id_padre = id_padre;
		this.tipre = tipre;
		this.cod_sap = cod_sap;
		this.uni_id = uni_id;
		this.mar_id = mar_id;
		this.estado = estado;
		this.tipo = tipo;
		this.desc_corta = desc_corta;
		this.desc_larga = desc_larga;
		this.img_mini_ficha = img_mini_ficha;
		this.img_ficha = img_ficha;
		this.unidad_medidad = unidad_medidad;
		this.valor_difer = valor_difer;
		this.rank_ventas = rank_ventas;
		this.fec_crea = fec_crea;
		this.fec_mod = fec_mod;
		this.user_mod = user_mod;
		this.generico = generico;
		this.nom_marca = nom_marca;
		this.adm_coment = adm_coment;
		this.es_prep = es_prep;
		this.es_pesable = es_pesable;
		this.inter_valor = int_val;
		this.inter_max = int_max;
		this.lista_items = lista_items;
		this.lista_sugeridos = lista_sugeridos;
	}
	// Log : pendiente
	/**
	 * @param cod_sap
	 * @param estado
	 * @param generico
	 * @param tipo
	 * @param marca
	 * @param descr_corta
	 * @param descr_larga
	 * @param unidad_med
	 * @param contenido
	 * @param admite_com
	 * @param preparable
	 * @param unidad_venta
	 * @param intervalo_med
	 * @param maximo
	 * @param ranking
	 * @param fec_creacion
	 * @param fec_ult_mod
	 * @param usuario_ult_mod
	 * @param ruta_img_minificha
	 * @param ruta_img_ficha
	 * @param lista_categorias
	 * @param lista_items
	 * @param lista_sugeridos
	 */
	/*
	public ProductosDTO(String cod_sap, String estado, boolean generico, String tipo, String marca, String descr_corta, String descr_larga, String unidad_med, int contenido, boolean admite_com, boolean preparable, String unidad_venta, int intervalo_med, int maximo, int orden, int ranking, Timestamp fec_creacion, Timestamp fec_ult_mod, String usuario_ult_mod, String ruta_img_minificha, String ruta_img_ficha, List lista_categorias, List lista_items, List lista_sugeridos) {
		//se autogenera this.id_producto=id_producto;
		this.cod_sap = cod_sap;
		this.estado = estado;
		this.generico = generico;
		this.tipo = tipo;
		this.marca = marca;
		this.descr_corta = descr_corta;
		this.descr_larga = descr_larga;
		this.unidad_med = unidad_med;
		this.contenido = contenido;
		this.admite_com = admite_com;
		this.preparable = preparable;
		this.unidad_venta = unidad_venta;
		this.intervalo_med = intervalo_med;
		this.maximo = maximo;
		this.orden = orden;
		this.ranking = ranking;
		this.fec_creacion = fec_creacion;
		this.fec_ult_mod = fec_ult_mod;
		this.usuario_ult_mod = usuario_ult_mod;
		this.ruta_img_minificha = ruta_img_minificha;
		this.ruta_img_ficha = ruta_img_ficha;
		this.lista_categorias = lista_categorias;
		this.lista_items = lista_items;
		this.lista_sugeridos = lista_sugeridos;
	}*/
	/**
	 * @return Returns the cod_sap.
	 */
	public String getCod_sap() {
		return cod_sap;
	}
	/**
	 * @param cod_sap The cod_sap to set.
	 */
	public void setCod_sap(String cod_sap) {
		this.cod_sap = cod_sap;
	}
	/**
	 * @return Returns the desc_corta.
	 */
	public String getDesc_corta() {
		return desc_corta;
	}
	/**
	 * @param desc_corta The desc_corta to set.
	 */
	public void setDesc_corta(String desc_corta) {
		this.desc_corta = desc_corta;
	}
	/**
	 * @return Returns the desc_larga.
	 */
	public String getDesc_larga() {
		return desc_larga;
	}
	/**
	 * @param desc_larga The desc_larga to set.
	 */
	public void setDesc_larga(String desc_larga) {
		this.desc_larga = desc_larga;
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
	 * @return Returns the fec_crea.
	 */
	public String getFec_crea() {
		return fec_crea;
	}
	/**
	 * @param fec_crea The fec_crea to set.
	 */
	public void setFec_crea(String fec_crea) {
		this.fec_crea = fec_crea;
	}
	/**
	 * @return Returns the fec_mod.
	 */
	public String getFec_mod() {
		return fec_mod;
	}
	/**
	 * @param fec_mod The fec_mod to set.
	 */
	public void setFec_mod(String fec_mod) {
		this.fec_mod = fec_mod;
	}
	/**
	 * @return Returns the generico.
	 */
	public String getGenerico() {
		return generico;
	}
	/**
	 * @param generico The generico to set.
	 */
	public void setGenerico(String generico) {
		this.generico = generico;
	}
	/**
	 * @return Returns the id.
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return Returns the id_padre.
	 */
	public long getId_padre() {
		return id_padre;
	}
	/**
	 * @param id_padre The id_padre to set.
	 */
	public void setId_padre(long id_padre) {
		this.id_padre = id_padre;
	}
	/**
	 * @return Returns the img_ficha.
	 */
	public String getImg_ficha() {
		return img_ficha;
	}
	/**
	 * @param img_ficha The img_ficha to set.
	 */
	public void setImg_ficha(String img_ficha) {
		this.img_ficha = img_ficha;
	}
	/**
	 * @return Returns the img_mini_ficha.
	 */
	public String getImg_mini_ficha() {
		return img_mini_ficha;
	}
	/**
	 * @param img_mini_ficha The img_mini_ficha to set.
	 */
	public void setImg_mini_ficha(String img_mini_ficha) {
		this.img_mini_ficha = img_mini_ficha;
	}
	/**
	 * @return Returns the lista_categorias.
	 */
	public List getLista_categorias() {
		return lista_categorias;
	}
	/**
	 * @param lista_categorias The lista_categorias to set.
	 */
	public void setLista_categorias(List lista_categorias) {
		this.lista_categorias = lista_categorias;
	}
	/**
	 * @return Returns the lista_items.
	 */
	public List getLista_items() {
		return lista_items;
	}
	/**
	 * @param lista_items The lista_items to set.
	 */
	public void setLista_items(List lista_items) {
		this.lista_items = lista_items;
	}
	/**
	 * @return Returns the lista_sugeridos.
	 */
	public List getLista_sugeridos() {
		return lista_sugeridos;
	}
	/**
	 * @param lista_sugeridos The lista_sugeridos to set.
	 */
	public void setLista_sugeridos(List lista_sugeridos) {
		this.lista_sugeridos = lista_sugeridos;
	}
	/**
	 * @return Returns the mar_id.
	 */
	public long getMar_id() {
		return mar_id;
	}
	/**
	 * @param mar_id The mar_id to set.
	 */
	public void setMar_id(long mar_id) {
		this.mar_id = mar_id;
	}
	/**
	 * @return Returns the rank_ventas.
	 */
	public int getRank_ventas() {
		return rank_ventas;
	}
	/**
	 * @param rank_ventas The rank_ventas to set.
	 */
	public void setRank_ventas(int rank_ventas) {
		this.rank_ventas = rank_ventas;
	}
	/**
	 * @return Returns the tipo.
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	/**
	 * @return Returns the tipre.
	 */
	public String getTipre() {
		return tipre;
	}
	/**
	 * @param tipre The tipre to set.
	 */
	public void setTipre(String tipre) {
		this.tipre = tipre;
	}
	/**
	 * @return Returns the uni_id.
	 */
	public long getUni_id() {
		return uni_id;
	}
	/**
	 * @param uni_id The uni_id to set.
	 */
	public void setUni_id(long uni_id) {
		this.uni_id = uni_id;
	}
	/**
	 * @return Returns the unidad_medidad.
	 */
	public double getUnidad_medidad() {
		return unidad_medidad;
	}
	/**
	 * @param unidad_medidad The unidad_medidad to set.
	 */
	public void setUnidad_medidad(double unidad_medidad) {
		this.unidad_medidad = unidad_medidad;
	}
	/**
	 * @return Returns the user_mod.
	 */
	public long getUser_mod() {
		return user_mod;
	}
	/**
	 * @param user_mod The user_mod to set.
	 */
	public void setUser_mod(long user_mod) {
		this.user_mod = user_mod;
	}
	/**
	 * @return Returns the valor_difer.
	 */
	public String getValor_difer() {
		return valor_difer;
	}
	/**
	 * @param valor_difer The valor_difer to set.
	 */
	public void setValor_difer(String valor_difer) {
		this.valor_difer = valor_difer;
	}
	/**
	 * @return Returns the nom_marca.
	 */
	public String getNom_marca() {
		return nom_marca;
	}
	/**
	 * @param nom_marca The nom_marca to set.
	 */
	public void setNom_marca(String nom_marca) {
		this.nom_marca = nom_marca;
	}
	/**
	 * @return Returns the adm_coment.
	 */
	public String getAdm_coment() {
		return adm_coment;
	}
	/**
	 * @param adm_coment The adm_coment to set.
	 */
	public void setAdm_coment(String adm_coment) {
		this.adm_coment = adm_coment;
	}
	/**
	 * @return Returns the es_prep.
	 */
	public String getEs_prep() {
		return es_prep;
	}
	/**
	 * @param es_prep The es_prep to set.
	 */
	public void setEs_prep(String es_prep) {
		this.es_prep = es_prep;
	}
	/**
	 * @return Returns the inter_max.
	 */
	public double getInter_max() {
		return inter_max;
	}
	/**
	 * @param inter_max The inter_max to set.
	 */
	public void setInter_max(double inter_max) {
		this.inter_max = inter_max;
	}
	/**
	 * @return Returns the inter_valor.
	 */
	public double getInter_valor() {
		return inter_valor;
	}
	/**
	 * @param inter_valor The inter_valor to set.
	 */
	public void setInter_valor(double inter_valor) {
		this.inter_valor = inter_valor;
	}
	public String getUni_med_desc() {
		return uni_med_desc;
	}
	public void setUni_med_desc(String uni_med_desc) {
		this.uni_med_desc = uni_med_desc;
	}
	/**
	 * @return Returns the id_bo.
	 */
	public long getId_bo() {
		return id_bo;
	}
	/**
	 * @param id_bo The id_bo to set.
	 */
	public void setId_bo(long id_bo) {
		this.id_bo = id_bo;
	}
	/**
	 * @return Returns the es_pesable.
	 */
	public String getEs_pesable() {
		return es_pesable;
	}
	/**
	 * @param es_pesable The es_pesable to set.
	 */
	public void setEs_pesable(String es_pesable) {
		this.es_pesable = es_pesable;
	}
	
	
	
	
	
    /**
     * @return Devuelve esParticionable.
     */
    public String getEsParticionable() {
        return esParticionable;
    }
    /**
     * @return Devuelve particion.
     */
    public Integer getParticion() {
        return particion;
    }
    /**
     * @param esParticionable El esParticionable a establecer.
     */
    public void setEsParticionable(String esParticionable) {
        this.esParticionable = esParticionable;
    }
    /**
     * @param particion El particion a establecer.
     */
    public void setParticion(Integer particion) {
        this.particion = particion;
    }
    /**
     * @return Returns the evitarPubDes.
     */
    public boolean isEvitarPubDes() {
        return evitarPubDes;
    }
    /**
     * @param evitarPubDes The evitarPubDes to set.
     */
    public void setEvitarPubDes(boolean evitarPubDes) {
        this.evitarPubDes = evitarPubDes;
    }
   public int getMotivoDesId() {
      return motivoDesId;
   }
   public void setMotivoDesId(int motivoDesId) {
      this.motivoDesId = motivoDesId;
   }
    /**
     * @return Devuelve idPilaUnidad.
     */
    public long getIdPilaUnidad() {
        return idPilaUnidad;
    }
    /**
     * @return Devuelve pilaPorcion.
     */
    public double getPilaPorcion() {
        return pilaPorcion;
    }
    /**
     * @param idPilaUnidad El idPilaUnidad a establecer.
     */
    public void setIdPilaUnidad(long idPilaUnidad) {
        this.idPilaUnidad = idPilaUnidad;
    }
    /**
     * @param pilaPorcion El pilaPorcion a establecer.
     */
    public void setPilaPorcion(double pilaPorcion) {
        this.pilaPorcion = pilaPorcion;
    }
	/**
	 * @return el banner_prod
	 */
	public String getBanner_prod() {
		return banner_prod;
	}
	/**
	 * @param banner_prod el banner_prod a establecer
	 */
	public void setBanner_prod(String banner_prod) {
		this.banner_prod = banner_prod;
	}
	/**
	 * @return el desc_banner_prod
	 */
	public String getDesc_banner_prod() {
		return desc_banner_prod;
	}
	/**
	 * @param desc_banner_prod el desc_banner_prod a establecer
	 */
	public void setDesc_banner_prod(String desc_banner_prod) {
		this.desc_banner_prod = desc_banner_prod;
	}
	
	/**
	 * @return el color_banner_prod
	 */
	public String getColor_banner_prod() {
		return color_banner_prod;
	}
	/**
	 * @param color_banner_prod el color_banner_prod a establecer
	 */
	public void setColor_banner_prod(String color_banner_prod) {
		this.color_banner_prod = color_banner_prod;
	}
    
    /**
     * @return Returns the evitarPubDes.
     */
    public boolean isPublicadoGrability() {
        return publicadoGrability;
    }
    /**
     * @param evitarPubDes The evitarPubDes to set.
     */
    public void setPublicadoGrability(boolean publicadoGrability) {
        this.publicadoGrability = publicadoGrability;
    }
}