package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class TPRegistroPickingDTO implements Serializable{
	private int ronda;
	private String usuario;
	private int perfil;
	private String hora;
	//	---------- mod_ene09 - ini------------------------
	private String usuario_fiscalizador;
	private int e1;
	private int e2;
	private int e3;
	private int e4;
	private int e5;
	private int e6;
	private int e7;	
	
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
	 * @return Returns the usuario_fiscalizador.
	 */
	public String getUsuario_fiscalizador() {
		return usuario_fiscalizador;
	}
	/**
	 * @param usuario_fiscalizador The usuario_fiscalizador to set.
	 */
	public void setUsuario_fiscalizador(String usuario_fiscalizador) {
		this.usuario_fiscalizador = usuario_fiscalizador;
	}
	//	---------- mod_ene09 - fin------------------------
	
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public int getPerfil() {
		return perfil;
	}
	public void setPerfil(int perfil) {
		this.perfil = perfil;
	}
	public int getRonda() {
		return ronda;
	}
	public void setRonda(int ronda) {
		this.ronda = ronda;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}
