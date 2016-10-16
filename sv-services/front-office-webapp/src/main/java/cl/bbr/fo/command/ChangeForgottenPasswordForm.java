package cl.bbr.fo.command;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sun.misc.BASE64Decoder;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.Cod_error;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Servlet que permite modificar la clave.
 *  
 * @author BBR e-commerce & retail
 *  
 */
public class ChangeForgottenPasswordForm extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException, ServletException, IOException {

		try {		
			// Carga properties
			ResourceBundle rb_fo = ResourceBundle.getBundle("fo");
            
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			// Recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();
	
			//long id  = Long.parseLong(arg0.getParameter("id").replaceAll("\\.",""));            
            //String clave_encriptada = arg0.getParameter("key").toString();
            
            if (arg0.getParameter("token") != null && !arg0.getParameter("token").toString().equals("")){
                String token = arg0.getParameter("token").toString();
                BizDelegate biz = new BizDelegate();
                
                //String key = rb_bo.getString("conf.bo.key");
                //String clave_decript1 = Cifrador.desencriptar(key, clave);
                //String clave_decript2 = Cifrador.desencriptar(key, clave_decript1);
                
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] token_decode64 = decoder.decodeBuffer(token);
                String token_decode = new String(token_decode64);

                
                String[] paramKey = token_decode.split("--"); //id_region-id_comuna-nom_comuna
                long rut = Long.parseLong(paramKey[0]);
                String fechaHora = paramKey[1];
                String clave = paramKey[2];
                
                String[] FecHora = fechaHora.split(" ");
                String fecha = FecHora[0];
                String hora = FecHora[1]; 
                
                String DATE_FORMAT_DATE = "yyyyMMdd";
                String DATE_FORMAT_TIME = "HHmmss";
                Calendar calHoy = Calendar.getInstance();
                calHoy.setTime(new Date());
                SimpleDateFormat sdfFechaHoy = new SimpleDateFormat(DATE_FORMAT_DATE);
                SimpleDateFormat sdfHoraHoy = new SimpleDateFormat(DATE_FORMAT_TIME);
                int fecha_hoy = Integer.parseInt(sdfFechaHoy.format(calHoy.getTime()));
                int hora_hoy = Integer.parseInt(sdfHoraHoy.format(calHoy.getTime()));
                
                
                /*Calendar calToken = Calendar.getInstance();
                int tokenYear = Integer.parseInt(fecha.substring(0, 4));
                int tokenMonth = Integer.parseInt(fecha.substring(4, 6)) - 1;
                int tokenDay = Integer.parseInt(fecha.substring(6, 8));

                int tokenHora = Integer.parseInt(fecha.substring(9, 11));
                int tokenMin  = Integer.parseInt(fecha.substring(11, 13));
                int tokenSeg = Integer.parseInt(fecha.substring(13, 15));

                calToken.set(tokenYear, tokenMonth, tokenDay, tokenHora, tokenMin, tokenSeg);
                
                SimpleDateFormat sdfToken = new SimpleDateFormat(DATE_FORMAT);
                int fecha_token = Integer.parseInt(sdfToken.format(calToken.getTime()));*/
                
                int fecha_token = Integer.parseInt(fecha);
                int hora_token  = Integer.parseInt(hora);
                
                ClienteDTO rutaux = (ClienteDTO) biz.clienteGetByRut( rut );
                System.out.println("");
                /****************************************************************************/
                /***********   VERIFICA KEY ALMACENADA SEA IGUAL A LA INGRESADA   ***********/
                /****************************************************************************/
                boolean ClienteSolicitaCambioClave = false;
                boolean ClaveActiva = false;  
                
                if (!rutaux.getKey_CambioClave().equals("") && rutaux.getKey_CambioClave().equals(clave)){
                    ClienteSolicitaCambioClave = true;
                }
                
                if (fecha_token > fecha_hoy){
                    ClaveActiva = true;
                }else if (fecha_token == fecha_hoy){
                    if (hora_token >= hora_hoy){
                        ClaveActiva = true;
                    }
                }else{
                    biz.RecuperaClave_GuardaKeyCliente(rutaux.getId(), "");
                }
                
                /****************************************************************************/
                /****************************************************************************/
                /****************************************************************************/

                if (ClienteSolicitaCambioClave && ClaveActiva){
                    if (rutaux != null) {
                        
                        // RESPUESTA CORRECTA POR LO TANTO PODEMOS MODIFICAR LA CLAVE           
                    
                        // Recupera pagina desde web.xml
                        String pag_form = rb_fo.getString("conf.dir.html") + "" +  getServletConfig().getInitParameter("pag_form");
                        
                        // Carga el template html
                        TemplateLoader load = new TemplateLoader(pag_form);
                        ITemplate tem = load.getTemplate();
                
                        IValueSet top = new ValueSet();
                        top.setVariable("{id}", rutaux.getId() + "");
                        top.setVariable("{rut}", rut+"");
                        
                        String result = tem.toString(top);
                        
                        out.print(result);
                    } else {
                        //  respuesta incorrecta
                        session.setAttribute("cod_error", Cod_error.CLI_RESPUESTA_INVALIDA);
                        getServletConfig().getServletContext().getRequestDispatcher(getServletConfig().getInitParameter("resp_inc")).forward(arg0, arg1);
                    }
                }else{
                    //  Token expirado o incorrecto
                    //session.setAttribute("cod_error", Cod_error.CLI_RESPUESTA_INVALIDA);
                    session.removeAttribute("cod_error");
                    getServletConfig().getServletContext().getRequestDispatcher(getServletConfig().getInitParameter("resp_inc2")).forward(arg0, arg1);                
                }
            }else{
                //  Token expirado o incorrecto (NO EXISTE o ESTA VACIO)
                //session.setAttribute("cod_error", Cod_error.CLI_RESPUESTA_INVALIDA);
                session.removeAttribute("cod_error");
                getServletConfig().getServletContext().getRequestDispatcher(getServletConfig().getInitParameter("resp_inc2")).forward(arg0, arg1);                
            }
		} catch (Exception e) {
            this.getLogger().error("Problama al solicitar clave", e);
            getServletConfig().getServletContext().getRequestDispatcher(getServletConfig().getInitParameter("dis_er")).forward(arg0, arg1);
		}
	}
}
