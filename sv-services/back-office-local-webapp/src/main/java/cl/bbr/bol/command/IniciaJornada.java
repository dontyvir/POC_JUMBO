package cl.bbr.bol.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.ctrl.PedidosCtrl;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * comando que permite iniciar una jornada y agrega un mensaje al log de la jornada 
 * @author mleiva
 */
public class IniciaJornada extends Command{

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Variables del método
		String paramUrl	= "";
		String paramIdJpicking ="";
		long id_jpicking = -1;
		
		
		// 1. Parámetros de inicialización servlet
		String UrlError01 = getServletConfig().getInitParameter("UrlError01");
		logger.debug("UrlError01: " + UrlError01);		
		
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros IniciaJornada...");

		// Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);
		
		//URL
		if ( req.getParameter("url") == null ){	throw new ParametroObligatorioException("url es null");}
		paramUrl = req.getParameter("url");
		logger.debug("url: " + paramUrl);
		
		if ( req.getParameter("id_jornada") == null ){throw new ParametroObligatorioException("id_jornada es null");}
		paramIdJpicking	= req.getParameter("id_jornada");
		id_jpicking = Long.parseLong(paramIdJpicking);
		logger.debug("id_jpicking: " + paramIdJpicking);
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		JornadaDTO jor = biz.getJornadaById(id_jpicking);
		
		
		if ( jor.getId_estado() != Constantes.ID_ESTADO_JORNADA_NO_INICIADA ){
			res.sendRedirect(UrlError01);
		}
		
		
		// Inicia la jornada
		try{
			biz.setIniciaJornada(id_jpicking);
//			inicializa los pedidos relacionados al id_jpicking
			//biz.setPedidosByIdJornada(id_jpicking);

		}catch(BolException ex){
			if(ex.getMessage().equals(Constantes._EX_JPICK_ID_INVALIDO)){
				logger.debug("El id de la Jornada de Picking es Inválido");
				fp.add( "rc" , Constantes._EX_JPICK_ID_INVALIDO);
				paramUrl = UrlError + fp.forward();
			}
		}
		
		
		PedidosCtrl pedido = new PedidosCtrl();
		List lstPedidos = pedido.getPedidosByJornadaPicking(id_jpicking);
		for(int i=0; i<lstPedidos.size(); i++){
			pedido.setValidaOP(Long.parseLong(lstPedidos.get(i).toString()));
		}
		
		
		// agregamos al log de la jorndad
		try{
			biz.addLogJornadaPick(id_jpicking,usr.getLogin(),"Se ha iniciado la jornada de picking");
		}catch(BolException ex){
			if(ex.getMessage().equals(Constantes._EX_JPICK_ID_INVALIDO)){
				logger.debug("El id del Local es Inválido");
				fp.add( "rc" , Constantes._EX_JPICK_ID_INVALIDO);
				paramUrl = UrlError + fp.forward();
			}			
		}
		
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}	
	
	
}
