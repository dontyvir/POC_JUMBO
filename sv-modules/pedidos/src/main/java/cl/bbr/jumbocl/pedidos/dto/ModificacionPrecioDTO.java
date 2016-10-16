package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class ModificacionPrecioDTO implements Serializable {

    private String fechaHora;
    private String usuario;
    private long idPedido;
    private long idDetPicking;
    private String producto;
    private double precioOriginal;
    private double precioNuevo;
    private double montoReservaOp;
    private double montoOriginalOp;
    private double montoNuevoOp;
    private double despacho;
    

    /**
     * @return Devuelve fechaHora.
     */
    public String getFechaHora() {
        return fechaHora;
    }
    /**
     * @return Devuelve idJPicking.
     */
    public long getIdDetPicking() {
        return idDetPicking;
    }
    /**
     * @return Devuelve idPedido.
     */
    public long getIdPedido() {
        return idPedido;
    }
    /**
     * @return Devuelve montoNuevoOp.
     */
    public double getMontoNuevoOp() {
        return montoNuevoOp;
    }
    /**
     * @return Devuelve montoOriginalOp.
     */
    public double getMontoOriginalOp() {
        return montoOriginalOp;
    }
    /**
     * @return Devuelve montoReservaOp.
     */
    public double getMontoReservaOp() {
        return montoReservaOp;
    }
    /**
     * @return Devuelve precioNuevo.
     */
    public double getPrecioNuevo() {
        return precioNuevo;
    }
    /**
     * @return Devuelve precioOriginal.
     */
    public double getPrecioOriginal() {
        return precioOriginal;
    }
    /**
     * @return Devuelve producto.
     */
    public String getProducto() {
        return producto;
    }
    /**
     * @return Devuelve usuario.
     */
    public String getUsuario() {
        return usuario;
    }
    /**
     * @param fechaHora El fechaHora a establecer.
     */
    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }
    /**
     * @param idJPicking El idJPicking a establecer.
     */
    public void setIdDetPicking(long idDetPicking) {
        this.idDetPicking = idDetPicking;
    }
    /**
     * @param idPedido El idPedido a establecer.
     */
    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
    }
    /**
     * @param montoNuevoOp El montoNuevoOp a establecer.
     */
    public void setMontoNuevoOp(double montoNuevoOp) {
        this.montoNuevoOp = montoNuevoOp;
    }
    /**
     * @param montoOriginalOp El montoOriginalOp a establecer.
     */
    public void setMontoOriginalOp(double montoOriginalOp) {
        this.montoOriginalOp = montoOriginalOp;
    }
    /**
     * @param montoReservaOp El montoReservaOp a establecer.
     */
    public void setMontoReservaOp(double montoReservaOp) {
        this.montoReservaOp = montoReservaOp;
    }
    /**
     * @param precioNuevo El precioNuevo a establecer.
     */
    public void setPrecioNuevo(double precioNuevo) {
        this.precioNuevo = precioNuevo;
    }
    /**
     * @param precioOriginal El precioOriginal a establecer.
     */
    public void setPrecioOriginal(double precioOriginal) {
        this.precioOriginal = precioOriginal;
    }
    /**
     * @param producto El producto a establecer.
     */
    public void setProducto(String producto) {
        this.producto = producto;
    }
    /**
     * @param usuario El usuario a establecer.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    /**
     * @return Devuelve despacho.
     */
    public double getDespacho() {
        return despacho;
    }
    /**
     * @param despacho El despacho a establecer.
     */
    public void setDespacho(double despacho) {
        this.despacho = despacho;
    }
}
