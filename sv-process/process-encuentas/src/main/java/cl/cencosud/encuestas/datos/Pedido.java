/*
 * Creado el 22-mar-2010
 * 
 */
package cl.cencosud.encuestas.datos;

/**
 * @author imoyano
 *
 */
public class Pedido {
    
    long idPedido;
    String nombreCliente;
    String rutCliente;
    String direccionCliente;
    String comunaCliente;
    String fechaHoraEntrega;
    String mesPedido;
    String emailCliente;

    /**
     * @return Devuelve comunaCliente.
     */
    public String getComunaCliente() {
        return comunaCliente;
    }
    /**
     * @return Devuelve direccionCliente.
     */
    public String getDireccionCliente() {
        return direccionCliente;
    }
    /**
     * @return Devuelve fechaHoraEntrega.
     */
    public String getFechaHoraEntrega() {
        return fechaHoraEntrega;
    }
    /**
     * @return Devuelve idPedido.
     */
    public long getIdPedido() {
        return idPedido;
    }
    /**
     * @return Devuelve nombreCliente.
     */
    public String getNombreCliente() {
        return nombreCliente;
    }
    /**
     * @param comunaCliente El comunaCliente a establecer.
     */
    public void setComunaCliente(String comunaCliente) {
        this.comunaCliente = comunaCliente;
    }
    /**
     * @param direccionCliente El direccionCliente a establecer.
     */
    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }
    /**
     * @param fechaHoraEntrega El fechaHoraEntrega a establecer.
     */
    public void setFechaHoraEntrega(String fechaHoraEntrega) {
        this.fechaHoraEntrega = fechaHoraEntrega;
    }
    /**
     * @param idPedido El idPedido a establecer.
     */
    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
    }
    /**
     * @param nombreCliente El nombreCliente a establecer.
     */
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
    /**
     * @return Devuelve mesPedido.
     */
    public String getMesPedido() {
        return mesPedido;
    }
    /**
     * @param mesPedido El mesPedido a establecer.
     */
    public void setMesPedido(String mesPedido) {
        this.mesPedido = mesPedido;
    }
    /**
     * @return Devuelve emailCliente.
     */
    public String getEmailCliente() {
        return emailCliente;
    }
    /**
     * @param emailCliente El emailCliente a establecer.
     */
    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }
    /**
     * @return Devuelve rutCliente.
     */
    public String getRutCliente() {
        return rutCliente;
    }
    /**
     * @param rutCliente El rutCliente a establecer.
     */
    public void setRutCliente(String rutCliente) {
        this.rutCliente = rutCliente;
    }
}
