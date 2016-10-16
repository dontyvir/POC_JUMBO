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
public class PasoDTO {
    
    private long idPaso;
    private String nombre;
    
	public PasoDTO() {
	    this.idPaso = 0;
	    this.nombre = "";
	}

	
    /**
     * @return Devuelve idPaso.
     */
    public long getIdPaso() {
        return idPaso;
    }
    /**
     * @return Devuelve nombre.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @param idPaso El idPaso a establecer.
     */
    public void setIdPaso(long idPaso) {
        this.idPaso = idPaso;
    }
    /**
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
