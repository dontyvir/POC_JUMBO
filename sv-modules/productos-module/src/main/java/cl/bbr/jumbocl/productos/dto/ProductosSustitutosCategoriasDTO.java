package cl.bbr.jumbocl.productos.dto;

import java.io.Serializable;
import java.util.List;

public class ProductosSustitutosCategoriasDTO implements Serializable {

	private static final long serialVersionUID = -1898691387865666656L;
	
	private long id;
	private String categoria;
	private List sustitutos;
    	
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
    public List getSustitutos() {
        return sustitutos;
    }
    public void setSustitutos(List sustitutos) {
        this.sustitutos = sustitutos;
    }

}
