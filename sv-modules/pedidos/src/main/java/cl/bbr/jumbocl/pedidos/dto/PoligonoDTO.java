package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class PoligonoDTO implements Serializable {

	private long   id_poligono;
	private long   id_zona;
	private long   id_comuna;
	private long   num_poligono;
	private String descripcion;
	
	public PoligonoDTO(){
		
	}

	
    /**
     * @return Devuelve descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @return Devuelve id_comuna.
     */
    public long getId_comuna() {
        return id_comuna;
    }
    /**
     * @return Devuelve id_poligono.
     */
    public long getId_poligono() {
        return id_poligono;
    }
    /**
     * @return Devuelve id_zona.
     */
    public long getId_zona() {
        return id_zona;
    }
    /**
     * @return Devuelve num_poligono.
     */
    public long getNum_poligono() {
        return num_poligono;
    }
    /**
     * @param descripcion El descripcion a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * @param id_comuna El id_comuna a establecer.
     */
    public void setId_comuna(long id_comuna) {
        this.id_comuna = id_comuna;
    }
    /**
     * @param id_poligono El id_poligono a establecer.
     */
    public void setId_poligono(long id_poligono) {
        this.id_poligono = id_poligono;
    }
    /**
     * @param id_zona El id_zona a establecer.
     */
    public void setId_zona(long id_zona) {
        this.id_zona = id_zona;
    }
    /**
     * @param num_poligono El num_poligono a establecer.
     */
    public void setNum_poligono(long num_poligono) {
        this.num_poligono = num_poligono;
    }
}
