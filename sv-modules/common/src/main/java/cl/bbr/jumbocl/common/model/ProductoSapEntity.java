package cl.bbr.jumbocl.common.model;

import java.sql.Timestamp;

/**
 * Clase que captura desde la base de datos los datos de un producto Sap
 * @author bbr
 *
 */
public class ProductoSapEntity {
	private Long id;
	private String id_cat;
	private String uni_med;
	private String cod_prod_1;	//codigo_sap
	private String cod_prod_2;
	private String des_corta;
	private String des_larga;
	private String estado;
	//private String cod_categ;
	private String marca;
	private String cod_propal;
	private String origen;
	private String un_base;
	private String ean13;
	private String un_empaque;
	private Integer num_conv;
	private Integer den_conv;
	private String atrib9;
	private String atrib10;
	private Timestamp fecCarga;
	private String mixWeb;
	private String estActivo;
	private String nom_cat_sap;
	
	//para la categoria decodificada
	private String seccion;
	private String rubro;
	private String subrubro;
	private String grupo;
	
	//nuevos campos
	private String con_precio;
	
	/**
	 * @return grupo
	 */
	public String getGrupo() {
		return grupo;
	}
	/**
	 * @return rubro
	 */
	public String getRubro() {
		return rubro;
	}
	/**
	 * @return seccion
	 */
	public String getSeccion() {
		return seccion;
	}
	/**
	 * @return subrubro
	 */
	public String getSubrubro() {
		return subrubro;
	}
	/**
	 * @param grupo
	 */
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	/**
	 * @param rubro
	 */
	public void setRubro(String rubro) {
		this.rubro = rubro;
	}
	/**
	 * @param seccion
	 */
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}
	/**
	 * @param subrubro
	 */
	public void setSubrubro(String subrubro) {
		this.subrubro = subrubro;
	} 
	//
	/**
	 * @return Returns the atrib10.
	 */
	public String getAtrib10() {
		return atrib10;
	}
	/**
	 * @param atrib10 The atrib10 to set.
	 */
	public void setAtrib10(String atrib10) {
		this.atrib10 = atrib10;
	}
	/**
	 * @return Returns the atrib9.
	 */
	public String getAtrib9() {
		return atrib9;
	}
	/**
	 * @param atrib9 The atrib9 to set.
	 */
	public void setAtrib9(String atrib9) {
		this.atrib9 = atrib9;
	}
	/**
	 * @return Returns the cod_prod_1.
	 */
	public String getCod_prod_1() {
		return cod_prod_1;
	}
	/**
	 * @param cod_prod_1 The cod_prod_1 to set.
	 */
	public void setCod_prod_1(String cod_prod_1) {
		this.cod_prod_1 = cod_prod_1;
	}
	/**
	 * @return Returns the cod_prod_2.
	 */
	public String getCod_prod_2() {
		return cod_prod_2;
	}
	/**
	 * @param cod_prod_2 The cod_prod_2 to set.
	 */
	public void setCod_prod_2(String cod_prod_2) {
		this.cod_prod_2 = cod_prod_2;
	}
	/**
	 * @return Returns the cod_propal.
	 */
	public String getCod_propal() {
		return cod_propal;
	}
	/**
	 * @param cod_propal The cod_propal to set.
	 */
	public void setCod_propal(String cod_propal) {
		this.cod_propal = cod_propal;
	}
	/**
	 * @return Returns the den_conv.
	 */
	public Integer getDen_conv() {
		return den_conv;
	}
	/**
	 * @param den_conv The den_conv to set.
	 */
	public void setDen_conv(Integer den_conv) {
		this.den_conv = den_conv;
	}
	/**
	 * @return Returns the des_corta.
	 */
	public String getDes_corta() {
		return des_corta;
	}
	/**
	 * @param des_corta The des_corta to set.
	 */
	public void setDes_corta(String des_corta) {
		this.des_corta = des_corta;
	}
	/**
	 * @return Returns the des_larga.
	 */
	public String getDes_larga() {
		return des_larga;
	}
	/**
	 * @param des_larga The des_larga to set.
	 */
	public void setDes_larga(String des_larga) {
		this.des_larga = des_larga;
	}
	/**
	 * @return Returns the ean13.
	 */
	public String getEan13() {
		return ean13;
	}
	/**
	 * @param ean13 The ean13 to set.
	 */
	public void setEan13(String ean13) {
		this.ean13 = ean13;
	}
	/**
	 * @return Returns the estActivo.
	 */
	public String getEstActivo() {
		return estActivo;
	}
	/**
	 * @param estActivo The estActivo to set.
	 */
	public void setEstActivo(String estActivo) {
		this.estActivo = estActivo;
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
	 * @return Returns the fecCarga.
	 */
	public Timestamp getFecCarga() {
		return fecCarga;
	}
	/**
	 * @param fecCarga The fecCarga to set.
	 */
	public void setFecCarga(Timestamp fecCarga) {
		this.fecCarga = fecCarga;
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
	public String getId_cat() {
		return id_cat;
	}
	/**
	 * @param id_cat The id_cat to set.
	 */
	public void setId_cat(String id_cat) {
		this.id_cat = id_cat;
	}
	/**
	 * @return Returns the marca.
	 */
	public String getMarca() {
		return marca;
	}
	/**
	 * @param marca The marca to set.
	 */
	public void setMarca(String marca) {
		this.marca = marca;
	}
	/**
	 * @return Returns the mixWeb.
	 */
	public String getMixWeb() {
		return mixWeb;
	}
	/**
	 * @param mixWeb The mixWeb to set.
	 */
	public void setMixWeb(String mixWeb) {
		this.mixWeb = mixWeb;
	}
	/**
	 * @return Returns the nom_cat_sap.
	 */
	public String getNom_cat_sap() {
		return nom_cat_sap;
	}
	/**
	 * @param nom_cat_sap The nom_cat_sap to set.
	 */
	public void setNom_cat_sap(String nom_cat_sap) {
		this.nom_cat_sap = nom_cat_sap;
	}
	/**
	 * @return Returns the num_conv.
	 */
	public Integer getNum_conv() {
		return num_conv;
	}
	/**
	 * @param num_conv The num_conv to set.
	 */
	public void setNum_conv(Integer num_conv) {
		this.num_conv = num_conv;
	}
	/**
	 * @return Returns the origen.
	 */
	public String getOrigen() {
		return origen;
	}
	/**
	 * @param origen The origen to set.
	 */
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	/**
	 * @return Returns the un_base.
	 */
	public String getUn_base() {
		return un_base;
	}
	/**
	 * @param un_base The un_base to set.
	 */
	public void setUn_base(String un_base) {
		this.un_base = un_base;
	}
	/**
	 * @return Returns the un_empaque.
	 */
	public String getUn_empaque() {
		return un_empaque;
	}
	/**
	 * @param un_empaque The un_empaque to set.
	 */
	public void setUn_empaque(String un_empaque) {
		this.un_empaque = un_empaque;
	}
	/**
	 * @return Returns the uni_med.
	 */
	public String getUni_med() {
		return uni_med;
	}
	/**
	 * @param uni_med The uni_med to set.
	 */
	public void setUni_med(String uni_med) {
		this.uni_med = uni_med;
	}
	/**
	 * @return Returns the con_precio.
	 */
	public String getCon_precio() {
		return con_precio;
	}
	/**
	 * @param con_precio The con_precio to set.
	 */
	public void setCon_precio(String con_precio) {
		this.con_precio = con_precio;
	}
	
	
}
