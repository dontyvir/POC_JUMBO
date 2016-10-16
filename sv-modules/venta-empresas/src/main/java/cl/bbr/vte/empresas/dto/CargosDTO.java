package cl.bbr.vte.empresas.dto;

import java.io.Serializable;

/**
 * DTO para datos de los cargos. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class CargosDTO implements Serializable{
	private long 	id_cargo;
	private long 	id_empresa;
	private long 	id_pedido;
	private double 	monto_cargo;
	private String	fecha_ing;
	/**
	 * @return Returns the fecha_ing.
	 */
	public String getFecha_ing() {
		return fecha_ing;
	}
	/**
	 * @param fecha_ing The fecha_ing to set.
	 */
	public void setFecha_ing(String fecha_ing) {
		this.fecha_ing = fecha_ing;
	}
	/**
	 * @return Returns the id_cargo.
	 */
	public long getId_cargo() {
		return id_cargo;
	}
	/**
	 * @param id_cargo The id_cargo to set.
	 */
	public void setId_cargo(long id_cargo) {
		this.id_cargo = id_cargo;
	}
	/**
	 * @return Returns the id_empresa.
	 */
	public long getId_empresa() {
		return id_empresa;
	}
	/**
	 * @param id_empresa The id_empresa to set.
	 */
	public void setId_empresa(long id_empresa) {
		this.id_empresa = id_empresa;
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
	 * @return Returns the monto_cargo.
	 */
	public double getMonto_cargo() {
		return monto_cargo;
	}
	/**
	 * @param monto_cargo The monto_cargo to set.
	 */
	public void setMonto_cargo(double monto_cargo) {
		this.monto_cargo = monto_cargo;
	}

	
}
