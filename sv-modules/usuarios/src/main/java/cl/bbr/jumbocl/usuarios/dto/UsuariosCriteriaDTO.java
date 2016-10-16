package cl.bbr.jumbocl.usuarios.dto;

import java.io.Serializable;

public class UsuariosCriteriaDTO implements Serializable {
	private int pag;
	private int regsperpage;
	private boolean pag_activa;
	private char activo;
	private String apellido = null;
	private String login	= null;
	private long id_local;
	
	public UsuariosCriteriaDTO(){
		this.login = null;
		this.apellido = null;
	}
	
	/**
	 * @param pag
	 * @param activo
	 * @param regsperpage
	 * @param apellido
	 * @param login
	 */
	public UsuariosCriteriaDTO(int pag, char activo, int regsperpage, String apellido, String login, boolean pag_act) {
		super();
		this.pag = pag;
		this.activo = activo;
		this.regsperpage = regsperpage;
		this.apellido = apellido;
		this.login = login;
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
	 * @return Returns the login.
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login The login to set.
	 */
	public void setLogin(String login) {
		this.login = login;
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

	public long getId_local() {
		return id_local;
	}

	public void setId_local(long id_local) {
		this.id_local = id_local;
	}


}
