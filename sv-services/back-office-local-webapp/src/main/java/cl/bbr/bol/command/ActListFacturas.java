package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.DbSQLCode;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.FacturasDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Elimina un horario de picking para un local con todas sus 
 * jornadas de picking asociadas
 * @author jsepulveda
 */
public class ActListFacturas extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Variables del método
		String URL       = "";
		long   id_pedido = 0L;
		long   num_doc   = 0L;
		String accion    = "";
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);		
	
		logger.debug("QueryString: " + req.getQueryString());
		logger.debug("QueryString: " + req.getQueryString());
		
		// 2.1 Parámetro id_pedido (Obligatorio)
		if ( req.getParameter("id_pedido") == null){
			throw new ParametroObligatorioException("id_pedido es null");
		}
		id_pedido = Long.parseLong(req.getParameter("id_pedido"));
		logger.debug("id_pedido: " + id_pedido);
		

		// 2.2 Parámetro url (Obligatorio)
		if ( req.getParameter("url") == null ){
			throw new ParametroObligatorioException("url es null");
		}
		URL = req.getParameter("url")+"?id_pedido="+id_pedido;		
		logger.debug("URL: " + URL);
		
		
		
		// 2.3 Parámetro num_doc (Obligatorio)
		if ( req.getParameter("num_doc") == null){
			throw new ParametroObligatorioException("num_doc es null");			
		}
		num_doc = Long.parseLong(req.getParameter("num_doc"));
		logger.debug("num_doc: " + num_doc);
		
		
		// 2.4 Parámetro accion (Obligatorio)
		if ( req.getParameter("accion") == null){
			throw new ParametroObligatorioException("accion es null");			
		}
		accion = req.getParameter("accion");
		logger.debug("accion: " + accion);
		
		FacturasDTO fact = new FacturasDTO();
		fact.setAccion(accion);
		fact.setId_pedido(id_pedido);
		fact.setNum_doc(num_doc);
		fact.setLogin(usr.getLogin());
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		ForwardParameters fp = new ForwardParameters();
		//fp.add( req.getParameterMap() );
		try{
		    biz.ActListFacturas(fact);
			//logger.debug("Se eliminó la Factura " + num_doc + " correctamente");
			//paramUrl = paramUrl;
		}catch(BolException ex){
			logger.debug("Controlando excepción: " + ex.getMessage());
            if ( ex.getMessage().equals(DbSQLCode.SQL_DUP_KEY_CODE) ){ 
                logger.info("La Factura Nº " + fact.getNum_doc() + " ya se encuentra asociada al Pedido Nº " + fact.getId_pedido());
                fp.add("id_pedido", ""+fact.getId_pedido());
				fp.add( "mensaje" , "La Factura Nº " + fact.getNum_doc() + " ya se encuentra asociada al Pedido Nº " );
				URL = UrlError + fp.forward();
			}
		}
		
		// 4. Redirecciona salida
		res.sendRedirect(URL);
	
	}		
	
	
}
