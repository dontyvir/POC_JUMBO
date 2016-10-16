package cl.jumbo.interfaces.ventaslocales;

import java.sql.Date;
import java.util.Vector;

/**
 * Contiene información de una compra histórica.
 * @author Informática Paris - Javier Villalobos Arancibia
 * @version 1.0 - 03/07/2006
 *
 */
public class CompraHistorica {

	/**
	 * Cantidad de productos de la compra.
	 */
	private int unidades;
	
	/**
	 * Identificador único para la compra.
	 */
	private int idCompra;
	
	/**
	 * Código del Local donde se realizó la compra.
	 */
	private String codigoLocal = "";
	
	/**
	 * Nombre del Local donde se realizó la compra.
	 */
	private String nombreLocal = "";
	
	/**
	 * Fecha en la que se realizó la compra.
	 */
	private Date fecha = null;
	
	/**
	 * Lista de productos de la compra.
	 */
	private Vector productos = null;
	
	/**
	 * Rut del Cliente que realizó o se asoció la compra.
	 */
	private int rut;
	
	/**
	 * Número de Caja donde se realizó la compra
	 */
	private int caja;
	
	/**
	 * Número del ticket asociado a la compra
	 */
	private int ticket;
	
	/**
	 * Monto total de la Compra
	 */
	private int montoTotal;
	
	
	public CompraHistorica() {
	}
	
	public CompraHistorica(int idCompra) {
		this.idCompra = idCompra;
	}

	/**
	 * @return Returns the codigoLocal.
	 */
	public String getCodigoLocal() {
		return codigoLocal;
	}

	/**
	 * @param codigoLocal The codigoLocal to set.
	 */
	public void setCodigoLocal(String codigoLocal) {
		this.codigoLocal = codigoLocal;
	}

	/**
	 * @return Returns the fecha.
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha The fecha to set.
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return Returns the idCompra.
	 */
	public int getIdCompra() {
		return idCompra;
	}

	/**
	 * @param idCompra The idCompra to set.
	 */
	public void setIdCompra(int idCompra) {
		this.idCompra = idCompra;
	}

	/**
	 * @return Returns the nombreLocal.
	 */
	public String getNombreLocal() {
		return nombreLocal;
	}

	/**
	 * @param nombreLocal The nombreLocal to set.
	 */
	public void setNombreLocal(String nombreLocal) {
		this.nombreLocal = nombreLocal;
	}

	/**
	 * @param productos The productos to set.
	 */
	public void setProductos(Vector productos) {
		this.productos = productos;
	}

	/**
	 * @return Returns the rut.
	 */
	public int getRut() {
		return rut;
	}

	/**
	 * @param rut The rut to set.
	 */
	public void setRut(int rut) {
		this.rut = rut;
	}
	
	/**
	 * @return Returns the unidades.
	 */
	public int getUnidades() {
		return unidades;
	}
	/**
	 * @param unidades The unidades to set.
	 */
	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}
	public int getCaja() {
		return caja;
	}
	public void setCaja(int caja) {
		this.caja = caja;
	}
	public int getTicket() {
		return ticket;
	}
	public void setTicket(int ticket) {
		this.ticket = ticket;
	}
	public int getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(int montoTotal) {
		this.montoTotal = montoTotal;
	}
}
