/*
 * Creado el 03-10-2007
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.cencosud.jumbo.beans;


/**
 * @author rbelmar
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class PedidoDesp {
				
    private String fecha;
    private String id_ruta;
    private String id_pedido;
    private String jumbo_va;
    private String horario;
    private String tipo_despacho;
    private String monto;
    private String pos_fecha;
    private String pos_hora;
    private String cant_bins;
    private String salida_camion;
    private String llegada_dom;
    private String salida_dom;
    private String pto_a_pto;
    private String tiempo_entrega;
    private String nombre_chofer;
    private String patente;
    private String estatus_entrega;
    private String resp_incumplimiento;
    private String motivo_incumplimiento;
    private String nom_cliente;
    private String direccion;
    private String comuna;
    private String telefonos;
    private String confirmacion;
    private String reprogramacion;
    private String num_reprogramaciones;
    private String nombre_responsable;
    /*01.-FECHA
    02.-ID_RUTA
    03.-ID_PEDIDO
    04.-JUMBO_VA
    05.-HORARIO
    06.-TIPO_DESPACHO
    07.-MONTO
    08.-POS_FECHA
    09.-POS_HORA
    10.-CANT_BINS
    11.-SALIDA_CAMION
    12.-LLEGADA_DOM
    13.-SALIDA_DOM
    14.-TIEMPO_ENTREGA
    15.-NOMBRE_CHOFER
    16.-PATENTE
    17.-ESTATUS_ENTREGA
    18.-RESP_INCUMPLIMIENTO
    19.-MOTIVO_INCUMPLIMIENTO
    20.-NOM_CLIENTE
    21.-DIRECCION
    22.-COMUNA
    23.-TELEFONOS
    24.-CONFIRMACION
    25.-REPROGRAMACION
    26.-NUM_REPROGRAMACIONES
    27.-NOMBRE_RESPONSABLE*/

    
    /**
     * 
     */
    public PedidoDesp() {
        super();
        // TODO Apéndice de constructor generado automáticamente
    }

    /**
     * @return Devuelve cant_bins.
     */
    public String getCant_bins() {
        return cant_bins;
    }
    /**
     * @return Devuelve comuna.
     */
    public String getComuna() {
        return comuna;
    }
    /**
     * @return Devuelve confirmacion.
     */
    public String getConfirmacion() {
        return confirmacion;
    }
    /**
     * @return Devuelve direccion.
     */
    public String getDireccion() {
        return direccion;
    }
    /**
     * @return Devuelve estatus_entrega.
     */
    public String getEstatus_entrega() {
        return estatus_entrega;
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
     * @return Devuelve id_pedido.
     */
    public String getId_pedido() {
        return id_pedido;
    }
    /**
     * @return Devuelve id_ruta.
     */
    public String getId_ruta() {
        return id_ruta;
    }
    /**
     * @return Devuelve jumbo_va.
     */
    public String getJumbo_va() {
        return jumbo_va;
    }
    /**
     * @return Devuelve llegada_dom.
     */
    public String getLlegada_dom() {
        return llegada_dom;
    }
    /**
     * @return Devuelve monto.
     */
    public String getMonto() {
        return monto;
    }
    /**
     * @return Devuelve motivo_incumplimiento.
     */
    public String getMotivo_incumplimiento() {
        return motivo_incumplimiento;
    }
    /**
     * @return Devuelve nom_cliente.
     */
    public String getNom_cliente() {
        return nom_cliente;
    }
    /**
     * @return Devuelve nombre_chofer.
     */
    public String getNombre_chofer() {
        return nombre_chofer;
    }
    /**
     * @return Devuelve nombre_responsable.
     */
    public String getNombre_responsable() {
        return nombre_responsable;
    }
    /**
     * @return Devuelve num_reprogramaciones.
     */
    public String getNum_reprogramaciones() {
        return num_reprogramaciones;
    }
    /**
     * @return Devuelve patente.
     */
    public String getPatente() {
        return patente;
    }
    /**
     * @return Devuelve pos_fecha.
     */
    public String getPos_fecha() {
        return pos_fecha;
    }
    /**
     * @return Devuelve pos_hora.
     */
    public String getPos_hora() {
        return pos_hora;
    }
    /**
     * @return Devuelve pto_a_pto.
     */
    public String getPto_a_pto() {
        return pto_a_pto;
    }
    /**
     * @return Devuelve reprogramacion.
     */
    public String getReprogramacion() {
        return reprogramacion;
    }
    /**
     * @return Devuelve resp_incumplimiento.
     */
    public String getResp_incumplimiento() {
        return resp_incumplimiento;
    }
    /**
     * @return Devuelve salida_camion.
     */
    public String getSalida_camion() {
        return salida_camion;
    }
    /**
     * @return Devuelve salida_dom.
     */
    public String getSalida_dom() {
        return salida_dom;
    }
    /**
     * @return Devuelve telefonos.
     */
    public String getTelefonos() {
        return telefonos;
    }
    /**
     * @return Devuelve tiempo_entrega.
     */
    public String getTiempo_entrega() {
        return tiempo_entrega;
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
     * @param comuna El comuna a establecer.
     */
    public void setComuna(String comuna) {
        this.comuna = comuna;
    }
    /**
     * @param confirmacion El confirmacion a establecer.
     */
    public void setConfirmacion(String confirmacion) {
        this.confirmacion = confirmacion;
    }
    /**
     * @param direccion El direccion a establecer.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    /**
     * @param estatus_entrega El estatus_entrega a establecer.
     */
    public void setEstatus_entrega(String estatus_entrega) {
        this.estatus_entrega = estatus_entrega;
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
     * @param id_pedido El id_pedido a establecer.
     */
    public void setId_pedido(String id_pedido) {
        this.id_pedido = id_pedido;
    }
    /**
     * @param id_ruta El id_ruta a establecer.
     */
    public void setId_ruta(String id_ruta) {
        this.id_ruta = id_ruta;
    }
    /**
     * @param jumbo_va El jumbo_va a establecer.
     */
    public void setJumbo_va(String jumbo_va) {
        this.jumbo_va = jumbo_va;
    }
    /**
     * @param llegada_dom El llegada_dom a establecer.
     */
    public void setLlegada_dom(String llegada_dom) {
        this.llegada_dom = llegada_dom;
    }
    /**
     * @param monto El monto a establecer.
     */
    public void setMonto(String monto) {
        this.monto = monto;
    }
    /**
     * @param motivo_incumplimiento El motivo_incumplimiento a establecer.
     */
    public void setMotivo_incumplimiento(String motivo_incumplimiento) {
        this.motivo_incumplimiento = motivo_incumplimiento;
    }
    /**
     * @param nom_cliente El nom_cliente a establecer.
     */
    public void setNom_cliente(String nom_cliente) {
        this.nom_cliente = nom_cliente;
    }
    /**
     * @param nombre_chofer El nombre_chofer a establecer.
     */
    public void setNombre_chofer(String nombre_chofer) {
        this.nombre_chofer = nombre_chofer;
    }
    /**
     * @param nombre_responsable El nombre_responsable a establecer.
     */
    public void setNombre_responsable(String nombre_responsable) {
        this.nombre_responsable = nombre_responsable;
    }
    /**
     * @param num_reprogramaciones El num_reprogramaciones a establecer.
     */
    public void setNum_reprogramaciones(String num_reprogramaciones) {
        this.num_reprogramaciones = num_reprogramaciones;
    }
    /**
     * @param patente El patente a establecer.
     */
    public void setPatente(String patente) {
        this.patente = patente;
    }
    /**
     * @param pos_fecha El pos_fecha a establecer.
     */
    public void setPos_fecha(String pos_fecha) {
        this.pos_fecha = pos_fecha;
    }
    /**
     * @param pos_hora El pos_hora a establecer.
     */
    public void setPos_hora(String pos_hora) {
        this.pos_hora = pos_hora;
    }
    /**
     * @param pto_a_pto El pto_a_pto a establecer.
     */
    public void setPto_a_pto(String pto_a_pto) {
        this.pto_a_pto = pto_a_pto;
    }
    /**
     * @param reprogramacion El reprogramacion a establecer.
     */
    public void setReprogramacion(String reprogramacion) {
        this.reprogramacion = reprogramacion;
    }
    /**
     * @param resp_incumplimiento El resp_incumplimiento a establecer.
     */
    public void setResp_incumplimiento(String resp_incumplimiento) {
        this.resp_incumplimiento = resp_incumplimiento;
    }
    /**
     * @param salida_camion El salida_camion a establecer.
     */
    public void setSalida_camion(String salida_camion) {
        this.salida_camion = salida_camion;
    }
    /**
     * @param salida_dom El salida_dom a establecer.
     */
    public void setSalida_dom(String salida_dom) {
        this.salida_dom = salida_dom;
    }
    /**
     * @param telefonos El telefonos a establecer.
     */
    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }
    /**
     * @param tiempo_entrega El tiempo_entrega a establecer.
     */
    public void setTiempo_entrega(String tiempo_entrega) {
        this.tiempo_entrega = tiempo_entrega;
    }
    /**
     * @param tipo_despacho El tipo_despacho a establecer.
     */
    public void setTipo_despacho(String tipo_despacho) {
        this.tipo_despacho = tipo_despacho;
    }
}
