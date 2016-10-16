package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;

public class DespachoCriteriaDTO implements Serializable{

	public final static String ORDEN_ZONA_ORD = "z.orden_zona";
	public final static String ORDEN_ZONA     = "z.nombre";
	public final static String ORDEN_JORNADA  = "p.id_jpicking";
	public final static String ORDEN_COMUNA   = "c.nombre";
	public final static String ORDEN_ESTADO   = "p.id_estado";
	public final static String ORDEN_ASCENDENTE  = "ASC";
	public final static String ORDEN_DESCENDENTE = "DESC";
	
	
	private long	 id_pedido;
	private long	 id_local;
	private long	 id_jdespacho;
	private long	 id_zona;
	private String	 f_despacho;
	private String	 h_inicio;
	private String	 h_fin;
	private long	 id_comuna;
	private long	 id_estado;
	private int 	 Pag;
	private int 	 Regsperpag;
	private String[] filtro_estados = null;
	private List     orden_columnas;
	private String   tipo_picking = Constantes.TIPO_PICKING_NORMAL_CTE; //N: Normal,  L: Light
    private String reprogramada;
    private String clienteRut;
    private String clienteApellido;
    private String origen;
	
	public DespachoCriteriaDTO() {
	}
	
	public List getOrden_columnas() {
		return orden_columnas;
	}

	public void setOrden_columnas(List orden_columnas) {
		this.orden_columnas = orden_columnas;
	}

	public int getPag() {
		return Pag;
	}

	public void setPag(int pag) {
		Pag = pag;
	}

	public int getRegsperpag() {
		return Regsperpag;
	}

	public void setRegsperpag(int regsperpag) {
		Regsperpag = regsperpag;
	}
	
	public long getId_jdespacho() {
		return id_jdespacho;
	}

	public void setId_jdespacho(long id_jdespacho) {
		this.id_jdespacho = id_jdespacho;
	}

	public String getF_despacho() {
		return f_despacho;
	}

	public void setF_despacho(String f_despacho) {
		this.f_despacho = f_despacho;
	}

	public String getH_inicio() {
		return h_inicio;
	}

	public void setH_inicio(String h_inicio) {
		this.h_inicio = h_inicio;
	}

	public long getId_comuna() {
		return id_comuna;
	}

	public void setId_comuna(long id_comuna) {
		this.id_comuna = id_comuna;
	}

	public long getId_estado() {
		return id_estado;
	}

	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}

	public long getId_pedido() {
		return id_pedido;
	}

	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}

	public long getId_local() {
		return id_local;
	}

	public void setId_local(long id_local) {
		this.id_local = id_local;
	}

	public long getId_zona() {
		return id_zona;
	}

	public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
	}

	public String[] getFiltro_estados() {
		return filtro_estados;
	}

	public void setFiltro_estados(String[] filtro_estados) {
		this.filtro_estados = filtro_estados;
	}

	
    /**
     * @return Devuelve h_fin.
     */
    public String getH_fin() {
        return h_fin;
    }
    /**
     * @param h_fin El h_fin a establecer.
     */
    public void setH_fin(String h_fin) {
        this.h_fin = h_fin;
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
     * @return Devuelve reprogramada.
     */
    public String getReprogramada() {
        return reprogramada;
    }
    /**
     * @param reprogramada El reprogramada a establecer.
     */
    public void setReprogramada(String reprogramada) {
        this.reprogramada = reprogramada;
    }
    /**
     * @return Devuelve clienteApellido.
     */
    public String getClienteApellido() {
        return clienteApellido;
    }
    /**
     * @return Devuelve clienteRut.
     */
    public String getClienteRut() {
        return clienteRut;
    }
    /**
     * @param clienteApellido El clienteApellido a establecer.
     */
    public void setClienteApellido(String clienteApellido) {
        this.clienteApellido = clienteApellido;
    }
    /**
     * @param clienteRut El clienteRut a establecer.
     */
    public void setClienteRut(String clienteRut) {
        this.clienteRut = clienteRut;
    }
    /**
     * @return Devuelve origen.
     */
    public String getOrigen() {
        return origen;
    }
    /**
     * @param origen El origen a establecer.
     */
    public void setOrigen(String origen) {
        this.origen = origen;
    }
}
