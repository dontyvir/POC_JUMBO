package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class MonitorRondasDTO implements Serializable{
	
    private long    id_pedido;
	private long	id_ronda;
	private long	id_jornada;
	private String	sector;
	private long	id_sector;
	private double	cant_prods;
	private String	estado;
	private long	id_estado;
	private long	id_local;
	private String 	f_picking;
	private String 	f_creacion;
	private int		dif_creacion;
	private int 	dif_picking;
	private int		dif_termino;
	private String  tipo_ve;
	private String  zonas;
	private int		orden;
	private double  cant_spick;
	private String  estadoImpEtiqueta;
	private String  estadoVerDetalle;
	private String  fecha_inico_ronda_pkl;
	private String  fecha_imp_listado_pkl;
    private String  estadoAuditSustitucion;
	
	
	/**
	 * @return Returns the cant_spick.
	 */
	public double getCant_spick() {
		return cant_spick;
	}
	/**
	 * @param cant_spick The cant_spick to set.
	 */
	public void setCant_spick(double cant_spick) {
		this.cant_spick = cant_spick;
	}
	/**
	 * @return Returns the orden.
	 */
	public int getOrden() {
		return orden;
	}
	/**
	 * @param orden The orden to set.
	 */
	public void setOrden(int orden) {
		this.orden = orden;
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
	public int getDif_picking() {
		return dif_picking;
	}
	public void setDif_picking(int dif_picking) {
		this.dif_picking = dif_picking;
	}
	
	public int getDif_termino() {
		return dif_termino;
	}
	public void setDif_termino(int dif_termino) {
		this.dif_termino = dif_termino;
	}
	
	public String getF_creacion() {
		return f_creacion;
	}
	public void setF_creacion(String f_creacion) {
		this.f_creacion = f_creacion;
	}
	public String getF_picking() {
		return f_picking;
	}
	public void setF_picking(String f_picking) {
		this.f_picking = f_picking;
	}
	public long getId_local() {
		return id_local;
	}
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}
	public double getCant_prods() {
		return cant_prods;
	}
	public void setCant_prods(double cant_prods) {
		this.cant_prods = cant_prods;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public long getId_estado() {
		return id_estado;
	}
	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}
	public long getId_jornada() {
		return id_jornada;
	}
	public void setId_jornada(long id_jornada) {
		this.id_jornada = id_jornada;
	}
	public long getId_ronda() {
		return id_ronda;
	}
	public void setId_ronda(long id_ronda) {
		this.id_ronda = id_ronda;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public int getDif_creacion() {
		return dif_creacion;
	}
	public void setDif_creacion(int dif_creacion) {
		this.dif_creacion = dif_creacion;
	}
	public long getId_sector() {
		return id_sector;
	}
	public void setId_sector(long id_sector) {
		this.id_sector = id_sector;
	}
	/**
	 * @return Returns the zonas.
	 */
	public String getZonas() {
		return zonas;
	}
	/**
	 * @param zonas The zonas to set.
	 */
	public void setZonas(String zonas) {
		this.zonas = zonas;
	}

	
    /**
     * @return Devuelve estadoImpEtiqueta.
     */
    public String getEstadoImpEtiqueta() {
        return estadoImpEtiqueta;
    }
    /**
     * @param estadoImpEtiqueta El estadoImpEtiqueta a establecer.
     */
    public void setEstadoImpEtiqueta(String estadoImpEtiqueta) {
        this.estadoImpEtiqueta = estadoImpEtiqueta;
    }
    /**
     * @return Devuelve estadoVerDetalle.
     */
    public String getEstadoVerDetalle() {
        return estadoVerDetalle;
    }
    /**
     * @param estadoVerDetalle El estadoVerDetalle a establecer.
     */
    public void setEstadoVerDetalle(String estadoVerDetalle) {
        this.estadoVerDetalle = estadoVerDetalle;
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
     * @return Devuelve fecha_imp_listado_pkl.
     */
    public String getFecha_imp_listado_pkl() {
        return fecha_imp_listado_pkl;
    }
    /**
     * @return Devuelve fecha_inico_ronda_pkl.
     */
    public String getFecha_inico_ronda_pkl() {
        return fecha_inico_ronda_pkl;
    }
    /**
     * @param fecha_imp_listado_pkl El fecha_imp_listado_pkl a establecer.
     */
    public void setFecha_imp_listado_pkl(String fecha_imp_listado_pkl) {
        this.fecha_imp_listado_pkl = fecha_imp_listado_pkl;
    }
    /**
     * @param fecha_inico_ronda_pkl El fecha_inico_ronda_pkl a establecer.
     */
    public void setFecha_inico_ronda_pkl(String fecha_inico_ronda_pkl) {
        this.fecha_inico_ronda_pkl = fecha_inico_ronda_pkl;
    }
    /**
     * @return Devuelve estadoAuditSustitucion.
     */
    public String getEstadoAuditSustitucion() {
        return estadoAuditSustitucion;
}
    /**
     * @param estadoAuditSustitucion El estadoAuditSustitucion a establecer.
     */
    public void setEstadoAuditSustitucion(String estadoAuditSustitucion) {
        this.estadoAuditSustitucion = estadoAuditSustitucion;
    }
}
