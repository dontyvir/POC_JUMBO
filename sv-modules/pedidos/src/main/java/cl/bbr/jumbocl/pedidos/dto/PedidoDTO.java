package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
import java.util.Date;

public class PedidoDTO implements Serializable {
    public static final String PARIS_TITULAR = "PARIS TITULAR";
    public static final String PARIS_EMPLEADO = "PARIS EMPLEADO";    
    public static final String PARIS_ADICIONAL = "PARIS ADICIONAL";

	private long id_pedido;
	private long id_estado;		/* Obligatorio */
	private long id_jdespacho;	/* Obligatorio */
	private long id_jpicking;	/* Obligatorio */
	private long id_mot;
	private long id_mot_ant;
	private long id_local;		/* Obligatorio */
	private long id_local_fact;
	private long id_comuna;		/* Obligatorio */
	private long id_zona;		/* Obligatorio */
	private long id_usuario;
	private long id_usuario_fono;  //usuario fonocompras
	private long id_cliente;		/* Obligatorio */
	private String genero;
	private Date   fnac;
	private long   rut_cliente;
	private String dv_cliente;
	private String nom_cliente;
	private String telefono2;
	private String telefono;
	private String tipo_despacho;  //E: Express, N: Normal, X: Extendido
	private double costo_despacho;
	private String fingreso;
	private String hingreso;
	private double monto;
	private String indicacion;
	private String medio_pago; //JBM:Jumbo mas TBK:Tarjeta bancaria TPT:Tarjeta Paris Titular TPA:Tarjeta Paris Adicional
	private String num_mp;		/* Obligatorio */
	private String clave_mp;
	private String fecha_exp;
	private int    n_cuotas;
	private int    meses_librpago;
	private String nom_tbancaria;
	private String tb_banco;
	private String rut_tit;
	private String dv_tit;
	private String nom_tit;
	private String apat_tit;
	private String amat_tit;
	private String dir_tit;
	private String dir_num_tit;
	private int    num_doc;
	private String tipo_doc; 		/* Obligatorio */ //B : boleta o F: Factura
	private long   cant_prods;		/* Obligatorio */
	private int    cant_bins;
	private int    sin_gente_op;
	private String sin_gente_txt;   //Nombre de la persona que recibe el pedido en casa o que retira el pedido en el local
    private long   sin_gente_rut;   //Rut de la persona que retira el pedido   
    private String sin_gente_dv;    //Dv de la persona que retira el pedido
    private long   dir_id;
	private String dir_tipo_calle;
	private String dir_calle;
	private String dir_numero;
	private String dir_depto;
	private long   pol_id;
	private String pol_sustitucion;
	//auxiliares
	private String nom_local;
	private String nom_local_fact;
	private String nom_motivo;
	private String nom_ejecutivo;
	private String nom_usuario_bo;
	private String nom_comuna;
	private String fpicking;
	private String fdespacho;
	private String hdespacho;
	private String hfindespacho;
	private String estado;
	private long   id_sector;
	private String num_mp_unmask;
	//datos de transaccion pos_ eliminados
	//horas de picking
	private String hpicking;
	private String hfinpicking;
	private String observacion;
	
	private long   id_cotizacion;
	private String origen;
	private String tipo_ve;
	private String tipo_comuna;
	private String tipo_picking;
	
	//para promociones
	private boolean flg_recalc_prod;
	private boolean flg_recalc_mp;
    private String  dispositivo; // Si es 'I' es compra via Iphone
    
    private PedidoExtDTO pedidoExt;
    
    private String nomZona;
    
    private boolean confirmada;
    private double latitud;
    private double longitud;

	private int tbk_secuencia_x;
	private int tbk_secuencia_y;

    private double monto_reservado;
    private String secuenciaPago;
    
    private boolean montoExcedido;
    
    private boolean anularBoleta;
    //(+) INDRA (+)
    private double porcentaje_monto;
    private double porcentaje_unidad;
    //(-) INDRA (-)
    
    private long id_cupon;
    private boolean isCupon;
    private boolean isPromocion;
    private String  codigoCupon;
    
    private FacturaDTO factura;
    

    public String getCodigoCupon() {
		return codigoCupon;
	}


	public void setCodigoCupon(String codigoCupon) {
		this.codigoCupon = codigoCupon;
	}


	/**
	 * @return el id_cupon
	 */
	public long getId_cupon() {
		return id_cupon;
	}


	/**
	 * @param id_cupon el id_cupon a establecer
	 */
	public void setId_cupon(long id_cupon) {
		this.id_cupon = id_cupon;
	}


	/**
	 * @return el isCupon
	 */
	public boolean isCupon() {
		return isCupon;
	}


	/**
	 * @param isCupon el isCupon a establecer
	 */
	public void setCupon(boolean isCupon) {
		this.isCupon = isCupon;
	}


	/**
	 * @return el isPromocion
	 */
	public boolean isPromocion() {
		return isPromocion;
	}


	/**
	 * @param isPromocion el isPromocion a establecer
	 */
	public void setPromocion(boolean isPromocion) {
		this.isPromocion = isPromocion;
	}
	
	/**
	 * @return Returns the flg_recalc_mp.
	 */
	public boolean isFlg_recalc_mp() {
		return flg_recalc_mp;
	}


	/**
	 * @param flg_recalc_mp The flg_recalc_mp to set.
	 */
	public void setFlg_recalc_mp(boolean flg_recalc_mp) {
		this.flg_recalc_mp = flg_recalc_mp;
	}


	/**
	 * @return Returns the flg_recalc_prod.
	 */
	public boolean isFlg_recalc_prod() {
		return flg_recalc_prod;
	}


	/**
	 * @param flg_recalc_prod The flg_recalc_prod to set.
	 */
	public void setFlg_recalc_prod(boolean flg_recalc_prod) {
		this.flg_recalc_prod = flg_recalc_prod;
	}


	/**
	 * @return Returns the tipo_comuna.
	 */
	public String getTipo_comuna() {
		return tipo_comuna;
	}


	/**
	 * @param tipo_comuna The tipo_comuna to set.
	 */
	public void setTipo_comuna(String tipo_comuna) {
		this.tipo_comuna = tipo_comuna;
	}


	public long getId_cotizacion() {
		return id_cotizacion;
	}


	public void setId_cotizacion(long id_cotizacion) {
		this.id_cotizacion = id_cotizacion;
	}


	/**
	 * @return Returns the nom_local_fact.
	 */
	public String getNom_local_fact() {
		return nom_local_fact;
	}


	/**
	 * @param nom_local_fact The nom_local_fact to set.
	 */
	public void setNom_local_fact(String nom_local_fact) {
		this.nom_local_fact = nom_local_fact;
	}


	/**
	 * @return Returns the id_local_fact.
	 */
	public long getId_local_fact() {
		return id_local_fact;
	}


	/**
	 * @param id_local_fact The id_local_fact to set.
	 */
	public void setId_local_fact(long id_local_fact) {
		this.id_local_fact = id_local_fact;
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


	public void setHfinpicking(String hfinpicking) {
		this.hfinpicking = hfinpicking;
	}


	public void setHpicking(String hpicking) {
		this.hpicking = hpicking;
	}


	public PedidoDTO(){
		
	}
	
		/**
	 * @return Returns the amat_tit.
	 */
	public String getAmat_tit() {
		return amat_tit;
	}
	/**
	 * @param amat_tit The amat_tit to set.
	 */
	public void setAmat_tit(String amat_tit) {
		this.amat_tit = amat_tit;
	}
	/**
	 * @return Returns the apat_tit.
	 */
	public String getApat_tit() {
		return apat_tit;
	}
	/**
	 * @param apat_tit The apat_tit to set.
	 */
	public void setApat_tit(String apat_tit) {
		this.apat_tit = apat_tit;
	}
	/**
	 * @return Returns the cant_bins.
	 */
	public int getCant_bins() {
		return cant_bins;
	}
	/**
	 * @param cant_bins The cant_bins to set.
	 */
	public void setCant_bins(int cant_bins) {
		this.cant_bins = cant_bins;
	}
	/**
	 * @return Returns the cant_prods.
	 */
	public long getCant_prods() {
		return cant_prods;
	}
	/**
	 * @param cant_prods The cant_prods to set.
	 */
	public void setCant_prods(long cant_prods) {
		this.cant_prods = cant_prods;
	}
	/**
	 * @return Returns the clave_mp.
	 */
	public String getClave_mp() {
		return clave_mp;
	}
	/**
	 * @param clave_mp The clave_mp to set.
	 */
	public void setClave_mp(String clave_mp) {
		this.clave_mp = clave_mp;
	}
	/**
	 * @return Returns the costo_despacho.
	 */
	public double getCosto_despacho() {
		return costo_despacho;
	}
	/**
	 * @param costo_despacho The costo_despacho to set.
	 */
	public void setCosto_despacho(double costo_despacho) {
		this.costo_despacho = costo_despacho;
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
	 * @return Returns the dir_id.
	 */
	public long getDir_id() {
		return dir_id;
	}
	/**
	 * @param dir_id The dir_id to set.
	 */
	public void setDir_id(long dir_id) {
		this.dir_id = dir_id;
	}
	/**
	 * @return Returns the dir_num_tit.
	 */
	public String getDir_num_tit() {
		return dir_num_tit;
	}
	/**
	 * @param dir_num_tit The dir_num_tit to set.
	 */
	public void setDir_num_tit(String dir_num_tit) {
		this.dir_num_tit = dir_num_tit;
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
	 * @return Returns the dir_tipo_calle.
	 */
	public String getDir_tipo_calle() {
		return dir_tipo_calle;
	}
	/**
	 * @param dir_tipo_calle The dir_tipo_calle to set.
	 */
	public void setDir_tipo_calle(String dir_tipo_calle) {
		this.dir_tipo_calle = dir_tipo_calle;
	}
	/**
	 * @return Returns the dir_tit.
	 */
	public String getDir_tit() {
		return dir_tit;
	}
	/**
	 * @param dir_tit The dir_tit to set.
	 */
	public void setDir_tit(String dir_tit) {
		this.dir_tit = dir_tit;
	}
	/**
	 * @return Returns the dv_cliente.
	 */
	public String getDv_cliente() {
		return dv_cliente;
	}
	/**
	 * @param dv_cliente The dv_cliente to set.
	 */
	public void setDv_cliente(String dv_cliente) {
		this.dv_cliente = dv_cliente;
	}
	/**
	 * @return Returns the dv_tit.
	 */
	public String getDv_tit() {
		return dv_tit;
	}
	/**
	 * @param dv_tit The dv_tit to set.
	 */
	public void setDv_tit(String dv_tit) {
		this.dv_tit = dv_tit;
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
	 * @return Returns the fdespacho.
	 */
	public String getFdespacho() {
		return fdespacho;
	}
	/**
	 * @param fdespacho The fdespacho to set.
	 */
	public void setFdespacho(String fdespacho) {
		this.fdespacho = fdespacho;
	}
	/**
	 * @return Returns the fecha_exp.
	 */
	public String getFecha_exp() {
		return fecha_exp;
	}
	/**
	 * @param fecha_exp The fecha_exp to set.
	 */
	public void setFecha_exp(String fecha_exp) {
		this.fecha_exp = fecha_exp;
	}
	/**
	 * @return Returns the fingreso.
	 */
	public String getFingreso() {
		return fingreso;
	}
	/**
	 * @param fingreso The fingreso to set.
	 */
	public void setFingreso(String fingreso) {
		this.fingreso = fingreso;
	}

	public Date getFnac() {
		return fnac;
	}
	
	public void setFnac(Date fnac) {
		this.fnac = fnac;
	}

	/**
	 * @return Returns the fpicking.
	 */
	public String getFpicking() {
		return fpicking;
	}
	/**
	 * @param fpicking The fpicking to set.
	 */
	public void setFpicking(String fpicking) {
		this.fpicking = fpicking;
	}
	/**
	 * @return Returns the genero.
	 */
	public String getGenero() {
		return genero;
	}
	/**
	 * @param genero The genero to set.
	 */
	public void setGenero(String genero) {
		this.genero = genero;
	}
	/**
	 * @return Returns the hdespacho.
	 */
	public String getHdespacho() {
		return hdespacho;
	}
	/**
	 * @param hdespacho The hdespacho to set.
	 */
	public void setHdespacho(String hdespacho) {
		this.hdespacho = hdespacho;
	}
	/**
	 * @return Returns the hfindespacho.
	 */
	public String getHfindespacho() {
		return hfindespacho;
	}
	/**
	 * @param hfindespacho The hfindespacho to set.
	 */
	public void setHfindespacho(String hfindespacho) {
		this.hfindespacho = hfindespacho;
	}
	/**
	 * @return Returns the id_cliente.
	 */
	public long getId_cliente() {
		return id_cliente;
	}
	/**
	 * @param id_cliente The id_cliente to set.
	 */
	public void setId_cliente(long id_cliente) {
		this.id_cliente = id_cliente;
	}
	/**
	 * @return Returns the id_comuna.
	 */
	public long getId_comuna() {
		return id_comuna;
	}
	/**
	 * @param id_comuna The id_comuna to set.
	 */
	public void setId_comuna(long id_comuna) {
		this.id_comuna = id_comuna;
	}
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
	 * @return Returns the id_jdespacho.
	 */
	public long getId_jdespacho() {
		return id_jdespacho;
	}
	/**
	 * @param id_jdespacho The id_jdespacho to set.
	 */
	public void setId_jdespacho(long id_jdespacho) {
		this.id_jdespacho = id_jdespacho;
	}
	/**
	 * @return Returns the id_jpicking.
	 */
	public long getId_jpicking() {
		return id_jpicking;
	}
	/**
	 * @param id_jpicking The id_jpicking to set.
	 */
	public void setId_jpicking(long id_jpicking) {
		this.id_jpicking = id_jpicking;
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
	 * @return Returns the id_mot.
	 */
	public long getId_mot() {
		return id_mot;
	}
	/**
	 * @param id_mot The id_mot to set.
	 */
	public void setId_mot(long id_mot) {
		this.id_mot = id_mot;
	}
	/**
	 * @return Returns the id_mot_ant.
	 */
	public long getId_mot_ant() {
		return id_mot_ant;
	}
	/**
	 * @param id_mot_ant The id_mot_ant to set.
	 */
	public void setId_mot_ant(long id_mot_ant) {
		this.id_mot_ant = id_mot_ant;
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
	 * @return Returns the id_usuario_fono.
	 */
	public long getId_usuario_fono() {
		return id_usuario_fono;
	}
	/**
	 * @param id_usuario_fono The id_usuario_fono to set.
	 */
	public void setId_usuario_fono(long id_usuario_fono) {
		this.id_usuario_fono = id_usuario_fono;
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
	 * @return Returns the indicacion.
	 */
	public String getIndicacion() {
		return indicacion;
	}
	/**
	 * @param indicacion The indicacion to set.
	 */
	public void setIndicacion(String indicacion) {
		this.indicacion = indicacion;
	}
	/**
	 * @return Returns the medio_pago.
	 */
	public String getMedio_pago() {
		return medio_pago;
	}
	/**
	 * @param medio_pago The medio_pago to set.
	 */
	public void setMedio_pago(String medio_pago) {
		this.medio_pago = medio_pago;
	}
	/**
	 * @return Returns the meses_librpago.
	 */
	public int getMeses_librpago() {
		return meses_librpago;
	}
	/**
	 * @param meses_librpago The meses_librpago to set.
	 */
	public void setMeses_librpago(int meses_librpago) {
		this.meses_librpago = meses_librpago;
	}
	/**
	 * @return Returns the monto.
	 */
	public double getMonto() {
		return monto;
	}
	/**
	 * @param monto The monto to set.
	 */
	public void setMonto(double monto) {
		this.monto = monto;
	}
	/**
	 * @return Returns the n_cuotas.
	 */
	public int getN_cuotas() {
		return n_cuotas;
	}
	/**
	 * @param n_cuotas The n_cuotas to set.
	 */
	public void setN_cuotas(int n_cuotas) {
		this.n_cuotas = n_cuotas;
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
	 * @return Returns the nom_ejecutivo.
	 */
	public String getNom_ejecutivo() {
		return nom_ejecutivo;
	}
	/**
	 * @param nom_ejecutivo The nom_ejecutivo to set.
	 */
	public void setNom_ejecutivo(String nom_ejecutivo) {
		this.nom_ejecutivo = nom_ejecutivo;
	}
	/**
	 * @return Returns the nom_local.
	 */
	public String getNom_local() {
		return nom_local;
	}
	/**
	 * @param nom_local The nom_local to set.
	 */
	public void setNom_local(String nom_local) {
		this.nom_local = nom_local;
	}
	/**
	 * @return Returns the nom_motivo.
	 */
	public String getNom_motivo() {
		return nom_motivo;
	}
	/**
	 * @param nom_motivo The nom_motivo to set.
	 */
	public void setNom_motivo(String nom_motivo) {
		this.nom_motivo = nom_motivo;
	}
	/**
	 * @return Returns the nom_tbancaria.
	 */
	public String getNom_tbancaria() {
		return nom_tbancaria;
	}
	/**
	 * @param nom_tbancaria The nom_tbancaria to set.
	 */
	public void setNom_tbancaria(String nom_tbancaria) {
		this.nom_tbancaria = nom_tbancaria;
	}
	/**
	 * @return Returns the nom_tit.
	 */
	public String getNom_tit() {
		return nom_tit;
	}
	/**
	 * @param nom_tit The nom_tit to set.
	 */
	public void setNom_tit(String nom_tit) {
		this.nom_tit = nom_tit;
	}
	/**
	 * @return Returns the nom_usuario_bo.
	 */
	public String getNom_usuario_bo() {
		return nom_usuario_bo;
	}
	/**
	 * @param nom_usuario_bo The nom_usuario_bo to set.
	 */
	public void setNom_usuario_bo(String nom_usuario_bo) {
		this.nom_usuario_bo = nom_usuario_bo;
	}
	/**
	 * @return Returns the num_doc.
	 */
	public int getNum_doc() {
		return num_doc;
	}
	/**
	 * @param num_doc The num_doc to set.
	 */
	public void setNum_doc(int num_doc) {
		this.num_doc = num_doc;
	}
	/**
	 * @return Returns the num_mp.
	 */
	public String getNum_mp() {
		return num_mp;
	}
	/**
	 * @param num_mp The num_mp to set.
	 */
	public void setNum_mp(String num_mp) {
		this.num_mp = num_mp;
	}
	/**
	 * @return Returns the pol_id.
	 */
	public long getPol_id() {
		return pol_id;
	}
	/**
	 * @param pol_id The pol_id to set.
	 */
	public void setPol_id(long pol_id) {
		this.pol_id = pol_id;
	}
	/**
	 * @return Returns the pol_sustitucion.
	 */
	public String getPol_sustitucion() {
		return pol_sustitucion;
	}
	/**
	 * @param pol_sustitucion The pol_sustitucion to set.
	 */
	public void setPol_sustitucion(String pol_sustitucion) {
		this.pol_sustitucion = pol_sustitucion;
	}
	/**
	 * @return Returns the rut_cliente.
	 */
	public long getRut_cliente() {
		return rut_cliente;
	}
	/**
	 * @param rut_cliente The rut_cliente to set.
	 */
	public void setRut_cliente(long rut_cliente) {
		this.rut_cliente = rut_cliente;
	}
	/**
	 * @return Returns the rut_tit.
	 */
	public String getRut_tit() {
		return rut_tit;
	}
	/**
	 * @param rut_tit The rut_tit to set.
	 */
	public void setRut_tit(String rut_tit) {
		this.rut_tit = rut_tit;
	}
	/**
	 * @return Returns the sin_gente_op.
	 */
	public int getSin_gente_op() {
		return sin_gente_op;
	}
	/**
	 * @param sin_gente_op The sin_gente_op to set.
	 */
	public void setSin_gente_op(int sin_gente_op) {
		this.sin_gente_op = sin_gente_op;
	}
	/**
	 * @return Returns the sin_gente_txt.
	 */
	public String getSin_gente_txt() {
		return sin_gente_txt;
	}
	/**
	 * @param sin_gente_txt The sin_gente_txt to set.
	 */
	public void setSin_gente_txt(String sin_gente_txt) {
		this.sin_gente_txt = sin_gente_txt;
	}
	/**
	 * @return Returns the tb_banco.
	 */
	public String getTb_banco() {
		return tb_banco;
	}
	/**
	 * @param tb_banco The tb_banco to set.
	 */
	public void setTb_banco(String tb_banco) {
		this.tb_banco = tb_banco;
	}
	/**
	 * @return Returns the telefono.
	 */
	public String getTelefono() {
		return telefono;
	}
	/**
	 * @param telefono The telefono to set.
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	/**
	 * @return Returns the telefono2.
	 */
	public String getTelefono2() {
		return telefono2;
	}
	/**
	 * @param telefono2 The telefono2 to set.
	 */
	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}
	/**
	 * @return Returns the tipo_doc.
	 */
	public String getTipo_doc() {
		return tipo_doc;
	}
	/**
	 * @param tipo_doc The tipo_doc to set.
	 */
	public void setTipo_doc(String tipo_doc) {
		this.tipo_doc = tipo_doc;
	}


	public String getNum_mp_unmask() {
		return num_mp_unmask;
	}


	public void setNum_mp_unmask(String num_mp_unmask) {
		this.num_mp_unmask = num_mp_unmask;
	}


	public String getHfinpicking() {
		return hfinpicking;
	}


	public String getHpicking() {
		return hpicking;
	}


	public String getNom_comuna() {
		return nom_comuna;
	}


	public void setNom_comuna(String nom_comuna) {
		this.nom_comuna = nom_comuna;
	}


	public String getObservacion() {
		return observacion;
	}


	public void setObservacion(String observacion) {
		this.observacion = observacion;
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
     * @return Devuelve hingreso.
     */
    public String getHingreso() {
        return hingreso;
    }
    /**
     * @param hingreso El hingreso a establecer.
     */
    public void setHingreso(String hingreso) {
        this.hingreso = hingreso;
    }
    /**
     * @return Devuelve tipo_picking.
     */
    public String getTipo_picking() {
        return tipo_picking;
    }
    /**
     * @param tipo_picking El tipo_picking a establecer.
     */
    public void setTipo_picking(String tipo_picking) {
        this.tipo_picking = tipo_picking;
    }
    /**
     * @return Devuelve pedidoExt.
     */
    public PedidoExtDTO getPedidoExt() {
        return pedidoExt;
    }
    /**
     * @param pedidoExt El pedidoExt a establecer.
     */
    public void setPedidoExt(PedidoExtDTO pedidoExt) {
        this.pedidoExt = pedidoExt;
    }
    /**
     * @return Devuelve nomZona.
     */
    public String getNomZona() {
        return nomZona;
    }
    /**
     * @param nomZona El nomZona a establecer.
     */
    public void setNomZona(String nomZona) {
        this.nomZona = nomZona;
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
     * @return Devuelve tbk_secuencia_x.
     */
    public int getTbk_secuencia_x() {
        return tbk_secuencia_x;
    }
    /**
     * @return Devuelve tbk_secuencia_y.
     */
    public int getTbk_secuencia_y() {
        return tbk_secuencia_y;
    }
    /**
     * @param tbk_secuencia_x El tbk_secuencia_x a establecer.
     */
    public void setTbk_secuencia_x(int tbk_secuencia_x) {
        this.tbk_secuencia_x = tbk_secuencia_x;
    }
    /**
     * @param tbk_secuencia_y El tbk_secuencia_y a establecer.
     */
    public void setTbk_secuencia_y(int tbk_secuencia_y) {
        this.tbk_secuencia_y = tbk_secuencia_y;
    }
   
	/**
	 * @return Devuelve monto_reservado.
	 */
	public double getMonto_reservado() {
		return monto_reservado;
	}
	/**
	 * @param monto_reservado El monto_reservado a establecer.
	 */
	public void setMonto_reservado(double monto_reservado) {
		this.monto_reservado = monto_reservado;
	}
    /**
     * @return Devuelve secuenciaPago.
     */
    public String getSecuenciaPago() {
        return secuenciaPago;
    }
    /**
     * @param secuenciaPago El secuenciaPago a establecer.
     */
    public void setSecuenciaPago(String secuenciaPago) {
        this.secuenciaPago = secuenciaPago;
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
//(+) INDRA 2012-12-12 (+)

	/**
	 * @param porcentaje_monto El porcentaje_monto a establecer.
	 */
	public void setPorcentaje_monto(double porcentaje_monto) {
		this.porcentaje_monto = porcentaje_monto;
}

	/**
	 * @return Devuelve porcentaje_monto.
	 */
	public double getPorcentaje_monto() {
		return porcentaje_monto;
	}


	/**
	 * @param porcentaje_unidad El porcentaje_unidad a establecer.
	 */
	public void setPorcentaje_unidad(double porcentaje_unidad) {
		this.porcentaje_unidad = porcentaje_unidad;
	}


	/**
	 * @return Devuelve porcentaje_unidad.
	 */
	public double getPorcentaje_unidad() {
		return porcentaje_unidad;
	}
	//(-) INDRA (-)


	public FacturaDTO getFactura() {
		return factura;
	}


	public void setFactura(FacturaDTO factura) {
		this.factura = factura;
	}
		
}