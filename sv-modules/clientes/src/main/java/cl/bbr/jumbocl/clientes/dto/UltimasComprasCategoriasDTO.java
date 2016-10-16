package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO para datos de las últimas compras. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class UltimasComprasCategoriasDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6837699842415421983L;
	private long id;
	private String categoria;
	private List UltimasComprasProductosDTO;
	
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
	public List getUltimasComprasProductosDTO() {
		return UltimasComprasProductosDTO;
	}
	public void setUltimasComprasProductosDTO(List ultimasComprasProductosDTO) {
		UltimasComprasProductosDTO = ultimasComprasProductosDTO;
	}
}