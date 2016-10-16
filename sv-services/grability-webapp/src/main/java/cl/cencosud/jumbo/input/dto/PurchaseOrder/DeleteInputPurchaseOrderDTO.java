package cl.cencosud.jumbo.input.dto.PurchaseOrder;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class DeleteInputPurchaseOrderDTO extends InputDTO implements Serializable{

	private static final long serialVersionUID = -7104039462959146414L;

	public DeleteInputPurchaseOrderDTO(HttpServletRequest request){
		
	}
	public void isValid() throws ExceptionInParam {
		// TODO Apéndice de método generado automáticamente
		
	}
}
