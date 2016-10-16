package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
import java.util.Date;

public class MonitorDespachosDTO implements Serializable{

	private long	id_pedido;
	private long	id_estado;
	private long	id_comuna;
	private long	id_jpicking;
	private long	id_zona;
	private String  tipo_despacho;
	private String	f_despacho;
	private String	h_ini;
	private String	h_fin;
	private long	cant_bins;
	private String	zona_despacho;
	private String  desc_zona_desp;
	private String	comuna;
	private String	estado;
	private Date	hini_jpick;
	private Date	hfin_jpick;
	private Date	fecha_picking;	
	private String	dir_calle;
	private String	dir_numero;
	private String	dir_depto;
	private String  dir_conflictiva;
    private String  personaRetiraRecibe;
    private String  rutCompletoPersona;
    private long    nroGuiaCaso;
    private long idRuta;
    private int reprogramada;
    private String tipoDocumento;
    private String origen;
    private boolean confirmada;
    private double latitud;
    private double longitud;
    private long idJDespacho;
    
	public MonitorDespachosDTO() {
	
	}
	
	public long getCant_bins() {
		return cant_bins;
	}

	public void setCant_bins(long cant_bins) {
		this.cant_bins = cant_bins;
	}

	public String getComuna() {
		return comuna;
	}

	public void setComuna(String comuna) {
		this.comuna = comuna;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getF_despacho() {
		return f_despacho;
	}

	public void setF_despacho(String f_despacho) {
		this.f_despacho = f_despacho;
	}

	public String getH_fin() {
		return h_fin;
	}

	public void setH_fin(String h_fin) {
		this.h_fin = h_fin;
	}

	public String getH_ini() {
		return h_ini;
	}

	public void setH_ini(String h_ini) {
		this.h_ini = h_ini;
	}

	public long getId_comuna() {
		return id_comuna;
	}

	public void setId_comuna(long id_comuna) {
		this.id_comuna = id_comuna;
	}

	public long getId_estado() {
		return id_estado;
	}

	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}

	public long getId_pedido() {
		return id_pedido;
	}

	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}

	public String getZona_despacho() {
		return zona_despacho;
	}

	public void setZona_despacho(String zona_despacho) {
		this.zona_despacho = zona_despacho;
	}

	public long getId_jpicking() {
		return id_jpicking;
	}

	public void setId_jpicking(long id_jpicking) {
		this.id_jpicking = id_jpicking;
	}

	public long getId_zona() {
		return id_zona;
	}

	public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
	}

	public String getDesc_zona_desp() {
		return desc_zona_desp;
	}

	public void setDesc_zona_desp(String desc_zona_desp) {
		this.desc_zona_desp = desc_zona_desp;
	}

	public Date getHfin_jpick() {
		return hfin_jpick;
	}

	public void setHfin_jpick(Date hfin_jpick) {
		this.hfin_jpick = hfin_jpick;
	}

	public Date getHini_jpick() {
		return hini_jpick;
	}

	public void setHini_jpick(Date hini_jpick) {
		this.hini_jpick = hini_jpick;
	}

	public Date getFecha_picking() {
		return fecha_picking;
	}

	public void setFecha_picking(Date fecha_picking) {
		this.fecha_picking = fecha_picking;
	}

	/**
	 * @return Returns the dir_calle.
	 */
	public String getDir_calle() {
		return dir_calle;
	}

	/**
	 * @param dir_calle The dir_calle to set.
	 */
	public void setDir_calle(String dir_calle) {
		this.dir_calle = dir_calle;
	}

	/**
	 * @return Returns the dir_depto.
	 */
	public String getDir_depto() {
		return dir_depto;
	}

	/**
	 * @param dir_depto The dir_depto to set.
	 */
	public void setDir_depto(String dir_depto) {
		this.dir_depto = dir_depto;
	}

	/**
	 * @return Returns the dir_numero.
	 */
	public String getDir_numero() {
		return dir_numero;
	}

	/**
	 * @param dir_numero The dir_numero to set.
	 */
	public void setDir_numero(String dir_numero) {
		this.dir_numero = dir_numero;
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
    /**
     * @return Devuelve dir_conflictiva.
     */
    public String getDir_conflictiva() {
        return dir_conflictiva;
    }
    /**
     * @param dir_conflictiva El dir_conflictiva a establecer.
     */
    public void setDir_conflictiva(String dir_conflictiva) {
        this.dir_conflictiva = dir_conflictiva;
    }
    /**
     * @return Devuelve personaRetiraRecibe.
     */
    public String getPersonaRetiraRecibe() {
        return personaRetiraRecibe;
    }
    /**
     * @return Devuelve rutCompletoPersona.
     */
    public String getRutCompletoPersona() {
        return rutCompletoPersona;
    }
    /**
     * @param personaRetiraRecibe El personaRetiraRecibe a establecer.
     */
    public void setPersonaRetiraRecibe(String personaRetiraRecibe) {
        this.personaRetiraRecibe = personaRetiraRecibe;
    }
    /**
     * @param rutCompletoPersona El rutCompletoPersona a establecer.
     */
    public void setRutCompletoPersona(String rutCompletoPersona) {
        this.rutCompletoPersona = rutCompletoPersona;
    }
    /**
     * @return Devuelve nroGuiaCaso.
     */
    public long getNroGuiaCaso() {
        return nroGuiaCaso;
    }
    /**
     * @param nroGuiaCaso El nroGuiaCaso a establecer.
     */
    public void setNroGuiaCaso(long nroGuiaCaso) {
        this.nroGuiaCaso = nroGuiaCaso;
    }
    /**
     * @return Devuelve idRuta.
     */
    public long getIdRuta() {
        return idRuta;
    }
    /**
     * @param idRuta El idRuta a establecer.
     */
    public void setIdRuta(long idRuta) {
        this.idRuta = idRuta;
    }

    /**
     * @return Devuelve reprogramada.
     */
    public int getReprogramada() {
        return reprogramada;
    }
    /**
     * @param reprogramada El reprogramada a establecer.
     */
    public void setReprogramada(int reprogramada) {
        this.reprogramada = reprogramada;
    }
    /**
     * @return Devuelve tipoDocumento.
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }
    /**
     * @param tipoDocumento El tipoDocumento a establecer.
     */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
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
    /**
     * @return Devuelve confirmada.
     */
    public boolean isConfirmada() {
        return confirmada;
    }
    /**
     * @return Devuelve latitud.
     */
    public double getLatitud() {
        return latitud;
    }
    /**
     * @return Devuelve longitud.
     */
    public double getLongitud() {
        return longitud;
    }
    /**
     * @param confirmada El confirmada a establecer.
     */
    public void setConfirmada(boolean confirmada) {
        this.confirmada = confirmada;
    }
    /**
     * @param latitud El latitud a establecer.
     */
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }
    /**
     * @param longitud El longitud a establecer.
     */
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
    /**
     * @return Devuelve idJDespacho.
     */
    public long getIdJDespacho() {
        return idJDespacho;
    }
    /**
     * @param idJDespacho El idJDespacho a establecer.
     */
    public void setIdJDespacho(long idJDespacho) {
        this.idJDespacho = idJDespacho;
    }
}
