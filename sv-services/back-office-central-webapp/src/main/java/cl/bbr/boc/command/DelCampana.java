package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelCampanaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * DelCampana Comando Process
 * permite eliminar una campaña
 * @author BBRI
 */

public class DelCampana extends Command {
	private final static long serialVersionUID = 1;
 
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     
     // 1. seteo de Variables del método
     String paramUrl = "";
     long paramId_campana=0L;
     // 2. Procesa parámetros del request

     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_campana") == null ){throw new ParametroObligatorioException("id_campana es nulo");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramId_campana = Long.parseLong(req.getParameter("id_campana")); //long:obligatorio:si

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_campana: " + paramId_campana);
     
     ForwardParameters fp = new ForwardParameters();
     fp.add( req.getParameterMap() );
     
     /*
      * 3. Procesamiento Principal
      */
		BizDelegate biz = new BizDelegate();
		
		try{
			ProcDelCampanaDTO prm = new ProcDelCampanaDTO();
			prm.setId_campana(paramId_campana);
			
			boolean elim = biz.setDelCampana(prm);
			logger.debug("elim?"+elim);
		}catch(BocException e){
			logger.debug("Controlando excepción: " + e.getMessage());
			String UrlError = getServletConfig().getInitParameter("UrlError");
			if ( e.getMessage().equals(Constantes._EX_CAM_ID_NO_EXISTE) ){
				logger.debug("La campaña no existe");
				fp.add( "rc" , Constantes._EX_CAM_ID_NO_EXISTE );
				fp.add("id_campana", paramId_campana+"");
				paramUrl = UrlError + fp.forward(); 
			}  else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
		}

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
