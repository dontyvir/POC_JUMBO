/*
 * Created on 09-nov-2009
 */
package cl.bbr.bol.command.asociar;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import cl.bbr.bol.dto.PickeadorDTO;
import cl.bbr.bol.service.PickeadorService;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.jumbocl.usuarios.service.UsuariosService;

/**
 * @author jdroguett
 *  
 */
public class PickeadoresPorEncargadoView extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      res.setCharacterEncoding("UTF-8");
      req.setCharacterEncoding("UTF-8");
      
      UsuariosService usuariosService = new UsuariosService();
      String login = req.getParameter("loginEncargado");
      UserDTO userDTO = usuariosService.getUserByLogin(login);
      
      PrintWriter out = res.getWriter();
      
      if (userDTO != null) {
         PickeadorService service = new PickeadorService();
         int localId = (int) usr.getId_local();
         String html = getServletConfig().getInitParameter("TplFile");
         html = path_html + html;
         TemplateLoader load = new TemplateLoader(html);
         ITemplate tem = load.getTemplate();
         IValueSet top = new ValueSet();

         int encargadoId = (int) userDTO.getId_usuario();
         List pickeadores = service.getPickeadoresPorEncargado(localId, encargadoId);
         List vPickeadores = vistaPrickeadores(pickeadores);
         top.setVariable("{encargadoId}", encargadoId + "");
         top.setDynamicValueSets("PICKEADORES", vPickeadores);
         out.print(tem.toString(top));
      }else
         out.print("Encargado no existe");
         

      
   }

   private List vistaPrickeadores(List pickeadores) {
      List lista = new ArrayList();

      for (int i = 0; i < pickeadores.size(); i++) {
         PickeadorDTO dto = (PickeadorDTO) pickeadores.get(i);
         IValueSet pick = new ValueSet();
         pick.setVariable("{id}", "" + dto.getId());
         pick.setVariable("{login}", dto.getLogin());
         pick.setVariable("{nombre}", dto.getNombre());
         pick.setVariable("{apellido}", dto.getApellido());
         pick.setVariable("{checked}", dto.isAsignado() ? "checked" : "");
         lista.add(pick);
      }
      return lista;
   }
}
