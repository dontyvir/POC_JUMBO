package cl.bbr.jumbocl.pedidos.dto;

public class PagoGrabilityDTO {
	private long idPedido; //Numero de pedido.
	private String tokenPago;//Hash para validar op post ejecucion del servicio de pago seleccionado.
	private String estado;//Estado de pago A: aceptado, R: rechazado.
	private String fCreacion;//Fecha creacion token de validacion.
	private String fValidacion;	//Fecha en que se valido el pago. (Cuando cambia el estado)

	
	public long getIdPedido() {
		return idPedido;
	}
	public void setIdPedido(long idPedido) {
		this.idPedido = idPedido;
	}
	public String getTokenPago() {
		return tokenPago;
	}
	public void setTokenPago(String tokenPago) {
		this.tokenPago = tokenPago;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getfCreacion() {
		return fCreacion;
	}
	public void setfCreacion(String fCreacion) {
		this.fCreacion = fCreacion;
	}
	public String getfValidacion() {
		return fValidacion;
	}
	public void setfValidacion(String fValidacion) {
		this.fValidacion = fValidacion;
	}
}
