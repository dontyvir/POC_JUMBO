/**
 * PromoSeccionEntity.java
 * Creado   : 26-feb-2007
 * Historia : 26-feb-2007 Version 1.0
 * Historia : 17-jun-2007 version 1.1
 * Version  : 1.1
 * BBR
 */
package cl.bbr.irs.promolib.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author JORGE SILVA
 *
 */
public class PromoSeccionEntity implements Serializable {

	private int id_promoseccion;
	private int tipo;
	private int local;
	private int codigo;
	private ArrayList secciones =  new ArrayList();
	
	/**
	 * <b>Description: </b>
	 */
	public PromoSeccionEntity() {}

	
	/**
	 * <b>Description: </b>
	 * @param id_promoseccion
	 * @param tipo Tipo de promocion (lunes,martes...especial8)
	 * @param local Local asociado a la promocion
	 * @param codigo Codigo de la promocion
	 */
	public PromoSeccionEntity(int id_promoseccion, int tipo, int local, int codigo) {
		this.id_promoseccion = id_promoseccion;
		this.tipo = tipo;
		this.local = local;
		this.codigo = codigo;
	}


	/**
	 * @return Returns the codigo.
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return Returns the local.
	 */
	public int getLocal() {
		return local;
	}

	/**
	 * @param local The local to set.
	 */
	public void setLocal(int local) {
		this.local = local;
	}

	/**
	 * @return Returns the tipo.
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return Returns the id_promoseccion.
	 */
	public int getId_promoseccion() {
		return id_promoseccion;
	}

	/**
	 * @param id_promoseccion The id_promoseccion to set.
	 */
	public void setId_promoseccion(int id_promoseccion) {
		this.id_promoseccion = id_promoseccion;
	}
	
		
	public ArrayList getSecciones() {
		return secciones;
	}


	public void setSecciones(ArrayList secciones) {
		this.secciones = secciones;
	}
	
	
}
