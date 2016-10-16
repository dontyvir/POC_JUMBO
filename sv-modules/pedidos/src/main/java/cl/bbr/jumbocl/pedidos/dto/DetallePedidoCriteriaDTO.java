package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class DetallePedidoCriteriaDTO implements Serializable {

	private long	id_pedido;
	private long	id_sector;
	
	public DetallePedidoCriteriaDTO(){
		
	}

	public long getId_pedido() {
		return id_pedido;
	}

	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}

	public long getId_sector() {
		return id_sector;
	}

	public void setId_sector(long id_sector) {
		this.id_sector = id_sector;
	}
	
	
	
}
