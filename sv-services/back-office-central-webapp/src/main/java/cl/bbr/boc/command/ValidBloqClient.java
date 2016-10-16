package cl.bbr.boc.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que valida el bloqueo de clientes
 * @author bbr
 *
 */
public class ValidBloqClient extends Command {

 

 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     
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
     
     // Constantes.ID_PERFIL_SUPERVISOR_BOC;
     int const_perfil = Constantes.ID_PERFIL_EJECUTIVO_BOC;
     List list_perfiles = usr.getPerfiles();
     int sup_boc = 0;
     String mensaje = "";
     for (int i=0; i<list_perfiles.size(); i++){
     
    	 PerfilesEntity perf1 = new PerfilesEntity();
       	 perf1 = (PerfilesEntity) list_perfiles.get(i);
    	 
    	 if (perf1.getIdPerfil().longValue() == const_perfil){
    		sup_boc = 1; // Indica que el tiene el perfil de supervisor BOC
    	 }
       	 logger.debug("perfil : " + perf1.getIdPerfil());
    	 
    	 
     }
     
     
     if ( paramReq.equals("Bloquear") ){
    	 bizDelegate.doBloqueaCliente( id_cliente );
    	 paramUrl = paramUrl + "&mje=3";
     }
     else if ( paramReq.equals("Desbloquear") ){
    	 //Valida que el desbloqueo sólo lo pueda realizar el Supervisor de BackOffice
    	 if (sup_boc == 1){
    		 bizDelegate.doDesbloqueaCliente( id_cliente );
    		 paramUrl = paramUrl + "&mje=2";
    	 }
    	 else{
    		 paramUrl = paramUrl + "&mje=1";
    		 logger.debug("Mensaje: " + mensaje );
    	 }
     }
     
     logger.debug(" URL " + paramUrl);
     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
