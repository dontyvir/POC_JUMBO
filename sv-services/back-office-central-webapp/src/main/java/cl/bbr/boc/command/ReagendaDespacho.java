package cl.bbr.boc.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.LogRutaDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Reagenda un despacho 
 * @author jsepulveda
 */
public class ReagendaDespacho extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        		
	    // 1. seteo de Variables del método
	     String paramUrl 			= "";
	     String paramId_pedido		= "";
	     String paramId_jdespacho	= "";
	     String paramOtroprecio		= "";
	     String paramPrecio			= "";
	     long id_pedido		= -1;
	     long id_jdespacho	= -1;
	     int  precio		= -1;
	     boolean sobrescribeprecio	= false;
	     
	     // 2. Procesa parámetros del request

	     logger.debug("Procesando parámetros...");

	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if ( req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es nulo");}
	     //if ( req.getParameter("id_jdespacho") == null ){throw new ParametroObligatorioException("id_jdespacho es nulo");}
	     if ( req.getParameter("otroprecio") == null ){throw new ParametroObligatorioException("otroprecio es nulo");}
         
         int origen = 0;
         long idRuta = 0;
        if (req.getParameter("origen") != null ) {
            try {
                origen = Integer.parseInt(req.getParameter("origen"));    
            } catch (Exception e) {}            
        }
        if ( req.getParameter("id_ruta") != null ) {
            try {
                idRuta = Long.parseLong(req.getParameter("id_ruta"));    
            } catch (Exception e) {}            
        }
        
	     // 2.2 obtiene parametros desde el request
	     paramUrl 			= req.getParameter("url");
	     paramId_pedido		= req.getParameter("id_pedido");
	     paramId_jdespacho 	= req.getParameter("id_jdespacho");
	     paramOtroprecio	= req.getParameter("otroprecio");
	     paramPrecio		= req.getParameter("precio");
	     
	          
	     id_pedido 		= Long.parseLong(paramId_pedido);
	     if(paramId_jdespacho!=null)
	    	 id_jdespacho 	= Long.parseLong(paramId_jdespacho);
	     
	     precio	= Integer.parseInt(paramPrecio);
	     if ( paramOtroprecio.equals("1") ){
	    	 sobrescribeprecio	= true;
	     }
	     else if ( paramOtroprecio.equals("0") ){
	    	 sobrescribeprecio	= false;
	     }
	     
	     
	     // 2.3 log de parametros y valores
	     logger.debug("url : " 			+ paramUrl);
	     logger.debug("id_pedido : " 	+ paramId_pedido);
	     logger.debug("id_jdespacho : " + paramId_jdespacho);
	     logger.debug("Otro precio : " 	+ paramOtroprecio);
	     logger.debug("Precio : " 		+ paramPrecio);
     
	     ForwardParameters fp = new ForwardParameters();
		 fp.add( req.getParameterMap() );
	     
	     /*
	      * 3. Procesamiento Principal
	      */

	     BizDelegate biz = new BizDelegate();
	     
	     try {
             String msg = "Jornada reagendada satisfactoriamente";
             
             PedidoDTO ped = biz.getPedidosById(id_pedido);
             long idJornadaDespachoAnterior = ped.getId_jdespacho();
             
             boolean modificarJPicking = false;
             if ( Utils.considerarPickingParaReprogramar( ped.getId_estado() )) {
                 modificarJPicking = true;
             }
             boolean modificarPrecio = false;
             if ( Utils.considerarPreciosParaReprogramar( ped.getId_estado() )) {
                 modificarPrecio = true;
             }
             biz.doReagendaDespacho(id_pedido, id_jdespacho, sobrescribeprecio, precio, usr.getLogin(), modificarJPicking, modificarPrecio );
             
             if ( ped.getPedidoExt() != null && ped.getPedidoExt().getIdRuta() != 0 ) {
                 
                 RutaDTO ruta = biz.getRutaById(ped.getPedidoExt().getIdRuta());
                 
                 LogRutaDTO log1 = new LogRutaDTO(ruta.getIdRuta(), ruta.getEstado(), usr, "[BOC] OP " + id_pedido + " reagendado.");
                 biz.addLogRuta( log1 );
                 
                 //Sacamos el pedido de la ruta
                 if ( biz.delPedidoRuta(id_pedido) == 1 ) {
                     LogPedidoDTO log2 = new LogPedidoDTO(id_pedido, usr.getLogin(), "[BOC] Pedido " + id_pedido + " eliminado de la Ruta: " + ruta.getIdRuta());
                     biz.addLogPedido(log2);
                 } 
                 //Actualizamos la cantidad de bins
                 biz.actualizaCantBinsRuta(ruta.getIdRuta());
                 
                 //Si es el ultimo pedido de la ruta, dejamos la ruta anulada
                 List peds = biz.getPedidosByRuta(ruta.getIdRuta());
                 if ( peds.size() == 0 ) {
                     biz.setEstadoRuta(Constantes.ESTADO_RUTA_ANULADA, ruta.getIdRuta());                     
                     LogRutaDTO log3 = new LogRutaDTO(ruta.getIdRuta(), ruta.getEstado(), usr, "[BOC] La ruta queda anulada ");
                     biz.addLogRuta( log3 );
                     msg = "Pedido reprogramado y ruta anulada";
                 } else {
                     boolean todosFinalizados = true;
                     for (int i=0; i < peds.size(); i++) {
                         PedidoDTO p = (PedidoDTO) peds.get(i);
                             if ( p.getId_estado() != Constantes.ID_ESTAD_PEDIDO_FINALIZADO ) {
                                 todosFinalizados = false;
                             }
                     }
                     if ( todosFinalizados ) {
                         biz.setEstadoRuta(Constantes.ESTADO_RUTA_FINALIZADA, ruta.getIdRuta());                     
                         LogRutaDTO log3 = new LogRutaDTO(ruta.getIdRuta(), ruta.getEstado(), usr, "[BOC] La ruta queda finalizada ");
                         biz.addLogRuta( log3 );
                         msg = "Pedido reprogramado y ruta finalizada";                         
                     }                     
                 }
             } 
             
             //Si estado es en despacho dejar en PAGADO
             if ( ped.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_DESPACHO ) {
                 biz.setCambiaEstadoDespacho(id_pedido, Constantes.ID_ESTAD_PEDIDO_PAGADO, usr.getLogin(), "[BOC] Por reprogramación pedido queda en estado Pagado");    
             }
             if ( Utils.mostrarCheckSiPedidoEstuvoEnTransito( ped.getId_estado() ) ) {
                 if ( req.getParameter("check_en_transito") != null ) {
                     if (req.getParameter("check_en_transito").toString().equalsIgnoreCase("1")) {
                         //Cambiamos las veces que a estado en transito (calle) el pedido
                         biz.modificaVecesEnRutaDePedido(id_pedido, "+");                
                     }           
                 }
             }          
             
             long idMotivo = 0;
             if ( req.getParameter("motivo") != null ) {
                 idMotivo = Long.parseLong(req.getParameter("motivo"));
             }
             long idResponsable = 0;
             if ( req.getParameter("responsable") != null ) {
                 idResponsable = Long.parseLong(req.getParameter("responsable"));
             }         
             
             biz.addMotivoResponsableReprogramacion(id_pedido,idMotivo,idResponsable,idJornadaDespachoAnterior,usr.getId_usuario());
             
             //Sumar reagendado
             biz.updPedidoReagendado(id_pedido);
             
             paramUrl += "&origen=" + origen + "&id_ruta=" + idRuta + "&msg=" + msg;
	    	 
	     }catch(BocException e){ 
             logger.debug("Controlando excepción: " + e.getMessage());
				String UrlError = getServletConfig().getInitParameter("UrlError");
				if (  e.getMessage().equals(Constantes._EX_JDESP_FALTAN_DATOS) ){
					logger.debug("El código de la jornada de despacho no existe");
					fp.add( "rc" , Constantes._EX_JDESP_FALTAN_DATOS );
					fp.add("msg","El código de la jornada de despacho no existe");
					paramUrl = UrlError + fp.forward();
				} else if( e.getMessage().equals(Constantes._EX_JDESP_SIN_CAPACIDAD) ){
					logger.debug("La jornada de despacho no tiene capacidad");
					fp.add( "rc" , Constantes._EX_JDESP_SIN_CAPACIDAD );
					fp.add("msg","La jornada de despacho no tiene capacidad");
					paramUrl = UrlError + fp.forward();
					
				}
	     }
	     logger.debug("PARAMURL: " + paramUrl);
	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);	     
	}	
}
