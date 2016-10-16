package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class MonitorPedidosDTO implements Serializable{
	private long    id_pedido;
	private long    rut_cliente;
	private String  dv_cliente;	
	private String  nom_cliente;	
	private long    id_jpicking;
	private String  fpicking;
	private double  cant_prods;
	private double  cant_pick;
	private String  tipo_despacho;
	private String  fdespacho;
	private String  hdespacho;
	private String  hdespacho_fin;
	private String  estado;
	private long    id_estado;
	private String  hini_jpicking;
	private String  hfin_jpicking;
	private String  zona_nombre;
	//BOC
	private String  fingreso;
	private String  local_despacho;
	private String  local_facturacion;
	private long    monto;
	private long    id_usuario;
	private boolean conAlertaMPago;
	private String  origen;
	private String  tipo_ve;
	private long    id_zona;
	private String  medioPago;
	private double  cant_spick;
    private PedidoExtDTO pedExt;
    private String direccion;
    private String comuna;
    private boolean montoExcedido;
    private boolean anularBoleta;
	
    /**
     * @return Devuelve medioPago.
     */
    public String getMedioPago() {
        return medioPago;
    }
    /**
     * @param medioPago El medioPago a establecer.
     */
    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
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
	 * @return Devuelve conAlertaMPago.
	 */
	public boolean isConAlertaMPago() {
		return conAlertaMPago;
	}
	/**
	 * @param conAlertaMPago El conAlertaMPago a establecer.
	 */
	public void setConAlertaMPago(boolean conAlertaMPago) {
		this.conAlertaMPago = conAlertaMPago;
	}
	/**
	 * @return Returns the local_facturacion.
	 */
	public String getLocal_facturacion() {
		return local_facturacion;
	}

	/**
	 * @param local_facturacion The local_facturacion to set.
	 */
	public void setLocal_facturacion(String local_facturacion) {
		this.local_facturacion = local_facturacion;
	}

	/**
	 * @return Returns the origen.
	 */
	public String getOrigen() {
		return origen;
	}

	/**
	 * @param origen The origen to set.
	 */
	public void setOrigen(String origen) {
		this.origen = origen;
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
	 * @return Returns the id_usuario.
	 */
	public long getId_usuario() {
		return id_usuario;
	}

	/**
	 * @param id_usuario The id_usuario to set.
	 */
	public void setId_usuario(long id_usuario) {
		this.id_usuario = id_usuario;
	}

	/**
	 * @return Returns the dv_cliente.
	 */
	public String getDv_cliente() {
		return dv_cliente;
	}

	/**
	 * @return Returns the rut_cliente.
	 */
	public long getRut_cliente() {
		return rut_cliente;
	}

	/**
	 * @param dv_cliente The dv_cliente to set.
	 */
	public void setDv_cliente(String dv_cliente) {
		this.dv_cliente = dv_cliente;
	}

	/**
	 * @param rut_cliente The rut_cliente to set.
	 */
	public void setRut_cliente(long rut_cliente) {
		this.rut_cliente = rut_cliente;
	}

	/**
	 * @return Returns the nom_cliente.
	 */
	public String getNom_cliente() {
		return nom_cliente;
	}
	
	/**
	 * @param nom_cliente The nom_cliente to set.
	 */
	public void setNom_cliente(String nom_cliente) {
		this.nom_cliente = nom_cliente;
	}

	/**
	 * @return Returns the fingreso.
	 */
	public String getFingreso() {
		return fingreso;
	}
	/**
	 * @return Returns the local_despacho.
	 */
	public String getLocal_despacho() {
		return local_despacho;
	}
	/**
	 * @return Returns the monto.
	 */
	public long getMonto() {
		return monto;
	}
	/**
	 * @param fingreso The fingreso to set.
	 */
	public void setFingreso(String fingreso) {
		this.fingreso = fingreso;
	}
	/**
	 * @param local_despacho The local_despacho to set.
	 */
	public void setLocal_despacho(String local_despacho) {
		this.local_despacho = local_despacho;
	}
	/**
	 * @param monto The monto to set.
	 */
	public void setMonto(long monto) {
		this.monto = monto;
	}
	public double getCant_prods() {
		return cant_prods;
	}
	public void setCant_prods(double cant_prods) {
		this.cant_prods = cant_prods;
	}
	
    public double getCant_pick() {
		return cant_pick;
	}
	public void setCant_pick(double cant_pick) {
		this.cant_pick = cant_pick;
	}
	
    /**
     * @return Devuelve tipo_despacho.
     */
    public String getTipo_despacho() {
        return tipo_despacho;
    }
    /**
     * @param tipo_despacho El tipo_despacho a establecer.
     */
    public void setTipo_despacho(String tipo_despacho) {
        this.tipo_despacho = tipo_despacho;
    }
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getFdespacho() {
		return fdespacho;
	}
	public void setFdespacho(String fdespacho) {
		this.fdespacho = fdespacho;
	}
	public String getFpicking() {
		return fpicking;
	}
	public void setFpicking(String fpicking) {
		this.fpicking = fpicking;
	}
	public String getHdespacho() {
		return hdespacho;
	}
	public void setHdespacho(String hdespacho) {
		this.hdespacho = hdespacho;
	}
	public long getId_jpicking() {
		return id_jpicking;
	}
	public void setId_jpicking(long id_jpicking) {
		this.id_jpicking = id_jpicking;
	}
	public long getId_pedido() {
		return id_pedido;
	}
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}

	public long getId_estado() {
		return id_estado;
	}

	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}
	
	public String getHfin_jpicking() {
		return hfin_jpicking;
	}

	public String getHini_jpicking() {
		return hini_jpicking;
	}

	public void setHfin_jpicking(String hfin_jpicking) {
		this.hfin_jpicking = hfin_jpicking;
	}

	public void setHini_jpicking(String hini_jpicking) {
		this.hini_jpicking = hini_jpicking;
	}
	/**
	 * @return Devuelve zona_nombre.
	 */
	public String getZona_nombre() {
		return zona_nombre;
	}
	/**
	 * @param zona_nombre El zona_nombre a establecer.
	 */
	public void setZona_nombre(String zona_nombre) {
		this.zona_nombre = zona_nombre;
	}
	/**
	 * @return Devuelve hdespacho_fin.
	 */
	public String getHdespacho_fin() {
		return hdespacho_fin;
	}
	/**
	 * @param hdespacho_fin El hdespacho_fin a establecer.
	 */
	public void setHdespacho_fin(String hdespacho_fin) {
		this.hdespacho_fin = hdespacho_fin;
	}
    /**
     * @return Devuelve cant_spick.
     */
    public double getCant_spick() {
        return cant_spick;
    }
    /**
     * @param cant_spick El cant_spick a establecer.
     */
    public void setCant_spick(double cant_spick) {
        this.cant_spick = cant_spick;
    }
    /**
     * @return Devuelve pedExt.
     */
    public PedidoExtDTO getPedExt() {
        return pedExt;
    }
    /**
     * @param pedExt El pedExt a establecer.
     */
    public void setPedExt(PedidoExtDTO pedExt) {
        this.pedExt = pedExt;
    }
    /**
     * @return Devuelve direccion.
     */
    public String getDireccion() {
        return direccion;
    }
    /**
     * @param direccion El direccion a establecer.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    /**
     * @return Devuelve comuna.
     */
    public String getComuna() {
        return comuna;
    }
    /**
     * @param comuna El comuna a establecer.
     */
    public void setComuna(String comuna) {
        this.comuna = comuna;
    }
    /**
     * @return Devuelve montoExcedido.
     */
    public boolean isMontoExcedido() {
        return montoExcedido;
    }
    /**
     * @param montoExcedido El montoExcedido a establecer.
     */
    public void setMontoExcedido(boolean montoExcedido) {
        this.montoExcedido = montoExcedido;
    }
    /**
     * @return Devuelve anularBoleta.
     */
    public boolean isAnularBoleta() {
        return anularBoleta;
    }
    /**
     * @param anularBoleta El anularBoleta a establecer.
     */
    public void setAnularBoleta(boolean anularBoleta) {
        this.anularBoleta = anularBoleta;
    }
}
