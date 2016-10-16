package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;

/**
 * 
 * @author imoyano
 *
 */

public class RutConfiableDTO implements Serializable{
    
	private long rut;
	private String dv;
	private String fechaCreacion;
	
	public RutConfiableDTO() {
	}

	
	
	
    /**
     * @return Devuelve dv.
     */
    public String getDv() {
        return dv;
    }
    /**
     * @return Devuelve fechaCreacion.
     */
    public String getFechaCreacion() {
        return fechaCreacion;
    }
    /**
     * @return Devuelve rut.
     */
    public long getRut() {
        return rut;
    }
    /**
     * @param dv El dv a establecer.
     */
    public void setDv(String dv) {
        this.dv = dv;
    }
    /**
     * @param fechaCreacion El fechaCreacion a establecer.
     */
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    /**
     * @param rut El rut a establecer.
     */
    public void setRut(long rut) {
        this.rut = rut;
    }
}
