package cl.bbr.jumbocl.common.dto;

import java.io.Serializable;

public class RegionDTO implements Serializable {
	
    private int	   idRegion;
	private String nombre;
    private int	   numero;
		
    /**
     * @return Devuelve idRegion.
     */
    public int getIdRegion() {
        return idRegion;
    }
    /**
     * @return Devuelve nombre.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @return Devuelve numero.
     */
    public int getNumero() {
        return numero;
    }
    /**
     * @param idRegion El idRegion a establecer.
     */
    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }
    /**
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * @param numero El numero a establecer.
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }
}
