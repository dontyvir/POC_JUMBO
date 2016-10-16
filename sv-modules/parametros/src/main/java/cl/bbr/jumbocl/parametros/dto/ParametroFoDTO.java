package cl.bbr.jumbocl.parametros.dto;

import java.io.Serializable;

public class ParametroFoDTO implements Serializable {

	private long id;
	private String nombre;
	private String llave;
	private String descripcion;
	private String valor;

	/**
	 * Constructor
	 */
	public ParametroFoDTO() {
	}
	
    /**
     * @return Devuelve descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @param descripcion El descripcion a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * @return Devuelve id.
     */
    public long getId() {
        return id;
    }
    /**
     * @param id El id a establecer.
     */
    public void setId(long id) {
        this.id = id;
    }
    /**
     * @return Devuelve llave.
     */
    public String getLlave() {
        return llave;
    }
    /**
     * @param llave El llave a establecer.
     */
    public void setLlave(String llave) {
        this.llave = llave;
    }
    /**
     * @return Devuelve nombre.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * @return Devuelve valor.
     */
    public String getValor() {
        return valor;
    }
    /**
     * @param valor El valor a establecer.
     */
    public void setValor(String valor) {
        this.valor = valor;
    }
}