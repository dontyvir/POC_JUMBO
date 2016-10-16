package cl.bbr.vte.cotizaciones.dto;

/**
 * Clase que contiene información de la cotización
 * 
 * @author BBR
 *
 */
public class ModProdCotizacionesDTO {
	private long detcot_id;
	private long detcot_cot_id;
	private double detcot_precio;
	private double  detcot_cantidad;
	private double detcot_dscto_item;
	/**
	 * @return Returns the detcot_cantidad.
	 */
	public double getDetcot_cantidad() {
		return detcot_cantidad;
	}
	/**
	 * @return Returns the detcot_cot_id.
	 */
	public long getDetcot_cot_id() {
		return detcot_cot_id;
	}
	/**
	 * @return Returns the detcot_dscto_item.
	 */
	public double getDetcot_dscto_item() {
		return detcot_dscto_item;
	}
	/**
	 * @return Returns the detcot_id.
	 */
	public long getDetcot_id() {
		return detcot_id;
	}
	/**
	 * @return Returns the detcot_precio.
	 */
	public double getDetcot_precio() {
		return detcot_precio;
	}
	/**
	 * @param detcot_cantidad The detcot_cantidad to set.
	 */
	public void setDetcot_cantidad(double detcot_cantidad) {
		this.detcot_cantidad = detcot_cantidad;
	}
	/**
	 * @param detcot_cot_id The detcot_cot_id to set.
	 */
	public void setDetcot_cot_id(long detcot_cot_id) {
		this.detcot_cot_id = detcot_cot_id;
	}
	/**
	 * @param detcot_dscto_item The detcot_dscto_item to set.
	 */
	public void setDetcot_dscto_item(double detcot_dscto_item) {
		this.detcot_dscto_item = detcot_dscto_item;
	}
	/**
	 * @param detcot_id The detcot_id to set.
	 */
	public void setDetcot_id(long detcot_id) {
		this.detcot_id = detcot_id;
	}
	/**
	 * @param detcot_precio The detcot_precio to set.
	 */
	public void setDetcot_precio(double detcot_precio) {
		this.detcot_precio = detcot_precio;
	}
	
	
}
