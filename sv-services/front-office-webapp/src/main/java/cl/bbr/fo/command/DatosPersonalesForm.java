/*
 * Created on 14/05/2012
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.bbr.fo.command;

/**
 * @author jumbotest
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.dto.AlternativaDTO;
import cl.bbr.fo.dto.PreguntaDTO;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.service.DatosClienteService;
import cl.bbr.fo.util.ComunasRegionesTexto;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.RegionesDTO;
import cl.bbr.jumbocl.clientes.dto.TipoCalleDTO;

/**
 * Presenta el formulario para la modificacion de direccion del cliente
 * 
 * @author BBRI
 *  
 */

public class DatosPersonalesForm extends Command {

	/**
	 * Presenta el formulario de direcciones de despacho.
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {
		
		try {
			
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");			
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();			
						
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter(); 
	
			// Recupera parámetro desde web.xml
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			
			// Template de despliegue Listado tipo de calle
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
		      // 05/10/2012 : INICIO COREMETRICS {MARIO LOAIZA: mario.loaiza@magnotechnology.com} 				
		      top.setVariable("{id_cliente}", session.getAttribute("ses_cli_id").toString());
		      //-05/10/2012 : INICIO COREMETRICS {MARIO LOAIZA: mario.loaiza@magnotechnology.com} 				
              top.setVariable("{rut}", cliente.getRut() + "");
              top.setVariable("{dv}", cliente.getDv() + "");
              if (cliente.getNombre().equals("Invitado"))
                  top.setVariable("{nombre}", "");
              else
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

              
              String[] listCod = {
                    "5", "6", "7", "8", "9",
                    "02", "32", "33", "34", "35", "41", 
                    "42", "43", "45", "51", "52", "53", 
                    "55", "57", "58", "61", "63", "64", 
                    "65", "67", "71", "72", "73", "75"};
            ArrayList listadoCodigos = new ArrayList();
            String cod_telefono = cliente.getFon_cod_1();
            for(int i=0; i<listCod.length; i++){
                IValueSet listCodFono = new ValueSet();
                listCodFono.setVariable("{cod_fono}" , listCod[i]);

            	if(cod_telefono.equals(listCod[i])){
                    listCodFono.setVariable("{cod_selected}" , "selected");
                }else{
                    listCodFono.setVariable("{cod_selected}" ,"");
                }
                listadoCodigos.add(listCodFono);
            }
            top.setDynamicValueSets("CODIGOS_TELEFONOS1", listadoCodigos);

		      top.setVariable("{auxcodfon1}", cliente.getFon_cod_1() + "");
              if (cliente.getFon_num_1() == null)
                  top.setVariable("{fon_num_1}", "");
              else
                  top.setVariable("{fon_num_1}", cliente.getFon_num_1() + "");
		      top.setVariable("{fon_cod_2}", cliente.getFon_cod_2() + "");

				listadoCodigos = new ArrayList();
				cod_telefono = cliente.getFon_cod_2();
	            for(int i=0; i<listCod.length; i++){
	                IValueSet listCodFono = new ValueSet();
	                listCodFono.setVariable("{cod_fono}" , listCod[i]);

	            	if(cod_telefono.equals(listCod[i])){
	                    listCodFono.setVariable("{cod_selected}" , "selected");
	                }else{
	                    listCodFono.setVariable("{cod_selected}" ,"");
	                }
	                listadoCodigos.add(listCodFono);
	            }
	            top.setDynamicValueSets("CODIGOS_TELEFONOS2", listadoCodigos);

		      top.setVariable("{auxcodfon2}", cliente.getFon_cod_2() + "");
              if (cliente.getFon_num_2() == null)
                  top.setVariable("{fon_num_2}", "");
              else
                  top.setVariable("{fon_num_2}", cliente.getFon_num_2() + "");
		      top.setVariable("{fon_num_3}", cliente.getFon_num_3() + "");
		      top.setVariable("{pregunta}", cliente.getPregunta() + "");
		      top.setVariable("{respuesta}", cliente.getRespuesta() + "");

		      if (cliente.getRecibeSms() == 0) {
		         top.setVariable("{sms_checked}", "");
		      } else {
		         top.setVariable("{sms_checked}", "checked=\"checked\"");
		      }

				if (cliente.getRecibeEMail() == 0) {
				   top.setVariable("{email_checked}", "");
				} else {
				   top.setVariable("{email_checked}", "checked=\"checked\"");
				}
				
		      DatosClienteService serv = new DatosClienteService();
		      List preguntas = serv.getPreguntas(cliente_id.intValue());
		      List dependencia = serv.getDependencia();

		      List vistaDep = vistaDependencia(dependencia);

		      List vista = vistaPreguntas(preguntas);
		      top.setDynamicValueSets("PREGUNTAS", vista);
		      top.setDynamicValueSets("DEPENDENCIA", vistaDep);
			
			//Inicio datos de direcciones
			// Revisión de errores
			if( session.getAttribute("cod_error") != null ) {
				top.setVariable("{error}", "1");
				top.setVariable("{mensaje_error}", rb.getString("general.mensaje.error") + " (" + session.getAttribute("cod_error") + ")");
				session.removeAttribute("cod_error");
				top.setVariable("{alias}", arg0.getParameter("alias"));
				top.setVariable("{calle}", arg0.getParameter("calle"));
				top.setVariable("{numero}", arg0.getParameter("numero"));
				top.setVariable("{departamento}", arg0.getParameter("departamento"));
				top.setVariable("{comentario}", arg0.getParameter("comentario"));
				top.setVariable("{_com_inicial}", "0");
			}
			else {
				top.setVariable("{error}", "0");
				top.setVariable("{mensaje_error}", "" );
				top.setVariable("{alias}", "");
				top.setVariable("{calle}", "");
				top.setVariable("{numero}", "");
				top.setVariable("{departamento}", "");
				top.setVariable("{comentario}", "ej: timbre malo");		
				top.setVariable("{_com_inicial}", "1");
			}
			
			// Listado de tipos de calle
			ArrayList arr_tipocalle = new ArrayList();
			List registros = biz.tiposCalleGetAll();
			for (int i = 0; i < registros.size(); i++) {
				TipoCalleDTO reg_tc = (TipoCalleDTO) registros.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{option_calle_id}", reg_tc.getId() + "");
				fila.setVariable("{option_calle_nombre}", reg_tc.getNombre());
				arr_tipocalle.add(fila);
			}
			top.setDynamicValueSets("select_tipocalle", arr_tipocalle);
			
			ArrayList arr_regiones = new ArrayList();
			List regiones = biz.regionesGetAll();
			for (int i = 0; i < regiones.size(); i++) {
				RegionesDTO dbregion = (RegionesDTO) regiones.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{option_reg_id}", dbregion.getId()+"");
				fila.setVariable("{option_reg_nombre}", dbregion.getNombre());
				arr_regiones.add(fila);
			}
			top.setDynamicValueSets("select_regiones", arr_regiones);
			
			if (session.getAttribute("ses_cli_id") != null) {				
				// Recuperar direcciones de despacho del cliente
				List lista = biz.clientegetAllDirecciones(cliente_id.longValue());
				List datos = new ArrayList();
				for (int i = 0; i < lista.size(); i++) {
					DireccionesDTO dir = (DireccionesDTO) lista.get(i);
					IValueSet fila = new ValueSet();
					fila.setVariable("{dir_opcion}", dir.getAlias() + "");
					fila.setVariable("{dir_calle}", dir.getCalle() + "");
					fila.setVariable("{dir_numero}", dir.getNumero() + "");
					fila.setVariable("{dir_dpto}", dir.getDepto() + "");
					fila.setVariable("{dir_valor}", dir.getId() + "");
					fila.setVariable("{dir_comentario}", dir.getComentarios() + "");
					int largo = 12;
					if( dir.getComentarios().length() < 12  )
						largo = dir.getComentarios().length(); 
					fila.setVariable("{dir_comentario_s}", dir.getComentarios().substring(0,largo) + "");
					fila.setVariable("{dir_tipid}", dir.getTipo_calle() + "");
					fila.setVariable("{dir_id_comuna}", dir.getCom_id() + "");
					fila.setVariable("{dir_comuna}", dir.getCom_nombre() + "");
					fila.setVariable("{dir_region}", dir.getReg_nombre() + "");
					fila.setVariable("{dir_id_region}", dir.getReg_id() + "");
					if (lista.size() > 1){
						List list_boton = new ArrayList();
						list_boton.add(fila);
						fila.setDynamicValueSets("NOBOTONBORRAR", list_boton );
					}
					
					datos.add(fila);
				}
				top.setDynamicValueSets("dir_despachos", datos);
			}
  
			top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
			
			//envio de datos
			if(top.getVariable("{nombre_cliente}").equals("Invitado")){
                top.setVariable("{ta_mx_user}", "invitado");
            }
            else{
                top.setVariable("{ta_mx_user}", "registrado");
            }
          //Tags Analytics - Captura de Comuna y Región en Texto
          /************   LISTADO DE REGIONES   ****************/
          
          String ta_mx_loc = ComunasRegionesTexto.ComunaRegionTexto(session);
          if(ta_mx_loc.equals(""))
                ta_mx_loc="none-none";
          top.setVariable("{ta_mx_loc}", ta_mx_loc);
          
          
          top.setVariable("{ta_mx_content}", "Datos Personales");
			
			String result = tem.toString(top);
	
			out.print(result);
			
			} catch (Exception e) {
				this.getLogger().error(e);
				e.printStackTrace();
				throw new CommandException(e);
			}

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
