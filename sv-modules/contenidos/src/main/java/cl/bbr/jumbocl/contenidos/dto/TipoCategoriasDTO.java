package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;

public class TipoCategoriasDTO implements Serializable{
	public int id_tipo;
	public String nom_tipo;
	/**
	 * @return Returns the id_tipo.
	 */
	public int getId_tipo() {
		return id_tipo;
	}
	/**
	 * @param id_tipo The id_tipo to set.
	 */
	public void setId_tipo(int id_tipo) {
		this.id_tipo = id_tipo;
	}
	/**
	 * @return Returns the nom_tipo.
	 */
	public String getNom_tipo() {
		return nom_tipo;
	}
	/**
	 * @param nom_tipo The nom_tipo to set.
	 */
	public void setNom_tipo(String nom_tipo) {
		this.nom_tipo = nom_tipo;
	}
	
}
