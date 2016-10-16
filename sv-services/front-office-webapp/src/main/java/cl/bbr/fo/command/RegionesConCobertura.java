package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.clientes.dto.RegionesDTO;
import cl.bbr.jumbocl.common.utils.Utils;

/**
 * Listado de regiones con cobertura
 *  
 * @author imoyano
 *  
 */
public class RegionesConCobertura extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
        
        arg1.setHeader("Cache-Control", "no-cache");
        arg0.setCharacterEncoding("UTF-8");
        arg1.setContentType("text/html; charset=iso-8859-15");

		// Se recupera la salida para el servlet
		PrintWriter out = arg1.getWriter();
        long id_region = 0;
        if (arg0.getParameter("id_region") != null && !((String) arg0.getParameter("id_region")).equals("")){
            id_region = Long.parseLong(arg0.getParameter("id_region"));
        }
        
        //session.setAttribute("ses_comuna_cliente", ""+com.getReg_id()+"-"+com.getId()+"-"+com.getNombre());
        HttpSession session = arg0.getSession();
        if (id_region == 0 && 
                session.getAttribute("ses_comuna_cliente") != null && 
                !session.getAttribute("ses_comuna_cliente").toString().trim().equals("")){
            String tmp = session.getAttribute("ses_comuna_cliente").toString();
            String[] com = tmp.split("-=-"); //id_region-id_comuna-nom_comuna
            id_region = Long.parseLong(com[0]);
        }
        
        
		// Instacia del bizdelegate
		BizDelegate biz = new BizDelegate();
		
		List regiones = biz.regionesConCobertura();
        if (id_region > 0){
            out.println("<option value=\"0\">Seleccione</option> \n");
        }else{
            out.println("<option value=\"0\" selected>Seleccione</option> \n");
        }
        
		for (int i = 0; i < regiones.size(); i++) {
			RegionesDTO reg = (RegionesDTO) regiones.get(i);
            if (id_region > 0 && id_region == reg.getId()){
                out.println("<option value=\""+reg.getId()+"\" selected>"+ Utils.printNombreFO( reg.getNombre() )+"</option> \n");
            }else{
                out.println("<option value=\""+reg.getId()+"\">"+ Utils.printNombreFO( reg.getNombre() )+"</option> \n");
            }
		}
	}
}