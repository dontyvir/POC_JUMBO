package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la Base de Datos los datos de un documento de pago
 * @author bbr
 *
 */
public class FacturaEntity {
	private Long id_factura;
	private Long id_pedido;
	private Long num_doc;
	private String tipo_doc;
	private String razon;
	private Integer rut;
	private String dv;
	private String direccion;
	private String fono;
	private String giro;
	private String ciudad;
	private String comuna;
	
	/**
	 * @return ciudad
	 */
	public String getCiudad() {
		return ciudad;
	}
	/**
	 * @return comuna
	 */
	public String getComuna() {
		return comuna;
	}
	/**
	 * @param ciudad
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	/**
	 * @param comuna
	 */
	public void setComuna(String comuna) {
		this.comuna = comuna;
	}
	/**
	 * @return direccion
	 */
	public String getDireccion() {
		return direccion;
	}
	/**
	 * @param direccion
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	/**
	 * @return dv
	 */
	public String getDv() {
		return dv;
	}
	/**
	 * @param dv
	 */
	public void setDv(String dv) {
		this.dv = dv;
	}
	/**
	 * @return fono
	 */
	public String getFono() {
		return fono;
	}
	/**
	 * @param fono
	 */
	public void setFono(String fono) {
		this.fono = fono;
	}
	/**
	 * @return giro
	 */
	public String getGiro() {
		return giro;
	}
	/**
	 * @param giro
	 */
	public void setGiro(String giro) {
		this.giro = giro;
	}
	/**
	 * @return id_factura
	 */
	public Long getId_factura() {
		return id_factura;
	}
	/**
	 * @param id_factura
	 */
	public void setId_factura(Long id_factura) {
		this.id_factura = id_factura;
	}
	/**
	 * @return id_pedido
	 */
	public Long getId_pedido() {
		return id_pedido;
	}
	/**
	 * @param id_pedido
	 */
	public void setId_pedido(Long id_pedido) {
		this.id_pedido = id_pedido;
	}
	/**
	 * @return razon
	 */
	public String getRazon() {
		return razon;
	}
	/**
	 * @param razon
	 */
	public void setRazon(String razon) {
		this.razon = razon;
	}
	/**
	 * @return rut
	 */
	public Integer getRut() {
		return rut;
	}
	/**
	 * @param rut
	 */
	public void setRut(Integer rut) {
		this.rut = rut;
	}
	/**
	 * @return num_doc
	 */
	public Long getNum_doc() {
		return num_doc;
	}
	/**
	 * @return tipo_doc
	 */
	public String getTipo_doc() {
		return tipo_doc;
	}
	/**
	 * @param num_doc
	 */
	public void setNum_doc(Long num_doc) {
		this.num_doc = num_doc;
	}
	/**
	 * @param tipo_doc
	 */
	public void setTipo_doc(String tipo_doc) {
		this.tipo_doc = tipo_doc;
	}
	
	
}
