package cl.bbr.promo.lib.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Resultado del calculo de promociones. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class doRecalculoResultado implements Serializable {

	private long descuento_pedido;
	private List promociones = null;

	/**
	 * toString
	 */
	public String toString() {

		String result = "";
		
		result += " descuento_pedido: " + this.descuento_pedido;
		
		return result;
		
	}

	/**
	 * @return Devuelve descuento_pedido.
	 */
	public long getDescuento_pedido() {
		return descuento_pedido;
	}
	/**
	 * @param descuento_pedido El descuento_pedido a establecer.
	 */
	public void setDescuento_pedido(long descuento_pedido) {
		this.descuento_pedido = descuento_pedido;
	}
	/**
	 * @return Devuelve promociones.
	 */
	public List getPromociones() {
		return promociones;
	}
	/**
	 * @param promociones El promociones a establecer.
	 */
	public void setPromociones(List promociones) {
		this.promociones = promociones;
	}
}