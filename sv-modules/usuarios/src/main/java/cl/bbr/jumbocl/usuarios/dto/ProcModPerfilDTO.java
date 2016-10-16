package cl.bbr.jumbocl.usuarios.dto;

import java.io.Serializable;

public class ProcModPerfilDTO implements Serializable {

	private long	id_perfil;
	private String  nombre;
	private String	descripcion;
	private long[] 	id_comandos;
	
	public ProcModPerfilDTO(){
		
	}
	
	public long[] getId_comandos() {
		return id_comandos;
	}
	
	public void setId_comandos(long[] id_comandos) {
		this.id_comandos = id_comandos;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public long getId_perfil() {
		return id_perfil;
	}

	public void setId_perfil(long id_perfil) {
		this.id_perfil = id_perfil;
	}


	
	
}
