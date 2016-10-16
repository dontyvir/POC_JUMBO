package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;

/**
 * Clase que muestra informacion del criterio de busqueda del producto.
 * 
 * @author BBR
 *
 */
public class ProductosCriteriaDTO implements Serializable  {
	
	/**
	 * Pagina actual 
	 */
	private int pag;
	
	/**
	 * Estado
	 */
	private char estado;
	
	/**
	 * Tipo de producto
	 */
	private char tipo;
	
	/**
	 * Id de categoria
	 */
	private int id_cat;
	
	/**
	 * Codigo del producto
	 */
	private String codprod;
	
	/**
	 * Producto SAP
	 */
	private String prodsap;
	
	/**
	 * Descripcion
	 */
	private String descrip;
	
	/**
	 * Número de registros por pagina
	 */
	private int regsperpage;
	
	/**
	 * Pagina activa
	 */
	private boolean pag_activa;
	
	
	public ProductosCriteriaDTO() {
	}
	
	/**
	 * @param pag
	 * @param codprod
	 * @param prodsap
	 * @param regsperpage
	 * @param pag_activa
	 * @param estado
	 * @param tipo
	 * @param descrip
	 * @param id_cat
	 */
	public ProductosCriteriaDTO(int pag , String codprod , String prodsap , int regsperpage , boolean pag_activa,
			char estado , char tipo , String descrip , int id_cat) {
		this.pag = pag;
		this.codprod = codprod;
		this.prodsap = prodsap;
		this.regsperpage = regsperpage;
		this.pag_activa = pag_activa;
		this.estado = estado;
		this.tipo = tipo;
		this.descrip = descrip;
		this.id_cat = id_cat;
	}
	
	/**
	 * @return Retorna pag.
	 */
	public int getPag() {
		return pag;
	}
	/**
	 * @return Retorna pag_activa.
	 */
	public boolean isPag_activa() {
		return pag_activa;
	}
	/**
	 * @return Retorna prodsap.
	 */
	public String getProdsap() {
		return prodsap;
	}
	/**
	 * @return Retorna regsperpage.
	 */
	public int getRegsperpage() {
		return regsperpage;
	}
	/**
	 * @param pag , pag a modificar.
	 */
	public void setPag(int pag) {
		this.pag = pag;
	}
	/**
	 * @param pag_activa , pag_activa a modificar.
	 */
	public void setPag_activa(boolean pag_activa) {
		this.pag_activa = pag_activa;
	}
	/**
	 * @param prodsap , prodsap a modificar.
	 */
	public void setProdsap(String prodsap) {
		this.prodsap = prodsap;
	}
	/**
	 * @param regsperpage , regsperpage a modificar.
	 */
	public void setRegsperpage(int regsperpage) {
		this.regsperpage = regsperpage;
	}
	/**
	 * @return Retorna estado.
	 */
	public char getEstado() {
		return estado;
	}
	/**
	 * @param estado , estado a modificar.
	 */
	public void setEstado(char estado) {
		this.estado = estado;
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
	 * @return Retorna codprod.
	 */
	public String getCodprod() {
		return codprod;
	}
	/**
	 * @param codprod , codprod a modificar.
	 */
	public void setCodprod(String codprod) {
		this.codprod = codprod;
	}
	/**
	 * @return Retorna id_cat.
	 */
	public int getId_cat() {
		return id_cat;
	}
	/**
	 * @param id_cat , id_cat a modificar.
	 */
	public void setId_cat(int id_cat) {
		this.id_cat = id_cat;
	}
	/**
	 * @return Retorna descrip.
	 */
	public String getDescrip() {
		return descrip;
	}
	/**
	 * @param descrip , descrip a modificar.
	 */
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
	
	
	
}
