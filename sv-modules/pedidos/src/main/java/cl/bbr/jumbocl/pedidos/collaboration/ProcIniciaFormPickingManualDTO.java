package cl.bbr.jumbocl.pedidos.collaboration;

public class ProcIniciaFormPickingManualDTO {

	private long id_ronda;
	private long id_usuario;
	private long id_perfil;
	private String login;
	
	/**
	 * @return Returns the id_perfil.
	 */
	public long getId_perfil() {
		return id_perfil;
	}
	/**
	 * @param id_perfil The id_perfil to set.
	 */
	public void setId_perfil(long id_perfil) {
		this.id_perfil = id_perfil;
	}
	/**
	 * @return Returns the id_ronda.
	 */
	public long getId_ronda() {
		return id_ronda;
	}
	/**
	 * @param id_ronda The id_ronda to set.
	 */
	public void setId_ronda(long id_ronda) {
		this.id_ronda = id_ronda;
	}
	/**
	 * @return Returns the id_usuario.
	 */
	public long getId_usuario() {
		return id_usuario;
	}
	/**
	 * @param id_usuario The id_usuario to set.
	 */
	public void setId_usuario(long id_usuario) {
		this.id_usuario = id_usuario;
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
	
}
