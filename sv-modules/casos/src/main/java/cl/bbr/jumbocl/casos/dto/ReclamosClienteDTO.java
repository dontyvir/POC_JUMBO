/*
 * Creado el 03-08-2007
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.jumbocl.casos.dto;

import java.io.Serializable;

/**
 * @author imoyano
 *
 * Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class ReclamosClienteDTO implements Serializable {
    
    private long cliRut;
    private String dv;
    private String[] reclamos;
    
    public ReclamosClienteDTO() {        
    }
    
    public ReclamosClienteDTO(int periodos) {
        this.cliRut = 0;
        this.dv = "";
        this.reclamos = new String[periodos];
        
    }
    
    /**
     * @return Devuelve cliRut.
     */
    public long getCliRut() {
        return cliRut;
    }
    /**
     * @return Devuelve dv.
     */
    public String getDv() {
        return dv;
    }
    /**
     * @return Devuelve reclamos.
     */
    public String[] getReclamos() {
        return reclamos;
    }
    /**
     * @param cliRut El cliRut a establecer.
     */
    public void setCliRut(long cliRut) {
        this.cliRut = cliRut;
    }
    /**
     * @param dv El dv a establecer.
     */
    public void setDv(String dv) {
        this.dv = dv;
    }
    /**
     * @param reclamos El reclamos a establecer.
     */
    public void setReclamos(String[] reclamos) {
        this.reclamos = reclamos;
    }
}
