package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class DespachoDTO implements Serializable{

	private long	id_pedido;
	private long	id_estado;
	private long	id_jpicking;
	private long	id_zona;
	private long	id_comuna;
	private String	f_ingreso;
	private String	f_despacho;
	private String  h_ingreso;
	private String	h_ini;
	private String	h_fin;
	private String	zona_despacho;
	private String	estado;
	private String	comuna;
	private int		cant_bins;
	private String	nom_cliente;
	private String	dir_tipo_calle;
	private String	dir_calle;
	private String	dir_numero;
	private String	dir_depto;
	private String  dir_conflictiva;
	private String  dir_conflictiva_comentario;
	private long    num_doc;
	private String  tipo_doc;
	private long    num_caja;
	private String  telefono;
	private String  cod_telefono;
	private String  telefono2;
	private String  cod_telefono2;
	private String  pers_autorizada;
    private String  rutPersonaRetira;
	private String  observaciones;
    private String  indicaciones;
	private long    monto_total;
	private String	zona_descripcion;
    private String  tipoDespacho;
    private ClientesDTO cliente;
    private double  latitud;
    private double  longitud;
    private boolean confirmada;
    private String  medioPago;
	

	public String getZona_descripcion() {
		return zona_descripcion;
	}

	public void setZona_descripcion(String zona_descripcion) {
		this.zona_descripcion = zona_descripcion;
	}

	public String getCod_telefono() {
		return cod_telefono;
	}

	public long getMonto_total() {
		return monto_total;
	}

	public long getNum_caja() {
		return num_caja;
	}

	public long getNum_doc() {
		return num_doc;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public String getPers_autorizada() {
		return pers_autorizada;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setCod_telefono(String cod_telefono) {
		this.cod_telefono = cod_telefono;
	}

	public void setMonto_total(long monto_total) {
		this.monto_total = monto_total;
	}

	public void setNum_caja(long num_caja) {
		this.num_caja = num_caja;
	}

	public void setNum_doc(long num_doc) {
		this.num_doc = num_doc;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public void setPers_autorizada(String pers_autorizada) {
		this.pers_autorizada = pers_autorizada;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public DespachoDTO() {

	}

	public String getComuna() {
		return comuna;
	}

	public void setComuna(String comuna) {
		this.comuna = comuna;
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

	public long getId_zona() {
		return id_zona;
	}

	public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
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

	public int getCant_bins() {
		return cant_bins;
	}
	public void setCant_bins(int cant_bins) {
		this.cant_bins = cant_bins;
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
	public String getF_ingreso() {
		return f_ingreso;
	}
	public void setF_ingreso(String f_ingreso) {
		this.f_ingreso = f_ingreso;
	}
	public String getH_fin() {
		return h_fin;
	}
	public void setH_fin(String h_fin) {
		this.h_fin = h_fin;
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
	public String getNom_cliente() {
		return nom_cliente;
	}
	public void setNom_cliente(String nom_cliente) {
		this.nom_cliente = nom_cliente;
	}

	public String getDir_calle() {
		return dir_calle;
	}

	public void setDir_calle(String dir_calle) {
		this.dir_calle = dir_calle;
	}

	public String getDir_depto() {
		return dir_depto;
	}

	public void setDir_depto(String dir_depto) {
		this.dir_depto = dir_depto;
	}

	public String getDir_numero() {
		return dir_numero;
	}

	public void setDir_numero(String dir_numero) {
		this.dir_numero = dir_numero;
	}

	public String getDir_tipo_calle() {
		return dir_tipo_calle;
	}

	public void setDir_tipo_calle(String dir_tipo_calle) {
		this.dir_tipo_calle = dir_tipo_calle;
	}

	public String getTipo_doc() {
		return tipo_doc;
	}

	public void setTipo_doc(String tipo_doc) {
		this.tipo_doc = tipo_doc;
	}

	public String getCod_telefono2() {
		return cod_telefono2;
	}

	public String getTelefono2() {
		return telefono2;
	}

	public void setCod_telefono2(String cod_telefono2) {
		this.cod_telefono2 = cod_telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}
		
    /**
     * @return Devuelve dir_conflictiva.
     */
    public String getDir_conflictiva() {
        return dir_conflictiva;
    }
    /**
     * @return Devuelve dir_conflictiva_comentario.
     */
    public String getDir_conflictiva_comentario() {
        return dir_conflictiva_comentario;
    }
    /**
     * @param dir_conflictiva El dir_conflictiva a establecer.
     */
    public void setDir_conflictiva(String dir_conflictiva) {
        this.dir_conflictiva = dir_conflictiva;
    }
    /**
     * @param dir_conflictiva_comentario El dir_conflictiva_comentario a establecer.
     */
    public void setDir_conflictiva_comentario(String dir_conflictiva_comentario) {
        this.dir_conflictiva_comentario = dir_conflictiva_comentario;
    }
    /**
     * @return Devuelve indicaciones.
     */
    public String getIndicaciones() {
        return indicaciones;
    }
    /**
     * @param indicaciones El indicaciones a establecer.
     */
    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }
    /**
     * @return Devuelve tipoDespacho.
     */
    public String getTipoDespacho() {
        return tipoDespacho;
    }
    /**
     * @param tipoDespacho El tipoDespacho a establecer.
     */
    public void setTipoDespacho(String tipoDespacho) {
        this.tipoDespacho = tipoDespacho;
    }
    /**
     * @return Devuelve rutPersonaRetira.
     */
    public String getRutPersonaRetira() {
        return rutPersonaRetira;
    }
    /**
     * @param rutPersonaRetira El rutPersonaRetira a establecer.
     */
    public void setRutPersonaRetira(String rutPersonaRetira) {
        this.rutPersonaRetira = rutPersonaRetira;
    }
    /**
     * @return Devuelve cliente.
     */
    public ClientesDTO getCliente() {
        return cliente;
    }
    /**
     * @param cliente El cliente a establecer.
     */
    public void setCliente(ClientesDTO cliente) {
        this.cliente = cliente;
    }
    /**
     * @return Devuelve h_ingreso.
     */
    public String getH_ingreso() {
        return h_ingreso;
    }
    /**
     * @param h_ingreso El h_ingreso a establecer.
     */
    public void setH_ingreso(String h_ingreso) {
        this.h_ingreso = h_ingreso;
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
    
    public String getMedioPago() {
		return medioPago;
	}

	public void setMedioPago(String medioPago) {
		this.medioPago = medioPago;
	}
}
