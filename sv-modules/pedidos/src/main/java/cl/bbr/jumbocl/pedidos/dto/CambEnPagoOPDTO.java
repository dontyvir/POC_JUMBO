package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class CambEnPagoOPDTO implements Serializable {
	
	private long id_pedido;
	private long id_usuario;
	private String usr_login;
	
	
	public long getId_pedido() {
		return id_pedido;
	}
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	public long getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(long id_usuario) {
		this.id_usuario = id_usuario;
	}
	
	
	public String getUsr_login() {
		return usr_login;
	}
	public void setUsr_login(String usr_login) {
		this.usr_login = usr_login;
	}
	

}
