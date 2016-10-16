package cl.bbr.jumbocl.pedidos.collaboration;

import java.io.Serializable;

/**
 * @author BBRI
 * 
 */
public class ProcInsPedidoDetalleFODTO implements Serializable {

	private long id_producto_fo;
	private double cant_solic;
	private String observacion;
	private String con_nota;
	private double precio_unitario;
	private String tipo_sel;
	//agregar datos para generar pedido desde cotizacion
	private long 	id_prod_bo;
	private double	dscto_item;
	private double	precio_lista;
	
	/**
	 * Constructor
	 */
	public ProcInsPedidoDetalleFODTO() {

	}

	public double getDscto_item() {
		return dscto_item;
	}

	public void setDscto_item(double dscto_item) {
		this.dscto_item = dscto_item;
	}

	public long getId_prod_bo() {
		return id_prod_bo;
	}

	public void setId_prod_bo(long id_prod_bo) {
		this.id_prod_bo = id_prod_bo;
	}

	public double getPrecio_lista() {
		return precio_lista;
	}

	public void setPrecio_lista(double precio_lista) {
		this.precio_lista = precio_lista;
	}

	public double getCant_solic() {
		return cant_solic;
	}

	public void setCant_solic(double cant_solic) {
		this.cant_solic = cant_solic;
	}

	public String getCon_nota() {
		return con_nota;
	}

	public void setCon_nota(String con_nota) {
		this.con_nota = con_nota;
	}

	/**
	 * ID_PRODUCTO de la tabla BO_PRODUCTOS
	 * @return long
	 */
	public long getId_producto_fo() {
		return id_producto_fo;
	}
	
	/**
	 * ID_PRODUCTO de la tabla BO_PRODUCTOS
	 * 
	 */
	public void setId_producto_fo(long id_producto) {
		this.id_producto_fo = id_producto;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public double getPrecio_unitario() {
		return precio_unitario;
	}

	public void setPrecio_unitario(double precio_unitario) {
		this.precio_unitario = precio_unitario;
	}

	public String getTipoSel() {
		return tipo_sel;
	}

	public void setTipoSel(String tipo_sel) {
		this.tipo_sel = tipo_sel;
	}
	
}