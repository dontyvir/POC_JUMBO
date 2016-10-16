package cl.bbr.jumbocl.common.model;

import java.sql.Date;

/**
 * Clase que captura desde la base de datos los datos del encabezado de una transacción de pago
 * @author bbr
 *
 */
public class TrxMpEncabezadoEntity {
	long id_trxmp;
	long id_pedido;
	long id_estado;
	long id_local;
	long id_cliente;
	double costo_despacho;
	Date fcreacion;
	double monto_trxmp;
	double pos_monto_fp;
	int pos_num_caja;
	int pos_boleta;
	int pos_trx_sma;
	String pos_fecha;
	String pos_hora;
	String pos_fp;
	int cant_productos;
	
	/**
	 * Constructor 
	 */
	public TrxMpEncabezadoEntity() {
	}

	/**
	 * @return cant_productos
	 */
	public int getCant_productos() {
		return cant_productos;
	}

	/**
	 * @return costo_despacho
	 */
	public double getCosto_despacho() {
		return costo_despacho;
	}

	/**
	 * @return fcreacion
	 */
	public Date getFcreacion() {
		return fcreacion;
	}

	/**
	 * @return id_cliente
	 */
	public long getId_cliente() {
		return id_cliente;
	}

	/**
	 * @return id_estado
	 */
	public long getId_estado() {
		return id_estado;
	}

	/**
	 * @return id_local
	 */
	public long getId_local() {
		return id_local;
	}

	/**
	 * @return id_pedido
	 */
	public long getId_pedido() {
		return id_pedido;
	}

	/**
	 * @return id_trxmp
	 */
	public long getId_trxmp() {
		return id_trxmp;
	}

	/**
	 * @return monto_trxmp
	 */
	public double getMonto_trxmp() {
		return monto_trxmp;
	}

	/**
	 * @return pos_boleta
	 */
	public int getPos_boleta() {
		return pos_boleta;
	}

	/**
	 * @return pos_fecha
	 */
	public String getPos_fecha() {
		return pos_fecha;
	}

	/**
	 * @return pos_fp
	 */
	public String getPos_fp() {
		return pos_fp;
	}

	/**
	 * @return pos_hora
	 */
	public String getPos_hora() {
		return pos_hora;
	}

	/**
	 * @return pos_monto_fp
	 */
	public double getPos_monto_fp() {
		return pos_monto_fp;
	}

	/**
	 * @return pos_num_caja
	 */
	public int getPos_num_caja() {
		return pos_num_caja;
	}

	/**
	 * @return pos_trx_sma
	 */
	public int getPos_trx_sma() {
		return pos_trx_sma;
	}

	/**
	 * @param cant_productos
	 */
	public void setCant_productos(int cant_productos) {
		this.cant_productos = cant_productos;
	}

	/**
	 * @param costo_despacho
	 */
	public void setCosto_despacho(double costo_despacho) {
		this.costo_despacho = costo_despacho;
	}

	/**
	 * @param fcreacion
	 */
	public void setFcreacion(Date fcreacion) {
		this.fcreacion = fcreacion;
	}

	/**
	 * @param id_cliente
	 */
	public void setId_cliente(long id_cliente) {
		this.id_cliente = id_cliente;
	}

	/**
	 * @param id_estado
	 */
	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}

	/**
	 * @param id_local
	 */
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}

	/**
	 * @param id_pedido
	 */
	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}

	/**
	 * @param id_trxmp
	 */
	public void setId_trxmp(long id_trxmp) {
		this.id_trxmp = id_trxmp;
	}

	/**
	 * @param monto_trxmp
	 */
	public void setMonto_trxmp(double monto_trxmp) {
		this.monto_trxmp = monto_trxmp;
	}

	/**
	 * @param pos_boleta
	 */
	public void setPos_boleta(int pos_boleta) {
		this.pos_boleta = pos_boleta;
	}

	/**
	 * @param pos_fecha
	 */
	public void setPos_fecha(String pos_fecha) {
		this.pos_fecha = pos_fecha;
	}

	/**
	 * @param pos_fp
	 */
	public void setPos_fp(String pos_fp) {
		this.pos_fp = pos_fp;
	}

	/**
	 * @param pos_hora
	 */
	public void setPos_hora(String pos_hora) {
		this.pos_hora = pos_hora;
	}

	/**
	 * @param pos_monto_fp
	 */
	public void setPos_monto_fp(double pos_monto_fp) {
		this.pos_monto_fp = pos_monto_fp;
	}

	/**
	 * @param pos_num_caja
	 */
	public void setPos_num_caja(int pos_num_caja) {
		this.pos_num_caja = pos_num_caja;
	}

	/**
	 * @param pos_trx_sma
	 */
	public void setPos_trx_sma(int pos_trx_sma) {
		this.pos_trx_sma = pos_trx_sma;
	}
	
	
}
