package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.PoligonoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


public class AddPoligono extends Command {
	private final static long serialVersionUID = 1;
 
    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

	    // 1. seteo de Variables del método
	    String Url       = "";
	    long   id_comuna = 0L;
	    long   numPol    = 0L;
	    String descPol   = "";
	    
	    
		// 1.1 Parámetros de inicialización servlet
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);
		
	    
	    // 2. Procesa parámetros del request
	    logger.debug("Procesando parámetros...");
	    
	    // 2.1 revision de parametros obligatorios
	    if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	    if ( req.getParameter("num_pol") == null ){throw new ParametroObligatorioException("num_pol es null");}
	   	if ( req.getParameter("desc_pol") == null ){throw new ParametroObligatorioException("desc_pol es null");}
	    if ( req.getParameter("id_comuna") == null ){throw new ParametroObligatorioException("id_comuna es null");}
	    
	    
	    // 2.2 obtiene parametros desde el request
	    Url    = req.getParameter("url");
	    id_comuna = Long.parseLong(req.getParameter("id_comuna"));
	    numPol = Long.parseLong(req.getParameter("num_pol"));
	    descPol= req.getParameter("desc_pol");
	    
	    
	    // 2.3 log de parametros y valores
	    logger.debug("url       : " + Url);
	    logger.debug("id_comuna : " + id_comuna);
	    logger.debug("num_pol   : " + numPol);
	    logger.debug("desc_pol  : " + descPol);
	    
	    /*
	     * 3. Procesamiento Principal
	     */
	    BizDelegate biz = new BizDelegate();
		PoligonoDTO pol = new PoligonoDTO();
		pol.setId_comuna(id_comuna);
		pol.setNum_poligono(numPol);
		pol.setDescripcion(descPol);
		
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		long id_poligono = 0;
		
		try {
			id_poligono = biz.addPoligono(pol);
			logger.debug("Se insertó usuario: " + id_poligono); 
			fp.add( "usr_cod" , id_poligono+"" );
			Url = Url + fp.forward();
		} catch (BocException e) {
			logger.debug("Controlando excepción: " + e.getMessage());
			if ( e.getMessage().equals(Constantes._EX_USR_LOGIN_DUPLICADO) ){
				logger.debug("El login ya existe en el sistema");
				fp.add( "rc" , Constantes._EX_USR_LOGIN_DUPLICADO );
				Url = UrlError + fp.forward();
			}
		}
		
	    // 4. Redirecciona salida
		logger.debug("Redireccionando a: " + Url);
		res.sendRedirect(Url);
    }//execute
}