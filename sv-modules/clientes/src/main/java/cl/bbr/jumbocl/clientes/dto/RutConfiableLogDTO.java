package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;

/**
 * 
 * @author imoyano
 *
 */

public class RutConfiableLogDTO implements Serializable{
    
	private long idLog;
	private String usuario;
	private String nombreUsuario;
    private String fechaHora;
    private String descripcion;
	
	public RutConfiableLogDTO() {
	}

	
	
	
    /**
     * @return Devuelve fechaHora.
     */
    public String getFechaHora() {
        return fechaHora;
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
     * @param fechaHora El fechaHora a establecer.
     */
    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
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
    /**
     * @return Devuelve nombreUsuario.
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    /**
     * @param nombreUsuario El nombreUsuario a establecer.
     */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    /**
     * @return Devuelve descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @param descripcion El descripcion a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
