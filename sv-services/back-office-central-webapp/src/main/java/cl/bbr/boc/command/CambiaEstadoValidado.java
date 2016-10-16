package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Servlet implementation class CambiaEstadoValidado
 */
public class CambiaEstadoValidado extends Command {
	private static final long serialVersionUID = 1L;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res,	UserDTO usr) throws Exception {
		logger.debug("User: " + usr.getLogin());
		String mensajeSistema = "OK";
		BizDelegate bizDelegate = new BizDelegate();
		long idPedido = 0;
		boolean resultado =false;
		try {
			if (req.getParameter("id_pedido") != null) {
				idPedido = Long.parseLong(req.getParameter("id_pedido").toString());
				logger.debug("Id_pedido: " + idPedido);
				
			resultado =bizDelegate.retrocederOPEstadoValidado(idPedido,usr.getLogin());

			}
			if (!resultado) {
				mensajeSistema = "Error al cambiar Estado OP a Validado. ";
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
			mensajeSistema = "Error al cambiar Estado OP a Validado. ";
        }
        res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write("<datos_objeto>");
        res.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
        res.getWriter().write("</datos_objeto>");
	}

}
