package cl.bbr.jumbocl.common.exceptions;

public class DuplicateKeyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5165766865480474424L;
	
	public DuplicateKeyException(Exception e) {
		super(e);
	}

}
