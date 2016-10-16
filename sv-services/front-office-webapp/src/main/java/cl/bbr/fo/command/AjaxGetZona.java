package cl.bbr.fo.command;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.clientes.dto.ComunaDTO;

/**
 * Trae la zona correspondiente a la comuna y poligono nro. 0
 *  
 * @author carriagada it4b
 *  
 */
public class AjaxGetZona extends Command {

	/**
	 * Trae la zona correspondiente a la comuna y poligono nro. 0
	 * 
	 * @param arg0	Request recibido desde el navegador
	 * @param arg1	Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
	    
	    arg1.setHeader("Cache-Control", "no-cache");
        arg0.setCharacterEncoding("UTF-8");
        arg1.setContentType("text/html; charset=utf-8");
        
        //Se recupera la salida para el servlet
        PrintWriter out = arg1.getWriter();
        
        // Instacia del bizdelegate
		BizDelegate biz = new BizDelegate();

		// recuperar identificador de la región de consulta
		long com_id = Long.parseLong(arg0.getParameter("id_comuna"));
        ComunaDTO comuna = biz.getZonaxComuna(com_id);
        
        arg1.setContentType("text/xml");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("UTF-8");
        
        String result = "<respuesta>";
        result += "<idlocal>" + comuna.getLocal_id() + "</idlocal>";
        result += "<idzona>" + comuna.getZona_id() + "</idzona>";
        result += "</respuesta>";
        
		out.print(result);
	}

}