package cl.bbr.jumbocl.pedidos.dto;

public class SustitutoDTO {
	private long id_detalle1;
	private long id_detalle_pick1;
	private String cod_prod1;
	private String uni_med1;
	private String descr1;
	private double cant1;
	private String obs1;
	private double precio1;
	private String cod_prod2;
	private String uni_med2;
	private String descr2;
	private double cant2;
	private double precio2;
	private long id_pedido;
    
    private long idCriterio;
    private String descCriterio;

	public SustitutoDTO() {

	}
	
	

	public long getId_pedido() {
		return id_pedido;
	}



	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}



	public double getCant1() {
		return cant1;
	}

	public double getCant2() {
		return cant2;
	}

	public void setCant1(double cant1) {
		this.cant1 = cant1;
	}

	public void setCant2(double cant2) {
		this.cant2 = cant2;
	}




	public long getId_detalle1() {
		return id_detalle1;
	}


	public void setId_detalle1(long id_detalle1) {
		this.id_detalle1 = id_detalle1;
	}


	/**
	 * @return Returns the obs1.
	 */
	public String getObs1() {
		return obs1;
	}


	/**
	 * @param obs1 The obs1 to set.
	 */
	public void setObs1(String obs1) {
		this.obs1 = obs1;
	}


	/**
	 * @return Returns the cod_prod1.
	 */
	public String getCod_prod1() {
		return cod_prod1;
	}


	/**
	 * @return Returns the cod_prod2.
	 */
	public String getCod_prod2() {
		return cod_prod2;
	}


	/**
	 * @return Returns the descr1.
	 */
	public String getDescr1() {
		return descr1;
	}


	/**
	 * @return Returns the descr2.
	 */
	public String getDescr2() {
		return descr2;
	}


	/**
	 * @return Returns the uni_med1.
	 */
	public String getUni_med1() {
		return uni_med1;
	}


	/**
	 * @return Returns the uni_med2.
	 */
	public String getUni_med2() {
		return uni_med2;
	}




	/**
	 * @param cod_prod1 The cod_prod1 to set.
	 */
	public void setCod_prod1(String cod_prod1) {
		this.cod_prod1 = cod_prod1;
	}


	/**
	 * @param cod_prod2 The cod_prod2 to set.
	 */
	public void setCod_prod2(String cod_prod2) {
		this.cod_prod2 = cod_prod2;
	}


	/**
	 * @param descr1 The descr1 to set.
	 */
	public void setDescr1(String descr1) {
		this.descr1 = descr1;
	}


	/**
	 * @param descr2 The descr2 to set.
	 */
	public void setDescr2(String descr2) {
		this.descr2 = descr2;
	}


	/**
	 * @param uni_med1 The uni_med1 to set.
	 */
	public void setUni_med1(String uni_med1) {
		this.uni_med1 = uni_med1;
	}


	/**
	 * @param uni_med2 The uni_med2 to set.
	 */
	public void setUni_med2(String uni_med2) {
		this.uni_med2 = uni_med2;
	}


	/**
	 * @return Returns the precio1.
	 */
	public double getPrecio1() {
		return precio1;
	}


	/**
	 * @param precio1 The precio1 to set.
	 */
	public void setPrecio1(double precio1) {
		this.precio1 = precio1;
	}


	/**
	 * @return Returns the precio2.
	 */
	public double getPrecio2() {
		return precio2;
	}


	/**
	 * @param precio2 The precio2 to set.
	 */
	public void setPrecio2(double precio2) {
		this.precio2 = precio2;
	}



	public long getId_detalle_pick1() {
		return id_detalle_pick1;
	}



	public void setId_detalle_pick1(long id_detalle_pick1) {
		this.id_detalle_pick1 = id_detalle_pick1;
	}

    /**
     * @return Devuelve descCriterio.
     */
    public String getDescCriterio() {
        return descCriterio;
    }
    /**
     * @return Devuelve idCriterio.
     */
    public long getIdCriterio() {
        return idCriterio;
    }
    /**
     * @param descCriterio El descCriterio a establecer.
     */
    public void setDescCriterio(String descCriterio) {
        this.descCriterio = descCriterio;
    }
    /**
     * @param idCriterio El idCriterio a establecer.
     */
    public void setIdCriterio(long idCriterio) {
        this.idCriterio = idCriterio;
    }
}
