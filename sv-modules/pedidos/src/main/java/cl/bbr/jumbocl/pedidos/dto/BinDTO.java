package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BinDTO  implements Serializable{
	
	private long   id_bp;
	private long   id_pedido;
	private long   id_ronda;
	private String cod_bin;
	private String cod_ubicacion;
	private String cod_sello1;
	private String cod_sello2;
	private String tipo;
	private long   id_sector_picking;
	private String nombre_sector_picking;
	private int    cant_prod_audit;
	private String auditor;
	
    //Jean
    private List detallePicking;

	//	---------- mod_feb09 - ini------------------------	
	private String visualizado;
	
	/**
	 * @return Returns the visualizado.
	 */
	public String getVisualizado() {
		return visualizado;
	}
	/**
	 * @param visualizado The visualizado to set.
	 */
	public void setVisualizado(String visualizado) {
		this.visualizado = visualizado;
	}

	//	---------- mod_feb09 - fin------------------------
	public BinDTO(){
        detallePicking = new ArrayList();
	}


	public String getCod_bin() {
		return cod_bin;
	}


	public void setCod_bin(String cod_bin) {
		this.cod_bin = cod_bin;
	}


	public long getId_bp() {
		return id_bp;
	}


	public void setId_bp(long id_bp) {
		this.id_bp = id_bp;
	}


	public long getId_pedido() {
		return id_pedido;
	}


	public void setId_pedido(long id_pedido) {
		this.id_pedido = id_pedido;
	}


	public long getId_ronda() {
		return id_ronda;
	}


	public void setId_ronda(long id_ronda) {
		this.id_ronda = id_ronda;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	


    /**
     * @return Devuelve id_sector_picking.
     */
    public long getId_sector_picking() {
        return id_sector_picking;
    }
    /**
     * @return Devuelve nombre_sector_picking.
     */
    public String getNombre_sector_picking() {
        return nombre_sector_picking;
    }
    /**
     * @param id_sector_picking El id_sector_picking a establecer.
     */
    public void setId_sector_picking(long id_sector_picking) {
        this.id_sector_picking = id_sector_picking;
    }
    /**
     * @param nombre_sector_picking El nombre_sector_picking a establecer.
     */
    public void setNombre_sector_picking(String nombre_sector_picking) {
        this.nombre_sector_picking = nombre_sector_picking;
    }
    
    public void addDetallePicking(DetallePickingDTO det){
        this.detallePicking.add(det);
    }
    /**
     * @return Returns the detallePicking.
     */
    public List getDetallePicking() {
        return detallePicking;
    }
    /**
     * @return Devuelve cant_prod_audit.
     */
    public int getCant_prod_audit() {
        return cant_prod_audit;
    }
    /**
     * @param cant_prod_audit El cant_prod_audit a establecer.
     */
    public void setCant_prod_audit(int cant_prod_audit) {
        this.cant_prod_audit = cant_prod_audit;
    }
    /**
     * @param detallePicking El detallePicking a establecer.
     */
    public void setDetallePicking(List detallePicking) {
        this.detallePicking = detallePicking;
    }
    /**
     * @return Devuelve cod_sello1.
     */
    public String getCod_sello1() {
        return cod_sello1;
    }
    /**
     * @return Devuelve cod_sello2.
     */
    public String getCod_sello2() {
        return cod_sello2;
    }
    /**
     * @return Devuelve cod_ubicacion.
     */
    public String getCod_ubicacion() {
        return cod_ubicacion;
    }
    /**
     * @param cod_sello1 El cod_sello1 a establecer.
     */
    public void setCod_sello1(String cod_sello1) {
        this.cod_sello1 = cod_sello1;
    }
    /**
     * @param cod_sello2 El cod_sello2 a establecer.
     */
    public void setCod_sello2(String cod_sello2) {
        this.cod_sello2 = cod_sello2;
    }
    /**
     * @param cod_ubicacion El cod_ubicacion a establecer.
     */
    public void setCod_ubicacion(String cod_ubicacion) {
        this.cod_ubicacion = cod_ubicacion;
    }
    /**
     * @return Devuelve auditor.
     */
    public String getAuditor() {
        return auditor;
    }
    /**
     * @param auditor El auditor a establecer.
     */
    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }
}
