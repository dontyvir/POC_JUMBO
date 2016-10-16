package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModCatWebDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que permite modificar las categorias Web
 * @author bbr
 *
 */
public class ModCatWeb extends Command {
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
     long paramId_categoria=0L;
     
     String fec_act = Formatos.getFecHoraActual();
     // 2. Procesa parámetros del request

     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_categoria") == null ){throw new ParametroObligatorioException("id_categoria es nulo");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramNombre = req.getParameter("nombre"); //string:obligatorio:no
     paramDescripcion = req.getParameter("descripcion"); //string:obligatorio:no
     paramEstado = req.getParameter("estado"); //string:obligatorio:no
     paramTipo = req.getParameter("tipo"); //string:obligatorio:no
     paramOrden = Long.parseLong(req.getParameter("orden")); //long:obligatorio:no
   	 paramRuta_banner = req.getParameter("arch"); //string:obligatorio:no
   	 paramTotem = req.getParameter("totem");
     paramImagen = req.getParameter("imagen");
     String paramUrlBanner=(req.getParameter("url_banner") != null) ? req.getParameter("url_banner") :"";
     
     paramId_categoria = Long.parseLong(req.getParameter("id_categoria")); //long:obligatorio:si
     logger.debug("Archivo de imagen: "+ req.getParameter("arch"));
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
     logger.debug("id_categoria: " + paramId_categoria);
     logger.debug("UrlBanner: " + paramUrlBanner);
     
     ForwardParameters fp = new ForwardParameters();
     fp.add( req.getParameterMap() );
     
     /*
      * 3. Procesamiento Principal
      */
     	BizDelegate bizDelegate = new BizDelegate();
     	
     	try{
    		ProcModCatWebDTO modCat = new ProcModCatWebDTO(paramNombre, paramDescripcion, paramEstado, paramTipo, 
    				paramOrden, fec_act, usr.getId_usuario() , paramRuta_banner, paramId_categoria);
    		modCat.setTotem(paramTotem);
            modCat.setImagen(paramImagen);
            modCat.setUrlBanner(paramUrlBanner);
            
    		bizDelegate.setModCatWeb(modCat);
     	}catch(BocException e){
     		logger.debug("Controlando excepción: " + e.getMessage());
			String UrlError = getServletConfig().getInitParameter("UrlError");
			if ( e.getMessage().equals(Constantes._EX_CAT_ID_NO_EXISTE) ){
				logger.debug("La categoria no existe");
				fp.add( "rc" , Constantes._EX_CAT_ID_NO_EXISTE );
				fp.add("categoria_id", paramId_categoria+"");
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
     	}

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
