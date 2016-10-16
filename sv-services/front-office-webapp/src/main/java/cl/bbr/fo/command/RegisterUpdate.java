package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.dto.RespuestaDTO;
import cl.bbr.fo.service.DatosClienteService;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.common.utils.Utils;

/**
 * Modifica el registro del cliente
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class RegisterUpdate extends Command {

   protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
      // Se recupera la salida para el servlet
      PrintWriter out = arg1.getWriter();

      try {

         // Recupera la sesión del usuario
         HttpSession session = arg0.getSession();

         Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());

         ClienteDTO cli = new ClienteDTO();

         // Instacia del bizdelegate
         BizDelegate biz = new BizDelegate();

         // Recuperar todos los datos del cliente desde el repositorio
         cli = biz.clienteGetById(cliente_id.longValue());

         // Se valida si las claves coinciden
         if (arg0.getParameter("clave_ant").length() > 0) {
            String clave_ant = arg0.getParameter("clave_ant").toString();
            if (cli.getClave().compareTo(Utils.encriptarFO(clave_ant)) != 0) {
               this.getLogger().error("Problemas con clave -> diferentes");
               out.print("2");
               return;
            }
         }
         if (Long.parseLong(arg0.getParameter("rut").toString()) != cli.getRut() ) {
             this.getLogger().error("El rut por seguridad debe ser el mismo");
             out.print("2");
             return;
         }

         // Si el cliente ha modificado sus apellidos => marcar
         if (cli.getApellido_pat().compareTo(arg0.getParameter("ape_pat").toString()) != 0
               || cli.getApellido_mat().compareTo(arg0.getParameter("ape_mat").toString()) != 0)
            cli.setCli_mod_dato("01");
         else
            cli.setCli_mod_dato("00");

         // Modificar los datos que vienen desde el argumento de la página
         cli.setNombre(arg0.getParameter("nombre").toString());
         cli.setApellido_pat(arg0.getParameter("ape_pat").toString());
         cli.setApellido_mat(arg0.getParameter("ape_mat").toString());
         int ano = Integer.parseInt(arg0.getParameter("ano"));
         int mes = Integer.parseInt(arg0.getParameter("mes")) - 1;
         int dia = Integer.parseInt(arg0.getParameter("dia"));
         // Para fecha de nacimiento
         Calendar cal = new GregorianCalendar(ano, mes, dia);
         long fec_nac = cal.getTimeInMillis();
         cli.setFec_nac(fec_nac);
         cli.setGenero(arg0.getParameter("genero").toString());
         cli.setEmail(arg0.getParameter("email1").toString() + "@" + arg0.getParameter("dominio1"));
         cli.setFon_cod_1(arg0.getParameter("fon_cod_1").toString());
         cli.setFon_num_1(arg0.getParameter("fon_num_1").toString());
         cli.setFon_cod_2(arg0.getParameter("fon_cod_2").toString());
         cli.setFon_num_2(arg0.getParameter("fon_num_2").toString());
         cli.setFon_cod_3(arg0.getParameter("fon_cod_3").toString());
         cli.setFon_num_3(arg0.getParameter("fon_num_3").toString());
         if (arg0.getParameter("clave") != null && arg0.getParameter("clave").compareTo("") != 0) {
            cli.setClave(Utils.encriptarFO(arg0.getParameter("clave")));
         }
         cli.setPregunta(arg0.getParameter("pregunta").toString());
         cli.setRespuesta(arg0.getParameter("respuesta").toString());
         if (arg0.getParameter("sms") != null) {
            cli.setRecibeSms(Integer.parseInt(arg0.getParameter("sms").toString()));
         } else {
            cli.setRecibeSms(0);
         }

         session.setAttribute("ses_cli_nombre", cli.getNombre() + " " + cli.getApellido_pat());
         session.setAttribute("ses_cli_nombre_pila", cli.getNombre());

         biz.clienteUpdate(cli);

         List respuestas = obtenerRespuestas(arg0);
         DatosClienteService serv = new DatosClienteService();
         serv.updateRespuestas(respuestas, cliente_id.intValue());

         out.print("1");

      } catch (Exception e) {
         e.printStackTrace();
         this.getLogger().error(e);
         out.print("0");
      }

   }

   /**
    * @param req
    */
   private List obtenerRespuestas(HttpServletRequest req) {
      List respuestas = new ArrayList();
      Enumeration enu = req.getParameterNames();
      while (enu.hasMoreElements()) {
         String ele = (String) enu.nextElement();
         if (ele.startsWith("pre_")) {
            String s[] = ele.split("_");
            RespuestaDTO resp = new RespuestaDTO();
            resp.setControl(s[1].toUpperCase());
            resp.setPreguntaId(Integer.parseInt(s[2]));

            if ("NUMERO".equals(resp.getControl())) {
               String val = req.getParameter(ele);
               try {
                  resp.setRespuesta(Integer.parseInt(val));
               } catch (Exception e) {
                  //puede que no haya respondido ya que es opcional
                  resp.setRespuesta(-1); //-1 == null en base de datos
               }
            } else if ("FECHA".equals(resp.getControl())) {
               String val = req.getParameter(ele);
               try {
                  resp.setFecha(val);
               } catch (Exception e) {
                  //puede que no haya respondido ya que es opcional
                  resp.setRespuesta(-1);
               }
            } else if ("COMBOBOX".equals(resp.getControl())) {
               String val = req.getParameter(ele);
               //si es 0 => no hay respuesta
               int[] alts = { Integer.parseInt(val) };
               resp.setAlternativasId(alts);
            } else if ("CHECKBOX".equals(resp.getControl())) {
               String val[] = req.getParameterValues(ele);
               int[] alts = new int[val.length];
               for (int i = 0; i < val.length; i++) {
                  alts[i] = Integer.parseInt(val[i]);
               }
               resp.setAlternativasId(alts);
            }
            respuestas.add(resp);
         }
      }
      return respuestas;
   }
}