package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.clientes.dto.ComunaDTO;

/**
 * Listado de comunas (to select DOM) por region [AJAX]
 *  
 * @author imoyano
 *  
 */
public class ComunasByRegion extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {

		// Se recupera la salida para el servlet
		PrintWriter out = arg1.getWriter();

		// Instacia del bizdelegate
		BizDelegate biz = new BizDelegate();

		// recuperar identificador de la región de consulta
		long idRegion = Long.parseLong(arg0.getParameter("id_region"));
		
		List comunas = biz.regionesGetAllComunas( idRegion );
        
        arg1.setContentType("text/html");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("ISO-8859-1");
		
        out.println("<option value=\"0\" selected>Comuna</option>");
		for (int i = 0; i < comunas.size(); i++) {
			ComunaDTO com = (ComunaDTO) comunas.get(i);
			out.println("<option value=\""+com.getNombre()+"\">"+com.getNombre()+"</option>");
		}
	}
}