package cl.bbr.promo.lib.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Criterio para búsquedas. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class doRecalculoCriterio implements Serializable {

	private long id_pedido;
	private int id_local;
	private String f_pago;
	private int cuotas;
	private List productos = null;
	private List grupos_tcp = null;
	private Long rutColaborador = null;

	/**
	 * toString
	 */
	public String toString() {

		String result = "";
		
		result += " id_pedido: " + this.id_pedido;
		result += " id_local: " + this.id_local;
		result += " f_pago: " + this.f_pago;
		result += " cuotas: " + this.cuotas;
		
		return result;
		
	}
	
	/**
	 * @return Devuelve cuotas.
	 */
	public int getCuotas() {
		return cuotas;
	}
	/**
	 * @param cuotas El cuotas a establecer.
	 */
	public void setCuotas(int cuotas) {
		this.cuotas = cuotas;
	}
	/**
	 * @return Devuelve id_local.
	 */
	public int getId_local() {
		return id_local;
	}
	/**
	 * @param id_local El id_local a establecer.
	 */
	public void setId_local(int id_local) {
		this.id_local = id_local;
	}
	/**
	 * @return Devuelve id_pedido.
	 */
	public long getId_pedido() {
		return id_pedido;
	}
	/**
	 * @param id_pedido El id_pedido a establecer.
	 */
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	/**
	 * @return Devuelve f_pago.
	 */
	public String getF_pago() {
		return f_pago;
	}
	/**
	 * @param f_pago El f_pago a establecer.
	 */
	public void setF_pago(String f_pago) {
		this.f_pago = f_pago;
	}
	/**
	 * @return Devuelve productos.
	 */
	public List getProductos() {
		return productos;
	}
	/**
	 * @param productos El productos a establecer.
	 */
	public void setProductos(List productos) {
		this.productos = productos;
	}
	/**
	 * @return Devuelve grupos_tcp.
	 */
	public List getGrupos_tcp() {
		return grupos_tcp;
	}
	/**
	 * @param grupos_tcp El grupos_tcp a establecer.
	 */
	public void setGrupos_tcp(List grupos_tcp) {
		this.grupos_tcp = grupos_tcp;
	}

    /**
     * @return Returns the rutColaborador.
     */
    public Long getRutColaborador() {
        return rutColaborador;
    }
    /**
     * @param rutColaborador The rutColaborador to set.
     */
    public void setRutColaborador(Long rutColaborador) {
        this.rutColaborador = rutColaborador;
    }
}