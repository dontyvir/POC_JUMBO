package cl.bbr.jumbocl.casos.dto;

import java.io.Serializable;

import cl.bbr.jumbocl.common.dto.ObjetoDTO;

public class CasoDTO implements Serializable {
    
	private long idCaso;
	private String fcCreacionCaso;
	private String fcModificacionCaso;
	private long idPedido;
	private String fcPedido;
	private String direccion;
	private String comuna;
	private ObjetoDTO local;
	private String fcDespacho;
	private long cliRut;	
	private String cliDv;
	private String cliNombre;
	private String cliFonoCod1;
	private String cliFonoNum1;
	private String cliFonoCod2;
	private String cliFonoNum2;
	private String cliFonoCod3;
	private String cliFonoNum3;
	private String fcCompromisoSolucion;//fcResolucion;
	private long idUsuFonoCompra;
	private String usuFonoCompraNombre;
	private String canalCompra; // Si idUsuFonoCompra existe es 'F'ono Compra, de lo contrario es vía 'W'eb
	private JornadaDTO jornada;
	private EstadoCasoDTO estado;
	private String alarma;	//  V: Vencida - Rojo - FF0000 ;  P: Por vencer - Naranjo -  FF9933 ; N: Normal - Negro - 000000
	private long idUsuarioEdit;
	private long idUsuarioCreador;
	private String editUsuario;
	private String creadorUsuario;
	private String indicaciones;
	private String fcCasoVerificado; // fcResolucionFinal;
	private String satisfaccionCliente;
	private String fcHoraEntrega;
	private boolean escalamiento;
    private String comentarioCallCenter;
	
	public CasoDTO() {
	    this.idCaso = 0;
	    this.fcCreacionCaso = "";
	    this.fcModificacionCaso = "";
	    this.idPedido = 0;
	    this.fcPedido = "";
	    this.direccion = "";
	    this.comuna = "";
	    this.local = new ObjetoDTO();
	    this.fcDespacho = "";
	    this.cliRut = 0;	
	    this.cliDv = "";
	    this.cliNombre = "";
	    this.cliFonoCod1 = "";
	    this.cliFonoNum1 = "";
	    this.cliFonoCod2 = "";
	    this.cliFonoNum2 = "";
	    this.cliFonoCod3 = "";
	    this.cliFonoNum3 = "";
	    this.fcCompromisoSolucion = "";
	    this.jornada = new JornadaDTO();
	    this.estado = new EstadoCasoDTO();
	    this.alarma = "";	
	    this.editUsuario = "";
	    this.creadorUsuario = "";
	    this.indicaciones = "";
	    this.idUsuarioEdit = 0;
	    this.idUsuarioCreador = 0;
	    this.idUsuFonoCompra = 0;
	    this.usuFonoCompraNombre = "";
	    this.canalCompra = "";
	    this.fcCasoVerificado = "";
	    this.satisfaccionCliente = "";
	    this.fcHoraEntrega = "";
	    this.escalamiento = false;
        this.comentarioCallCenter = "";
	}
	
	
	public String getDescripcionCanalCompra() {
	
	    if (this.canalCompra.equalsIgnoreCase("W")) {
	        return "Web";
	    }
	    if (this.canalCompra.equalsIgnoreCase("F")) {
	        return "Fono Compra - " + this.usuFonoCompraNombre ;
	    }
	    return "";
	}
	
    /**
     * @return Devuelve cliDv.
     */
    public String getCliDv() {
        return cliDv;
    }
    /**
     * @return Devuelve cliFonoCod1.
     */
    public String getCliFonoCod1() {
        return cliFonoCod1;
    }
    /**
     * @return Devuelve cliFonoCod2.
     */
    public String getCliFonoCod2() {
        return cliFonoCod2;
    }
    /**
     * @return Devuelve cliFonoCod3.
     */
    public String getCliFonoCod3() {
        return cliFonoCod3;
    }
    /**
     * @return Devuelve cliFonoNum1.
     */
    public String getCliFonoNum1() {
        return cliFonoNum1;
    }
    /**
     * @return Devuelve cliFonoNum2.
     */
    public String getCliFonoNum2() {
        return cliFonoNum2;
    }
    /**
     * @return Devuelve cliFonoNum3.
     */
    public String getCliFonoNum3() {
        return cliFonoNum3;
    }
    /**
     * @return Devuelve cliNombre.
     */
    public String getCliNombre() {
        return cliNombre;
    }
    /**
     * @return Devuelve cliRut.
     */
    public long getCliRut() {
        return cliRut;
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
     * @return Devuelve fcCreacionCaso.
     */
    public String getFcCreacionCaso() {
        return fcCreacionCaso;
    }
    /**
     * @return Devuelve fcDespacho.
     */
    public String getFcDespacho() {
        return fcDespacho;
    }
    /**
     * @return Devuelve fcModificacionCaso.
     */
    public String getFcModificacionCaso() {
        return fcModificacionCaso;
    }
    /**
     * @return Devuelve fcPedido.
     */
    public String getFcPedido() {
        return fcPedido;
    }
    /**
     * @return Devuelve idCaso.
     */
    public long getIdCaso() {
        return idCaso;
    }

    /**
     * @return Devuelve idPedido.
     */
    public long getIdPedido() {
        return idPedido;
    }
    
    /**
     * @param cliDv El cliDv a establecer.
     */
    public void setCliDv(String cliDv) {
        this.cliDv = cliDv;
    }
    /**
     * @param cliFonoCod1 El cliFonoCod1 a establecer.
     */
    public void setCliFonoCod1(String cliFonoCod1) {
        this.cliFonoCod1 = cliFonoCod1;
    }
    /**
     * @param cliFonoCod2 El cliFonoCod2 a establecer.
     */
    public void setCliFonoCod2(String cliFonoCod2) {
        this.cliFonoCod2 = cliFonoCod2;
    }
    /**
     * @param cliFonoCod3 El cliFonoCod3 a establecer.
     */
    public void setCliFonoCod3(String cliFonoCod3) {
        this.cliFonoCod3 = cliFonoCod3;
    }
    /**
     * @param cliFonoNum1 El cliFonoNum1 a establecer.
     */
    public void setCliFonoNum1(String cliFonoNum1) {
        this.cliFonoNum1 = cliFonoNum1;
    }
    /**
     * @param cliFonoNum2 El cliFonoNum2 a establecer.
     */
    public void setCliFonoNum2(String cliFonoNum2) {
        this.cliFonoNum2 = cliFonoNum2;
    }
    /**
     * @param cliFonoNum3 El cliFonoNum3 a establecer.
     */
    public void setCliFonoNum3(String cliFonoNum3) {
        this.cliFonoNum3 = cliFonoNum3;
    }
    /**
     * @param cliNombre El cliNombre a establecer.
     */
    public void setCliNombre(String cliNombre) {
        this.cliNombre = cliNombre;
    }
    /**
     * @param cliRut El cliRut a establecer.
     */
    public void setCliRut(long cliRut) {
        this.cliRut = cliRut;
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
     * @param fcCreacionCaso El fcCreacionCaso a establecer.
     */
    public void setFcCreacionCaso(String fcCreacionCaso) {
        this.fcCreacionCaso = fcCreacionCaso;
    }
    /**
     * @param fcDespacho El fcDespacho a establecer.
     */
    public void setFcDespacho(String fcDespacho) {
        this.fcDespacho = fcDespacho;
    }
    /**
     * @param fcModificacionCaso El fcModificacionCaso a establecer.
     */
    public void setFcModificacionCaso(String fcModificacionCaso) {
        this.fcModificacionCaso = fcModificacionCaso;
    }
    /**
     * @param fcPedido El fcPedido a establecer.
     */
    public void setFcPedido(String fcPedido) {
        this.fcPedido = fcPedido;
    }
    /**
     * @param idCaso El idCaso a establecer.
     */
    public void setIdCaso(long idCaso) {
        this.idCaso = idCaso;
    }
    /**
     * @param idPedido El idPedido a establecer.
     */
    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
    }
    
    /**
     * @return Devuelve estado.
     */
    public EstadoCasoDTO getEstado() {
        return estado;
    }
    /**
     * @return Devuelve jornada.
     */
    public JornadaDTO getJornada() {
        return jornada;
    }
    /**
     * @param estado El estado a establecer.
     */
    public void setEstado(EstadoCasoDTO estado) {
        this.estado = estado;
    }
    /**
     * @param jornada El jornada a establecer.
     */
    public void setJornada(JornadaDTO jornada) {
        this.jornada = jornada;
    }
    /**
     * @return Devuelve editUsuario.
     */
    public String getEditUsuario() {
        return editUsuario;
    }
    /**
     * @param editUsuario El editUsuario a establecer.
     */
    public void setEditUsuario(String editUsuario) {
        this.editUsuario = editUsuario;
    }
    /**
     * @return Devuelve alarma.
     */
    public String getAlarma() {
        return alarma;
    }
    /**
     * @param alarma El alarma a establecer.
     */
    public void setAlarma(String alarma) {
        this.alarma = alarma;
    }
    
    /**
     * @return Devuelve local.
     */
    public ObjetoDTO getLocal() {
        return local;
    }
    /**
     * @param local El local a establecer.
     */
    public void setLocal(ObjetoDTO local) {
        this.local = local;
    }
    /**
     * @return Devuelve indicaciones.
     */
    public String getIndicaciones() {
        return indicaciones;
    }
    /**
     * @param indicaciones El indicaciones a establecer.
     */
    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }
    /**
     * @return Devuelve idUsuarioEdit.
     */
    public long getIdUsuarioEdit() {
        return idUsuarioEdit;
    }
    /**
     * @param idUsuarioEdit El idUsuarioEdit a establecer.
     */
    public void setIdUsuarioEdit(long idUsuarioEdit) {
        this.idUsuarioEdit = idUsuarioEdit;
    }
    /**
     * @return Devuelve canalCompra.
     */
    public String getCanalCompra() {
        return canalCompra;
    }
    /**
     * @return Devuelve idUsuFonoCompra.
     */
    public long getIdUsuFonoCompra() {
        return idUsuFonoCompra;
    }
    /**
     * @return Devuelve usuFonoCompraNombre.
     */
    public String getUsuFonoCompraNombre() {
        return usuFonoCompraNombre;
    }
    /**
     * @param canalCompra El canalCompra a establecer.
     */
    public void setCanalCompra(String canalCompra) {
        this.canalCompra = canalCompra;
    }
    /**
     * @param idUsuFonoCompra El idUsuFonoCompra a establecer.
     */
    public void setIdUsuFonoCompra(long idUsuFonoCompra) {
        this.idUsuFonoCompra = idUsuFonoCompra;
    }
    /**
     * @param usuFonoCompraNombre El usuFonoCompraNombre a establecer.
     */
    public void setUsuFonoCompraNombre(String usuFonoCompraNombre) {
        this.usuFonoCompraNombre = usuFonoCompraNombre;
    }
    /**
     * @return Devuelve satisfaccionCliente.
     */
    public String getSatisfaccionCliente() {
       return satisfaccionCliente;
    }
    /**
     * @param satisfaccionCliente El satisfaccionCliente a establecer.
     */
    public void setSatisfaccionCliente(String satisfaccionCliente) {
        this.satisfaccionCliente = satisfaccionCliente;
    }
    /**
     * @return Devuelve fcCasoVerificado.
     */
    public String getFcCasoVerificado() {
        return fcCasoVerificado;
    }
    /**
     * @param fcCasoVerificado El fcCasoVerificado a establecer.
     */
    public void setFcCasoVerificado(String fcCasoVerificado) {
        this.fcCasoVerificado = fcCasoVerificado;
    }
    /**
     * @return Devuelve fcCompromisoSolucion.
     */
    public String getFcCompromisoSolucion() {
        return fcCompromisoSolucion;
    }
    /**
     * @param fcCompromisoSolucion El fcCompromisoSolucion a establecer.
     */
    public void setFcCompromisoSolucion(String fcCompromisoSolucion) {
        this.fcCompromisoSolucion = fcCompromisoSolucion;
    }
    /**
     * @return Devuelve fcHoraEntrega.
     */
    public String getFcHoraEntrega() {
        return fcHoraEntrega;
    }
    /**
     * @param fcHoraEntrega El fcHoraEntrega a establecer.
     */
    public void setFcHoraEntrega(String fcHoraEntrega) {
        this.fcHoraEntrega = fcHoraEntrega;
    }
    /**
     * @return Devuelve escalamiento.
     */
    public boolean isEscalamiento() {
        return escalamiento;
    }
    /**
     * @param escalamiento El escalamiento a establecer.
     */
    public void setEscalamiento(boolean escalamiento) {
        this.escalamiento = escalamiento;
    }
    /**
     * @return Devuelve idUsuarioCreador.
     */
    public long getIdUsuarioCreador() {
        return idUsuarioCreador;
    }
    /**
     * @param idUsuarioCreador El idUsuarioCreador a establecer.
     */
    public void setIdUsuarioCreador(long idUsuarioCreador) {
        this.idUsuarioCreador = idUsuarioCreador;
    }
    /**
     * @return Devuelve creadorUsuario.
     */
    public String getCreadorUsuario() {
        return creadorUsuario;
    }
    /**
     * @param creadorUsuario El creadorUsuario a establecer.
     */
    public void setCreadorUsuario(String creadorUsuario) {
        this.creadorUsuario = creadorUsuario;
    }
    /**
     * @return Devuelve comentarioCallCenter.
     */
    public String getComentarioCallCenter() {
        return comentarioCallCenter;
    }
    /**
     * @param comentarioCallCenter El comentarioCallCenter a establecer.
     */
    public void setComentarioCallCenter(String comentarioCallCenter) {
        this.comentarioCallCenter = comentarioCallCenter;
    }
}
