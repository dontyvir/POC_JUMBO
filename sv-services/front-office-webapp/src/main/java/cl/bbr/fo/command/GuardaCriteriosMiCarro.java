package cl.bbr.fo.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.clientes.dto.CriterioClienteSustitutoDTO;

/**
 * Guarda sustitutos de los clientes
 * 
 */
public class GuardaCriteriosMiCarro extends Command {
	
	protected void execute(HttpServletRequest req, HttpServletResponse arg1) throws CommandException {
		try {	
			// Recupera la sesión del usuario
			HttpSession session = req.getSession();
			if (!(req.getParameter("criterios")==null))
				session.setAttribute("criterio", req.getParameter("criterios").toString());

			if (!(req.getParameter("productos")==null))
				session.setAttribute("producto", req.getParameter("productos").toString());
			if (!(req.getParameter("categorias")==null))
				session.setAttribute("categoria", req.getParameter("categorias").toString());

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			Long idCliente = new Long(session.getAttribute("ses_cli_id").toString());
            List criteriosProductos = new ArrayList();
            
            String idSession = null;
            if (session.getAttribute("ses_invitado_id") != null)
                idSession = session.getAttribute("ses_invitado_id").toString();
            
            
            if (!session.getAttribute("criterio").toString().equals("")) {
                String[] categorias = session.getAttribute("categoria").toString().split("_");
                String[] productos = session.getAttribute("producto").toString().split("_");
                String[] criterios = session.getAttribute("criterio").toString().split("_");
            
                CriterioClienteSustitutoDTO criterio = null;
                for ( int i = 0; i < criterios.length; i++ ) {
                    criterio = new CriterioClienteSustitutoDTO();
                    criterio.setIdProducto(Long.parseLong( productos[i] ));
                    criterio.setIdCriterio(Long.parseLong( criterios[i] ));
                    if ( criterio.getIdCriterio() == 4 ) {
                        criterio.setSustitutoCliente( req.getParameter("text_" + categorias[i] + "_" + productos[i]));
                    }
                    criteriosProductos.add(criterio);
                }
                biz.guardaCriteriosMiCarro(idCliente, criteriosProductos, idSession);
            }
            arg1.sendRedirect(getServletConfig().getInitParameter("dis_ok"));
		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException(e);
		}
	}
}