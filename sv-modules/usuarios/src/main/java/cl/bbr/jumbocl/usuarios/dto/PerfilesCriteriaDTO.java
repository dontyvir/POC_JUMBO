package cl.bbr.jumbocl.usuarios.dto;

import java.io.Serializable;

public class PerfilesCriteriaDTO implements Serializable {
	private int pag;
	private int regsperpage;
	private boolean pag_activa;
	private char activo;
	
	
	/**
	 * @param pag
	 * @param activo
	 * @param regsperpage
	 */
	public PerfilesCriteriaDTO(int pag, char activo, int regsperpage, boolean pag_act) {
		super();
		this.pag = pag;
		this.activo = activo;
		this.regsperpage = regsperpage;
		this.pag_activa = pag_act;
	}
	/**
	 * @return Returns the activo.
	 */
	public char getActivo() {
		return activo;
	}
	/**
	 * @param activo The activo to set.
	 */
	public void setActivo(char activo) {
		this.activo = activo;
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
	 * @return Returns the pag_activa.
	 */
	public boolean isPag_activa() {
		return pag_activa;
	}
	/**
	 * @param pag_activa The pag_activa to set.
	 */
	public void setPag_activa(boolean pag_activa) {
		this.pag_activa = pag_activa;
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

}
