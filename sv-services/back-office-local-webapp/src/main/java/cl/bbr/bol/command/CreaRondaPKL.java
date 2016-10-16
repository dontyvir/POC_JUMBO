package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.CreaRondaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que Crea una ronda de picking para un local en un sector determinado
 * @author jsepulveda
 */
public class CreaRondaPKL extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		// 1. Variables del método
		
		// 1.1 parámetro Url
		String paramUrl	 = "";
		String paramId_jornada = "";
		String paramId_pedido  = "";
		//String paramCant_prod  = "";
		//List   indices = new ArrayList();
		long   id_jornada = -1L;
		long   id_pedido  = -1L;
		long   id_ronda   = -1L;
		String cant_prod  = "";
		String paramTipo_ve = "";

		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		//Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);
		
		String MsjeError = getServletConfig().getInitParameter("MsjeError");
		MsjeError += " " + Constantes.CANT_MAX_PED_X_RONDA;
		logger.debug("MsjeError: " + MsjeError );
		
		paramUrl = "ViewResCrearRondaPKLOk";
		logger.debug("url: " + paramUrl);
		
		// 2.1 Parámetro cant_prod
		if ( req.getParameter("cant_prod") == null ){ throw new ParametroObligatorioException("cant_prod es null");}
		cant_prod = req.getParameter("cant_prod");
		logger.debug("cant_prod: " + cant_prod);

		
		// 2.2 Parámetro id_pedido
		if ( req.getParameter("id_pedido") == null ){ throw new ParametroObligatorioException("id_pedido es null");}
		paramId_pedido = req.getParameter("id_pedido");
		logger.debug("id_pedido: " + paramId_pedido);
		id_pedido = Long.parseLong(paramId_pedido);
		
		// 2.3 Parámetro id_jornada
		if ( req.getParameter("id_jornada") == null ){ throw new ParametroObligatorioException("id_jornada es null");}
		paramId_jornada = req.getParameter("id_jornada");
		logger.debug("id_jornada: " + paramId_jornada);
		id_jornada = Long.parseLong(paramId_jornada);
		
		// 2.4 Tipo VE N:Normal o S:Especial
		if ( req.getParameter("tipo_ve") == null ){ throw new ParametroObligatorioException("tipo_ve es null");}
		paramTipo_ve = req.getParameter("tipo_ve");
		logger.debug("tipo_ve: " + paramTipo_ve);		
		
	
		// 3. Procesamiento Principal
		
		BizDelegate biz	= new BizDelegate();
		
		CreaRondaDTO rondanueva = new CreaRondaDTO();
		rondanueva.setId_jpicking(id_jornada);
		rondanueva.setId_pedido(id_pedido);
		rondanueva.setId_local(usr.getId_local());
		rondanueva.setCant_prod(cant_prod);
		//rondanueva.setLst_indices(indices);
		rondanueva.setTipo_ve(paramTipo_ve);
		
		/*if (usr.getLocal_tipo_picking().equals("L")){
		    usr.getl
		}*/
		
		//biz.CreaRonda(id_sector, id_jornada, lista);
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap());
		try{
			id_ronda = biz.CreaRondaPKL(rondanueva);
		}catch(BolException ex){
			if(ex.getMessage().equals(Constantes._EX_PED_ID_INVALIDO)){
				logger.debug("Id del pedido es inválido");
				fp.add( "rc" , Constantes._EX_PED_ID_INVALIDO);
				paramUrl = UrlError + fp.forward();
			}else if(ex.getMessage().equals(Constantes._EX_RON_ID_INVALIDO)){
				logger.debug("Id de la ronda es inválido");
				fp.add( "rc" , Constantes._EX_RON_ID_INVALIDO);
				paramUrl = UrlError + fp.forward();
			}else if(ex.getMessage().equals(Constantes._EX_RON_MAX_PEDIDOS_X_RONDA)){
				logger.debug("No se pudo crear la Ronda, pedidos superan los "+Constantes._EX_RON_MAX_PEDIDOS_X_RONDA);
				fp.add( "rc" , Constantes._EX_RON_MAX_PEDIDOS_X_RONDA);
				fp.add("msje",MsjeError);
				paramUrl = UrlError + fp.forward();
			}else if(ex.getMessage().equals(Constantes._EX_RON_SIN_PEDIDOS_SELEC)){
				logger.debug("no hay pedidos seleccionados");
				fp.add( "rc" , Constantes._EX_RON_SIN_PEDIDOS_SELEC);
				fp.add("msje",MsjeError);
				paramUrl = UrlError + fp.forward();
			}else if(ex.getMessage().equals(Constantes._EX_RON_PEDIDO_NO_COINCIDE)){
				logger.debug("Pulso el boton atras");
				fp.add( "rc" , Constantes._EX_RON_PEDIDO_NO_COINCIDE);
				fp.add("msje","No es posible crear la ronda debido a que pulso el boton Atras del Internet Explorer");
				paramUrl = UrlError + fp.forward();
			}
		}
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl+"?tipo_ve="+paramTipo_ve+"&id_jornada="+id_jornada+"&id_ronda="+id_ronda+"&mjeError="+MsjeError);
	}
	
}
