package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class PickingCriteriaDTO implements Serializable {

	private long id_dpicking;
	private long id_detalle;
	private long id_bp;
	private long id_pedido;
	
	public PickingCriteriaDTO(){
		
	}
	
	public long getId_pedido() {
		return id_pedido;
	}

	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}

	public long getId_bp() {
		return id_bp;
	}

	public void setId_bp(long id_bp) {
		this.id_bp = id_bp;
	}

	public long getId_detalle() {
		return id_detalle;
	}

	public void setId_detalle(long id_detalle) {
		this.id_detalle = id_detalle;
	}

	public long getId_dpicking() {
		return id_dpicking;
	}

	public void setId_dpicking(long id_dpicking) {
		this.id_dpicking = id_dpicking;
	}
	
	
}
