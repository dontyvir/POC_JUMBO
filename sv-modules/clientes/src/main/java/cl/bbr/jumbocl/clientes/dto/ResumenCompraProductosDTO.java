package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;

/**
 * DTO para datos de los productos del carro de compras. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class ResumenCompraProductosDTO implements Serializable {

	private long pro_id;
	private double cantidad;
	private String nombre;
	private String codigo;
	private String categoria;
	private double precio;
	private String nota = "";	
	private String marca;

	/**
	 * Constructor
	 */
	public ResumenCompraProductosDTO() {

	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public long getPro_id() {
		return pro_id;
	}

	public void setPro_id(long pro_id) {
		this.pro_id = pro_id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String caregoria) {
		this.categoria = caregoria;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}
}