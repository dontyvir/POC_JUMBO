/*
 * Creado el 12-sep-2012
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.jumbocl.common.model;

import java.util.Date;

/**
 * @author Sebastian
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class ParametroEntity {

	private int id_local;
	
	private int id_umbral;
	
	private Date fecha_modi;

	private String id_usuario;

	private double u_unidad;
	
	private double u_monto;
	
	private String mod_dato;
	
	private String u_activacion;
	
	private String nom_local;

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
	 * @param fecha_modi El fecha_modi a establecer.
	 */
	public void setFecha_modi(Date fecha_modi) {
		this.fecha_modi = fecha_modi;
	}

	/**
	 * @return Devuelve fecha_modi.
	 */
	public Date getFecha_modi() {
		return fecha_modi;
	}

	/**
	 * @param id_usuario El id_usuario a establecer.
	 */
	public void setId_usuario(String id_usuario) {
		this.id_usuario = id_usuario;
	}

	/**
	 * @return Devuelve id_usuario.
	 */
	public String getId_usuario() {
		return id_usuario;
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
	 * @param mod_dato El mod_dato a establecer.
	 */
	public void setMod_dato(String mod_dato) {
		this.mod_dato = mod_dato;
	}

	/**
	 * @return Devuelve mod_dato.
	 */
	public String getMod_dato() {
		return mod_dato;
	}

	/**
	 * @param u_activacion El u_activacion a establecer.
	 */
	public void setU_activacion(String u_activacion) {
		this.u_activacion = u_activacion;
	}

	/**
	 * @return Devuelve u_activacion.
	 */
	public String getU_activacion() {
		return u_activacion;
	}

	/**
	 * @param id_local El id_local a establecer.
	 */
	public void setId_local(int id_local) {
		this.id_local = id_local;
	}

	/**
	 * @return Devuelve id_local.
	 */
	public int getId_local() {
		return id_local;
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
	
}
