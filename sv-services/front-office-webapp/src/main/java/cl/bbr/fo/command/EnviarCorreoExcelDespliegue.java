package cl.bbr.fo.command;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import cl.bbr.log.Logging;

public class EnviarCorreoExcelDespliegue extends HttpServlet implements Servlet {

	protected Logging logger = new Logging(this);

	public EnviarCorreoExcelDespliegue() {
		super();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	    ResourceBundle rb = ResourceBundle.getBundle("fo");		
	    try{
		    String pag_exitosa = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_exitosa");
		    
		    TemplateLoader load = new TemplateLoader(pag_exitosa);
			ITemplate tem = load.getTemplate();
			IValueSet param =new ValueSet();
	
			String solicitud = req.getParameter("solicitud");
			String tupla = req.getParameter("tupla");
			String ses_loc_id = req.getParameter("ses_loc_id").toString();
			long ses_cli_id = Long.parseLong(req.getParameter("ses_cli_id").toString());
			long ses_cli_rut = Long.parseLong(req.getParameter("ses_cli_rut").toString());
			String http = rb.getString("mail.lista.productos.www.jumbo");			
			
			String urlDescargaExcel = http + "FO/EnviarCorreoExcel?solicitud="+solicitud+
				"&tupla="+ tupla + 
				"&ses_loc_id=" + ses_loc_id + 
				"&ses_cli_id=" + ses_cli_id + 
				"&ses_cli_rut=" + ses_cli_rut;
			
			param.setVariable("{solicitud}",urlDescargaExcel);
			res.getWriter().write(tem.toString(param));
			
	    }catch (Exception e) {
	        logger.error("metodo doGet",e);
	        res.sendRedirect(rb.getString("command.dir_error"));
	    }
	
	}


}