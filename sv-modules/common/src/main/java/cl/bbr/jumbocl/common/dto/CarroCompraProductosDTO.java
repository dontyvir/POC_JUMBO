package cl.bbr.jumbocl.common.dto;

import java.io.Serializable;

/**
 * DTO para datos de los productos del carro de compras. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class CarroCompraProductosDTO implements Serializable {

	private long pro_id;
	private double cantidad;
	private String nombre;
	private String codigo;
	private boolean con_nota;
	private boolean en_carro;
	private String categoria;
	private double ppum;
	private String ppum_full;
	private double precio;
	private String unidad_nombre;
	private String unidad_tipo;
	private String unidadMedida;
	private double inter_valor;
	private double inter_maximo;
	private long stock;
	private long car_id;
	private String nota = "";	
	private String tipre;
	private String marca;
	private long idDetalle;
	private String ean13;
	private String descripcion;

	
	

	/**
	 * Constructor
	 */
	public CarroCompraProductosDTO() {

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

	public boolean isCon_nota() {
		return con_nota;
	}

	public void setCon_nota(boolean con_nota) {
		this.con_nota = con_nota;
	}

	public boolean isEn_carro() {
		return en_carro;
	}

	public void setEn_carro(boolean en_carro) {
		this.en_carro = en_carro;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String caregoria) {
		this.categoria = caregoria;
	}

	public double getInter_maximo() {
		return inter_maximo;
	}

	public void setInter_maximo(double inter_maximo) {
		this.inter_maximo = inter_maximo;
	}

	public double getInter_valor() {
		return inter_valor;
	}

	public void setInter_valor(double inter_valor) {
		this.inter_valor = inter_valor;
	}

	public String getUnidad_tipo() {
		return unidad_tipo;
	}

	public void setUnidad_tipo(String unidad_tipo) {
		this.unidad_tipo = unidad_tipo;
	}

	public long getStock() {
		return stock;
	}

	public void setStock(long stock) {
		this.stock = stock;
	}

	public long getCar_id() {
		return car_id;
	}

	public void setCar_id(long car_id) {
		this.car_id = car_id;
	}

	public String getUnidad_nombre() {
		return unidad_nombre;
	}

	public void setUnidad_nombre(String unidad_nombre) {
		this.unidad_nombre = unidad_nombre;
	}

	public double getPpum() {
		return ppum;
	}

	public void setPpum(double ppum) {
		this.ppum = ppum;
	}

	public String getPpum_full() {
		return ppum_full;
	}

	public void setPpum_full(String ppum_full) {
		this.ppum_full = ppum_full;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getTipre() {
		return tipre;
	}

	public void setTipre(String tipre) {
		this.tipre = tipre;
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
    /**
     * @return Devuelve unidadMedida.
     */
    public String getUnidadMedida() {
        return unidadMedida;
    }
    /**
     * @param unidadMedida El unidadMedida a establecer.
     */
    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
    public long getIdDetalle() {
		return idDetalle;
	}

	public void setIdDetalle(long idDetalle) {
		this.idDetalle = idDetalle;
	}

	public String getEan13() {
		return ean13;
	}

	public void setEan13(String ean13) {
		this.ean13 = ean13;
	}
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}