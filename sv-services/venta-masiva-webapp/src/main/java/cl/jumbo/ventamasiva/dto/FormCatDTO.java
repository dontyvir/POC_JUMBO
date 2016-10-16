package cl.jumbo.ventamasiva.dto;

import java.io.Serializable;

public class FormCatDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String actionForm;
	private String numeroEmpresa;
	private String numeroTransaccion;
	private String idCarroCompra;
	private String montoOperacion;
	private String urlNotificacion;
	private String tiempoSesion;
	private String tiempoNotificacion;
	
	public String getActionForm() {
		return actionForm;
	}
	public void setActionForm(String actionForm) {
		this.actionForm = actionForm;
	}
	public String getNumeroEmpresa() {
		return numeroEmpresa;
	}
	public void setNumeroEmpresa(String numeroEmpresa) {
		this.numeroEmpresa = numeroEmpresa;
	}
	public String getNumeroTransaccion() {
		return numeroTransaccion;
	}
	public void setNumeroTransaccion(String numeroTransaccion) {
		this.numeroTransaccion = numeroTransaccion;
	}
	public String getIdCarroCompra() {
		return idCarroCompra;
	}
	public void setIdCarroCompra(String idCarroCompra) {
		this.idCarroCompra = idCarroCompra;
	}
	public String getMontoOperacion() {
		return montoOperacion;
	}
	public void setMontoOperacion(String montoOperacion) {
		this.montoOperacion = montoOperacion;
	}
	public String getUrlNotificacion() {
		return urlNotificacion;
	}
	public void setUrlNotificacion(String urlNotificacion) {
		this.urlNotificacion = urlNotificacion;
	}
	public String getTiempoSesion() {
		return tiempoSesion;
	}
	public void setTiempoSesion(String tiempoSesion) {
		this.tiempoSesion = tiempoSesion;
	}
	public String getTiempoNotificacion() {
		return tiempoNotificacion;
	}
	public void setTiempoNotificacion(String tiempoNotificacion) {
		this.tiempoNotificacion = tiempoNotificacion;
	}
	
	public String toString() {
		return "FormCatDTO [actionForm=" + actionForm + ", numeroEmpresa="
				+ numeroEmpresa + ", numeroTransaccion=" + numeroTransaccion
				+ ", idCarroCompra=" + idCarroCompra + ", montoOperacion="
				+ montoOperacion + ", urlNotificacion=" + urlNotificacion
				+ ", tiempoSesion=" + tiempoSesion + ", tiempoNotificacion="
				+ tiempoNotificacion + "]";
	}
}
