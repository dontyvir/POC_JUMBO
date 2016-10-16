package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;


public class ProductosSapCriteriaDTO implements Serializable{
	private final static long serialVersionUID = 1;
	
	private int pag;
	private String mix_opcion;
	private int regsperpage;
	private boolean pag_activa;
	private String codSapProd;
	private String codSapCat;
	private String cat_sel;
	//nuevo criterio de búsqueda
	private String con_precio;
	//id_cat de todos los productos asociados
	private String id_cat_all;

	
	/**
	 * @param pag
	 * @param mix_opcion
	 * @param regsperpage
	 * @param pag_activa
	 * @param codSapProd
	 * @param codSapCat
	 * @param cat_sel
	 */
	public ProductosSapCriteriaDTO(int pag, String mix_opcion, int regsperpage, boolean pag_activa, String codSapProd, String codSapCat, String cat_sel) {
		super();
		this.pag = pag;
		this.mix_opcion = mix_opcion;
		this.regsperpage = regsperpage;
		this.pag_activa = pag_activa;
		this.codSapProd = codSapProd;
		this.codSapCat = codSapCat;
		this.cat_sel = cat_sel;
	}

	public ProductosSapCriteriaDTO() {
	}
	
	/**
	 * @return Returns the cat_sel.
	 */
	public String getCat_sel() {
		return cat_sel;
	}
	/**
	 * @param cat_sel The cat_sel to set.
	 */
	public void setCat_sel(String cat_sel) {
		this.cat_sel = cat_sel;
	}



	
	

	/**
	 * @return Returns the mix_opcion.
	 */
	public String getMix_opcion() {
		return mix_opcion;
	}
	/**
	 * @return Returns the pag.
	 */
	public int getPag() {
		return pag;
	}
	/**
	 * @return Returns the pag_activa.
	 */
	public boolean isPag_activa() {
		return pag_activa;
	}
	/**
	 * @return Returns the regsperpage.
	 */
	public int getRegsperpage() {
		return regsperpage;
	}
	/**
	 * @param mix_opcion The mix_opcion to set.
	 */
	public void setMix_opcion(String mix_opcion) {
		this.mix_opcion = mix_opcion;
	}
	/**
	 * @param pag The pag to set.
	 */
	public void setPag(int pag) {
		this.pag = pag;
	}
	/**
	 * @param pag_activa The pag_activa to set.
	 */
	public void setPag_activa(boolean pag_activa) {
		this.pag_activa = pag_activa;
	}
	/**
	 * @param regsperpage The regsperpage to set.
	 */
	public void setRegsperpage(int regsperpage) {
		this.regsperpage = regsperpage;
	}



	/**
	 * @return Returns the codSapCat.
	 */
	public String getCodSapCat() {
		return codSapCat;
	}



	/**
	 * @param codSapCat The codSapCat to set.
	 */
	public void setCodSapCat(String codSapCat) {
		this.codSapCat = codSapCat;
	}



	/**
	 * @return Returns the codSapProd.
	 */
	public String getCodSapProd() {
		return codSapProd;
	}



	/**
	 * @param codSapProd The codSapProd to set.
	 */
	public void setCodSapProd(String codSapProd) {
		this.codSapProd = codSapProd;
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

	/**
	 * @return Returns the id_cat_all.
	 */
	public String getId_cat_all() {
		return id_cat_all;
	}

	/**
	 * @param id_cat_all The id_cat_all to set.
	 */
	public void setId_cat_all(String id_cat_all) {
		this.id_cat_all = id_cat_all;
	}
	
	
}
