package cl.bbr.jumbocl.pedidos.promos.interfaces.dto;

public class ConsultaCuponesPorRutDTO {
	private int cod_local_pos;
	private int num_pos;
	private int documento;
	private int operador;
	private int journal;
	private long rut;

	public ConsultaCuponesPorRutDTO() {

	}

	/**
	 * @return Returns the rut.
	 */
	public long getRut() {
		return rut;
	}

	/**
	 * @param rut The rut to set.
	 */
	public void setRut(long rut) {
		this.rut = rut;
	}

	/**
	 * @return Returns the num_pos.
	 */
	public int getNum_pos() {
		return num_pos;
	}

	/**
	 * @param num_pos The num_pos to set.
	 */
	public void setNum_pos(int num_pos) {
		this.num_pos = num_pos;
	}

	/**
	 * @return Returns the cod_local_pos.
	 */
	public int getCod_local_pos() {
		return cod_local_pos;
	}

	/**
	 * @param cod_local_pos The cod_local_pos to set.
	 */
	public void setCod_local_pos(int cod_local_pos) {
		this.cod_local_pos = cod_local_pos;
	}

	/**
	 * @return Returns the documento.
	 */
	public int getDocumento() {
		return documento;
	}

	/**
	 * @param documento The documento to set.
	 */
	public void setDocumento(int documento) {
		this.documento = documento;
	}

	/**
	 * @return Returns the journal.
	 */
	public int getJournal() {
		return journal;
	}

	/**
	 * @param journal The journal to set.
	 */
	public void setJournal(int journal) {
		this.journal = journal;
	}

	/**
	 * @return Returns the operador.
	 */
	public int getOperador() {
		return operador;
	}

	/**
	 * @param operador The operador to set.
	 */
	public void setOperador(int operador) {
		this.operador = operador;
	}

}
