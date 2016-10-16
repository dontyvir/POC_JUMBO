package cl.cencosud.jumbo.input.dto.Client;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class DeleteInputClientDTO extends InputDTO implements Serializable {

	private static final long serialVersionUID = 9125694161486239518L;

	public DeleteInputClientDTO(HttpServletRequest request){
		
	}	
	
	public void isValid() throws ExceptionInParam {
		// TODO Apéndice de método generado automáticamente
		
	}

}
