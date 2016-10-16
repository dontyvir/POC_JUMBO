package cl.cencosud.jumbo.input.dto.Payment;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class DeleteInputPaymentDTO extends InputDTO implements Serializable {
	
	private static final long serialVersionUID = -6876141370129519718L;

	public DeleteInputPaymentDTO(HttpServletRequest request){
		
	}
	public void isValid() throws ExceptionInParam {
		// TODO Apéndice de método generado automáticamente
		
	}

}
