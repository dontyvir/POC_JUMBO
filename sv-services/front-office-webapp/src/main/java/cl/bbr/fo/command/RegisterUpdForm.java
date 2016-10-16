package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;

import cl.bbr.fo.dto.AlternativaDTO;
import cl.bbr.fo.dto.PreguntaDTO;
import cl.bbr.fo.service.DatosClienteService;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Actualiza datos personales de los clientes
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class RegisterUpdForm extends Command {

   /**
    * Actualiza datos persona de los cliente
    * 
    * @param arg0
    *           Request recibido desde el navegador
    * @param arg1
    *           Response recibido desde el navegador
    * @throws Exception
    */
   protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {

      // Carga properties
      ResourceBundle rb = ResourceBundle.getBundle("fo");

      // Recupera la sesión del usuario
      HttpSession session = arg0.getSession();

      // Se recupera la salida para el servlet
      PrintWriter out = arg1.getWriter();

      // Recupera parámetro desde web.xml
      String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");

      // Template de despliegue
      TemplateLoader load = new TemplateLoader(pag_form);
      ITemplate tem = load.getTemplate();

      IValueSet top = new ValueSet();

      ArrayList arr_dias = new ArrayList();
      for (int i = 1; i <= 31; i++) {
         IValueSet fila = new ValueSet();
         fila.setVariable("{option_dia}", i + "");
         arr_dias.add(fila);
      }
      top.setDynamicValueSets("select_dias", arr_dias);

      ArrayList arr_anos = new ArrayList();
      Calendar ahora = Calendar.getInstance();
      for (int i = 1900; i <= ahora.get(Calendar.YEAR); i++) {
         IValueSet fila = new ValueSet();
         fila.setVariable("{option_ano}", i + "");
         arr_anos.add(fila);
      }
      top.setDynamicValueSets("select_anos", arr_anos);

      // Instacia del bizdelegate
      BizDelegate biz = new BizDelegate();

      //Solo para el caso que exista id del ejecutivo
      if (session.getAttribute("ses_eje_id") != null) {
         top.setVariable("{claves_eje}", "readonly");
         top.setVariable("{msg_claves_eje}", rb.getString("registerupform.msg_clave_ejecutivo"));
      } else {
         top.setVariable("{msg_claves_eje}", "");
         top.setVariable("{claves_eje}", "");
      }

      Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());

      ClienteDTO cliente = (ClienteDTO) biz.clienteGetById(cliente_id.longValue());

      top.setVariable("{rut}", cliente.getRut() + "");
      top.setVariable("{dv}", cliente.getDv() + "");
      top.setVariable("{nombre}", cliente.getNombre() + "");
      top.setVariable("{ape_pat}", cliente.getApellido_pat() + "");
      top.setVariable("{ape_mat}", cliente.getApellido_mat() + "");
      Calendar cal = new GregorianCalendar();
      cal.setTimeInMillis(cliente.getFec_nac());
      top.setVariable("{auxmes}", (cal.get(Calendar.MONTH) + 1) + "");
      top.setVariable("{auxano}", cal.get(Calendar.YEAR) + "");
      top.setVariable("{auxdia}", cal.get(Calendar.DAY_OF_MONTH) + "");

      if (cliente.getGenero().compareTo("F") == 0) {
         top.setVariable("{checkedF}", "checked=\"checked\"");
         top.setVariable("{checkedM}", "");
      } else {
         top.setVariable("{checkedM}", "checked=\"checked\"");
         top.setVariable("{checkedF}", "");
      }

      int arroba_pos = (cliente.getEmail() + "").indexOf("@");
      top.setVariable("{email1}", (cliente.getEmail() + "").substring(0, arroba_pos));
      top.setVariable("{dominio1}", (cliente.getEmail() + "").substring(arroba_pos + 1, (cliente.getEmail() + "")
            .length()));

      top.setVariable("{email2}", (cliente.getEmail() + "").substring(0, arroba_pos));
      top.setVariable("{dominio2}", (cliente.getEmail() + "").substring(arroba_pos + 1, (cliente.getEmail() + "")
            .length()));

      top.setVariable("{fon_cod_1}", cliente.getFon_cod_1() + "");
      top.setVariable("{auxcodfon1}", cliente.getFon_cod_1() + "");
      top.setVariable("{fon_num_1}", cliente.getFon_num_1() + "");
      top.setVariable("{fon_cod_2}", cliente.getFon_cod_2() + "");
      top.setVariable("{auxcodfon2}", cliente.getFon_cod_2() + "");
      top.setVariable("{fon_num_2}", cliente.getFon_num_2() + "");
      top.setVariable("{fon_num_3}", cliente.getFon_num_3() + "");
      top.setVariable("{pregunta}", cliente.getPregunta() + "");
      top.setVariable("{respuesta}", cliente.getRespuesta() + "");

      if (cliente.getRecibeSms() == 0) {
         top.setVariable("{sms_checked}", "");
      } else {
         top.setVariable("{sms_checked}", "checked=\"checked\"");
      }

      DatosClienteService serv = new DatosClienteService();
      List preguntas = serv.getPreguntas(cliente_id.intValue());
      List dependencia = serv.getDependencia();

      List vistaDep = vistaDependencia(dependencia);

      List vista = vistaPreguntas(preguntas);
      top.setDynamicValueSets("PREGUNTAS", vista);
      top.setDynamicValueSets("DEPENDENCIA", vistaDep);

      String result = tem.toString(top);

      out.print(result);
   }

   /**
    * @param dependencia
    * @return
    */
   private List vistaDependencia(List dependencia) {
      List lista = new ArrayList();
      for (int i = 0; i < dependencia.size(); i++) {
         int[] dep = (int[]) dependencia.get(i);
         IValueSet val = new ValueSet();
         val.setVariable("{pre_id}", dep[0] + "");
         val.setVariable("{depende_alt_id}", dep[1] + "");
         val.setVariable("{depende_pre_id}", dep[2] + "");
         lista.add(val);
      }
      return lista;
   }

   private List vistaPreguntas(List preguntas) {
      List lista = new ArrayList();
      for (int i = 0; i < preguntas.size(); i++) {
         PreguntaDTO pre = (PreguntaDTO) preguntas.get(i);
         IValueSet vPre = new ValueSet();
         vPre.setVariable("{pre_id}", pre.getId() + "");
         vPre.setVariable("{pre_enunciado}", pre.getEnunciado());
         vPre.setVariable("{ocultar}", (pre.isOcultar() ? "style=\"display: none;\"" : ""));

         IValueSet vControl = new ValueSet();
         vControl.setVariable("{pre_id}", pre.getId() + "");
         vControl.setVariable("{respuesta}", pre.getSRespuesta());
         vControl.setVariable("{ocultar}", (pre.isOcultar() ? "style=\"display: none;\"" : ""));

         List alternativas = pre.getAlternativas();
         List listaAlt = new ArrayList();
         for (int j = 0; j < alternativas.size(); j++) {
            AlternativaDTO alt = (AlternativaDTO) alternativas.get(j);
            IValueSet vAlt = new ValueSet();
            vAlt.setVariable("{pre_id}", pre.getId() + "");
            vAlt.setVariable("{alt_id}", alt.getId() + "");
            vAlt.setVariable("{alt_enunciado}", alt.getEnunciado());
            vAlt.setVariable("{alt_estado}", (alt.isElegida() ? pre.getEstado() : ""));
            listaAlt.add(vAlt);
         }
         vControl.setDynamicValueSets("ALTERNATIVAS", listaAlt);
         List listaC = new ArrayList();
         listaC.add(vControl);
         vPre.setDynamicValueSets(pre.getControl().toUpperCase(), listaC);
         lista.add(vPre);
      }
      return lista;
   }
}