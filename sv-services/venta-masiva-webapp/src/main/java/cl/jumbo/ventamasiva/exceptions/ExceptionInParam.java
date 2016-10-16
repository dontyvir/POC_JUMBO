package cl.jumbo.ventamasiva.exceptions;

import cl.bbr.jumbocl.shared.log.Logging;

public class ExceptionInParam extends Exception{

	private static final long serialVersionUID = 8532994918640737420L;
	private String codError;
	private String MsjError;
	Logging logger = new Logging(this);
	
	public ExceptionInParam(String codError, String messageError) {
		super(codError+":::"+messageError);
		setCodError(codError);
		setMsjError(messageError);
		logger.error(codError+":::"+messageError);
	}
	
	public ExceptionInParam(String codError, String messageError, Exception e) {
		super(e);
		setCodError(codError);
		setMsjError(messageError);
		logger.error(e);
	}
	
	/**
	 * Constructs a new instance of Exception
	 *
	 * @param e exception.
	 */
	public ExceptionInParam(Exception e) {
		super(e);
		logger.error(e);
		//super(e.getMessage()+":::"+e.getMessage());
	} 	

    /**
     * Constructs a new instance of BolException
     *
     * @param throwable the parent Throwable
     */
    public ExceptionInParam(Throwable throwable) {
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
