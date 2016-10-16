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
public class JornadaDTO implements Serializable {
    
    private long idJornada;
    private String descripcion;
    private String activado;
    
    public JornadaDTO() {
        this.idJornada = 0;
        this.descripcion = "";
        this.activado = "0";
    }
    
    

    /**
     * @return Devuelve descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @return Devuelve idJornada.
     */
    public long getIdJornada() {
        return idJornada;
    }
    /**
     * @param descripcion El descripcion a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * @param idJornada El idJornada a establecer.
     */
    public void setIdJornada(long idJornada) {
        this.idJornada = idJornada;
    }
    /**
     * @return Devuelve activado.
     */
    public String getActivado() {
        return activado;
    }
    /**
     * @param activado El activado a establecer.
     */
    public void setActivado(String activado) {
        this.activado = activado;
    }
}
