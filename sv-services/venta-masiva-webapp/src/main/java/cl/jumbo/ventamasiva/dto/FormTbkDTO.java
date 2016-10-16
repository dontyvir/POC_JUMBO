package cl.jumbo.ventamasiva.dto;

import java.io.Serializable;

public class FormTbkDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String actionForm;
	private String tipoTransaccion;;
	private String urlExito;
	private String urlFracaso;
	private String idSession;
	private String ordenCompra;
	private String monto;
	public String getActionForm() {
		return actionForm;
	}
	public void setActionForm(String actionForm) {
		this.actionForm = actionForm;
	}
	public String getTipoTransaccion() {
		return tipoTransaccion;
	}
	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}
	public String getUrlExito() {
		return urlExito;
	}
	public void setUrlExito(String urlExito) {
		this.urlExito = urlExito;
	}
	public String getUrlFracaso() {
		return urlFracaso;
	}
	public void setUrlFracaso(String urlFracaso) {
		this.urlFracaso = urlFracaso;
	}
	public String getIdSession() {
		return idSession;
	}
	public void setIdSession(String idSession) {
		this.idSession = idSession;
	}
	public String getOrdenCompra() {
		return ordenCompra;
	}
	public void setOrdenCompra(String ordenCompra) {
		this.ordenCompra = ordenCompra;
	}
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public String toString() {
		return "FormTbkDTO [actionForm=" + actionForm + ", tipoTransaccion="
				+ tipoTransaccion + ", urlExito=" + urlExito + ", urlFracaso="
				+ urlFracaso + ", idSession=" + idSession + ", ordenCompra="
				+ ordenCompra + ", monto=" + monto + "]";
	}

}
