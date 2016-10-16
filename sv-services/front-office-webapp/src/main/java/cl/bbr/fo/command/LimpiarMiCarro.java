package cl.bbr.fo.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;

/**
 * Elimina un producto del Carro - Ajax
 * 
 * @author carriagada IT4B
 *  
 */
public class LimpiarMiCarro extends Command {
	
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {
        
       try {
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			//Se recupera la session del cliente	
			long cliente_id = 0L;
			String ses_invitado_id = "0";
			if(session.getAttribute("ses_cli_id") != null)
			  cliente_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());
			if(session.getAttribute("ses_invitado_id")!= null)
			  ses_invitado_id = session.getAttribute("ses_invitado_id").toString();
                
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
            biz.deleteCarroCompraAll(cliente_id, ses_invitado_id);
          //se elimina variables de session
            session.removeAttribute("top");
            session.removeAttribute("dto");
            
            if (arg0.getParameter("noreenviar") != null &&
                    arg0.getParameter("noreenviar").trim().compareTo("1")==0){
//+20120719coh
                arg1.setContentType("text/xml");
                arg1.setHeader("Cache-Control", "no-cache");
                arg1.setCharacterEncoding("UTF-8");
                
                //vemos cual es el mensaje a desplegar
                arg1.getWriter().write("<respuesta>");
                arg1.getWriter().write("<mensaje>OK</mensaje>");
                arg1.getWriter().write("</respuesta>");
            }else{
//-20120719coh
	            String dis_ok = getServletConfig().getInitParameter("dis_ok");
	            arg1.sendRedirect(dis_ok);
//+20120719coh
            }
//-20120719coh
        } catch (Exception e) {
			this.getLogger().error(e);
            e.printStackTrace();
        }
	}
}