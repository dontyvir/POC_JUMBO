package cl.bbr.jumbocl.common.exceptions;

public class ParametroObligatorioException extends Exception {

	public ParametroObligatorioException() {
	}

	public ParametroObligatorioException(String param) {
		super(param);
	}

}
