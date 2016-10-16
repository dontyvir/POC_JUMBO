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
 * Cambia el estado a un despacho y agrega al log de despacho
 * @author jsepulveda
 */
public class ModSectoresxProd extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_local = 0L;
		long id_sector = 0L;
		long id_bo = 0L;
		long cod_prod = 0L;
		
		// 1. Variables del método
		String paramUrl	= "";
		// Recupera pagina desde web.xml
		String msjeOK = getServletConfig().getInitParameter("msjeOK");		
		//String msjeMAL = getServletConfig().getInitParameter("msjeMAL");
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		
		if ( req.getParameter("url") == null ){ throw new ParametroObligatorioException("url es null");	}
		paramUrl = req.getParameter("url");
		logger.debug("url: " + paramUrl);
		
		if ( req.getParameter("sector") == null ){ throw new ParametroObligatorioException("sectores es null");	}
		id_sector = Long.parseLong(req.getParameter("sector").toString());		
		logger.debug("sector: " + id_sector);
		
		if ( req.getParameter("id_bo") == null ){ throw new ParametroObligatorioException("id_bo es null");	}
		id_bo = Long.parseLong(req.getParameter("id_bo"));
		logger.debug("id_bo: " + id_bo);
		
		if ( req.getParameter("cod_prod") == null ){ throw new ParametroObligatorioException("cod_prod es null");	}
		cod_prod = Long.parseLong(req.getParameter("cod_prod"));
		logger.debug("cod_prod: " + cod_prod);

		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		try {
		    boolean cambio = false;
		    //if ( sectorLocal.length > 0 ) {
		    //    for (int i=0; i < sectorLocal.length; i++ ) {
		    //        id_local  = Long.parseLong( sectorLocal[i].split("-")[0] );
		    //        id_sector = Long.parseLong( sectorLocal[i].split("-")[1] );
		            if (biz.getSectorByProd(id_bo) != id_sector ) {
			            biz.setDelProductoXSector(id_bo);
						biz.setAddProductoXSector(id_bo, id_sector);
						cambio = true;
		            }
		    //    }		        
		    //}
		    if (!cambio) {
		        paramUrl +="&mensaje=No se realizaron cambios";    
		    } else {
		        paramUrl +="&mensaje="+msjeOK;    
		    }
		    			
		} catch( BocException e ) {
			String UrlError =  getServletConfig().getInitParameter("UrlError");
			String msjeMAL ="";
			fp.add( "cod_prod" , cod_prod+"" );
			if (  e.getMessage().equals(Constantes._EX_ID_SECTOR_INVALIDO) ){
				logger.debug("El código del sector ingresado no existe");
				msjeMAL = "El código del sector ingresado no existe";
				fp.add( "rc" , Constantes._EX_ID_SECTOR_INVALIDO );
				paramUrl = UrlError + fp.forward();
			} else if (  e.getMessage().equals(Constantes._EX_PSAP_ID_NO_EXISTE) ){
				logger.debug("El código del producto ingresado no existe");
				msjeMAL = "El código del producto ingresado no existe";
				fp.add( "rc" , Constantes._EX_PSAP_ID_NO_EXISTE );
				paramUrl = UrlError + fp.forward();
			} 
			paramUrl += "&mensaje="+msjeMAL;
			//logger.debug("URL DENTRO DEL CATCH : "+paramUrl);
		}
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}		
	
}
