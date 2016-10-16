package cl.bbr.vte.empresas.dto;

import java.io.Serializable;

/**
 * DTO para datos de la sucursal 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class SucursalCriteriaDTO implements Serializable {
	
	private long id_sucursal;
	private long id_empresa;
	private String rut;
	private String razon_social;
	private String nombre;
	private String estado;
	//atributos de paginacion
	private int pag;
	private int regsperpage;
	private String listempresas;
	
	
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Returns the pag.
	 */
	public int getPag() {
		return pag;
	}
	/**
	 * @param pag The pag to set.
	 */
	public void setPag(int pag) {
		this.pag = pag;
	}
	/**
	 * @return Returns the regsperpage.
	 */
	public int getRegsperpage() {
		return regsperpage;
	}
	/**
	 * @param regsperpage The regsperpage to set.
	 */
	public void setRegsperpage(int regsperpage) {
		this.regsperpage = regsperpage;
	}
	/**
	 * @return Returns the razon_social.
	 */
	public String getRazon_social() {
		return razon_social;
	}
	/**
	 * @param razon_social The razon_social to set.
	 */
	public void setRazon_social(String razon_social) {
		this.razon_social = razon_social;
	}

	/**
	 * @return Returns the rut.
	 */
	public String getRut() {
		return rut;
	}
	/**
	 * @param rut The rut to set.
	 */
	public void setRut(String rut) {
		this.rut = rut;
	}
	/**
	 * @return Returns the id_sucursal.
	 */
	public long getId_sucursal() {
		return id_sucursal;
	}
	/**
	 * @param id_sucursal The id_sucursal to set.
	 */
	public void setId_sucursal(long id_sucursal) {
		this.id_sucursal = id_sucursal;
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
	 * @return Returns the listempresas.
	 */
	public String getListempresas() {
		return listempresas;
	}
	/**
	 * @param listempresas The listempresas to set.
	 */
	public void setListempresas(String listempresas) {
		this.listempresas = listempresas;
	}
	/**
	 * @return Returns the id_empresa.
	 */
	public long getId_empresa() {
		return id_empresa;
	}
	/**
	 * @param id_empresa The id_empresa to set.
	 */
	public void setId_empresa(long id_empresa) {
		this.id_empresa = id_empresa;
	}
	
	
}
