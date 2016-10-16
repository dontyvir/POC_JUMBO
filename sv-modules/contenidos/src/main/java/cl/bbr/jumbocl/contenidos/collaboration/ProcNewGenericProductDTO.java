package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

public class ProcNewGenericProductDTO implements Serializable{
      private String atrdiff_nom; // obligatorio
      private String tipo; 
      private String marca; 
      private String descr_corta; 
      private String desc_larga; 
      private String umedida; 
      private long contenido; 
      private boolean admite_com; 
      private boolean preparable; 
      private long interv_med; 
      private long maximo; 
      private long orden; 
      private String img1; 
      private String img2;
	/**
	 * 
	 */
	public ProcNewGenericProductDTO() {
	}
	/**
	 * @param atrdiff_nom
	 * @param tipo
	 * @param marca
	 * @param descr_corta
	 * @param desc_larga
	 * @param umedida
	 * @param contenido
	 * @param admite_com
	 * @param preparable
	 * @param interv_med
	 * @param maximo
	 * @param orden
	 * @param img1
	 * @param img2
	 */
	public ProcNewGenericProductDTO(String atrdiff_nom, String tipo, String marca, String descr_corta, String desc_larga, String umedida, long contenido, boolean admite_com, boolean preparable, long interv_med, long maximo, long orden, String img1, String img2) {
		this.atrdiff_nom = atrdiff_nom;
		this.tipo = tipo;
		this.marca = marca;
		this.descr_corta = descr_corta;
		this.desc_larga = desc_larga;
		this.umedida = umedida;
		this.contenido = contenido;
		this.admite_com = admite_com;
		this.preparable = preparable;
		this.interv_med = interv_med;
		this.maximo = maximo;
		this.orden = orden;
		this.img1 = img1;
		this.img2 = img2;
	}
	/**
	 * @return Returns the admite_com.
	 */
	public boolean isAdmite_com() {
		return admite_com;
	}
	/**
	 * @return Returns the atrdiff_nom.
	 */
	public String getAtrdiff_nom() {
		return atrdiff_nom;
	}
	/**
	 * @return Returns the contenido.
	 */
	public long getContenido() {
		return contenido;
	}
	/**
	 * @return Returns the desc_larga.
	 */
	public String getDesc_larga() {
		return desc_larga;
	}
	/**
	 * @return Returns the descr_corta.
	 */
	public String getDescr_corta() {
		return descr_corta;
	}
	/**
	 * @return Returns the img1.
	 */
	public String getImg1() {
		return img1;
	}
	/**
	 * @return Returns the img2.
	 */
	public String getImg2() {
		return img2;
	}
	/**
	 * @return Returns the interv_med.
	 */
	public long getInterv_med() {
		return interv_med;
	}
	/**
	 * @return Returns the marca.
	 */
	public String getMarca() {
		return marca;
	}
	/**
	 * @return Returns the maximo.
	 */
	public long getMaximo() {
		return maximo;
	}
	/**
	 * @return Returns the orden.
	 */
	public long getOrden() {
		return orden;
	}
	/**
	 * @return Returns the preparable.
	 */
	public boolean isPreparable() {
		return preparable;
	}
	/**
	 * @return Returns the tipo.
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @return Returns the umedida.
	 */
	public String getUmedida() {
		return umedida;
	}
	/**
	 * @param admite_com The admite_com to set.
	 */
	public void setAdmite_com(boolean admite_com) {
		this.admite_com = admite_com;
	}
	/**
	 * @param atrdiff_nom The atrdiff_nom to set.
	 */
	public void setAtrdiff_nom(String atrdiff_nom) {
		this.atrdiff_nom = atrdiff_nom;
	}
	/**
	 * @param contenido The contenido to set.
	 */
	public void setContenido(long contenido) {
		this.contenido = contenido;
	}
	/**
	 * @param desc_larga The desc_larga to set.
	 */
	public void setDesc_larga(String desc_larga) {
		this.desc_larga = desc_larga;
	}
	/**
	 * @param descr_corta The descr_corta to set.
	 */
	public void setDescr_corta(String descr_corta) {
		this.descr_corta = descr_corta;
	}
	/**
	 * @param img1 The img1 to set.
	 */
	public void setImg1(String img1) {
		this.img1 = img1;
	}
	/**
	 * @param img2 The img2 to set.
	 */
	public void setImg2(String img2) {
		this.img2 = img2;
	}
	/**
	 * @param interv_med The interv_med to set.
	 */
	public void setInterv_med(long interv_med) {
		this.interv_med = interv_med;
	}
	/**
	 * @param marca The marca to set.
	 */
	public void setMarca(String marca) {
		this.marca = marca;
	}
	/**
	 * @param maximo The maximo to set.
	 */
	public void setMaximo(long maximo) {
		this.maximo = maximo;
	}
	/**
	 * @param orden The orden to set.
	 */
	public void setOrden(long orden) {
		this.orden = orden;
	}
	/**
	 * @param preparable The preparable to set.
	 */
	public void setPreparable(boolean preparable) {
		this.preparable = preparable;
	}
	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	/**
	 * @param umedida The umedida to set.
	 */
	public void setUmedida(String umedida) {
		this.umedida = umedida;
	} 
	
      
}
