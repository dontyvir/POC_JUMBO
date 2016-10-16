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
 * @author imoyano
 * 
 */
public class SaveSustitutos extends Command {
	
	protected void execute(HttpServletRequest req, HttpServletResponse arg1)
			throws CommandException {

		try {	
			
			// Recupera la sesión del usuario
			HttpSession session = req.getSession();
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			Long idCliente = new Long(session.getAttribute("ses_cli_id").toString());
            List sustitutosPorCategorias = new ArrayList();
            List sustitutosPorCategoriasBO = new ArrayList();
            
            String from = "";
            if ( req.getParameter("from") != null ) {
                from = req.getParameter("from");
            }
            
            String[] cats = req.getParameter("categorias").toString().split(",");
            
            for ( int i = 0; i < cats.length; i++ ) {
                String[] prods = req.getParameter("prods_" + cats[i]).toString().split(",");
                for ( int j = 0; j < prods.length; j++ ) {
                	CriterioClienteSustitutoDTO criterio = new CriterioClienteSustitutoDTO();
                    cl.bbr.jumbocl.pedidos.dto.CriterioSustitutoDTO criBO = new cl.bbr.jumbocl.pedidos.dto.CriterioSustitutoDTO();
                    criterio.setIdProducto(Long.parseLong( prods[j] ));
                    criBO.setIdProducto(Long.parseLong( prods[j] ));
                    criterio.setIdCriterio(Long.parseLong( req.getParameter("radiobutton_" + cats[i] + "_" + prods[j] )) );
                    criBO.setIdCriterio(Integer.parseInt( req.getParameter("radiobutton_" + cats[i] + "_" + prods[j] )));
                    if ( criterio.getIdCriterio() == 4 ) {
                        criterio.setSustitutoCliente( req.getParameter( "textfield_" + cats[i] + "_" + prods[j] ) );
                        criBO.setSustitutoCliente(req.getParameter( "textfield_" + cats[i] + "_" + prods[j] ));
                    }
                    sustitutosPorCategorias.add( criterio );
                    sustitutosPorCategoriasBO.add( criBO );
                }
            }
            biz.updateSustitutosCliente(idCliente, sustitutosPorCategorias);
            
            // actualizamos los sustitutos en el pedido
            if ( from.equalsIgnoreCase("RESUMEN") ) {
                if ( session.getAttribute("sesspedido") != null ) {
                    biz.updateCriterioSustitucionEnPedido(Long.parseLong(session.getAttribute("sesspedido").toString()), sustitutosPorCategoriasBO);
                }
            }
                        
            arg1.sendRedirect(getServletConfig().getInitParameter("dis_ok") + "?save=SI&from=" + from);

		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException(e);
		}
	}
}