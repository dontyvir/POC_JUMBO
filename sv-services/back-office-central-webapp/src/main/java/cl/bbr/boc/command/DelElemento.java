package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelElementoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * DelElemento Comando Process
 * permite eliminar un elemento
 * @author BBRI
 */

public class DelElemento extends Command {
	private final static long serialVersionUID = 1;
 
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     
     // 1. seteo de Variables del método
     String paramUrl = "";
     long paramId_elemento=0L;
     // 2. Procesa parámetros del request

     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_elemento") == null ){throw new ParametroObligatorioException("id_elemento es nulo");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramId_elemento = Long.parseLong(req.getParameter("id_elemento")); //long:obligatorio:si

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_elemento: " + paramId_elemento);
     
     ForwardParameters fp = new ForwardParameters();
     fp.add( req.getParameterMap() );
     
     /*
      * 3. Procesamiento Principal
      */
		BizDelegate biz = new BizDelegate();
		
		try{
			ProcDelElementoDTO prm = new ProcDelElementoDTO();
			prm.setId_elemento(paramId_elemento);
			
			boolean elim = biz.setDelElemento(prm);
			logger.debug("elim?"+elim);
		}catch(BocException e){
			logger.debug("Controlando excepción: " + e.getMessage());
			String UrlError = getServletConfig().getInitParameter("UrlError");
			if ( e.getMessage().equals(Constantes._EX_ELM_ID_NO_EXISTE) ){
				logger.debug("El elemento no existe");
				fp.add( "rc" , Constantes._EX_ELM_ID_NO_EXISTE );
				fp.add("id_elemento", paramId_elemento+"");
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
		}
		
     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
