package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.collaboration.ProcModFacturaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que permite modificar un medio de pago
 * @author bbr
 *
 */
public class ModFactura extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl = "";
     long paramIdPedido=0L;
     long paramIdFactura=0L;
     String paramRut = "";
     String paramDv = "";
     String paramRazon = "";
     String paramDirecc = "";
     String paramTelef = "";
     String paramGiro = "";     
     String paramComuna = "";     
     String paramCiudad = "";     
     long   paramDpago = 0L;     
     String paramTDpago = "";     
     
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");
     String msjeOK = getServletConfig().getInitParameter("msjeOK");		
	 String msjeMAL = getServletConfig().getInitParameter("msjeMAL");
     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es null");}
     if(paramTDpago.equals("F")){
    	 if ( req.getParameter("id_factura") == null ){throw new ParametroObligatorioException("id_factura es null");}       	
     	paramIdFactura = Long.parseLong(req.getParameter("id_factura")); //long:obligatorio:si
      }
     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramIdPedido = Long.parseLong(req.getParameter("id_pedido")); //long:obligatorio:si     
      //String:obligatorio:si
     paramTDpago = req.getParameter("dpago");
     
     if ( Constantes.TIPO_DOC_FACTURA.equalsIgnoreCase( paramTDpago ) ) {
         //No se permite realizar el cambio a factura
         throw new Exception();
     }
     
     /*if(paramTDpago.equals("B")){
      	if ( req.getParameter("num_doc") == null ){throw new ParametroObligatorioException("num_doc es null");
      	}
      	paramDpago = Long.parseLong(req.getParameter("num_doc"));
      }	*/
     paramRut = req.getParameter("rut"); 
     paramDv = req.getParameter("dv");
     paramRazon = req.getParameter("razon");
     paramDirecc = req.getParameter("direccion");
     paramTelef = req.getParameter("telefono");
     paramGiro = req.getParameter("giro");
     paramComuna = req.getParameter("comuna");
     paramCiudad = req.getParameter("ciudad");

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_pedido: " + paramIdPedido);
     logger.debug("id_factura: " + paramIdFactura);
     logger.debug("paramTDpago: " + paramTDpago);
     logger.debug("rut: " + paramRut);
     logger.debug("dv: " + paramDv);
     logger.debug("razon: " + paramRazon);
     logger.debug("direccion: " + paramDirecc);
     logger.debug("fono: " + paramTelef);
     logger.debug("giro: " + paramGiro);
     logger.debug("comuna: " + paramComuna);
     logger.debug("ciudad: " + paramCiudad);
     
     ForwardParameters fp = new ForwardParameters();
     //String mensaje = "";
     /*
      * 3. Procesamiento Principal
      */
	    logger.debug("paramDpago: "+paramDpago);	
	    BizDelegate biz = new BizDelegate();
	  	ProcModFacturaDTO prm = new ProcModFacturaDTO();
  		
  		if(paramTDpago.equals("B")){
  			//prm.setId_factura(paramDpago);
  			prm.setId_pedido(paramIdPedido);
  			prm.setTipo_doc("B");
  			logger.debug("paramDpago: "+paramDpago);
  			logger.debug("paramIdPedido: "+paramIdPedido);
  			
  		}
  		else{
  			prm.setId_pedido(paramIdPedido);
  			prm.setId_factura(paramIdFactura);
  			prm.setRut(Long.parseLong(paramRut));
  			prm.setDv(paramDv);
  			prm.setRazon(paramRazon);
  			prm.setDireccion(paramDirecc);
  			prm.setTelefono(paramTelef);
  			prm.setGiro(paramGiro);
  			prm.setCiudad(paramCiudad);
  			prm.setComuna(paramComuna);
  			prm.setTipo_doc("F");
  			
  			
  		}
  		/*ProcModFacturaDTO prm = new ProcModFacturaDTO(paramIdPedido, paramIdFactura, paramRut, paramDv, paramRazon, 
  				paramDirecc, paramTelef, paramGiro);*/ 
     
  		boolean result = false;
  		try{
  			result = biz.setModFactura(prm);
  			paramUrl +="&mensaje="+msjeOK;
  			
  		} catch(BocException e){
  			String UrlError = getServletConfig().getInitParameter("UrlError");
  			logger.debug("Controlando excepción: " + e.getMessage());
  			if (  e.getMessage().equals(Constantes._EX_OPE_ID_NO_EXISTE) ){
				logger.debug("El código del pedido no existe");
				fp.add( "rc" , Constantes._EX_OPE_ID_NO_EXISTE );
				paramUrl = UrlError + fp.forward()+"&mensaje="+msjeMAL;
			} if (  e.getMessage().equals(Constantes._EX_OPE_ID_DIR_NO_EXISTE) ){
				logger.debug("El código de la dirección no existe");
				fp.add( "rc" , Constantes._EX_OPE_ID_DIR_NO_EXISTE );
				paramUrl = UrlError + fp.forward()+"&mensaje="+msjeMAL;
			} 
  		}
  		
  		logger.debug("result:"+result);

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
