/*
 * Created on 20-may-2010
 */
package cl.bbr.boc.command;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 */
public class PreciosIndex extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
       res.setCharacterEncoding("UTF-8");
       res.setHeader("Cache-Control", "no-cache");
       PrintWriter salida = res.getWriter();
       String html = path_html + "precios/index.html";
       String layout = path_html + "precios/layout.html";
       ITemplate template = (new TemplateLoader(layout)).getTemplate();
       ITemplate temHtml = (new TemplateLoader(html)).getTemplate();
       //pagina
       IValueSet top = new ValueSet();
       top.setVariable("{test}", "hola mundo");
       String result = temHtml.toString(top);
       //end pagina
       
       IValueSet yield = new ValueSet();
       yield.setVariable("{yield}", result);
       salida.print(template.toString(yield));
   }
}
