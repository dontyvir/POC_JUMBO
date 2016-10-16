package cl.bbr.fo.marketing.dto;

import java.io.Serializable;

/**
 * DTO para datos de los elementos de Marketing. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class MarketingElementoDTO implements Serializable {

	private long ele_id;
	private long tipo;
	private String nombre;
	private String descripcion;
	private String estado;
	private String url;
	private long elca_eleid;
	private long elca_camid;
	private long click;
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public long getEle_id() {
		return ele_id;
	}
	public void setEle_id(long ele_id) {
		this.ele_id = ele_id;
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
	public long getTipo() {
		return tipo;
	}
	public void setTipo(long tipo) {
		this.tipo = tipo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getClick() {
		return click;
	}
	public void setClick(long click) {
		this.click = click;
	}
	public long getElca_camid() {
		return elca_camid;
	}
	public void setElca_camid(long elca_camid) {
		this.elca_camid = elca_camid;
	}
	public long getElca_eleid() {
		return elca_eleid;
	}
	public void setElca_eleid(long elca_eleid) {
		this.elca_eleid = elca_eleid;
	}
	
	
}