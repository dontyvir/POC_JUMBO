package cl.bbr.bol.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.pedidos.dto.ChoferTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.EstadoDTO;
import cl.bbr.jumbocl.pedidos.dto.FonoTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.LogRutaDTO;
import cl.bbr.jumbocl.pedidos.dto.PatenteTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agregar Ruta de Despacho (Ajax)
 * 
 * @author imoyano
 */

public class AgregaRutaDespacho extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo AgregaRutaDespacho [AJAX]");
        
        BizDelegate biz = new BizDelegate();
        
        String pds      = req.getParameter("pedidos").toString();
        long idChofer   = Long.parseLong(req.getParameter("chofer"));
        long idFono     = Long.parseLong(req.getParameter("fono"));
        long idPatente  = Long.parseLong(req.getParameter("patente"));
        
        if ( idChofer == 0 || idFono == 0 || idPatente == 0 || pds.length() == 0 ) {
            throw new Exception("Faltan datos necesarios para una Ruta");
        }
        
        RutaDTO ruta = new RutaDTO();
        
        ChoferTransporteDTO chofer = new ChoferTransporteDTO();
        chofer.setIdChofer(idChofer);
        ruta.setChofer(chofer);
        FonoTransporteDTO fono = new FonoTransporteDTO();
        fono.setIdFono(idFono);
        ruta.setFono(fono);
        PatenteTransporteDTO patente = new PatenteTransporteDTO();
        patente.setIdPatente(idPatente);
        ruta.setPatente(patente);
        EstadoDTO estado = new EstadoDTO();
        estado.setId_estado(Constantes.ESTADO_RUTA_EN_PREPARACION);
        ruta.setEstado(estado);
        LocalDTO loc = new LocalDTO();
        loc.setId_local(usr.getId_local());
        ruta.setLocal(loc);
        
        long idRuta = biz.addRuta(ruta);
        
        try {            
            // Agregamos al log de la ruta
            LogRutaDTO log = new LogRutaDTO(idRuta, estado, usr, "[BOL] Creación de la Ruta");
            biz.addLogRuta( log );
            
            //Agregamos los pedidos a la ruta
            String[] pedidos = pds.split("-=-");
            String pedidosAgregados = "";
            String sep = "";
            for ( int i=0; i < pedidos.length; i++ ) {
                try {
                    if ( biz.addPedidoRuta(Long.parseLong(pedidos[i]), idRuta) == 1 ) {
                        //agregamos al log
                        pedidosAgregados += (sep + pedidos[i]);
                        sep = ",";                        
                        LogPedidoDTO logp = new LogPedidoDTO(Long.parseLong(pedidos[i]), usr.getLogin(), "[BOL] Pedido agregado a la Ruta: " + idRuta);
                        biz.addLogPedido(logp);
                        
                    }    
                } catch (Exception e) {}                
            }
            if ( pedidosAgregados.length() > 0 ) {
                //Agregamos al Log de la ruta los pedidos agregados
                LogRutaDTO log1 = new LogRutaDTO(idRuta, estado, usr, "[BOL] Se agregaron a Ruta los pedidos: " + pedidosAgregados);
                biz.addLogRuta( log1 );
                
                //Actualizamos los bins de la Ruta
                biz.actualizaCantBinsRuta(idRuta);
            }
            
            
        } catch (Exception e) {}
        
        
		res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");
		
        res.getWriter().write("<datos_trx>");
        res.getWriter().write("<mensaje>OK</mensaje>");
        res.getWriter().write("<id_ruta>"+idRuta+"</id_ruta>");
        res.getWriter().write("</datos_trx>");
		
        logger.debug("Fin AgregaRutaDespacho [AJAX]");
    }
}
