package cl.bbr.irsmonitor.datos;

public class ProductoC2 {
	public static final int SKU_LEN    = 12;
	public static final int RET_LEN    = 2;
	
	public static final int PROD_EMPAQUETADO_LEN    = 7;
	
	private String sku; //12 Bytes
	private String ret; //2 Bytes
	
	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
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
		largo += RET_LEN;
				
		return largo;
	}
	
}
