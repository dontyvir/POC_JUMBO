package cl.bbr.fo.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.marketing.dto.MarketingElementoDTO;

/**
 * Presenta elemento de marketing.
 *  
 * @author BBR e-commerce & retail
 *  
 */
public class ViewElemento extends Command {

	/**
	 * Presenta y modifica el campo click de marketing para el elemento presentado
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {
		try {

			BizDelegate biz = new BizDelegate();
			
			long eleid = Long.parseLong(arg0.getParameter("eleid").toString());
			long marid = Long.parseLong(arg0.getParameter("marid").toString());
			
			MarketingElementoDTO mar = new MarketingElementoDTO();
			
			// Recuperar datos del elemento desde el repositorio
			if (arg0.getParameter("eleid").toString() != null && arg0.getParameter("marid").toString() != null){
				mar = biz.getMarkElemento(eleid, marid);
				arg1.sendRedirect(mar.getUrl());
			}
			
		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}

	}

}