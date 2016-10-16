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
public class TipoEventoDTO {
    
    private long idTipoEvento;
    private String nombre;
    
	public TipoEventoDTO() {
	    this.idTipoEvento = 0;
	    this.nombre = "";
	}

	
    /**
     * @return Devuelve idTipoEvento.
     */
    public long getIdTipoEvento() {
        return idTipoEvento;
    }
    /**
     * @return Devuelve nombre.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @param idTipoEvento El idTipoEvento a establecer.
     */
    public void setIdTipoEvento(long idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }
    /**
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
