package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;

/**
 * DTO para datos de los productos del carro de compras. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class CarroCompraDTO implements Serializable {

	private static final long serialVersionUID = -2765819752266385882L;
	
	private long id;
	private String pro_id;
	private double cantidad;
	private String nota;
    private boolean tieneNota;
	private String tipo_sel;
	private String nombre;
	private double precio;
	private double ppum;
	private String ppum_full;
	private long stock;
	private String unidad_tipo;
	private String unidad_nombre;
	private double inter_valor;
	private double inter_maximo;
	private String tipre;
	private String lugar_compra;
	private String tipo_producto;
	private String nom_marca;
	private String pesable;
	private long id_bo;
	private String catsap;
	private String codbarra;
    private String imagen;
    
    private String car_fec_crea;    
    private int id_rubro;
    
	/**
	 * Constructor
	 */
	public CarroCompraDTO() {
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = Math.abs(cantidad);
	}

	public String getPro_id() {
		return pro_id;
	}

	public void setPro_id(String pro_id) {
		this.pro_id = pro_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getNombre() {
		return nombre;
	}

	public void setTipoSel(String tipo_sel) {
		this.tipo_sel = tipo_sel;
	}

	public String getTipoSel() {
		return tipo_sel;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public long getStock() {
		return stock;
	}

	public void setStock(long stock) {
		this.stock = stock;
	}

	public String getUnidad_tipo() {
		return unidad_tipo;
	}

	public void setUnidad_tipo(String unidad_tipo) {
		this.unidad_tipo = unidad_tipo;
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

	public String getLugar_compra() {
		return lugar_compra;
	}

	public void setLugar_compra(String lugar_compra) {
		this.lugar_compra = lugar_compra;
	}

	public String getNom_marca() {
		return nom_marca;
	}

	public void setNom_marca(String nom_marca) {
		this.nom_marca = nom_marca;
	}

	public String getTipo_producto() {
		return tipo_producto;
	}

	public void setTipo_producto(String tipo_producto) {
		this.tipo_producto = tipo_producto;
	}

	
	/**
	 * @return Devuelve id_bo.
	 */
	public long getId_bo() {
		return id_bo;
	}
	/**
	 * @param id_bo El id_bo a establecer.
	 */
	public void setId_bo(long id_bo) {
		this.id_bo = id_bo;
	}
	/**
	 * @return Devuelve pesable.
	 */
	public String getPesable() {
		return pesable;
	}
	/**
	 * @param pesable El pesable a establecer.
	 */
	public void setPesable(String pesable) {
		this.pesable = pesable;
	}
	/**
	 * @return Devuelve catsap.
	 */
	public String getCatsap() {
		return catsap;
	}
	/**
	 * @param catsap El catsap a establecer.
	 */
	public void setCatsap(String catsap) {
		this.catsap = catsap;
	}
	/**
	 * @return Devuelve codbarra.
	 */
	public String getCodbarra() {
		return codbarra;
	}
	/**
	 * @param codbarra El codbarra a establecer.
	 */
	public void setCodbarra(String codbarra) {
		this.codbarra = codbarra;
	}

    /**
     * @return Devuelve tieneNota.
     */
    public boolean isTieneNota() {
        return tieneNota;
    }
    /**
     * @param tieneNota El tieneNota a establecer.
     */
    public void setTieneNota(boolean tieneNota) {
        this.tieneNota = tieneNota;
    }

    /**
     * @return Devuelve imagen.
     */
    public String getImagen() {
        return imagen;
    }
    /**
     * @param imagen El imagen a establecer.
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

	public int getId_rubro() {
		return id_rubro;
	}

	public void setId_rubro(int id_rubro) {
		this.id_rubro = id_rubro;
	}

	public String getCar_fec_crea() {
		return car_fec_crea;
	}

	public void setCar_fec_crea(String car_fec_crea) {
		this.car_fec_crea = car_fec_crea;
	}	
	
}