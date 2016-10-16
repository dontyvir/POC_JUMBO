package cl.bbr.boc.command;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.informes.dto.MailSustitutosFaltantesDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Agregar y Modificar los datos del mail de SyF (Ajax)
 * 
 * @author imoyano
 */

public class AddModMailsSyF extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
        logger.debug("Comienzo AddModMailsSyF [AJAX]");
        
        String mensajeSistema	= "";
        long idMail 			= 0;
    	String nombres	 		= "";
    	String apellidos 		= "";
    	String mail		 		= "";
    	boolean esNuevo			= true;
    	String estActivado		= "";
    	String accion			= "";
    	BizDelegate bizDelegate = new BizDelegate();
    	MailSustitutosFaltantesDTO m = new MailSustitutosFaltantesDTO();
    	
        try { 
            if (req.getParameter("accion") != null) {
                accion = req.getParameter("accion").toString();
            }
            if (req.getParameter("id_mail") != null) {
                idMail = Long.parseLong(req.getParameter("id_mail").toString());
            }
            if (req.getParameter("nombres") != null) {
                nombres = req.getParameter("nombres").toString();
            }
            if (req.getParameter("apellidos") != null) {
                apellidos = req.getParameter("apellidos").toString();
            }
            if (req.getParameter("mail") != null) {
                mail = req.getParameter("mail").toString();
            }
            if (req.getParameter("activado") != null) {
                estActivado = req.getParameter("activado").toString();
            }
            
            if (accion.equalsIgnoreCase("DEL")) {
                //ELIMINAMOS EL MAIL
                bizDelegate.delMailSyFById(idMail);
                mensajeSistema = "OK"; 
                
            } else {            
                if (idMail == 0) {
                    // AGREGAMOS UN NUEVO MAIL
                    esNuevo = true;                
                    m.setNombre(nombres);
                    m.setApellido(apellidos);
                    m.setMail(mail);
                    m.setActivado(estActivado);                
                    idMail = bizDelegate.addMailSyF(m);
                    mensajeSistema = "OK";
                    
                } else {
                    // MODIFICAMOS EL MAIL
                    esNuevo = false;
                    m.setId(idMail);
                    m.setNombre(nombres);
                    m.setApellido(apellidos);
                    m.setMail(mail);
                    m.setActivado(estActivado);
                    bizDelegate.modMailSyF(m);
                    mensajeSistema = "La información de mail fue guardada exitosamente";                               
                }
                m = bizDelegate.getMailSyFById(idMail);
                
            }          
            
        } catch (BocException e) {
            e.printStackTrace();            
            if (accion.equalsIgnoreCase("DEL")) {
                mensajeSistema = "La información de mail no pudo ser eliminada";
            } else {
	            if (esNuevo) {
	                mensajeSistema = "El Mail no pudo ser guardado";
	            } else {
	                //En caso de estar modificando y se cae el servicio, se rescatan los datos originales y se muestran en pantalla
	                m = bizDelegate.getMailSyFById(idMail);                
	            }
            }
        }   
            
        //RESPUESTA
        if (accion.equalsIgnoreCase("DEL")) {
            res.setContentType("text/xml");
            res.setHeader("Cache-Control", "no-cache");
            res.setCharacterEncoding("UTF-8");

            res.getWriter().write("<datos_objeto>");
            res.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
            res.getWriter().write("</datos_objeto>");
            
        } else {
            if (esNuevo) {            
                res.setContentType("text/xml");
                res.setHeader("Cache-Control", "no-cache");
                res.setCharacterEncoding("UTF-8");
                res.getWriter().write("<datos_objeto>");
                res.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
                res.getWriter().write("<id_mail_new>" + idMail + "</id_mail_new>");
                res.getWriter().write("<fc_ini>" + Formatos.frmFecha( m.getFechaIngreso() ) + "</fc_ini>");
                res.getWriter().write("<fc_mod>" + Formatos.frmFecha( m.getFechaModificacion() ) + "</fc_mod>");
                res.getWriter().write("</datos_objeto>");            
                
            } else {
                res.setContentType("text/html");
                res.setHeader("Cache-Control", "no-cache");
                res.setCharacterEncoding("UTF-8");
                res.getWriter().write("<td align=\"left\" height=\"30\">" + idMail + "</td>");
                res.getWriter().write("<td align=\"left\">" + m.getNombre() + " " + m.getApellido() + "</td>");
                res.getWriter().write("<td align=\"left\">" + m.getMail() + "</td>");
                if (estActivado.equals("1")) {
                    res.getWriter().write("<td align=\"center\">Si&nbsp;</td>");
                } else {
                    res.getWriter().write("<td align=\"center\">No&nbsp;</td>");
                }
                res.getWriter().write("<td align=\"center\">" + Formatos.frmFecha( m.getFechaIngreso() ) + "</td>");
                res.getWriter().write("<td align=\"center\">" + Formatos.frmFecha( m.getFechaModificacion() ) + "</td>");
                res.getWriter().write("<td align=\"center\" nowrap>" +
                					   "<a href=\"javascript:ventanaObjeto('Modificar Mail','" + m.getNombre() + "','" + m.getApellido() + "','" + m.getMail() + "','" + idMail + "','" + m.getActivado() + "');\">" +
                					    "<img src=\"img/editicon.gif\" border=\"0\" height=\"17\" width=\"19\" title=\"Editar Mail\">" +
                					   "</a>&nbsp;" +
                					   "<a href=\"javascript:aEliminar('" + idMail + "');\">" +
                					    "<img src=\"img/trash.gif\" border=\"0\" height=\"16\" width=\"16\" title=\"Eliminar Mail\">" +
                					   "</a>" +
                					  "</td>");            
            }    
        }
        
        logger.debug("Fin AddModMailsSyF [AJAX]");
    }
}
