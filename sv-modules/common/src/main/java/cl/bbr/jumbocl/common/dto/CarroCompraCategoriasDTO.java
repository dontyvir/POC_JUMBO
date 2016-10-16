package cl.bbr.jumbocl.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para datos de los productos del carro de compras y sus categorías. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class CarroCompraCategoriasDTO implements Serializable {

	private long id;
	private String categoria;
	private List CarroCompraProductosDTO;
			
	public CarroCompraCategoriasDTO() {
		this.CarroCompraProductosDTO = new ArrayList();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public List getCarroCompraProductosDTO() {
		return CarroCompraProductosDTO;
	}
	public void setCarroCompraProductosDTO(List carroCompraProductosDTO) {
		CarroCompraProductosDTO = carroCompraProductosDTO;
	}

		
}