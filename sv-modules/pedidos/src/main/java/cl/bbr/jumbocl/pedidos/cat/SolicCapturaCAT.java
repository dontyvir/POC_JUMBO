/*
 * Creado el 05-11-2010
 *
 */
package cl.bbr.jumbocl.pedidos.cat;

/**
 * @author rsuarezr
 *
 */
public class SolicCapturaCAT {
	
	private String version 	= "01";
	private String numempre = "1234";
	private String fectrans;
	private String codtrans;
	private String idtrn;
	private String tipoauto	= " ";
	private String codautor;
	private String monconta;
	private String horenvio;
    private String dpc = "0000";

	/**
	 * @return Devuelve codautor.
	 */
	public String getCodautor() {
		return codautor;
	}
	/**
	 * @param codautor El codautor a establecer.
	 */
	public void setCodautor(String codautor) {
		this.codautor = codautor;
	}
	/**
	 * @return Devuelve codtrans.
	 */
	public String getCodtrans() {
		return codtrans;
	}
	/**
	 * @param codtrans El codtrans a establecer.
	 */
	public void setCodtrans(String codtrans) {
		this.codtrans = codtrans;
	}
	/**
	 * @return Devuelve fectrans.
	 */
	public String getFectrans() {
		return fectrans;
	}
	/**
	 * @param fectrans El fectrans a establecer.
	 */
	public void setFectrans(String fectrans) {
		this.fectrans = fectrans;
	}
	/**
	 * @return Devuelve horenvio.
	 */
	public String getHorenvio() {
		return horenvio;
	}
	/**
	 * @param horenvio El horenvio a establecer.
	 */
	public void setHorenvio(String horenvio) {
		this.horenvio = horenvio;
	}
	/**
	 * @return Devuelve idtrn.
	 */
	public String getIdtrn() {
		return idtrn;
	}
	/**
	 * @param idtrn El idtrn a establecer.
	 */
	public void setIdtrn(String idtrn) {
		this.idtrn = idtrn;
	}
	/**
	 * @return Devuelve monconta.
	 */
	public String getMonconta() {
		return monconta;
	}
	/**
	 * @param monconta El monconta a establecer.
	 */
	public void setMonconta(String monconta) {
		this.monconta = monconta;
	}
	/**
	 * @return Devuelve numempre.
	 */
	public String getNumempre() {
		return numempre;
	}
	/**
	 * @param numempre El numempre a establecer.
	 */
	public void setNumempre(String numempre) {
		this.numempre = numempre;
	}
	/**
	 * @return Devuelve tipoauto.
	 */
	public String getTipoauto() {
		return tipoauto;
	}
	/**
	 * @param tipoauto El tipoauto a establecer.
	 */
	public void setTipoauto(String tipoauto) {
		this.tipoauto = tipoauto;
	}
	/**
	 * @return Devuelve version.
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version El version a establecer.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
    /**
     * @return Devuelve dpc.
     */
    public String getDpc() {
        return dpc;
    }
    /**
     * @param dpc El dpc a establecer.
     */
    public void setDpc(String dpc) {
        this.dpc = dpc;
    }
}
