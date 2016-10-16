package cl.bbr.jumbocl.pedidos.dto;

public class AsignaOPDTO {
	private long id_pedido;
	private long id_usuario;
	private long id_motivo;
	private long id_pedido_usr_act;
	private String usr_login;
	private String log;

	public AsignaOPDTO() {
	}
	/**
	 * @return Returns the id_pedido.
	 */
	public long getId_pedido() {
		return id_pedido;
	}
	/**
	 * @return Returns the id_usuario.
	 */
	public long getId_usuario() {
		return id_usuario;
	}
	
	/**
	 * @param id_pedido The id_pedido to set.
	 */
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	/**
	 * @param id_usuario The id_usuario to set.
	 */
	public void setId_usuario(long id_usuario) {
		this.id_usuario = id_usuario;
	}

	public long getId_motivo() {
		return id_motivo;
	}
	public void setId_motivo(long id_motivo) {
		this.id_motivo = id_motivo;
	}
	
	public long getId_pedido_usr_act() {
		return id_pedido_usr_act;
	}
	public void setId_pedido_usr_act(long id_pedido_usr_act) {
		this.id_pedido_usr_act = id_pedido_usr_act;
	}
	public String getUsr_login() {
		return usr_login;
	}
	public void setUsr_login(String usr_login) {
		this.usr_login = usr_login;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	
}
