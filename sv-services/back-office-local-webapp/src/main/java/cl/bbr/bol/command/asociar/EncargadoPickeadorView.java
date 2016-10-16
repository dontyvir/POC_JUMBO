/*
 * Created on 06-nov-2009
 */
package cl.bbr.bol.command.asociar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import cl.bbr.bol.dto.EncargadoDTO;
import cl.bbr.bol.service.PickeadorService;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 */
public class EncargadoPickeadorView extends Command {

   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      logger.debug("Local del usuario: " + usr.getId_local());
      int localId = (int) usr.getId_local();
      String html = getServletConfig().getInitParameter("TplFile");
      html = path_html + html;
      View salida = new View(res);
      TemplateLoader load = new TemplateLoader(html);
      ITemplate tem = load.getTemplate();
      IValueSet top = new ValueSet();

      //Perfil encargado de sección: 63

      PickeadorService service = new PickeadorService();

      List encargados = service.getEncargados(localId);

      List vEncargados = vistaEncargados(encargados);
      top.setDynamicValueSets("ENCARGADOS", vEncargados);

      top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno() );
      top.setVariable("{hdr_local}", usr.getLocal());
      top.setVariable("{hdr_fecha}", new Date());

      String result = tem.toString(top);
      salida.setHtmlOut(result);
      salida.Output();
   }

   private List vistaEncargados(List encargados) {
      List lista = new ArrayList();

      for (int i = 0; i < encargados.size(); i++) {
         EncargadoDTO dto = (EncargadoDTO) encargados.get(i);
         IValueSet enc = new ValueSet();
         enc.setVariable("{id}", dto.getId() + "");
         enc.setVariable("{login}", dto.getLogin());
         enc.setVariable("{nombre}", dto.getNombre());
         enc.setVariable("{nPickeadores}", dto.getPickeadoresAsociados() + "");
         lista.add(enc);
      }
      return lista;
   }

}
