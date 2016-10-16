package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class MotivoDTO implements Serializable{

	private long	id_mot;
	private String	nombre;
	private String	descrip;
	private String	activo;
	
	public MotivoDTO(){
		
	}

	/**
	 * @param id_mot
	 * @param nombre
	 * @param descrip
	 * @param activo
	 */
	public MotivoDTO(long id_mot, String nombre, String descrip, String activo) {
		super();
		this.id_mot = id_mot;
		this.nombre = nombre;
		this.descrip = descrip;
		this.activo = activo;
	}

	/**
	 * @return Returns the activo.
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * @param activo The activo to set.
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	/**
	 * @return Returns the descrip.
	 */
	public String getDescrip() {
		return descrip;
	}

	/**
	 * @param descrip The descrip to set.
	 */
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	/**
	 * @return Returns the id_mot.
	 */
	public long getId_mot() {
		return id_mot;
	}

	/**
	 * @param id_mot The id_mot to set.
	 */
	public void setId_mot(long id_mot) {
		this.id_mot = id_mot;
	}

	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
