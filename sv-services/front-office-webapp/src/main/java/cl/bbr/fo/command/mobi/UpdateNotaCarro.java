package cl.bbr.fo.command.mobi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.command.Command;
import cl.bbr.fo.exception.CommandException;

/**
 * Entrega el total en pesos de los productos que tiene el cliente en el carro
 * 
 * Pagina para mobile
 * 
 * @author imoyano
 * 
 */
public class UpdateNotaCarro extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {

		try {
		    // Recupera la sesión del usuario
			HttpSession session = arg0.getSession();			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
            String msg = "OK";
            
            try {
                long idCliente  = Long.parseLong(session.getAttribute("ses_cli_id").toString());
                long idCarro    = Long.parseLong(arg0.getParameter("id_carro"));
                long idProducto = Long.parseLong(arg0.getParameter("id_producto"));
                String nota     = arg0.getParameter("nota");
                
                biz.updateNotaCarroCompra( idCliente, idCarro, idProducto, nota );                 
            } catch (Exception e) {
                msg = "No se pudo modificar la nota del carro.";
            }                   
            
            arg1.setContentType("text/xml");
            arg1.setHeader("Cache-Control", "no-cache");
            arg1.setCharacterEncoding("UTF-8");
            arg1.getWriter().write("<datos_objeto>");
            arg1.getWriter().write("<mensaje>" + msg + "</mensaje>");
            arg1.getWriter().write("</datos_objeto>");

		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}


	}

}