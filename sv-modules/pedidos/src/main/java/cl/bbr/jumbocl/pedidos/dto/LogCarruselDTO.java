package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class LogCarruselDTO implements Serializable{

    private long	idLog;
	private String	usuario;
	private String	log;
	private String	fecha;

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
     * @return Devuelve log.
     */
    public String getLog() {
        return log;
    }
    /**
     * @return Devuelve usuario.
     */
    public String getUsuario() {
        return usuario;
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
     * @param log El log a establecer.
     */
    public void setLog(String log) {
        this.log = log;
    }
    /**
     * @param usuario El usuario a establecer.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
