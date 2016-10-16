/*
 * Creado el 11-sep-2012
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de c�digo - Plantillas de c�digo
 */
package cl.cencosud.jumbocl.umbrales.dto;
import java.io.Serializable;

public class UmbralDTO implements Serializable {
	private int pagina;
	private int regsperpage;
	private boolean pag_activa;
	private int id_local;
	private double u_unidad;
	private double u_monto;
	private String u_activacion;
	private String nom_local;
	private String fecha_modi;
	private int id_umbral;
	
		
	/**
	 * 	@param int pagina;
	 	@param int regsperpage;
		@param boolean pag_activa;
		@param int id_local;
		@param double u_unidad;
		@param double u_monto;
		@param String u_activacion;
		@param String nom_local;
		@param String fecha_modi;
		@param int id_umbral;
	 *
	 */
	
	
	
	public UmbralDTO(double u_unidad , double u_monto){
		this.u_unidad = u_unidad;
		this.u_monto = u_monto;
		
	}
	
	
	
	public UmbralDTO() {
	}

		public UmbralDTO(int pagina, int id_local, int regsperpage, boolean pag_activa) {
		this.pagina = pagina;
		this.id_local = id_local;
		this.regsperpage = regsperpage;
		this.pag_activa = pag_activa;
				
	}

	
		
public UmbralDTO ( int pagina , int regsperpage,boolean pag_activa,
		int id_local,int u_unidad,int u_monto,String u_activacion,String nom_local)
{
	this.pagina = pagina;
	this.id_local = id_local;
	this.regsperpage = regsperpage;
	this.pag_activa = pag_activa;
	this.id_local = id_local;
	this.u_unidad = u_unidad;
	this.u_monto  = u_monto;
	this.u_activacion = u_activacion;
	this.nom_local = nom_local;
	
}
	
	
	
	
	/**
	 * @return Returns the pag_activa.
	 */
	public boolean isPag_activa() {
		return pag_activa;
	}

	/**
	 * @return Returns the pagina.
	 */
	public int getPagina() {
		return pagina;
	}

	/**
	 * @return Returns the regsperpage.
	 */
	public int getRegsperpage() {
		return regsperpage;
	}

	
	/**
	 * @param pag_activa The pag_activa to set.
	 */
	public void setPag_activa(boolean pag_activa) {
		this.pag_activa = pag_activa;
	}

	/**
	 * @param pagina The pagina to set.
	 */
	public void setPagina(int pagina) {
		this.pagina = pagina;
	}

	/**
	 * @param regsperpage The regsperpage to set.
	 */
	public void setRegsperpage(int regsperpage) {
		this.regsperpage = regsperpage;
	}
	/**
	 * @return Returns the id_local.
	 */
	public int getId_local() {
		return id_local;
	}
	/**
	 * @param rut The id_local to set.
	 */
	public void setId_local(int id_local) {
		this.id_local = id_local;
	}

	/**
	 * @param u_unidad El u_unidad a establecer.
	 */
	public void setU_unidad(double u_unidad) {
		this.u_unidad = u_unidad;
	}

	/**
	 * @return Devuelve u_unidad.
	 */
	public double getU_unidad() {
		return u_unidad;
	}

	/**
	 * @param u_monto El u_monto a establecer.
	 */
	public void setU_monto(double u_monto) {
		this.u_monto = u_monto;
	}

	/**
	 * @return Devuelve u_monto.
	 */
	public double getU_monto() {
		return u_monto;
	}

	/**
	 * @param id_activacion El id_activacion a establecer.
	 */
	public void setU_activacion(String u_activacion) {
		this.u_activacion = u_activacion;
	}

	/**
	 * @return Devuelve id_activacion.
	 */
	public String getU_activacion() {
		return u_activacion;
	}

	/**
	 * @param nom_local El nom_local a establecer.
	 */
	public void setNom_local(String nom_local) {
		this.nom_local = nom_local;
	}

	/**
	 * @return Devuelve nom_local.
	 */
	public String getNom_local() {
		return nom_local;
	}

	/**
	 * @param id_umbral El id_umbral a establecer.
	 */
	public void setId_umbral(int id_umbral) {
		this.id_umbral = id_umbral;
	}

	/**
	 * @return Devuelve id_umbral.
	 */
	public int getId_umbral() {
		return id_umbral;
	}

	/**
	 * @param nom_local El nom_local a establecer.
	 */
	public void setFecha_modi(String fecha_modi) {
		this.fecha_modi = fecha_modi;
	}

	/**
	 * @return Devuelve nom_local.
	 */
	public String getFecha_modi() {
		return fecha_modi;
	}

	
}
