package cl.bbr.fo.faq.dto;

import java.io.Serializable;

/**
 * DTO para datos de las categorias. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class FaqCategoriaDTO implements Serializable {

	private long cat_id;
	private String nombre;
	private String estado;
	private String orden;
	
	public long getCat_id() {
		return cat_id;
	}
	public void setCat_id(long cat_id) {
		this.cat_id = cat_id;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	
	
}