package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

import cl.bbr.jumbocl.usuarios.dto.UserDTO;


public class LogRutaDTO implements Serializable {
    
    private long    idLog;
    private long    idRuta;
    private EstadoDTO estado;
    private UserDTO usuario;
    private String  fecha;
    private String  descripcion;
    
    public LogRutaDTO() {
        
    }

    public LogRutaDTO(long idRuta, EstadoDTO estado, UserDTO usuario, String descripcion) {
        this.idRuta = idRuta;
        this.estado = estado;
        this.usuario = usuario;
        this.descripcion = descripcion;
    }
    
    /**
     * @return Devuelve descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @return Devuelve estado.
     */
    public EstadoDTO getEstado() {
        return estado;
    }
    /**
     * @return Devuelve fecha.
     */
    public String getFecha() {
        return fecha;
    }
    /**
     * @return Devuelve idLog.
     */
    public long getIdLog() {
        return idLog;
    }
    /**
     * @return Devuelve idRuta.
     */
    public long getIdRuta() {
        return idRuta;
    }
    /**
     * @param descripcion El descripcion a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * @param estado El estado a establecer.
     */
    public void setEstado(EstadoDTO estado) {
        this.estado = estado;
    }
    /**
     * @param fecha El fecha a establecer.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    /**
     * @param idLog El idLog a establecer.
     */
    public void setIdLog(long idLog) {
        this.idLog = idLog;
    }
    /**
     * @param idRuta El idRuta a establecer.
     */
    public void setIdRuta(long idRuta) {
        this.idRuta = idRuta;
    }
    /**
     * @return Devuelve usuario.
     */
    public UserDTO getUsuario() {
        return usuario;
    }
    /**
     * @param usuario El usuario a establecer.
     */
    public void setUsuario(UserDTO usuario) {
        this.usuario = usuario;
    }
}
