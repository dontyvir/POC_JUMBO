package cl.bbr.jumbocl.pedidos.promos.interfaces.dto;

import java.util.List;

public class RespR1DTO {
	public static final int GLOSA1_LEN         = 20;
	public static final int GLOSA2_LEN         = 20;
	
	private String cod_ret;
	//se leen siempre los 13 tcps pero se responde con los que sean mayor a 0
	private int cant_tcps;
	private List tcps;
	private String glosa1;
	private String glosa2;

	public RespR1DTO() {		
	}

	/**
	 * @return Returns the cant_tcps.
	 */
	public int getCant_tcps() {
		return cant_tcps;
	}

	/**
	 * @param cant_tcps The cant_tcps to set.
	 */
	public void setCant_tcps(int cant_tcps) {
		this.cant_tcps = cant_tcps;
	}

	/**
	 * @return Returns the cod_ret.
	 */
	public String getCod_ret() {
		return cod_ret;
	}

	/**
	 * @param cod_ret The cod_ret to set.
	 */
	public void setCod_ret(String cod_ret) {
		this.cod_ret = cod_ret;
	}

	/**
	 * @return Returns the glosa1.
	 */
	public String getGlosa1() {
		return glosa1;
	}

	/**
	 * @param glosa1 The glosa1 to set.
	 */
	public void setGlosa1(String glosa1) {
		this.glosa1 = glosa1;
	}

	/**
	 * @return Returns the glosa2.
	 */
	public String getGlosa2() {
		return glosa2;
	}

	/**
	 * @param glosa2 The glosa2 to set.
	 */
	public void setGlosa2(String glosa2) {
		this.glosa2 = glosa2;
	}

	/**
	 * @return Returns the tcps.
	 */
	public List getTcps() {
		return tcps;
	}

	/**
	 * @param tcps The tcps to set.
	 */
	public void setTcps(List tcps) {
		this.tcps = tcps;
	}

}
