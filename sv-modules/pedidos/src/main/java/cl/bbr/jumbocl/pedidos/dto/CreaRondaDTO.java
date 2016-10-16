package cl.bbr.jumbocl.pedidos.dto;

import java.util.List;

public class CreaRondaDTO {
	private long   id_sector;
	private long   id_jpicking;
	private long   id_estado;
	private long   id_local;
	private long   id_pedido; // Usado sólo en Picking Light
	private String cant_prod; // Usado sólo en Picking Light
	private List   pedidos;
	private List   lst_indices;
	private String mnsError;
	private String tipo_ve;
	
	
	
	/**
	 * @return Returns the tipo_ve.
	 */
	public String getTipo_ve() {
		return tipo_ve;
	}
	/**
	 * @param tipo_ve The tipo_ve to set.
	 */
	public void setTipo_ve(String tipo_ve) {
		this.tipo_ve = tipo_ve;
	}
	public String getMnsError() {
		return mnsError;
	}
	public void setMnsError(String mnsError) {
		this.mnsError = mnsError;
	}
	/**
	 * @return Returns the id_local.
	 */
	public long getId_local() {
		return id_local;
	}
	/**
	 * @param id_local The id_local to set.
	 */
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}
	/**
	 * @return Returns the id_estado.
	 */
	public long getId_estado() {
		return id_estado;
	}
	/**
	 * @return Returns the id_jpicking.
	 */
	public long getId_jpicking() {
		return id_jpicking;
	}
	/**
	 * @return Returns the id_sector.
	 */
	public long getId_sector() {
		return id_sector;
	}
	/**
	 * @return Returns the pedidos.
	 */
	public List getPedidos() {
		return pedidos;
	}
	/**
	 * @param id_estado The id_estado to set.
	 */
	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}
	/**
	 * @param id_jpicking The id_jpicking to set.
	 */
	public void setId_jpicking(long id_jpicking) {
		this.id_jpicking = id_jpicking;
	}
	/**
	 * @param id_sector The id_sector to set.
	 */
	public void setId_sector(long id_sector) {
		this.id_sector = id_sector;
	}
	/**
	 * @param pedidos The pedidos to set.
	 */
	public void setPedidos(List pedidos) {
		this.pedidos = pedidos;
	}
	public List getLst_indices() {
		return lst_indices;
	}
	public void setLst_indices(List lst_indices) {
		this.lst_indices = lst_indices;
	}

    /**
     * @return Devuelve id_pedido.
     */
    public long getId_pedido() {
        return id_pedido;
    }
    /**
     * @param id_pedido El id_pedido a establecer.
     */
    public void setId_pedido(long id_pedido) {
        this.id_pedido = id_pedido;
    }
    /**
     * @return Devuelve cant_prod.
     */
    public String getCant_prod() {
        return cant_prod;
    }
    /**
     * @param cant_prod El cant_prod a establecer.
     */
    public void setCant_prod(String cant_prod) {
        this.cant_prod = cant_prod;
    }
}
