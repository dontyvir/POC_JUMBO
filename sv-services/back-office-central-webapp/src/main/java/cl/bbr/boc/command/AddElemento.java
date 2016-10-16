package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddElementoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * AddElemento Comando Process
 * Agrega un Elemento
 * 
 * @author BBRI
 */

public class AddElemento extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl = "";
     String paramNombre="";
     String paramDescripcion="";
     long paramTipo = 0L;
     String paramEstado="";
     String paramUrl_dest="";
     //String fec_crea = Formatos.getFecHoraActual();
     
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("nombre") == null ){throw new ParametroObligatorioException("nombre es null");}
     if ( req.getParameter("descripcion") == null ){throw new ParametroObligatorioException("descripcion es null");}
     if ( req.getParameter("sel_est") == null ){throw new ParametroObligatorioException("sel_est es null");}
     if ( req.getParameter("sel_tip") == null ){throw new ParametroObligatorioException("sel_tip es null");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramNombre = req.getParameter("nombre"); //string:obligatorio:no
     paramDescripcion = req.getParameter("descripcion"); //string:obligatorio:no
     paramTipo = Long.parseLong(req.getParameter("sel_tip"));
     paramUrl_dest = req.getParameter("url_dest");
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
  		ProcAddElementoDTO prm = new ProcAddElementoDTO();
  		prm.setNombre(paramNombre);
  		prm.setDescripcion(paramDescripcion);
  		prm.setEstado(paramEstado);
  		//toma la fecha de la base de datos por defecto al crear
  		//prm.setFec_crea(fec_crea);
  		prm.setTipo(paramTipo);
  		prm.setUrl_dest(paramUrl_dest);
  	     
	  	ForwardParameters fp = new ForwardParameters();
  	  	fp.add( req.getParameterMap() );

  	  	long id = -1;
  		try{
  			id = biz.setAddElemento(prm);
  			logger.debug("result?"+id);
  			/*if(id>-1){
  				fp.add("id_elemento",id+"");
  				paramUrl = paramUrl + fp.forward(); 
  			}*/
  		} catch(BocException e){
  	  	    
     		logger.debug("Controlando excepción: " + e.getMessage());
			String UrlError = getServletConfig().getInitParameter("UrlError");
			if ( e.getMessage().equals(Constantes._EX_ELM_ID_NO_EXISTE) ){
				logger.debug("El elemento no se puede crear...");
				fp.add( "rc" , Constantes._EX_ELM_ID_NO_EXISTE );
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
     	}
  		
  		logger.debug("result:"+id);

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
