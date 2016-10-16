package cl.bbr.promo.lib.dto;

import java.io.Serializable;
import java.util.Date;

public class ClientePRDTO implements Serializable {
	
	
	private String rut;
	private String dv;
	private String email;
	private String nombre;
	private String apellido;
	private String direccion;
	private String comunaDespacho;
	private String aceptaInformacion;
	private Date fechaNacimiento;
	
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public String getDv() {
		return dv;
	}
	public void setDv(String dv) {
		this.dv = dv;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getComunaDespacho() {
		return comunaDespacho;
	}
	public void setComunaDespacho(String comunaDespacho) {
		this.comunaDespacho = comunaDespacho;
	}
	public String getAceptaInformacion() {
		return aceptaInformacion;
	}
	public void setAceptaInformacion(String aceptaInformacion) {
		this.aceptaInformacion = aceptaInformacion;
	}
	
	
}
