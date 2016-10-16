package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class BinCriteriaDTO implements Serializable {

	private long	id_ronda;
	private long	id_pedido;

	public BinCriteriaDTO(){
		
	}
	
	public long getId_pedido() {
		return id_pedido;
	}
	
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	
	public long getId_ronda() {
		return id_ronda;
	}
	
	public void setId_ronda(long id_ronda) {
		this.id_ronda = id_ronda;
	}

}
