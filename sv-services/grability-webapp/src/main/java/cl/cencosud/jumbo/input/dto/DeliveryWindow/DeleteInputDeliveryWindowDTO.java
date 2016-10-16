package cl.cencosud.jumbo.input.dto.DeliveryWindow;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class DeleteInputDeliveryWindowDTO extends InputDTO implements Serializable{


	private static final long serialVersionUID = -2538312084295119367L;

	public DeleteInputDeliveryWindowDTO(HttpServletRequest request){
		
	}
	
	public void isValid() throws ExceptionInParam {
		// TODO Apéndice de método generado automáticamente
		
	}

}
