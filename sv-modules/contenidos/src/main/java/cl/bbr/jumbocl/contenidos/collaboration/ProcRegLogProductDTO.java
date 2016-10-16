package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

public class ProcRegLogProductDTO implements Serializable{
      private String texto; // obligatorio

	/**
	 * 
	 */
	public ProcRegLogProductDTO() {
		super();
	}

	/**
	 * @param texto
	 */
	public ProcRegLogProductDTO(String texto) {
		this.texto = texto;
	}

	/**
	 * @return Returns the texto.
	 */
	public String getTexto() {
		return texto;
	}

	/**
	 * @param texto The texto to set.
	 */
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
      
}
