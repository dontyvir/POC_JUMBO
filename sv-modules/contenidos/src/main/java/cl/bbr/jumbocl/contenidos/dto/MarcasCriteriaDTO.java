package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;

public class MarcasCriteriaDTO implements Serializable {

	/**
	 * Pagina del listado. 
	 */
	private int pag;
	
	/**
	 * Numero de registros por pagina. 
	 */
	private int regsperpage;
	
	/**
	 * Numero de pagina activa 
	 */
	private boolean pag_activa;

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
