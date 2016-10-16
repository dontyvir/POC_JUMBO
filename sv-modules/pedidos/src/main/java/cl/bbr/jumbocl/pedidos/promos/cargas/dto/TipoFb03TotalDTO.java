package cl.bbr.jumbocl.pedidos.promos.cargas.dto;

import cl.bbr.jumbocl.common.utils.Formatos;

public class TipoFb03TotalDTO {
	public static final int PROMOCION_LEN = 6;
	public static final int EAN_LEN = 12;
	public static final int COD_RET_LEN = 1;
	public static final int FILLER_LEN = 1;
	
	public static final int TIPO_03_TOTAL_REG_LEN = 20;
	
	private String promocion;
	private String ean;
	private String cod_ret;
	private String filler; 

	public TipoFb03TotalDTO() {
	}

	public String toMsg(){
		String out = "";
		out += Formatos.formatField(promocion, 	PROMOCION_LEN, 	Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(ean, 		EAN_LEN, 		Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(cod_ret, 	COD_RET_LEN, 	Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(filler, 	FILLER_LEN, 	Formatos.ALIGN_RIGHT,"0");
	
		return out;
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
	 * @return Returns the ean.
	 */
	public String getEan() {
		return ean;
	}

	/**
	 * @param ean The ean to set.
	 */
	public void setEan(String ean) {
		this.ean = ean;
	}

	/**
	 * @return Returns the filler.
	 */
	public String getFiller() {
		return filler;
	}

	/**
	 * @param filler The filler to set.
	 */
	public void setFiller(String filler) {
		this.filler = filler;
	}

	/**
	 * @return Returns the promocion.
	 */
	public String getPromocion() {
		return promocion;
	}

	/**
	 * @param promocion The promocion to set.
	 */
	public void setPromocion(String promocion) {
		this.promocion = promocion;
	}
	
}
