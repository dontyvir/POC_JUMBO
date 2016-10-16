package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;

public class ProductosSapDTO implements Serializable{
	private long id;
	private String id_cat;
	private String uni_med;
	private String cod_prod_1;
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
	private int num_conv;
	private int den_conv;
	private String atrib9;
	private String atrib10;
	private String estActivo;
	
	//atributos encontrados....
	private String nom_cat_sap;
	private String fec_carga;
	private String flag_mix;
	
	//codigo categoria decodificado
	private String cat_seccion;
	private String cat_rubro;
	private String cat_subrubro;
	private String cat_grupo;
	
	//nuevos campos
	private String con_precio;
	
	
	/**
	 * @param cod_prod_1
	 * @param des_corta
	 * @param nom_cat_sap
	 * @param fec_carga
	 * @param flag_mix
	 */
	public ProductosSapDTO(long id,String cod_prod_1, String des_corta, String nom_cat_sap, String fec_carga, String flag_mix, String estado, String estActivo) {
		super();
		this.id = id;
		this.cod_prod_1 = cod_prod_1;
		this.des_corta = des_corta;
		this.nom_cat_sap = nom_cat_sap;
		this.fec_carga = fec_carga;
		this.flag_mix = flag_mix;
		this.estado = estado;
		this.estActivo = estActivo;
	}
	
	
	/**
	 * @param id
	 * @param id_cat
	 * @param uni_med
	 * @param cod_prod_1
	 * @param cod_prod_2
	 * @param des_corta
	 * @param des_larga
	 * @param estado
	 * @param marca
	 * @param cod_propal
	 * @param origen
	 * @param un_base
	 * @param ean13
	 * @param un_empaque
	 * @param num_conv
	 * @param den_conv
	 * @param atrib9
	 * @param atrib10
	 * @param estActivo
	 * @param nom_cat_sap
	 * @param fec_carga
	 * @param flag_mix
	 * @param nom_cat
	 */
	public ProductosSapDTO(long id, String id_cat, String uni_med, String cod_prod_1, String cod_prod_2, 
			String des_corta, String des_larga, String estado, String marca, String cod_propal, String origen, 
			String un_base, String ean13, String un_empaque, int num_conv, int den_conv, String atrib9, 
			String atrib10, String estActivo, String nom_cat_sap, String fec_carga, String flag_mix, String nom_cat) {
		super();
		this.id = id;
		this.id_cat = id_cat;
		this.uni_med = uni_med;
		this.cod_prod_1 = cod_prod_1;
		this.cod_prod_2 = cod_prod_2;
		this.des_corta = des_corta;
		this.des_larga = des_larga;
		this.estado = estado;
		this.marca = marca;
		this.cod_propal = cod_propal;
		this.origen = origen;
		this.un_base = un_base;
		this.ean13 = ean13;
		this.un_empaque = un_empaque;
		this.num_conv = num_conv;
		this.den_conv = den_conv;
		this.atrib9 = atrib9;
		this.atrib10 = atrib10;
		this.estActivo = estActivo;
		this.nom_cat_sap = nom_cat_sap;
		this.fec_carga = fec_carga;
		this.flag_mix = flag_mix;
		this.nom_cat_sap = nom_cat;
	}


	/**
	 * 
	 */
	public ProductosSapDTO() {
		super();
	}

	public String getCat_grupo() {
		return cat_grupo;
	}


	public String getCat_rubro() {
		return cat_rubro;
	}


	public String getCat_seccion() {
		return cat_seccion;
	}


	public String getCat_subrubro() {
		return cat_subrubro;
	}


	public void setCat_grupo(String cat_grupo) {
		this.cat_grupo = cat_grupo;
	}


	public void setCat_rubro(String cat_rubro) {
		this.cat_rubro = cat_rubro;
	}


	public void setCat_seccion(String cat_seccion) {
		this.cat_seccion = cat_seccion;
	}


	public void setCat_subrubro(String cat_subrubro) {
		this.cat_subrubro = cat_subrubro;
	}


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
	public int getDen_conv() {
		return den_conv;
	}

	/**
	 * @param den_conv The den_conv to set.
	 */
	public void setDen_conv(int den_conv) {
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
	 * @return Returns the fec_carga.
	 */
	public String getFec_carga() {
		return fec_carga;
	}

	/**
	 * @param fec_carga The fec_carga to set.
	 */
	public void setFec_carga(String fec_carga) {
		this.fec_carga = fec_carga;
	}

	/**
	 * @return Returns the flag_mix.
	 */
	public String getFlag_mix() {
		return flag_mix;
	}

	/**
	 * @param flag_mix The flag_mix to set.
	 */
	public void setFlag_mix(String flag_mix) {
		this.flag_mix = flag_mix;
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
	public int getNum_conv() {
		return num_conv;
	}

	/**
	 * @param num_conv The num_conv to set.
	 */
	public void setNum_conv(int num_conv) {
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
