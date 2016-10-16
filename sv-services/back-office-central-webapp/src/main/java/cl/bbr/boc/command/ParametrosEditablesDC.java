package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

public class ParametrosEditablesDC extends Command {

	private static final long serialVersionUID = 1L;
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res,	UserDTO usr) throws Exception {
		try {
			logger.info("Inicio ParametrosEditablesDC");
			logger.info("Usuario: " + usr.getNombre());
			int resultado = 0;
			BizDelegate biz = new BizDelegate();
			if(req.getParameter("checkMontoLimite") != null && req.getParameter("checkMontoLimite").trim().equalsIgnoreCase("on") && req.getParameter("changeMontoLimite") != null){
				biz.setMontoLimite(req.getParameter("changeMontoLimite"));
				logger.info("Se realiza cambio de monto limite");
				resultado = 1;
			}
			if(req.getParameter("checkDescuentoMayor") != null && req.getParameter("checkDescuentoMayor").trim().equalsIgnoreCase("on") && req.getParameter("changeDescuentoMayor") != null){
				biz.setDescuentoMayor(req.getParameter("changeDescuentoMayor"));
				logger.info("Se realiza cambio de descuento mayor");
				resultado = 1;
			}
			if(req.getParameter("checkDescuentoMenor") != null && req.getParameter("checkDescuentoMenor").trim().equalsIgnoreCase("on") && req.getParameter("changeDescuentoMenor") != null){
				biz.setDescuentoMenor(req.getParameter("changeDescuentoMenor"));
				logger.info("Se realiza cambio de descuento menor");
				resultado = 1;
			}
			logger.info("Fin ParametrosEditablesDC");
			res.sendRedirect("ViewPromoDsctoColabPE?resultado=" + resultado);
		} catch (Exception e) {
			logger.error("Error: ", e);
			res.sendRedirect("ViewError?mensaje=Ocurrio un error en la edicion de parametros");
		}
	}
}
