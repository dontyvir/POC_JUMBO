package cl.bbr.jumbocl.pedidos.collaboration;

import java.io.Serializable;

public class ProcGeneraDespMasivoDTO implements Serializable {
	
	private long	id_zona;
	private long 	id_semana;
	private String	fecha_ini;
	private String	fecha_fin;
	private long	id_local;
	
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
	 * @return Returns the fecha_fin.
	 */
	public String getFecha_fin() {
		return fecha_fin;
	}
	/**
	 * @param fecha_fin The fecha_fin to set.
	 */
	public void setFecha_fin(String fecha_fin) {
		this.fecha_fin = fecha_fin;
	}
	/**
	 * @return Returns the fecha_ini.
	 */
	public String getFecha_ini() {
		return fecha_ini;
	}
	/**
	 * @param fecha_ini The fecha_ini to set.
	 */
	public void setFecha_ini(String fecha_ini) {
		this.fecha_ini = fecha_ini;
	}
	/**
	 * @return Returns the id_semana.
	 */
	public long getId_semana() {
		return id_semana;
	}
	/**
	 * @param id_semana The id_semana to set.
	 */
	public void setId_semana(long id_semana) {
		this.id_semana = id_semana;
	}
	/**
	 * @return Returns the id_zona.
	 */
	public long getId_zona() {
		return id_zona;
	}
	/**
	 * @param id_zona The id_zona to set.
	 */
	public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
	}
	
}
