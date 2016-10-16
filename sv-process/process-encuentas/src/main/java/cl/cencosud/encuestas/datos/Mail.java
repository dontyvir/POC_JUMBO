/*
 * Creado el 23-mar-2010
 *
 */
package cl.cencosud.encuestas.datos;

/**
 * @author imoyano
 *
 */
public class Mail {
    
    long idPedido;
    String remitente;
    String destinatario;
    String titulo;
    String contenido;
    
    public Mail(long idPedido, String remitente, String destinatario, String titulo, String contenido) {
        this.idPedido = idPedido;
        this.remitente  = remitente;
        this.destinatario = destinatario;
        this.titulo     = titulo;
        this.contenido  = contenido;
    }

    /**
     * @return Devuelve contenido.
     */
    public String getContenido() {
        return contenido;
    }
    /**
     * @return Devuelve destinatario.
     */
    public String getDestinatario() {
        return destinatario;
    }
    /**
     * @return Devuelve remitente.
     */
    public String getRemitente() {
        return remitente;
    }
    /**
     * @return Devuelve titulo.
     */
    public String getTitulo() {
        return titulo;
    }
    /**
     * @param contenido El contenido a establecer.
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    /**
     * @param destinatario El destinatario a establecer.
     */
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
    /**
     * @param remitente El remitente a establecer.
     */
    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }
    /**
     * @param titulo El titulo a establecer.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    /**
     * @return Devuelve idPedido.
     */
    public long getIdPedido() {
        return idPedido;
    }
    /**
     * @param idPedido El idPedido a establecer.
     */
    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
    }
}
