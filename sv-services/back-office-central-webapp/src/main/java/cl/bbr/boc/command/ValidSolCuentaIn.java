package cl.bbr.boc.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Servlet implementation class ValidSolCuentaIn
 */
public class ValidSolCuentaIn  extends Command {
	
	private static final long serialVersionUID = 1L;
    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

    	logger.debug("User: " + usr.getLogin());
	     
	     // 1. seteo de Variables del método
	     String paramUrl 		= "";
	     String paramId_cliente	= "";
	     long id_cliente		= 0L;
	     String paramReq	 	= "";
	     
	     
	     
	     // 2. Procesa parámetros del request

	     logger.debug("Procesando parámetros...");

	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if ( req.getParameter("id_cliente") == null ){throw new ParametroObligatorioException("id_cliente es nulo");}
	     if ( req.getParameter("req") == null ){throw new ParametroObligatorioException("req es nulo");}

	     // 2.2 obtiene parametros desde el request
	     paramUrl = req.getParameter("url");
	     paramId_cliente = req.getParameter("id_cliente"); //long:obligatorio:si
	     paramReq = req.getParameter("req");
	     
	     id_cliente = Long.parseLong( paramId_cliente );
	     
	     logger.debug("PARAMURL: " + paramUrl);
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("id_cliente: " + paramId_cliente);
	     logger.debug("Req: " + paramReq);

	     /*
	      * 3. Procesamiento Principal
	      */
	     BizDelegate bizDelegate = new BizDelegate();
	     boolean resultado=false;
	     
	     if ( paramReq.equals("Blanqueo") ){
	    	 resultado= bizDelegate.doBlanqueoDireccion(id_cliente);
	    	paramUrl = paramUrl + "&mje=4";
	     }
	     if (resultado) {
	    	 logger.debug("Se realizo blanqueo de direccion por User: " + usr.getLogin());
		}
	     
	     logger.debug(" URL " + paramUrl);
	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);

	 }//execute


}
