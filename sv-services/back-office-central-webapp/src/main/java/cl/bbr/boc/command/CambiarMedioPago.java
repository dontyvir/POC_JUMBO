package cl.bbr.boc.command;

//import java.util.List;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.ctrl.PedidosCtrl;
import cl.bbr.jumbocl.pedidos.dao.DAOFactory;
import cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO;
import cl.bbr.jumbocl.pedidos.dto.AlertaDTO;
import cl.bbr.jumbocl.pedidos.dto.DatosMedioPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Cambia el estado a un despacho y agrega al log de despacho
 * @author jsepulveda
 */
public class CambiarMedioPago extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		String paramId_pedido= "";
		String medio_pago    = "";
		String num_mp        = "";
		String banco         = "";
		String exp_mes       = "";
		String exp_ano       = "";
		String nom_tban      = "";
		String cuotas        = "";
		long   id_pedido     = -1;
		String rut_tit       = "";
		String dv_tit        = "";
		String nombre_tit    = "";
		String appaterno_tit = "";
		String apmaterno_tit = "";
		String direccion_tit = "";
		String numero_tit    = "";
		String Titular       = "";
		// 1. Variables del método
		String paramUrl	= "";
		// Recupera pagina desde web.xml
		String msjeOK = getServletConfig().getInitParameter("msjeOK");
		String msjeTAR = getServletConfig().getInitParameter("msjeTAR");
		String msjeMAL = getServletConfig().getInitParameter("msjeMAL");
		String msjeMPago = "";
		
		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		
		if ( req.getParameter("url") == null ){ throw new ParametroObligatorioException("url es null");	}
		paramUrl = req.getParameter("url");
		logger.debug("url: " + paramUrl);
		
		if ( req.getParameter("id_pedido") == null ){ throw new ParametroObligatorioException("id_pedido es null"); }
		paramId_pedido = req.getParameter("id_pedido");
		id_pedido = Long.parseLong(paramId_pedido);
		logger.debug("id_pedido: " + paramId_pedido);

		/*if ( req.getParameter("rut_titular") == null ){ throw new ParametroObligatorioException("rut_titular es null"); }
		paramRut = req.getParameter("rut_titular");		
		logger.debug("rut titular: " + paramRut);*/
		
		// Tipo de medio de pago
		if ( req.getParameter("mpago") == null ){ throw new ParametroObligatorioException("mpago es null"); }
		
		// JBM: Jumbo Más
		// TBK: Transbank
		medio_pago = req.getParameter("mpago");
		logger.debug("medio pago: " + medio_pago);
		
		
		// en caso de ser tarjeta bancaria TBK
		if ( medio_pago.equals(Constantes.MEDIO_PAGO_TBK) ){
			num_mp 	 = req.getParameter("num_mp");
			banco 	 = req.getParameter("t_banco");
			exp_mes  = req.getParameter("exp_mes");
			exp_ano  = req.getParameter("exp_ano");
			nom_tban = (req.getParameter("nom_tban")==null?"":req.getParameter("nom_tban"));
			cuotas   = req.getParameter("t_cuotas");
			
			if (!num_mp.trim().equals("")){
				logger.debug("num_mp: " + "************"+num_mp.substring(num_mp.length()-4,num_mp.length()));
			}else{
				logger.debug("num_mp: '' -- No se modifico el Número de la Tarjeta");
			}
			logger.debug("banco: " + banco);
			//logger.debug("exp_mes: " + exp_mes);
			//logger.debug("exp_ano: " + exp_ano);	
			logger.debug("t_cuotas: " + cuotas);
		}

		PedidosCtrl pedCtrl = new PedidosCtrl();
		//en caso de ser tarjeta jumbo JBM
		if ( medio_pago.equals(Constantes.MEDIO_PAGO_CAT) ){
			
			if ( req.getParameter("num_mp_jumbo_1") == null ){ throw new ParametroObligatorioException("num_mp_jumbo_1 es null");}
			if ( req.getParameter("num_mp_jumbo_2") == null ){ throw new ParametroObligatorioException("num_mp_jumbo_2 es null");}
			if ( req.getParameter("num_mp_jumbo_3") == null ){ throw new ParametroObligatorioException("num_mp_jumbo_3 es null");}
			if ( req.getParameter("num_mp_jumbo_4") == null ){ throw new ParametroObligatorioException("num_mp_jumbo_4 es null");}
			if ( req.getParameter("j_cuotas") 		== null ){ throw new ParametroObligatorioException("j_cuotas es null");}

			num_mp  = req.getParameter("num_mp_jumbo_1");
			num_mp += req.getParameter("num_mp_jumbo_2");
			num_mp += req.getParameter("num_mp_jumbo_3");
			num_mp += req.getParameter("num_mp_jumbo_4");
			
			if (num_mp != null && !num_mp.equals("")){
				//Bean utilizado 615290 (Jumbo Más), 61528031 (Más Paris) y 61529025 (Más Easy)
	            if (num_mp.substring(0,8).equals("61529025")){
	                nom_tban = "MAS EASY";
	            }else if(num_mp.substring(0,6).equals("615290")){
	                nom_tban = "MAS JUMBO";
	            }else if (num_mp.substring(0,8).equals("61528031")){
	                nom_tban = "MAS PARIS";
	            }else if (isMPagoAlternativo(num_mp)){
	                nom_tban = "CHEQUE";
	            }
			}

			//exp_mes = "00"; No existe fecha de expiración para las Tarjetas CAT
			//exp_ano = "00"; por lo cual se asigna NULL al momento de almacenarla.
			cuotas = req.getParameter("j_cuotas");

			if (!num_mp.trim().equals("")){
				logger.debug("num_mp: " + "************"+num_mp.substring(num_mp.length()-4,num_mp.length()));
			}else{
				logger.debug("num_mp: '' -- No se modifico el Número de la Tarjeta");
			}
			logger.debug("j_cuotas: " + cuotas);
			
			// SE EXCLUYE DE LA VALIDACIÓN DEL MEDIO DE PAGO:
			//    1.- CUANDO NO ES MODIFICADO EL NUMERO DE LA TARJETA
			//    2.- CUANDO EL MEDIO DE PAGO ES ALTERNATIVO (CAT => 1111111111111111)
			if (!num_mp.equals("") && !isMPagoAlternativo(num_mp)){
			   	//(J)nuevo
			   	PedidoDTO pedido = pedCtrl.getPedidoById(id_pedido);
			   	JdbcPedidosDAO daoPedidos = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
			   	pedido.setNum_mp_unmask(num_mp);
			   	pedCtrl.validaSaldoYBloqueo(pedido, daoPedidos);
			   	//(J)fin nuevo 
			   
				//pedCtrl.validaMPagoCAT(id_pedido, num_mp);
				
				List lstAlertas = pedCtrl.getAlertasPedido(id_pedido);
				for(int i=0; i<lstAlertas.size(); i++){
					AlertaDTO alerta = (AlertaDTO)lstAlertas.get(i);
					int idAlerta = (int)alerta.getId_alerta();
					if (idAlerta > 15 && idAlerta < 25){
						msjeMPago += alerta.getDescripcion() + "<br>";
					}
				}
			}
			
			if (isMPagoAlternativo(num_mp)){
				pedCtrl.elimAlertaByPedidoFromMPago(id_pedido);
			}else{
				if (!msjeMPago.equals("")){
					msjeMPago = "El medio de pago fue modificado, pero presenta las siguientes Alertas:<br>" + msjeMPago;	
				}

			}
		}		
		
		//en caso de ser TARJETA PARIS
		
		if ( medio_pago.equals(Constantes.MEDIO_PAGO_PARIS) ){
            num_mp  = req.getParameter("num_mp_paris");
            Titular = (req.getParameter("titular")==null?"":req.getParameter("titular"));
            if (Titular.equals("1")){
            	nom_tban = "PARIS TITULAR";	
            }else if (Titular.equals("0")){
            	nom_tban = "PARIS ADICIONAL";
    			rut_tit       = req.getParameter("rut_tit");
    			dv_tit        = req.getParameter("dv_tit");
    			nombre_tit    = req.getParameter("nombre_tit");
    			appaterno_tit = req.getParameter("appaterno_tit");
    			apmaterno_tit = req.getParameter("apmaterno_tit");
    			direccion_tit = req.getParameter("direccion_tit");
    			numero_tit    = req.getParameter("numero_tit");
            }
			
			//exp_mes = "00"; No existe fecha de expiración para las Tarjetas PARIS
			//exp_ano = "00"; por lo cual se asigna NULL al momento de almacenarla.
			cuotas = req.getParameter("cuotas_tp");
			//MesesLibrePago = req.getParameter("meses_libre_pago_tp");

			if (!num_mp.trim().equals("")){
				logger.debug("num_mp: " + "************"+num_mp.substring(num_mp.length()-4,num_mp.length()));
			}else{
				logger.debug("num_mp: '' -- No se modifico el Número de la Tarjeta");
			}
			logger.debug("cuotas_tp: " + cuotas);

			if (!num_mp.equals("")){
			   
			   //(J)nuevo
			   	PedidoDTO pedido = pedCtrl.getPedidoById(id_pedido);
			   	JdbcPedidosDAO daoPedidos = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();
			   	pedido.setNum_mp_unmask(num_mp);
			   	pedCtrl.validaSaldoYBloqueo(pedido, daoPedidos);
			   	//(J)fin nuevo 
				//pedCtrl.validaMPagoPAR(id_pedido, num_mp, cuotas, nom_tban, rut_tit, dv_tit);
				
				List lstAlertas = pedCtrl.getAlertasPedido(id_pedido);
				
				for(int i=0; i<lstAlertas.size(); i++){
					AlertaDTO alerta = (AlertaDTO)lstAlertas.get(i);
					int idAlerta = (int)alerta.getId_alerta();
					if (idAlerta > 15 && idAlerta < 25){
						msjeMPago += alerta.getDescripcion() + "<br>";
					}
				}
				if (!msjeMPago.equals("")){
					msjeMPago = "El medio de pago fue modificado, pero presenta las siguientes Alertas:<br>" + msjeMPago;
				}
			}
		}
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		DatosMedioPagoDTO mp = new DatosMedioPagoDTO();
		
		if(num_mp != ""){
			mp.setId_pedido(id_pedido);
			mp.setMedio_pago(medio_pago);
			mp.setNum_mp(num_mp);
			mp.setCuotas(cuotas);
			mp.setBanco(banco);
			mp.setFecha_expiracion(exp_mes + exp_ano);
			//mp.setMeses_LibrePago(MesesLibrePago);
			mp.setUsr_login(usr.getLogin());
			mp.setNom_tban(nom_tban);
			mp.setRut_tit(rut_tit);
			mp.setDv_tit(dv_tit);
			mp.setNombre_tit(nombre_tit);
			mp.setAppaterno_tit(appaterno_tit);
			mp.setApmaterno_tit(apmaterno_tit);
			mp.setDireccion_tit(direccion_tit);
			mp.setNumero_tit(numero_tit);
			try{
				biz.setCambiarMedio_pago(mp);
				if (!msjeMPago.equals("")){
					paramUrl +="&mensaje="+msjeMPago;
				}else{
					paramUrl +="&mensaje="+msjeOK;
				}
			}catch(BocException e){
				paramUrl += "&mensaje="+msjeMAL;
				logger.debug("URL DENTRO DEL CATCH : "+paramUrl);
			}
		}else
			paramUrl += "&mensaje="+msjeTAR;
		
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	}		
	
	public boolean isMPagoAlternativo(String num_mp){
		return (num_mp.equals("1111111111111111"));
	}
}
