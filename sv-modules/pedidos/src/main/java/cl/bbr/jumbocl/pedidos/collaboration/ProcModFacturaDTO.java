package cl.bbr.jumbocl.pedidos.collaboration;

import java.io.Serializable;

public class ProcModFacturaDTO implements Serializable{

	private long id_pedido;
	private long id_factura;
	private long rut;
	private String dv;
	private String razon;
	private String direccion;
	private String telefono;
	private String giro;
	private String tipo_doc;
	private String comuna;
	private String ciudad;
	
	public ProcModFacturaDTO(){		
	}
	
	public ProcModFacturaDTO(long id_pedido, long id_factura, long rut, String dv, String razon, String direccion, String telefono, String giro) {
		super();
		this.id_pedido = id_pedido;
		this.id_factura = id_factura;
		this.rut = rut;
		this.dv = dv;
		this.razon = razon;
		this.direccion = direccion;
		this.telefono = telefono;
		this.giro = giro;
	}	
	
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getComuna() {
		return comuna;
	}
	public void setComuna(String comuna) {
		this.comuna = comuna;
	}

	public String getDireccion() {
		return direccion;
	}


	public String getDv() {
		return dv;
	}


	public String getGiro() {
		return giro;
	}


	public long getId_factura() {
		return id_factura;
	}


	public long getId_pedido() {
		return id_pedido;
	}


	public String getRazon() {
		return razon;
	}

	public String getTelefono() {
		return telefono;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public void setDv(String dv) {
		this.dv = dv;
	}


	public void setGiro(String giro) {
		this.giro = giro;
	}


	public void setId_factura(long id_factura) {
		this.id_factura = id_factura;
	}


	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}


	public void setRazon(String razon) {
		this.razon = razon;
	}

	public long getRut() {
		return rut;
	}

	public void setRut(long rut) {
		this.rut = rut;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getTipo_doc() {
		return tipo_doc;
	}
	
	public void setTipo_doc(String tipo_doc) {
		this.tipo_doc = tipo_doc;
	}
	
	

}
