/*
 * Creado el 07-nov-2012
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.bol.dto;

import java.util.Date;

/**
 * @author Sebastian
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class CabeceraInformeFaltantesDTO {
	
	
	/**
	 * Variable para fechaActual
	 */
	private Date fechaActual;
	/**
	 * Variable para horaDesde
	 */
	/**
	 * Variable para horaDesde
	 */
	private String horaDesde;
	/**
	 * Variable para horaHasta
	 */
	private String horaHasta;
	/**
	 * Variable para jornadaActual
	 */
	private long jornadaActual;
	
	
	
	/**
	 * @return Devuelve fechaActual.
	 */
	public Date getFechaActual() {
		return fechaActual;
	}
	/**
	 * @param fechaActual El fechaActual a establecer.
	 */
	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}
	/**
	 * @return Devuelve horaDesde.
	 */
	public String getHoraDesde() {
		return horaDesde;
	}
	/**
	 * @param horaDesde El horaDesde a establecer.
	 */
	public void setHoraDesde(String horaDesde) {
		this.horaDesde = horaDesde;
	}
	/**
	 * @return Devuelve horaHasta.
	 */
	public String getHoraHasta() {
		return horaHasta;
	}
	/**
	 * @param horaHasta El horaHasta a establecer.
	 */
	public void setHoraHasta(String horaHasta) {
		this.horaHasta = horaHasta;
	}
	/**
	 * @return Devuelve jornadaActual.
	 */
	public long getJornadaActual() {
		return jornadaActual;
	}
	/**
	 * @param jornadaActual El jornadaActual a establecer.
	 */
	public void setJornadaActual(long jornadaActual) {
		this.jornadaActual = jornadaActual;
	}
}
