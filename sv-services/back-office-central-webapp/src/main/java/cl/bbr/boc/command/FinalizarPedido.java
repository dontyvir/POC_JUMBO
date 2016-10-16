package cl.bbr.boc.command;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.dto.MailDTO;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.LogRutaDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoExtDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Finalizar un pedido desde el monitor de despacho
 * Solo desde el monitor de despacho
 * 
 * @author imoyano
 */

public class FinalizarPedido extends Command {

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo FinalizarPedido Execute");
        
        String msg = "OK";
        BizDelegate biz = new BizDelegate();
        try {
            
            long idRuta = Long.parseLong(req.getParameter("id_ruta"));
            long idPedido = Long.parseLong(req.getParameter("id_pedido"));
            String fcLlegada = req.getParameter("fc_llegada");
            String hrIn = req.getParameter("hr_in");
            String hrOut = req.getParameter("hr_out");
            String cumplimiento = req.getParameter("cumplimiento");
            String motivo = req.getParameter("motivo");
            String responsable = req.getParameter("responsable");
            String devolucion = req.getParameter("devolucion");
            String observacion = req.getParameter("observacion");
            
            PedidoDTO ped = biz.getPedidosById(idPedido);
            
            if ( ped.getId_estado() == Constantes.ID_ESTAD_PEDIDO_FINALIZADO ) {
                msg = "Pedido ya finalizado previamente";
            } else {
                
                if ( observacion.length() > 200 ) {
                    observacion = observacion.substring(0,200);
                }
                
                biz.setCambiaEstadoDespacho(idPedido, Constantes.ID_ESTAD_PEDIDO_FINALIZADO, usr.getLogin(), "[BOC] FINALIZADO. " + observacion);
                
                PedidoExtDTO  pedExt = new PedidoExtDTO();
                pedExt.setIdPedido(idPedido);
                pedExt.setFcHoraLlegadaDomicilio(fcLlegada + " " + hrIn);
                pedExt.setFcHoraSalidaDomicilio(fcLlegada + " " + hrOut);
                pedExt.setCumplimiento(cumplimiento);
                if ( !Constantes.CUMPLIMIENTO_CTE_EN_TIEMPO.equalsIgnoreCase(cumplimiento) ) {
                    ObjetoDTO mot = new ObjetoDTO();
                    mot.setIdObjeto(Long.parseLong(motivo));
                    pedExt.setMotivoNoCumplimiento(mot);
                    
                    ObjetoDTO resp = new ObjetoDTO();
                    resp.setIdObjeto(Long.parseLong(responsable));
                    pedExt.setResponsableNoCumplimiento(resp);                
                }
                if ( devolucion.length() > 0 ) {
                    //mandar mail a casos
                    pedExt.setConDevolucion("S");
                    
                    ResourceBundle rb = ResourceBundle.getBundle("confCasos");
                    MailDTO mail = new MailDTO();
                    mail.setFsm_subject( rb.getString("devolucion.titulo") );
                    mail.setFsm_destina( rb.getString("devolucion.destinatario") );
                    mail.setFsm_remite( rb.getString("devolucion.remitente") );
                    
                    String body = rb.getString("devolucion.texto");
                    body = body.replaceAll("@id_pedido",String.valueOf(idPedido));
                    body = body.replaceAll("@id_ruta",String.valueOf(idRuta));
                    body = body.replaceAll("@devolucion",devolucion.replaceAll("\r\n","<br>"));
                    mail.setFsm_data( body );
                    
                    biz.enviarMailSupervisor( mail );
                    
                    if ( devolucion.length() > 200 ) {
                        devolucion = devolucion.substring(0,200);    
                    }
                    
                    LogPedidoDTO logp = new LogPedidoDTO(idPedido, usr.getLogin(), "[BOC] Finalizado con devolución: " + devolucion);
                    biz.addLogPedido(logp);
                    
                } else {
                    pedExt.setConDevolucion("N");
                }
                
                biz.updPedidoFinalizado(pedExt);
                
                RutaDTO ruta = biz.getRutaById(idRuta);            
                LogRutaDTO log1 = new LogRutaDTO(idRuta, ruta.getEstado(), usr, "[BOC] OP " + idPedido + " queda 'Finalizado'.");
                biz.addLogRuta( log1 );
                
                if (biz.getCountPedidoNoFinalizadosByRuta(idRuta) == 0) {
                    biz.setEstadoRuta(Constantes.ESTADO_RUTA_FINALIZADA, idRuta);
                    LogRutaDTO log2 = new LogRutaDTO(idRuta, ruta.getEstado(), usr, "[BOC] Ruta 'Finalizada'.");
                    biz.addLogRuta( log2 );
                }
                if ( ped.getId_cliente() != 0 ) {
                    ResourceBundle rb = ResourceBundle.getBundle("bo");
                    biz.actualizaContadorEncuestaCliente(ped.getId_cliente(),ped.getId_pedido(),Integer.parseInt( rb.getString("nro_compras") ));
                }
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            msg = "Ocurrió un error al finalizar el pedido";
        }
        
        res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");
        
        res.getWriter().write("<datos_trx>");
        res.getWriter().write("<mensaje>" + msg + "</mensaje>");
        res.getWriter().write("</datos_trx>");
        
        logger.debug("Fin FinalizarPedido Execute");
    }
}
