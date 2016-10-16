package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO para datos de los sustitutos. 
 * 
 * @author imoyano
 *
 */
public class SustitutosCategoriasDTO implements Serializable {

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