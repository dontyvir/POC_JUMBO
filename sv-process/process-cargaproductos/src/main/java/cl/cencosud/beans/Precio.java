package cl.cencosud.beans;

import java.io.Serializable;
import java.util.Date;
import org.apache.log4j.Logger;
import cl.cencosud.util.LogUtil;

/**
 * @author jdroguett
 * 
 *         Clase que representa el precio de un producto en un local determinado
 * 
 */
public class Precio implements Serializable {
	// (COD_LOCAL, COD_PROD1, PREC_FLEJE, COD_BARRA, UMEDIDA)
	private static final long serialVersionUID = 8881635809377891960L;
	
	static {
		LogUtil.initLog4J();
	}
	private static Logger logger = Logger.getLogger(Precio.class);
	
	private String codigoLocal;
	private String codigoProducto;
	private int precio;
	private int costoPromedio;
	private String codigoBarra;
	private String unidadMedida;

	/**
	 * Indica si el producto y su precio estám bloqueados para cierto local, es
	 * decir, no se puede vender. Este valos viene desde los archivos SAP.
	 */
	private String bloqueoCompra;
	private int idLocal;
	private int idProducto;
	private int idFoProducto;

	/**
	 * sirve para guardar en el archivo de precios que no cumplen limite de
	 * variación el precio actual para comparar
	 */
	private int precioActual;
	private String nombreArchivo;
	private Date fechaPrecioNuevo;

	/**
	 * Indica si el producto está publicado. N: Nuevo cargado de sap A:Publicado
	 * D: Despublicado E:Eliminado
	 */
	private String estado;
	
	/**
	 * Si el nuevo precio no sube más del 100% del actual ni diminuyen más del
	 * 80% del actual esta dentro del rango. Dicho de otra forma, el precio
	 * nuevo no es mayor que el 200% del actual (doble) ni menor que el 20% del
	 * actual.
	 * 
	 * @param nuevoPrecio
	 * @return true si el nuevo precio esta fuera de rango
	 */
	public boolean isFueraDeRango(Precio nuevoPrecio) {		
		float porcentaje = this.getPrecio() == 0 ? 100.0f : 100.0f * nuevoPrecio.getPrecio() / this.getPrecio();
		
		//logger.debug("FueraDeRango, nuevoPrecio:"+nuevoPrecio.getPrecio()+ ", Precio db:"+this.getPrecio()+", porcentaje:"+porcentaje);
		
		// if (porcentaje >= 20.0f && porcentaje <= 200.0f) {
		if (porcentaje >= 20.0f && porcentaje <= 1000.0f) {
			return false;
		}
		return true;
	}
	
	public boolean isFueraDeRango(int newPrecio, int oldPrecio) {		
		double porcentaje, diferencia;
		String msg = "";
		try{
			diferencia = newPrecio-oldPrecio;
			porcentaje = (Math.abs(diferencia) / oldPrecio) * 100;						
			if(diferencia>=0){	
				msg = "Aumento de Precio";			
			}else{
				msg = "Disminucion de Precio";					
			}
			logger.debug(msg + " - precio nuevo:"+newPrecio+" y antiguo:"+oldPrecio +", diferencia es:"+Math.abs(diferencia) + " y porcentaje:"+porcentaje+"%");
			
			if (porcentaje >= 20.0f && porcentaje <= 200.0f) {			
				return false;
			}	
			
		}catch(Exception ex){
			logger.error("ERROR, calcular porcentaje entre dos precios, nuevo:"+newPrecio+" y antiguo:"+oldPrecio+", "+ex);
		}
		return true;
	}
	
	public String getCodigoBarra() {
		return codigoBarra;
	}
	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public String getCodigoLocal() {
		return codigoLocal;
	}
	public void setCodigoLocal(String codigoLocal) {
		this.codigoLocal = codigoLocal;
	}

	public String getCodigoProducto() {
		return codigoProducto;
	}
	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public int getPrecio() {
		return precio;
	}
	public void setPrecio(int precio) {
		this.precio = precio;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}
	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public int getIdLocal() {
		return idLocal;
	}
	public void setIdLocal(int idLocal) {
		this.idLocal = idLocal;
	}

	public int getIdProducto() {
		return idProducto;
	}
	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}

	public int getPrecioActual() {
		return precioActual;
	}
	public void setPrecioActual(int precioActual) {
		this.precioActual = precioActual;
	}

	public String getBloqueoCompra() {
		return bloqueoCompra;
	}
	public void setBloqueoCompra(String bloqueoCompra) {
		this.bloqueoCompra = bloqueoCompra;
	}

	public Date getFechaPrecioNuevo() {
		return fechaPrecioNuevo;
	}
	public void setFechaPrecioNuevo(Date fechaPrecioNuevo) {
		this.fechaPrecioNuevo = fechaPrecioNuevo;
	}

	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getCostoPromedio() {
		return costoPromedio;
	}
	public void setCostoPromedio(int costo) {
		this.costoPromedio = costo;
	}

	public int getIdFoProducto() {
		return idFoProducto;
	}
	public void setIdFoProducto(int idFoProducto) {
		this.idFoProducto = idFoProducto;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
}