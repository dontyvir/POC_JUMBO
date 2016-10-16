/*
 * Creado el 03-08-2007
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c�digo - Plantillas de c�digo
 */
package cl.bbr.jumbocl.common.dto;

import java.io.Serializable;

/**
 * @author imoyano
 *
 * Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c�digo - Plantillas de c�digo
 */
public class ObjetoDTO implements Serializable {
    
    private long idObjeto;
    private String nombre;
    private String activado;
    private String codigo;
    
    public ObjetoDTO() {
        this.idObjeto = 0;
        this.nombre = "";
        this.activado = "0";
        this.codigo = "";
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
    
    /**
     * @return Devuelve codigo.
     */
    public String getCodigo() {
        return codigo;
    }
    /**
     * @param codigo El codigo a establecer.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
