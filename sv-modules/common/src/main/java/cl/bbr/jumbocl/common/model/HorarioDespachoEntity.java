package cl.bbr.jumbocl.common.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que captura desde la Base de Datos los datos de un Horario de Despacho
 * @author bbr
 *
 */
public class HorarioDespachoEntity implements Serializable {

	private	long	id_hor_desp;
	private	long	id_semana;
	private	long	id_zona;
	private	Time	h_ini;
	private	Time	h_fin;
	private	int		n_horas;
    private List    jornadas = new ArrayList();

	/**
	 * Constructor 
	 */
	public HorarioDespachoEntity(){
		
	}
	
	/**
	 * @return id_zona
	 */
	public long getId_zona() {
		return id_zona;
	}

	/**
	 * @param id_zona
	 */
	public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
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
	 * @return id_hor_desp
	 */
	public long getId_hor_desp() {
		return id_hor_desp;
	}

	/**
	 * @param id_hor_desp
	 */
	public void setId_hor_desp(long id_hor_desp) {
		this.id_hor_desp = id_hor_desp;
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
	public int getN_horas() {
		return n_horas;
	}

	/**
	 * @param n_horas
	 */
	public void setN_horas(int n_horas) {
		this.n_horas = n_horas;
	}
	
    /**
     * @return Devuelve jornadas.
     */
    public List getJornadas() {
        return jornadas;
    }
    /**
     * @param jornadas El jornadas a establecer.
     */
    public void setJornadas(List jornadas) {
        this.jornadas = jornadas;
    }
}
