package cl.bbr.jumbocl.usuarios.dto;

import java.io.Serializable;

public class ProcModUserDTO implements Serializable{
	
	private long id_usuario;
	private long id_local;
	private String accion;
	
	/**
	 * @return Returns the accion.
	 */
	public String getAccion() {
		return accion;
	}
	/**
	 * @param accion The accion to set.
	 */
	public void setAccion(String accion) {
		this.accion = accion;
	}
	/**
	 * @return Returns the id_local.
	 */
	public long getId_local() {
		return id_local;
	}
	/**
	 * @param id_local The id_local to set.
	 */
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}
	/**
	 * @return Returns the id_usuario.
	 */
	public long getId_usuario() {
		return id_usuario;
	}
	/**
	 * @param id_usuario The id_usuario to set.
	 */
	public void setId_usuario(long id_usuario) {
		this.id_usuario = id_usuario;
	}
	
	

}
