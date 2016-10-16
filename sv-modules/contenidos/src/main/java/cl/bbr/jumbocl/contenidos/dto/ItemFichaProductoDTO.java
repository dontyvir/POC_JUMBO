package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;

public class ItemFichaProductoDTO implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1311011003005393040L;
	private long pfiItem;				
	private String pfiDescripcion;
	
	public ItemFichaProductoDTO(){
		
	}

	public ItemFichaProductoDTO(long pfiItem, String pfiDescripcion) {
		super();
		this.pfiItem = pfiItem;
		this.pfiDescripcion = pfiDescripcion;
	}

	public long getPfiItem() {
		return pfiItem;
	}

	public void setPfiItem(long pfiItem) {
		this.pfiItem = pfiItem;
	}

	public String getPfiDescripcion() {
		return pfiDescripcion;
	}

	public void setPfiDescripcion(String pfiDescripcion) {
		this.pfiDescripcion = pfiDescripcion;
	}
}