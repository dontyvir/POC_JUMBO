package cl.cencosud.jumbo.input.dto.PurchaseOrder;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class GetInputPurchaseOrderDTO extends InputDTO implements Serializable{

	private static final long serialVersionUID = -1934909530629210992L;

	public GetInputPurchaseOrderDTO(HttpServletRequest request){
		
	}
	public void isValid() throws ExceptionInParam {
		// TODO Apéndice de método generado automáticamente
		
	}
}
