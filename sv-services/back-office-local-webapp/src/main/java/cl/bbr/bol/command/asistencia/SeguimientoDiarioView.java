/*
 * Created on 12-nov-2009
 */
package cl.bbr.bol.command.asistencia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import cl.bbr.bol.dto.AsistenciaDTO;
import cl.bbr.bol.dto.EncargadoDTO;
import cl.bbr.bol.service.PickeadorService;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 */
public class SeguimientoDiarioView extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      String html = getServletConfig().getInitParameter("TplFile");
      html = path_html + html;
      View salida = new View(res);
      TemplateLoader load = new TemplateLoader(html);
      ITemplate tem = load.getTemplate();
      IValueSet top = new ValueSet();

      int localId = (int) usr.getId_local();
      
      PickeadorService service = new PickeadorService(); 
      List encargados = service.getEncargados(localId);
      List asistencias = service.getAsistencias();
      List vEncargados = vistaEncargados(encargados);
      List vAsistencias= vistaAsistencias(asistencias);
      String sAsistencias = vistaSAsistencias(asistencias);
      top.setDynamicValueSets("ENCARGADOS", vEncargados);
      top.setDynamicValueSets("ASISTENCIAS", vAsistencias);
      top.setVariable("{fecha}", Formatos.fecha(new Date()));
      top.setVariable("{sAsistencias}", sAsistencias);
      
      top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno() );
      top.setVariable("{hdr_local}", usr.getLocal());
      top.setVariable("{hdr_fecha}", new Date());
      String result = tem.toString(top);
      salida.setHtmlOut(result);
      salida.Output();
   }
   
   /**
    * @param asistencias
    * @return
    */
   private String vistaSAsistencias(List asistencias) {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < asistencias.size(); i++) {
         AsistenciaDTO dto = (AsistenciaDTO) asistencias.get(i);
         sb.append(dto.getNombre()+"-");
      }
      return sb.toString();
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
   
   private List vistaAsistencias(List asistencias){
      List lista = new ArrayList();

      for (int i = 0; i < asistencias.size(); i++) {
         if(i > 0 && i % 4 ==  0){
            IValueSet asis = new ValueSet();
            asis.setVariable("{nombre}", "");
            asis.setVariable("{descripcion}", "</div><div style=\"float:left;padding: 0px 20px;\">");
            lista.add(asis);
         }
         AsistenciaDTO dto = (AsistenciaDTO) asistencias.get(i);
         IValueSet asis = new ValueSet();
         asis.setVariable("{nombre}", dto.getNombre());
         asis.setVariable("{descripcion}", dto.getDescripcion());
         lista.add(asis);
      }
      return lista;
   }
}
