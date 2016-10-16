package cl.bbr.jumbocl.pedidos.promos.dto;

public class SafDTO {
	private long id_saf;
	private String msg;
	private String estado;

	public SafDTO() {
	
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return Returns the id_saf.
	 */
	public long getId_saf() {
		return id_saf;
	}

	/**
	 * @param id_saf The id_saf to set.
	 */
	public void setId_saf(long id_saf) {
		this.id_saf = id_saf;
	}

	/**
	 * @return Returns the msg.
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg The msg to set.
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
