package cl.bbr.promo.lib.dto;

/**
 * DTO para datos de los productos del catalogo externo del carro de compras. 
 * 
 * @author nsepulveda
 *
 */
public class ProductoCatalogoExternoDTO {
	
	/**
	 * Identificador del producto (String).
	 */
	private String id_producto_catalogo;
	
	/**
	 * Identificador del producto (long).
	 */
	private long longIdProducto;
	
	/**
	 * Cantidad asociada al producto del catalogo.
	 */
	private double cantidad_producto_catalogo;
	
	/**
	 * Identificador del local.
	 */
	private long longIdLocal;
	
	/**
	 * Nombre de producto del catalogo.
	 */
	private String nombre_producto_catalogo;
	
	/**
	 * Valor booleano que indica si tiene stock el producto.
	 */
	private boolean tieneStock;
	
	/**
	 * Cantidad maxima asociada al producto.
	 */
	private long cantidadMaxima;
	
	/**
	 * Mensaje del sistema asociado al producto en Local. 
	 */
	private String mensajeSistemaLocal;
	
	/**
	 * Mensaje del sistema asociado al producto en Stock. 
	 */
	private String mensajeSistemaStockLocal;
	
	/**
	 * Mensaje del sistema asociado a la cantidad maxima del producto. 
	 */
	private String mensajeSistemaCantMax;
	
	/**
	 * Valor booleano que indica si el producto se agrega al carro de compras.
	 */
	private boolean agregarCarro;
	
	/**
	 * Marca del producto.
	 */
	private String marca_producto;
	
	/**
	 * Flag que indica si existe producto, dado su ID,
	 * en la base de datos (1: Existe, 0: No existe).
	 */
	private int existeProducto;
	
	/**
	 * Categoria Padre.
	 */
	private long catPadre;
	
	/**
	 * Categoria Intermedia.
	 */
	private long catIntermedia;
	
	/**
	 * Categoria Terminal.
	 */
	private long catTerminal;
	
	/**
	 * Constructor de la clase.
	 */
	public ProductoCatalogoExternoDTO(){
		
	}

	public String getId_producto_catalogo() {
		return id_producto_catalogo;
	}

	public void setId_producto_catalogo(String id_producto_catalogo) {
		this.id_producto_catalogo = id_producto_catalogo;
	}

	public long getLongIdProducto() {
		return longIdProducto;
	}

	public void setLongIdProducto(long longIdProducto) {
		this.longIdProducto = longIdProducto;
	}

	public double getCantidad_producto_catalogo() {
		return cantidad_producto_catalogo;
	}

	public void setCantidad_producto_catalogo(double cantidad_producto_catalogo) {
		this.cantidad_producto_catalogo = cantidad_producto_catalogo;
	}

	public long getLongIdLocal() {
		return longIdLocal;
	}

	public void setLongIdLocal(long longIdLocal) {
		this.longIdLocal = longIdLocal;
	}

	public String getNombre_producto_catalogo() {
		return nombre_producto_catalogo;
	}

	public void setNombre_producto_catalogo(String nombre_producto_catalogo) {
		this.nombre_producto_catalogo = nombre_producto_catalogo;
	}

	public boolean isTieneStock() {
		return tieneStock;
	}

	public void setTieneStock(boolean tieneStock) {
		this.tieneStock = tieneStock;
	}

	public long getCantidadMaxima() {
		return cantidadMaxima;
	}

	public void setCantidadMaxima(long cantidadMaxima) {
		this.cantidadMaxima = cantidadMaxima;
	}

	public String getMensajeSistemaLocal() {
		return mensajeSistemaLocal;
	}

	public void setMensajeSistemaLocal(String mensajeSistemaLocal) {
		this.mensajeSistemaLocal = mensajeSistemaLocal;
	}

	public String getMensajeSistemaStockLocal() {
		return mensajeSistemaStockLocal;
	}

	public void setMensajeSistemaStockLocal(String mensajeSistemaStockLocal) {
		this.mensajeSistemaStockLocal = mensajeSistemaStockLocal;
	}

	public String getMensajeSistemaCantMax() {
		return mensajeSistemaCantMax;
	}

	public void setMensajeSistemaCantMax(String mensajeSistemaCantMax) {
		this.mensajeSistemaCantMax = mensajeSistemaCantMax;
	}

	public boolean isAgregarCarro() {
		return agregarCarro;
	}

	public void setAgregarCarro(boolean agregarCarro) {
		this.agregarCarro = agregarCarro;
	}

	public String getMarca_producto() {
		return marca_producto;
	}

	public void setMarca_producto(String marca_producto) {
		this.marca_producto = marca_producto;
	}

	public int getExisteProducto() {
		return existeProducto;
	}

	public void setExisteProducto(int existeProducto) {
		this.existeProducto = existeProducto;
	}

	public long getCatPadre() {
		return catPadre;
	}

	public void setCatPadre(long catPadre) {
		this.catPadre = catPadre;
	}

	public long getCatIntermedia() {
		return catIntermedia;
	}

	public void setCatIntermedia(long catIntermedia) {
		this.catIntermedia = catIntermedia;
	}

	public long getCatTerminal() {
		return catTerminal;
	}

	public void setCatTerminal(long catTerminal) {
		this.catTerminal = catTerminal;
	}

	public String toString() {
		return "ProductoCatalogoExternoDTO [id_producto_catalogo="
				+ id_producto_catalogo + ", longIdProducto=" + longIdProducto
				+ ", cantidad_producto_catalogo=" + cantidad_producto_catalogo
				+ ", longIdLocal=" + longIdLocal
				+ ", nombre_producto_catalogo=" + nombre_producto_catalogo
				+ ", tieneStock=" + tieneStock + ", cantidadMaxima="
				+ cantidadMaxima + ", mensajeSistemaLocal="
				+ mensajeSistemaLocal + ", mensajeSistemaStockLocal="
				+ mensajeSistemaStockLocal + ", mensajeSistemaCantMax="
				+ mensajeSistemaCantMax + ", agregarCarro=" + agregarCarro
				+ ", marca_producto=" + marca_producto + ", existeProducto="
				+ existeProducto + ", catPadre=" + catPadre
				+ ", catIntermedia=" + catIntermedia + ", catTerminal="
				+ catTerminal + "]";
	}
}