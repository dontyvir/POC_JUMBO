package cl.jumbo.interfaces.ventaslocales;

/**
 * Contiene información de un producto contenido en una compra determianda.
 * @author Informática Paris - Javier Villalobos Arancibia
 * @version 1.0 - 03/07/2006
 */
public class ProductoCompraHistorica {

	/**
	 * Código de Barras (EAN) del Producto.
	 */
	private String codigoEAN = "";
	
	/**
	 * Cantidad del producto comprado.
	 */
	private double cantidad;
	
	/**
	 * Referencia al objeto CompraHistorica al que pertenece este producto.
	 */
	private CompraHistorica compra = null;
	
	/**
	 * @return Returns the cantidad.
	 */
	public double getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return Returns the codigoEAN.
	 */
	public String getCodigoEAN() {
		return codigoEAN;
	}

	/**
	 * @param codigoEAN The codigoEAN to set.
	 */
	public void setCodigoEAN(String codigoEAN) {
		this.codigoEAN = codigoEAN;
	}

	/**
	 * @return Returns the compra.
	 */
	public CompraHistorica getCompra() {
		return compra;
	}

	/**
	 * @param compra The compra to set.
	 */
	public void setCompra(CompraHistorica compra) {
		this.compra = compra;
	}

	public ProductoCompraHistorica() {
		super();
	}
	
}
