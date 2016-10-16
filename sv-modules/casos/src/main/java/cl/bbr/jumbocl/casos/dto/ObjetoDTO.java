/*
 * Creado el 03-08-2007
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.jumbocl.casos.dto;

import java.io.Serializable;

/**
 * @author imoyano
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class ObjetoDTO implements Serializable {
    
    private long idObjeto;
    private String nombre;
    private String activado;
    
    public ObjetoDTO() {
        this.idObjeto = 0;
        this.nombre = "";
        this.activado = "0";
        
    }
    
    /**
     * @return Devuelve idObjeto.
     */
    public long getIdObjeto() {
        return idObjeto;
    }
    /**
     * @return Devuelve nombre.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @param idObjeto El idObjeto a establecer.
     */
    public void setIdObjeto(long idObjeto) {
        this.idObjeto = idObjeto;
    }
    /**
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
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
