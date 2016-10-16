package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

public class ProcModMPVProductDTO implements Serializable{
	private final static long serialVersionUID = 1;
	
      private String action; // obligatorio
      private String cod_prod_sap; // obligatorio
      private long id_prod; // obligatorio
      private long id_user; // obligatorio
      private String usr_login; // obligatorio
      private String mnsAgreg; // obligatorio
      private String mnsElim; // obligatorio
      

	
	/**
	 * @param action
	 * @param cod_prod_sap
	 * @param id_prod
	 * @param id_user
	 * @param usr_login
	 * @param mnsAgreg
	 * @param mnsElim
	 */
	public ProcModMPVProductDTO(String action, String cod_prod_sap, long id_prod, long id_user, String usr_login, String mnsAgreg, String mnsElim) {
		this.action = action;
		this.cod_prod_sap = cod_prod_sap;
		this.id_prod = id_prod;
		this.id_user = id_user;
		this.usr_login = usr_login;
		this.mnsAgreg = mnsAgreg;
		this.mnsElim = mnsElim;
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
	 * @return Returns the id_user.
	 */
	public long getId_user() {
		return id_user;
	}
	/**
	 * @param id_user The id_user to set.
	 */
	public void setId_user(long id_user) {
		this.id_user = id_user;
	}
	/**
	 * @return Returns the id_prod.
	 */
	public long getId_prod() {
		return id_prod;
	}
	/**
	 * @param id_prod The id_prod to set.
	 */
	public void setId_prod(long id_prod) {
		this.id_prod = id_prod;
	}
	/**
	 * 
	 */
	public ProcModMPVProductDTO() {
	}
	/**
	 * @param action
	 * @param cod_prod_sap
	 */
	public ProcModMPVProductDTO(String action, String cod_prod_sap) {
		this.action = action;
		this.cod_prod_sap = cod_prod_sap;
	}
	/**
	 * @return Returns the action.
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @return Returns the cod_prod_sap.
	 */
	public String getCod_prod_sap() {
		return cod_prod_sap;
	}
	/**
	 * @param action The action to set.
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @param cod_prod_sap The cod_prod_sap to set.
	 */
	public void setCod_prod_sap(String cod_prod_sap) {
		this.cod_prod_sap = cod_prod_sap;
	}
	/**
	 * @return Returns the mnsAgreg.
	 */
	public String getMnsAgreg() {
		return mnsAgreg;
	}
	/**
	 * @param mnsAgreg The mnsAgreg to set.
	 */
	public void setMnsAgreg(String mnsAgreg) {
		this.mnsAgreg = mnsAgreg;
	}
	/**
	 * @return Returns the mnsElim.
	 */
	public String getMnsElim() {
		return mnsElim;
	}
	/**
	 * @param mnsElim The mnsElim to set.
	 */
	public void setMnsElim(String mnsElim) {
		this.mnsElim = mnsElim;
	}
      
}
