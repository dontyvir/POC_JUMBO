/*
 * Created on 05-feb-2009
 *
 */
package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author hs proyecto stock online
 *  
 */
public class ViewCatalogacionMasivaForm extends Command {
	
   private final static long serialVersionUID = 1;
	    
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	  res.setCharacterEncoding("UTF-8");
      View salida = new View(res);

      String html = getServletConfig().getInitParameter("TplFile");
      html = path_html + html;
      
      TemplateLoader load = new TemplateLoader(html);
      ITemplate tem = load.getTemplate();
      IValueSet top = new ValueSet();
      
      top.setVariable("{error}","");
      //top.setVariable("{habilitaBoton}","");
      top.setVariable("{loc_5}", "checked");
      top.setVariable("{loc}", "5");
      top.setVariable("{msj}","");
      
		// 6. Setea variables del template
		// variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
      
      String result = tem.toString(top);
      salida.setHtmlOut(result);
      salida.Output();
   }
   
}
