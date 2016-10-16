package cl.bbr.jumbocl.common.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * Clase que captura desde la Base de Datos los datos de un Jornada de Despacho 
 * @author bbr
 *
 */
public class JornadaDespachoEntity implements Serializable {
	
	private long	id_jdespacho;
	private long	id_zona;
	private long	id_hor_desp;
	private long	id_semana;
	private long	id_jpicking;
	private int		day_of_week;
	private Date	fecha;
	private long	capac_picking;
	private long	capac_despacho;
	private long	capac_picking_ocupada;
	private long	capac_despacho_ocupada;
	private int		tarifa_express;
	private int		tarifa_normal;
	private int		tarifa_economica;
	private int		tarifa_umbral;
	private int 	hrs_validacion;
	private int 	hrs_ofrecido_web;
	private Time	horaIniPicking;
	private Time	horaFinPicking;
	private Date	fecha_picking;
	
	public JornadaDespachoEntity(){
		
	}


	public Time getHoraFinPicking() {
		return horaFinPicking;
	}


	public void setHoraFinPicking(Time horaFinPicking) {
		this.horaFinPicking = horaFinPicking;
	}


	public Time getHoraIniPicking() {
		return horaIniPicking;
	}


	public void setHoraIniPicking(Time horaIniPicking) {
		this.horaIniPicking = horaIniPicking;
	}


	public long getCapac_despacho() {
		return capac_despacho;
	}

	public void setCapac_despacho(long capac_despacho) {
		this.capac_despacho = capac_despacho;
	}

	public long getCapac_despacho_ocupada() {
		return capac_despacho_ocupada;
	}

	public void setCapac_despacho_ocupada(long capac_despacho_ocupada) {
		this.capac_despacho_ocupada = capac_despacho_ocupada;
	}

    
    /**
     * @return Devuelve tarifa_economica.
     */
    public int getTarifa_economica() {
        return tarifa_economica;
    }
    /**
     * @param tarifa_economica El tarifa_economica a establecer.
     */
    public void setTarifa_economica(int tarifa_economica) {
        this.tarifa_economica = tarifa_economica;
    }
    /**
     * @return Devuelve tarifa_express.
     */
    public int getTarifa_express() {
        return tarifa_express;
    }
    /**
     * @param tarifa_express El tarifa_express a establecer.
     */
    public void setTarifa_express(int tarifa_express) {
        this.tarifa_express = tarifa_express;
    }
    /**
     * @return Devuelve tarifa_normal.
     */
    public int getTarifa_normal() {
        return tarifa_normal;
    }
    /**
     * @param tarifa_normal El tarifa_normal a establecer.
     */
    public void setTarifa_normal(int tarifa_normal) {
        this.tarifa_normal = tarifa_normal;
    }
    
	public int getTarifa_umbral() {
		return tarifa_umbral;
	}


	public void setTarifa_umbral(int tarifa_umbral) {
		this.tarifa_umbral = tarifa_umbral;
	}
    
	public long getCapac_picking_ocupada() {
		return capac_picking_ocupada;
	}

	public void setCapac_picking_ocupada(long capac_picking_ocupada) {
		this.capac_picking_ocupada = capac_picking_ocupada;
	}

	public long getCapac_picking() {
		return capac_picking;
	}

	public void setCapac_picking(long capac_picking) {
		this.capac_picking = capac_picking;
	}

	public int getDay_of_week() {
		return day_of_week;
	}

	public void setDay_of_week(int day_of_week) {
		this.day_of_week = day_of_week;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public long getId_hor_desp() {
		return id_hor_desp;
	}

	public void setId_hor_desp(long id_hor_desp) {
		this.id_hor_desp = id_hor_desp;
	}

	public long getId_jdespacho() {
		return id_jdespacho;
	}

	public void setId_jdespacho(long id_jdespacho) {
		this.id_jdespacho = id_jdespacho;
	}

	public long getId_jpicking() {
		return id_jpicking;
	}

	public void setId_jpicking(long id_jpicking) {
		this.id_jpicking = id_jpicking;
	}

	public long getId_semana() {
		return id_semana;
	}

	public void setId_semana(long id_semana) {
		this.id_semana = id_semana;
	}

	public long getId_zona() {
		return id_zona;
	}

	public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
	}

	public int getHrs_ofrecido_web() {
		return hrs_ofrecido_web;
	}

	public void setHrs_ofrecido_web(int hrs_ofrecido_web) {
		this.hrs_ofrecido_web = hrs_ofrecido_web;
	}

	public int getHrs_validacion() {
		return hrs_validacion;
	}

	public void setHrs_validacion(int hrs_validacion) {
		this.hrs_validacion = hrs_validacion;
	}


	public Date getFecha_picking() {
		return fecha_picking;
	}


	public void setFecha_picking(Date fecha_picking) {
		this.fecha_picking = fecha_picking;
	}
	
	
	
}
