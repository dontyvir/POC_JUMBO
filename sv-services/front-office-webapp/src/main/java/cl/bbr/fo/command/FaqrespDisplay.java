package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.faq.dto.FaqPreguntaDTO;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.Tracking_web;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página de Faq Respuestas
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class FaqrespDisplay extends Command {

	/**
	 * Preguntas Frecuentes
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {

		try {
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");			
			
			// Se almacena tracking en este sector
			//Tracking_web.saveTracking("FAQ", arg0);

			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();		
			
			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();

			long idcat = Long.parseLong(arg0.getParameter("cat_id").toString());
			
			if (arg0.getParameter("cat_id") != null){	
				// Instacia del bizdelegate
				BizDelegate biz = new BizDelegate();
				
				// Recuperar litado de respuesta - preguntas frecuentes
				List lista = biz.getFaqPregunta(idcat); 
				List datos = new ArrayList();
	
				for (int i = 0; i < lista.size(); i++) {
					 FaqPreguntaDTO pre = (FaqPreguntaDTO) lista.get(i); 
					 IValueSet fila = new ValueSet(); 
					 fila.setVariable("{pre_id}", pre.getPre_id()+"");
					 fila.setVariable("{pre_cat_id}", pre.getPre_cat_id()+"");
					 fila.setVariable("{pre_pregunta}",pre.getPregunta()+"");
					 fila.setVariable("{pre_respuesta}",pre.getRespuesta()+"");
					 datos.add(fila);
				} 
				top.setDynamicValueSets("respuestas",datos);
			}
			
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
			throw new CommandException(e);
		}

	}

}