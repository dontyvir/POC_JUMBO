package cl.bbr.bol.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.pedidos.dto.LogRutaDTO;
import cl.bbr.jumbocl.pedidos.dto.RutaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Anular una Ruta (Ajax)
 * 
 * @author imoyano
 */

public class AnularRuta extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo AnularRuta [AJAX]");
        
        String msg = "OK";
        BizDelegate biz = new BizDelegate();
        
        long idRuta = Long.parseLong(req.getParameter("id_ruta"));
        
        if ( idRuta == 0 ) {
            throw new Exception("Faltan datos necesarios");
        }
        
        try {            
            RutaDTO ruta = biz.getRutaById(idRuta);
            if ( ruta.getEstado().getId_estado() != Constantes.ESTADO_RUTA_EN_PREPARACION ) {
                msg = "Acción no permitida por el estado de la Ruta";
                
            } else {                
                biz.liberarPedidosByRuta(idRuta);                
                biz.setEstadoRuta(Constantes.ESTADO_RUTA_ANULADA, idRuta);
                
                LogRutaDTO log1 = new LogRutaDTO(idRuta, ruta.getEstado(), usr, "[BOL] Ruta anulada ");
                biz.addLogRuta( log1 );
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
		
        logger.debug("Fin AnularRuta [AJAX]");
    }
}
