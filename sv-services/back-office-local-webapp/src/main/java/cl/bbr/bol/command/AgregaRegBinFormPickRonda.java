package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.BinFormPickDTO;
import cl.bbr.jumbocl.pedidos.dto.FPickDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 *  Comando que permite agregar un registro de picking. 
 *  Además permite crear un nuevo bin.
 *   
 * @author BBR
 *
 */


public class AgregaRegBinFormPickRonda extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Variables del método
		String paramUrl		 = "";
		String cod_bin = "";
		String cod_ubica = "";
		long id_ronda = -1;
		long id_bin = -1;
		double cantidad = 0;
		String cod_barra = "";
		int posicion=0;
		long id_op = 0;
		String tipo_bin = "";
		
		
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("UrlError");
		//logger.debug("UrlError: " + UrlError);		
		
		// 2.1 Parámetro url (Obligatorio)
		if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
		paramUrl = req.getParameter("url");
		logger.debug("url: " + paramUrl);
		
		if ( req.getParameter("cod_bin") == null ){throw new ParametroObligatorioException("cod_bin es null");}
		cod_bin = req.getParameter("cod_bin");
		logger.debug("cod_bin: "+cod_bin);
		
		
		if ( req.getParameter("id_ronda") == null ){throw new ParametroObligatorioException("id_ronda es null");}
		id_ronda = Long.parseLong(req.getParameter("id_ronda"));
		logger.debug("id_ronda: " + id_ronda);
		
		if ( req.getParameter("sel_bin") == null ){throw new ParametroObligatorioException("sel_bin es null");}
		id_bin = Long.parseLong(req.getParameter("sel_bin"));
		logger.debug("sel_bin: " + id_bin);
		
		if ( req.getParameter("cantidad") == null ){throw new ParametroObligatorioException("cantidad es null");}
		cantidad = Double.parseDouble(req.getParameter("cantidad"));
		logger.debug("cantidad: " + cantidad);
		
		if ( req.getParameter("cod_barra") == null ){throw new ParametroObligatorioException("cod_barra es null");}
		cod_barra = req.getParameter("cod_barra");
		logger.debug("cod_barra: " + cod_barra);
		
		if (req.getParameter("posicion") != null){
			posicion = Integer.parseInt(req.getParameter("posicion"));
		}
		if (req.getParameter("id_pedido") != null){
			id_op = Long.parseLong(req.getParameter("id_pedido"));
		}
		
		if (req.getParameter("tipo_flujo")!= null && req.getParameter("tipo_flujo").equals(Constantes.LOCAL_TIPO_FLUJO_NORMAL_CTE)){
			if (req.getParameter("cod_ubica") == null){throw new ParametroObligatorioException("ubicacion es null");}
		}
		
		if ( req.getParameter("cod_ubica") != null ){
			cod_ubica = req.getParameter("cod_ubica");
		}
		
		if ( req.getParameter("tipo_bin") != null ){
			tipo_bin = req.getParameter("tipo_bin");
		}
		
		logger.debug("tipo_flujo: " + req.getParameter("tipo_flujo"));
		logger.debug("cod_ubica: " + cod_ubica);
		logger.debug("tipo bin: " + tipo_bin);
		//paramUrl += "&cod_ubica="+cod_ubica+"&cod_bin="+cod_bin;
			// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		try{
			BinFormPickDTO bin = new BinFormPickDTO();
			bin.setCod_bin(cod_bin);
			bin.setCod_ubicacion(cod_ubica);
			bin.setId_ronda(id_ronda);
			bin.setTipo(tipo_bin);
			bin.setPosicion(posicion);
			
			
			
			
			bin.setId_pedido(id_op);
			
			
			
			FPickDTO pick = new FPickDTO();
			pick.setCbarra(cod_barra);
			pick.setCantidad(cantidad);
			pick.setCant_pend(cantidad);
			pick.setId_form_bin(id_bin);
			pick.setId_ronda(id_ronda);
			
			//Agregamos el id de pedido al bin
			//bin.setId_pedido(pick.getId_pedido());
			id_bin = biz.doAgregaDetalleFormPicking(pick, bin);
			fp.add("sel_bin",String.valueOf(id_bin));
		}catch(BolException ex){
			if(ex.getMessage().equals(Constantes._EX_RON_ID_INVALIDO)){
				logger.debug("Id del ronda es inválido");
				fp.add( "rc" , Constantes._EX_RON_ID_INVALIDO);
				paramUrl = UrlError + fp.forward();
			}else if(ex.getMessage().equals(Constantes._EX_VE_FPIK_OP_RONDA_NO_EXISTE)){
				logger.debug("Error: " +ex.getMessage());
				fp.add( "rc" , Constantes._EX_VE_FPIK_OP_RONDA_NO_EXISTE);
				paramUrl = UrlError + fp.forward();
			}
			else{
				logger.debug("Error: " +ex.getMessage());
				fp.add( "rc" , Constantes._EX_VE_FPIK_NO_REALIZO_PICKING);
				paramUrl = UrlError + fp.forward();
			}
		}
		// 4. Redirecciona salida
		paramUrl += fp.forward();
		res.sendRedirect(paramUrl);
	
	}	
	
	
}
