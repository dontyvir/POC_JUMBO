package cl.bbr.irsmonitor.datos;

public class ProductoC1 {
	public static final int SKU_LEN        = 12;
	public static final int INDICAT_LEN    = 1;
	public static final int CANTIDAD_LEN   = 6;
	public static final int DECIMAL_LEN    = 3;
	public static final int PRECIO_LEN     = 8;

	private String sku;      //12 bytes
	private String indicat;  //1 byte
	private String cantidad; //6 bytes
	private String decimal;  //3 bytes
	private String precio;   //8 bytes
	
	
	public String getCantidad() {
		return cantidad;
	}
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	public String getDecimal() {
		return decimal;
	}
	public void setDecimal(String decimal) {
		this.decimal = decimal;
	}
	public String getIndicat() {
		return indicat;
	}
	public void setIndicat(String indicat) {
		this.indicat = indicat;
	}
	public String getPrecio() {
		return precio;
	}
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	
	/**
	 * Obtiene el largo del registro
	 * @return int largo
	 */
	public static int getLargo(){
		int largo = 0;
		
		largo += SKU_LEN;
		largo += INDICAT_LEN;
		largo += CANTIDAD_LEN;
		largo += DECIMAL_LEN;
		largo += PRECIO_LEN;
				
		return largo;
	}
	
}
