package cl.bbr.promo.lib.dto;

import java.io.Serializable;

public class ClienteKccDTO implements Serializable {
	
	
	private String rut;
	private String dv;
	private String email;
	private String sexo;
	private String talla;
	private String nombreCompleto;
	private String direccion;
	private String comunaDespacho;
	private String boleta;
	private int annosBebe;
	private int mesesBebe;
	private String aceptaInformacion;
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
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getTalla() {
		return talla;
	}
	public void setTalla(String talla) {
		this.talla = talla;
	}
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
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
	public String getBoleta() {
		return boleta;
	}
	public void setBoleta(String boleta) {
		this.boleta = boleta;
	}
	public int getAnnosBebe() {
		return annosBebe;
	}
	public void setAnnosBebe(int annosBebe) {
		this.annosBebe = annosBebe;
	}
	public int getMesesBebe() {
		return mesesBebe;
	}
	public void setMesesBebe(int mesesBebe) {
		this.mesesBebe = mesesBebe;
	}
	public String getAceptaInformacion() {
		return aceptaInformacion;
	}
	public void setAceptaInformacion(String aceptaInformacion) {
		this.aceptaInformacion = aceptaInformacion;
	}
	
}
