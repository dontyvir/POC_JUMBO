package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

public class ProcModSubCatWebDTO implements Serializable{
	private final static long serialVersionUID = 1;
	
      private String action; // obligatorio, valores: A Asocia, D Desasociar
      private long id_subcategoria; // obligatorio
      private long id_cat; //obligatorio
      private int orden; //obligatorio
      private long id_usr;
      
	/**
	 * @return Returns the id_usr.
	 */
	public long getId_usr() {
		return id_usr;
	}
	/**
	 * @param id_usr The id_usr to set.
	 */
	public void setId_usr(long id_usr) {
		this.id_usr = id_usr;
	}

	/**
	 * @param action
	 * @param id_subcategoria
	 * @param id_cat
	 * @param id_usr
	 */
	public ProcModSubCatWebDTO(String action, long id_subcategoria, long id_cat, long id_usr) {
		super();
		this.action = action;
		this.id_subcategoria = id_subcategoria;
		this.id_cat = id_cat;
		this.id_usr = id_usr;
	}
	/**
	 * @return Returns the orden.
	 */
	public int getOrden() {
		return orden;
	}
	/**
	 * @param orden The orden to set.
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}
	/**
	 * 
	 */
	public ProcModSubCatWebDTO() {
	}
	/**
	 * @param action
	 * @param id_subcategoria
	 */
	public ProcModSubCatWebDTO(String action, long id_subcategoria) {
		this.action = action;
		this.id_subcategoria = id_subcategoria;
	}
	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @return Returns the id_subcategoria.
	 */
	public long getId_subcategoria() {
		return id_subcategoria;
	}
	/**
	 * @param action The action to set.
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @param id_subcategoria The id_subcategoria to set.
	 */
	public void setId_subcategoria(long id_subcategoria) {
		this.id_subcategoria = id_subcategoria;
	}
	/**
	 * @return Returns the id_cat.
	 */
	public long getId_cat() {
		return id_cat;
	}
	/**
	 * @param id_cat The id_cat to set.
	 */
	public void setId_cat(long id_cat) {
		this.id_cat = id_cat;
	}
	
      
}
