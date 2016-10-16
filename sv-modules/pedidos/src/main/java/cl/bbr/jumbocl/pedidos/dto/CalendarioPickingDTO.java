package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
import java.util.List;

import cl.bbr.jumbocl.common.model.SemanasEntity;

public class CalendarioPickingDTO implements Serializable {

	private SemanasEntity 	semana;
	private List			horarios;
	private List			jornadas;
	
	public CalendarioPickingDTO(){
		
	}

	public List getHorarios() {
		return horarios;
	}

	public void setHorarios(List horarios) {
		this.horarios = horarios;
	}

	public List getJornadas() {
		return jornadas;
	}

	public void setJornadas(List jornadas) {
		this.jornadas = jornadas;
	}

	public SemanasEntity getSemana() {
		return semana;
	}

	public void setSemana(SemanasEntity semana) {
		this.semana = semana;
	}
	
}
