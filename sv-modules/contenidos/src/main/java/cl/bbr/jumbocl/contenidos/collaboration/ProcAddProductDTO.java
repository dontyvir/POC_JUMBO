package cl.bbr.jumbocl.contenidos.collaboration;

import java.util.List;

/**
 * Clase que contiene información del nuevo producto a agregar. 
 * @author BBR
 *
 */
public class ProcAddProductDTO {
	
	/**
	 * Código Sap del producto. 
	 */
	private String cod_sap;
	
	/**
	 * Estado del producto. 
	 */
	private String estado;
	
	/**
	 * Flag genérico del producto. 
	 */
	private String generico;
	
	/**
	 * Tipo del producto. 
	 */
	private String tipo = "";
	
	/**
	 * Marca del producto. 
	 */
	private int marca;
	
	/**
	 * Descripción corta del producto.
	 */
	private String desc_corta;
	
	/**
	 * Descripción larga del producto. 
	 */
	private String desc_larga;
	
	/**
	 * Atributo diferenciador del producto. 
	 */
	private String atr_difer;
	
	/**
	 * Unidad de medida del producto.
	 */
	private int uni_medida;
	
	/**
	 * Contenido del producto. 
	 */
	private double contenido;
	
	/**
	 * Flag de admisión de comentarios del producto. 
	 */
	private String adm_coment = "";
	
	/**
	 * Flag de indicación si es preparable del producto. 
	 */
	private String es_prepar = "";
	
	/**
	 * Unidad de venta del producto. 
	 */
	private String uni_vta;
	
	/**
	 * Intervalo inicial de medida. 
	 */
	private double int_medida;
	
	/**
	 * Intervalo máximo de medida. 
	 */
	private double int_max;
	
	/**
	 * Fecha de creación. 
	 */
	private String fecha;
	
	/**
	 * Identificador del usuario 
	 */
	private long id_user;
	
	/**
	 * Ruta de imagen mini ficha. 
	 */
	private String img_min_ficha;
	
	/**
	 * Ruta de imagen de ficha. 
	 */
	private String img_ficha;
	
	/**
	 * Id del producto SAP. 
	 */
	private long id_bo;
	
	/**
	 * Lista de categorias relacionadas. 
	 */
	private List lst_categorias;
	
	/**
	 * Lista de items relacionadas. 
	 */
	private List lst_items;
	
	/**
	 * Lista de productos sugeridos. 
	 */
	private List lst_sugeridos;
	
	
	/**
	 * @return Retorna el adm_coment.
	 */
	public String getAdm_coment() {
		return adm_coment;
	}
	/**
	 * @param adm_coment  , adm_coment a modificar.
	 */
	public void setAdm_coment(String adm_coment) {
		this.adm_coment = adm_coment;
	}
	/**
	 * @return Retorna el atr_difer.
	 */
	public String getAtr_difer() {
		return atr_difer;
	}
	/**
	 * @param atr_difer  , atr_difer a modificar.
	 */
	public void setAtr_difer(String atr_difer) {
		this.atr_difer = atr_difer;
	}
	/**
	 * @return Retorna el cod_sap.
	 */
	public String getCod_sap() {
		return cod_sap;
	}
	/**
	 * @param cod_sap , cod_sap a modificar.
	 */
	public void setCod_sap(String cod_sap) {
		this.cod_sap = cod_sap;
	}
	/**
	 * @return Retorna el contenido.
	 */
	public double getContenido() {
		return contenido;
	}
	/**
	 * @param contenido , contenido a modificar.
	 */
	public void setContenido(double contenido) {
		this.contenido = contenido;
	}
	/**
	 * @return Retorna el desc_corta.
	 */
	public String getDesc_corta() {
		return desc_corta;
	}
	/**
	 * @param desc_corta , desc_corta a modificar.
	 */
	public void setDesc_corta(String desc_corta) {
		this.desc_corta = desc_corta;
	}
	/**
	 * @return Retorna el desc_larga.
	 */
	public String getDesc_larga() {
		return desc_larga;
	}
	/**
	 * @param desc_larga , desc_larga a modificar.
	 */
	public void setDesc_larga(String desc_larga) {
		this.desc_larga = desc_larga;
	}
	/**
	 * @return Retorna el es_prepar.
	 */
	public String getEs_prepar() {
		return es_prepar;
	}
	/**
	 * @param es_prepar , es_prepar a modificar.
	 */
	public void setEs_prepar(String es_prepar) {
		this.es_prepar = es_prepar;
	}
	/**
	 * @return Retorna el estado.
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
	 * @return Retorna el fecha.
	 */
	public String getFecha() {
		return fecha;
	}
	/**
	 * @param fecha , fecha a modificar.
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return Retorna el generico.
	 */
	public String getGenerico() {
		return generico;
	}
	/**
	 * @param generico , generico a modificar.
	 */
	public void setGenerico(String generico) {
		this.generico = generico;
	}
	/**
	 * @return Retorna el img_ficha.
	 */
	public String getImg_ficha() {
		return img_ficha;
	}
	/**
	 * @param img_ficha , img_ficha a modificar.
	 */
	public void setImg_ficha(String img_ficha) {
		this.img_ficha = img_ficha;
	}
	/**
	 * @return Retorna el img_min_ficha.
	 */
	public String getImg_min_ficha() {
		return img_min_ficha;
	}
	/**
	 * @param img_min_ficha , img_min_ficha a modificar.
	 */
	public void setImg_min_ficha(String img_min_ficha) {
		this.img_min_ficha = img_min_ficha;
	}
	/**
	 * @return Retorna el int_max.
	 */
	public double getInt_max() {
		return int_max;
	}
	/**
	 * @param int_max , int_max a modificar.
	 */
	public void setInt_max(double int_max) {
		this.int_max = int_max;
	}
	/**
	 * @return Retorna el int_medida.
	 */
	public double getInt_medida() {
		return int_medida;
	}
	/**
	 * @param int_medida , int_medida a modificar.
	 */
	public void setInt_medida(double int_medida) {
		this.int_medida = int_medida;
	}
	/**
	 * @return Retorna el lst_categorias.
	 */
	public List getLst_categorias() {
		return lst_categorias;
	}
	/**
	 * @param lst_categorias , lst_categorias a modificar.
	 */
	public void setLst_categorias(List lst_categorias) {
		this.lst_categorias = lst_categorias;
	}
	/**
	 * @return Retorna el lst_items.
	 */
	public List getLst_items() {
		return lst_items;
	}
	/**
	 * @param lst_items , lst_items a modificar.
	 */
	public void setLst_items(List lst_items) {
		this.lst_items = lst_items;
	}
	/**
	 * @return Retorna el lst_sugeridos.
	 */
	public List getLst_sugeridos() {
		return lst_sugeridos;
	}
	/**
	 * @param lst_sugeridos , lst_sugeridos a modificar.
	 */
	public void setLst_sugeridos(List lst_sugeridos) {
		this.lst_sugeridos = lst_sugeridos;
	}
	/**
	 * @return Retorna el marca.
	 */
	public int getMarca() {
		return marca;
	}
	/**
	 * @param marca , marca a modificar.
	 */
	public void setMarca(int marca) {
		this.marca = marca;
	}
	/**
	 * @return Retorna el tipo.
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
	 * @return Retorna el uni_medida.
	 */
	public int getUni_medida() {
		return uni_medida;
	}
	/**
	 * @param uni_medida , uni_medida a modificar.
	 */
	public void setUni_medida(int uni_medida) {
		this.uni_medida = uni_medida;
	}
	/**
	 * @return Retorna el uni_vta.
	 */
	public String getUni_vta() {
		return uni_vta;
	}
	/**
	 * @param uni_vta , uni_vta a modificar.
	 */
	public void setUni_vta(String uni_vta) {
		this.uni_vta = uni_vta;
	}
	/**
	 * @return Retorna el id_user.
	 */
	public long getId_user() {
		return id_user;
	}
	/**
	 * @param id_user , id_user a modificar.
	 */
	public void setId_user(long id_user) {
		this.id_user = id_user;
	}
	/**
	 * @return Retorna el id_bo.
	 */
	public long getId_bo() {
		return id_bo;
	}
	/**
	 * @param id_bo , id_bo a modificar.
	 */
	public void setId_bo(long id_bo) {
		this.id_bo = id_bo;
	}
}
