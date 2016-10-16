package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModElementoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que permite modificar los elementos
 * @author bbr
 *
 */
public class ModElemento extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	 
     // 1. seteo de Variables del método
     String paramUrl = "";
     String paramNombre="";
     String paramDescripcion="";
     String paramEstado="";
     String paramUrl_dest="";
     long paramId_elemento=0L;
     long paramTipo=0L;
     
     // 2. Procesa parámetros del request

     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_elemento") == null ){throw new ParametroObligatorioException("id_elemento es nulo");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramNombre = req.getParameter("nombre"); //string:obligatorio:no
     paramDescripcion = req.getParameter("descripcion"); //string:obligatorio:no
     paramEstado = req.getParameter("sel_est"); //string:obligatorio:no
   	 paramUrl_dest = req.getParameter("url_dest"); //string:obligatorio:no
     paramTipo = Long.parseLong(req.getParameter("sel_tip"));
   
     paramId_elemento = Long.parseLong(req.getParameter("id_elemento")); //long:obligatorio:si
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("nombre: " + paramNombre);
     logger.debug("descripcion: " + paramDescripcion);
     logger.debug("estado: " + paramEstado);
     logger.debug("url_dest: " + paramUrl_dest);
     logger.debug("id_elemento: " + paramId_elemento);
     logger.debug("paramTipo: " + paramTipo);
     
     ForwardParameters fp = new ForwardParameters();
     fp.add( req.getParameterMap() );
     
     /*
      * 3. Procesamiento Principal
      */
     	BizDelegate bizDelegate = new BizDelegate();
     	
     	try{
     		ProcModElementoDTO modElem = new ProcModElementoDTO();
     		modElem.setId_elemento(paramId_elemento);
     		modElem.setNombre(paramNombre);
     		modElem.setDescripcion(paramDescripcion);
     		modElem.setEstado(paramEstado);
     		modElem.setTipo(paramTipo);
     		modElem.setUrl_destino(paramUrl_dest);
    		
    		bizDelegate.setModElemento(modElem);
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
