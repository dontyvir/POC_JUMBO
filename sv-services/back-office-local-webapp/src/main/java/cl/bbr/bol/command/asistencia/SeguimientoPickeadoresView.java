/*
 * Created on 12-nov-2009
 */
package cl.bbr.bol.command.asistencia;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.dto.AsistenciaPickeadorDTO;
import cl.bbr.bol.dto.PickeadorDTO;
import cl.bbr.bol.service.PickeadorService;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 */
public class SeguimientoPickeadoresView extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      res.setCharacterEncoding("UTF-8");
      req.setCharacterEncoding("UTF-8");
      String html = getServletConfig().getInitParameter("TplFile");
      html = path_html + html;
      View salida = new View(res);
      TemplateLoader load = new TemplateLoader(html);
      ITemplate tem = load.getTemplate();
      IValueSet top = new ValueSet();

      int localId = (int) usr.getId_local();
      int encargadoId = Integer.parseInt(req.getParameter("encargadoId"));
      Date fecha = null;
      String sFecha = req.getParameter("fecha");
      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
      if(sFecha == null)
         fecha = new Date();//hoy
      else
         fecha = sdf.parse(sFecha);
      
      PickeadorService service = new PickeadorService();
      List pickeadores = service.getAsistenciasPickeadores(localId, encargadoId, fecha);
      List vPickeadores = vistaSeguimiento(pickeadores);

      top.setDynamicValueSets("PICKEADORES", vPickeadores);
      String result = tem.toString(top);
      salida.setHtmlOut(result);
      salida.Output();

   }

   private List vistaSeguimiento(List pickeadores) {
      List lista = new ArrayList();

      for (int i = 0; i < pickeadores.size(); i++) {
         PickeadorDTO dto = (PickeadorDTO) pickeadores.get(i);
         IValueSet pick = new ValueSet();
         pick.setVariable("{id}", dto.getId() + "");
         pick.setVariable("{login}", dto.getLogin());
         pick.setVariable("{nombre}", dto.getNombre());
         pick.setVariable("{apellido}", dto.getApellido());
         AsistenciaPickeadorDTO asis = dto.getAsistencia();
         pick.setVariable("{asistencia_id}", asis.getId() == 0 ? "" : asis.getId() + "");
         pick.setVariable("{asistencia}", asis.getAsistencia() == null ? "" : asis.getAsistencia());

         lista.add(pick);
      }
      return lista;
   }
}
