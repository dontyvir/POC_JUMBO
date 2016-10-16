package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
import java.util.List;

import cl.bbr.jumbocl.common.codes.Constantes;

public class PedidosCriteriaDTO implements Serializable {
	
	public final static String ORDEN_ID_PEDIDO 	= "P.ID_PEDIDO";
	public final static String ORDEN_FECHA_PICK 	= "JP.FECHA";
	public final static String ORDEN_CANT_PROD		= "P.CANT_PRODUCTOS";
	public final static String ORDEN_FECHA_COMPRA 	= "P.fcreacion";	
	public final static String ORDEN_HORA_INI_PICK	= "HP.HINI";
	public final static String ORDEN_HORA_FIN_PICK	= "HP.HFIN";
	public final static String ORDEN_ZONA_ORDEN		= "Z.orden_zona";
		
	public final static String ORDEN_ASCENDENTE  = "ASC";
	public final static String ORDEN_DESCENDENTE = "DESC";


	private long	 id_pedido;
	private long	 id_jpicking;
	private long	 id_estado;
	private String	 fpicking;
	private int		 cant_prods;
	private String	 fdespacho;
	private String	 hdespacho;
	private int		 pag;
	private int		 regsperpag;
	private long	 id_local;
	private long 	 id_cliente;
	private long     id_motivo;
	private long	 id_zona;
	private String	 cli_rut;
	private String	 cli_ape;
	private String	 tip_fec = "";
	private String	 fec_ini = "";
	private String	 fec_fin = "";
	private String[] filtro_estados = null;
	/*
	private boolean orderByIdAsc;
	private boolean orderByIdDesc;
	private boolean orderByFecCreacionAsc;
	private boolean orderByFecCreacionDesc;
	*/
	private List    orden_columnas;	
	private boolean sin_gestion;	
	private String  jdesp_dia;	
	private long    id_local_fact;
	private String  origen;
	private String  tipo_ve;
	private String  tipo_desp;
	private String  tipo_picking = Constantes.TIPO_PICKING_NORMAL_CTE;//N: Normal,  L: Light
	private boolean limitarFecha=true; //true: limita 6 meses, la consulta de pedidos, false: no limita la consulta de OP
	private String cli_email; //criterio para la busqueda de pedidos por email
	
	private String checkEstado69 = "";
	
	
	
	public String getCheckEstado69() {
		return checkEstado69;
	}
	public void setCheckEstado69(String checkEstado69) {
		this.checkEstado69 = checkEstado69;
	}
	/**
	 * @return Devuelve id_zona.
	 */
	public long getId_zona() {
		return id_zona;
	}
	/**
	 * @param id_zona El id_zona a establecer.
	 */
	public void setId_zona(long id_zona) {
		this.id_zona = id_zona;
	}
	/**
	 * @return Returns the origen.
	 */
	public String getOrigen() {
		return origen;
	}

	/**
	 * @param origen The origen to set.
	 */
	public void setOrigen(String origen) {
		this.origen = origen;
	}

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

	/**
	 * @return Returns the id_local_fact.
	 */
	public long getId_local_fact() {
		return id_local_fact;
	}

	/**
	 * @param id_local_fact The id_local_fact to set.
	 */
	public void setId_local_fact(long id_local_fact) {
		this.id_local_fact = id_local_fact;
	}

	public boolean isSin_gestion() {
		return sin_gestion;
	}

	public void setSin_gestion(boolean sin_gestion) {
		this.sin_gestion = sin_gestion;
	}

	public List getOrden_columnas() {
		return orden_columnas;
	}

	public void setOrden_columnas(List orden_columnas) {
		this.orden_columnas = orden_columnas;
	}

	/**
	 * 
	 */
	public PedidosCriteriaDTO() {
		super();
		
	}

	/**
	 * @param id_estado
	 * @param pag
	 * @param regsperpag
	 * @param id_pedido
	 * @param cli_rut
	 * @param cli_ape
	 * @param tip_fec
	 * @param fec_ini
	 * @param fec_fin
	 */
	public PedidosCriteriaDTO(long id_estado, int pag, int regsperpag, long id_pedido, String cli_rut, String cli_ape, 
			String tip_fec, String fec_ini, String fec_fin, long id_motivo, boolean orderByIdAsc, boolean orderByIdDesc,
			boolean orderByFecCreacionAsc, boolean orderByFecCreacionDesc) {
		super();
		this.id_estado = id_estado;
		this.pag = pag;
		this.regsperpag = regsperpag;
		this.id_pedido = id_pedido;
		this.cli_rut = cli_rut;
		this.cli_ape = cli_ape;
		this.tip_fec = tip_fec;
		this.fec_ini = fec_ini;
		this.fec_fin = fec_fin;
		this.id_motivo = id_motivo;
	}

	/**
	 * @return Returns the id_motivo.
	 */
	public long getId_motivo() {
		return id_motivo;
	}

	/**
	 * @param id_motivo The id_motivo to set.
	 */
	public void setId_motivo(long id_motivo) {
		this.id_motivo = id_motivo;
	}

	/**
	 * @return Returns the id_cliente.
	 */
	public long getId_cliente() {
		return id_cliente;
	}

	/**
	 * @param id_cliente The id_cliente to set.
	 */
	public void setId_cliente(long id_cliente) {
		this.id_cliente = id_cliente;
	}

	public long getId_local() {
		return id_local;
	}

	public void setId_local(long id_local) {
		this.id_local = id_local;
	}

	public long getId_estado() {
		return id_estado;
	}

	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}

	public int getCant_prods() {
		return cant_prods;
	}
	
	public void setCant_prods(int cant_prods) {
		this.cant_prods = cant_prods;
	}
	
	public String getFdespacho() {
		return fdespacho;
	}
	
	public void setFdespacho(String fdespacho) {
		this.fdespacho = fdespacho;
	}
	
	public String getFpicking() {
		return fpicking;
	}
	
	public void setFpicking(String fpicking) {
		this.fpicking = fpicking;
	}
	
	public String getHdespacho() {
		return hdespacho;
	}
	
	public void setHdespacho(String hdespacho) {
		this.hdespacho = hdespacho;
	}
	
	public long getId_jpicking() {
		return id_jpicking;
	}
	
	public void setId_jpicking(long id_jpicking) {
		this.id_jpicking = id_jpicking;
	}
	
	public long getId_pedido() {
		return id_pedido;
	}

	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}

	public int getPag() {
		return pag;
	}

	public void setPag(int pag) {
		this.pag = pag;
	}

	public int getRegsperpag() {
		return regsperpag;
	}

	public void setRegsperpag(int regsperpag) {
		this.regsperpag = regsperpag;
	}

	/**
	 * @return Returns the cli_ape.
	 */
	public String getCli_ape() {
		return cli_ape;
	}

	/**
	 * @param cli_ape The cli_ape to set.
	 */
	public void setCli_ape(String cli_ape) {
		this.cli_ape = cli_ape;
	}

	/**
	 * @return Returns the cli_rut.
	 */
	public String getCli_rut() {
		return cli_rut;
	}

	/**
	 * @param cli_rut The cli_rut to set.
	 */
	public void setCli_rut(String cli_rut) {
		this.cli_rut = cli_rut;
	}

	/**
	 * @return Returns the fec_fin.
	 */
	public String getFec_fin() {
		return fec_fin;
	}

	/**
	 * @param fec_fin The fec_fin to set.
	 */
	public void setFec_fin(String fec_fin) {
		this.fec_fin = fec_fin;
	}

	/**
	 * @return Returns the fec_ini.
	 */
	public String getFec_ini() {
		return fec_ini;
	}

	/**
	 * @param fec_ini The fec_ini to set.
	 */
	public void setFec_ini(String fec_ini) {
		this.fec_ini = fec_ini;
	}

	/**
	 * @return Returns the tip_fec.
	 */
	public String getTip_fec() {
		return tip_fec;
	}

	/**
	 * @param tip_fec The tip_fec to set.
	 */
	public void setTip_fec(String tip_fec) {
		this.tip_fec = tip_fec;
	}

	public String[] getFiltro_estados() {
		return filtro_estados;
	}

	public void setFiltro_estados(String[] filtro_estados) {
		this.filtro_estados = filtro_estados;
	}

	public String getJdesp_dia() {
		return jdesp_dia;
	}

	public void setJdesp_dia(String jdesp_dia) {
		this.jdesp_dia = jdesp_dia;
	}

	
    /**
     * @return Devuelve tipo_desp.
     */
    public String getTipo_desp() {
        return tipo_desp;
    }
    /**
     * @param tipo_desp El tipo_desp a establecer.
     */
    public void setTipo_desp(String tipo_desp) {
        this.tipo_desp = tipo_desp;
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
     * @return Devuelve limitarFecha.
     */
    public boolean isLimitarFecha() {
        return limitarFecha;
    }
    /**
     * @param limitarFecha El limitarFecha a establecer.
     */
    public void setLimitarFecha(boolean limitarFecha) {
        this.limitarFecha = limitarFecha;
    }
	public String getCli_email() {
		return cli_email;
	}
	public void setCli_email(String cli_email) {
		this.cli_email = cli_email;
	}
}
