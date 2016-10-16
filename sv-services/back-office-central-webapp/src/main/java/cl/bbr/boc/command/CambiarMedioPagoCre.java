package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.DatosMedioPagoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Cambia el estado a un despacho y agrega al log de despacho
 * @author jsepulveda
 */
public class CambiarMedioPagoCre extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		String paramId_pedido= "";
		String medio_pago    = "";
		String num_mp        = "";
		String banco         = "";
		String exp_mes       = "";
		String exp_ano       = "";
		String MesesLibrePago= "";
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
		boolean cambiarMedioPago = true;
		// 1. Variables del método
		String paramUrl	= "";
		// Recupera pagina desde web.xml
		String msjeOK = getServletConfig().getInitParameter("msjeOK");
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

	
		medio_pago = "CRE";
		logger.debug("medio pago: " + medio_pago);
		
		// 3. Procesamiento Principal
		BizDelegate biz = new BizDelegate();
		DatosMedioPagoDTO mp = new DatosMedioPagoDTO();
		
		mp.setId_pedido(id_pedido);
		mp.setMedio_pago(medio_pago);
		mp.setNum_mp(num_mp);
		mp.setCuotas(cuotas);
		mp.setBanco(banco);
		mp.setFecha_expiracion(exp_mes + exp_ano);
		mp.setUsr_login(usr.getLogin());
		mp.setNom_tban("CREDITO");
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
		
		// 4. Redirecciona salida
		res.sendRedirect(paramUrl);
	}		
}
