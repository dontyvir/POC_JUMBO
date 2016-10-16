package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;

/**
 * Clase que contiene los criterios de busqueda del Monitor de Campanias.
 *  
 * @author BBR
 *
 */
public class CampanasCriteriaDTO implements Serializable {
	
	/**
	 * Pagina del listado. 
	 */
	private int pag;
	
	/**
	 * Indicador si esta activo o no. 
	 */
	private char activo;
	
	/**
	 * Numero de registors por pagina. 
	 */
	private int regsperpage;
	
	/**
	 * Numero de pagina activa 
	 */
	private boolean pag_activa;
	
	/**
	 * Nombre de campana 
	 */
	private String nombre;
	
	/**
	 * Numero de campana 
	 */
	private String numero;
	
	/**
	 * 
	 */
	public CampanasCriteriaDTO() {
		
	}
	
	/**
	 * @return Retorna activo.
	 */
	public char getActivo() {
		return activo;
	}
	/**
	 * @param activo , activo a modificar.
	 */
	public void setActivo(char activo) {
		this.activo = activo;
	}
	/**
	 * @return Retorna nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre , nombre a modificar.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return Retorna numero.
	 */
	public String getNumero() {
		return numero;
	}
	/**
	 * @param numero , numero a modificar.
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}
	/**
	 * @return Retorna pag.
	 */
	public int getPag() {
		return pag;
	}
	/**
	 * @param pag , pag a modificar.
	 */
	public void setPag(int pag) {
		this.pag = pag;
	}
	/**
	 * @return Retorna pag_activa.
	 */
	public boolean isPag_activa() {
		return pag_activa;
	}
	/**
	 * @param pag_activa , pag_activa a modificar.
	 */
	public void setPag_activa(boolean pag_activa) {
		this.pag_activa = pag_activa;
	}
	/**
	 * @return Retorna regsperpage.
	 */
	public int getRegsperpage() {
		return regsperpage;
	}
	/**
	 * @param regsperpage , regsperpage a modificar.
	 */
	public void setRegsperpage(int regsperpage) {
		this.regsperpage = regsperpage;
	}
}
