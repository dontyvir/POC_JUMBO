package cl.bbr.jumbocl.pedidos.collaboration;

import java.io.Serializable;

public class ProcModPedidoDirDTO implements Serializable{
	private long id_pedido;
	private long id_dir;
	private long id_local_pedido;
	private String usr_login;
	
	

	public long getId_local_pedido() {
		return id_local_pedido;
	}
	public void setId_local_pedido(long id_local_pedido) {
		this.id_local_pedido = id_local_pedido;
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
	 * @return Returns the id_dir.
	 */
	public long getId_dir() {
		return id_dir;
	}
	/**
	 * @param id_dir The id_dir to set.
	 */
	public void setId_dir(long id_dir) {
		this.id_dir = id_dir;
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
	
	

}
