/*
 * Creado el 26-01-2006
 *
 * Para cambiar la plantilla para este archivo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
package cl.cencosud.jumbo.beans;

import java.util.Map;

/**
 * @author rbelmar
 *
 * Para cambiar la plantilla para este comentario de tipo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
public class BeanParamConfig {
    private String nombreArchivo;
	private String tipo;
	private Map horarios;
	private String numReintentos;
	private String intervalo;
	private String puerto;
	private String server;
	private String from;
	private Map to;
	private Map cc;
	private Map cco;
	private String subject;
	private String mensaje;
    private String pathImagen1;
    private String pathImagen2;
    private String EliminarArchivosTemporales;


    /**
     * @return Devuelve destinatarios.
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }
    /**
     * @return Devuelve to.
     */
    public Map getTo() {
        return to;
    }
    /**
     * @return Devuelve cc.
     */
    public Map getCc() {
        return cc;
    }
    /**
     * @return Devuelve cco.
     */
    public Map getCco() {
        return cco;
    }
    /**
     * @return Devuelve from.
     */
    public String getFrom() {
        return from;
    }
    /**
     * @return Devuelve horarios.
     */
    public Map getHorarios() {
        return horarios;
    }
    /**
     * @return Devuelve intervalo.
     */
    public String getIntervalo() {
        return intervalo;
    }
    /**
     * @return Devuelve mensaje.
     */
    public String getMensaje() {
        return mensaje;
    }
    /**
     * @return Devuelve numReintentos.
     */
    public String getNumReintentos() {
        return numReintentos;
    }
    /**
     * @return Devuelve puerto.
     */
    public String getPuerto() {
        return puerto;
    }
    /**
     * @return Devuelve server.
     */
    public String getServer() {
        return server;
    }
    /**
     * @return Devuelve subject.
     */
    public String getSubject() {
        return subject;
    }
    /**
     * @return Devuelve tipo.
     */
    public String getTipo() {
        return tipo;
    }
    /**
     * @return Devuelve pathImagen1.
     */
    public String getPathImagen1() {
        return pathImagen1;
    }
    /**
     * @return Devuelve pathImagen2.
     */
    public String getPathImagen2() {
        return pathImagen2;
    }
    /**
     * @param nombreArchivo El nombreArchivo a establecer.
     */
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
   /**
     * @param to El to a establecer.
     */
    public void setTo(Map to) {
        this.to = to;
    }
    /**
     * @param cc El cc a establecer.
     */
    public void setCc(Map cc) {
        this.cc = cc;
    }
    /**
     * @param cco El cco a establecer.
     */
    public void setCco(Map cco) {
        this.cco = cco;
    }
    /**
     * @param from El from a establecer.
     */
    public void setFrom(String from) {
        this.from = from;
    }
    /**
     * @param horarios El horarios a establecer.
     */
    public void setHorarios(Map horarios) {
        this.horarios = horarios;
    }
    /**
     * @param intervalo El intervalo a establecer.
     */
    public void setIntervalo(String intervalo) {
        this.intervalo = intervalo;
    }
    /**
     * @param mensaje El mensaje a establecer.
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    /**
     * @param numReintentos El numReintentos a establecer.
     */
    public void setNumReintentos(String numReintentos) {
        this.numReintentos = numReintentos;
    }
    /**
     * @param puerto El puerto a establecer.
     */
    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }
    /**
     * @param server El server a establecer.
     */
    public void setServer(String server) {
        this.server = server;
    }
    /**
     * @param subject El subject a establecer.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
    /**
     * @param tipo El tipo a establecer.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    /**
     * @param pathImagen1 El pathImagen1 a establecer.
     */
    public void setPathImagen1(String pathImagen1) {
        this.pathImagen1 = pathImagen1;
    }
    /**
     * @param pathImagen2 El pathImagen2 a establecer.
     */
    public void setPathImagen2(String pathImagen2) {
        this.pathImagen2 = pathImagen2;
    }
    /**
     * @return Devuelve eliminarArchivosTemporales.
     */
    public String getEliminarArchivosTemporales() {
        return EliminarArchivosTemporales;
    }
    /**
     * @param eliminarArchivosTemporales El eliminarArchivosTemporales a establecer.
     */
    public void setEliminarArchivosTemporales(String eliminarArchivosTemporales) {
        EliminarArchivosTemporales = eliminarArchivosTemporales;
    }
}
