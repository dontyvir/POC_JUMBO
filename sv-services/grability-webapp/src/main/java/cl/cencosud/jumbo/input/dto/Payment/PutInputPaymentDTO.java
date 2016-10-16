package cl.cencosud.jumbo.input.dto.Payment;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class PutInputPaymentDTO extends InputDTO implements Serializable{

	private static final long serialVersionUID = 5627034788403351896L;

	public  PutInputPaymentDTO(HttpServletRequest request){
		
	}
	public void isValid() throws ExceptionInParam {
		// TODO Apéndice de método generado automáticamente
	}

}
