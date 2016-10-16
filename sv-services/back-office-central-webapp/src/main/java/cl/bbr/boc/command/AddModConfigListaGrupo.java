package cl.bbr.boc.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.pedidos.dto.ListaGrupoDTO;
import cl.bbr.jumbocl.pedidos.dto.ListaTipoGrupoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agregar y Modificar un datos para la configuracion de casos
 * Puede ser Tipo de Quiebre, Responsables o Jornadas (Ajax)
 * 
 * @author imoyano
 */

public class AddModConfigListaGrupo extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo AddModConfigListaGrupo [AJAX]");
        
        String mensajeSistema	= "";
        long idObjeto 			= 0;
    	int tipo	 			= 0;
    	String descripcion 		= "";
    	String estActivado		= "";
        String accion           = "";
    	BizDelegate bizDelegate 	 = new BizDelegate();

        try {
            
            if (req.getParameter("tipo") != null) {
                tipo = Integer.parseInt(req.getParameter("tipo").toString());
            }
            if (req.getParameter("accion") != null) {
                accion = req.getParameter("accion").toString();
            }
            if (req.getParameter("id_objeto") != null) {
                idObjeto = Long.parseLong(req.getParameter("id_objeto").toString());
            }
            if (req.getParameter("desc_objeto") != null) {
                descripcion = req.getParameter("desc_objeto").toString();
            }
            if (req.getParameter("activado") != null) {
                estActivado = req.getParameter("activado").toString();
            }
            
            if (accion.equalsIgnoreCase("DEL")) {
                logger.debug("A borrar un Grupo de Lista");
                bizDelegate.delGrupoLista(idObjeto);
                mensajeSistema = "OK";
                
            } else {
            
                if (idObjeto == 0) {
                    logger.debug("Agregaremos un nuevo Grupo de Lista");
                    ListaGrupoDTO lg = new ListaGrupoDTO();
                    lg.setNombre(descripcion);
                    lg.setActivado(estActivado);
                    ListaTipoGrupoDTO ltg = new ListaTipoGrupoDTO();
                    ltg.setIdListaTipoGrupo(tipo);
                    lg.setTipo(ltg);
                    idObjeto = bizDelegate.addGrupoLista(lg);
                    mensajeSistema = "OK";
                                    
                } else {
                    logger.debug("A modificar un Grupo de Lista");
                    ListaGrupoDTO lg = new ListaGrupoDTO();
                    lg.setIdListaGrupo(idObjeto);
                    lg.setNombre(descripcion);
                    lg.setActivado(estActivado);
                    ListaTipoGrupoDTO ltg = new ListaTipoGrupoDTO();
                    ltg.setIdListaTipoGrupo(tipo);
                    lg.setTipo(ltg);
                    bizDelegate.modGrupoLista(lg);
                    mensajeSistema = "OK";
                }
            }
            
        } catch (BocException e) {
            e.printStackTrace();
            mensajeSistema = "Ocurrió un error al realizar la acción";
                                   
        }        
            
        res.setContentType("text/xml");
        res.setHeader("Cache-Control", "no-cache");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write("<datos_objeto>");
        res.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
        res.getWriter().write("</datos_objeto>");            
        
        logger.debug("Fin AddModConfigListaGrupo [AJAX]");
    }
}
