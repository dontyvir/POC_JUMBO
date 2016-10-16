/**
 * MatrizSeccionEntity.java
 * Creado   : 26-feb-2007
 * Historia : 26-feb-2007 Version 1.0
 * Historia : 17-jun-2007 version 1.1
 * Version  : 1.1
 * BBR
 */
package cl.bbr.irs.promolib.entity;

import java.io.Serializable;

/**
 * @author JORGE SILVA
 *
 */
public class MatrizSeccionEntity implements Serializable {

	private int id_matrizseccion;
	private int seccion;
	private int local;
	private boolean lunes;
	private boolean martes;
	private boolean miercoles;
	private boolean jueves;
	private boolean viernes;
	private boolean sabado;
	private boolean domingo;
	private boolean esp1;
	private boolean esp2;
	private boolean esp3;
	private boolean esp4;
	private boolean esp5;
	private boolean esp6;
	private boolean esp7;
	private boolean esp8;
	
	/**
	 * <b>Description: </b>
	 */
	public MatrizSeccionEntity() {}

	
	/**
	 * <b>Description: </b>
	 * @param id_matrizseccion
	 * @param seccion Codigo de seccion
	 * @param local Local asociado a la promocion
	 * @param lunes Flag activo
	 * @param martes Flag activo
	 * @param miercoles Flag activo
	 * @param jueves Flag activo
	 * @param viernes Flag activo
	 * @param sabado Flag activo
	 * @param domingo Flag activo
	 * @param esp1 Flag activo
	 * @param esp2 Flag activo
	 * @param esp3 Flag activo
	 * @param esp4 Flag activo
	 * @param esp5 Flag activo
	 * @param esp6 Flag activo
	 * @param esp7 Flag activo
	 * @param esp8 Flag activo
	 */
	public MatrizSeccionEntity(int id_matrizseccion, int seccion, int local, boolean lunes, boolean martes, boolean miercoles, boolean jueves, boolean viernes, boolean sabado, boolean domingo, boolean esp1, boolean esp2, boolean esp3, boolean esp4, boolean esp5, boolean esp6, boolean esp7, boolean esp8) {
		this.id_matrizseccion = id_matrizseccion;
		this.seccion = seccion;
		this.local = local;
		this.lunes = lunes;
		this.martes = martes;
		this.miercoles = miercoles;
		this.jueves = jueves;
		this.viernes = viernes;
		this.sabado = sabado;
		this.domingo = domingo;
		this.esp1 = esp1;
		this.esp2 = esp2;
		this.esp3 = esp3;
		this.esp4 = esp4;
		this.esp5 = esp5;
		this.esp6 = esp6;
		this.esp7 = esp7;
		this.esp8 = esp8;
	}


	/**
	 * @return Returns the domingo.
	 */
	public boolean isDomingo() {
		return domingo;
	}

	/**
	 * @param domingo The domingo to set.
	 */
	public void setDomingo(boolean domingo) {
		this.domingo = domingo;
	}

	/**
	 * @return Returns the esp1.
	 */
	public boolean isEsp1() {
		return esp1;
	}

	/**
	 * @param esp1 The esp1 to set.
	 */
	public void setEsp1(boolean esp1) {
		this.esp1 = esp1;
	}

	/**
	 * @return Returns the esp2.
	 */
	public boolean isEsp2() {
		return esp2;
	}

	/**
	 * @param esp2 The esp2 to set.
	 */
	public void setEsp2(boolean esp2) {
		this.esp2 = esp2;
	}

	/**
	 * @return Returns the esp3.
	 */
	public boolean isEsp3() {
		return esp3;
	}

	/**
	 * @param esp3 The esp3 to set.
	 */
	public void setEsp3(boolean esp3) {
		this.esp3 = esp3;
	}

	/**
	 * @return Returns the esp4.
	 */
	public boolean isEsp4() {
		return esp4;
	}

	/**
	 * @param esp4 The esp4 to set.
	 */
	public void setEsp4(boolean esp4) {
		this.esp4 = esp4;
	}

	/**
	 * @return Returns the esp5.
	 */
	public boolean isEsp5() {
		return esp5;
	}

	/**
	 * @param esp5 The esp5 to set.
	 */
	public void setEsp5(boolean esp5) {
		this.esp5 = esp5;
	}

	/**
	 * @return Returns the esp6.
	 */
	public boolean isEsp6() {
		return esp6;
	}

	/**
	 * @param esp6 The esp6 to set.
	 */
	public void setEsp6(boolean esp6) {
		this.esp6 = esp6;
	}

	/**
	 * @return Returns the esp7.
	 */
	public boolean isEsp7() {
		return esp7;
	}

	/**
	 * @param esp7 The esp7 to set.
	 */
	public void setEsp7(boolean esp7) {
		this.esp7 = esp7;
	}

	/**
	 * @return Returns the esp8.
	 */
	public boolean isEsp8() {
		return esp8;
	}

	/**
	 * @param esp8 The esp8 to set.
	 */
	public void setEsp8(boolean esp8) {
		this.esp8 = esp8;
	}

	/**
	 * @return Returns the jueves.
	 */
	public boolean isJueves() {
		return jueves;
	}

	/**
	 * @param jueves The jueves to set.
	 */
	public void setJueves(boolean jueves) {
		this.jueves = jueves;
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
	 * @return Returns the lunes.
	 */
	public boolean isLunes() {
		return lunes;
	}

	/**
	 * @param lunes The lunes to set.
	 */
	public void setLunes(boolean lunes) {
		this.lunes = lunes;
	}

	/**
	 * @return Returns the martes.
	 */
	public boolean isMartes() {
		return martes;
	}

	/**
	 * @param martes The martes to set.
	 */
	public void setMartes(boolean martes) {
		this.martes = martes;
	}

	/**
	 * @return Returns the miercoles.
	 */
	public boolean isMiercoles() {
		return miercoles;
	}

	/**
	 * @param miercoles The miercoles to set.
	 */
	public void setMiercoles(boolean miercoles) {
		this.miercoles = miercoles;
	}

	/**
	 * @return Returns the sabado.
	 */
	public boolean isSabado() {
		return sabado;
	}

	/**
	 * @param sabado The sabado to set.
	 */
	public void setSabado(boolean sabado) {
		this.sabado = sabado;
	}

	/**
	 * @return Returns the seccion.
	 */
	public int getSeccion() {
		return seccion;
	}

	/**
	 * @param seccion The seccion to set.
	 */
	public void setSeccion(int seccion) {
		this.seccion = seccion;
	}

	/**
	 * @return Returns the viernes.
	 */
	public boolean isViernes() {
		return viernes;
	}

	/**
	 * @param viernes The viernes to set.
	 */
	public void setViernes(boolean viernes) {
		this.viernes = viernes;
	}

	/**
	 * @return Returns the id_matrizseccion.
	 */
	public int getId_matrizseccion() {
		return id_matrizseccion;
	}

	/**
	 * @param id_matrizseccion The id_matrizseccion to set.
	 */
	public void setId_matrizseccion(int id_matrizseccion) {
		this.id_matrizseccion = id_matrizseccion;
	}
	
}
