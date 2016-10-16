package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;

/**
 * Clase que contiene los criterios de busqueda del Monitor de Categorias.
 *  
 * @author BBR
 *
 */
public class CategoriasCriteriaDTO implements Serializable {
	
	/**
	 * Pagina del listado. 
	 */
	private int pag;
	
	/**
	 * Indicador si esta activo o no. 
	 */
	private char activo;
	
	/**
	 * Tipo de categoria. 
	 */
	private char tipo;
	
	/**
	 * Numero de registors por pagina. 
	 */
	private int regsperpage;
	
	/**
	 * Numero de pagina activa 
	 */
	private boolean pag_activa;
	
	/**
	 * Nombre de categoria 
	 */
	private String nombre;
	
	/**
	 * Numero de categoria 
	 */
	private String numero;
	
	/**
	 * Id de la categoria padre 
	 */
	private String id_cat_padre;

	
	/**
	 * 
	 */
	public CategoriasCriteriaDTO() {
		
	}
	
	/**
	 * @param pag
	 * @param activo
	 * @param tipo
	 * @param regsperpage
	 * @param pag_activa
	 * @param nombre
	 * @param numero
	 * @param id_cat_padre
	 */
	public CategoriasCriteriaDTO(int pag , char activo , char tipo , int regsperpage , boolean pag_activa , 
			String nombre , String numero , String id_cat_padre) {
		this.pag = pag;
		this.activo = activo;
		this.tipo = tipo;
		this.regsperpage = regsperpage;
		this.pag_activa = pag_activa;
		this.nombre = nombre;
		this.numero = numero;
		this.id_cat_padre = id_cat_padre; 
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
	 * @return Retorna id_cat_padre.
	 */
	public String getId_cat_padre() {
		return id_cat_padre;
	}
	/**
	 * @param id_cat_padre , id_cat_padre a modificar.
	 */
	public void setId_cat_padre(String id_cat_padre) {
		this.id_cat_padre = id_cat_padre;
	}
	/**
	 * @return Retorna tipo.
	 */
	public char getTipo() {
		return tipo;
	}
	/**
	 * @param tipo , tipo a modificar.
	 */
	public void setTipo(char tipo) {
		this.tipo = tipo;
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
