package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
//import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCatWebDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * AddCatWeb Comando Process
 * Agrega una Categoría Web
 * @author BBRI
 */

public class AddCatWeb extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl = "";
     String paramNombre="";
     String paramDescripcion="";
     String paramEstado="";
     String paramTipo="";
     long paramOrden=0L;
     String paramRuta_banner="";
     String paramTotem="";
     String paramImagen="";
     String fec_crea = Formatos.getFecHoraActual();
     
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("nombre") == null ){throw new ParametroObligatorioException("nombre es null");}
     if ( req.getParameter("desc") == null ){throw new ParametroObligatorioException("desc es null");}
     if ( req.getParameter("estado") == null ){throw new ParametroObligatorioException("estado es null");}
     if ( req.getParameter("tipo") == null ){throw new ParametroObligatorioException("tipo es null");}
     if ( req.getParameter("orden") == null ){throw new ParametroObligatorioException("orden es null");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramNombre = req.getParameter("nombre"); //string:obligatorio:no
     paramDescripcion = req.getParameter("desc"); //string:obligatorio:no
     paramEstado = req.getParameter("estado"); //string:obligatorio:no
     paramTipo = req.getParameter("tipo"); //string:obligatorio:no
     paramOrden = Long.parseLong(req.getParameter("orden")); //long:obligatorio:no
     if ( req.getParameter("arch") != null )
    	 paramRuta_banner =  req.getParameter("arch"); //string:obligatorio:no
     else
    	 paramRuta_banner = "logo2.jpg";
     paramTotem = req.getParameter("totem");
     paramImagen = req.getParameter("imagen");
     
     String paramUrlBanner=(req.getParameter("url_banner") != null && ("I".equalsIgnoreCase(req.getParameter("tipo")) || "T".equalsIgnoreCase(req.getParameter("tipo")))) ? req.getParameter("url_banner") : null;
     paramUrlBanner=(paramUrlBanner != null && (req.getParameter("arch") != null || req.getParameter("imagen") != null)) ? req.getParameter("url_banner") :"";
     
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("nombre: " + paramNombre);
     logger.debug("descripcion: " + paramDescripcion);
     logger.debug("estado: " + paramEstado);
     logger.debug("tipo: " + paramTipo);
     logger.debug("orden: " + paramOrden);
     logger.debug("ruta_banner: " + paramRuta_banner);
     logger.debug("totem: " + paramTotem);
     logger.debug("imagen: " + paramImagen);
     logger.debug("UrlBanner: " + paramUrlBanner);
     //ForwardParameters fp = new ForwardParameters();
     /*
      * 3. Procesamiento Principal
      */
  		BizDelegate biz = new BizDelegate();
  		ProcAddCatWebDTO prm = new ProcAddCatWebDTO(paramNombre, paramDescripcion, paramEstado, paramTipo, 
  				paramOrden, fec_crea, paramRuta_banner); 
  		prm.setTotem(paramTotem);
        prm.setImagen(paramImagen);
     
        prm.setUrlBanner(paramUrlBanner);
  		long result = 0;
  		try{
  			result = biz.setAddCatWeb(prm);
  		} catch(Exception e){
  			logger.debug("Controlando excepción: " + e.getMessage());
  		}
  		
  		logger.debug("result:"+result);

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
