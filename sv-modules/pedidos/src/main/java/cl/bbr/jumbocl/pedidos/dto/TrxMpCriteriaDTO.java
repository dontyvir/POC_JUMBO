package cl.bbr.jumbocl.pedidos.dto;

public class TrxMpCriteriaDTO {

	private long   local_fact;
	private long   id_pedido;
	private long   id_estado;
	private int    pag;
	private int    regsperpag;
	private String tipo_fecha;
	private String fecha_ini;
	private String fecha_fin;
	private String origen;
	private String tipo_doc;
	private boolean limitarFecha=true; //true: limita 6 meses, la consulta de Transacciones, false: no limita la consulta de Transacciones

	/**
	 * @return Returns the id_estado.
	 */
	public long getId_estado() {
		return id_estado;
	}

	/**
	 * @param id_estado The id_estado to set.
	 */
	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}

	/**
	 * @return Returns the fecha_fin.
	 */
	public String getFecha_fin() {
		return fecha_fin;
	}

	/**
	 * @param fecha_fin The fecha_fin to set.
	 */
	public void setFecha_fin(String fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

	/**
	 * @return Returns the fecha_ini.
	 */
	public String getFecha_ini() {
		return fecha_ini;
	}

	/**
	 * @param fecha_ini The fecha_ini to set.
	 */
	public void setFecha_ini(String fecha_ini) {
		this.fecha_ini = fecha_ini;
	}

	/**
	 * @return Returns the tipo_fecha.
	 */
	public String getTipo_fecha() {
		return tipo_fecha;
	}

	/**
	 * @param tipo_fecha The tipo_fecha to set.
	 */
	public void setTipo_fecha(String tipo_fecha) {
		this.tipo_fecha = tipo_fecha;
	}

	/**
	 * @return Returns the pag.
	 */
	public int getPag() {
		return pag;
	}

	/**
	 * @param pag The pag to set.
	 */
	public void setPag(int pag) {
		this.pag = pag;
	}

	/**
	 * @return Returns the regsperpag.
	 */
	public int getRegsperpag() {
		return regsperpag;
	}

	/**
	 * @param regsperpag The regsperpag to set.
	 */
	public void setRegsperpag(int regsperpag) {
		this.regsperpag = regsperpag;
	}

	public TrxMpCriteriaDTO() {
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
	 * @return Returns the local_fact.
	 */
	public long getLocal_fact() {
		return local_fact;
	}

	/**
	 * @param local_fact The local_fact to set.
	 */
	public void setLocal_fact(long local_fact) {
		this.local_fact = local_fact;
	}
	
	
	

    /**
     * @return Devuelve origen.
     */
    public String getOrigen() {
        return origen;
    }
    /**
     * @return Devuelve tipo_doc.
     */
    public String getTipo_doc() {
        return tipo_doc;
    }
    /**
     * @param origen El origen a establecer.
     */
    public void setOrigen(String origen) {
        this.origen = origen;
    }
    /**
     * @param tipo_doc El tipo_doc a establecer.
     */
    public void setTipo_doc(String tipo_doc) {
        this.tipo_doc = tipo_doc;
    }
    /**
     * @return Devuelve limitarFecha.
     */
    public boolean isLimitarFecha() {
        return limitarFecha;
    }
    /**
     * @param limitarFecha El limitarFecha a establecer.
     */
    public void setLimitarFecha(boolean limitarFecha) {
        this.limitarFecha = limitarFecha;
    }
}
