/*
 * Created on 09-nov-2009
 */
package cl.bbr.bol.command.asociar;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.dto.EncargadoDTO;
import cl.bbr.bol.service.PickeadorService;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 *
 */
public class ListaEncargadosView  extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      res.setCharacterEncoding("UTF-8");
      req.setCharacterEncoding("UTF-8");

      int localId = (int) usr.getId_local();
      PickeadorService service = new PickeadorService();
      PrintWriter out = res.getWriter();
      StringBuffer result = new StringBuffer();
      String q = req.getParameter("q");

      List encargados = service.getEncargados(localId, q);

      for (int i = 0; i < encargados.size(); i++) {
         EncargadoDTO encargado = (EncargadoDTO) encargados.get(i); 
         result.append( encargado.getLogin() + "|" + encargado.getNombre() + "\n");
      }
      out.print(result.toString());
   }
}
