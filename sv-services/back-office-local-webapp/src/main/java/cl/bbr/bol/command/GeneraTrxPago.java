package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.jumbocl.pedidos.dao.DAOFactory;
import cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;

/**
 * comando que genera una transacción de pago y agrega un log al pedido
 * @author mleiva
 */
public class GeneraTrxPago extends Command{


	/**
	 * 
	 */
	private static final long serialVersionUID = -2520638922536743193L;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Variables del método
		String paramUrl	= "";
		String paramIdPedido ="";
		long id_pedido = -1;
		
		
		// 1. Parámetros de inicialización servlet
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);
		String MnsTrx = getServletConfig().getInitParameter("MnsTrx");
		logger.debug("MnsTrx: " + MnsTrx);
		String MnsProd = getServletConfig().getInitParameter("MnsProd");
		logger.debug("MnsProd: " + MnsProd);
		
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros GeneraTrxPago...");

		//URL
		if ( req.getParameter("url") == null ){	throw new ParametroObligatorioException("url es null");}
		paramUrl = req.getParameter("url");
		logger.debug("url: " + paramUrl);
		
		if ( req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es null");}
		paramIdPedido	= req.getParameter("id_pedido");
		id_pedido = Long.parseLong(paramIdPedido);
		logger.debug("id_pedido: " + paramIdPedido);
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		
		/*
		JornadaDTO jor = biz.getJornadaById(id_pedido);
				
		if ( jor.getId_estado() != Constantes.ID_ESTADO_JORNADA_NO_INICIADA ){
			res.sendRedirect(UrlError01);
		}*/
				
		// Inicia la jornada
		try{
			biz.doGeneraTrxMp(id_pedido);
			paramUrl = "ViewDetallePedido?id_pedido=" +id_pedido+"&mensaje_rc=Se ha generado la TRX de pago.&rc=_";
			
			try{
				LogPedidoDTO log = new LogPedidoDTO();
				log.setId_pedido(id_pedido);
				log.setLog("[BOL] Se han generado las trx. de pago");
				log.setUsuario(usr.getLogin());
				biz.addLogPedido(log);
			}catch(BolException ex){
				logger.error("Error al guardar log del pediddo",ex);			
			}

		}catch(BolException ex){
			fp.add( "id_pedido" , String.valueOf(id_pedido));
			if ( ex.getMessage().equals(Constantes._EX_OPE_ID_NO_EXISTE)) {
				logger.debug("El id del pedido es Inválido");				
				fp.add( "rc" , Constantes._EX_OPE_ID_NO_EXISTE);
				fp.add( "mensaje_rc" , MnsTrx);
				paramUrl = UrlError + fp.forward();
                
			} else if ( ex.getMessage().equals(Constantes._EX_TRX_PRODS_INVALIDOS)) {
				logger.debug("El producto tiene datos inválidos");
				fp.add( "rc" , Constantes._EX_TRX_PRODS_INVALIDOS);
				fp.add( "mensaje_rc" , MnsProd);
				paramUrl = UrlError + fp.forward();
                
			} else if ( ex.getMessage().equals(Constantes._EX_TRX_PROD_SIN_UM)) {
				logger.debug("Un producto del pedido no tiene unidad de medida");
				fp.add( "rc" , Constantes._EX_TRX_PROD_SIN_UM);
				fp.add( "mensaje_rc" , MnsProd);
				paramUrl = UrlError + fp.forward();
                
			} else if ((ex.getMessage().equals(Constantes._EX_TRX_NO_INSERTA_ENC))|| (ex.getMessage().equals(Constantes._EX_TRX_NO_INSERTA_DET))) {
                logger.debug("No puede generar las transacciones");
				fp.add( "rc" , Constantes._EX_TRX_NO_INSERTA_ENC);
				fp.add( "mensaje_rc" , MnsTrx);
				paramUrl = UrlError + fp.forward();
                
			} else if (ex.getMessage().equals(Constantes._EX_TRX_DUPLICADA)) {
                logger.debug("clave duplicada");
				fp.add( "rc" , Constantes._EX_TRX_DUPLICADA);
				fp.add( "mensaje_rc" , MnsTrx);
				paramUrl = UrlError + fp.forward();
				
			} else if(ex.getMessage().equals(Constantes._EX_OPE_MONTO_PICK_MAYOR)) {
				try{
					LogPedidoDTO log = new LogPedidoDTO();
					log.setId_pedido(id_pedido);
					log.setLog("[BOL] No es posible generar TRX por exceso en OP");
					log.setUsuario(usr.getLogin());
					biz.addLogPedido(log);
				}catch(BolException e){
					logger.error("Error al guardar log del pediddo",e);			
				}
				
                fp.add( "rc" , Constantes._EX_OPE_MONTO_PICK_MAYOR);
                fp.add( "mensaje_rc" , "El monto pickeado excede el monto reservado");
                paramUrl = UrlError + fp.forward();
                
			} else if(ex.getMessage().equals(Constantes._EX_OPE_MONTO_MXN)) {
				try{
					LogPedidoDTO log = new LogPedidoDTO();
					log.setId_pedido(id_pedido);
					log.setLog("[BOL] No es posible generar TRX por precios igual a $1 MxN");
					log.setUsuario(usr.getLogin());
					biz.addLogPedido(log);
				}catch(BolException e){
					logger.error("Error al guardar log del pediddo",e);			
				}
				
                fp.add( "rc" , Constantes._EX_OPE_MONTO_PICK_MAYOR);
                fp.add( "mensaje_rc" , "El precio no puerde ser $1z MxN");
                paramUrl = UrlError + fp.forward();
                
            } else{
				throw new SystemException(ex);
			}
		}
		
		
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}	
	
	
}
