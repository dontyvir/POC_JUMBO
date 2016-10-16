package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class DetallePedidoDTO implements Serializable  {
	
	private long id_detalle;
	private long id_pedido;
	private long id_sector;
	private long id_producto;
	private String cod_prod1;
	private String uni_med;
	private double precio;
	private String descripcion;
	private double cant_solic;
	private String observacion;
	private String preparable;
	private String pesable;
	private String con_nota;
	private double cant_pick;
	private double cant_faltan;
	private double cant_spick;
	private double dscto_item;
	private double desc_pesos_item;
	private double precio_lista;
	private long codPromo;
	
	/**
	 * @return el codPromo
	 */
	public long getCodPromo() {
		return codPromo;
	}
	/**
	 * @param codPromo el codPromo a establecer
	 */
	public void setCodPromo(long codPromo) {
		this.codPromo = codPromo;
	}
	
	public double getCant_faltan() {
		return cant_faltan;
	}
	public void setCant_faltan(double cant_faltan) {
		this.cant_faltan = cant_faltan;
	}
	public double getCant_pick() {
		return cant_pick;
	}
	public void setCant_pick(double cant_pick) {
		this.cant_pick = cant_pick;
	}
	public double getCant_spick() {
		return cant_spick;
	}
	public void setCant_spick(double cant_spick) {
		this.cant_spick = cant_spick;
	}
	public double getCant_solic() {
		return cant_solic;
	}
	public void setCant_solic(double cant_solic) {
		this.cant_solic = cant_solic;
	}
	public String getCod_prod1() {
		return cod_prod1;
	}
	public void setCod_prod1(String cod_prod1) {
		this.cod_prod1 = cod_prod1;
	}
	public String getCon_nota() {
		return con_nota;
	}
	public void setCon_nota(String con_nota) {
		this.con_nota = con_nota;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public long getId_detalle() {
		return id_detalle;
	}
	public void setId_detalle(long id_detalle) {
		this.id_detalle = id_detalle;
	}
	public long getId_pedido() {
		return id_pedido;
	}
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	public long getId_producto() {
		return id_producto;
	}
	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}
	public long getId_sector() {
		return id_sector;
	}
	public void setId_sector(long id_sector) {
		this.id_sector = id_sector;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getPesable() {
		return pesable;
	}
	public void setPesable(String pesable) {
		this.pesable = pesable;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public String getPreparable() {
		return preparable;
	}
	public void setPreparable(String preparable) {
		this.preparable = preparable;
	}
	public String getUni_med() {
		return uni_med;
	}
	public void setUni_med(String uni_med) {
		this.uni_med = uni_med;
	}
	/**
	 * @return Returns the dscto_item.
	 */
	public double getDscto_item() {
		return dscto_item;
	}
	/**
	 * @param dscto_item The dscto_item to set.
	 */
	public void setDscto_item(double dscto_item) {
		this.dscto_item = dscto_item;
	}
	/**
	 * @return Returns the desc_pesos_item.
	 */
	public double getDesc_pesos_item() {
		return desc_pesos_item;
	}
	/**
	 * @param desc_pesos_item The desc_pesos_item to set.
	 */
	public void setDesc_pesos_item(double desc_pesos_item) {
		this.desc_pesos_item = desc_pesos_item;
	}
	/**
	 * @return Returns the precio_lista.
	 */
	public double getPrecio_lista() {
		return precio_lista;
	}
	/**
	 * @param precio_lista The precio_lista to set.
	 */
	public void setPrecio_lista(double precio_lista) {
		this.precio_lista = precio_lista;
	}
	
	
	
}
