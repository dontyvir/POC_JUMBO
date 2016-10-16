package cl.bbr.jumbocl.pedidos.collaboration;

import java.io.Serializable;

public class ProcCreaJornadaDespachoDTO implements Serializable {
	private long id_sem_origen;
	private long id_sem_inicial;
	private long id_sem_final;
	private long id_zona;
	
	/**
	 * @return Returns the id_sem_final.
	 */
	public long getId_sem_final() {
		return id_sem_final;
	}
	/**
	 * @param id_sem_final The id_sem_final to set.
	 */
	public void setId_sem_final(long id_sem_final) {
		this.id_sem_final = id_sem_final;
	}
	/**
	 * @return Returns the id_sem_inicial.
	 */
	public long getId_sem_inicial() {
		return id_sem_inicial;
	}
	/**
	 * @param id_sem_inicial The id_sem_inicial to set.
	 */
	public void setId_sem_inicial(long id_sem_inicial) {
		this.id_sem_inicial = id_sem_inicial;
	}
	/**
	 * @return Returns the id_sem_origen.
	 */
	public long getId_sem_origen() {
		return id_sem_origen;
	}
	/**
	 * @param id_sem_origen The id_sem_origen to set.
	 */
	public void setId_sem_origen(long id_sem_origen) {
		this.id_sem_origen = id_sem_origen;
	}
	/**
	 * @return Returns the id_zona.
	 */
	public long getId_zona() {
		return id_zona;
	}
	/**
	 * @param id_zona The id_zona to set.
	 */
	public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
	}
	
	

}
