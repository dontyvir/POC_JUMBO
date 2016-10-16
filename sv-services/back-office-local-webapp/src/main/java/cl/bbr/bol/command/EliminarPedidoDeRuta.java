package cl.bbr.bol.command;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.LogRutaDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Eliminar Pedido(s) de Ruta (Ajax)
 * 
 * @author imoyano
 */

public class EliminarPedidoDeRuta extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo EliminarPedidoDeRuta [AJAX]");
        
        String msg = "OK";
        BizDelegate biz = new BizDelegate();
        
        String pds  = req.getParameter("pedidos").toString();
        long idRuta = Long.parseLong(req.getParameter("id_ruta"));
        
        if ( idRuta == 0 || pds.length() == 0 ) {
            throw new Exception("Faltan datos para eliminar pedidos de una Ruta");
        }
        
        try {            
            RutaDTO ruta = biz.getRutaById(idRuta);
            if ( ruta.getEstado().getId_estado() != Constantes.ESTADO_RUTA_EN_PREPARACION ) {
                msg = "Acción no permitida por el estado de la Ruta";
                
            } else {            
                //Agregamos los pedidos a la ruta
                String[] pedidos = pds.split("-=-");
                
                String pedidosEliminados = "";
                String sep = "";
                for ( int i=0; i < pedidos.length; i++ ) {
                    try {
                        if ( biz.delPedidoRuta(Long.parseLong(pedidos[i])) == 1 ) {
                            //agregamos al log
                            pedidosEliminados += (sep + pedidos[i]);
                            sep = ",";                        
                            LogPedidoDTO logp = new LogPedidoDTO(Long.parseLong(pedidos[i]), usr.getLogin(), "[BOL] Pedido eliminado de la Ruta: " + idRuta);
                            biz.addLogPedido(logp);
                        }                            
                    } catch (Exception e) {}                
                }
                
                if ( pedidosEliminados.length() > 0 ) {
                    //Agregamos al Log de la ruta los pedidos agregados
                    LogRutaDTO log1 = new LogRutaDTO(idRuta, ruta.getEstado(), usr, "[BOL] Se eliminaron de Ruta los pedidos: " + pedidosEliminados);
                    biz.addLogRuta( log1 );
                } else {
                    msg = "No se eliminaron pedidos de Ruta.";
                }
                
                //anular ruta si no hay mas pedidos
                List peds = biz.getPedidosByRuta(idRuta);
                if ( peds.size() == 0 ) {
                    biz.setEstadoRuta(Constantes.ESTADO_RUTA_ANULADA, idRuta);
                    
                    LogRutaDTO log1 = new LogRutaDTO(idRuta, ruta.getEstado(), usr, "[BOL] La ruta queda anulada ");
                    biz.addLogRuta( log1 );
                    
                }
                biz.actualizaCantBinsRuta(idRuta);
                
            }                        
            
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        
		res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");
		
        res.getWriter().write("<datos_trx>");
        res.getWriter().write("<mensaje>" + msg + "</mensaje>");
        res.getWriter().write("</datos_trx>");
		
        logger.debug("Fin EliminarPedidoDeRuta [AJAX]");
    }
}
