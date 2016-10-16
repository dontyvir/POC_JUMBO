package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;

public class ListaPedidoClienteDTO  implements Serializable{

	private static final long serialVersionUID = -5930457633061277791L;
	
	private long id_pedido;
	private String fingreso;
	private String fdespacho;
	private long id_estado;
	private String estado;
	private String secuenciaPago;
	public long getId_pedido() {
		return id_pedido;
	}
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	public String getFingreso() {
		return fingreso;
	}
	public void setFingreso(String fingreso) {
		this.fingreso = fingreso;
	}
	public String getFdespacho() {
		return fdespacho;
	}
	public void setFdespacho(String fdespacho) {
		this.fdespacho = fdespacho;
	}
	public long getId_estado() {
		return id_estado;
	}
	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getSecuenciaPago() {
		return secuenciaPago;
	}
	public void setSecuenciaPago(String secuenciaPago) {
		this.secuenciaPago = secuenciaPago;
	}
	
	

}
