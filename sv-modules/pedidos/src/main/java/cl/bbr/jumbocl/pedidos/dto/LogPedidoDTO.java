package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class LogPedidoDTO implements Serializable{
	private long	id_log;
	private long 	id_pedido;
	private String	usuario;
	private String	log;
	private String	fecha;
	private long    id_motivo;
	private String  motivo;
	private long    id_motivo_anterior;
	private String  motivo_anterior;
	
    public LogPedidoDTO() {
        
    }
    
    public LogPedidoDTO(long id_pedido, String usuario, String log) {
        this.id_pedido = id_pedido;
        this.usuario = usuario;
        this.log = log;
    }	
	
	/**
	 * @return Returns the id_pedido.
	 */
	public long getId_pedido() {
		return id_pedido;
	}
	/**
	 * @param id_pedido The id_pedido to set.
	 */
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	/**
	 * @return Returns the id_motivo.
	 */
	public long getId_motivo() {
		return id_motivo;
	}
	/**
	 * @return Returns the id_motivo_anterior.
	 */
	public long getId_motivo_anterior() {
		return id_motivo_anterior;
	}
	/**
	 * @return Returns the motivo.
	 */
	public String getMotivo() {
		return motivo;
	}
	/**
	 * @return Returns the motivo_anterior.
	 */
	public String getMotivo_anterior() {
		return motivo_anterior;
	}
	/**
	 * @param id_motivo The id_motivo to set.
	 */
	public void setId_motivo(long id_motivo) {
		this.id_motivo = id_motivo;
	}
	/**
	 * @param id_motivo_anterior The id_motivo_anterior to set.
	 */
	public void setId_motivo_anterior(long id_motivo_anterior) {
		this.id_motivo_anterior = id_motivo_anterior;
	}
	/**
	 * @param motivo The motivo to set.
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	/**
	 * @param motivo_anterior The motivo_anterior to set.
	 */
	public void setMotivo_anterior(String motivo_anterior) {
		this.motivo_anterior = motivo_anterior;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public long getId_log() {
		return id_log;
	}
	public void setId_log(long id_log) {
		this.id_log = id_log;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}
