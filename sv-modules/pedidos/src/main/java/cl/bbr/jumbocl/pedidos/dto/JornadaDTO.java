package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class JornadaDTO implements Serializable {

	private long	id_jornada;
	private long	id_estado;
	private long	id_local;
	private String	f_jornada;
	private String	h_inicio;
	private String	h_fin;
	private String	estado;
	private long	cant_pedidos;
	private long	capac_picking_ut;
	private long	capac_picking_max;
	private double	porc_utilizacion;
	private long	cant_ped_por_pickear;
	private long	cant_ped_en_picking;
	private long	cant_ped_en_bodega;
	private long	cant_prod_solicitados; //total de productos
	private long	cant_prod_sin_pickear;
	private double	porc_prod_sin_pickear;
	private long	cant_prod_sin_asignar;
	private double	porc_prod_sin_asignar;
	private long	cant_prod_en_bodega;
	private double	porc_prod_en_bodega;
	private int 	hrs_validacion;
	private int 	hrs_ofrecido_web;
	private long	id_semana;
	private int 	dow;
	
	
	public long getCant_ped_en_bodega() {
		return cant_ped_en_bodega;
	}
	public void setCant_ped_en_bodega(long cant_ped_en_bodega) {
		this.cant_ped_en_bodega = cant_ped_en_bodega;
	}
	public long getCant_ped_en_picking() {
		return cant_ped_en_picking;
	}
	public void setCant_ped_en_picking(long cant_ped_en_picking) {
		this.cant_ped_en_picking = cant_ped_en_picking;
	}
	public long getCant_ped_por_pickear() {
		return cant_ped_por_pickear;
	}
	public void setCant_ped_por_pickear(long cant_ped_por_pickear) {
		this.cant_ped_por_pickear = cant_ped_por_pickear;
	}
	public long getCant_pedidos() {
		return cant_pedidos;
	}
	public void setCant_pedidos(long cant_pedidos) {
		this.cant_pedidos = cant_pedidos;
	}
	public long getCant_prod_en_bodega() {
		return cant_prod_en_bodega;
	}
	public void setCant_prod_en_bodega(long cant_prod_en_bodega) {
		this.cant_prod_en_bodega = cant_prod_en_bodega;
	}
	public long getCant_prod_sin_asignar() {
		return cant_prod_sin_asignar;
	}
	public void setCant_prod_sin_asignar(long cant_prod_sin_asignar) {
		this.cant_prod_sin_asignar = cant_prod_sin_asignar;
	}
	public long getCapac_picking_max() {
		return capac_picking_max;
	}
	public void setCapac_picking_max(long capac_picking_max) {
		this.capac_picking_max = capac_picking_max;
	}
	public long getCapac_picking_ut() {
		return capac_picking_ut;
	}
	public void setCapac_picking_ut(long capac_picking_ut) {
		this.capac_picking_ut = capac_picking_ut;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getF_jornada() {
		return f_jornada;
	}
	public void setF_jornada(String f_jornada) {
		this.f_jornada = f_jornada;
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
	public long getId_jornada() {
		return id_jornada;
	}
	public void setId_jornada(long id_jornada) {
		this.id_jornada = id_jornada;
	}
	public double getPorc_prod_en_bodega() {
		return porc_prod_en_bodega;
	}
	public void setPorc_prod_en_bodega(double porc_prod_en_bodega) {
		this.porc_prod_en_bodega = porc_prod_en_bodega;
	}
	public double getPorc_prod_sin_asignar() {
		return porc_prod_sin_asignar;
	}
	public void setPorc_prod_sin_asignar(double porc_prod_sin_asignar) {
		this.porc_prod_sin_asignar = porc_prod_sin_asignar;
	}
	public double getPorc_utilizacion() {
		return porc_utilizacion;
	}
	public void setPorc_utilizacion(double porc_utilizacion) {
		this.porc_utilizacion = porc_utilizacion;
	}
	public long getId_estado() {
		return id_estado;
	}
	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}
	public long getId_local() {
		return id_local;
	}
	public void setId_local(long id_local) {
		this.id_local = id_local;
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
	/**
	 * @return Returns the id_semana.
	 */
	public long getId_semana() {
		return id_semana;
	}
	/**
	 * @param id_semana The id_semana to set.
	 */
	public void setId_semana(long id_semana) {
		this.id_semana = id_semana;
	}
	/**
	 * @return Returns the dow.
	 */
	public int getDow() {
		return dow;
	}
	/**
	 * @param dow The dow to set.
	 */
	public void setDow(int dow) {
		this.dow = dow;
	}
	
	
    /**
     * @return Devuelve cant_prod_sin_pickear.
     */
    public long getCant_prod_sin_pickear() {
        return cant_prod_sin_pickear;
    }
    /**
     * @return Devuelve cant_prod_solicitados.
     */
    public long getCant_prod_solicitados() {
        return cant_prod_solicitados;
    }
    /**
     * @return Devuelve porc_prod_sin_pickear.
     */
    public double getPorc_prod_sin_pickear() {
        return porc_prod_sin_pickear;
    }
    /**
     * @param cant_prod_sin_pickear El cant_prod_sin_pickear a establecer.
     */
    public void setCant_prod_sin_pickear(long cant_prod_sin_pickear) {
        this.cant_prod_sin_pickear = cant_prod_sin_pickear;
    }
    /**
     * @param cant_prod_solicitados El cant_prod_solicitados a establecer.
     */
    public void setCant_prod_solicitados(long cant_prod_solicitados) {
        this.cant_prod_solicitados = cant_prod_solicitados;
    }
    /**
     * @param porc_prod_sin_pickear El porc_prod_sin_pickear a establecer.
     */
    public void setPorc_prod_sin_pickear(double porc_prod_sin_pickear) {
        this.porc_prod_sin_pickear = porc_prod_sin_pickear;
    }
}
