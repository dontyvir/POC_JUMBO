package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author JoLazoGu
 * @version 1
 */
public class SeccionesExcluidasDC extends Command {

	private static final long serialVersionUID = 1L;

	protected void Execute(HttpServletRequest req, HttpServletResponse res,
			UserDTO usr) throws Exception {

		logger.info("Inicio SeccionesExcluidas");
		logger.info("Usuario: " + usr.getNombre());

		BizDelegate biz = new BizDelegate();

		// parámetros
		if (req.getParameter("marca") != null) {
			int id_seccion = new Integer(req.getParameter("marca")).intValue();
			biz.excluirSeccion(id_seccion);
			logger.debug("Se insertó seccion excluidas: " + id_seccion);
		}
		// parámetros
		if (req.getParameter("codigo") != null) {
			int id_seccion = new Integer(req.getParameter("codigo")).intValue();
			biz.permitirSeccion(id_seccion);
			logger.debug("Se elimino seccion excluidas: " + id_seccion);
		}

		String paramUrl = "ViewPromoDsctoColabSE";
		res.sendRedirect(paramUrl);

	}
}
