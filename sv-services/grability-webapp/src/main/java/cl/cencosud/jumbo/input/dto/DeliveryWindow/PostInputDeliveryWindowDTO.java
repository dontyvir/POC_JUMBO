package cl.cencosud.jumbo.input.dto.DeliveryWindow;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class PostInputDeliveryWindowDTO extends InputDTO implements Serializable{

	private static final long serialVersionUID = 8687662095107560036L;

	public PostInputDeliveryWindowDTO(HttpServletRequest request){
		
	}
	
	
	public void isValid() throws ExceptionInParam {
		// TODO Apéndice de método generado automáticamente
		
	}

}
