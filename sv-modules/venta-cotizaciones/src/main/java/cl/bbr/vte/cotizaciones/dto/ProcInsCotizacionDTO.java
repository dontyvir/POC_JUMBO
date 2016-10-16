package cl.bbr.vte.cotizaciones.dto;

import java.util.List;

/**
 * Clase que contiene información de la cotización
 * 
 * @author BBR
 *
 */
public class ProcInsCotizacionDTO {

	/**
	 * Identificador único de la empresa
	 */
	private long 	id_empresa;
	/**
	 * Identificador único de la sucursal
	 */
	private long 	id_sucursal;
	/**
	 * Identificador único de la dirección de despacho
	 */
	private long 	id_dir_desp;
	/**
	 * Idenfificador único de la dirección de facturación
	 */
	private long 	id_dir_fac;
	/**
	 * Identificador único del comprador
	 */
	private long 	id_comprador;
	/**
	 * Tipo de comprador
	 */
	private String 	tipo_cpr;
	/**
	 * Fecha de ingreso de la cotización
	 */
	private String 	fec_ingreso;
	/**
	 * Fecha de despacho de la cotización
	 */
	private String 	fec_acordada;
	/**
	 * Monto total de la cotización
	 */
	private double 	monto_total;
	/**
	 * Costo de despacho de la cotización
	 */
	private double 	costo_desp;
	/**
	 * Observación de la cotización
	 */
	private String 	obs;
	/**
	 * Estado de la cotización
	 */
	private int 	estado;
	/**
	 * Fecha de vencimiento de la cotización 
	 */
	private String 	fec_vencimiento;
	/**
	 * Observación para los productos fuera de mix
	 */
	private String 	fueramix;
	/**
	 * Tipo de documento
	 */
	private String 	tipo_doc;
	/**
	 * Detalle de la cotización
	 */
	private List 	lst_detalle;
	/**
	 * Usuario fonocompras que ingresa la cotización
	 */
	private int 	cot_user_fono_id;
	/**
	 * Identificador único de la cotización
	 */
	private long 	cot_id;

	/**
	 * Descripción de la Política de Sustitucion
	 */
	private String 	sustitucion;
	/**
	 * Identificador único de la Política de Sustitución 
	 */
	private long 	id_sustitucion;
	/**
	 * Persona autorizada a recibir el pedido
	 */
	private String 	sgente_txt;
	/**
	 * Identificador de la opción
	 */
	private long 	sgente_op;

	/**
	 * Medio de pago
	 */
	private String 	medio_pago;	
	/**
	 * Nombre del banco del medio de pago
	 */
	private String 	nombre_banco;	
	/**
	 * Nombre de la tarjeta bancaria
	 */
	private String 	tbk_nombre_tarjeta;
	/**
	 * Fecha de expiración del medio de pago
	 */
	private String 	tbk_fec_expira;	
	/**
	 * Número de cuotas del medio de pago
	 */
	private int 	numero_cuotas;	
	/**
	 * Númeror de tarjeta para el medio de pago 
	 */
	private String 	numero_tarjeta;
	/**
	 * clave del medio de pago --> No utilizado
	 */
	private String 	medio_pago_clave;


	/**
	 * @return Devuelve costo_desp.
	 */
	public double getCosto_desp() {
		return costo_desp;
	}
	/**
	 * @param costo_desp El costo_desp a establecer.
	 */
	public void setCosto_desp(double costo_desp) {
		this.costo_desp = costo_desp;
	}
	/**
	 * @return Devuelve cot_id.
	 */
	public long getCot_id() {
		return cot_id;
	}
	/**
	 * @param cot_id El cot_id a establecer.
	 */
	public void setCot_id(long cot_id) {
		this.cot_id = cot_id;
	}
	/**
	 * @return Devuelve cot_usr_id.
	 */
	public int getCot_user_fono_id() {
		return cot_user_fono_id;
	}
	/**
	 * @param cot_usr_id El cot_usr_id a establecer.
	 */
	public void setCot_user_fono_id(int cot_user_fono_id) {
		this.cot_user_fono_id = cot_user_fono_id;
	}
	/**
	 * @return Devuelve estado.
	 */
	public int getEstado() {
		return estado;
	}
	/**
	 * @param estado El estado a establecer.
	 */
	public void setEstado(int estado) {
		this.estado = estado;
	}
	/**
	 * @return Devuelve fec_acordada.
	 */
	public String getFec_acordada() {
		return fec_acordada;
	}
	/**
	 * @param fec_acordada El fec_acordada a establecer.
	 */
	public void setFec_acordada(String fec_acordada) {
		this.fec_acordada = fec_acordada;
	}
	/**
	 * @return Devuelve fec_ingreso.
	 */
	public String getFec_ingreso() {
		return fec_ingreso;
	}
	/**
	 * @param fec_ingreso El fec_ingreso a establecer.
	 */
	public void setFec_ingreso(String fec_ingreso) {
		this.fec_ingreso = fec_ingreso;
	}
	/**
	 * @return Devuelve fec_vencimiento.
	 */
	public String getFec_vencimiento() {
		return fec_vencimiento;
	}
	/**
	 * @param fec_vencimiento El fec_vencimiento a establecer.
	 */
	public void setFec_vencimiento(String fec_vencimiento) {
		this.fec_vencimiento = fec_vencimiento;
	}
	/**
	 * @return Devuelve fueramix.
	 */
	public String getFueramix() {
		return fueramix;
	}
	/**
	 * @param fueramix El fueramix a establecer.
	 */
	public void setFueramix(String fueramix) {
		this.fueramix = fueramix;
	}
	/**
	 * @return Devuelve id_comprador.
	 */
	public long getId_comprador() {
		return id_comprador;
	}
	/**
	 * @param id_comprador El id_comprador a establecer.
	 */
	public void setId_comprador(long id_comprador) {
		this.id_comprador = id_comprador;
	}
	/**
	 * @return Devuelve id_dir_desp.
	 */
	public long getId_dir_desp() {
		return id_dir_desp;
	}
	/**
	 * @param id_dir_desp El id_dir_desp a establecer.
	 */
	public void setId_dir_desp(long id_dir_desp) {
		this.id_dir_desp = id_dir_desp;
	}
	/**
	 * @return Devuelve id_dir_fac.
	 */
	public long getId_dir_fac() {
		return id_dir_fac;
	}
	/**
	 * @param id_dir_fac El id_dir_fac a establecer.
	 */
	public void setId_dir_fac(long id_dir_fac) {
		this.id_dir_fac = id_dir_fac;
	}
	/**
	 * @return Devuelve id_empresa.
	 */
	public long getId_empresa() {
		return id_empresa;
	}
	/**
	 * @param id_empresa El id_empresa a establecer.
	 */
	public void setId_empresa(long id_empresa) {
		this.id_empresa = id_empresa;
	}
	/**
	 * @return Devuelve id_sucursal.
	 */
	public long getId_sucursal() {
		return id_sucursal;
	}
	/**
	 * @param id_sucursal El id_sucursal a establecer.
	 */
	public void setId_sucursal(long id_sucursal) {
		this.id_sucursal = id_sucursal;
	}
	/**
	 * @return Devuelve id_sustitucion.
	 */
	public long getId_sustitucion() {
		return id_sustitucion;
	}
	/**
	 * @param id_sustitucion El id_sustitucion a establecer.
	 */
	public void setId_sustitucion(long id_sustitucion) {
		this.id_sustitucion = id_sustitucion;
	}
	/**
	 * @return Devuelve lst_detalle.
	 */
	public List getLst_detalle() {
		return lst_detalle;
	}
	/**
	 * @param lst_detalle El lst_detalle a establecer.
	 */
	public void setLst_detalle(List lst_detalle) {
		this.lst_detalle = lst_detalle;
	}
	/**
	 * @return Devuelve medio_pago.
	 */
	public String getMedio_pago() {
		return medio_pago;
	}
	/**
	 * @param medio_pago El medio_pago a establecer.
	 */
	public void setMedio_pago(String medio_pago) {
		this.medio_pago = medio_pago;
	}
	/**
	 * @return Devuelve medio_pago_clave.
	 */
	public String getMedio_pago_clave() {
		return medio_pago_clave;
	}
	/**
	 * @param medio_pago_clave El medio_pago_clave a establecer.
	 */
	public void setMedio_pago_clave(String medio_pago_clave) {
		this.medio_pago_clave = medio_pago_clave;
	}
	/**
	 * @return Devuelve monto_total.
	 */
	public double getMonto_total() {
		return monto_total;
	}
	/**
	 * @param monto_total El monto_total a establecer.
	 */
	public void setMonto_total(double monto_total) {
		this.monto_total = monto_total;
	}
	/**
	 * @return Devuelve nombre_banco.
	 */
	public String getNombre_banco() {
		return nombre_banco;
	}
	/**
	 * @param nombre_banco El nombre_banco a establecer.
	 */
	public void setNombre_banco(String nombre_banco) {
		this.nombre_banco = nombre_banco;
	}
	/**
	 * @return Devuelve numero_cuotas.
	 */
	public int getNumero_cuotas() {
		return numero_cuotas;
	}
	/**
	 * @param numero_cuotas El numero_cuotas a establecer.
	 */
	public void setNumero_cuotas(int numero_cuotas) {
		this.numero_cuotas = numero_cuotas;
	}
	/**
	 * @return Devuelve numero_tarjeta.
	 */
	public String getNumero_tarjeta() {
		return numero_tarjeta;
	}
	/**
	 * @param numero_tarjeta El numero_tarjeta a establecer.
	 */
	public void setNumero_tarjeta(String numero_tarjeta) {
		this.numero_tarjeta = numero_tarjeta;
	}
	/**
	 * @return Devuelve obs.
	 */
	public String getObs() {
		return obs;
	}
	/**
	 * @param obs El obs a establecer.
	 */
	public void setObs(String obs) {
		this.obs = obs;
	}
	/**
	 * @return Devuelve sgente_op.
	 */
	public long getSgente_op() {
		return sgente_op;
	}
	/**
	 * @param sgente_op El sgente_op a establecer.
	 */
	public void setSgente_op(long sgente_op) {
		this.sgente_op = sgente_op;
	}
	/**
	 * @return Devuelve sgente_txt.
	 */
	public String getSgente_txt() {
		return sgente_txt;
	}
	/**
	 * @param sgente_txt El sgente_txt a establecer.
	 */
	public void setSgente_txt(String sgente_txt) {
		this.sgente_txt = sgente_txt;
	}
	/**
	 * @return Devuelve sustitucion.
	 */
	public String getSustitucion() {
		return sustitucion;
	}
	/**
	 * @param sustitucion El sustitucion a establecer.
	 */
	public void setSustitucion(String sustitucion) {
		this.sustitucion = sustitucion;
	}
	/**
	 * @return Devuelve tbk_fec_expira.
	 */
	public String getTbk_fec_expira() {
		return tbk_fec_expira;
	}
	/**
	 * @param tbk_fec_expira El tbk_fec_expira a establecer.
	 */
	public void setTbk_fec_expira(String tbk_fec_expira) {
		this.tbk_fec_expira = tbk_fec_expira;
	}
	/**
	 * @return Devuelve tbk_nombre_tarjeta.
	 */
	public String getTbk_nombre_tarjeta() {
		return tbk_nombre_tarjeta;
	}
	/**
	 * @param tbk_nombre_tarjeta El tbk_nombre_tarjeta a establecer.
	 */
	public void setTbk_nombre_tarjeta(String tbk_nombre_tarjeta) {
		this.tbk_nombre_tarjeta = tbk_nombre_tarjeta;
	}
	/**
	 * @return Devuelve tipo_cpr.
	 */
	public String getTipo_cpr() {
		return tipo_cpr;
	}
	/**
	 * @param tipo_cpr El tipo_cpr a establecer.
	 */
	public void setTipo_cpr(String tipo_cpr) {
		this.tipo_cpr = tipo_cpr;
	}
	/**
	 * @return Devuelve tipo_doc.
	 */
	public String getTipo_doc() {
		return tipo_doc;
	}
	/**
	 * @param tipo_doc El tipo_doc a establecer.
	 */
	public void setTipo_doc(String tipo_doc) {
		this.tipo_doc = tipo_doc;
	}

}
