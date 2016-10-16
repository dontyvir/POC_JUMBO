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
import cl.bbr.jumbocl.pedidos.collaboration.ProcModTrxMPDetalleDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * comando que genera una transacción de pago y agrega un log al pedido
 * @author mleiva
 */
public class ModTrxDetalle extends Command{

	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		// 1. Variables del método
		String paramUrl	= "";
		String paramIdPedido ="";
		String paramIdTrxMp ="";
		String paramIdTrxDet[] = null;
		String paramCodBar[] = null;
		String paramCodBarOld[] = null;
		long id_pedido = -1;
		long id_trx_mp = -1;
		long id_trxdet[] = null;
		String codbar[] = null;
		String codbar_old[] = null;		
		
		
		// 1. Parámetros de inicialización servlet
		String UrlError = getServletConfig().getInitParameter("UrlError");
		logger.debug("UrlError: " + UrlError);	
		
		String MnsTrx = getServletConfig().getInitParameter("MnsTrx");		
		logger.debug("MnsTrx: " + MnsTrx);
		
		String MnsTrxOk = getServletConfig().getInitParameter("MnsTrxOk");
		logger.debug("MnsTrxOk: " + MnsTrxOk);
		
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros ModTrxDetalle...");

		//URL		
		if ( req.getParameter("url") == null ){	throw new ParametroObligatorioException("url es null");}
		paramUrl = req.getParameter("url");
		logger.debug("url: " + paramUrl);
		
		//ID_PEDIDO
		if ( req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es null");}
		paramIdPedido	= req.getParameter("id_pedido");
		logger.debug("id_pedido: " + paramIdPedido);
		id_pedido = Long.parseLong(paramIdPedido);	
		
		//ID_TRX_MP
		if ( req.getParameter("id_trx_mp") == null ){throw new ParametroObligatorioException("id_pedido es null");}
		paramIdTrxMp	= req.getParameter("id_trx_mp");
		logger.debug("id_trx_mp: " + paramIdTrxMp);
		id_trx_mp = Long.parseLong(paramIdTrxMp);
		
		//ID_TRXDET
		if ( req.getParameterValues("id_trxdet") == null ){throw new ParametroObligatorioException("id_trxdet es null");}		
		paramIdTrxDet	= req.getParameterValues("id_trxdet");
		logger.debug("paramIdTrxDet.length:"+paramIdTrxDet.length);
		id_trxdet = new long [paramIdTrxDet.length];
		for(int i=0; i<paramIdTrxDet.length; i++){
			id_trxdet[i] = Long.parseLong(paramIdTrxDet[i]);
			logger.debug("id_trxdet["+i+"]:"+id_trxdet[i]);
		}
				
		
		//CODBAR_OLD
		if ( req.getParameterValues("codbar_old") == null ){throw new ParametroObligatorioException("codbar_old es null");}
		paramCodBarOld	= req.getParameterValues("codbar_old");
		codbar_old = new String [paramCodBarOld.length];
		for(int i=0; i<paramCodBarOld.length; i++){
			codbar_old[i] = paramCodBarOld[i];
			logger.debug("codbar_old["+i+"]:"+codbar_old[i]);
		}
		
		
		//CODBAR
		if ( req.getParameterValues("codbar") == null ){throw new ParametroObligatorioException("codbar es null");}
		paramCodBar	= req.getParameterValues("codbar");
		codbar = new String [paramCodBar.length];
		for(int i=0; i<paramCodBar.length; i++){
			codbar[i] = paramCodBar[i];
			logger.debug("codbar["+i+"]:"+codbar[i]);
		}
		
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
			ProcModTrxMPDetalleDTO trx = new ProcModTrxMPDetalleDTO();
			trx.setId_pedido(id_pedido);
			trx.setId_trx_mp(id_trx_mp);
			trx.setId_trx_mp_det(id_trxdet);
			trx.setCod_barra(codbar);

			if (!biz.setModTrxPagoDet(trx)){
				logger.debug("El codigo de barras no fue aceptado :"+codbar);
				fp.add( "rc" , Constantes._EX_TRX_CODBAR_INVALIDO);
				fp.add( "mensaje_rc" , MnsTrx);
				paramUrl = UrlError + fp.forward();
				}
			else{
				fp.add( "rc" , String.valueOf(0));
				fp.add( "mensaje_rc" , MnsTrxOk);
				paramUrl = paramUrl + fp.forward();
				// agregamos al log de pedidos
				try{
					LogPedidoDTO log = new LogPedidoDTO();
					log.setId_pedido(id_pedido);
					for(int i=0; i<id_trxdet.length; i++){
						if (!codbar_old[i].equalsIgnoreCase(codbar[i].toString())){
							log.setLog("Se actualizó el detalle de TRX: "+id_trxdet[i]+ "   Código Barra: "+codbar_old[i] +" ==> "+codbar[i]);
						}
					}
					log.setUsuario(usr.getLogin());
					biz.addLogPedido(log);
				}catch(BolException ex){
					if(ex.getMessage().equals(Constantes._EX_PED_ID_INVALIDO)){
						logger.debug("El id del pedido es Inválido");
						fp.add( "rc" , Constantes._EX_PED_ID_INVALIDO);
						paramUrl = UrlError + fp.forward();
					}else{
						throw new SystemException(ex);
					}
				}				
			}			
			

		}catch(BolException ex){
			logger.debug("El id trx det es Inválido y no puede actualizar");
			throw new SystemException(ex);			
		}
		
		
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	
	}	
	
	
}
