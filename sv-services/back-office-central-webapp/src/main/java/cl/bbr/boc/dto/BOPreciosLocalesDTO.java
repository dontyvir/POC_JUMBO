/*
 * Created on 20-may-2010
 */
package cl.bbr.boc.dto;

/**
 * @author hs
 */
public class BOPreciosLocalesDTO {
	
	private int 	preProID;
	private int 	preLocID;
	private float 	preCosto;
	private float	preValor;
	private int		preStock;
	private String	preEstado;
	private int		preTieneStock;
	private String preLocal;
	
	
	
	/**
	 * @return el preLocal
	 */
	public String getPreLocal() {
		return preLocal;
	}
	/**
	 * @param preLocal el preLocal a establecer
	 */
	public void setPreLocal(String preLocal) {
		this.preLocal = preLocal;
	}
	public int getPreProID() {
		return preProID;
	}
	public void setPreProID(int preProID) {
		this.preProID = preProID;
	}
	public int getPreLocID() {
		return preLocID;
	}
	public void setPreLocID(int preLocID) {
		this.preLocID = preLocID;
	}
	public float getPreCosto() {
		return preCosto;
	}
	public void setPreCosto(float preCosto) {
		this.preCosto = preCosto;
	}
	public float getPreValor() {
		return preValor;
	}
	public void setPreValor(float preValor) {
		this.preValor = preValor;
	}
	public int getPreStock() {
		return preStock;
	}
	public void setPreStock(int preStock) {
		this.preStock = preStock;
	}
	public String getPreEstado() {
		return preEstado;
	}
	public void setPreEstado(String preEstado) {
		this.preEstado = preEstado;
	}
	public int getPreTieneStock() {
		return preTieneStock;
	}
	public void setPreTieneStock(int preTieneStock) {
		this.preTieneStock = preTieneStock;
	}
	
	
	

}
