/*
 * Creado el 03-10-2007
 *
 */
package cl.bbr.jumbocl.pedidos.dto;

/**
 * @author rbelmar
 *
 */
public class PedidoDesp {
				
    private String fecha;
    private String origen;
    private String local;
    private String comuna;
    private String zona;
    private String horario;
    private String estado;
    private String op;
    private String tipo_despacho;
    private String cant_bins;
    private long   pos_monto;
    private String pos_fecha;
    private String pos_hora;
    private String primera_compra;
    private String medio_pago;
    private String nom_cliente;
    private String eMail;
    private String direccion;
    private String telefonos;
    private String tipo_tarjeta;

    
    /**
     * 
     */
    public PedidoDesp() {
        super();
    }

    /**
     * @return Devuelve comuna.
     */
    public String getComuna() {
        return comuna;
    }
    /**
     * @return Devuelve direccion.
     */
    public String getDireccion() {
        return direccion;
    }
    /**
     * @return Devuelve estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @return Devuelve fecha.
     */
    public String getFecha() {
        return fecha;
    }
    /**
     * @return Devuelve horario.
     */
    public String getHorario() {
        return horario;
    }
    /**
     * @return Devuelve local.
     */
    public String getLocal() {
        return local;
    }
    /**
     * @return Devuelve medio_pago.
     */
    public String getMedio_pago() {
        return medio_pago;
    }
    /**
     * @return Devuelve nom_cliente.
     */
    public String getNom_cliente() {
        return nom_cliente;
    }
    /**
     * @return Devuelve op.
     */
    public String getOp() {
        return op;
    }
    /**
     * @return Devuelve origen.
     */
    public String getOrigen() {
        return origen;
    }
    /**
     * @return Devuelve primera_compra.
     */
    public String getPrimera_compra() {
        return primera_compra;
    }
    /**
     * @return Devuelve telefonos.
     */
    public String getTelefonos() {
        return telefonos;
    }
    /**
     * @return Devuelve tipo_tarjeta.
     */
    public String getTipo_tarjeta() {
        return tipo_tarjeta;
    }
    /**
     * @return Devuelve zona.
     */
    public String getZona() {
        return zona;
    }
    /**
     * @param comuna El comuna a establecer.
     */
    public void setComuna(String comuna) {
        this.comuna = comuna;
    }
    /**
     * @param direccion El direccion a establecer.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    /**
     * @param estado El estado a establecer.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    /**
     * @param fecha El fecha a establecer.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    /**
     * @param horario El horario a establecer.
     */
    public void setHorario(String horario) {
        this.horario = horario;
    }
    /**
     * @param local El local a establecer.
     */
    public void setLocal(String local) {
        this.local = local;
    }
    /**
     * @param medio_pago El medio_pago a establecer.
     */
    public void setMedio_pago(String medio_pago) {
        this.medio_pago = medio_pago;
    }
    /**
     * @param nom_cliente El nom_cliente a establecer.
     */
    public void setNom_cliente(String nom_cliente) {
        this.nom_cliente = nom_cliente;
    }
    /**
     * @param op El op a establecer.
     */
    public void setOp(String op) {
        this.op = op;
    }
    /**
     * @param origen El origen a establecer.
     */
    public void setOrigen(String origen) {
        this.origen = origen;
    }
    /**
     * @param primera_compra El primera_compra a establecer.
     */
    public void setPrimera_compra(String primera_compra) {
        this.primera_compra = primera_compra;
    }
    /**
     * @param telefonos El telefonos a establecer.
     */
    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }
    /**
     * @param tipo_tarjeta El tipo_tarjeta a establecer.
     */
    public void setTipo_tarjeta(String tipo_tarjeta) {
        this.tipo_tarjeta = tipo_tarjeta;
    }
    /**
     * @param zona El zona a establecer.
     */
    public void setZona(String zona) {
        this.zona = zona;
    }
    /**
     * @return Devuelve pos_fecha.
     */
    public String getPos_fecha() {
        return pos_fecha;
    }
    /**
     * @return Devuelve pos_monto.
     */
    public long getPos_monto() {
        return pos_monto;
    }
    /**
     * @param pos_fecha El pos_fecha a establecer.
     */
    public void setPos_fecha(String pos_fecha) {
        this.pos_fecha = pos_fecha;
    }
    /**
     * @param pos_monto El pos_monto a establecer.
     */
    public void setPos_monto(long pos_monto) {
        this.pos_monto = pos_monto;
    }
    /**
     * @return Devuelve cant_bins.
     */
    public String getCant_bins() {
        return cant_bins;
    }
    /**
     * @return Devuelve eMail.
     */
    public String getEMail() {
        return eMail;
    }
    /**
     * @return Devuelve pos_hora.
     */
    public String getPos_hora() {
        return pos_hora;
    }
    /**
     * @return Devuelve tipo_despacho.
     */
    public String getTipo_despacho() {
        return tipo_despacho;
    }
    /**
     * @param cant_bins El cant_bins a establecer.
     */
    public void setCant_bins(String cant_bins) {
        this.cant_bins = cant_bins;
    }
    /**
     * @param mail El eMail a establecer.
     */
    public void setEMail(String mail) {
        eMail = mail;
    }
    /**
     * @param pos_hora El pos_hora a establecer.
     */
    public void setPos_hora(String pos_hora) {
        this.pos_hora = pos_hora;
    }
    /**
     * @param tipo_despacho El tipo_despacho a establecer.
     */
    public void setTipo_despacho(String tipo_despacho) {
        this.tipo_despacho = tipo_despacho;
    }
}
