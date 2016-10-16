package cl.bbr.jumbocl.common.model;


/**
 * Clase que captura desde la base de datos los datos de la ficha de un producto
 * @author bbr
 *
 */
public class FichaProductoEntity {
	private long pftProId;	
	private long pftPfiItem;
	private int pftPfiSecuencia;
	private String pftDescripcionItem;
	private int pftEstadoItem;
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