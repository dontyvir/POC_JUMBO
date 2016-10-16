package cl.bbr.jumbocl.common.model;

import java.sql.Timestamp;

/**
 * Clase que captura desde la base de datos los datos de un producto
 * @author bbr
 *
 */
public class ProductoEntity {
	private Long      id;
	private Long      id_padre;
	private String    tipre;//es la unidad de venta
	private String    cod_sap;
	private Long      uni_id;
	private Long      mar_id;
	private String    estado;
	private String    tipo;
	private String    desc_corta;
	private String    desc_larga;
	private String    img_mini_ficha;
	private String    img_ficha;
	
	/**banner para cualquier producto*/
	private String    banner_prod;
	private String    desc_banner_prod;
	private String    color_banner_prod;
	
	private Double    unidad_medidad;
	private String    uni_med_desc;
	private String    valor_difer;
	private Integer   rank_ventas;
	private Timestamp fec_crea;
	private Timestamp fec_mod;
	private Integer   user_mod;
	private String    generico;
	private Double    inter_valor;
	private Double    inter_max;
	private String    adm_coment;
	private String    es_prep;
	private Long      id_bo;
	private String    nom_marca;
	private String    es_pesable;
	private String    esParticionable;
	private Integer   particion;
	private boolean   evitarPubDes;
	private long      id_sector;
	private int		  pro_id_desp;
    private double    pilaPorcion;
    private long      idPilaUnidad;

    //20120907avc
	private Double impuesto;
	private int rubro;
	private String codBarra;
    private int idSeccion;	
    
    //Variable para flag publicado en Grability
    private boolean publicadoGrability;	
	
	/**
	 * @return Returns the impuesto.
	 */
	public Double getImpuesto() {
		return impuesto;
	}
	/**
	 * @param impuesto The impuesto to set.
	 */
	public void setImpuesto(Double impuesto) {
		this.impuesto = impuesto;
	}
	
	/**
	 * @return Returns the codBarra.
	 */	
    public String getCodBarra() {
        return codBarra;
    }
	/**
	 * @param codBarra The codBarra to set.
	 */    
    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }
	
	/**
	 * @return Returns the idSeccion.
	 */	    
    public int getIdSeccion() {
        return idSeccion;
    }
 	/**
	 * @param idSeccion The idSeccion to set.
	 */ 
    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }
	//-20120907avc

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
	public Timestamp getFec_crea() {
		return fec_crea;
	}
	/**
	 * @param fec_crea The fec_crea to set.
	 */
	public void setFec_crea(Timestamp fec_crea) {
		this.fec_crea = fec_crea;
	}
	/**
	 * @return Returns the fec_mod.
	 */
	public Timestamp getFec_mod() {
		return fec_mod;
	}
	/**
	 * @param fec_mod The fec_mod to set.
	 */
	public void setFec_mod(Timestamp fec_mod) {
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
	 * @return Returns the id_padre.
	 */
	public Long getId_padre() {
		return id_padre;
	}
	/**
	 * @param id_padre The id_padre to set.
	 */
	public void setId_padre(Long id_padre) {
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
	 * @return Returns the mar_id.
	 */
	public Long getMar_id() {
		return mar_id;
	}
	/**
	 * @param mar_id The mar_id to set.
	 */
	public void setMar_id(Long mar_id) {
		this.mar_id = mar_id;
	}
	/**
	 * @return Returns the rank_ventas.
	 */
	public Integer getRank_ventas() {
		return rank_ventas;
	}
	/**
	 * @param rank_ventas The rank_ventas to set.
	 */
	public void setRank_ventas(Integer rank_ventas) {
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
	public Long getUni_id() {
		return uni_id;
	}
	/**
	 * @param uni_id The uni_id to set.
	 */
	public void setUni_id(Long uni_id) {
		this.uni_id = uni_id;
	}
	/**
	 * @return Returns the unidad_medidad.
	 */
	public Double getUnidad_medidad() {
		return unidad_medidad;
	}
	/**
	 * @param unidad_medidad The unidad_medidad to set.
	 */
	public void setUnidad_medidad(Double unidad_medidad) {
		this.unidad_medidad = unidad_medidad;
	}
	/**
	 * @return Returns the user_mod.
	 */
	public Integer getUser_mod() {
		return user_mod;
	}
	/**
	 * @param user_mod The user_mod to set.
	 */
	public void setUser_mod(Integer user_mod) {
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
	public Double getInter_max() {
		return inter_max;
	}
	/**
	 * @param inter_max The inter_max to set.
	 */
	public void setInter_max(Double inter_max) {
		this.inter_max = inter_max;
	}
	/**
	 * @return Returns the inter_valor.
	 */
	public Double getInter_valor() {
		return inter_valor;
	}
	/**
	 * @param inter_valor The inter_valor to set.
	 */
	public void setInter_valor(Double inter_valor) {
		this.inter_valor = inter_valor;
	}
	/**
	 * @return uni_med_desc
	 */
	public String getUni_med_desc() {
		return uni_med_desc;
	}
	/**
	 * @param uni_med_desc
	 */
	public void setUni_med_desc(String uni_med_desc) {
		this.uni_med_desc = uni_med_desc;
	}
	/**
	 * @return id_bo
	 */
	public Long getId_bo() {
		return id_bo;
	}
	/**
	 * @param id_bo
	 */
	public void setId_bo(Long id_bo) {
		this.id_bo = id_bo;
	}
	/**
	 * @return es_pesable
	 */
	public String getEs_pesable() {
		return es_pesable;
	}
	/**
	 * @param es_pesable
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
    /**
     * @return Devuelve id_sector.
     */
    public long getId_sector() {
        return id_sector;
    }
    /**
     * @param id_sector El id_sector a establecer.
     */
    public void setId_sector(long id_sector) {
        this.id_sector = id_sector;
    }
   public int getPro_id_desp() {
      return pro_id_desp;
   }
   public void setPro_id_desp(int pro_id_desp) {
      this.pro_id_desp = pro_id_desp;
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

	public int getRubro() {
		return rubro;
	}

	public void setRubro(int rubro) {
		this.rubro = rubro;
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
	 * @return Returns the pubGrability.
	 */
    public boolean isPublicadoGrability(){
    	return publicadoGrability;
    }
	/**
	 * @param pubGrability The pubGrability to set.
	 */
    public void setPublicadoGrability(boolean publicadoGrability){
    	this.publicadoGrability = publicadoGrability;
    }
}