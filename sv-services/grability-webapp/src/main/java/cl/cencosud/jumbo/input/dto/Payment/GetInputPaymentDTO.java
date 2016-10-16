package cl.cencosud.jumbo.input.dto.Payment;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class GetInputPaymentDTO extends InputDTO implements Serializable {

	private static final long serialVersionUID = 8683511256063606994L;

	public GetInputPaymentDTO(HttpServletRequest request){
		
	}	
	public void isValid() throws ExceptionInParam {
		// TODO Apéndice de método generado automáticamente
		
	}

}
