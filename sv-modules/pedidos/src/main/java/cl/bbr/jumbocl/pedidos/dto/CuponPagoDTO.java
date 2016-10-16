package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class CuponPagoDTO implements Serializable{
	
	private long id_trxmp;
	private double monto_trxmp;
	private long cant_prods;
	private long id_pedido;
	private String fdespacho;
	private String tipoDocPago;
	private String ventanaDespacho;
	private String nom_cliente;
	private String tipo_despacho;


	public String getNom_cliente() {
		return nom_cliente;
	}

	public void setNom_cliente(String nom_cliente) {
		this.nom_cliente = nom_cliente;
	}

	public CuponPagoDTO() {
	}

	public long getCant_prods() {
		return cant_prods;
	}

	public String getFdespacho() {
		return fdespacho;
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

	public String getTipoDocPago() {
		return tipoDocPago;
	}

	public String getVentanaDespacho() {
		return ventanaDespacho;
	}

	public void setCant_prods(long cant_prods) {
		this.cant_prods = cant_prods;
	}

	public void setFdespacho(String fdespacho) {
		this.fdespacho = fdespacho;
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

	public void setTipoDocPago(String tipoDocPago) {
		this.tipoDocPago = tipoDocPago;
	}

	public void setVentanaDespacho(String ventanaDespacho) {
		this.ventanaDespacho = ventanaDespacho;
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
}
