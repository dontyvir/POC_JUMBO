package cl.bbr.jumbocl.common.model;

/**
 * Clase que captura desde la base de datos los datos de los pedidos de un producto
 * @author bbr
 *
 */
public class ProductosPedidoEntity {

	private int id_detalle;       //id
	private int id_pedido;        // tabla pedido 
	private int id_sector;        // tabla sector
	private int id_producto;      // tabla productos
	private String cod_prod1;
	private String uni_med;
	private String descripcion;
	private double cant_solic;
	private double cant_faltan;
	private String observacion;	
	private String preparable;
	private String con_nota;
	private double precio;
	
	//adicionales tabla sector
	private String nom_sector;
    
    private long idCriterio;
    private String descCriterio;
	
	public ProductosPedidoEntity() {
	}


	/**
	 * @param id_detalle
	 * @param id_ronda
	 * @param id_pedido
	 * @param id_sector
	 * @param id_producto
	 * @param descripcion
	 * @param cant_solic
	 * @param observacion
	 * @param preparable
	 * @param con_nota
	 * @param nom_sector
	 */
	public ProductosPedidoEntity(int id_detalle, int id_ronda, int id_pedido, int id_sector, int id_producto,String cod_prod1,String uni_med, String descripcion, double cant_solic, String observacion, String preparable, String con_nota, String nom_sector) {
		this.id_detalle = id_detalle;
		this.id_pedido = id_pedido;
		this.id_sector = id_sector;
		this.id_producto = id_producto;
		this.cod_prod1 = cod_prod1;
		this.uni_med = uni_med;
		this.descripcion = descripcion;
		this.cant_solic = cant_solic;
		this.observacion = observacion;
		this.preparable = preparable;
		this.con_nota = con_nota;
		this.nom_sector = nom_sector;
	}


	/**
	 * @return Returns the cod_prod1.
	 */
	public String getCod_prod1() {
		return cod_prod1;
	}


	/**
	 * @return Returns the uni_med.
	 */
	public String getUni_med() {
		return uni_med;
	}


	/**
	 * @param cod_prod1 The cod_prod1 to set.
	 */
	public void setCod_prod1(String cod_prod1) {
		this.cod_prod1 = cod_prod1;
	}


	/**
	 * @param uni_med The uni_med to set.
	 */
	public void setUni_med(String uni_med) {
		this.uni_med = uni_med;
	}


	/**
	 * @return Returns the con_nota.
	 */
	public String getCon_nota() {
		return con_nota;
	}


	/**
	 * @return Returns the preparable.
	 */
	public String getPreparable() {
		return preparable;
	}


	/**
	 * @param con_nota The con_nota to set.
	 */
	public void setCon_nota(String con_nota) {
		this.con_nota = con_nota;
	}


	/**
	 * @return cant_faltan
	 */
	public double getCant_faltan() {
		return cant_faltan;
	}


	/**
	 * @param cant_faltan
	 */
	public void setCant_faltan(double cant_faltan) {
		this.cant_faltan = cant_faltan;
	}


	/**
	 * @param preparable The preparable to set.
	 */
	public void setPreparable(String preparable) {
		this.preparable = preparable;
	}


	/**
	 * @return Returns the cant_solic.
	 */
	public double getCant_solic() {
		return cant_solic;
	}

	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @return Returns the id_detalle.
	 */
	public int getId_detalle() {
		return id_detalle;
	}

	/**
	 * @return Returns the id_pedido.
	 */
	public int getId_pedido() {
		return id_pedido;
	}

	/**
	 * @return Returns the id_producto.
	 */
	public int getId_producto() {
		return id_producto;
	}

	/**
	 * @return Returns the id_sector.
	 */
	public int getId_sector() {
		return id_sector;
	}

	/**
	 * @return Returns the nom_sector.
	 */
	public String getNom_sector() {
		return nom_sector;
	}

	/**
	 * @return Returns the observacion.
	 */
	public String getObservacion() {
		return observacion;
	}

	/**
	 * @param cant_solic The cant_solic to set.
	 */
	public void setCant_solic(double cant_solic) {
		this.cant_solic = cant_solic;
	}

	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @param id_detalle The id_detalle to set.
	 */
	public void setId_detalle(int id_detalle) {
		this.id_detalle = id_detalle;
	}

	/**
	 * @param id_pedido The id_pedido to set.
	 */
	public void setId_pedido(int id_pedido) {
		this.id_pedido = id_pedido;
	}

	/**
	 * @param id_producto The id_producto to set.
	 */
	public void setId_producto(int id_producto) {
		this.id_producto = id_producto;
	}

	/**
	 * @param id_sector The id_sector to set.
	 */
	public void setId_sector(int id_sector) {
		this.id_sector = id_sector;
	}

	/**
	 * @param nom_sector The nom_sector to set.
	 */
	public void setNom_sector(String nom_sector) {
		this.nom_sector = nom_sector;
	}

	/**
	 * @param observacion The observacion to set.
	 */
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}


	/**
	 * @return precio
	 */
	public double getPrecio() {
		return precio;
	}


	/**
	 * @param precio
	 */
	public void setPrecio(double precio) {
		this.precio = precio;
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
