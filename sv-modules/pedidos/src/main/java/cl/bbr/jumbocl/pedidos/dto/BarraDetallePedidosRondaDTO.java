package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class BarraDetallePedidosRondaDTO implements Serializable {

	private long 	id_detalle;
	private String 	cod_barra;
	private String	tip_codbar;
	
	public String getCod_barra() {
		return cod_barra;
	}
	public void setCod_barra(String cod_barra) {
		this.cod_barra = cod_barra;
	}
	public long getId_detalle() {
		return id_detalle;
	}
	public void setId_detalle(long id_detalle) {
		this.id_detalle = id_detalle;
	}
	public String getTip_codbar() {
		return tip_codbar;
	}
	public void setTip_codbar(String tip_codbar) {
		this.tip_codbar = tip_codbar;
	}
	
}
