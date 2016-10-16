package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
import java.util.List;

public class ActDetallePickingDTO implements Serializable{
	private List lst_det_pedido;
	private List lst_det_picking;
	private List lst_bin_op;
	private List lst_reg_picking;
	private long id_ronda;
	
	public List getLst_bin_op() {
		return lst_bin_op;
	}
	public void setLst_bin_op(List lst_bin_op) {
		this.lst_bin_op = lst_bin_op;
	}
	public List getLst_det_pedido() {
		return lst_det_pedido;
	}
	public void setLst_det_pedido(List lst_det_pedido) {
		this.lst_det_pedido = lst_det_pedido;
	}
	public List getLst_det_picking() {
		return lst_det_picking;
	}
	public void setLst_det_picking(List lst_det_picking) {
		this.lst_det_picking = lst_det_picking;
	}
	public long getId_ronda() {
		return id_ronda;
	}
	public void setId_ronda(long id_ronda) {
		this.id_ronda = id_ronda;
	}
	public List getLst_reg_picking() {
		return lst_reg_picking;
	}
	public void setLst_reg_picking(List lst_reg_picking) {
		this.lst_reg_picking = lst_reg_picking;
	}
	
	
}
