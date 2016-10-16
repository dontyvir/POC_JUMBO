package cl.bbr.boc.command;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class CorreccionExcesosBOC extends Command {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8057929260743251343L;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception  {
		
		try {			
			if(!"XMLHttpRequest".equals(req.getHeader("X-Requested-With")))
				res.sendRedirect("/JumboBOCentral/ViewMonOP");
			
			HttpSession session = req.getSession();
			res.setContentType("application/json");
			PrintWriter out = res.getWriter(); 			
			JSONObject oJsonResponse = new JSONObject();
			
			if(req.getParameter("id_pedido") == null || usr == null){
				oJsonResponse.put("status", "500");//The request is for something forbidden. Authorization will not help.))
				out.println(oJsonResponse.toString());
				return;	 
			}
	        
	        long idPedido = Long.parseLong(req.getParameter("id_pedido"));
			BizDelegate biz = new BizDelegate();	
			
			if(!biz.isActivaCorreccionAutomatica() || !biz.isOpConExceso(idPedido) || biz.isExcesoCorreccionAutomatico(idPedido) ){	
				oJsonResponse.put("status", "500");//The request is for something forbidden. Authorization will not help.))
				out.println(oJsonResponse.toString());
				return;
			}
			
			String accion = req.getParameter("accion");	
			
			//Accion eliminar exceso....
			if("AP".equals(accion)){
				
				long idDetalle = Long.parseLong(req.getParameter("idDetalle"));
				boolean isUpdate = biz.updatePrecioPickXPrecioSegunCantidad(idDetalle, idPedido);
				if(isUpdate){//EXCESO CORREGIDO :)
					
					try{
						LogPedidoDTO log = new LogPedidoDTO();
						log.setId_pedido(idPedido);
						log.setLog("[BOC-EXCESO] OK -> Ajustar Precio Segun Cantidad, corregido por ejecutivo, DETALLE_PEDIDO: "+idDetalle);
						log.setUsuario(usr.getLogin());
						biz.addLogPedido(log);
					}catch(BocException e){
						logger.error("Error al guardar log del pediddo",e);			
					}
					oJsonResponse.put("status", "200");//The request was fulfilled.
					
				}else{//EXCESO NO CORREGIDO :(
					
					try{
						LogPedidoDTO log = new LogPedidoDTO();
						log.setId_pedido(idPedido);
						log.setLog("[BOC-EXCESO] ERROR -> Ajustar Precio Segun Cantidad, exceso no puede ser corregido por ejecutivo, DETALLE_PEDIDO: "+idDetalle);
						log.setUsuario(usr.getLogin());
						biz.addLogPedido(log);
					}catch(BocException e){
						logger.error("Error al guardar log del pediddo",e);			
					}		
		 
		        	oJsonResponse.put("status", "500");//Unexpected condition which prevented it from fulfilling the request.
				}			
			}else if("PS".equals(accion)){
				oJsonResponse.put("status", "200");
				long idDetalle = Long.parseLong(req.getParameter("idDetalle"));
				boolean isUpdate = biz.CorregirPrecioSegúnPrecioSolicitado(idDetalle, idPedido);
				if(isUpdate){//EXCESO CORREGIDO :)
					
					try{
						LogPedidoDTO log = new LogPedidoDTO();
						log.setId_pedido(idPedido);
						log.setLog("[BOC-EXCESO] OK -> Ajustar A Precio Solicitado, corregido por ejecutivo, DETALLE_PEDIDO: "+idDetalle);
						log.setUsuario(usr.getLogin());
						biz.addLogPedido(log);
					}catch(BocException e){
						logger.error("Error al guardar log del pediddo",e);			
					}
					oJsonResponse.put("status", "200");//The request was fulfilled.
					
				}else{//EXCESO NO CORREGIDO :(
					
					try{
						LogPedidoDTO log = new LogPedidoDTO();
						log.setId_pedido(idPedido);
						log.setLog("[BOC-EXCESO] ERROR -> Ajustar A Precio Solicitado, exceso no puede ser corregido por ejecutivo, DETALLE_PEDIDO: "+idDetalle);
						log.setUsuario(usr.getLogin());
						biz.addLogPedido(log);
					}catch(BocException e){
						logger.error("Error al guardar log del pediddo",e);			
					}		
		 
		        	oJsonResponse.put("status", "500");//Unexpected condition which prevented it from fulfilling the request.
				}
				
			}else if("EPK".equals(accion)){
				long idDetalle = Long.parseLong(req.getParameter("idDetalle"));
				long IdDpicking = Long.parseLong(req.getParameter("IdDpicking"));
				
				boolean isUpdate = biz.deletePickingByIdDpicking(idPedido, IdDpicking);
				if(isUpdate){//EXCESO CORREGIDO :)
					
					try{
						LogPedidoDTO log = new LogPedidoDTO();
						log.setId_pedido(idPedido);
						log.setLog("[BOC-EXCESO] OK -> Eliminar Picking Seleccionado, corregido por ejecutivo, DETALLE_PEDIDO: "+idDetalle);
						log.setUsuario(usr.getLogin());
						biz.addLogPedido(log);
					}catch(BocException e){
						logger.error("Error al guardar log del pediddo",e);			
					}
					oJsonResponse.put("status", "200");//The request was fulfilled.
					
				}else{//EXCESO NO CORREGIDO :(
					
					try{
						LogPedidoDTO log = new LogPedidoDTO();
						log.setId_pedido(idPedido);
						log.setLog("[BOC-EXCESO] ERROR -> Eliminar Picking Seleccionado, exceso no puede ser corregido por ejecutivo, DETALLE_PEDIDO: "+idDetalle);
						log.setUsuario(usr.getLogin());
						biz.addLogPedido(log);
					}catch(BocException e){
						logger.error("Error al guardar log del pediddo",e);			
					}		
		 
		        	oJsonResponse.put("status", "500");//Unexpected condition which prevented it from fulfilling the request.
				}	
			}else if("CS".equals(accion)){
				long idDetalle  = Long.parseLong(req.getParameter("idDetalle"));
				
				boolean isUpdate = biz.ajustarCantidadSolicitada(idPedido, idDetalle);
				if(isUpdate){//EXCESO CORREGIDO :)
					
					try{
						LogPedidoDTO log = new LogPedidoDTO();
						log.setId_pedido(idPedido);
						log.setLog("[BOC-EXCESO] OK -> Ajusta Cantidad Solicitada, corregido por ejecutivo, DETALLE_PEDIDO: "+idDetalle);
						log.setUsuario(usr.getLogin());
						biz.addLogPedido(log);
					}catch(BocException e){
						logger.error("Error al guardar log del pediddo",e);			
					}
					oJsonResponse.put("status", "200");//The request was fulfilled.
					
				}else{//EXCESO NO CORREGIDO :(
					
					try{
						LogPedidoDTO log = new LogPedidoDTO();
						log.setId_pedido(idPedido);
						log.setLog("[BOC-EXCESO] ERROR -> Ajusta Cantidad Solicitada, exceso no puede ser corregido por ejecutivo, DETALLE_PEDIDO: "+idDetalle);
						log.setUsuario(usr.getLogin());
						biz.addLogPedido(log);
					}catch(BocException e){
						logger.error("Error al guardar log del pediddo",e);			
					}		
		 
		        	oJsonResponse.put("status", "500");//Unexpected condition which prevented it from fulfilling the request.
				}	
			}else{
				oJsonResponse.put("status", "500");//Unexpected condition which prevented it from fulfilling the request.
			}		
			
			out.println(oJsonResponse.toString());
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}	
	}

}
