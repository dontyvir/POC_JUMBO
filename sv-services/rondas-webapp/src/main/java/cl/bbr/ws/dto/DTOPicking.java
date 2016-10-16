package cl.bbr.ws.dto;
import org.jdom.Document;

/**
 * DTO para los datos del picking en XML
 */
public class DTOPicking implements java.io.Serializable{

	private Document TablaUsuariosXML;
	private Document TablaDetallePedidoXML;
	private Document TablaCBarraXML;
	private Document tablaDetalleRondaXML;
	private Document tablaDetalle_PickingXML;
	private Document tablaBin_OpXML;
	private Document tablaRegistro_PickingXML;

	/** Constructor para DTOPicking */	 
	public DTOPicking() {
		super();
	}

	/**
	 * Returns the tablaCBarraXML.
	 * @return Document
	 */
	public Document getTablaCBarraXML() {
		return TablaCBarraXML;
	}

	/**
	 * Returns the tablaDetallePedidoXML.
	 * @return Document
	 */
	public Document getTablaDetallePedidoXML() {
		return TablaDetallePedidoXML;
	}

	/**
	 * Returns the tablaUsuariosXML.
	 * @return Document
	 */
	public Document getTablaUsuariosXML() {
		return TablaUsuariosXML;
	}

	/**
	 * Returns the tablaBin_OpXML.
	 * @return Document
	 */
	public Document getTablaBin_OpXML() {
		return tablaBin_OpXML;
	}

	/**
	 * Returns the tablaDetalle_PickingXML.
	 * @return Document
	 */
	public Document getTablaDetalle_PickingXML() {
		return tablaDetalle_PickingXML;
	}

	/**
	 * Returns the tablaDetalleRondaXML.
	 * @return Document
	 */
	public Document getTablaDetalleRondaXML() {
		return tablaDetalleRondaXML;
	}

	/**
	 * Sets the tablaCBarraXML.
	 * @param tablaCBarraXML The tablaCBarraXML to set
	 */
	public void setTablaCBarraXML(Document tablaCBarraXML) {
		TablaCBarraXML = tablaCBarraXML;
	}

	/**
	 * Sets the tablaDetallePedidoXML.
	 * @param tablaDetallePedidoXML The tablaDetallePedidoXML to set
	 */
	public void setTablaDetallePedidoXML(Document tablaDetallePedidoXML) {
		TablaDetallePedidoXML = tablaDetallePedidoXML;
	}

	/**
	 * Sets the tablaUsuariosXML.
	 * @param tablaUsuariosXML The tablaUsuariosXML to set
	 */
	public void setTablaUsuariosXML(Document tablaUsuariosXML) {
		TablaUsuariosXML = tablaUsuariosXML;
	}

	/**
	 * Sets the tablaBin_OpXML.
	 * @param tablaBin_OpXML The tablaBin_OpXML to set
	 */
	public void setTablaBin_OpXML(Document tablaBin_OpXML) {
		this.tablaBin_OpXML = tablaBin_OpXML;
	}

	/**
	 * Sets the tablaDetalle_PickingXML.
	 * @param tablaDetalle_PickingXML The tablaDetalle_PickingXML to set
	 */
	public void setTablaDetalle_PickingXML(Document tablaDetalle_PickingXML) {
		this.tablaDetalle_PickingXML = tablaDetalle_PickingXML;
	}

	/**
	 * Sets the tablaDetalleRondaXML.
	 * @param tablaDetalleRondaXML The tablaDetalleRondaXML to set
	 */
	public void setTablaDetalleRondaXML(Document tablaDetalleRondaXML) {
		this.tablaDetalleRondaXML = tablaDetalleRondaXML;
	}

	public Document getTablaRegistro_PickingXML() {
		return tablaRegistro_PickingXML;
	}

	public void setTablaRegistro_PickingXML(Document tablaRegistro_PickingXML) {
		this.tablaRegistro_PickingXML = tablaRegistro_PickingXML;
	}



}
