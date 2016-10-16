package cl.bbr.jumbocl.pedidos.collaboration;

import java.io.Serializable;
import java.util.List;

/**
 * @author BBRI
 * 
 */
public class ProcInsPedidoDTO implements Serializable {  

	private long   id_cliente;
	private long   dir_id;
	private long   id_jdespacho;
	private long   id_zona;
	private String fecha_despacho;
	
	private String medio_pago;				/* Tipo de medio de pago */
	private String num_mp;					/* Número de tarjeta medio de pago */ 
				
	private long   id_usuario_fono;	
	private long   id_estado;				/* Id Estado del Pedido */
	private String clave_mp;				/* Clave de la tarjeta sólo tarjetas paris */
	private String fecha_exp;				/* Fecha expiración tarjetas bancarias */
	private long   n_cuotas;				/* Número de cuotas */
	private long   meses_librpago;			/* Meses libre de pago */
	private String nom_tbancaria;			/* Nombre de tarjeta bancaria */ 
	private String tb_banco;				/* Nombre de banco para de tarjeta */ 
	
	private String rut_tit;
	private String dv_tit;
	private String nom_tit;
	private String apat_tit;
	private String amat_tit;
	private String dir_tit;
	private String dir_num_tit;
	//private long num_doc;
	private String tipo_doc;				/*  B: Boleta;  F: Factura  */
	//private long cant_productos;
	//private long cant_bins; 
	private long   sin_gente_op; 
	private String sin_gente_txt;
    private long   sin_gente_rut;
    private String sin_gente_dv;
	
	//private String dir_tipo_calle; 
	//private String dir_calle; 
	//private String dir_numero; 
	//private String dir_depto; 
	
	// política de sustitución
	private long   pol_id;
	private String pol_sustitucion; 
	
	// Información de facturación
	private String fac_razon;
	private long   fac_rut;
	private String fac_dv;
	private String fac_direccion;
	private String fac_fono;
	private String fac_giro;
	private String fac_comuna;
	private String fac_ciudad;

	private String observacion;
	
	// Listado de DetallePedido
	private List   productos = null;
	
	//campos para pedidos a generar desde cotizacion
	private long   id_cotizacion;
	private long   id_local_desp;
	private long   id_local_fact;
	private String tipo_despacho;   //E: Express, N: Normal, X: Extendido
	private double costo_desp;
	private String origen;
	private String tipo_ve;
	
	// Campos tcp y cupones
	private List lst_tcp     = null;
	private List lst_cupones = null;
    private String dispositivo = null;
    
    private double cantidadProductos = 0.0;
    private double montoTotal = 0.0;
    private double montoReservado = 0.0;
	    
    private boolean invitado = false;
    
    private boolean descuentoDespacho = false;
	
	/**
	 * Constructor
	 */
	public ProcInsPedidoDTO() {

	}

	public long getId_cotizacion() {
		return id_cotizacion;
	}

	public void setId_cotizacion(long id_cotizacion) {
		this.id_cotizacion = id_cotizacion;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getTipo_ve() {
		return tipo_ve;
	}

	public void setTipo_ve(String tipo_ve) {
		this.tipo_ve = tipo_ve;
	}

	public double getCosto_desp() {
		return costo_desp;
	}

	public void setCosto_desp(double costo_desp) {
		this.costo_desp = costo_desp;
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
    
	public long getId_local_desp() {
		return id_local_desp;
	}

	public void setId_local_desp(long id_local_desp) {
		this.id_local_desp = id_local_desp;
	}

	public long getId_local_fact() {
		return id_local_fact;
	}

	public void setId_local_fact(long id_local_fact) {
		this.id_local_fact = id_local_fact;
	}

	public long getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(long id_cliente) {
		this.id_cliente = id_cliente;
	}

	public long getId_jdespacho() {
		return id_jdespacho;
	}

	public void setId_jdespacho(long id_jdespacho) {
		this.id_jdespacho = id_jdespacho;
	}

	public long getId_zona() {
		return id_zona;
	}

	public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
	}

	public String getMedio_pago() {
		return medio_pago;
	}

	public void setMedio_pago(String medio_pago) {
		this.medio_pago = medio_pago;
	}

	public String getNum_mp() {
		return num_mp;
	}

	public void setNum_mp(String num_mp) {
		this.num_mp = num_mp;
	}

	public String getNom_tit() {
		return nom_tit;
	}

	public void setNom_tit(String nom_tit) {
		this.nom_tit = nom_tit;
	}

	public String getRut_tit() {
		return rut_tit;
	}

	public void setRut_tit(String rut_tit) {
		this.rut_tit = rut_tit;
	}

	public String getSin_gente_txt() {
		return sin_gente_txt;
	}

	public void setSin_gente_txt(String sin_gente_txt) {
		this.sin_gente_txt = sin_gente_txt;
	}

	public long getSin_gente_op() {
		return sin_gente_op;
	}

	public void setSin_gente_op(long sin_gente_op) {
		this.sin_gente_op = sin_gente_op;
	}

	public String getPol_sustitucion() {
		return pol_sustitucion;
	}

	public void setPol_sustitucion(String pol_sustitucion) {
		this.pol_sustitucion = pol_sustitucion;
	}

	public List getProductos() {
		return productos;
	}

	public void setProductos(List roductos) {
		this.productos = roductos;
	}

	public long getDir_id() {
		return dir_id;
	}

	public void setDir_id(long dir_id) {
		this.dir_id = dir_id;
	}

	public long getPol_id() {
		return pol_id;
	}

	public void setPol_id(long pol_id) {
		this.pol_id = pol_id;
	}

	public String getAmat_tit() {
		return amat_tit;
	}

	public void setAmat_tit(String amat_tit) {
		this.amat_tit = amat_tit;
	}

	public String getApat_tit() {
		return apat_tit;
	}

	public void setApat_tit(String apat_tit) {
		this.apat_tit = apat_tit;
	}

	public String getClave_mp() {
		return clave_mp;
	}

	public void setClave_mp(String clave_mp) {
		this.clave_mp = clave_mp;
	}

	public String getDir_num_tit() {
		return dir_num_tit;
	}

	public void setDir_num_tit(String dir_num_tit) {
		this.dir_num_tit = dir_num_tit;
	}

	public String getDir_tit() {
		return dir_tit;
	}

	public void setDir_tit(String dir_tit) {
		this.dir_tit = dir_tit;
	}

	public String getDv_tit() {
		return dv_tit;
	}

	public void setDv_tit(String dv_tit) {
		this.dv_tit = dv_tit;
	}

	public String getFecha_exp() {
		return fecha_exp;
	}

	public void setFecha_exp(String fecha_exp) {
		this.fecha_exp = fecha_exp;
	}

	public long getId_usuario_fono() {
		return id_usuario_fono;
	}

	public void setId_usuario_fono(long id_usuario_fono) {
		this.id_usuario_fono = id_usuario_fono;
	}

	public long getMeses_librpago() {
		return meses_librpago;
	}

	public void setMeses_librpago(long meses_librpago) {
		this.meses_librpago = meses_librpago;
	}

	public long getN_cuotas() {
		return n_cuotas;
	}

	public void setN_cuotas(long n_cuotas) {
		this.n_cuotas = n_cuotas;
	}

	public String getNom_tbancaria() {
		return nom_tbancaria;
	}

	public void setNom_tbancaria(String nom_tbancaria) {
		this.nom_tbancaria = nom_tbancaria;
	}

	public String getTb_banco() {
		return tb_banco;
	}

	public void setTb_banco(String tb_banco) {
		this.tb_banco = tb_banco;
	}

	public String getTipo_doc() {
		return tipo_doc;
	}

	public void setTipo_doc(String tipo_doc) {
		this.tipo_doc = tipo_doc;
	}

	public String getFac_direccion() {
		return fac_direccion;
	}

	public void setFac_direccion(String fac_direccion) {
		this.fac_direccion = fac_direccion;
	}

	public String getFac_dv() {
		return fac_dv;
	}

	public void setFac_dv(String fac_dv) {
		this.fac_dv = fac_dv;
	}

	public String getFac_fono() {
		return fac_fono;
	}

	public void setFac_fono(String fac_fono) {
		this.fac_fono = fac_fono;
	}

	public String getFac_giro() {
		return fac_giro;
	}

	public void setFac_giro(String fac_giro) {
		this.fac_giro = fac_giro;
	}

	public String getFac_razon() {
		return fac_razon;
	}

	public void setFac_razon(String fac_razon) {
		this.fac_razon = fac_razon;
	}

	public long getFac_rut() {
		return fac_rut;
	}

	public void setFac_rut(long fac_rut) {
		this.fac_rut = fac_rut;
	}

	public String getFac_ciudad() {
		return fac_ciudad;
	}

	public void setFac_ciudad(String fac_ciudad) {
		this.fac_ciudad = fac_ciudad;
	}

	public String getFac_comuna() {
		return fac_comuna;
	}

	public void setFac_comuna(String fac_comuna) {
		this.fac_comuna = fac_comuna;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}


    /**
     * @return Devuelve fecha_despacho (YYYY-MM-DD).
     */
    public String getFecha_despacho(){
        return fecha_despacho;
    }
    /**
     * @param fecha_despacho El fecha_despacho a establecer (YYYY-MM-DD).
     */
    public void setFecha_despacho(String fecha_despacho){
        this.fecha_despacho = fecha_despacho;
    }
	/**
	 * @return Returns the lst_cupones.
	 */
	public List getLst_cupones() {
		return lst_cupones;
	}

	/**
	 * @param lst_cupones The lst_cupones to set.
	 */
	public void setLst_cupones(List lst_cupones) {
		this.lst_cupones = lst_cupones;
	}

	/**
	 * @return Returns the lst_tcp.
	 */
	public List getLst_tcp() {
		return lst_tcp;
	}

	/**
	 * @param lst_tcp The lst_tcp to set.
	 */
	public void setLst_tcp(List lst_tcp) {
		this.lst_tcp = lst_tcp;
	}




    /**
     * @return Devuelve dispositivo.
     */
    public String getDispositivo() {
        return dispositivo;
    }
    /**
     * @param dispositivo El dispositivo a establecer.
     */
    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }
    /**
     * @return Devuelve sin_gente_dv.
     */
    public String getSin_gente_dv() {
        return sin_gente_dv;
    }
    /**
     * @return Devuelve sin_gente_rut.
     */
    public long getSin_gente_rut() {
        return sin_gente_rut;
    }
    /**
     * @param sin_gente_dv El sin_gente_dv a establecer.
     */
    public void setSin_gente_dv(String sin_gente_dv) {
        this.sin_gente_dv = sin_gente_dv;
    }
    /**
     * @param sin_gente_rut El sin_gente_rut a establecer.
     */
    public void setSin_gente_rut(long sin_gente_rut) {
        this.sin_gente_rut = sin_gente_rut;
    }
    
    /**
     * @return Devuelve cantidadProductos.
     */
    public double getCantidadProductos() {
        return cantidadProductos;
    }
    /**
     * @return Devuelve montoTotal.
     */
    public double getMontoTotal() {
        return montoTotal;
    }
    /**
     * @param cantidadProductos El cantidadProductos a establecer.
     */
    public void setCantidadProductos(double cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }
    /**
     * @param montoTotal El montoTotal a establecer.
     */
    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }
    /**
     * @return Devuelve montoReservado.
     */
    public double getMontoReservado() {
        return montoReservado;
    }
    /**
     * @param montoReservado El montoReservado a establecer.
     */
    public void setMontoReservado(double montoReservado) {
        this.montoReservado = montoReservado;
    }

	public long getId_estado() {
		return id_estado;
	}

	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}

	public boolean isInvitado() {
		return invitado;
	}

	public void setInvitado(boolean invitado) {
		this.invitado = invitado;
	}

	public boolean isDescuentoDespacho() {
		return descuentoDespacho;
	}

	public void setDescuentoDespacho(boolean descuentoDespacho) {
		this.descuentoDespacho = descuentoDespacho;
	}
}
