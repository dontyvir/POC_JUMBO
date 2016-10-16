package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class HorarioDespachoDTO implements Serializable {

	String	h_ini;
	String	h_fin;
	JorDespachoCalDTO	lun = null;
	JorDespachoCalDTO	mar = null;
	JorDespachoCalDTO	mie = null;
	JorDespachoCalDTO	jue = null;
	JorDespachoCalDTO	vie = null;
	JorDespachoCalDTO	sab = null;
	JorDespachoCalDTO	dom = null;
	
	public HorarioDespachoDTO(){
		
	}
	
	public JorDespachoCalDTO getDom() {
		return dom;
	}

	public void setDom(JorDespachoCalDTO dom) {
		this.dom = dom;
	}

	public String getH_fin() {
		return h_fin;
	}

	public void setH_fin(String h_fin) {
		this.h_fin = h_fin;
	}

	public String getH_ini() {
		return h_ini;
	}

	public void setH_ini(String h_ini) {
		this.h_ini = h_ini;
	}

	public JorDespachoCalDTO getJue() {
		return jue;
	}

	public void setJue(JorDespachoCalDTO jue) {
		this.jue = jue;
	}

	public JorDespachoCalDTO getLun() {
		return lun;
	}

	public void setLun(JorDespachoCalDTO lun) {
		this.lun = lun;
	}

	public JorDespachoCalDTO getMar() {
		return mar;
	}

	public void setMar(JorDespachoCalDTO mar) {
		this.mar = mar;
	}

	public JorDespachoCalDTO getMie() {
		return mie;
	}

	public void setMie(JorDespachoCalDTO mie) {
		this.mie = mie;
	}

	public JorDespachoCalDTO getSab() {
		return sab;
	}

	public void setSab(JorDespachoCalDTO sab) {
		this.sab = sab;
	}

	public JorDespachoCalDTO getVie() {
		return vie;
	}

	public void setVie(JorDespachoCalDTO vie) {
		this.vie = vie;
	}

}
