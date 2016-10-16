package cl.bbr.jumbocl.casos.dto;

import java.io.Serializable;

public class LogCasosDTO implements Serializable {
    
	private long	idLog;
	private long 	idCaso;
	private EstadoCasoDTO estado;
	private String	usuario;
	private String	fecha;
	private String  descripcion;	
	
	public LogCasosDTO() {
	    
	}
	
	
	public LogCasosDTO(long idCaso, EstadoCasoDTO estado, String usuario, String descripcion) {
	    this.idCaso = idCaso;
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
    public EstadoCasoDTO getEstado() {
        return estado;
    }
    /**
     * @return Devuelve fecha.
     */
    public String getFecha() {
        return fecha;
    }
    /**
     * @return Devuelve idCaso.
     */
    public long getIdCaso() {
        return idCaso;
    }
    /**
     * @return Devuelve idLog.
     */
    public long getIdLog() {
        return idLog;
    }
    /**
     * @return Devuelve usuario.
     */
    public String getUsuario() {
        return usuario;
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
    public void setEstado(EstadoCasoDTO estado) {
        this.estado = estado;
    }
    /**
     * @param fecha El fecha a establecer.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    /**
     * @param idCaso El idCaso a establecer.
     */
    public void setIdCaso(long idCaso) {
        this.idCaso = idCaso;
    }
    /**
     * @param idLog El idLog a establecer.
     */
    public void setIdLog(long idLog) {
        this.idLog = idLog;
    }
    /**
     * @param usuario El usuario a establecer.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
