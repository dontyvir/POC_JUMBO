package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

public class ProcModSugProductDTO implements Serializable{
      private long id_producto; // obligatorio
      private long id_producto_sug; // obligatorio
      private String action; // obligatorio
      private String direccion; // obligatorio
      private String mns_agre; // obligatorio
      private String mns_desa; // obligatorio
      private String usr_login; // obligatorio
      private long id_sug; // obligatorio
	/**
	 * 
	 */
	public ProcModSugProductDTO() {
	}
	
	/**
	 * @param id_producto
	 * @param id_producto_sug
	 * @param action
	 * @param direccion
	 * @param mns_agre
	 * @param mns_desa
	 * @param usr_login
	 * @param id_sug
	 */
	public ProcModSugProductDTO(long id_producto, long id_producto_sug, String action, String direccion, String mns_agre, String mns_desa, String usr_login, long id_sug) {
		super();
		this.id_producto = id_producto;
		this.id_producto_sug = id_producto_sug;
		this.action = action;
		this.direccion = direccion;
		this.mns_agre = mns_agre;
		this.mns_desa = mns_desa;
		this.usr_login = usr_login;
		this.id_sug = id_sug;
	}

	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @return Returns the id_producto.
	 */
	public long getId_producto() {
		return id_producto;
	}
	/**
	 * @return Returns the id_producto_sug.
	 */
	public long getId_producto_sug() {
		return id_producto_sug;
	}
	/**
	 * @param action The action to set.
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @param id_producto The id_producto to set.
	 */
	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}
	/**
	 * @param id_producto_sug The id_producto_sug to set.
	 */
	public void setId_producto_sug(long id_producto_sug) {
		this.id_producto_sug = id_producto_sug;
	}
	/**
	 * @return Returns the direccion.
	 */
	public String getDireccion() {
		return direccion;
	}
	/**
	 * @param direccion The direccion to set.
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return Returns the mns_agre.
	 */
	public String getMns_agre() {
		return mns_agre;
	}

	/**
	 * @param mns_agre The mns_agre to set.
	 */
	public void setMns_agre(String mns_agre) {
		this.mns_agre = mns_agre;
	}

	/**
	 * @return Returns the mns_desa.
	 */
	public String getMns_desa() {
		return mns_desa;
	}

	/**
	 * @param mns_desa The mns_desa to set.
	 */
	public void setMns_desa(String mns_desa) {
		this.mns_desa = mns_desa;
	}

	/**
	 * @return Returns the usr_login.
	 */
	public String getUsr_login() {
		return usr_login;
	}

	/**
	 * @param usr_login The usr_login to set.
	 */
	public void setUsr_login(String usr_login) {
		this.usr_login = usr_login;
	}



	/**
	 * @return Returns the id_sug.
	 */
	public long getId_sug() {
		return id_sug;
	}



	/**
	 * @param id_sug The id_sug to set.
	 */
	public void setId_sug(long id_sug) {
		this.id_sug = id_sug;
	}
	
      
}
