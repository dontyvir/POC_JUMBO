package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class POSProductoDTO implements Serializable {

	private String 	cbarra;
	private String	cod_ret;
	
	public POSProductoDTO(){
		
	}

	public String getCbarra() {
		return cbarra;
	}

	public void setCbarra(String cbarra) {
		this.cbarra = cbarra;
	}

	public String getCod_ret() {
		return cod_ret;
	}

	public void setCod_ret(String cod_ret) {
		this.cod_ret = cod_ret;
	}
		
	
}
