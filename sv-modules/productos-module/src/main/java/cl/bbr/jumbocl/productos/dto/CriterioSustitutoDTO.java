package cl.bbr.jumbocl.productos.dto;

import java.io.Serializable;

/**
 * DTO para datos de los criterios de sustitutos. 
 * 
 * @author imoyano
 *
 */
public class CriterioSustitutoDTO implements Serializable {

    private long idProducto;
	private long idCriterio;
	private String descripcion;
    private String sustitutoCliente;
    private String asignoCliente; //S: Criterio asignado por cliente   N: Criterio asignado por el sistema al comprar
		
    /**
     * @return Devuelve descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @return Devuelve idCriterio.
     */
    public long getIdCriterio() {
        return idCriterio;
    }
    /**
     * @return Devuelve sustitutoCliente.
     */
    public String getSustitutoCliente() {
        return sustitutoCliente;
    }
    /**
     * @param descripcion El descripcion a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * @param idCriterio El idCriterio a establecer.
     */
    public void setIdCriterio(long idCriterio) {
        this.idCriterio = idCriterio;
    }
    /**
     * @param sustitutoCliente El sustitutoCliente a establecer.
     */
    public void setSustitutoCliente(String sustitutoCliente) {
        this.sustitutoCliente = sustitutoCliente;
    }
    /**
     * @return Devuelve idProducto.
     */
    public long getIdProducto() {
        return idProducto;
    }
    /**
     * @param idProducto El idProducto a establecer.
     */
    public void setIdProducto(long idProducto) {
        this.idProducto = idProducto;
    }
    /**
     * @return Devuelve asignoCliente.
     */
    public String getAsignoCliente() {
        return asignoCliente;
    }
    /**
     * @param asignoCliente El asignoCliente a establecer.
     */
    public void setAsignoCliente(String asignoCliente) {
        this.asignoCliente = asignoCliente;
    }
}