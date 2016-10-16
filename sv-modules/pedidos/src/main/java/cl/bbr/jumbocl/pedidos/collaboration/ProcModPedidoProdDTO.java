package cl.bbr.jumbocl.pedidos.collaboration;

import java.io.Serializable;

public class ProcModPedidoProdDTO implements Serializable{
	private long id_pedido;
	private long id_producto;
	private double cantidad;
	private String observacion;
	private String accion;
	private String usr_login;
	private String mnsAgreg;
	private long id_detalle;
	private String mnsElim;
    private int idCriterio;
    private String descCriterio;
	
	public ProcModPedidoProdDTO(long id_pedido, long id_producto, double cantidad, String observacion, 
			String accion, String usr_login, String mnsAgreg, long id_detalle, String mnsElim) {
		super();
		this.id_pedido = id_pedido;
		this.id_producto = id_producto;
		this.cantidad = cantidad;
		this.observacion = observacion;
		this.accion = accion;
		this.usr_login = usr_login;
		this.mnsAgreg = mnsAgreg;
		this.id_detalle = id_detalle;
		this.mnsElim = mnsElim;
	}

	public double getCantidad() {
		return cantidad;
	}

	public long getId_pedido() {
		return id_pedido;
	}

	public long getId_producto() {
		return id_producto;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}

	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getUsr_login() {
		return usr_login;
	}

	public void setUsr_login(String usr_login) {
		this.usr_login = usr_login;
	}

	public String getMnsAgreg() {
		return mnsAgreg;
	}

	public String getMnsElim() {
		return mnsElim;
	}

	public long getId_detalle() {
		return id_detalle;
	}

	public void setId_detalle(long id_detalle) {
		this.id_detalle = id_detalle;
	}

	public void setMnsAgreg(String mnsAgreg) {
		this.mnsAgreg = mnsAgreg;
	}

	public void setMnsElim(String mnsElim) {
		this.mnsElim = mnsElim;
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
    public int getIdCriterio() {
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
    public void setIdCriterio(int idCriterio) {
        this.idCriterio = idCriterio;
    }
}
