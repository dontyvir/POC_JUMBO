/*
 * Creado el 23-nov-2012
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
public class ParametroConsultaFaltantesDTO {

	private Date fechaConsulta;
	private long indicadorJornada;
	private long numeroJornada;
	private long idLocal;
	private String textoJornada;
	
	
	public String getTextoJornada() {
		return textoJornada;
	}
	public void setTextoJornada(String textoJornada) {
		this.textoJornada = textoJornada;
	}
	/**
	 * @return Devuelve fechaConsulta.
	 */
	public Date getFechaConsulta() {
		return fechaConsulta;
	}
	/**
	 * @param fechaConsulta El fechaConsulta a establecer.
	 */
	public void setFechaConsulta(Date fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}
	/**
	 * @return Devuelve idLocal.
	 */
	public long getIdLocal() {
		return idLocal;
	}
	/**
	 * @param idLocal El idLocal a establecer.
	 */
	public void setIdLocal(long idLocal) {
		this.idLocal = idLocal;
	}
	/**
	 * @return Devuelve indicadorJornada.
	 */
	public long getIndicadorJornada() {
		return indicadorJornada;
	}
	/**
	 * @param indicadorJornada El indicadorJornada a establecer.
	 */
	public void setIndicadorJornada(long indicadorJornada) {
		this.indicadorJornada = indicadorJornada;
	}
	/**
	 * @return Devuelve numeroJornada.
	 */
	public long getNumeroJornada() {
		return numeroJornada;
	}
	/**
	 * @param numeroJornada El numeroJornada a establecer.
	 */
	public void setNumeroJornada(long numeroJornada) {
		this.numeroJornada = numeroJornada;
	}
}
