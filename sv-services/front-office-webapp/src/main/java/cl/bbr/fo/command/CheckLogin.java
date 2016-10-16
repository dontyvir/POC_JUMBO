package cl.bbr.fo.command;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.util.Cod_error;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.log.Logging;

/**
 * CheckLogin es un Servlet que valida los usuarios para conectarse al sitio.
 * <p>
 * Comportamiento:
 * <ul>
 * <li>revisa si el RUT y la clave de un cliente son correctos.</li>
 * <li>crea una session para su identificación. El cliente desde ahora tiene
 * una sesión identificada. La sesión contiene la siguiente información: nombre
 * del cliente.</li>
 * <li>Se devuelve respuesta afirmativa o incorrecta para manejo desde la página.</li>
 * </ul>
 * <p>
 * Excepciones:
 * <ul>
 * <li>Si el RUT del cliente no existe</li>
 * </ul>
 * <p>
 * 
 * @author CAF
 * 
 */
public class CheckLogin extends Command {

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {

		//variable guarda error
		String mensajeSistema = "";
		String codError = "";
        String destination = "CategoryDisplay";
		
		try {
			
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");			
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession(true);
						
            //Instacia del bizdelegate
            BizDelegate biz = new BizDelegate();
            ClienteDTO cliente = new ClienteDTO();  
			
		
			// Se revisan que los parámetros mínimos existan
			if ( !validateParametersLocal(arg0) ) {
				this.getLogger().error("Faltan parámetros mínimos (CheckLogin): " + Cod_error.REG_FALTAN_PARA);
				codError = Cod_error.REG_FALTAN_PARA;
			} else {
				// Se cargan los parámetros del request a variables locales
				String rut_s = arg0.getParameter("rut").toString();
                this.getLogger().info("Intento conexion ( RUT:" + rut_s + " )");
                
                rut_s = rut_s.replaceAll("\\.","").replaceAll("\\-","");
                rut_s = rut_s.substring(0, rut_s.length()-1);
                
				long rut = Long.parseLong(rut_s);
				String clave = arg0.getParameter("clave").toString();
			
				cliente = biz.clienteGetByRut(rut);
			
				// Se revisa si existe el cliente
				if( cliente == null ) {
					this.getLogger().info("Cliente no existe");
					codError = Cod_error.CLI_NO_EXISTE;
				} else {
					this.getLogger().info( "Cliente existe" );
					//Verificamos número de intententos de logeo
			
					Calendar cal = new GregorianCalendar();

					if (((cal.getTimeInMillis()-cliente.getFec_login()) <= Long.parseLong(rb.getString("logon.tiempo.segundos"))*1000) && cliente.getIntento() >= Long.parseLong(rb.getString("logon.intentos")) ){
						this.getLogger().info("Problemas con el numero de intentos");
						codError = "Se ha superado el número de intentos fallidos";
					} else {			
						// Se valida si las claves coinciden
						if( cliente.getClave().compareTo(Utils.encriptarFO(clave)) != 0 ) {
							//1: modifica campos, 0 : limpia campos
							biz.updateIntentos(cliente.getId(), 1); 
							codError = Cod_error.CLI_CLAVE_INVALIDA;
						} else {
                            if (cliente.getEstado().compareTo("C") == 0) {
                                logger.debug("Clave Caducada: " + cliente.getId());
                                codError = "Clave Caducada";
                            } else {
    							biz.updateIntentos(cliente.getId(), 0); 
    							this.getLogger().info( "Cliente clave OK" );
    							this.getLogger().info( "Nombre Cliente:" + cliente.getNombre() + " " + cliente.getApellido_pat());                                
    							mensajeSistema = "OK";
                            }
						}
					}	
				}
			}
            
			if ( !mensajeSistema.equals("OK") ) {
				this.getLogger().info("Cod_error:" + codError);
				if( codError.compareTo(Cod_error.CLI_NO_EXISTE) == 0) {
					mensajeSistema = rb.getString("logon.mensaje.rut");
				}
				else if( codError.compareTo(Cod_error.CLI_CLAVE_INVALIDA) == 0) {
					mensajeSistema = rb.getString("logon.mensaje.clave");
				}
				else if( codError.compareTo(Cod_error.GEN_ERR_PERMISOS) == 0) {
					mensajeSistema = rb.getString("logon.mensaje.nopermisos");
				} else {
					mensajeSistema = rb.getString("general.mensaje.error.nn") + " (" + codError + ")";
				}
                
			} else {
				 if ( arg0.getParameter("accesoLfckfield") != null && session.getAttribute("ses_oJsonFacebook") != null ) {
					JSONObject oJsonFromFacebook = (JSONObject) session.getAttribute("ses_oJsonFacebook");
					if(arg0.getParameter("accesoLfckfieldCheckLogin") != null)
						biz.setClienteFacebook(cliente.getId(), oJsonFromFacebook.getString("id"));
					sessionStart(arg0, biz, cliente, FACEBOOK_SESSION);
				}else{				
					sessionStart(arg0, biz, cliente, JUMBO_SESSION);
					if(session.getAttribute("ses_destination") != null)
						destination = session.getAttribute("ses_destination").toString();
				}
            }
                
            
            if ( !"BienvenidaForm".equalsIgnoreCase(destination) &&
            		arg0.getParameter("destination") != null && 
            		!"".equalsIgnoreCase( arg0.getParameter("destination").toString() ) ) {
                destination = arg0.getParameter("destination").toString(); 
            }
            
		} catch (Exception e) {
			this.getLogger().error(e);
			mensajeSistema = "Ocurrio un problema con el servidor";
		}
		
		arg1.setContentType("text/xml");
        arg1.setHeader("Cache-Control", "no-cache");
        arg1.setCharacterEncoding("UTF-8");
        
        //vemos cual es el mensaje a desplegar
        arg1.getWriter().write("<login>");
        arg1.getWriter().write("<mensaje>" + mensajeSistema + "</mensaje>");
        arg1.getWriter().write("<destination>" + destination + "</destination>");
        arg1.getWriter().write("</login>");
	}

    /**
	 * Valida parámetros mínimos necesarios
	 * 
	 * @param arg0	Request recibido desde el navegador
	 * @param arg1	Response recibido desde el navegador
	 * @return		True: ok, False: faltan parámetros
	 */
	private boolean validateParametersLocal(HttpServletRequest arg0) {
		Logging logger = this.getLogger();

		ArrayList campos = new ArrayList();
		campos.add("rut");
		campos.add("clave");

		for (int i = 0; i < campos.size(); i++) {
			String campo = (String) campos.get(i);
			if (arg0.getParameter(campo) == null || arg0.getParameter(campo).compareTo("") == 0) {
				logger.error("Falta parámetro: " + campo + " en CheckLogin");
				return false;
			}
		}
		return true;
	}
}