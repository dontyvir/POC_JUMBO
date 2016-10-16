package cl.bbr.jumbocl.usuarios.dto;

import java.util.List;
import java.io.Serializable;

public class PerfilDTO implements Serializable{
	  private long idPerfil;
	  private String nombre;
	  private String descripcion;
	  private List lst_cmd;
	  
	/**
	 * 
	 */
	public PerfilDTO() {
		super();
		
	}

	/**
	 * @param idPerfil
	 * @param nombre
	 * @param descripcion
	 */
	public PerfilDTO(long idPerfil, String nombre, String descripcion) {
		super();
		this.idPerfil = idPerfil;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}

	/**
	 * @param idPerfil
	 * @param nombre
	 * @param descripcion
	 * @param lst_cmd
	 */
	public PerfilDTO(long idPerfil, String nombre, String descripcion, List lst_cmd) {
		super();
		this.idPerfil = idPerfil;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.lst_cmd = lst_cmd;
	}

	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return Returns the idPerfil.
	 */
	public long getIdPerfil() {
		return idPerfil;
	}
	/**
	 * @param idPerfil The idPerfil to set.
	 */
	public void setIdPerfil(long idPerfil) {
		this.idPerfil = idPerfil;
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
	/**
	 * @return Returns the lst_cmd.
	 */
	public List getLst_cmd() {
		return lst_cmd;
	}
	/**
	 * @param lst_cmd The lst_cmd to set.
	 */
	public void setLst_cmd(List lst_cmd) {
		this.lst_cmd = lst_cmd;
	}

}
