package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class RondaPropuestaDTO implements Serializable{

	long   id_jornada;
	long   id_op;
	String h_inicio;
	String h_fin;
	long   id_sector;
	String sector;
	double cant_prods;
	String comuna;
	String zona;
	String tipo_despacho;
	String prod_SPick_con_ant;
    String origenPedido;
			
	/**
	 * @return Devuelve comuna.
	 */
	public String getComuna() {
		return comuna;
	}
	/**
	 * @param comuna El comuna a establecer.
	 */
	public void setComuna(String comuna) {
		this.comuna = comuna;
	}
	public RondaPropuestaDTO() {

	}
	public double getCant_prods() {
		return cant_prods;
	}
	public void setCant_prods(double cant_prods) {
		this.cant_prods = cant_prods;
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
	public long getId_op() {
		return id_op;
	}
	public void setId_op(long id_op) {
		this.id_op = id_op;
	}
	public long getId_sector() {
		return id_sector;
	}
	public void setId_sector(long id_sector) {
		this.id_sector = id_sector;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	/**
	 * @return Returns the zona.
	 */
	public String getZona() {
		return zona;
	}
	/**
	 * @param zona The zona to set.
	 */
	public void setZona(String zona) {
		this.zona = zona;
	}
	
	
	
    /**
     * @return Devuelve tipo_despacho.
     */
    public String getTipo_despacho() {
        return tipo_despacho;
    }
    /**
     * @param tipo_despacho El tipo_despacho a establecer.
     */
    public void setTipo_despacho(String tipo_despacho) {
        this.tipo_despacho = tipo_despacho;
    }
    /**
     * @return Devuelve prod_SPick_con_ant.
     */
    public String getProd_SPick_con_ant() {
        return prod_SPick_con_ant;
    }
    /**
     * @param prod_SPick_con_ant El prod_SPick_con_ant a establecer.
     */
    public void setProd_SPick_con_ant(String prod_SPick_con_ant) {
        this.prod_SPick_con_ant = prod_SPick_con_ant;
    }
    /**
     * @return Devuelve origenPedido.
     */
    public String getOrigenPedido() {
        return origenPedido;
    }
    /**
     * @param origenPedido El origenPedido a establecer.
     */
    public void setOrigenPedido(String origenPedido) {
        this.origenPedido = origenPedido;
    }
}
