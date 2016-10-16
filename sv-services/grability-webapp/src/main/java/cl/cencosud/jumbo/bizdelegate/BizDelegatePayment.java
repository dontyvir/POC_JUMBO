package cl.cencosud.jumbo.bizdelegate;

import org.apache.commons.lang.StringUtils;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantPayment;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.util.Util;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.parametros.service.ParametrosService;
import cl.bbr.jumbocl.pedidos.dto.PagoGrabilityDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.service.PedidosService;

public class BizDelegatePayment {
	
	private PedidosService pedidosService = null;
	private static ParametrosService  parametroService  = null;
		
	public BizDelegatePayment() {
		
		if (pedidosService== null)
			pedidosService = new PedidosService();
		
		if(parametroService == null) 
		    parametroService = new ParametrosService();
	}
	
	
	public PedidoDTO getPedido(long id_pedido) throws GrabilityException{
		
		try {
			return pedidosService.getPedido(id_pedido);
		} catch (Exception e) {				
			if(Util.isSqlException(e)){					
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantPayment.SC_ERROR_RECUPERAR_OP, ConstantPayment.MSG_ERROR_RECUPERAR_OP,e);
			}					
		}		
	
	}

	public ParametroDTO getParametroByName(String paramName) throws GrabilityException { 
		
	  try {
	        return parametroService.getParametroByName(paramName);
	  } catch (Exception e) {
	  		if(Util.isSqlException(e)){				
				throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
			}else{
				throw new GrabilityException(ConstantPayment.SC_ERROR_RECUPERAR_PARAMETRO_PAGO, ConstantPayment.MSG_ERROR_RECUPERAR_PARAMETRO_PAGO,e);
			}					
	  }
	  
	}


	public void insertRegistroPago(PagoGrabilityDTO oPagoGrabilityDTO) throws GrabilityException {
		  try {
			  pedidosService.insertRegistroPago(oPagoGrabilityDTO);
		  } catch (Exception e) {
			  if(Util.isSqlException(e)){				
					throw new GrabilityException(Constant.SC_ERROR_INESPERADO_SQL, Constant.MSG_ERROR_INESPERADO_SQL, e);
				}else{
					throw new GrabilityException(ConstantPayment.SC_ERROR_RECUPERAR_PARAMETRO_PAGO, ConstantPayment.MSG_ERROR_RECUPERAR_PARAMETRO_PAGO,e);
				}					
		  }
		
	}
}
