package cl.bbr.vte.cotizaciones.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO para datos de los productos. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class ProductoDTO implements Serializable {

	private long pro_id;
	private long dcot_id;
	private long pro_id_bo;
	private String tipo_producto;
	private String descripcion;
	private double ppum;
	private String ppum_full;
	private double precio;
	private String unidad_nombre;
	private String unidad_tipo;
	private double inter_valor;
	private double inter_maximo;
	private long stock;
	private String img_chica;
	private String img_grande;
	private String marca = "";
	private long marca_id;
	private boolean con_nota;
	private boolean en_carro;
	private double cantidad;
	private String generico;
	private String valor_diferenciador;
	private String tipre;
	private List ProductosDTO = null;
	private String nota;
	private String codigo;
	
    /**
     * @return Devuelve dcot_id.
     */
    public long getDcot_id() {
        return dcot_id;
    }
    /**
     * @param dcot_id El dcot_id a establecer.
     */
    public void setDcot_id(long dcot_id) {
        this.dcot_id = dcot_id;
    }
	/**
	 * Constructor
	 */
	public ProductoDTO() {

	}
	
	public String toString() {
		String result = "";
		result += " pro_id: " + this.pro_id;
		result += " tipo_producto: " + this.tipo_producto;
		result += " descripcion: " + this.descripcion;
		result += " ppum: " + this.ppum;
		
		return result;
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

	public String getImg_chica() {
		return img_chica;
	}

	public void setImg_chica(String img_chica) {
		this.img_chica = img_chica;
	}

	public String getImg_grande() {
		return img_grande;
	}

	public void setImg_grande(String img_grande) {
		this.img_grande = img_grande;
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

	public long getPro_id() {
		return pro_id;
	}

	public void setPro_id(long pro_id) {
		this.pro_id = pro_id;
	}

	public List getProductosDTO() {
		return ProductosDTO;
	}

	public void setProductosDTO(List productosDTO) {
		ProductosDTO = productosDTO;
	}

	public long getStock() {
		return stock;
	}

	public void setStock(long stock) {
		this.stock = stock;
	}

	public String getUnidad_nombre() {
		return unidad_nombre;
	}

	public void setUnidad_nombre(String unidad_nombre) {
		this.unidad_nombre = unidad_nombre;
	}

	public String getUnidad_tipo() {
		return unidad_tipo;
	}

	public void setUnidad_tipo(String unidad_tipo) {
		this.unidad_tipo = unidad_tipo;
	}

	public String getTipo_producto() {
		return tipo_producto;
	}

	public void setTipo_producto(String tipo_producto) {
		this.tipo_producto = tipo_producto;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public String getGenerico() {
		return generico;
	}

	public void setGenerico(String generico) {
		this.generico = generico;
	}

	public String getValor_diferenciador() {
		return valor_diferenciador;
	}

	public void setValor_diferenciador(String valor_diferenciador) {
		this.valor_diferenciador = valor_diferenciador;
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

	public String getTipre() {
		return tipre;
	}

	public void setTipre(String tipre) {
		this.tipre = tipre;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public long getMarca_id() {
		return marca_id;
	}

	public void setMarca_id(long marca_id) {
		this.marca_id = marca_id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	/**
	 * @return Devuelve pro_id_bo.
	 */
	public long getPro_id_bo() {
		return pro_id_bo;
	}
	/**
	 * @param pro_id_bo El pro_id_bo a establecer.
	 */
	public void setPro_id_bo(long pro_id_bo) {
		this.pro_id_bo = pro_id_bo;
	}
}