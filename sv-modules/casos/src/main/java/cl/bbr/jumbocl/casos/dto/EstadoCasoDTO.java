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
public class EstadoCasoDTO implements Serializable {
    
    private long idEstado;
    private String nombre;
    
    public EstadoCasoDTO() {
        this.idEstado = 0;
        this.nombre = "";
    }

    /**
     * @return Devuelve idEstado.
     */
    public long getIdEstado() {
        return idEstado;
    }
    /**
     * @return Devuelve nombre.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @param idEstado El idEstado a establecer.
     */
    public void setIdEstado(long idEstado) {
        this.idEstado = idEstado;
    }
    /**
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
