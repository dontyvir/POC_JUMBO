package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

/**
 * Clase que modifica la información entre item y producto. 
 * @author BBR
 *
 */
public class ProcModGenericItemDTO implements Serializable{
      private String action; // obligatorio
      private long id_prod_item; // obligatorio
      private long id_prod_gen; // obligatorio
      private String atrdiff_val; // obligatorio
      private String mns_agreg;
      private String mns_desa;
      private String usr_login;
	
    /**
	 * Constructor inicial. 
	 */
	public ProcModGenericItemDTO() {
	}
	
	/**
	 * @param action
	 * @param id_prod_item
	 * @param id_prod_gen
	 * @param atrdiff_val
	 * @param mns_agreg
	 * @param mns_desa
	 * @param usr_login
	 */
	public ProcModGenericItemDTO(String action , long id_prod_item , long id_prod_gen , String atrdiff_val , 
			String mns_agreg , String mns_desa , String usr_login) {
		super();
		this.action = action;
		this.id_prod_item = id_prod_item;
		this.id_prod_gen = id_prod_gen;
		this.atrdiff_val = atrdiff_val;
		this.mns_agreg = mns_agreg;
		this.mns_desa = mns_desa;
		this.usr_login = usr_login;
	}

	/**
	 * @return Retorna action.
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @return Retorna atrdiff_val.
	 */
	public String getAtrdiff_val() {
		return atrdiff_val;
	}
	/**
	 * @return Retorna id_prod_item.
	 */
	public long getId_prod_item() {
		return id_prod_item;
	}
	/**
	 * @param action , action a modificar.
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @param atrdiff_val , atrdiff_val a modificar.
	 */
	public void setAtrdiff_val(String atrdiff_val) {
		this.atrdiff_val = atrdiff_val;
	}
	/**
	 * @param id_prod_item , id_prod_item a modificar.
	 */
	public void setId_prod_item(long id_prod_item) {
		this.id_prod_item = id_prod_item;
	}
	/**
	 * @return Retorna id_prod_gen.
	 */
	public long getId_prod_gen() {
		return id_prod_gen;
	}
	/**
	 * @param id_prod_gen , id_prod_gen a modificar.
	 */
	public void setId_prod_gen(long id_prod_gen) {
		this.id_prod_gen = id_prod_gen;
	}
	/**
	 * @return Retorna mns_agreg.
	 */
	public String getMns_agreg() {
		return mns_agreg;
	}
	/**
	 * @param mns_agreg , mns_agreg a modificar.
	 */
	public void setMns_agreg(String mns_agreg) {
		this.mns_agreg = mns_agreg;
	}
	/**
	 * @return Retorna mns_desa.
	 */
	public String getMns_desa() {
		return mns_desa;
	}
	/**
	 * @param mns_desa , mns_desa a modificar.
	 */
	public void setMns_desa(String mns_desa) {
		this.mns_desa = mns_desa;
	}
	/**
	 * @return Retorna usr_login.
	 */
	public String getUsr_login() {
		return usr_login;
	}
	/**
	 * @param usr_login , usr_login a modificar.
	 */
	public void setUsr_login(String usr_login) {
		this.usr_login = usr_login;
	}
	
      
}
