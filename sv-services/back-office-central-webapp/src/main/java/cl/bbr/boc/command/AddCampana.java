package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCampanaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * AddCampana Comando Process
 * Agrega una Campaña
 * @author BBRI
 */

public class AddCampana extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl = "";
     String paramNombre="";
     String paramDescripcion="";
     String paramEstado="";
     //String fec_crea = Formatos.getFecHoraActual();
     
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("nombre") == null ){throw new ParametroObligatorioException("nombre es null");}
     if ( req.getParameter("descripcion") == null ){throw new ParametroObligatorioException("descripcion es null");}
     if ( req.getParameter("sel_est") == null ){throw new ParametroObligatorioException("sel_est es null");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramNombre = req.getParameter("nombre"); //string:obligatorio:no
     paramDescripcion = req.getParameter("descripcion"); //string:obligatorio:no
     paramEstado = req.getParameter("sel_est"); //string:obligatorio:no
     
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("nombre: " + paramNombre);
     logger.debug("descripcion: " + paramDescripcion);
     logger.debug("estado: " + paramEstado);
     
     //ForwardParameters fp = new ForwardParameters();
     /*
      * 3. Procesamiento Principal
      */
  		BizDelegate biz = new BizDelegate();
  		ProcAddCampanaDTO prm = new ProcAddCampanaDTO();
  		prm.setNombre(paramNombre);
  		prm.setDescripcion(paramDescripcion);
  		prm.setEstado(paramEstado);
  		//La hora se pone automaticamente al ser creado
  		//prm.setFec_crea(fec_crea);
     
  	    
  		boolean result = false;
  		try{
  			result = biz.setAddCampana(prm);
  			logger.debug("result?"+result);
  		} catch(BocException e){
  	  		ForwardParameters fp = new ForwardParameters();
  	  	    fp.add( req.getParameterMap() );
  	  	    
     		logger.debug("Controlando excepción: " + e.getMessage());
			String UrlError = getServletConfig().getInitParameter("UrlError");
			if ( e.getMessage().equals(Constantes._EX_CAM_ID_NO_EXISTE) ){
				logger.debug("La campana no se puedo crear...");
				fp.add( "rc" , Constantes._EX_CAM_ID_NO_EXISTE );
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
     	}
  		
  		logger.debug("result:"+result);

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
