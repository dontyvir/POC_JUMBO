package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.clientes.dto.ComunaDTO;
import cl.bbr.jumbocl.common.utils.Utils;

/**
 * Listado de comunas con cobertura
 *  
 * @author imoyano
 *  
 */
public class ComunasConCoberturaByRegion extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
        
        arg1.setHeader("Cache-Control", "no-cache");
        arg0.setCharacterEncoding("UTF-8");
        arg1.setContentType("text/html; charset=iso-8859-15");

		// Se recupera la salida para el servlet
		PrintWriter out = arg1.getWriter();

		// Instacia del bizdelegate
		BizDelegate biz = new BizDelegate();

		// recuperar identificador de la región de consulta
		long idRegion = 0;
		long idComuna = 0;
        
        if (arg0.getParameter("id_region") != null && !arg0.getParameter("id_region").equals("") && Utils.isNumericFO(arg0.getParameter("id_region")) ){
            idRegion = Long.parseLong(arg0.getParameter("id_region"));
        }

        if (arg0.getParameter("id_comuna") != null && !arg0.getParameter("id_comuna").equals("") && Utils.isNumericFO(arg0.getParameter("id_comuna"))){
            idComuna = Long.parseLong(arg0.getParameter("id_comuna"));
        }
        
        //session.setAttribute("ses_comuna_cliente", ""+com.getReg_id()+"-"+com.getId()+"-"+com.getNombre());
        HttpSession session = arg0.getSession();
        if (idRegion == 0 && 
                session.getAttribute("ses_comuna_cliente") != null &&
                !session.getAttribute("ses_comuna_cliente").toString().trim().equals("")){
            String tmp = session.getAttribute("ses_comuna_cliente").toString();
            String[] com = tmp.split("-=-"); //id_region-id_comuna-nom_comuna
            idRegion = Long.parseLong(com[0]);
            idComuna = Long.parseLong(com[1]);
        }

		List comunas = biz.comunasConCoberturaByRegion( idRegion );
        
        if (idComuna > 0){
            out.println("<option value=\"0\">Seleccione</option> \n");
        }else{
            out.println("<option value=\"0\" selected>Seleccione</option> \n");
        }
        
		for (int i = 0; i < comunas.size(); i++) {
			ComunaDTO com = (ComunaDTO) comunas.get(i);
            if (idComuna > 0 && idComuna == com.getId()){
                out.println("<option value=\""+com.getId()+"\" selected>"+ Utils.printNombreFO( com.getNombre() ) +"</option> \n");
                session.setAttribute("ses_comuna_cliente", ""+com.getReg_id()+"-=-"+com.getId()+"-=-"+com.getNombre());
            }else{
                out.println("<option value=\""+com.getId()+"\">"+ Utils.printNombreFO( com.getNombre() ) +"</option> \n");
            }
		}
	}
}