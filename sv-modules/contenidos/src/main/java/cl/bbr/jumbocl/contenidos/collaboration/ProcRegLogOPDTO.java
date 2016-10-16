package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

public class ProcRegLogOPDTO implements Serializable{
      private String texto; // obligatorio
      private long id_motivo_nuevo; // obligatorio
	/**
	 * 
	 */
	public ProcRegLogOPDTO() {
	}
	/**
	 * @param texto
	 * @param id_motivo_nuevo
	 */
	public ProcRegLogOPDTO(String texto, long id_motivo_nuevo) {
		this.texto = texto;
		this.id_motivo_nuevo = id_motivo_nuevo;
	}
	/**
	 * @return Returns the id_motivo_nuevo.
	 */
	public long getId_motivo_nuevo() {
		return id_motivo_nuevo;
	}
	/**
	 * @return Returns the texto.
	 */
	public String getTexto() {
		return texto;
	}
	/**
	 * @param id_motivo_nuevo The id_motivo_nuevo to set.
	 */
	public void setId_motivo_nuevo(long id_motivo_nuevo) {
		this.id_motivo_nuevo = id_motivo_nuevo;
	}
	/**
	 * @param texto The texto to set.
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}
    
      
}
