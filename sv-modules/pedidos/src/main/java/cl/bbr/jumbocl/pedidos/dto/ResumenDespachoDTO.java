package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class ResumenDespachoDTO implements Serializable{

	long ped_solicitados;
	long ped_validados;
	long ped_en_pick;
	long ped_en_bod;
	long ped_en_pago;
	long ped_pagado;
	long ped_en_desp;
	long ped_finalizado;
    long pedSinRuta;
	long ped_otro_estado;
	double avance;
	String nom_zona;
	long id_zona;
	/**
	 * @return Returns the avance.
	 */
	public double getAvance() {
		return avance;
	}
	/**
	 * @param avance The avance to set.
	 */
	public void setAvance(double avance) {
		this.avance = avance;
	}
	/**
	 * @return Returns the nom_zona.
	 */
	public String getNom_zona() {
		return nom_zona;
	}
	/**
	 * @param nom_zona The nom_zona to set.
	 */
	public void setNom_zona(String nom_zona) {
		this.nom_zona = nom_zona;
	}
	/**
	 * @return Returns the ped_en_bod.
	 */
	public long getPed_en_bod() {
		return ped_en_bod;
	}
	/**
	 * @param ped_en_bod The ped_en_bod to set.
	 */
	public void setPed_en_bod(long ped_en_bod) {
		this.ped_en_bod = ped_en_bod;
	}
	/**
	 * @return Returns the ped_en_desp.
	 */
	public long getPed_en_desp() {
		return ped_en_desp;
	}
	/**
	 * @param ped_en_desp The ped_en_desp to set.
	 */
	public void setPed_en_desp(long ped_en_desp) {
		this.ped_en_desp = ped_en_desp;
	}
	/**
	 * @return Returns the ped_en_pago.
	 */
	public long getPed_en_pago() {
		return ped_en_pago;
	}
	/**
	 * @param ped_en_pago The ped_en_pago to set.
	 */
	public void setPed_en_pago(long ped_en_pago) {
		this.ped_en_pago = ped_en_pago;
	}
	/**
	 * @return Returns the ped_en_pick.
	 */
	public long getPed_en_pick() {
		return ped_en_pick;
	}
	/**
	 * @param ped_en_pick The ped_en_pick to set.
	 */
	public void setPed_en_pick(long ped_en_pick) {
		this.ped_en_pick = ped_en_pick;
	}
	/**
	 * @return Returns the ped_finalizado.
	 */
	public long getPed_finalizado() {
		return ped_finalizado;
	}
	/**
	 * @param ped_finalizado The ped_finalizado to set.
	 */
	public void setPed_finalizado(long ped_finalizado) {
		this.ped_finalizado = ped_finalizado;
	}
	/**
	 * @return Returns the ped_otro_estado.
	 */
	public long getPed_otro_estado() {
		return ped_otro_estado;
	}
	/**
	 * @param ped_otro_estado The ped_otro_estado to set.
	 */
	public void setPed_otro_estado(long ped_otro_estado) {
		this.ped_otro_estado = ped_otro_estado;
	}
	/**
	 * @return Returns the ped_pagado.
	 */
	public long getPed_pagado() {
		return ped_pagado;
	}
	/**
	 * @param ped_pagado The ped_pagado to set.
	 */
	public void setPed_pagado(long ped_pagado) {
		this.ped_pagado = ped_pagado;
	}
	/**
	 * @return Returns the ped_solicitados.
	 */
	public long getPed_solicitados() {
		return ped_solicitados;
	}
	/**
	 * @param ped_solicitados The ped_solicitados to set.
	 */
	public void setPed_solicitados(long ped_solicitados) {
		this.ped_solicitados = ped_solicitados;
	}
	/**
	 * @return Returns the ped_validados.
	 */
	public long getPed_validados() {
		return ped_validados;
	}
	/**
	 * @param ped_validados The ped_validados to set.
	 */
	public void setPed_validados(long ped_validados) {
		this.ped_validados = ped_validados;
	}
	/**
	 * @return Returns the id_zona.
	 */
	public long getId_zona() {
		return id_zona;
	}
	/**
	 * @param id_zona The id_zona to set.
	 */
	public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
	}
	
	
	
    /**
     * @return Devuelve pedSinRuta.
     */
    public long getPedSinRuta() {
        return pedSinRuta;
    }
    /**
     * @param pedSinRuta El pedSinRuta a establecer.
     */
    public void setPedSinRuta(long pedSinRuta) {
        this.pedSinRuta = pedSinRuta;
    }
}
