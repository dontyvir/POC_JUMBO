package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Representa los productos del pedido 
 * @author jsepulveda
 *
 *
 */
public class ProductosPedidoDTO implements Serializable{

	private long   id_detalle;
	private long   id_pedido;
	private long   id_ronda;
	private long   id_sector;
	private long   id_producto;
	private long   id_prod_fo;
	private long   id_dronda;
	private String id_catprod;
	private long   id_zona;
	private int    idCriterio;
	private long   id_seccion;
	private String seccion;
	private String cod_producto;
	private String cod_sap;
	private String unid_medida;
	private double cant_solic;
	private String sector;
	private String descripcion;
	private String observacion = "";
	private double precio;
	private String pesable;
	private String preparable;
	private String con_nota;
	private double cant_pick;
	private double cant_faltan;
	private double cant_spick;
	private long   cod_bin;
	private String pol_sustitucion;
	//agregar campos para generar pedido desde cotizacion
	private double precio_lista;
	private double dscto_item;
	private String cod_barra;
	private String sust_cformato;
	private int    mot_sustitucion;
	private String estado;
	private String tipo_sel;
	private String descCriterio;
	private String orden;
    private BigDecimal cantidad;
    private String nombreUsuario; //Nombre del Ultimo Usuario que dejo el Producto Sin Pickear
    private String nombreLocal;
    private String fechaDespacho;
    private String horaInicio;
    private String horaFin;
    
    /*Octubte 2015*/
    private String umbral_minimo;
    private String umbral_maximo;
    /*Octubte 2015*/
    
    private int rubro;
	
    private int codigoPromocion;
    
    public String getFechaDespacho() {
		return fechaDespacho;
	}
	public void setFechaDespacho(String fechaDespacho) {
		this.fechaDespacho = fechaDespacho;
	}
	public String getHoraInicio() {
		return horaInicio;
	}
	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}
	public String getHoraFin() {
		return horaFin;
	}
	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
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
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * @return Returns the mot_sustitucion.
	 */
	public int getMot_sustitucion() {
		return mot_sustitucion;
	}
	/**
	 * @param mot_sustitucion The mot_sustitucion to set.
	 */
	public void setMot_sustitucion(int mot_sustitucion) {
		this.mot_sustitucion = mot_sustitucion;
	}
	/**
	 * @return Returns the sust_cformato.
	 */
	public String getSust_cformato() {
		return sust_cformato;
	}
	/**
	 * @param sust_cformato The sust_cformato to set.
	 */
	public void setSust_cformato(String sust_cformato) {
		this.sust_cformato = sust_cformato;
	}
	public double getDscto_item() {
		return dscto_item;
	}
	public void setDscto_item(double dscto_item) {
		this.dscto_item = dscto_item;
	}
	public double getPrecio_lista() {
		return precio_lista;
	}
	public void setPrecio_lista(double precio_lista) {
		this.precio_lista = precio_lista;
	}
	public String getPol_sustitucion() {
		return pol_sustitucion;
	}
	public void setPol_sustitucion(String pol_sustitucion) {
		this.pol_sustitucion = pol_sustitucion;
	}
	public double getCant_faltan() {
		return cant_faltan;
	}
	public void setCant_faltan(double cant_faltan) {
		this.cant_faltan = cant_faltan;
	}
	public double getCant_pick() {
		return cant_pick;
	}
	public void setCant_pick(double cant_pick) {
		this.cant_pick = cant_pick;
	}
	public double getCant_spick() {
		return cant_spick;
	}
	public void setCant_spick(double cant_spick) {
		this.cant_spick = cant_spick;
	}
	public String getCon_nota() {
		return con_nota;
	}
	public void setCon_nota(String con_nota) {
		this.con_nota = con_nota;
	}
	public String getPreparable() {
		return preparable;
	}
	public void setPreparable(String preparable) {
		this.preparable = preparable;
	}
	public double getCant_solic() {
		return cant_solic;
	}
	public void setCant_solic(double cant_solic) {
		this.cant_solic = cant_solic;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public long getId_detalle() {
		return id_detalle;
	}
	public void setId_detalle(long id_detalle) {
		this.id_detalle = id_detalle;
	}
	public long getId_pedido() {
		return id_pedido;
	}
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}
	public long getId_producto() {
		return id_producto;
	}
	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}
	public long getId_ronda() {
		return id_ronda;
	}
	public void setId_ronda(long id_ronda) {
		this.id_ronda = id_ronda;
	}
	public long getId_sector() {
		return id_sector;
	}
	public void setId_sector(long id_sector) {
		this.id_sector = id_sector;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getCod_producto() {
		return cod_producto;
	}
	public void setCod_producto(String cod_producto) {
		this.cod_producto = cod_producto;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getUnid_medida() {
		return unid_medida;
	}
	public void setUnid_medida(String unid_medida) {
		this.unid_medida = unid_medida;
	}
	/**
	 * @return Returns the precio.
	 */
	public double getPrecio() {
		return precio;
	}
	/**
	 * @param precio The precio to set.
	 */
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	public String getPesable() {
		return pesable;
	}
	
	public void setPesable(String pesable) {
		this.pesable = pesable;
	}
	public long getId_dronda() {
		return id_dronda;
	}
	public void setId_dronda(long id_dronda) {
		this.id_dronda = id_dronda;
	}
	public long getCod_bin() {
		return cod_bin;
	}
	public void setCod_bin(long cod_bin) {
		this.cod_bin = cod_bin;
	}
	public String getId_catprod() {
		return id_catprod;
	}
	public void setId_catprod(String id_catprod) {
		this.id_catprod = id_catprod;
	}
	/**
	 * @return Returns the cod_barra.
	 */
	public String getCod_barra() {
		return cod_barra;
	}
	/**
	 * @param cod_barra The cod_barra to set.
	 */
	public void setCod_barra(String cod_barra) {
		this.cod_barra = cod_barra;
	}

	public String getTipoSel() {
		return tipo_sel;
	}

	public void setTipoSel(String tipo_sel) {
		this.tipo_sel = tipo_sel;
	}
	
    /**
     * @return Devuelve id_prod_fo.
     */
    public long getId_prod_fo() {
        return id_prod_fo;
    }
    /**
     * @param id_prod_fo El id_prod_fo a establecer.
     */
    public void setId_prod_fo(long id_prod_fo) {
        this.id_prod_fo = id_prod_fo;
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
    public int getIdCriterio() {
        return idCriterio;
    }
    /**
     * @return Devuelve tipo_sel.
     */
    public String getTipo_sel() {
        return tipo_sel;
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
    public void setIdCriterio(int idCriterio) {
        this.idCriterio = idCriterio;
    }
    /**
     * @param tipo_sel El tipo_sel a establecer.
     */
    public void setTipo_sel(String tipo_sel) {
        this.tipo_sel = tipo_sel;
    }
    /**
     * @return Devuelve orden.
     */
    public String getOrden() {
        return orden;
    }
    /**
     * @param orden El orden a establecer.
     */
    public void setOrden(String orden) {
        this.orden = orden;
    }
    /**
     * @return Devuelve cantidad.
     */
    public BigDecimal getCantidad() {
        return cantidad;
    }
    /**
     * @param cantidad El cantidad a establecer.
     */
    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
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
     * @return Devuelve id_seccion.
     */
    public long getId_seccion() {
        return id_seccion;
    }
    /**
     * @return Devuelve seccion.
     */
    public String getSeccion() {
        return seccion;
    }
    /**
     * @param id_seccion El id_seccion a establecer.
     */
    public void setId_seccion(long id_seccion) {
        this.id_seccion = id_seccion;
    }
    /**
     * @param seccion El seccion a establecer.
     */
    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }
	/**
	 * @return Devuelve cod_sap.
	 */
	public String getCod_sap() {
		return cod_sap;
	}
	/**
	 * @param cod_sap El cod_sap a establecer.
	 */
	public void setCod_sap(String cod_sap) {
		this.cod_sap = cod_sap;
	}
	public int getRubro() {
		return rubro;
	}
	public void setRubro(int rubro) {
		this.rubro = rubro;
	}
	public String getNombreLocal() {
		return nombreLocal;
	}
	public void setNombreLocal(String nombreLocal) {
		this.nombreLocal = nombreLocal;
	}
	
	public String getUmbral_minimo() {
		return umbral_minimo;
	}
	public void setUmbral_minimo(String umbral_minimo) {
		this.umbral_minimo = umbral_minimo;
	}
	public String getUmbral_maximo() {
		return umbral_maximo;
	}
	public void setUmbral_maximo(String umbral_maximo) {
		this.umbral_maximo = umbral_maximo;
	}
	public int getCodigoPromocion() {
		return codigoPromocion;
	}
	public void setCodigoPromocion(int codigoPromocion) {
		this.codigoPromocion = codigoPromocion;
	}
}
