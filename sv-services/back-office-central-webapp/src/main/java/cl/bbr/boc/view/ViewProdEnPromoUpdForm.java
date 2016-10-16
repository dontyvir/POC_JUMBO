/*
 * Created on 05-feb-2009
 *
 */
package cl.bbr.boc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 *  
 */
public class ViewProdEnPromoUpdForm extends Command {

	private static final long serialVersionUID = 8738124570813496532L;

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	  res.setCharacterEncoding("UTF-8");
      View salida = new View(res);

      String html = getServletConfig().getInitParameter("TplFile");
      html = path_html + html;

      TemplateLoader load = new TemplateLoader(html);
      ITemplate tem = load.getTemplate();
      IValueSet top = new ValueSet();

      String result = tem.toString(top);
      salida.setHtmlOut(result);
      salida.Output();
   }
   
}