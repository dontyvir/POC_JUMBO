package cl.bbr.jumbocl.pedidos.promos.dto;

import java.util.ArrayList;
import java.util.List;

public class ProductosRelacionadosPromoDTO {
	private long id_producto;
	private long id_detalle;
	private String descripcion;
	private double precio_lista; 
	private double cantidad;
	private String Ean13;
	private long prorrateo;
	
	private List promoDetallePedido=new ArrayList();
	
	public ProductosRelacionadosPromoDTO() {
	}

	
	/**
	 * @return Returns the cantidad.
	 */
	public double getCantidad() {
		return cantidad;
	}


	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}


	/**
	 * @return Returns the precio_lista.
	 */
	public double getPrecio_lista() {
		return precio_lista;
	}

	/**
	 * @param precio_lista The precio_lista to set.
	 */
	public void setPrecio_lista(double precio_lista) {
		this.precio_lista = precio_lista;
	}

	/**
	 * @return Returns the id_detalle.
	 */
	public long getId_detalle() {
		return id_detalle;
	}

	/**
	 * @param id_detalle The id_detalle to set.
	 */
	public void setId_detalle(long id_detalle) {
		this.id_detalle = id_detalle;
	}

	/**
	 * @return Returns the prorrateo.
	 */
	public long getProrrateo() {
		return prorrateo;
	}

	/**
	 * @param prorrateo The prorrateo to set.
	 */
	public void setProrrateo(long prorrateo) {
		this.prorrateo = prorrateo;
	}

	/**
	 * @return Returns the ean13.
	 */
	public String getEan13() {
		return Ean13;
	}

	/**
	 * @param ean13 The ean13 to set.
	 */
	public void setEan13(String ean13) {
		Ean13 = ean13;
	}

	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return Returns the id_producto.
	 */
	public long getId_producto() {
		return id_producto;
	}

	/**
	 * @param id_producto The id_producto to set.
	 */
	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}
    public List getPromoDetallePedido() {
        return promoDetallePedido;
    }
    public void setPromoDetallePedido(List promoDetallePedido) {
        this.promoDetallePedido = promoDetallePedido;
    }
}
