package cl.bbr.promo.lib.dto;

import java.io.Serializable;

/**
 * DTO para datos de las promociones por producto. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class ProductoPromosDTO implements Serializable {

	private long id_prodpromos;
	private long id_producto;
	private long id_local;
	private long id_promocion1;
	private long id_promocion2;
	private long id_promocion3;
	private double cant_solicitada;
	private double precio_lista;
	private String pesable;
	private String cod_barra;
	private String seccion_sap;
	private String id_catprod;
	
	private int rubro;
	

	/**
	 * @return el id_catprod
	 */
	public String getId_catprod() {
		return id_catprod;
	}
	/**
	 * @param id_catprod el id_catprod a establecer
	 */
	public void setId_catprod(String id_catprod) {
		this.id_catprod = id_catprod;
	}
	/**
	 * @return Devuelve id_local.
	 */
	public long getId_local() {
		return id_local;
	}
	/**
	 * @param id_local El id_local a establecer.
	 */
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}
	/**
	 * @return Devuelve id_prodpromos.
	 */
	public long getId_prodpromos() {
		return id_prodpromos;
	}
	/**
	 * @param id_prodpromos El id_prodpromos a establecer.
	 */
	public void setId_prodpromos(long id_prodpromos) {
		this.id_prodpromos = id_prodpromos;
	}
	/**
	 * @return Devuelve id_producto.
	 */
	public long getId_producto() {
		return id_producto;
	}
	/**
	 * @param id_producto El id_producto a establecer.
	 */
	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}
	/**
	 * @return Devuelve id_promocion1.
	 */
	public long getId_promocion1() {
		return id_promocion1;
	}
	/**
	 * @param id_promocion1 El id_promocion1 a establecer.
	 */
	public void setId_promocion1(long id_promocion1) {
		this.id_promocion1 = id_promocion1;
	}
	/**
	 * @return Devuelve id_promocion2.
	 */
	public long getId_promocion2() {
		return id_promocion2;
	}
	/**
	 * @param id_promocion2 El id_promocion2 a establecer.
	 */
	public void setId_promocion2(long id_promocion2) {
		this.id_promocion2 = id_promocion2;
	}
	/**
	 * @return Devuelve id_promocion3.
	 */
	public long getId_promocion3() {
		return id_promocion3;
	}
	/**
	 * @param id_promocion3 El id_promocion3 a establecer.
	 */
	public void setId_promocion3(long id_promocion3) {
		this.id_promocion3 = id_promocion3;
	}
	
	/**
	 * @return Devuelve cant_solicitada.
	 */
	public double getCant_solicitada() {
		return cant_solicitada;
	}
	/**
	 * @param cant_solicitada El cant_solicitada a establecer.
	 */
	public void setCant_solicitada(double cant_solicitada) {
		this.cant_solicitada = cant_solicitada;
	}
	/**
	 * @return Devuelve cod_barra.
	 */
	public String getCod_barra() {
		return cod_barra;
	}
	/**
	 * @param cod_barra El cod_barra a establecer.
	 */
	public void setCod_barra(String cod_barra) {
		this.cod_barra = cod_barra;
	}
	/**
	 * @return Devuelve pesable.
	 */
	public String getPesable() {
		return pesable;
	}
	/**
	 * @param pesable El pesable a establecer.
	 */
	public void setPesable(String pesable) {
		this.pesable = pesable;
	}
	/**
	 * @return Devuelve precio_lista.
	 */
	public double getPrecio_lista() {
		return precio_lista;
	}
	/**
	 * @param precio_lista El precio_lista a establecer.
	 */
	public void setPrecio_lista(double precio_lista) {
		this.precio_lista = precio_lista;
	}
	/**
	 * @return Devuelve seccion_sap.
	 */
	public String getSeccion_sap() {
		return seccion_sap;
	}
	/**
	 * @param seccion_sap El seccion_sap a establecer.
	 */
	public void setSeccion_sap(String seccion_sap) {
		this.seccion_sap = seccion_sap;
	}
	public int getRubro() {
		return rubro;
	}
	public void setRubro(int rubro) {
		this.rubro = rubro;
	}
}