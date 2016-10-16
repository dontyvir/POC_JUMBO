package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import net.sf.json.JSONObject;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.promo.lib.dto.ClientePRDTO;

/**
 * Ingreso Cliente KCC
 * 
 *
 */
public class ClientePR extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6161847227080990772L;

	protected void execute(HttpServletRequest req, HttpServletResponse arg1) throws CommandException {
		try {
			// Recupera la sesión del usuario
			HttpSession session = req.getSession();
			ResourceBundle rb = ResourceBundle.getBundle("fo");
			BizDelegate biz = new BizDelegate();
					    
		    
		    if (!(req.getHeader("X-Requested-With") != null && req.getHeader("X-Requested-With").toString().equals("XMLHttpRequest"))){
		    	arg1.setHeader("Cache-Control", "no-cache");
		    	arg1.setCharacterEncoding("UTF-8");
		    	arg1.setContentType("text/html; charset=iso-8859-1");
				
				// Se recupera la salida para el servlet
				PrintWriter out = arg1.getWriter();
				
				
				
				// Recupera pagina desde web.xml y se inicia parser
				String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
		        TemplateLoader load = new TemplateLoader(pag_form);
		        ITemplate tem = load.getTemplate();	
		        IValueSet top = new ValueSet();
		        
		    	ArrayList arr_dias = new ArrayList();
				for (int i = 1; i <= 31; i++) {
					IValueSet fila = new ValueSet();
					if (req.getParameter("dia") == null ){
						fila.setVariable("{selectdia}", "" );
					}else if( Integer.parseInt(req.getParameter("dia")) == i){
						fila.setVariable("{selectdia}", "selected=\"selected\"" );
					}else{
						fila.setVariable("{selectdia}", "" );
					}
					
					fila.setVariable("{option_dia}", i + "");
					arr_dias.add(fila);
				}
				top.setDynamicValueSets("select_dias", arr_dias);
		
				ArrayList arr_anos = new ArrayList();
				Calendar ahora = Calendar.getInstance();
				for (int i = 1900; i <= ahora.get(Calendar.YEAR); i++) {
					IValueSet fila = new ValueSet();
					if (req.getParameter("ano") == null ){
						fila.setVariable("{selectano}", "" );
					}else if( Integer.parseInt(req.getParameter("ano")) == i){
						fila.setVariable("{selectano}", "selected=\"selected\"" );
					}else{
						fila.setVariable("{selectano}", "" );
					}
					
					fila.setVariable("{option_ano}", i + "");
					arr_anos.add(fila);
				}
				top.setDynamicValueSets("select_anos", arr_anos);
		        
		        
		        
		        
		        String result = tem.toString(top);
				out.print(result);
	    	} else{
	    		JSONObject oJsonResponse = new JSONObject();  			
	  		
	  			//validar cliente ya existente
	  			if (!validaCaptcha(req)){
	  				//arg1.setStatus(500);//Envia 4 en el caso que el captcha sea equivocado
	  				oJsonResponse.put("cod","100");	  								
	  			}else{	  			
	  				ClientePRDTO datosCliente = validateDatosKcc(req);
			
	  				if (datosCliente != null){
						//Registrar Datos
						if (!biz.getClientePRByRut(datosCliente.getRut(), datosCliente.getDv()))
						{
							biz.addDataClientePR(datosCliente);
							oJsonResponse.put("cod","200");							
						}
						else //ya existe o los datos son erroneos
						{	
							oJsonResponse.put("cod","201");						
						}
					}else{
						oJsonResponse.put("cod","500");
					}
	  				
	  				
	  				
				}
	  			
	  			PrintWriter out = arg1.getWriter(); 
	  			arg1.setContentType("application/json");
				out.println(oJsonResponse.toString());
				out.close();
			}			
		} catch (Exception e) {
			logger.error(e);
			//throw new CommandException(e);
		}
	}
	
	
	//valida captcha
	private boolean validaCaptcha(HttpServletRequest req1){		
		String captcha = req1.getParameter("captcha_Hdn").toString();
	    HttpSession session = req1.getSession();
	    if ( session.getAttribute("captcha_mail") != null ) {
	        if ( !session.getAttribute("captcha_mail").toString().equals(captcha) ) {	          
	         return false;
	        }
	    } else {
	        return false;
	    }
	    session.removeAttribute("captcha_mail");
	    return true;
	}
	
	//Valida datos formulario
	private ClientePRDTO validateDatosKcc(HttpServletRequest req1) {		
		
		ClientePRDTO datosCliente = new ClientePRDTO();						
			boolean datosValidos=true;
		/*	
			<input type="hidden" name="rutHdn" id="rutHdn" value="" />
			<input type="hidden" name="dvHdn" id="dvHdn" value="" />
			<input type="hidden" name="nombreHdn" id="nombreHdn" value="" />
			<input type="hidden" name="emailHdn" id="emailHdn" value="" />
			<input type="hidden" name="direccionHdn" id="direccionHdn" value="nulo" />
			<input type="hidden" name="comunaHdn" id="comunaHdn" value="" />
			<input type="hidden" name="sexoHdn" id="sexoHdn" value="" />
			<input type="hidden" name="tallaHdn" id="tallaHdn" value="" />
			<input type="hidden" name="anno_bebeHdn" id="anno_bebeHdn" value="nulo" />
			<input type="hidden" name="meses_bebeHdn" id="meses_bebeHdn" value="nulo" />
			<input type="hidden" name="numero_boletaHdn" id="numero_boletaHdn" value="nulo" />
			<input type="hidden" name="acepta_basesHdn" id="acepta_basesHdn" value="nulo" />
			<input type="hidden" name="acepta_informacionHdn" id="acepta_informacionHdn" value="nulo" />
			<input type="hidden" name="captcha_Hdn" id="captcha_Hdn" value="nulo" />
	*/
			
			String rutHdn = Utils.sanitizeFO(req1.getParameter("rutHdn"));
			String dvHdn = Utils.sanitizeFO(req1.getParameter("dvHdn").toUpperCase());
			String nombreHdn = Utils.utf8DecodeFO(Utils.sanitizeFO(req1.getParameter("nombreHdn")));
			String apellidoHdn=Utils.utf8DecodeFO(Utils.sanitizeFO(req1.getParameter("apellidoHdn")));
			String emailHdn = Utils.sanitizeFO(req1.getParameter("emailHdn"));
			String direccionHdn = Utils.utf8DecodeFO(Utils.sanitizeFO(req1.getParameter("direccionHdn")));
			String comunaHdn = Utils.sanitizeFO(req1.getParameter("comunaHdn"));
			String aceptaInformacion = Utils.sanitizeFO(req1.getParameter("acepta_informacionHdn"));
			aceptaInformacion = ("S".equals(aceptaInformacion))? "S":"N";
			String fecha=req1.getParameter("fechaNacimientoHdn");
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
		 
				Date fechaNacimiento = formatter.parse(fecha);
				
				datosCliente.setFechaNacimiento(fechaNacimiento);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//Validacion de los Datos
			//Valida Rut
			if (!Utils.verificarRutFO(Integer.parseInt(rutHdn),(char)dvHdn.charAt(0))){
				datosValidos=false;
			}
			//Valida Nombre Completo
			//if (!Utils.validateTexto(nombreHdn,"^[A-Za-z\\s]{2,200}$")){
			if (nombreHdn == null || nombreHdn.length() == 0){
				datosValidos=false;
			}
			//Valida Correo
			if (!Utils.validateEmailFO(emailHdn)){ 
				 datosValidos=false;
			}
			//Valida Direccion
			if (direccionHdn == null || direccionHdn.length() == 0){
				datosValidos=false;
			}
			//Valida Comuna
			if (!Utils.validateTextoFO(comunaHdn,"^[A-Za-z\\s]{2,}$")){
				datosValidos=false;
			}			
			
			
			
			// --- FIN VALIDACION
			
			if (datosValidos)
			{
				datosCliente.setRut(rutHdn);
				datosCliente.setDv(dvHdn);
				datosCliente.setEmail(emailHdn);
				datosCliente.setNombre(nombreHdn);
				datosCliente.setDireccion(direccionHdn);
				datosCliente.setComunaDespacho(comunaHdn);
				datosCliente.setAceptaInformacion(aceptaInformacion);
				datosCliente.setApellido(apellidoHdn);
				
			}
			
			return (datosValidos)?datosCliente:null;
		}
}
