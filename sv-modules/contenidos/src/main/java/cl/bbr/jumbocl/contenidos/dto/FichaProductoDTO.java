package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;

public class FichaProductoDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7965037897424654187L;
	private long pftProId;			
	private long pftPfiItem;
	private int pftPfiSecuencia;
	private String pftDescripcionItem;
	private int pftEstadoItem;

	public FichaProductoDTO(){
		
	}

	public FichaProductoDTO(long pftProId, long pftPfiItem,
			int pftPfiSecuencia, String pftDescripcionItem, int pftEstadoItem) {
		super();
		this.pftProId = pftProId;
		this.pftPfiItem = pftPfiItem;
		this.pftPfiSecuencia = pftPfiSecuencia;
		this.pftDescripcionItem = pftDescripcionItem;
		this.pftEstadoItem = pftEstadoItem;
	}

	public long getPftProId() {
		return pftProId;
	}

	public void setPftProId(long pftProId) {
		this.pftProId = pftProId;
	}

	public long getPftPfiItem() {
		return pftPfiItem;
	}

	public void setPftPfiItem(long pftPfiItem) {
		this.pftPfiItem = pftPfiItem;
	}

	public int getPftPfiSecuencia() {
		return pftPfiSecuencia;
	}

	public void setPftPfiSecuencia(int pftPfiSecuencia) {
		this.pftPfiSecuencia = pftPfiSecuencia;
	}

	public String getPftDescripcionItem() {
		return pftDescripcionItem;
	}

	public void setPftDescripcionItem(String pftDescripcionItem) {
		this.pftDescripcionItem = pftDescripcionItem;
	}

	public int getPftEstadoItem() {
		return pftEstadoItem;
	}

	public void setPftEstadoItem(int pftEstadoItem) {
		this.pftEstadoItem = pftEstadoItem;
	}
}