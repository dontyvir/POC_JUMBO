package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Modifica el costo del despacho 
 * @author jsepulveda
 */
public class ModCostoDespacho extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
	    // 1. seteo de Variables del método
	     String paramUrl 			= "";
	     String paramId_pedido		= "";
	     String paramPrecio			= "";
	     long id_pedido		= -1;
	     int  precio		= -1;
	     
	     // 2. Procesa parámetros del request

	     logger.debug("Procesando parámetros...");

	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if ( req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es nulo");}

	     // 2.2 obtiene parametros desde el request
	     paramUrl 			= req.getParameter("url");
	     paramId_pedido		= req.getParameter("id_pedido");
	     paramPrecio		= req.getParameter("precio");
	     
	     id_pedido 		= Long.parseLong(paramId_pedido);
	     precio	= Integer.parseInt(paramPrecio);
     
	     
	     // 2.3 log de parametros y valores
	     logger.debug("url : " 			+ paramUrl);
	     logger.debug("id_pedido : " 	+ paramId_pedido);
	     logger.debug("Precio : " 		+ paramPrecio);
     
	     ForwardParameters fp = new ForwardParameters();
		 fp.add( req.getParameterMap() );
	     
	     /*
	      * 3. Procesamiento Principal
	      */

	     BizDelegate biz = new BizDelegate();
	     
	     try{
	    	biz.doCambiaCostoDespacho(id_pedido, precio, usr.getLogin());
	    	paramUrl += "&msg=Costo de Despacho Actualizado Satisfactoriamente";
	     }catch(BocException e){ 
	    	 logger.debug("Controlando excepción: " + e.getMessage());
				String UrlError = getServletConfig().getInitParameter("UrlError");
				if (  e.getMessage().equals(Constantes._EX_JDESP_FALTAN_DATOS) ){
					logger.debug("El código de la jornada de despacho no existe");
					fp.add( "rc" , Constantes._EX_JDESP_FALTAN_DATOS );
					fp.add("msg","El código de la jornada de despacho no existe");
					paramUrl = UrlError + fp.forward();
				} else if( e.getMessage().equals(Constantes._EX_JDESP_SIN_CAPACIDAD) ){
					logger.debug("La jornada de despacho no tiene capacidad");
					fp.add( "rc" , Constantes._EX_JDESP_SIN_CAPACIDAD );
					fp.add("msg","La jornada de despacho no tiene capacidad");
					paramUrl = UrlError + fp.forward();
				}
	     }
	     logger.debug("PARAMURL: " + paramUrl);
	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);
	}
}
