package cl.bbr.jumbocl.pedidos.collaboration;

import java.io.Serializable;

public class ProcModPedidoPolSustDTO implements Serializable{
	
	private long id_pedido;
	private long id_pol_sust;
	private String desc_pol_sust;
	private String login;
	
	public long getId_pedido() {
		return id_pedido;
	}
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	public long getId_pol_sust() {
		return id_pol_sust;
	}
	public void setId_pol_sust(long id_pol_sust) {
		this.id_pol_sust = id_pol_sust;
	}
	public String getDesc_pol_sust() {
		return desc_pol_sust;
	}
	public void setDesc_pol_sust(String desc_pol_sust) {
		this.desc_pol_sust = desc_pol_sust;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	

}
