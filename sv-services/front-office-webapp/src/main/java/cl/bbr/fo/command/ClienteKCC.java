package cl.bbr.fo.command;

import java.io.PrintWriter;
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
import cl.bbr.promo.lib.dto.ClienteKccDTO;

/**
 * Ingreso Cliente KCC
 * 
 *
 */
public class ClienteKCC extends Command {

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
		        
		        String result = tem.toString(top);
				out.print(result);
	    	} else{
	    		JSONObject oJsonResponse = new JSONObject();  			
	  		
	  			//validar cliente ya existente
	  			if (!validaCaptcha(req)){
	  				//arg1.setStatus(500);//Envia 4 en el caso que el captcha sea equivocado
	  				oJsonResponse.put("cod","100");	  								
	  			}else{	  			
					ClienteKccDTO datosCliente = validateDatosKcc(req);
					if (datosCliente != null){
						//Registrar Datos
						if (!biz.getClienteKccByRut(datosCliente.getRut(), datosCliente.getDv()))
						{
							biz.addDataClienteKcc(datosCliente);
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
	private ClienteKccDTO validateDatosKcc(HttpServletRequest req1) {		
		
			ClienteKccDTO datosCliente = new ClienteKccDTO();						
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
			String dvHdn = Utils.sanitizeFO(req1.getParameter("dvHdn"));
			String nombreHdn = Utils.utf8DecodeFO(Utils.sanitizeFO(req1.getParameter("nombreHdn")));
			String emailHdn = Utils.sanitizeFO(req1.getParameter("emailHdn"));
			String direccionHdn = Utils.utf8DecodeFO(Utils.sanitizeFO(req1.getParameter("direccionHdn")));
			String comunaHdn = Utils.sanitizeFO(req1.getParameter("comunaHdn"));
			String sexoHdn = (req1.getParameter("sexoHdn") != null)? String.valueOf(Utils.sanitizeFO(req1.getParameter("sexoHdn"))).trim():"";
			String tallaHdn = (req1.getParameter("tallaHdn") != null)? String.valueOf(Utils.sanitizeFO(req1.getParameter("tallaHdn"))).trim():"";
			String annoBebe =Utils.sanitizeFO(req1.getParameter("anno_bebeHdn"));
			String mesesBebe =Utils.sanitizeFO(req1.getParameter("meses_bebeHdn"));
			String boleta =Utils.sanitizeFO(req1.getParameter("numero_boletaHdn"));
			String aceptaInformacion = Utils.sanitizeFO(req1.getParameter("acepta_informacionHdn"));
			aceptaInformacion = ("S".equals(aceptaInformacion))? "S":"N";
	
			
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
			//Valida Sexo
			if (!("F".equals(sexoHdn) || "M".equals(sexoHdn)))
			{
				datosValidos=false;
			}			
			//Valida talla
			if (!Utils.validateTextoFO(tallaHdn,"^[A-Z]{1,5}$")){
				datosValidos=false;
			}
			//Valida annos bebe
			if (!Utils.validateTextoFO(annoBebe,"^[0-9]{1,2}$") && !isNumeric(annoBebe)){
				datosValidos=false;
			}
			//Valida meses bebe
			if (!Utils.validateTextoFO(mesesBebe,"^[0-9]{1,2}$") && !isNumeric(mesesBebe)){
				datosValidos=false;
			}			
			
			if (!Utils.validateTextoFO(boleta,"^[A-Za-z0-9]{2,}$")){
				datosValidos=false;
			}
			
			
			// --- FIN VALIDACION
			
			if (datosValidos)
			{
				datosCliente.setRut(rutHdn);
				datosCliente.setDv(dvHdn);
				datosCliente.setEmail(emailHdn);
				datosCliente.setSexo(sexoHdn);
				datosCliente.setTalla(tallaHdn);
				datosCliente.setNombreCompleto(nombreHdn);
				datosCliente.setDireccion(direccionHdn);
				datosCliente.setComunaDespacho(comunaHdn);
				datosCliente.setBoleta(boleta);
				datosCliente.setAnnosBebe(Integer.parseInt(annoBebe));
				datosCliente.setMesesBebe(Integer.parseInt(mesesBebe));
				datosCliente.setAceptaInformacion(aceptaInformacion);
				
			}
			
			return (datosValidos)?datosCliente:null;
		}
}
