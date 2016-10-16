package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

import cl.bbr.jumbocl.common.dto.ObjetoDTO;

public class ReprogramacionDTO implements Serializable {
    
    private long idPedido;
    private ObjetoDTO motivoReprogramacion;
    private ObjetoDTO responsableReprogramacion;
    private String fechaReprogramacion;
    private JornadaDTO jornadaDespachoAnterior;
    private String usuario;
    
    

    /**
     * @return Devuelve fechaReprogramacion.
     */
    public String getFechaReprogramacion() {
        return fechaReprogramacion;
    }
    /**
     * @return Devuelve idPedido.
     */
    public long getIdPedido() {
        return idPedido;
    }
    /**
     * @return Devuelve jornadaDespachoAnterior.
     */
    public JornadaDTO getJornadaDespachoAnterior() {
        return jornadaDespachoAnterior;
    }
    /**
     * @return Devuelve motivoReprogramacion.
     */
    public ObjetoDTO getMotivoReprogramacion() {
        return motivoReprogramacion;
    }
    /**
     * @return Devuelve responsableReprogramacion.
     */
    public ObjetoDTO getResponsableReprogramacion() {
        return responsableReprogramacion;
    }
    /**
     * @return Devuelve usuario.
     */
    public String getUsuario() {
        return usuario;
    }
    /**
     * @param fechaReprogramacion El fechaReprogramacion a establecer.
     */
    public void setFechaReprogramacion(String fechaReprogramacion) {
        this.fechaReprogramacion = fechaReprogramacion;
    }
    /**
     * @param idPedido El idPedido a establecer.
     */
    public void setIdPedido(long idPedido) {
        this.idPedido = idPedido;
    }
    /**
     * @param jornadaDespachoAnterior El jornadaDespachoAnterior a establecer.
     */
    public void setJornadaDespachoAnterior(JornadaDTO jornadaDespachoAnterior) {
        this.jornadaDespachoAnterior = jornadaDespachoAnterior;
    }
    /**
     * @param motivoReprogramacion El motivoReprogramacion a establecer.
     */
    public void setMotivoReprogramacion(ObjetoDTO motivoReprogramacion) {
        this.motivoReprogramacion = motivoReprogramacion;
    }
    /**
     * @param responsableReprogramacion El responsableReprogramacion a establecer.
     */
    public void setResponsableReprogramacion(ObjetoDTO responsableReprogramacion) {
        this.responsableReprogramacion = responsableReprogramacion;
    }
    /**
     * @param usuario El usuario a establecer.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
