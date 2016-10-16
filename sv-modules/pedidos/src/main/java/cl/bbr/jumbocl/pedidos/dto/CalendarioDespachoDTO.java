package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
import java.util.List;

import cl.bbr.jumbocl.common.model.SemanasEntity;

public class CalendarioDespachoDTO implements Serializable {

	private	SemanasEntity 	semana;
	private List			horarios;	/* Lista de HorarioDespachoEntity */
	private List			jornadas;	/* Lista de JornadaDespachoEntity */
	
	public CalendarioDespachoDTO(){
		
	}
	
	public List getHorarios() {
		return horarios;
	}
	public void setHorarios(List horarios) {
		this.horarios = horarios;
	}
	
	/**
	 * Lista de JornadaDespachoEntity 
	 * @return Lista de JornadaDespachoEntity
	 */
	public List getJornadas() {
		return jornadas;
	}
	
	/**
	 * Lista de JornadaDespachoEntity
	 * @param jornadas
	 */
	public void setJornadas(List jornadas) {
		this.jornadas = jornadas;
	}
	
	/**
	 * Lista de HorarioDespachoEntity
	 * @return SemanasEntity
	 */
	public SemanasEntity getSemana() {
		return semana;
	}
	
	/**
	 * Lista de HorarioDespachoEntity
	 * @param semana
	 */
	public void setSemana(SemanasEntity semana) {
		this.semana = semana;
	}

}
