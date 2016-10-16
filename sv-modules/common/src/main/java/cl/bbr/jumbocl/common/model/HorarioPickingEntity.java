package cl.bbr.jumbocl.common.model;

import java.io.Serializable;
import java.sql.Time;

/**
 * Clase que captura desde la Base de Datos los datos de un Horario de Picking
 * @author bbr
 *
 */
public class HorarioPickingEntity implements Serializable {

	private long	id_hor_pick;
	private long	id_semana;
	private long	id_local;
	private Time	h_ini;
	private Time	h_fin;
	private int		n_horas;
	
	/**
	 * Constructor 
	 */
	public HorarioPickingEntity(){
		
	}	
	
	/**
	 * @return h_fin
	 */
	public Time getH_fin() {
		return h_fin;
	}

	/**
	 * @param h_fin
	 */
	public void setH_fin(Time h_fin) {
		this.h_fin = h_fin;
	}

	/**
	 * @return h_ini
	 */
	public Time getH_ini() {
		return h_ini;
	}

	/**
	 * @param h_ini
	 */
	public void setH_ini(Time h_ini) {
		this.h_ini = h_ini;
	}

	/**
	 * @return id_hor_pick
	 */
	public long getId_hor_pick() {
		return id_hor_pick;
	}

	/**
	 * @param id_hor_pick
	 */
	public void setId_hor_pick(long id_hor_pick) {
		this.id_hor_pick = id_hor_pick;
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
	 * @return n_horas
	 */
	public long getN_horas() {
		return n_horas;
	}

	/**
	 * @param n_horas
	 */
	public void setN_horas(int n_horas) {
		this.n_horas = n_horas;
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
	
}
