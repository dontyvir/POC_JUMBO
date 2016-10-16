package cl.bbr.jumbocl.common.exceptions;

public class UserNotFoundException extends Exception {
	
	public UserNotFoundException(){
		super("Exception, Usuario no ha sido encontrado");
	}

	public UserNotFoundException(Throwable throwable) {
		super(throwable);
	}

	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public UserNotFoundException(Exception e) {
		super(e.getMessage());
	}		
	
}
