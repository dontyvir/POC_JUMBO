package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;

public class ClienteCriteriaDTO implements Serializable {
	private int pagina;
	private String rut;
	private String apellido;
	private int regsperpage;
	private boolean pag_activa;
	private String est_bloqueo;
	
	//(20-10-2014) NSepulveda - Filtro para buscar clientes en el BOC con el dato del mail
	private String email;
	
	public String getEst_bloqueo() {
		return est_bloqueo;
	}

	public void setEst_bloqueo(String est_bloqueo) {
		this.est_bloqueo = est_bloqueo;
	}

	public ClienteCriteriaDTO() {
	}

	/**
	 * @param pagina
	 * @param rut
	 * @param apellido
	 * @param est_bloqueo
	 * @param regsperpage
	 * @param pag_activa
	 */
	public ClienteCriteriaDTO(int pagina, String rut, String apellido, String est_bloqueo, int regsperpage, boolean pag_activa) {
		this.pagina = pagina;
		this.rut = rut;
		this.apellido = apellido;
		this.est_bloqueo = est_bloqueo;
		this.regsperpage = regsperpage;
		this.pag_activa = pag_activa;
	}

	/**
	 * @return Returns the apellido.
	 */
	public String getApellido() {
		return apellido;
	}

	/**
	 * @return Returns the pag_activa.
	 */
	public boolean isPag_activa() {
		return pag_activa;
	}

	/**
	 * @return Returns the pagina.
	 */
	public int getPagina() {
		return pagina;
	}

	/**
	 * @return Returns the regsperpage.
	 */
	public int getRegsperpage() {
		return regsperpage;
	}

	/**
	 * @return Returns the rut.
	 */
	public String getRut() {
		return rut;
	}

	/**
	 * @param apellido The apellido to set.
	 */
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	/**
	 * @param pag_activa The pag_activa to set.
	 */
	public void setPag_activa(boolean pag_activa) {
		this.pag_activa = pag_activa;
	}

	/**
	 * @param pagina The pagina to set.
	 */
	public void setPagina(int pagina) {
		this.pagina = pagina;
	}

	/**
	 * @param regsperpage The regsperpage to set.
	 */
	public void setRegsperpage(int regsperpage) {
		this.regsperpage = regsperpage;
	}

	/**
	 * @param rut The rut to set.
	 */
	public void setRut(String rut) {
		this.rut = rut;
	}

	/**
	 * 
	 * @return the email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}
