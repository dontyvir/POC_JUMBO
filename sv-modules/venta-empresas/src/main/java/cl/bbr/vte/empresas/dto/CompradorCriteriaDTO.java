package cl.bbr.vte.empresas.dto;

import java.io.Serializable;

/**
 * DTO para datos del comprador 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class CompradorCriteriaDTO implements Serializable {
	
	private long id_sucursal;
	private long id_empresa;
	
	private String rut;
	private String apellido;
	
	
	//atributos de paginacion
	private int pag;
	private int regsperpage;
	
	public long getId_empresa() {
		return id_empresa;
	}
	public void setId_empresa(long id_empresa) {
		this.id_empresa = id_empresa;
	}
	public long getId_sucursal() {
		return id_sucursal;
	}
	public void setId_sucursal(long id_sucursal) {
		this.id_sucursal = id_sucursal;
	}
	public int getPag() {
		return pag;
	}
	public void setPag(int pag) {
		this.pag = pag;
	}
	public int getRegsperpage() {
		return regsperpage;
	}
	public void setRegsperpage(int regsperpage) {
		this.regsperpage = regsperpage;
	}
	/**
	 * @return Returns the apellido.
	 */
	public String getApellido() {
		return apellido;
	}
	/**
	 * @param apellido The apellido to set.
	 */
	public void setApellido(String apellido) {
		this.apellido = apellido;
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
	
}
