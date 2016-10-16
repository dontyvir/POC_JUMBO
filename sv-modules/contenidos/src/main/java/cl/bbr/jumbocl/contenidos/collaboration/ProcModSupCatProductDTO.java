package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

public class ProcModSupCatProductDTO implements Serializable{
      private long id_producto; // obligatorio
      private long id_categoria; // obligatorio
      private String action; // obligatorio
	/**
	 * 
	 */
	public ProcModSupCatProductDTO() {
	}
	/**
	 * @param id_producto
	 * @param id_categoria
	 * @param action
	 */
	public ProcModSupCatProductDTO(long id_producto, long id_categoria, String action) {
		this.id_producto = id_producto;
		this.id_categoria = id_categoria;
		this.action = action;
	}
	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @return Returns the id_categoria.
	 */
	public long getId_categoria() {
		return id_categoria;
	}
	/**
	 * @return Returns the id_producto.
	 */
	public long getId_producto() {
		return id_producto;
	}
	/**
	 * @param action The action to set.
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @param id_categoria The id_categoria to set.
	 */
	public void setId_categoria(long id_categoria) {
		this.id_categoria = id_categoria;
	}
	/**
	 * @param id_producto The id_producto to set.
	 */
	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}
	
      
      
}
