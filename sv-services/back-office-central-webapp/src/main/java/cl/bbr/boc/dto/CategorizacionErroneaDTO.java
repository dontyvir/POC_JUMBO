package cl.bbr.boc.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Fecha de creación: 27/10/2011
 *
 * Clase de serializacion que contiene la información para el reporte de categorizacion erronea
 */
public class CategorizacionErroneaDTO implements Serializable {

	/**
	 * indica la cantidad de filas que fueron procesadas del archivo excel
	 */
	private long cantidadFilasProcesadas;
	
	/**
	 * indica la cantidad de filas cargadas correctamente del archivo
	 */
	private long cantidadCargada;
	
	/**
	 * indica la cantidad de filas fallidas del archivo excel
	 */
	private long cantidadFallida;
	
	/**
	 * lista que contiene el detalle de los datos erroneos a mostrar en el reporte
	 */
	private ArrayList detalle = new ArrayList();
	
	/**
	 * @return Devuelve cantidadCargada.
	 */
	public long getCantidadCargada() {
		return cantidadCargada;
	}
	/**
	 * @param cantidadCargada El cantidadCargada a establecer.
	 */
	public void setCantidadCargada(long cantidadCargada) {
		this.cantidadCargada = cantidadCargada;
	}
	/**
	 * @return Devuelve cantidadFallida.
	 */
	public long getCantidadFallida() {
		return cantidadFallida;
	}
	/**
	 * @param cantidadFallida El cantidadFallida a establecer.
	 */
	public void setCantidadFallida(long cantidadFallida) {
		this.cantidadFallida = cantidadFallida;
	}
	/**
	 * @return Devuelve cantidadFilasProcesadas.
	 */
	public long getCantidadFilasProcesadas() {
		return cantidadFilasProcesadas;
	}
	/**
	 * @param cantidadFilasProcesadas El cantidadFilasProcesadas a establecer.
	 */
	public void setCantidadFilasProcesadas(long cantidadFilasProcesadas) {
		this.cantidadFilasProcesadas = cantidadFilasProcesadas;
	}
	/**
	 * @return Devuelve detalle.
	 */
	public ArrayList getDetalle() {
		return detalle;
	}
	/**
	 * @param detalle El detalle a establecer.
	 */
	public void setDetalle(ArrayList detalle) {
		this.detalle = detalle;
	}
}
