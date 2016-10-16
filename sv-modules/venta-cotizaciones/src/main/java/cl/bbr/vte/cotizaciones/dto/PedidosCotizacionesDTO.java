package cl.bbr.vte.cotizaciones.dto;

/**
 * Clase que contiene información del pedido
 * 
 * @author BBR
 *
 */
public class PedidosCotizacionesDTO {
	private long ped_id;
	private String fec_pedido;
	private String fec_despacho;
	private String local;
	private String local_fact;
	private double costo_desp;
	private double cant_falt;
	private String estado;
	private long   id_estado;
	
	public long getId_estado() {
		return id_estado;
	}
	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}
	public String getLocal_fact() {
		return local_fact;
	}
	public void setLocal_fact(String local_fact) {
		this.local_fact = local_fact;
	}
	/**
	 * @return Returns the cant_falt.
	 */
	public double getCant_falt() {
		return cant_falt;
	}
	/**
	 * @return Returns the costo_desp.
	 */
	public double getCosto_desp() {
		return costo_desp;
	}
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @return Returns the fec_despacho.
	 */
	public String getFec_despacho() {
		return fec_despacho;
	}
	/**
	 * @return Returns the fec_pedido.
	 */
	public String getFec_pedido() {
		return fec_pedido;
	}
	/**
	 * @return Returns the local.
	 */
	public String getLocal() {
		return local;
	}
	/**
	 * @return Returns the ped_id.
	 */
	public long getPed_id() {
		return ped_id;
	}
	/**
	 * @param cant_falt The cant_falt to set.
	 */
	public void setCant_falt(double cant_falt) {
		this.cant_falt = cant_falt;
	}
	/**
	 * @param costo_desp The costo_desp to set.
	 */
	public void setCosto_desp(double costo_desp) {
		this.costo_desp = costo_desp;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @param fec_despacho The fec_despacho to set.
	 */
	public void setFec_despacho(String fec_despacho) {
		this.fec_despacho = fec_despacho;
	}
	/**
	 * @param fec_pedido The fec_pedido to set.
	 */
	public void setFec_pedido(String fec_pedido) {
		this.fec_pedido = fec_pedido;
	}
	/**
	 * @param local The local to set.
	 */
	public void setLocal(String local) {
		this.local = local;
	}
	/**
	 * @param ped_id The ped_id to set.
	 */
	public void setPed_id(long ped_id) {
		this.ped_id = ped_id;
	}
	
		
	
}
