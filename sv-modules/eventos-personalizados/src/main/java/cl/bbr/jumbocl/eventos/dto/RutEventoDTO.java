/*
 * Creado el 13-11-2007
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.jumbocl.eventos.dto;

/**
 * @author imoyano
 *
 * Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class RutEventoDTO {
    
    private long idEvento;
    private long cliRut;
    private String cliDv;
    private String fechaCreacion;
    private String fechaModificacion;
    private long ocurrenciaPorRut;
    
	public RutEventoDTO() {
	    this.idEvento = 0;
	    this.cliRut = 0;
	    this.cliDv = "";
	    this.fechaCreacion = "";
	    this.fechaModificacion = "";
	    this.ocurrenciaPorRut = 0;
	}

    /**
     * @return Devuelve cliDv.
     */
    public String getCliDv() {
        return cliDv;
    }
    /**
     * @return Devuelve cliRut.
     */
    public long getCliRut() {
        return cliRut;
    }
    /**
     * @return Devuelve fechaCreacion.
     */
    public String getFechaCreacion() {
        return fechaCreacion;
    }
    /**
     * @return Devuelve idEvento.
     */
    public long getIdEvento() {
        return idEvento;
    }
    /**
     * @return Devuelve ocurrenciaPorRut.
     */
    public long getOcurrenciaPorRut() {
        return ocurrenciaPorRut;
    }
    /**
     * @param cliDv El cliDv a establecer.
     */
    public void setCliDv(String cliDv) {
        this.cliDv = cliDv;
    }
    /**
     * @param cliRut El cliRut a establecer.
     */
    public void setCliRut(long cliRut) {
        this.cliRut = cliRut;
    }
    /**
     * @param fechaCreacion El fechaCreacion a establecer.
     */
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    /**
     * @param idEvento El idEvento a establecer.
     */
    public void setIdEvento(long idEvento) {
        this.idEvento = idEvento;
    }
    /**
     * @param ocurrenciaPorRut El ocurrenciaPorRut a establecer.
     */
    public void setOcurrenciaPorRut(long ocurrenciaPorRut) {
        this.ocurrenciaPorRut = ocurrenciaPorRut;
    }
    /**
     * @return Devuelve fechaModificacion.
     */
    public String getFechaModificacion() {
        return fechaModificacion;
    }
    /**
     * @param fechaModificacion El fechaModificacion a establecer.
     */
    public void setFechaModificacion(String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}
