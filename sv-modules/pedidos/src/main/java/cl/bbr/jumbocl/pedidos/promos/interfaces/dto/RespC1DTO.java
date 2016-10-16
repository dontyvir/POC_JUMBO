package cl.bbr.jumbocl.pedidos.promos.interfaces.dto;


public class RespC1DTO {
	public static final int GLOSA1_LEN         = 20;
	public static final int GLOSA2_LEN         = 20;
		
	private String cod_ret;
	//solo 1	
	private TcpDTO tcp;
	private String glosa1;
	private String glosa2;

	public RespC1DTO() {		
	}

	/**
	 * @return Returns the tcp.
	 */
	public TcpDTO getTcp() {
		return tcp;
	}



	/**
	 * @param tcp The tcp to set.
	 */
	public void setTcp(TcpDTO tcp) {
		this.tcp = tcp;
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


}
