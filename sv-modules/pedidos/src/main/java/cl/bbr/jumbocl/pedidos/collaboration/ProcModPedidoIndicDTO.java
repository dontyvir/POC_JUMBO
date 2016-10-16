package cl.bbr.jumbocl.pedidos.collaboration;

import java.io.Serializable;

public class ProcModPedidoIndicDTO implements Serializable{
	private long id_pedido;
	private String indicacion;
	private String usr_login;
	private String mensaje;
	private String observacion;
	
	/**
	 * @return Returns the mensaje.
	 */
	public String getMensaje() {
		return mensaje;
	}
	/**
	 * @param mensaje The mensaje to set.
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	/**
	 * @return Returns the usr_login.
	 */
	public String getUsr_login() {
		return usr_login;
	}
	/**
	 * @param usr_login The usr_login to set.
	 */
	public void setUsr_login(String usr_login) {
		this.usr_login = usr_login;
	}
	/**
	 * @return Returns the indicacion.
	 */
	public String getIndicacion() {
		return indicacion;
	}
	/**
	 * @param indicacion The indicacion to set.
	 */
	public void setIndicacion(String indicacion) {
		this.indicacion = indicacion;
	}
	/**
	 * @return Returns the id_pedido.
	 */
	public long getId_pedido() {
		return id_pedido;
	}
	/**
	 * @param id_pedido The id_pedido to set.
	 */
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	

}
