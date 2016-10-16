package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
import java.util.List;

public class RondasCriteriaDTO implements Serializable {
	
	public final static String ORDEN_NJPICK     = "r.id_jpicking";
	public final static String ORDEN_ESTADO     = "r.id_estado";
	public final static String ORDEN_SECTOR     = "s.nombre";
	public final static String ORDEN_FECHA_FIN_PICKING = "r.ffin_picking";
	public final static String ORDEN_ASCENDENTE  = "ASC";
	public final static String ORDEN_DESCENDENTE = "DESC";
	
	private long   id_ronda;
	private long   id_jornada;
	private String f_ronda;
	private long   id_sector;
	private long   id_estado;
	private int	   pag;
	private int    regsperpag;
	private long   id_local;
	private List   orden_columnas;
	private String tipo_ve; 	
	private long   id_zona;
	private int    pagina_seleccionada;
	private String esPickingLight = "N";
	
	
	/**
	 * @return Returns the pagina_seleccionada.
	 */
	public int getPagina_seleccionada() {
		return pagina_seleccionada;
	}

	/**
	 * @param pagina_seleccionada The pagina_seleccionada to set.
	 */
	public void setPagina_seleccionada(int pagina_seleccionada) {
		this.pagina_seleccionada = pagina_seleccionada;
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

	public List getOrden_columnas() {
		return orden_columnas;
	}

	public void setOrden_columnas(List orden_columnas) {
		this.orden_columnas = orden_columnas;
	}

	public RondasCriteriaDTO(){
		
	}
	
	public long getId_local() {
		return id_local;
	}
	public void setId_local(long id_local) {
		this.id_local = id_local;
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
	public String getF_ronda() {
		return f_ronda;
	}
	public void setF_ronda(String f_ronda) {
		this.f_ronda = f_ronda;
	}
	public long getId_estado() {
		return id_estado;
	}
	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}
	public long getId_jornada() {
		return id_jornada;
	}
	public void setId_jornada(long id_jornada) {
		this.id_jornada = id_jornada;
	}
	public long getId_ronda() {
		return id_ronda;
	}
	public void setId_ronda(long id_ronda) {
		this.id_ronda = id_ronda;
	}
	public long getId_sector() {
		return id_sector;
	}
	public void setId_sector(long id_sector) {
		this.id_sector = id_sector;
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
     * @return Devuelve esPickingLight.
     */
    public String getEsPickingLight() {
        return esPickingLight;
    }
    /**
     * @param esPickingLight El esPickingLight a establecer.
     */
    public void setEsPickingLight(String esPickingLight) {
        this.esPickingLight = esPickingLight;
    }
}
