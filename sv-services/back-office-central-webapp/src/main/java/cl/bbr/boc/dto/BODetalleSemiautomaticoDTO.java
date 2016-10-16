/*
 * Created on 20-may-2010
 */
package cl.bbr.boc.dto;

/**
 * @author hs
 */
public class BODetalleSemiautomaticoDTO {
	
	private String 	sku;//1236666
	private int 	id;//43977
	private String 	estadoInicial;//CON STOCK
	private String 	estadoFinal;//SIN STOCK
	private String 	local;//7613033291401
	private String 	idSeccion;//	03
	private String 	seccion;//LACTEOS
	private String 	idRubro;//0301
	private String 	rubro;//RUBRO LECHES FLUIDAS
	private String 	idSubrubro;//030105
	private String  subrubro;////SUBRUBRO LECHES L.V. DESC
	
	private String cambioEstado;
	
	public String getCambioEstado() {
		return cambioEstado;
	}
	public void setCambioEstado(String cambioEstado) {
		this.cambioEstado = cambioEstado;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEstadoInicial() {
		return estadoInicial;
	}
	public void setEstadoInicial(String estadoInicial) {
		this.estadoInicial = estadoInicial;
	}
	public String getEstadoFinal() {
		return estadoFinal;
	}
	public void setEstadoFinal(String estadoFinal) {
		this.estadoFinal = estadoFinal;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getIdSeccion() {
		return idSeccion;
	}
	public void setIdSeccion(String idSeccion) {
		this.idSeccion = idSeccion;
	}
	public String getSeccion() {
		return seccion;
	}
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}
	public String getIdRubro() {
		return idRubro;
	}
	public void setIdRubro(String idRubro) {
		this.idRubro = idRubro;
	}
	public String getRubro() {
		return rubro;
	}
	public void setRubro(String rubro) {
		this.rubro = rubro;
	}
	public String getIdSubrubro() {
		return idSubrubro;
	}
	public void setIdSubrubro(String idSubrubro) {
		this.idSubrubro = idSubrubro;
	}
	public String getSubrubro() {
		return subrubro;
	}
	public void setSubrubro(String subrubro) {
		this.subrubro = subrubro;
	}

  	    

}
