package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

/**
 * Clase que contiene la información de liberar una OP.
 * @author BBR
 *
 */
public class ProcLiberaOPDTO implements Serializable{
      
	/**
	 * Id del pedido 
	 */
	private long id_op; // obligatorio
      
	/**
	 * Texto que ingresó el usuario. 
	 */
	private String texto; // obligatorio
      
	/**
	 * Identificador del motivo nuevo. 
	 */
	private long id_motivo_nuevo; // obligatorio
	
	/**
	 * Constructor inicial. 
	 */
	public ProcLiberaOPDTO() {
	}
	
	/**
	 * @param id_op
	 * @param texto
	 * @param id_motivo_nuevo
	 */
	public ProcLiberaOPDTO(long id_op , String texto , long id_motivo_nuevo) {
		this.id_op = id_op;
		this.texto = texto;
		this.id_motivo_nuevo = id_motivo_nuevo;
	}
	
	/**
	 * @return Retorna id_motivo_nuevo.
	 */
	public long getId_motivo_nuevo() {
		return id_motivo_nuevo;
	}
	/**
	 * @return Retorna id_op.
	 */
	public long getId_op() {
		return id_op;
	}
	/**
	 * @return Retorna texto.
	 */
	public String getTexto() {
		return texto;
	}
	/**
	 * @param id_motivo_nuevo , id_motivo_nuevo a modificar.
	 */
	public void setId_motivo_nuevo(long id_motivo_nuevo) {
		this.id_motivo_nuevo = id_motivo_nuevo;
	}
	/**
	 * @param id_op , id_op a modificar.
	 */
	public void setId_op(long id_op) {
		this.id_op = id_op;
	}
	/**
	 * @param texto , texto a modificar.
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
      
      
}
