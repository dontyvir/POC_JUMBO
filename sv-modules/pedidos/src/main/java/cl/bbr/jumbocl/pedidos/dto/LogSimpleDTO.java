package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class LogSimpleDTO implements Serializable {

	long	id_log;
	String	fecha;
	String	hora;
	String	usuario;
	String	descripcion;
	
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public long getId_log() {
		return id_log;
	}
	public void setId_log(long id_log) {
		this.id_log = id_log;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}
