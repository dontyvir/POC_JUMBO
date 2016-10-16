package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.ProcModPerfilDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que permite modificar los perfiles
 * @author bbr
 *
 */
public class ModPerfil extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	 
     // 1. seteo de Variables del método
     String paramUrl = "";
     long paramPer_cod = 0L;
     String paramNombre = "";
     String paramDescrip = "";
     //String[] paramCmds;
     String paramCmds = "";
     int  paramCant_cmd = 0;
     
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("per_cod") == null ){throw new ParametroObligatorioException("per_cod es nulo");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramPer_cod = Long.parseLong(req.getParameter("per_cod")); //string:obligatorio:si
     paramNombre = req.getParameter("per_nom"); //string:obligatorio:no
     paramDescrip = req.getParameter("per_des"); //string:obligatorio:no
     //paramCmds = req.getParameterValues("com_id"); //long:obligatorio:si
     paramCmds = req.getParameter("com_id"); //long:obligatorio:si
     
     logger.debug("*******************************: " + paramCmds);
     paramCant_cmd = Integer.parseInt(req.getParameter("cant_cmd"));

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("per_cod: " + paramPer_cod);
     logger.debug("per_nom: " + paramNombre);
     logger.debug("per_des: " + paramDescrip);
     logger.debug("com_id: " + paramCmds);
     
     /*if(paramCmds!=null){
    	 for(int i=0;i<paramCmds.length;i++)
        	 logger.debug("com_id: " + paramCmds[i]);
     }*/

     /*
      * 3. Procesamiento Principal
      */
     	BizDelegate biz = new BizDelegate();
     	
     	ProcModPerfilDTO dto = new ProcModPerfilDTO();
     	
     	dto.setId_perfil(paramPer_cod);
     	dto.setDescripcion(paramDescrip);
     	dto.setNombre(paramNombre);
     	
     	//obtener la cantidad de comandos
     	if (paramCmds != null){
     		long[] id_cmds = new long[paramCant_cmd];
     		int ind = 0;
	     	for(int i=0; i<paramCant_cmd;i++){
	     		ind = paramCmds.indexOf(",");
	     		if(ind>0){
	     			id_cmds[i] = Long.parseLong(paramCmds.substring(0,ind));
		     		paramCmds = paramCmds.substring(ind+1);
	     		}else{
	     			id_cmds[i] = Long.parseLong(paramCmds);
	     		}
	     	}
	     	dto.setId_comandos(id_cmds);
     	}
		
		
		// Ejecuta Operación
     	biz.doModComandosAlPerfil( dto );
     	
     	
	
     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
