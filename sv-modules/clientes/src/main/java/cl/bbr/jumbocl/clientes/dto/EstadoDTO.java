package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;

public class EstadoDTO implements Serializable{

	private char	id_estado;
	private String	nombre;
	
	public EstadoDTO(){
		
	}
	public EstadoDTO(char id_estado, String nombre) {
		this.id_estado = id_estado;
		this.nombre = nombre;
	}
	public char getId_estado() {
		return id_estado;
	}
	public void setId_estado(char id_estado) {
		this.id_estado = id_estado;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
