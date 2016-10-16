package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class ComunaDTO implements Serializable {

	private long	id_comuna;
	private String	nombre;
	private String tipo;
	
	public ComunaDTO(){
		
	}

	/**
	 * @return Returns the tipo.
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public long getId_comuna() {
		return id_comuna;
	}

	public void setId_comuna(long id_comuna) {
		this.id_comuna = id_comuna;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
