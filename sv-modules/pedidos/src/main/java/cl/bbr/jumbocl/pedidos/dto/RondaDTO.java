package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class RondaDTO implements Serializable {
	
	private long	  id_ronda;
	private long	  id_estado;
	private long	  id_jpicking;
	private String	  estado;
	private long      id_sector;
	private String    sector;
	private double	  cant_prods;
	private String	  f_creacion;
	private String	  h_creacion;
	private long	  pickeador;
	private long	  id_local;
	private Timestamp fini_picking;
	private Timestamp ffin_picking;
	private String    tipo_ve;
	private Timestamp    fecha_inico_ronda_pkl;
	private Timestamp    fecha_imp_listado_pkl;
	
	//	---------- mod_ene09 - ini------------------------
	private long fiscalizador;
	private String tipo_picking;
	private int e1;
	private int e2;
	private int e3;
	private int e4;
	private int e5;
	private int e6;
	private int e7;
	//	---------- mod_ene09 - fin------------------------
	
	
	public RondaDTO(){
		
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
	 * @return Returns the id_sector.
	 */
	public long getId_sector() {
		return id_sector;
	}

	/**
	 * @param id_sector The id_sector to set.
	 */
	public void setId_sector(long id_sector) {
		this.id_sector = id_sector;
	}

	public long getId_jpicking() {
		return id_jpicking;
	}

	public void setId_jpicking(long id_jpicking) {
		this.id_jpicking = id_jpicking;
	}

	public long getId_estado() {
		return id_estado;
	}
	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public double getCant_prods() {
		return cant_prods;
	}
	public void setCant_prods(double cant_prods) {
		this.cant_prods = cant_prods;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getF_creacion() {
		return f_creacion;
	}
	public void setF_creacion(String f_creacion) {
		this.f_creacion = f_creacion;
	}

	public String getH_creacion() {
		return h_creacion;
	}
	public void setH_creacion(String h_creacion) {
		this.h_creacion = h_creacion;
	}
	public long getId_ronda() {
		return id_ronda;
	}
	public void setId_ronda(long id_ronda) {
		this.id_ronda = id_ronda;
	}
	public long getPickeador() {
		return pickeador;
	}
	public void setPickeador(long pickeador) {
		this.pickeador = pickeador;
	}

	public long getId_local() {
		return id_local;
	}

	public void setId_local(long id_local) {
		this.id_local = id_local;
	}

	public Date getFfin_picking() {
		return ffin_picking;
	}

	public void setFfin_picking(Timestamp ffin_picking) {
		this.ffin_picking = ffin_picking;
	}

	public Date getFini_picking() {
		return fini_picking;
	}

	public void setFini_picking(Timestamp fini_picking) {
		this.fini_picking = fini_picking;
	}

	//	---------- mod_ene09 - ini------------------------
		
	/**
	 * @return Returns the e1.
	 */
	public int getE1() {
		return e1;
	}
	/**
	 * @param e1 The e1 to set.
	 */
	public void setE1(int e1) {
		this.e1 = e1;
	}
	/**
	 * @return Returns the e2.
	 */
	public int getE2() {
		return e2;
	}
	/**
	 * @param e2 The e2 to set.
	 */
	public void setE2(int e2) {
		this.e2 = e2;
	}
	/**
	 * @return Returns the e3.
	 */
	public int getE3() {
		return e3;
	}
	/**
	 * @param e3 The e3 to set.
	 */
	public void setE3(int e3) {
		this.e3 = e3;
	}
	/**
	 * @return Returns the e4.
	 */
	public int getE4() {
		return e4;
	}
	/**
	 * @param e4 The e4 to set.
	 */
	public void setE4(int e4) {
		this.e4 = e4;
	}
	/**
	 * @return Returns the e5.
	 */
	public int getE5() {
		return e5;
	}
	/**
	 * @param e5 The e5 to set.
	 */
	public void setE5(int e5) {
		this.e5 = e5;
	}
	/**
	 * @return Returns the e6.
	 */
	public int getE6() {
		return e6;
	}
	/**
	 * @param e6 The e6 to set.
	 */
	public void setE6(int e6) {
		this.e6 = e6;
	}
	/**
	 * @return Returns the e7.
	 */
	public int getE7() {
		return e7;
	}
	/**
	 * @param e7 The e7 to set.
	 */
	public void setE7(int e7) {
		this.e7 = e7;
	}
	/**
	 * @return Returns the fiscalizador.
	 */
	public long getFiscalizador() {
		return fiscalizador;
	}
	/**
	 * @param fiscalizador The fiscalizador to set.
	 */
	public void setFiscalizador(long fiscalizador) {
		this.fiscalizador = fiscalizador;
	}
	/**
	 * @return Returns the tipo_picking.
	 */
	public String getTipo_picking() {
		return tipo_picking;
	}
	/**
	 * @param tipo_picking The tipo_picking to set.
	 */
	public void setTipo_picking(String tipo_picking) {
		this.tipo_picking = tipo_picking;
	}

	//	---------- mod_ene09 - fin------------------------	
    /**
     * @return Devuelve fecha_imp_listado_pkl.
     */
    public Timestamp getFecha_imp_listado_pkl() {
        return fecha_imp_listado_pkl;
    }
    /**
     * @return Devuelve fecha_inico_ronda_pkl.
     */
    public Timestamp getFecha_inico_ronda_pkl() {
        return fecha_inico_ronda_pkl;
    }
    /**
     * @param fecha_imp_listado_pkl El fecha_imp_listado_pkl a establecer.
     */
    public void setFecha_imp_listado_pkl(Timestamp fecha_imp_listado_pkl) {
        this.fecha_imp_listado_pkl = fecha_imp_listado_pkl;
    }
    /**
     * @param fecha_inico_ronda_pkl El fecha_inico_ronda_pkl a establecer.
     */
    public void setFecha_inico_ronda_pkl(Timestamp fecha_inico_ronda_pkl) {
        this.fecha_inico_ronda_pkl = fecha_inico_ronda_pkl;
    }
}
