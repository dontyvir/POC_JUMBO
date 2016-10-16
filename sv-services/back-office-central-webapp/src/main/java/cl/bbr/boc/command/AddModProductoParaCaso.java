package cl.bbr.boc.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
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
 * Agragar y Modificar un producto de un caso (Ajax)
 * 
 * @author imoyano
 */

public class AddModProductoParaCaso extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo AddModProductoParaCaso [AJAX]");

        String mensajeSistema = "";
        String titleIcono = "";
        ProductoCasoDTO producto = new ProductoCasoDTO();
        long idProducto = 0;
        BizDelegate bizDelegate = new BizDelegate();
        QuiebreCasoDTO q = new QuiebreCasoDTO();
        ObjetoDTO r = new ObjetoDTO();
        ObjetoDTO mot = new ObjetoDTO();

        try {            
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
            
            q.setIdQuiebre(Long.parseLong(req.getParameter("quiebre")));
            producto.setQuiebre(q);
            
            r.setIdObjeto(Long.parseLong(req.getParameter("responsable")));
            producto.setResponsable(r);
            mot.setIdObjeto(Long.parseLong(req.getParameter("motivo")));
            producto.setMotivo(mot);
            producto.setPickeador(req.getParameter("pickeador"));

            // ---- Productos ----
            if (producto.getIdProducto() == 0) {
                idProducto = bizDelegate.addProductoCaso(producto);
                logger.debug("Se agregó el producto:" + idProducto);
                if (idProducto == 0) {
                    mensajeSistema = CasosConstants.MSJ_ADD_PRODUCTO_ERROR;
                } else {
                    mensajeSistema = CasosConstants.MSJ_ADD_PRODUCTO_EXITO;
                    logger.debug("Inicio del loggeo");
                    CasoDTO caso = bizDelegate.getCasoByIdCaso(producto.getIdCaso());
                    LogCasosDTO log = new LogCasosDTO(caso.getIdCaso(), caso.getEstado(), usr.getLogin(), "[BOC] " + CasosConstants.LOG_ADD_PRD_CASO + "Descripción: " + producto.getPpDescripcion());
                    bizDelegate.addLogCaso(log);
                    logger.debug("Fin del loggeo");
                }
                
            } else {
                if (bizDelegate.modProductoCaso(producto)) {
                    mensajeSistema = CasosConstants.MSJ_ADD_PRODUCTO_EXITO;
                    logger.debug("Inicio del loggeo");
                    CasoDTO caso = bizDelegate.getCasoByIdCaso(producto.getIdCaso());
                    LogCasosDTO log = new LogCasosDTO(caso.getIdCaso(), caso.getEstado(), usr.getLogin(), "[BOC] " + CasosConstants.LOG_MOD_PRD_CASO + "Descripción: " + producto.getPpDescripcion());
                    bizDelegate.addLogCaso(log);
                    logger.debug("Fin del loggeo");
                } else {
                    mensajeSistema = CasosConstants.MSJ_ADD_PRODUCTO_ERROR;
                }
                
            }

        } catch (BocException e) {
            e.printStackTrace();
            mensajeSistema = CasosConstants.MSJ_ADD_PRODUCTO_ERROR;
        }
        

        // RESPUESTA            
        if (producto.getIdProducto() == 0) {
            
            res.setContentType("text/xml");
            res.setHeader("Cache-Control", "no-cache");
            res.setCharacterEncoding("UTF-8");

            res.getWriter().write("<datos_producto>");
            res.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
            res.getWriter().write("<producto_desc_log>" + producto.getPpDescripcion().replaceAll("'","") + "</producto_desc_log>");
            res.getWriter().write("<id_producto_new>" + idProducto + "</id_producto_new>");
            res.getWriter().write("<quiebre>" + bizDelegate.getQuiebreById(q.getIdQuiebre()).getNombre() + "</quiebre>");
            res.getWriter().write("<responsable>" + bizDelegate.getResponsableById(r.getIdObjeto()).getNombre() + "</responsable>");
            if ( producto.getPickeador().length() > 0 ) {
                res.getWriter().write("<pickeador>"+producto.getPickeador()+"</pickeador>");
            } else {
                res.getWriter().write("<pickeador>--</pickeador>");    
            }
            res.getWriter().write("<precio>" + Formatos.formatoPrecio(Double.parseDouble(String.valueOf(bizDelegate.getProductoById(idProducto).getPrecio()))) + "</precio>");
            res.getWriter().write("</datos_producto>");
            
        } else {
            if (producto.getTipoAccion().equalsIgnoreCase("R")) {
                titleIcono = "Producto a Retirar";
            } else if (producto.getTipoAccion().equalsIgnoreCase("D")) {
                titleIcono = "Documento";
            } else if (producto.getTipoAccion().equalsIgnoreCase("P")) {
                titleIcono = "Envío de Dinero";
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
            if (!producto.getTipoAccion().equalsIgnoreCase("D") && !producto.getTipoAccion().equalsIgnoreCase("P")) {
                res.getWriter().write("<td align=\"left\">"+ producto.getPickeador() +"</td>");
            }
            res.getWriter().write("<td align=\"left\">"+producto.getComentarioBOC()+"</td>");
            res.getWriter().write("<td align=\"left\">"+producto.getComentarioBOL()+"</td>");
            if (!producto.getTipoAccion().equalsIgnoreCase("D")) {
                res.getWriter().write("<td align=\"right\">"+Formatos.formatoPrecio(Double.parseDouble(String.valueOf(producto.getPrecio())))+"&nbsp;</td>");
            }
            res.getWriter().write("<td align=\"center\" nowrap>");
            res.getWriter().write("  <a href=\"javascript:traerInfoProducto('"+producto.getIdProducto()+"');\"><img src=\"img/editicon.gif\" border=\"0\" height=\"17\" width=\"19\" title=\"Editar "+titleIcono+"\"></a>&nbsp;");
            res.getWriter().write("  <a href=\"javascript:eliminarProducto('"+producto.getIdProducto()+"','"+producto.getTipoAccion()+"','"+producto.getPpDescripcion().replaceAll("'","")+"');\"><img src=\"img/trash.gif\" border=\"0\" height=\"16\" width=\"16\" title=\"Eliminar "+titleIcono+"\"></a>");
            if (producto.getTipoAccion().equalsIgnoreCase("E")) {
                res.getWriter().write("&nbsp;<a href=\"javascript:cambiarEnvioDinero('"+producto.getIdProducto()+"');\"><img src=\"img/envio_dinero.gif\" border=\"0\" height=\"16\" width=\"16\" title=\"Cambiar a 'Envío de Dinero'\"></a>");
            }
            res.getWriter().write("</td>");
            
        }        

        logger.debug("Fin AddModProductoParaCaso [AJAX]");
    }
}
