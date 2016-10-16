package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;

public class ComunasDTO implements Serializable{
	private long id_comuna;
	private long id_region;
	private String nombre;
	private String reg_nombre;

    public ComunasDTO() {
        
    }
    
	/**
	 * @param id_comuna
	 * @param id_region
	 * @param nombre
	 */
	public ComunasDTO(long id_comuna, long id_region, String nombre, String reg_nombre) {
		super();
		this.id_comuna = id_comuna;
		this.id_region = id_region;
		this.nombre = nombre;
		this.reg_nombre = reg_nombre;
	}

	/**
	 * @return Returns the id_comuna.
	 */
	public long getId_comuna() {
		return id_comuna;
	}
	/**
	 * @param id_comuna The id_comuna to set.
	 */
	public void setId_comuna(long id_comuna) {
		this.id_comuna = id_comuna;
	}
	/**
	 * @return Returns the id_region.
	 */
	public long getId_region() {
		return id_region;
	}
	/**
	 * @param id_region The id_region to set.
	 */
	public void setId_region(long id_region) {
		this.id_region = id_region;
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

	public String getReg_nombre() {
		return reg_nombre;
	}

	public void setReg_nombre(String reg_nombre) {
		this.reg_nombre = reg_nombre;
	}

}
