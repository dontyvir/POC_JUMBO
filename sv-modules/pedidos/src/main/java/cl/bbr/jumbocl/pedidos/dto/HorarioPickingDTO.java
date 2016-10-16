package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class HorarioPickingDTO implements Serializable{

	String	h_inicio;
	String	h_fin;
	CalJorPickDTO lun = null;
	CalJorPickDTO mar = null;
	CalJorPickDTO mie = null;
	CalJorPickDTO jue = null;
	CalJorPickDTO vie = null;
	CalJorPickDTO sab = null;
	CalJorPickDTO dom = null;

	public HorarioPickingDTO(){
		
	}

	public CalJorPickDTO getDom() {
		return dom;
	}

	public void setDom(CalJorPickDTO dom) {
		this.dom = dom;
	}

	public CalJorPickDTO getJue() {
		return jue;
	}

	public void setJue(CalJorPickDTO jue) {
		this.jue = jue;
	}

	public CalJorPickDTO getLun() {
		return lun;
	}

	public void setLun(CalJorPickDTO lun) {
		this.lun = lun;
	}

	public CalJorPickDTO getMar() {
		return mar;
	}

	public void setMar(CalJorPickDTO mar) {
		this.mar = mar;
	}

	public CalJorPickDTO getMie() {
		return mie;
	}

	public void setMie(CalJorPickDTO mie) {
		this.mie = mie;
	}

	public CalJorPickDTO getSab() {
		return sab;
	}

	public void setSab(CalJorPickDTO sab) {
		this.sab = sab;
	}

	public CalJorPickDTO getVie() {
		return vie;
	}

	public void setVie(CalJorPickDTO vie) {
		this.vie = vie;
	}

	public String getH_fin() {
		return h_fin;
	}

	public void setH_fin(String h_fin) {
		this.h_fin = h_fin;
	}

	public String getH_inicio() {
		return h_inicio;
	}

	public void setH_inicio(String h_inicio) {
		this.h_inicio = h_inicio;
	}
	
}
