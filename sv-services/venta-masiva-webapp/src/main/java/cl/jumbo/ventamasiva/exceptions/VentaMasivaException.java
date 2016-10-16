package cl.jumbo.ventamasiva.exceptions;

import cl.bbr.jumbocl.shared.log.Logging;

public class VentaMasivaException  extends Exception {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = -7622418140470966651L;
	private String codError;
	private String MsjError;
	Logging logger = new Logging(this);
		
	public VentaMasivaException(String codError, String messageError) {
		super(codError+":::"+messageError);
		setCodError(codError);
		setMsjError(messageError);
		logger.error(codError+":::"+messageError);
	}
	
	public VentaMasivaException(String codError, String messageError, Exception e) {
		super(e);
		setCodError(codError);
		setMsjError(messageError);
		logger.error(e);
	}
		
	public VentaMasivaException(Exception e) {
		super(e);
		logger.error(e);
	} 
	
	public VentaMasivaException(Throwable throwable) {
		super(throwable);
		logger.error(throwable);
    }

	public String getCodError() {
		return codError;
	}

	public void setCodError(String codError) {
		this.codError = codError;
	}

	public String getMsjError() {
		return MsjError;
	}

	public void setMsjError(String msjError) {
		MsjError = msjError;
	}
}
