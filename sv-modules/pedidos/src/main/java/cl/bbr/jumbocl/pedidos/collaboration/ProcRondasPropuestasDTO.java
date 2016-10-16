package cl.bbr.jumbocl.pedidos.collaboration;

public class ProcRondasPropuestasDTO {

	private long id_local; 
	private long id_sector; 
	private long id_jornada;
	private String tipo_ve;
	private long   id_zona;
	private long   id_pedido; //usado sólo en Picking Light
	private long   cant_prod; //usado sólo en Picking Light

	/**
	 * @return Returns the id_jornada.
	 */
	public long getId_jornada() {
		return id_jornada;
	}
	/**
	 * @param id_jornada The id_jornada to set.
	 */
	public void setId_jornada(long id_jornada) {
		this.id_jornada = id_jornada;
	}
	/**
	 * @return Returns the id_local.
	 */
	public long getId_local() {
		return id_local;
	}
	/**
	 * @param id_local The id_local to set.
	 */
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}
	/**
	 * @return Returns the id_sector.
	 */
	public long getId_sector() {
		return id_sector;
	}
	/**
	 * @param id_sector The id_sector to set.
	 */
	public void setId_sector(long id_sector) {
		this.id_sector = id_sector;
	}
	/**
	 * @return Returns the tipo_ve.
	 */
	public String getTipo_ve() {
		return tipo_ve;
	}
	/**
	 * @param tipo_ve The tipo_ve to set.
	 */
	public void setTipo_ve(String tipo_ve) {
		this.tipo_ve = tipo_ve;
	}
	/**
	 * @return Returns the id_zona.
	 */
	public long getId_zona() {
		return id_zona;
	}
	/**
	 * @param id_zona The id_zona to set.
	 */
	public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
	}
	
	
    /**
     * @return Devuelve id_pedido.
     */
    public long getId_pedido() {
        return id_pedido;
    }
    /**
     * @param id_pedido El id_pedido a establecer.
     */
    public void setId_pedido(long id_pedido) {
        this.id_pedido = id_pedido;
    }
    /**
     * @return Devuelve cant_prod.
     */
    public long getCant_prod() {
        return cant_prod;
    }
    /**
     * @param cant_prod El cant_prod a establecer.
     */
    public void setCant_prod(long cant_prod) {
        this.cant_prod = cant_prod;
    }
}
