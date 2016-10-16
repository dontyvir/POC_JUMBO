package cl.bbr.bol.command;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

public class CorreccionExcesos extends Command {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception  {
	
		try {
			
			if(!"XMLHttpRequest".equals(req.getHeader("X-Requested-With")))
				res.sendRedirect("/JumboBOLocal/");
					
			HttpSession session = req.getSession();
			res.setContentType("application/json");
			PrintWriter out = res.getWriter(); 
			
			JSONObject oJsonResponse = new JSONObject();
			
			BizDelegate biz = new BizDelegate();	
			
			if(!biz.isActivaCorreccionAutomatica()){	
				oJsonResponse.put("status", "403");//The request is for something forbidden. Authorization will not help.))
				out.println(oJsonResponse.toString());
				return;
			}
		
			String accion = req.getParameter("accion");		
			
			//Accion eliminar exceso....
			if("c".equals(accion) && session.getAttribute("EXCESO_CORRECCION_AUTOMATICA") != null){
				long idPedido = Long.parseLong(session.getAttribute("EXCESO_CORRECCION_AUTOMATICA").toString());
				
				if(biz.corrigeExcesoOP(idPedido)){//EXCESO CORREGIDO :)
					try{
						LogPedidoDTO log = new LogPedidoDTO();
						log.setId_pedido(idPedido);
						log.setLog("[BOL] OP con exceso corregido automaticamente.");
						log.setUsuario(usr.getLogin());
						biz.addLogPedido(log);
					}catch(BolException e){
						logger.error("Error al guardar log del pediddo",e);			
					}
					session.setAttribute("EXCESO_CORRECCION_AUTOMATICA",null);
					session.removeAttribute("EXCESO_CORRECCION_AUTOMATICA");
					oJsonResponse.put("status", "200");//The request was fulfilled.
					
				}else{//EXCESO NO CORREGIDO :(
					
					try{
						LogPedidoDTO log = new LogPedidoDTO();
						log.setId_pedido(idPedido);
						log.setLog("[BOL] Error al corregir exceso automaticamente.");
						log.setUsuario(usr.getLogin());
						biz.addLogPedido(log);
					}catch(BolException e){
						logger.error("Error al guardar log del pediddo",e);			
					}					
		 
		        	List listprdSolicitados = biz.getIdDetalleSolicitadoConExceso(idPedido);
		        	Iterator itIdDetalle =listprdSolicitados.iterator();
		        	String  idDetalle = "";
		        	while(itIdDetalle.hasNext()){		        		
		        		idDetalle += (String) itIdDetalle.next()+" <br> ";
		        	}
		        	
		        	ParametroDTO oParam = biz.getParametroByName("EMAIL_CORRECCION_EXCESO");
		        	JSONObject oJson = (JSONObject) JSONSerializer.toJSON(oParam.getValor());
		        	Iterator it= oJson.keys();
		        	
		        	while( it.hasNext() ){
						String key = (String)it.next();	               
			                          
						String strSubject = "[Error OP con Exceso] "+idPedido;
						String strMail= key+", <br> <br>"+ "La OP <b>"+idPedido+"</b> se encuentra con exceso en los siguientes productos: <br> <br> ID_DETALLE_PEDIDO <br> " +idDetalle;
						String strFrom=oJson.getString(key);
						String strRemitente=usr.getEmail();
						
						MailDTO mail = new MailDTO();
						mail.setFsm_subject(strSubject);
						mail.setFsm_data(strMail);                
						mail.setFsm_destina(strFrom);
						mail.setFsm_remite(strRemitente);
						biz.addMail(mail);
		           }
		        	oJsonResponse.put("status", "500");//Unexpected condition which prevented it from fulfilling the request.
				}			
			}
			//Accion notificar exceso (enviar email)
			else if("n".equals(accion) && session.getAttribute("EXCESO_NOTIFICAR") != null){
				
				long idPedido = Long.parseLong(session.getAttribute("EXCESO_NOTIFICAR").toString());
				
				List listprdSolicitados = biz.getIdDetalleSolicitadoConExceso(idPedido);
	        	Iterator itIdDetalle =listprdSolicitados.iterator();
	        	String  idDetalle = "";
	        	while(itIdDetalle.hasNext()){		        		
	        		idDetalle += (String) itIdDetalle.next()+" <br> ";
	        	}
	        	
				//Inicio mail
	        	ParametroDTO oParam = biz.getParametroByName("EMAIL_CORRECCION_EXCESO");
	        	JSONObject oJson = (JSONObject) JSONSerializer.toJSON(oParam.getValor());
	        	Iterator it= oJson.keys();
	 
				while (it.hasNext()) {
					String key = (String) it.next();
	
					String strSubject = "[OP con Exceso] " + idPedido;
					String strMail= key+", <br> <br> "+ "La OP <b>"+idPedido+"</b> mantiene excesos que no son posible corregir automaticamente, los productos con exceso son los siguientes: <br> <br> ID_DETALLE_PEDIDO <br> " +idDetalle;
					String strFrom = oJson.getString(key);
					String strRemitente = usr.getEmail();
	
					MailDTO mail = new MailDTO();
					mail.setFsm_subject(strSubject);
					mail.setFsm_data(strMail);
					mail.setFsm_destina(strFrom);
					mail.setFsm_remite(strRemitente);
					biz.addMail(mail);
				}
				oJsonResponse.put("status", "202");//The request has been accepted for processing
				session.setAttribute("EXCESO_NOTIFICAR",null);
				session.removeAttribute("EXCESO_NOTIFICAR");
	            //Fin mail	
			}else if("mxn".equals(accion) && session.getAttribute("ERROR_PROMOCION_MXN") != null){
				
				long idPedido = Long.parseLong(session.getAttribute("ERROR_PROMOCION_MXN").toString());
				String  idDetalle = "";
				String  codigoPro = "";
				Map mapa = biz.getOPConProductosFaltantesEnPromoMxN(idPedido);
				Iterator ite = mapa.keySet().iterator();
				if(ite!=null){
					while(ite.hasNext()){
						String keyPartyId = (String) ite.next();
						List listprdSolicitados = (List) mapa.get(keyPartyId);
						
						//List listprdSolicitados = biz.getOPConProductosFaltantesEnPromoMxN(idPedido); ///
			        	Iterator itIdDetalle =listprdSolicitados.iterator();
			        	
			        	//prod.setId_pedido(rs.getLong("ID_PEDIDO"));
			        	////prod.setPrecio(rs.getDouble("dp.PRECIO"));
			        	////prod.setCodigoPromocion(rs.getInt("PROMO_CODIGO"));
			        	
			        	while(itIdDetalle.hasNext()){	
			        		ProductosPedidoDTO pro = (ProductosPedidoDTO)itIdDetalle.next();			        					        		
			        		idDetalle += String.valueOf( pro.getId_detalle() )+" <br> ";
			        		codigoPro  += String.valueOf( pro.getCodigoPromocion() )+" <br> ";
			        	}			        							
					}
					//Inicio mail
		        	ParametroDTO oParam = biz.getParametroByName("EMAIL_CORRECCION_EXCESO");
		        	JSONObject oJson = (JSONObject) JSONSerializer.toJSON(oParam.getValor());
		        	Iterator it= oJson.keys();
		        	
					while (it.hasNext()) {
						String key = (String) it.next();
		
						String strSubject = "[OP - Precios con Monto de $1 - MxN] " + idPedido;
						String strMail= key+", <br> <br> "+ "La OP <b>"+idPedido+"</b>, contiene productos con un precio de $1, que no son posible corregir automaticamente," +
								" Detalle de pedidos son los siguientes: <br> <br> " +
								"<table  border=0><tr><td width=207>ID DETALLE PEDIDO</td>" +
								"<td width=231>CODIGO PROMOCION</td></tr>" +
								"<tr><td>"+idDetalle+"</td><td>"+codigoPro+"</td></tr></table>";

						String strFrom = oJson.getString(key);
						String strRemitente = usr.getEmail();
		
						MailDTO mail = new MailDTO();
						mail.setFsm_subject(strSubject);
						mail.setFsm_data(strMail);
						mail.setFsm_destina(strFrom);
						mail.setFsm_remite(strRemitente);
						biz.addMail(mail);
					}
					oJsonResponse.put("status", "202");//The request has been accepted for processing
					session.setAttribute("ERROR_PROMOCION_MXN",null);
					session.removeAttribute("ERROR_PROMOCION_MXN");	
				}
												
			}else{
				oJsonResponse.put("status", "204");//no information to send back
			}
		
			out.println(oJsonResponse.toString());
			
		} catch (Exception e) {
			logger.error("Error: ", e);
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}	
	}

}
