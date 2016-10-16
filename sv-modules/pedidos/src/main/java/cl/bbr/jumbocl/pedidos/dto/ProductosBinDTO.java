package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class ProductosBinDTO implements Serializable {

	private long   id_bin;
	private String cod_producto;
	private String descripcion;
	private double cantidad;
	private String cbarra;
	private String cod_prod_sap;
	private String uni_med;
	private String auditado;
	

	public ProductosBinDTO() {

	}

	
	public String getCod_prod_sap() {
		return cod_prod_sap;
	}


	public void setCod_prod_sap(String cod_prod_sap) {
		this.cod_prod_sap = cod_prod_sap;
	}


	public String getUni_med() {
		return uni_med;
	}


	public void setUni_med(String uni_med) {
		this.uni_med = uni_med;
	}


	public String getCbarra() {
		return cbarra;
	}

	public void setCbarra(String cbarra) {
		this.cbarra = cbarra;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public String getCod_producto() {
		return cod_producto;
	}

	public void setCod_producto(String cod_producto) {
		this.cod_producto = cod_producto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public long getId_bin() {
		return id_bin;
	}

	public void setId_bin(long id_bin) {
		this.id_bin = id_bin;
	}
		
    /**
     * @return Devuelve auditado.
     */
    public String getAuditado() {
        return auditado;
    }
    /**
     * @param auditado El auditado a establecer.
     */
    public void setAuditado(String auditado) {
        this.auditado = auditado;
    }
}
