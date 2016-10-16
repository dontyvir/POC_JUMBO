package cl.cencosud.jumbo.input.dto.List;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;


import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class DeleteInputListDTO extends InputDTO implements Serializable{
	

	private static final long serialVersionUID = 9093391802939458156L;

	public DeleteInputListDTO(HttpServletRequest request){
		
	}

	public void isValid() throws ExceptionInParam {
		// TODO Apéndice de método generado automáticamente
		
	}

}
