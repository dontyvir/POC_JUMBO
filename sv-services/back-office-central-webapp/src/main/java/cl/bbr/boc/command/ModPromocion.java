package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.promos.dto.PromocionDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que permite modificar la información de la promoción, segun los datos 
 * ingresados en el formulario
 * @author BBR
 *
 */
public class ModPromocion extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl 			="";
     int	codigo  			= -1;
     String paramDesc       	= "";
     String paramSust   		= "";
     String paramFalt	        = "";
     String paramBanner			= "";
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("codigo") == null ){throw new ParametroObligatorioException("id_promo es null");}
     
     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     codigo = Integer.parseInt(req.getParameter("codigo"));
     
     if (req.getParameter("desc") != null){
    	 paramDesc = req.getParameter("desc");
     }
     if (req.getParameter("sustituto") != null){
    	 paramSust = req.getParameter("sustituto");
     }
     if (req.getParameter("faltante") != null ){
    	 paramFalt = req.getParameter("faltante");
     }
     if (req.getParameter("banner") != null){
    	 paramBanner = req.getParameter("banner");
     }

     ForwardParameters fp = new ForwardParameters();
     fp.add( req.getParameterMap() );
     
	//obtiene mensajes
	String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito"); 
	String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
     /*
      * 3. Procesamiento Principal
      */
   	BizDelegate biz = new BizDelegate();
     	
 	try{
 		PromocionDTO promo = new PromocionDTO();
 		promo.setCod_promo(codigo);
 		promo.setDescr(paramDesc);
 		promo.setSustituible(paramSust);
 		promo.setFaltante(paramFalt);
 		promo.setBanner(paramBanner);
 		
 		boolean result = biz.updPromocion(promo);
		logger.debug("result?"+result);
		if(result){
			paramUrl += "&msje=" +  mensaje_exito;
		}else{
			paramUrl += "&msje=" +  mensaje_fracaso;
		}
		
 	}catch(BocException e){
 		logger.debug("Controlando excepción: " + e.getMessage());
		String UrlError = getServletConfig().getInitParameter("UrlError");
		if ( e.getMessage()!= null && !e.getMessage().equals("") ){
			logger.debug(e.getMessage());
			fp.add( "msje" , e.getMessage() );
			fp.add( "codigo" , String.valueOf(codigo) );
			paramUrl = UrlError + fp.forward(); 
		} else { 
			logger.debug("Controlando excepción: " + e.getMessage());
		}
 	}
      // 4. Redirecciona salida
     res.sendRedirect(paramUrl);
 }//execute
}
