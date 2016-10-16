package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class IdPedidoCantDTO implements Serializable {
	private long 	id_pedido;
	private double	 cant;
	private double	 cant_prod;
	
	public IdPedidoCantDTO(){
		
	}

	
	public double getCant() {
		return cant;
	}

	public void setCant(double cant) {
		this.cant = cant;
	}

	public long getId_pedido() {
		return id_pedido;
	}

	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}


	/**
	 * @return Returns the cant_prod.
	 */
	public double getCant_prod() {
		return cant_prod;
	}


	/**
	 * @param cant_prod The cant_prod to set.
	 */
	public void setCant_prod(double cant_prod) {
		this.cant_prod = cant_prod;
	}
	
	
	
}
