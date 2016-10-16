package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class LocalDTO implements Serializable{

	private long   id_local;
	private String cod_local;
	private String nom_local;
	private String fcarga_precios;
	private int    cod_local_pos;
	private String tipo_flujo;
	private int    orden;
	private String cod_local_promo;
    private String retiroLocal;
    private String direccion;
    private long   idZonaRetiro;
    private String tipo_picking;
    private long dpc;
	
	
	public LocalDTO(){
		
	}

	/**
	 * @return Returns the cod_local_promo.
	 */
	public String getCod_local_promo() {
		return cod_local_promo;
	}

	/**
	 * @param cod_local_promo The cod_local_promo to set.
	 */
	public void setCod_local_promo(String cod_local_promo) {
		this.cod_local_promo = cod_local_promo;
	}

	/**
	 * @return Returns the orden.
	 */
	public int getOrden() {
		return orden;
	}

	/**
	 * @param orden The orden to set.
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}

	/**
	 * @return Returns the cod_local_pos.
	 */
	public int getCod_local_pos() {
		return cod_local_pos;
	}

	/**
	 * @param cod_local_pos The cod_local_pos to set.
	 */
	public void setCod_local_pos(int cod_local_pos) {
		this.cod_local_pos = cod_local_pos;
	}

	/**
	 * @return Returns the tipo_flujo.
	 */
	public String getTipo_flujo() {
		return tipo_flujo;
	}

	/**
	 * @param tipo_flujo The tipo_flujo to set.
	 */
	public void setTipo_flujo(String tipo_flujo) {
		this.tipo_flujo = tipo_flujo;
	}

	public String getCod_local() {
		return cod_local;
	}

	public void setCod_local(String cod_local) {
		this.cod_local = cod_local;
	}

	public String getFcarga_precios() {
		return fcarga_precios;
	}

	public void setFcarga_precios(String fcarga_precios) {
		this.fcarga_precios = fcarga_precios;
	}

	public long getId_local() {
		return id_local;
	}

	public void setId_local(long id_local) {
		this.id_local = id_local;
	}

	public String getNom_local() {
		return nom_local;
	}

	public void setNom_local(String nom_local) {
		this.nom_local = nom_local;
	}
		
    /**
     * @return Devuelve direccion.
     */
    public String getDireccion() {
        return direccion;
    }
    /**
     * @return Devuelve retiroLocal.
     */
    public String getRetiroLocal() {
        return retiroLocal;
    }
    /**
     * @param direccion El direccion a establecer.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    /**
     * @param retiroLocal El retiroLocal a establecer.
     */
    public void setRetiroLocal(String retiroLocal) {
        this.retiroLocal = retiroLocal;
    }
    /**
     * @return Devuelve idZonaRetiro.
     */
    public long getIdZonaRetiro() {
        return idZonaRetiro;
    }
    /**
     * @param idZonaRetiro El idZonaRetiro a establecer.
     */
    public void setIdZonaRetiro(long idZonaRetiro) {
        this.idZonaRetiro = idZonaRetiro;
    }
    /**
     * @return Devuelve tipo_picking.
     */
    public String getTipo_picking() {
        return tipo_picking;
    }
    /**
     * @param tipo_picking El tipo_picking a establecer.
     */
    public void setTipo_picking(String tipo_picking) {
        this.tipo_picking = tipo_picking;
    }
    /**
     * @return Devuelve dpc.
     */
    public long getDpc() {
        return dpc;
    }
    /**
     * @param dpc El dpc a establecer.
     */
    public void setDpc(long dpc) {
        this.dpc = dpc;
    }
}
