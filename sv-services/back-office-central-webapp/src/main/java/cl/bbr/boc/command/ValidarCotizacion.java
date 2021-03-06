package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que permite validar una cotizacion determinada
 * @author bbr
 */
public class ValidarCotizacion extends Command {
	private final static long serialVersionUID = 1;

	 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	     // 1. seteo de Variables del m�todo
	     String paramUrl = "";
	     long paramId_cot=0L;
	     
	     // 2. Procesa par�metros del request
	     logger.debug("Procesando par�metros...");

	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){ throw new ParametroObligatorioException("url es null"); }
	     if ( req.getParameter("id_cotizacion") == null ){ throw new ParametroObligatorioException("id_cotizacion es nulo"); }
	      
	     // 2.2 obtiene parametros desde el request
	     paramUrl = req.getParameter("url");
	     paramId_cot = Long.parseLong(req.getParameter("id_cotizacion")); //long:obligatorio:si
	     
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("id_cotizacion: " + paramId_cot);
	     logger.debug("id_usuario actual:"+ usr.getId_usuario());
	     
	     ForwardParameters fp = new ForwardParameters();

	     /*
	      * 3. Procesamiento Principal
	      * 
	      * Validaci�n de la cotizacion
	      * Paso 1: Eliminar las alertas de la cotizacion a validar
	      * Paso 2: Revisar cada alerta
	      * Paso 3: Cambio de estado: si existen alertas -> En Validacion
	      * 						  no existen alertas -> Cotizada
	      */
	     BizDelegate biz = new BizDelegate();
	     try{
		     
		    boolean valido = biz.setValidarCotizacion(paramId_cot);
		    logger.debug("valido ? "+valido );
		     
	     }catch(BocException e){
	    	 logger.debug("Error:"+e.getMessage());
	    	 String UrlError = getServletConfig().getInitParameter("UrlError");
	  			logger.debug("Controlando excepci�n: " + e.getMessage());
				fp.add( "id_cotizacion" , paramId_cot+"" );
				fp.add( "mod" , "1" );
	  			if (  e.getMessage().equals(Constantes._EX_COT_ID_NO_EXISTE) ){
					logger.debug("El c�digo de cotizacion no existe");
					fp.add( "rc" , Constantes._EX_COT_ID_NO_EXISTE );
					paramUrl = UrlError + fp.forward();
				} 
			logger.debug("paramUrl:"+paramUrl);
	     }
	     
	     res.sendRedirect(paramUrl);
	 }
}
	 