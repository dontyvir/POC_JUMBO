package cl.cencosud.jumbo.input.dto.PurchaseOrder;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.input.dto.InputDTO;

public class PutInputPurchaseOrderDTO extends InputDTO implements Serializable{

	private static final long serialVersionUID = 4578746301401566461L;

	public PutInputPurchaseOrderDTO(HttpServletRequest request){
		
	}
	public void isValid() throws ExceptionInParam {
		// TODO Apéndice de método generado automáticamente
		
	}
}
