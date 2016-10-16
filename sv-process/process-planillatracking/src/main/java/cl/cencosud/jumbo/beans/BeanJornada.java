/*
 * Creado el 26-01-2006
 *
 * Para cambiar la plantilla para este archivo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
package cl.cencosud.jumbo.beans;


/**
 * @author rbelmar
 *
 * Para cambiar la plantilla para este comentario de tipo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
public class BeanJornada {

    private String horario;
    private short color;



    /**
     * @return Devuelve color.
     */
    public short getColor() {
        return color;
    }
    /**
     * @return Devuelve horario.
     */
    public String getHorario() {
        return horario;
    }
    /**
     * @param color El color a establecer.
     */
    public void setColor(short color) {
        this.color = color;
    }
    /**
     * @param horario El horario a establecer.
     */
    public void setHorario(String horario) {
        this.horario = horario;
    }
}
