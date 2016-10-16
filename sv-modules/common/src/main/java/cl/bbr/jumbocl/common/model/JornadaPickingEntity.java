package cl.bbr.jumbocl.common.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase que captura desde la Base de Datos los datos de una Jornada de picking 
 * @author bbr
 *
 */
public class JornadaPickingEntity implements Serializable{

	private long	id_jpicking;
	private long	id_local;
	private long	id_hor_picking;
	private long	id_semana;
	private int		day_of_week;
	private Date	fecha;
	private long	capac_picking;
	private long	capac_ocupada;
	private long	capac_pickeada;
	private int 	hrs_validacion;
	private int 	hrs_ofrecido_web;
	
	
	/**
	 * Constructor
	 */
	public JornadaPickingEntity(){
		
	}

	/**
	 * @return capac_ocupada
	 */
	public long getCapac_ocupada() {
		return capac_ocupada;
	}

	/**
	 * @param capac_ocupada
	 */
	public void setCapac_ocupada(long capac_ocupada) {
		this.capac_ocupada = capac_ocupada;
	}

	/**
	 * @return capac_picking
	 */
	public long getCapac_picking() {
		return capac_picking;
	}

	/**
	 * @param capac_picking
	 */
	public void setCapac_picking(long capac_picking) {
		this.capac_picking = capac_picking;
	}

	/**
	 * @return el capac_pickeada
	 */
	public long getCapac_pickeada() {
		return capac_pickeada;
	}

	/**
	 * @param capac_pickeada el capac_pickeada a establecer
	 */
	public void setCapac_pickeada(long capac_pickeada) {
		this.capac_pickeada = capac_pickeada;
	}

	/**
	 * @return fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return id_jpicking
	 */
	public long getId_jpicking() {
		return id_jpicking;
	}

	/**
	 * @param id_jpicking
	 */
	public void setId_jpicking(long id_jpicking) {
		this.id_jpicking = id_jpicking;
	}

	/**
	 * @return day_of_week
	 */
	public int getDay_of_week() {
		return day_of_week;
	}

	/**
	 * @param day_of_week
	 */
	public void setDay_of_week(int day_of_week) {
		this.day_of_week = day_of_week;
	}

	/**
	 * @return id_hor_picking
	 */
	public long getId_hor_picking() {
		return id_hor_picking;
	}

	/**
	 * @param id_hor_picking
	 */
	public void setId_hor_picking(long id_hor_picking) {
		this.id_hor_picking = id_hor_picking;
	}

	/**
	 * @return id_local
	 */
	public long getId_local() {
		return id_local;
	}

	/**
	 * @param id_local
	 */
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}

	/**
	 * @return id_semana
	 */
	public long getId_semana() {
		return id_semana;
	}

	/**
	 * @param id_semana
	 */
	public void setId_semana(long id_semana) {
		this.id_semana = id_semana;
	}
	
	
	/**
	 * @return hrs_ofrecido_web
	 */
	public int getHrs_ofrecido_web() {
		return hrs_ofrecido_web;
	}

	/**
	 * @param hrs_ofrecido_web
	 */
	public void setHrs_ofrecido_web(int hrs_ofrecido_web) {
		this.hrs_ofrecido_web = hrs_ofrecido_web;
	}

	/**
	 * @return hrs_validacion
	 */
	public int getHrs_validacion() {
		return hrs_validacion;
	}

	/**
	 * @param hrs_validacion
	 */
	public void setHrs_validacion(int hrs_validacion) {
		this.hrs_validacion = hrs_validacion;
	}

	
}
