package cl.bbr.jumbocl.parametros.dto;

import java.io.Serializable;

public class ParametroDTO implements Serializable{
    private int idParametro;
	private String nombre;
	private String valor;
	
	
	
    /**
     * @return Devuelve idParametro.
     */
    public int getIdParametro() {
        return idParametro;
    }
    /**
     * @return Devuelve nombre.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @return Devuelve valor.
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param idParametro El idParametro a establecer.
     */
    public void setIdParametro(int idParametro) {
        this.idParametro = idParametro;
    }
    /**
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * @param valor El valor a establecer.
     */
    public void setValor(String valor) {
        this.valor = valor;
    }
}
