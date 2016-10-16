package cl.bbr.bol.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.bol.exceptions.BolException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.LogCasosDTO;
import cl.bbr.jumbocl.casos.dto.ProductoCasoDTO;
import cl.bbr.jumbocl.casos.dto.QuiebreCasoDTO;
import cl.bbr.jumbocl.casos.utils.CasosConstants;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agregar y Modificar un producto de un caso (Ajax)
 * 
 * @author imoyano
 */
public class AgregaModProductoParaCaso extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {        
        logger.debug("Comienzo AgregaModProductoParaCaso [AJAX]");
        
        //String mensajeSistema = "";
        String titleIcono = "";
        
        try {
	        ProductoCasoDTO producto = new ProductoCasoDTO();
	        producto.setIdProducto(Long.parseLong(req.getParameter("idProducto")));
	        producto.setIdCaso(Long.parseLong(req.getParameter("idCaso")));
	        producto.setTipoAccion(req.getParameter("tipo"));
	        
	        producto.setPpCantidad(req.getParameter("ppCantidad"));
	        producto.setPpUnidad(req.getParameter("ppUnidad"));
	        producto.setPpDescripcion(req.getParameter("ppDescripcion"));
	        producto.setComentarioBOC(req.getParameter("comentarioBoc"));
	        
	        producto.setPsCantidad(req.getParameter("psCantidad"));
	        producto.setPsUnidad(req.getParameter("psUnidad"));
	        producto.setPsDescripcion(req.getParameter("psDescripcion"));
	        producto.setComentarioBOL(req.getParameter("comentarioBol"));
	        producto.setPrecio(Long.parseLong(req.getParameter("precio")));
	        
	        QuiebreCasoDTO q = new QuiebreCasoDTO();
	        q.setIdQuiebre(Long.parseLong(req.getParameter("quiebre")));
	        producto.setQuiebre(q);
	        ObjetoDTO r = new ObjetoDTO();
	        r.setIdObjeto(Long.parseLong(req.getParameter("responsable")));
            producto.setResponsable(r);
            ObjetoDTO mot = new ObjetoDTO();
            mot.setIdObjeto(Long.parseLong(req.getParameter("motivo")));
            producto.setMotivo(mot);
	        producto.setPickeador(req.getParameter("pickeador"));	        
	        
	        BizDelegate bizDelegate = new BizDelegate();
	        
	        if (bizDelegate.modProductoCaso(producto)) {
		        //mensajeSistema = CasosConstants.MSJ_ADD_PRODUCTO_EXITO;		        
		        logger.debug("Inicio del loggeo");
			    CasoDTO caso = bizDelegate.getCasoByIdCaso(producto.getIdCaso());
		        LogCasosDTO log = new LogCasosDTO(caso.getIdCaso(),caso.getEstado(),usr.getLogin(),"[BOL] "+CasosConstants.LOG_MOD_PRD_CASO+"Comentario: "+producto.getComentarioBOL());
		        bizDelegate.addLogCaso(log);
		        logger.debug("Fin del loggeo");
		        
		    } else {
		        //mensajeSistema = CasosConstants.MSJ_ADD_PRODUCTO_ERROR;
		    }	    
	        
	        if (producto.getTipoAccion().equalsIgnoreCase("R")) {
                titleIcono = "Producto a Retirar";
            } else if (producto.getTipoAccion().equalsIgnoreCase("D")) {
                titleIcono = "Documento";
            } else  {
                titleIcono = "Producto a Enviar";
            }
	        
	        res.setContentType("text/html");
            res.setHeader("Cache-Control", "no-cache");
            res.setCharacterEncoding("UTF-8");
            if (producto.getTipoAccion().equalsIgnoreCase("D")) {
                res.getWriter().write("<td align=\"left\">"+producto.getPpDescripcion()+"</td>");
            } else {
                res.getWriter().write("<td align=\"left\">"+producto.getPpDescripcion()+" / "+producto.getPpCantidad()+" / "+producto.getPpUnidad()+"</td>");
            }
            if (producto.getTipoAccion().equalsIgnoreCase("E")) {
                res.getWriter().write("<td align=\"left\">"+producto.getPsDescripcion()+" / "+producto.getPsCantidad()+" / "+producto.getPsUnidad()+"</td>");
            }
            
            res.getWriter().write("<td align=\"left\">" + bizDelegate.getQuiebreById(q.getIdQuiebre()).getNombre() + "</td>");
            res.getWriter().write("<td align=\"left\">"+bizDelegate.getResponsableById(r.getIdObjeto()).getNombre()+"</td>");
            res.getWriter().write("<td align=\"left\">"+producto.getComentarioBOC()+"</td>");
            res.getWriter().write("<td align=\"left\">"+producto.getComentarioBOL()+"</td>");
            if (!producto.getTipoAccion().equalsIgnoreCase("D")) {
                res.getWriter().write("<td align=\"right\">"+Formatos.formatoPrecio(Double.parseDouble(String.valueOf(producto.getPrecio())))+"&nbsp;</td>");
            }
            res.getWriter().write("<td align=\"center\" nowrap><a href=\"javascript:traerInfoProducto('"+producto.getIdProducto()+"');\"><img src=\"img/editicon.gif\" border=\"0\" height=\"17\" width=\"19\" title=\"Editar "+titleIcono+"\"></a></td>");
            
			
        } catch (BolException e) {
	        e.printStackTrace();
            //mensajeSistema = CasosConstants.MSJ_ADD_PRODUCTO_ERROR;
        }
		
        logger.debug("Fin AgregaModProductoParaCaso [AJAX]");
    }
}
