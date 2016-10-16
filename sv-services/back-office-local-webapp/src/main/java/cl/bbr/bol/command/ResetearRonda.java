package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * LiberaOP Comando Process
 * comando que libera una op
 * @author BBRI
 */

public class ResetearRonda extends Command {

 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
     // 1. seteo de Variables del método
     String paramUrl = "";
     long paramId_ronda=0L;
     long paramId_jornada=0L;

     //Recupera pagina desde web.xml
     String UrlError = getServletConfig().getInitParameter("UrlError");
     logger.debug("UrlError: " + UrlError);
		
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");
     String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");     
	 logger.debug("mensaje_fracaso: " + mensaje_fracaso);
	 String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito");     
	 logger.debug("mensaje_exito: " + mensaje_exito);
	 

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_ronda") == null ){throw new ParametroObligatorioException("id_ronda es nulo");}
     if ( req.getParameter("id_jornada") == null ){throw new ParametroObligatorioException("id_jornada es nulo");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramId_ronda = Long.parseLong(req.getParameter("id_ronda")); //long:obligatorio:si
     paramId_jornada = Long.parseLong(req.getParameter("id_jornada")); //long:obligatorio:si

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_pedido: " + paramId_ronda);

     /*
      * 3. Procesamiento Principal
      */
     BizDelegate bizDelegate = new BizDelegate();
     
     ForwardParameters fp = new ForwardParameters();
	 fp.add( req.getParameterMap() );
     
     try{
    	 boolean exito = bizDelegate.setReseteaRonda(paramId_ronda);
    	 logger.debug("exito?"+exito);
    	 if(exito){
    		 
    		 //bizDelegate.addLogJornadaPick(paramId_jornada, usr.getLogin(), "Se ha resetado la ronda "+paramId_ronda);
    		 bizDelegate.addLogRonda(paramId_ronda,usr.getLogin(), "Se ha resetado la ronda");
    		 
    		 //fp.add("mensaje",mensaje_exito);
    		 paramUrl += "&mensaje="+mensaje_exito;
    	 }else{
    		 //fp.add("mensaje",mensaje_fracaso);
    		 paramUrl += "&mensaje="+mensaje_fracaso;
    	 }
    	 
     }catch(BolException ex){
			if(ex.getMessage().equals(Constantes._EX_RON_ID_INVALIDO)){
				logger.debug("El id de ronda es Inválido");
				fp.add( "rc" , Constantes._EX_RON_ID_INVALIDO);
				paramUrl = UrlError + fp.forward();
			}
		}
     
     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
