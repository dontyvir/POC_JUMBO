package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
import java.util.Date;

public class TrxMpDTO implements Serializable {
	
	private long 	id_trxmp;
	private long 	id_pedido;
	private long 	id_estado;
	private long 	id_local;
	private long 	id_cliente;
	private double 	costo_despacho;
	private Date	fcreacion;
	private double	monto_trxmp;
	private double	pos_monto_fp;
	private int    pos_num_caja;
	private int     pos_boleta;
	private int     pos_trx_sma;
	private String  pos_fecha;
	private String  pos_hora;
	private String  pos_fp;
	private int  	cant_productos;
	
	public TrxMpDTO() {
	}

	public int getCant_productos() {
		return cant_productos;
	}

	public double getCosto_despacho() {
		return costo_despacho;
	}

	public Date getFcreacion() {
		return fcreacion;
	}

	public long getId_cliente() {
		return id_cliente;
	}

	public long getId_estado() {
		return id_estado;
	}

	public long getId_local() {
		return id_local;
	}

	public long getId_pedido() {
		return id_pedido;
	}

	public long getId_trxmp() {
		return id_trxmp;
	}

	public double getMonto_trxmp() {
		return monto_trxmp;
	}

	public int getPos_boleta() {
		return pos_boleta;
	}

	public String getPos_fecha() {
		return pos_fecha;
	}

	public String getPos_fp() {
		return pos_fp;
	}

	public String getPos_hora() {
		return pos_hora;
	}

	public double getPos_monto_fp() {
		return pos_monto_fp;
	}

	public long getPos_num_caja() {
		return pos_num_caja;
	}

	public int getPos_trx_sma() {
		return pos_trx_sma;
	}

	public void setCant_productos(int cant_productos) {
		this.cant_productos = cant_productos;
	}

	public void setCosto_despacho(double costo_despacho) {
		this.costo_despacho = costo_despacho;
	}

	public void setFcreacion(Date fcreacion) {
		this.fcreacion = fcreacion;
	}

	public void setId_cliente(long id_cliente) {
		this.id_cliente = id_cliente;
	}

	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}

	public void setId_local(long id_local) {
		this.id_local = id_local;
	}

	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}

	public void setId_trxmp(long id_trxmp) {
		this.id_trxmp = id_trxmp;
	}

	public void setMonto_trxmp(double monto_trxmp) {
		this.monto_trxmp = monto_trxmp;
	}

	public void setPos_boleta(int pos_boleta) {
		this.pos_boleta = pos_boleta;
	}

	public void setPos_fecha(String pos_fecha) {
		this.pos_fecha = pos_fecha;
	}

	public void setPos_fp(String pos_fp) {
		this.pos_fp = pos_fp;
	}

	public void setPos_hora(String pos_hora) {
		this.pos_hora = pos_hora;
	}

	public void setPos_monto_fp(double pos_monto_fp) {
		this.pos_monto_fp = pos_monto_fp;
	}

	public void setPos_num_caja(int pos_num_caja) {
		this.pos_num_caja = pos_num_caja;
	}

	public void setPos_trx_sma(int pos_trx_sma) {
		this.pos_trx_sma = pos_trx_sma;
	}
	
	
	
	
	
}
