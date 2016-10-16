package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class FacturaDTO implements Serializable {
	private long id_factura;
	private long id_pedido;
	private long num_doc;
	private String tipo_doc;	
	private String razon;
	private int rut;
	private String dv;
	private String direccion;
	private String fono;
	private String giro;
	private String ciudad;
	private String comuna;
	
	public String getCiudad() {
		return ciudad;
	}

	public String getComuna() {
		return comuna;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public void setComuna(String comuna) {
		this.comuna = comuna;
	}

	public FacturaDTO(){
		
	}
	
	public FacturaDTO(long id_factura, long id_pedido, String razon, int rut, String dv, String direccion, String fono, String giro) {
		super();
		this.id_factura = id_factura;
		this.id_pedido = id_pedido;
		this.razon = razon;
		this.rut = rut;
		this.dv = dv;
		this.direccion = direccion;
		this.fono = fono;
		this.giro = giro;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getDv() {
		return dv;
	}
	public void setDv(String dv) {
		this.dv = dv;
	}
	public String getFono() {
		return fono;
	}
	public void setFono(String fono) {
		this.fono = fono;
	}
	public String getGiro() {
		return giro;
	}
	public void setGiro(String giro) {
		this.giro = giro;
	}
	public long getId_factura() {
		return id_factura;
	}
	public void setId_factura(long id_factura) {
		this.id_factura = id_factura;
	}
	public long getId_pedido() {
		return id_pedido;
	}
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	public String getRazon() {
		return razon;
	}
	public void setRazon(String razon) {
		this.razon = razon;
	}
	public int getRut() {
		return rut;
	}
	public void setRut(int rut) {
		this.rut = rut;
	}

	public long getNum_doc() {
		return num_doc;
	}

	public String getTipo_doc() {
		return tipo_doc;
	}

	public void setNum_doc(long num_doc) {
		this.num_doc = num_doc;
	}

	public void setTipo_doc(String tipo_doc) {
		this.tipo_doc = tipo_doc;
	}

	
}
