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
public class QuiebreCasoDTO implements Serializable {
    
    private long idQuiebre;
    private String nombre;
    private long puntaje;
    private String activado;
    
    public QuiebreCasoDTO() {
        this.idQuiebre = 0;
        this.nombre = "";
        this.puntaje = 0;
        this.activado = "0";
    }
    
    /**
     * @return Devuelve idQuiebre.
     */
    public long getIdQuiebre() {
        return idQuiebre;
    }
    /**
     * @return Devuelve nombre.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @return Devuelve puntaje.
     */
    public long getPuntaje() {
        return puntaje;
    }
    /**
     * @param idQuiebre El idQuiebre a establecer.
     */
    public void setIdQuiebre(long idQuiebre) {
        this.idQuiebre = idQuiebre;
    }
    /**
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * @param puntaje El puntaje a establecer.
     */
    public void setPuntaje(long puntaje) {
        this.puntaje = puntaje;
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
