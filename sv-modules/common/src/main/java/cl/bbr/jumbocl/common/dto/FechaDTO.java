package cl.bbr.jumbocl.common.dto;

import java.io.Serializable;
import java.util.Date;

public class FechaDTO implements Serializable {
	private Date 	fecha;
	private int		n_semana;
	private int		anio;
	private int		dia_semana;
	private String	fecha_Str;
	/**
	 * @return Returns the anio.
	 */
	public int getAnio() {
		return anio;
	}
	/**
	 * @param anio The anio to set.
	 */
	public void setAnio(int anio) {
		this.anio = anio;
	}
	/**
	 * @return Returns the dia_semana.
	 */
	public int getDia_semana() {
		return dia_semana;
	}
	/**
	 * @param dia_semana The dia_semana to set.
	 */
	public void setDia_semana(int dia_semana) {
		this.dia_semana = dia_semana;
	}
	/**
	 * @return Returns the fecha.
	 */
	public Date getFecha() {
		return fecha;
	}
	/**
	 * @param fecha The fecha to set.
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return Returns the fecha_Str.
	 */
	public String getFecha_Str() {
		return fecha_Str;
	}
	/**
	 * @param fecha_Str The fecha_Str to set.
	 */
	public void setFecha_Str(String fecha_Str) {
		this.fecha_Str = fecha_Str;
	}
	/**
	 * @return Returns the n_semana.
	 */
	public int getN_semana() {
		return n_semana;
	}
	/**
	 * @param n_semana The n_semana to set.
	 */
	public void setN_semana(int n_semana) {
		this.n_semana = n_semana;
	}
	
	
}
