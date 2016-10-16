package cl.bbr.jumbocl.common.exceptions;

public class ServiceException extends Exception {
	
	public ServiceException() {
		super("Exception, ServiceException Detectada!!!...");
	}

	public ServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ServiceException(String arg0) {
		super(arg0);
	}

	public ServiceException(Throwable arg0) {
		super(arg0);
	}
	
}
