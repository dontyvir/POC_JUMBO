package cl.bbr.jumbocl.pedidos.dto;

public class MonitorTrxMpDTO {
		
	private long id_trxmp;
	private double monto_trxmp;
	private long cant_prods;
	private long id_estado;	
	private String estado_nom;
	private long id_pedido;
	private long num_doc;
	private String fecha;
	private long num_caja;
	private double pos_monto_fp;
	private String pos_fecha;
	private String pos_hora;
	private String pos_fp;
	private long id_local_fact;
	private String nom_local_fact;
	private String tipo_doc;
	private String fecha_picking;
	private String fecha_despacho;
	private String origen;
	

	
	/**
	 * @return Returns the fecha_despacho.
	 */
	public String getFecha_despacho() {
		return fecha_despacho;
	}

	/**
	 * @param fecha_despacho The fecha_despacho to set.
	 */
	public void setFecha_despacho(String fecha_despacho) {
		this.fecha_despacho = fecha_despacho;
	}

	/**
	 * @return Returns the fecha_picking.
	 */
	public String getFecha_picking() {
		return fecha_picking;
	}

	/**
	 * @param fecha_picking The fecha_picking to set.
	 */
	public void setFecha_picking(String fecha_picking) {
		this.fecha_picking = fecha_picking;
	}

	/**
	 * @return Returns the tipo_doc.
	 */
	public String getTipo_doc() {
		return tipo_doc;
	}

	/**
	 * @param tipo_doc The tipo_doc to set.
	 */
	public void setTipo_doc(String tipo_doc) {
		this.tipo_doc = tipo_doc;
	}

	/**
	 * @return Returns the id_long_fact.
	 */
	public long getId_local_fact() {
		return id_local_fact;
	}

	/**
	 * @param id_long_fact The id_long_fact to set.
	 */
	public void setId_local_fact(long id_local_fact) {
		this.id_local_fact = id_local_fact;
	}

	/**
	 * @return Returns the nom_local_fact.
	 */
	public String getNom_local_fact() {
		return nom_local_fact;
	}

	/**
	 * @param nom_local_fact The nom_local_fact to set.
	 */
	public void setNom_local_fact(String nom_local_fact) {
		this.nom_local_fact = nom_local_fact;
	}

	public String getFecha() {
		return fecha;
	}

	public long getNum_caja() {
		return num_caja;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public void setNum_caja(long num_caja) {
		this.num_caja = num_caja;
	}

	public long getNum_doc() {
		return num_doc;
	}

	public void setNum_doc(long num_doc) {
		this.num_doc = num_doc;
	}

	public MonitorTrxMpDTO() {
		
	}

	public long getCant_prods() {
		return cant_prods;
	}

	public String getEstado_nom() {
		return estado_nom;
	}

	public long getId_estado() {
		return id_estado;
	}

	public long getId_pedido() {
		return id_pedido;
	}

	public long getId_trxmp() {
		return id_trxmp;
	}

	public double getMonto_trxmp() {
		return monto_trxmp;
	}

	public void setCant_prods(long cant_prods) {
		this.cant_prods = cant_prods;
	}

	public void setEstado_nom(String estado_nom) {
		this.estado_nom = estado_nom;
	}

	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}

	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}

	public void setId_trxmp(long id_trxmp) {
		this.id_trxmp = id_trxmp;
	}

	public void setMonto_trxmp(double monto_trxmp) {
		this.monto_trxmp = monto_trxmp;
	}

	/**
	 * @return Returns the pos_fecha.
	 */
	public String getPos_fecha() {
		return pos_fecha;
	}

	/**
	 * @param pos_fecha The pos_fecha to set.
	 */
	public void setPos_fecha(String pos_fecha) {
		this.pos_fecha = pos_fecha;
	}

	/**
	 * @return Returns the pos_fp.
	 */
	public String getPos_fp() {
		return pos_fp;
	}

	/**
	 * @param pos_fp The pos_fp to set.
	 */
	public void setPos_fp(String pos_fp) {
		this.pos_fp = pos_fp;
	}

	/**
	 * @return Returns the pos_hora.
	 */
	public String getPos_hora() {
		return pos_hora;
	}

	/**
	 * @param pos_hora The pos_hora to set.
	 */
	public void setPos_hora(String pos_hora) {
		this.pos_hora = pos_hora;
	}

	/**
	 * @return Returns the pos_monto_fp.
	 */
	public double getPos_monto_fp() {
		return pos_monto_fp;
	}

	/**
	 * @param pos_monto_fp The pos_monto_fp to set.
	 */
	public void setPos_monto_fp(double pos_monto_fp) {
		this.pos_monto_fp = pos_monto_fp;
	}

	
    /**
     * @return Devuelve origen.
     */
    public String getOrigen() {
        return origen;
    }
    /**
     * @param origen El origen a establecer.
     */
    public void setOrigen(String origen) {
        this.origen = origen;
    }
}
