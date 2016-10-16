package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModSubCatWebDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que permite modificar las categorias web
 * @author bbr
 *
 */
public class ModSubCatWeb extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
     
     // 1. seteo de Variables del método, obligatorios
     String paramUrl = "";
     String paramAction="";
     String paramIdSubCat="";
     String paramIdCat="";
     
     long id_sub_cat = -1;
     long id_cat = -1;
     
     // 2. Procesa parámetros del request

     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("action") == null ){throw new ParametroObligatorioException("action es nulo");}
     if ( req.getParameter("sel_cat") == null ){throw new ParametroObligatorioException("sel_cat es nulo");}
     if ( req.getParameter("id_cat") == null ){throw new ParametroObligatorioException("id_cat es nulo");}
     

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramAction = req.getParameter("action"); 
     paramIdSubCat = req.getParameter("sel_cat");
     id_sub_cat = Long.parseLong(paramIdSubCat); 
     paramIdCat = req.getParameter("id_cat");
     id_cat = Long.parseLong(paramIdCat); 
     
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("action: " + paramAction);	//Agregar / Desasociar
     logger.debug("sel_cat: " + paramIdSubCat);
     logger.debug("id_cat: " + paramIdCat);
     
     ForwardParameters fp = new ForwardParameters();
     fp.add( req.getParameterMap() );
     /*
      * 3. Procesamiento Principal
      */
     	BizDelegate bizDelegate = new BizDelegate();
     	
     	try{ 
     		ProcModSubCatWebDTO prm = new ProcModSubCatWebDTO(paramAction, id_sub_cat,  id_cat, usr.getId_usuario()); 
			boolean result = bizDelegate.setModSubCatWeb(prm);
			logger.debug("result:"+result);
     	}catch(BocException e){
     		logger.debug("Controlando excepción: " + e.getMessage());
			String UrlError = getServletConfig().getInitParameter("UrlError");
			if ( e.getMessage().equals(Constantes._EX_CAT_SUBCAT_IGUALES) ){
				logger.debug("La categoria y subcategoria son iguales");
				fp.add( "rc" , Constantes._EX_CAT_SUBCAT_IGUALES );
				paramUrl = UrlError + fp.forward(); 
			} if ( e.getMessage().equals(Constantes._EX_CAT_ID_NO_EXISTE) ){
				logger.debug("La categoria no existe");
				fp.add( "rc" , Constantes._EX_CAT_ID_NO_EXISTE );
				paramUrl = UrlError + fp.forward(); 
			} if ( e.getMessage().equals(Constantes._EX_CAT_SUBCAT_ID_NO_EXISTE) ){
				logger.debug("La subcategoria no existe");
				fp.add( "rc" , Constantes._EX_CAT_SUBCAT_ID_NO_EXISTE );
				paramUrl = UrlError + fp.forward(); 
			} if ( e.getMessage().equals(Constantes._EX_CAT_SUBCAT_ASIG_INCORRECTA) ){
                logger.debug("Asigne correctamente los tipos de categoría y subcategoría");
                fp.add( "rc" , Constantes._EX_CAT_SUBCAT_ASIG_INCORRECTA );
                paramUrl = UrlError + fp.forward(); 
            }if ( e.getMessage().equals(Constantes._EX_CAT_SUBCAT_REL_EXISTE) ){
				logger.debug("La subcategoria ya se encuentra asociada a la categoria");
				fp.add( "rc" , Constantes._EX_CAT_SUBCAT_REL_EXISTE );
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
     	}
     	
	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);

 	}//execute

}

