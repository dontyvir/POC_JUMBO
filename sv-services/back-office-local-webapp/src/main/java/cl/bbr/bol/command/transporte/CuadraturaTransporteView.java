/*
 * Created on 26-mar-2010
 */
package cl.bbr.bol.command.transporte;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.ChoferTransporteDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.PatenteTransporteDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author jdroguett
 */
public class CuadraturaTransporteView extends Command {
   protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
      String html = getServletConfig().getInitParameter("TplFile");
      html = path_html + html;
      View salida = new View(res);
      TemplateLoader load = new TemplateLoader(html);
      ITemplate tem = load.getTemplate();
      IValueSet top = new ValueSet();

      GregorianCalendar hoy = new GregorianCalendar();
      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
      top.setVariable("{fecha_hasta}", sdf.format(hoy.getTime()));
      hoy.add(Calendar.DAY_OF_YEAR, -35);
      top.setVariable("{fecha_desde}", sdf.format(hoy.getTime()));

      BizDelegate biz = new BizDelegate();
      List patentes = biz.getPatentesDeTransporte(0, usr.getId_local());
      List htmlPatentes = new ArrayList();
      for (int i = 0; i < patentes.size(); i++) {
         PatenteTransporteDTO patente = (PatenteTransporteDTO) patentes.get(i);
         IValueSet fila = new ValueSet();
         fila.setVariable("{id}", String.valueOf(patente.getIdPatente()));
         fila.setVariable("{patente}", patente.getPatente());
         htmlPatentes.add(fila);
      }
      top.setDynamicValueSets("PATENTES", htmlPatentes);

      List htmlChoferes = new ArrayList();
      List choferes = biz.getChoferesDeTransporte(0, usr.getId_local());
      for (int i = 0; i < choferes.size(); i++) {
         ChoferTransporteDTO chofer = (ChoferTransporteDTO) choferes.get(i);
         IValueSet fila = new ValueSet();
         fila.setVariable("{id}", String.valueOf(chofer.getIdChofer()));
         fila.setVariable("{chofer}", chofer.getNombre());
         htmlChoferes.add(fila);
      }
      top.setDynamicValueSets("CHOFERES", htmlChoferes);

      List htmlJornadas = new ArrayList();
      List jornadas = biz.getJornadasDespacho((int) usr.getId_local(), hoy.getTime(), hoy.getTime());
      for (int i = 0; i < jornadas.size(); i++) {
         JornadaDTO jor = (JornadaDTO) jornadas.get(i);
         IValueSet fila = new ValueSet();
         fila.setVariable("{id_jornada}", String.valueOf(jor.getId_jornada()));
         fila.setVariable("{jornada}", Formatos.frmHoraSola(jor.getH_inicio()) + " - "
               + Formatos.frmHoraSola(jor.getH_fin()));
         htmlJornadas.add(fila);
      }
      top.setDynamicValueSets("JORNADAS", htmlJornadas);

      top.setVariable("{hdr_nombre}", usr.getNombre() + " " + usr.getApe_paterno());
      top.setVariable("{hdr_local}", usr.getLocal());
      top.setVariable("{hdr_fecha}", new Date());

      String result = tem.toString(top);
      salida.setHtmlOut(result);
      salida.Output();
   }
}
