package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class JornadaCriteria implements Serializable{

	private long	id_jornada;
	private String	f_jornada;
	private String[] est_pedido_no_mostrar;
	
	public JornadaCriteria(){
		
	}
	
	public String getF_jornada() {
		return f_jornada;
	}
	public void setF_jornada(String f_jornada) {
		this.f_jornada = f_jornada;
	}
	public long getId_jornada() {
		return id_jornada;
	}
	public void setId_jornada(long id_jornada) {
		this.id_jornada = id_jornada;
	}

	public String[] getEst_pedido_no_mostrar() {
		return est_pedido_no_mostrar;
	}

	public void setEst_pedido_no_mostrar(String[] est_pedido_no_mostrar) {
		this.est_pedido_no_mostrar = est_pedido_no_mostrar;
	}
	
	
	
}
