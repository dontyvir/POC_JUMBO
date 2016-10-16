/*
 * Creado el 20-sep-2011
 * 
 */
package cl.cencosud.enviotasa.datos;

/**
 * @author imoyano
 * 
 */
public class Pedido {
    
    long idPedido;
    double valorCompraContado; //valor capturado
    String tasaInteres;
    String cuotas;
    double valorCuota;
    double montoTotalConCuotas;
    String fechaPrimerVencimiento;
    
    String mailCliente;
    String nombreCliente;
    long rutCliente;
    String dvCliente;
    String fechaCompra;
    String fechaCaptura;
    String tarjeta;
    double montoCaptura;
    boolean existeEnBD;
    
    String local;
    String rutClienteMas;
    
    
    public Pedido() {
        
    }

    /**
     * @return Devuelve cuotas.
     */
    public String getCuotas() {
        return cuotas;
    }
    /**
     * @return Devuelve fechaPrimerVencimiento.
     */
    public String getFechaPrimerVencimiento() {
        return fechaPrimerVencimiento;
    }
    /**
     * @return Devuelve idPedido.
     */
    public long getIdPedido() {
        return idPedido;
    }
    /**
     * @return Devuelve montoTotalConCuotas.
     */
    public double getMontoTotalConCuotas() {
        return montoTotalConCuotas;
    }
    /**
     * @return Devuelve tasaInteres.
     */
    public String getTasaInteres() {
        return tasaInteres;
    }
    /**
     * @return Devuelve valorCompraContado.
     */
    public double getValorCompraContado() {
        return valorCompraContado;
    }
    /**
     * @return Devuelve valorCuota.
     */
    public double getValorCuota() {
        return valorCuota;
    }
    /**
     * @param cuotas El cuotas a establecer.
     */
    public void setCuotas(String cuotas) {
        this.cuotas = cuotas;
    }
    /**
     * @param fechaPrimerVencimiento El fechaPrimerVencimiento a establecer.
     */
    public void setFechaPrimerVencimiento(String fechaPrimerVencimiento) {
        this.fechaPrimerVencimiento = fechaPrimerVencimiento;
    }
    /**
     * @param idPedido El idPedido a establecer.
     */
    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
    }
    /**
     * @param montoTotalConCuotas El montoTotalConCuotas a establecer.
     */
    public void setMontoTotalConCuotas(double montoTotalConCuotas) {
        this.montoTotalConCuotas = montoTotalConCuotas;
    }
    /**
     * @param tasaInteres El tasaInteres a establecer.
     */
    public void setTasaInteres(String tasaInteres) {
        this.tasaInteres = tasaInteres;
    }
    /**
     * @param valorCompraContado El valorCompraContado a establecer.
     */
    public void setValorCompraContado(double valorCompraContado) {
        this.valorCompraContado = valorCompraContado;
    }
    /**
     * @param valorCuota El valorCuota a establecer.
     */
    public void setValorCuota(double valorCuota) {
        this.valorCuota = valorCuota;
    }
    /**
     * @return Devuelve mailCliente.
     */
    public String getMailCliente() {
        return mailCliente;
    }
    /**
     * @param mailCliente El mailCliente a establecer.
     */
    public void setMailCliente(String mailCliente) {
        this.mailCliente = mailCliente;
    }
    /**
     * @return Devuelve dvCliente.
     */
    public String getDvCliente() {
        return dvCliente;
    }
    /**
     * @return Devuelve fechaCaptura.
     */
    public String getFechaCaptura() {
        return fechaCaptura;
    }
    /**
     * @return Devuelve fechaCompra.
     */
    public String getFechaCompra() {
        return fechaCompra;
    }
    /**
     * @return Devuelve montoCaptura.
     */
    public double getMontoCaptura() {
        return montoCaptura;
    }
    /**
     * @return Devuelve nombreCliente.
     */
    public String getNombreCliente() {
        return nombreCliente;
    }
    /**
     * @return Devuelve rutCliente.
     */
    public long getRutCliente() {
        return rutCliente;
    }
    /**
     * @return Devuelve tarjeta.
     */
    public String getTarjeta() {
        return tarjeta;
    }
    /**
     * @param dvCliente El dvCliente a establecer.
     */
    public void setDvCliente(String dvCliente) {
        this.dvCliente = dvCliente;
    }
    /**
     * @param fechaCaptura El fechaCaptura a establecer.
     */
    public void setFechaCaptura(String fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }
    /**
     * @param fechaCompra El fechaCompra a establecer.
     */
    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
    /**
     * @param montoCaptura El montoCaptura a establecer.
     */
    public void setMontoCaptura(double montoCaptura) {
        this.montoCaptura = montoCaptura;
    }
    /**
     * @param nombreCliente El nombreCliente a establecer.
     */
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
    /**
     * @param rutCliente El rutCliente a establecer.
     */
    public void setRutCliente(long rutCliente) {
        this.rutCliente = rutCliente;
    }
    /**
     * @param tarjeta El tarjeta a establecer.
     */
    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }
    /**
     * @return Devuelve existeEnBD.
     */
    public boolean isExisteEnBD() {
        return existeEnBD;
    }
    /**
     * @param existeEnBD El existeEnBD a establecer.
     */
    public void setExisteEnBD(boolean existeEnBD) {
        this.existeEnBD = existeEnBD;
    }
    /**
     * @return Devuelve local.
     */
    public String getLocal() {
        return local;
    }
    /**
     * @return Devuelve rutClienteMas.
     */
    public String getRutClienteMas() {
        return rutClienteMas;
    }
    /**
     * @param local El local a establecer.
     */
    public void setLocal(String local) {
        this.local = local;
    }
    /**
     * @param rutClienteMas El rutClienteMas a establecer.
     */
    public void setRutClienteMas(String rutClienteMas) {
        this.rutClienteMas = rutClienteMas;
    }
}
