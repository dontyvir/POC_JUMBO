package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcModGenericItemDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Comando que permite modificar un item de genericos
 * @author bbr
 *
 */
public class ModGenericItem extends Command {
	private final static long serialVersionUID = 1;
 
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	     
	     // 1. seteo de Variables del método
	     String paramUrl = "";
	     String paramAction = "";
	     long paramId_prod_item = 0L;
	     long paramId_prod_gen = 0L;
	     String paramAtrdiff_val = "";
	     // 2. Procesa parámetros del request
	
	     logger.debug("Procesando parámetros...");
	
	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if ( req.getParameter("action") == null ){throw new ParametroObligatorioException("action es nulo");}
	     if ( req.getParameter("id_prod_item") == null ){throw new ParametroObligatorioException("id_prod_item es nulo");}
	     if ( req.getParameter("id_prod_gen") == null ){throw new ParametroObligatorioException("id_prod_gen es nulo");}
	     if ( req.getParameter("atrdiff_val") == null ){throw new ParametroObligatorioException("atrdiff_val es nulo");}
	
	     // 2.2 obtiene parametros desde el request
	     paramUrl = req.getParameter("url");
	     paramAction = req.getParameter("action"); //string:obligatorio:si
	     paramId_prod_item = Long.parseLong(req.getParameter("id_prod_item"));
	     paramId_prod_gen = Long.parseLong(req.getParameter("id_prod_gen")); //long:obligatorio:si
	     paramAtrdiff_val = req.getParameter("atrdiff_val"); //string:obligatorio:si
	
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("action: " + paramAction);
	     logger.debug("id_prod_gen: " + paramId_prod_gen);
	     logger.debug("atrdiff_val: " + paramAtrdiff_val);
	     logger.debug("id_prod_item: " + paramId_prod_item);
	     
	     ForwardParameters fp = new ForwardParameters();
	     fp.add( req.getParameterMap() );
	     /*
	      * 3. Procesamiento Principal
	      */
	     BizDelegate biz = new BizDelegate();
	     
	     String mensAgreg = getServletConfig().getInitParameter("MensAgreg");
	     String mensDesa = getServletConfig().getInitParameter("MensDesa");
	     try{
	    	 ProcModGenericItemDTO proc = new ProcModGenericItemDTO (paramAction, paramId_prod_item, paramId_prod_gen, paramAtrdiff_val, 
	    			 mensAgreg, mensDesa, usr.getLogin()); 
	    	 boolean resp = biz.agregaItemProducto(proc);
			logger.debug("agregar:"+resp);
	     }catch(BocException e) {
				logger.debug("Controlando excepción: " + e.getMessage());
				String UrlError = getServletConfig().getInitParameter("UrlError");
				if ( e.getMessage().equals(Constantes._EX_PROD_ITEM_IGUAL_PROD) ){
					logger.debug("El producto item debe ser distinto al producto");
					fp.add( "rc" , Constantes._EX_PROD_ITEM_IGUAL_PROD );
					paramUrl = UrlError + fp.forward(); 
				} else if ( e.getMessage().equals(Constantes._EX_PROD_ID_NO_EXISTE) ){
					logger.debug("El código de producto ingresado no existe");
					fp.add( "rc" , Constantes._EX_PROD_ID_NO_EXISTE );
					paramUrl = UrlError + fp.forward(); 
				} else if (e.getMessage().equals(Constantes._EX_PROD_ITEM_GENERICO) ){
						logger.debug("No se permite asociar un producto genérico");
						fp.add( "rc" , Constantes._EX_PROD_ITEM_GENERICO );
						paramUrl = UrlError + fp.forward(); 
						logger.debug("paramUrl:"+paramUrl);
				} else {
					logger.debug("Controlando excepción: " + e.getMessage());
				}
		} catch(Exception e){
			logger.debug("Controlando excepción desconocida: " + e.getMessage());
				
		}
	     
	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);
	
	 }//execute

}
