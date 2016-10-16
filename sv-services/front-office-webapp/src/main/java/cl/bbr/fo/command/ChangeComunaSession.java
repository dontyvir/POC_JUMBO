package cl.bbr.fo.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.clientes.dto.ComunaDTO;
import cl.bbr.jumbocl.clientes.dto.RegionesDTO;
import cl.bbr.jumbocl.common.dto.RegionDTO;

/**
 * cambia en session la comuna
 *  
 * @author imoyano
 *  
 */
public class ChangeComunaSession extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		arg0.setCharacterEncoding("UTF-8");
        String mensajeSistema = "OK";
		String nombreComuna = "";
        long idComuna = 0;
        try {
            BizDelegate biz = new BizDelegate();

            // recuperar identificador de la región de consulta
            idComuna = Long.parseLong(arg0.getParameter("id_comuna"));
            if (!(arg0.getParameter("nombre_comuna")==null)){
            	nombreComuna= arg0.getParameter("nombre_comuna").toString();
            }
            HttpSession session = arg0.getSession();
            
            if (session.getAttribute("ses_cli_id") == null || session.getAttribute("ses_cli_rut") == null ) {
				toMakeDonaldSession(session);				
				//Crea la sesion en la BD
				String id_sess= (String)session.getAttribute("ses_id");                	
				int invitado_id = biz.crearSesionInvitado(id_sess);
				session.setAttribute("ses_invitado_id", invitado_id + "");
				logger.info("Se inicia session invitado: " + session.getId() + " IP: ["+arg0.getRemoteAddr()+"]");
            }
            
            ComunaDTO com = biz.getComunaConLocal(idComuna);
            RegionesDTO reg = biz.getRegionById((int)com.getReg_id());
            
            session.setAttribute("ses_loc_id", ""+com.getLocal_id());
            session.setAttribute("ses_zona_id", ""+com.getZona_id());
            //session.setAttribute("ses_comuna_cliente", ""+com.getReg_id()+"-"+com.getId()+"-"+com.getNombre());
            if ((nombreComuna.equalsIgnoreCase("undefined"))||(nombreComuna.equals(""))){
              nombreComuna = com.getNombre();
            }
            session.setAttribute("ses_comuna_cliente", ""+com.getReg_id()+"-=-"+com.getId()+"-=-"+nombreComuna);
            
            if (session.getAttribute("region") != null){
                session.setAttribute("region", ""+com.getReg_id());
            }
            if (session.getAttribute("comuna") != null){
                session.setAttribute("comuna", ""+com.getId());
            }

        } catch (Exception e) {
            mensajeSistema = "Error al rescatar la comuna";
        }
        
        arg1.setContentType("text/xml");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("UTF-8");
        
        //vemos cual es el mensaje a desplegar
        
        arg1.getWriter().write("<comuna>");
        arg1.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
        arg1.getWriter().write("<nombre_comuna>" + nombreComuna + "</nombre_comuna>");
        arg1.getWriter().write("<id_comuna>" + idComuna + "</id_comuna>");
        arg1.getWriter().write("</comuna>");
	}
}