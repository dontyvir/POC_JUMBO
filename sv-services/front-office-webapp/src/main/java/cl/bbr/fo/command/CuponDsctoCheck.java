package cl.bbr.fo.command;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;

public class CuponDsctoCheck extends Command {

	private static final long serialVersionUID = 1L;

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws CommandException {
		HttpSession session = null;
		BizDelegate biz = null;
		CuponDsctoDTO cdDTO = null;
		String respuesta = "";
		String msg_cupon = "";
		long rut = 0;

		if (arg0.getParameter("cupones") != null) {
			try {
				String codigo = arg0.getParameter("cupones").toUpperCase().trim();
				session = arg0.getSession();
				if (session.getAttribute("ses_cli_rut") != null) {
					rut = Integer.parseInt(session.getAttribute("ses_cli_rut").toString());
				}
				biz = new BizDelegate();
				cdDTO = biz.getCuponDscto(codigo);

				if (cdDTO == null) {
					respuesta = "NOK";
					msg_cupon = "El cupon es invalido ó vigencia de esta promoción ya se acabó";
				} else if (cdDTO.getCantidad() == 0) {
					respuesta = "NOK";
					msg_cupon = "El stock de esta promoción ya se acabó";
				} else if (cdDTO.getPublico() == 1 || biz.isCuponForRut(rut, cdDTO.getId_cup_dto())) {
					respuesta = "OK";
					msg_cupon = "EL cupon puede ser aplicado";
					session.setAttribute("ses_cupon_descuento_object", cdDTO);
				} else {
					respuesta = "NOK";
					msg_cupon = "El cupon no esta asociado";
				}

			} catch (Exception e) {
				logger.error("Error: ", e);
			}

		} else {
			respuesta = "NOK";
			msg_cupon = "Debe agregar un codigo de cupón";
		}

		arg1.setContentType("text/xml");
		arg1.setHeader("Cache-Control", "no-cache");
		arg1.setCharacterEncoding("UTF-8");
		try {
			arg1.getWriter().write("<datos_objeto>");
			arg1.getWriter().write("<respuesta>" + respuesta + "</respuesta>");
			arg1.getWriter().write("<mensaje>" + msg_cupon + "</mensaje>");
			arg1.getWriter().write("</datos_objeto>");
		} catch (IOException e1) {
			logger.error("Error: ", e1);
		}
	}

}
