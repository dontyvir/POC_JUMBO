package cl.bbr.jumbocl.pedidos.dto;

public class FaltanteDTO {
	String	cod_producto;
	String	descripcion;
	double	cant_faltante;
	long	id_pedido;
	String  uni_med;
	double  precio;
    
    private long idCriterio;
    private String descCriterio;
	
	/**
	 * 
	 */
	public FaltanteDTO() {
	}
	/**
	 * @param cod_producto
	 * @param descripcion
	 * @param cant_faltante
	 * @param id_pedido
	 */
	public FaltanteDTO(String cod_producto, String descripcion, double cant_faltante, long id_pedido) {
		this.cod_producto = cod_producto;
		this.descripcion = descripcion;
		this.cant_faltante = cant_faltante;
		this.id_pedido = id_pedido;
	}
	public long getId_pedido() {
		return id_pedido;
	}
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	public double getCant_faltante() {
		return cant_faltante;
	}
	public void setCant_faltante(double cant_faltante) {
		this.cant_faltante = cant_faltante;
	}
	public String getCod_producto() {
		return cod_producto;
	}
	public void setCod_producto(String cod_producto) {
		this.cod_producto = cod_producto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public double getPrecio() {
		return precio;
	}
	public String getUni_med() {
		return uni_med;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public void setUni_med(String uni_med) {
		this.uni_med = uni_med;
	}
	
    /**
     * @return Devuelve descCriterio.
     */
    public String getDescCriterio() {
        return descCriterio;
    }
    /**
     * @return Devuelve idCriterio.
     */
    public long getIdCriterio() {
        return idCriterio;
    }
    /**
     * @param descCriterio El descCriterio a establecer.
     */
    public void setDescCriterio(String descCriterio) {
        this.descCriterio = descCriterio;
    }
    /**
     * @param idCriterio El idCriterio a establecer.
     */
    public void setIdCriterio(long idCriterio) {
        this.idCriterio = idCriterio;
    }
}
