package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.SectorLocalDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * comando que permite agregar una jornada de Despacho
 * @author mleiva
  */


public class EditarSectorPicking extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Variables del método
		String paramUrl			= "";
		String nombre_sector 	= "";
		String paramId_sector	= "";
		String paramMaxProd		= "";
		String paramMaxOp		= "";
		String paramMinOpFill	= "";
		String paramCantMinProds= "";
		
		long id_sector			= 0L;
		long max_prod			= 0L;
		long max_op				= 0L;
		long min_op_fill		= 0L;
		long cant_min_prods		= 0L;
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);		
		
		// 2.1 Parámetro url (Obligatorio)
		if ( req.getParameter("url") == null ){	throw new ParametroObligatorioException("url es null");}
		paramUrl = req.getParameter("url");
		logger.debug("url: " + paramUrl);
		
		if ( req.getParameter("sector") == null ){throw new ParametroObligatorioException("sector es null");}
		nombre_sector = req.getParameter("sector");
		logger.debug("nombre_sector: " + nombre_sector);

		if ( req.getParameter("id_sector") == null ){throw new ParametroObligatorioException("id_sector es null");}
		paramId_sector = req.getParameter("id_sector");
		id_sector = Long.parseLong(paramId_sector);
		logger.debug("id_sector: " + paramId_sector);
		
		if ( req.getParameter("max_prod") == null ){throw new ParametroObligatorioException("max_prod es null");}
		paramMaxProd = req.getParameter("max_prod");
		max_prod = Long.parseLong(paramMaxProd);
		logger.debug("max_prod: " + paramMaxProd);
		
		if ( req.getParameter("max_op") == null ){throw new ParametroObligatorioException("max_op es null");}
		paramMaxOp = req.getParameter("max_op");
		max_op = Long.parseLong(paramMaxOp);
		logger.debug("max_op: " + paramMaxOp);
		
		if ( req.getParameter("min_op_fill") == null ){throw new ParametroObligatorioException("min_op_fill es null");}
		paramMinOpFill = req.getParameter("min_op_fill");
		min_op_fill = Long.parseLong(paramMinOpFill);
		logger.debug("min_op_fill: " + paramMinOpFill);
		
		if ( req.getParameter("cant_min_prods") == null ){throw new ParametroObligatorioException("min_op_fill es null");}
		paramCantMinProds = req.getParameter("cant_min_prods");
		cant_min_prods = Long.parseLong(paramCantMinProds);
		logger.debug("cant_min_prods: " + paramCantMinProds);
		
			// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		try{
			SectorLocalDTO sector = new SectorLocalDTO();
			sector.setNombre(nombre_sector);
			sector.setId_sector(id_sector);
			sector.setMax_prod(max_prod);
			sector.setMax_op(max_op);
			sector.setMin_op_fill(min_op_fill);
			sector.setCant_min_prods(cant_min_prods);
			
			biz.doActualizaSectorPicking(sector);
		}catch(BolException ex){
			if(ex.getMessage().equals(Constantes._EX_ID_SECTOR_INVALIDO)){
				logger.debug("Id del sector es inválido");
				fp.add( "rc" , Constantes._EX_ID_SECTOR_INVALIDO);
				paramUrl = UrlError + fp.forward();
			}
		} //4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}	
	
	
}
