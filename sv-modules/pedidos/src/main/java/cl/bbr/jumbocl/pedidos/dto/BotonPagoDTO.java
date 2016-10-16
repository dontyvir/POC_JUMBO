package cl.bbr.jumbocl.pedidos.dto;

import java.util.Calendar;

/**
 * 
 * @author ctapiat
 * 
 * Estructura de la tabla
 * 
 * id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY
 * 1, CACHE 20), cat_id_pedido INTEGER, cat_monto_pedido INTEGER,
 * cat_despacho_pedido INTEGER, cat_tipo_autorizacion CHAR(1),
 * cat_codigo_autorizacion VARCHAR(50), cat_fecha_autorizacion TIMESTAMP,
 * cat_nro_cuotas INTEGER, cat_monto_cuota INTEGER, cat_monto_operacion INTEGER,
 * CREATED_AT TIMESTAMP DEFAULT CURRENT TIMESTAMP, UPDATED_AT TIMESTAMP DEFAULT
 * CURRENT TIMESTAMP,
 * 
 *  
 */
public class BotonPagoDTO {

    private long id;

    private long idPedido;

    private long idCatPedido;

    private double montoReservado = 0;

    private String tipoAutorizacion;

    private String codigoAutorizacion;

    private Calendar fechaAutorizacion;

    private Integer nroCuotas;

    private Integer montoCuota;

    private Integer montoOperacion;

    private String clienteValidado;

    private String nroTarjeta;

    private String rutCliente;
    
    private String glosaRespuesta;
    
    private String codRespuesta;

    /**
     * @return Devuelve codigoAutorizacion.
     */
    public String getCodigoAutorizacion() {
        return codigoAutorizacion;
    }

    /**
     * @param codigoAutorizacion
     *            El codigoAutorizacion a establecer.
     */
    public void setCodigoAutorizacion(String codigoAutorizacion) {
        this.codigoAutorizacion = codigoAutorizacion;
    }

    /**
     * @return Devuelve fechaAutorizacion.
     */
    public Calendar getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    /**
     * @param fechaAutorizacion
     *            El fechaAutorizacion a establecer.
     */
    public void setFechaAutorizacion(Calendar fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    /**
     * @return Devuelve id.
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     *            El id a establecer.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return Devuelve montoCuota.
     */
    public Integer getMontoCuota() {
        return montoCuota;
    }

    /**
     * @param montoCuota
     *            El montoCuota a establecer.
     */
    public void setMontoCuota(Integer montoCuota) {
        this.montoCuota = montoCuota;
    }

    /**
     * @return Devuelve montoOperacion.
     */
    public Integer getMontoOperacion() {
        return montoOperacion;
    }

    /**
     * @param montoOperacion
     *            El montoOperacion a establecer.
     */
    public void setMontoOperacion(Integer montoOperacion) {
        this.montoOperacion = montoOperacion;
    }

    /**
     * @return Devuelve nroCuotas.
     */
    public Integer getNroCuotas() {
        return nroCuotas;
    }

    /**
     * @param nroCuotas
     *            El nroCuotas a establecer.
     */
    public void setNroCuotas(Integer nroCuotas) {
        this.nroCuotas = nroCuotas;
    }

    /**
     * @return Devuelve tipoAutorizacion.
     */
    public String getTipoAutorizacion() {
        return tipoAutorizacion;
    }

    /**
     * @param tipoAutorizacion
     *            El tipoAutorizacion a establecer.
     */
    public void setTipoAutorizacion(String tipoAutorizacion) {
        this.tipoAutorizacion = tipoAutorizacion;
    }

    /**
     * @return Devuelve id.
     */
    public long getIdPedido() {
        return idPedido;
    }

    /**
     * @param id
     *            El id a establecer.
     */
    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
    }

    /**
     * @return Devuelve monto.
     */
    public double getMontoReservado() {
        return montoReservado;
    }

    /**
     * @param monto
     *            El monto a establecer.
     */
    public void setMontoReservado(double montoReservado) {
        this.montoReservado = montoReservado;
    }

    /**
     * @return Devuelve clienteValidado.
     */
    public String getClienteValidado() {
        return clienteValidado;
    }

    /**
     * @param clienteValidado
     *            El clienteValidado a establecer.
     */
    public void setClienteValidado(String clienteValidado) {
        this.clienteValidado = clienteValidado;
    }

    /**
     * @return Devuelve nroTarjeta.
     */
    public String getNroTarjeta() {
        return nroTarjeta;
    }

    /**
     * @param nroTarjeta
     *            El nroTarjeta a establecer.
     */
    public void setNroTarjeta(String nroTarjeta) {
        this.nroTarjeta = nroTarjeta;
    }

    /**
     * @return Devuelve rutCliente.
     */
    public String getRutCliente() {
        return rutCliente;
    }

    /**
     * @param rutCliente
     *            El rutCliente a establecer.
     */
    public void setRutCliente(String rutCliente) {
        this.rutCliente = rutCliente;
    }
    /**
     * @return Devuelve idCatPedido.
     */
    public long getIdCatPedido() {
        return idCatPedido;
    }
    /**
     * @param idCatPedido El idCatPedido a establecer.
     */
    public void setIdCatPedido(long idCatPedido) {
        this.idCatPedido = idCatPedido;
    }
    /**
     * @return Devuelve codRespuesta.
     */
    public String getCodRespuesta() {
        return codRespuesta;
    }
    /**
     * @return Devuelve glosaRespuesta.
     */
    public String getGlosaRespuesta() {
        return glosaRespuesta;
    }
    /**
     * @param codRespuesta El codRespuesta a establecer.
     */
    public void setCodRespuesta(String codRespuesta) {
        this.codRespuesta = codRespuesta;
    }
    /**
     * @param glosaRespuesta El glosaRespuesta a establecer.
     */
    public void setGlosaRespuesta(String glosaRespuesta) {
        this.glosaRespuesta = glosaRespuesta;
    }
}
