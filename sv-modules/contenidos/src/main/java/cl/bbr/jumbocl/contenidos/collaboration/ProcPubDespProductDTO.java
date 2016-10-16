package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

public class ProcPubDespProductDTO implements Serializable{
	private final static long serialVersionUID = 1;
	
      private long id_producto; // obligatorio
      private int action; // obligatorio
      private String generico;
      private String tipo = "";
      private String idMarca = "";
      private String desCor = "";
      private String desLar = "";
      private String uniMed = "";
      private double conten = 0;
      private String admCom = "";
      private String esPrep = "";
      private double intVal = 0;
      private double intMax = 0;
      private long id_usr;
      private String usr_login = "";
      private String mensPubl = "";
      private String mensDesp = "";
      private String motivo = "";
      private long   id_motivo = 0L;

      
	public long getId_motivo() {
		return id_motivo;
	}
	public void setId_motivo(long id_motivo) {
		this.id_motivo = id_motivo;
	}
	/**
	 * @return Returns the id_usr.
	 */
	public long getId_usr() {
		return id_usr;
	}
	/**
	 * @param id_usr The id_usr to set.
	 */
	public void setId_usr(long id_usr) {
		this.id_usr = id_usr;
	}
	
	/**
	 * @param id_producto
	 * @param action
	 * @param generico
	 * @param tipo
	 * @param idMarca
	 * @param desCor
	 * @param desLar
	 * @param uniMed
	 * @param conten
	 * @param admCom
	 * @param esPrep
	 * @param intVal
	 * @param intMax
	 * @param id_usr
	 * @param usr_login
	 * @param mensPubl
	 * @param mensDesp
	 * @param motivo
	 */
	public ProcPubDespProductDTO(long id_producto, int action, String generico, String tipo, String idMarca, 
			String desCor, String desLar, String uniMed, double conten, String admCom, String esPrep, 
			double intVal, double intMax, long id_usr, String usr_login, String mensPubl, String mensDesp, 
			String motivo) {
		super();
		this.id_producto = id_producto;
		this.action = action;
		this.generico = generico;
		this.tipo = tipo;
		this.idMarca = idMarca;
		this.desCor = desCor;
		this.desLar = desLar;
		this.uniMed = uniMed;
		this.conten = conten;
		this.admCom = admCom;
		this.esPrep = esPrep;
		this.intVal = intVal;
		this.intMax = intMax;
		this.id_usr = id_usr;
		this.usr_login = usr_login;
		this.mensPubl = mensPubl;
		this.mensDesp = mensDesp;
		this.motivo = motivo;
	}
	/**
	 * @return Returns the generico.
	 */
	public String getGenerico() {
		return generico;
	}
	/**
	 * @param generico The generico to set.
	 */
	public void setGenerico(String generico) {
		this.generico = generico;
	}
	/**
	 * 
	 */
	public ProcPubDespProductDTO() {
	}
	/**
	 * @return Returns the action.
	 */
	public int getAction() {
		return action;
	}
	/**
	 * @return Returns the id_producto.
	 */
	public long getId_producto() {
		return id_producto;
	}
	/**
	 * @param action The action to set.
	 */
	public void setAction(int action) {
		this.action = action;
	}
	/**
	 * @param id_producto The id_producto to set.
	 */
	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}
	/**
	 * @return Returns the admCom.
	 */
	public String getAdmCom() {
		return admCom;
	}
	/**
	 * @param admCom The admCom to set.
	 */
	public void setAdmCom(String admCom) {
		this.admCom = admCom;
	}
	/**
	 * @return Returns the conten.
	 */
	public double getConten() {
		return conten;
	}
	/**
	 * @param conten The conten to set.
	 */
	public void setConten(double conten) {
		this.conten = conten;
	}
	/**
	 * @return Returns the desCor.
	 */
	public String getDesCor() {
		return desCor;
	}
	/**
	 * @param desCor The desCor to set.
	 */
	public void setDesCor(String desCor) {
		this.desCor = desCor;
	}
	/**
	 * @return Returns the desLar.
	 */
	public String getDesLar() {
		return desLar;
	}
	/**
	 * @param desLar The desLar to set.
	 */
	public void setDesLar(String desLar) {
		this.desLar = desLar;
	}
	/**
	 * @return Returns the esPrep.
	 */
	public String getEsPrep() {
		return esPrep;
	}
	/**
	 * @param esPrep The esPrep to set.
	 */
	public void setEsPrep(String esPrep) {
		this.esPrep = esPrep;
	}
	/**
	 * @return Returns the idMarca.
	 */
	public String getIdMarca() {
		return idMarca;
	}
	/**
	 * @param idMarca The idMarca to set.
	 */
	public void setIdMarca(String idMarca) {
		this.idMarca = idMarca;
	}
	/**
	 * @return Returns the intMax.
	 */
	public double getIntMax() {
		return intMax;
	}
	/**
	 * @param intMax The intMax to set.
	 */
	public void setIntMax(double intMax) {
		this.intMax = intMax;
	}
	/**
	 * @return Returns the intVal.
	 */
	public double getIntVal() {
		return intVal;
	}
	/**
	 * @param intVal The intVal to set.
	 */
	public void setIntVal(double intVal) {
		this.intVal = intVal;
	}
	/**
	 * @return Returns the tipo.
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	/**
	 * @return Returns the uniMed.
	 */
	public String getUniMed() {
		return uniMed;
	}
	/**
	 * @param uniMed The uniMed to set.
	 */
	public void setUniMed(String uniMed) {
		this.uniMed = uniMed;
	}
	/**
	 * @return Returns the mensDesp.
	 */
	public String getMensDesp() {
		return mensDesp;
	}
	/**
	 * @param mensDesp The mensDesp to set.
	 */
	public void setMensDesp(String mensDesp) {
		this.mensDesp = mensDesp;
	}
	/**
	 * @return Returns the mensPubl.
	 */
	public String getMensPubl() {
		return mensPubl;
	}
	/**
	 * @param mensPubl The mensPubl to set.
	 */
	public void setMensPubl(String mensPubl) {
		this.mensPubl = mensPubl;
	}
	/**
	 * @return Returns the motivo.
	 */
	public String getMotivo() {
		return motivo;
	}
	/**
	 * @param motivo The motivo to set.
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
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
     
}
