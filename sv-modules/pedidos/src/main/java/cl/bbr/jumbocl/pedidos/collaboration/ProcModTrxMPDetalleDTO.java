package cl.bbr.jumbocl.pedidos.collaboration;

public class ProcModTrxMPDetalleDTO {
	
	private long 	id_pedido;
	private long 	id_trx_mp;
	private long[] 	id_trx_mp_det;
	private String[] cod_barra;
	
	public String[] getCod_barra() {
		return cod_barra;
	}
	public void setCod_barra(String[] cod_barra) {
		this.cod_barra = cod_barra;
	}
	public long getId_pedido() {
		return id_pedido;
	}
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	public long getId_trx_mp() {
		return id_trx_mp;
	}
	public void setId_trx_mp(long id_trx_mp) {
		this.id_trx_mp = id_trx_mp;
	}
	public long[] getId_trx_mp_det() {
		return id_trx_mp_det;
	}
	public void setId_trx_mp_det(long[] id_trx_mp_det) {
		this.id_trx_mp_det = id_trx_mp_det;
	}
	
	
}
