package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import net.sf.json.JSONObject;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.clientes.dto.MailDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.parametros.dto.ParametroFoDTO;
import cl.bbr.promo.lib.dto.ClienteSG6DTO;
import cl.bbr.promo.lib.dto.SamsungGalaxy6DTO;


/**
 * Preventa Samsung Galaxy 6
 * 
 *
 */
public class PreventaSG6 extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6161847227080990772L;

	protected void execute(HttpServletRequest req, HttpServletResponse arg1) throws CommandException {
		JSONObject oJsonResponse = new JSONObject();
		try {
			// Recupera la sesiÛn del usuario
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
		        ArrayList modelosGalaxy = biz.getModelosSamsung("SG6_MODELOS");
		        ArrayList modelosGalaxyDesplegar = new ArrayList();
		        
		        for(int i=0; i < modelosGalaxy.size();i++){
		        	SamsungGalaxy6DTO galaxy = (SamsungGalaxy6DTO) modelosGalaxy.get(i);
		        	if(Integer.valueOf(galaxy.getStock()).intValue() >0){
			        	ValueSet varGalaxy = new ValueSet();
			        	varGalaxy.setVariable("{idGalaxy}", String.valueOf(galaxy.getId()));
			        	varGalaxy.setVariable("{descripcionGalaxy}", galaxy.getPar_descripcion());
			        	modelosGalaxyDesplegar.add(varGalaxy);
		        	} else {
		        		ValueSet varGalaxy = new ValueSet();
			        	varGalaxy.setVariable("{idGalaxy}", "0");
			        	varGalaxy.setVariable("{descripcionGalaxy}", galaxy.getPar_descripcion()+" (Agotado)");
			        	modelosGalaxyDesplegar.add(varGalaxy);
		        	}
		        }
		        top.setDynamicValueSets("MODELOS_GALAXY", modelosGalaxyDesplegar);
		        ParametroFoDTO fechaLimite = biz.getParametroByKey("SG6_FECH_LIMITE");
				if(validaFechaLimite(fechaLimite)){
					top.setVariable("{FECHA_CADUCADA}", "Y");
				}
		        String result = tem.toString(top);
				out.print(result);
	    	} else{
	    				
	    		int criterio = identificaCriterio(req);
	    		switch (criterio) {
				case 1: /** b˙squeda de cliente por rut ingresado **/
					if(validaRutCliente(req)){
						ClienteSG6DTO cliente = setRutCliente(req);
						cliente=biz.getClienteByRut(cliente);
						if(cliente != null && cliente.isRegistrado()){
							cliente = formatear(cliente);
							oJsonResponse.put("nombreHdn", cliente.getNombre());
							oJsonResponse.put("cli_id", String.valueOf(cliente.getId()));
							oJsonResponse.put("apellidoHdn", cliente.getApellido());
							oJsonResponse.put("emailHdn", cliente.getEmail());
							oJsonResponse.put("telefonoHdn",cliente.getCodFono() +" "+cliente.getTelefono());
							oJsonResponse.put("cod","0");	
						} else {
							oJsonResponse.put("cod","1");	
						}
					} else {
						oJsonResponse.put("cod","3");
					}
					
					break;
				case 2: /** registro de preventa de samsung galaxy s6 para cliente **/
					ClienteSG6DTO cliente=validaDatosCliente(req);
					if(null != cliente){
						ParametroFoDTO maximaReserva = biz.getParametroByKey("SG6_CANT_MAXIMA");
						if(null != maximaReserva && null != maximaReserva.getValor()){
							int cantidadMaxima = Integer.valueOf(maximaReserva.getValor()).intValue();
							int cantReservaCliente = biz.getReservasSamsungCliente(cliente);
							if(cantidadMaxima <= cantReservaCliente){
								oJsonResponse.put("cod","1");
							} else {
								ParametroFoDTO modeloReserva = biz.getParametroByID(cliente.getId_modelo());
								if(null != modeloReserva && null != modeloReserva.getValor()){
									cliente.setModeloSamsung(modeloReserva.getDescripcion());
									try{
										int cantidadDisponible = Integer.valueOf(modeloReserva.getValor()).intValue();
										if(cantidadDisponible > 0){
											cliente = biz.getDireccionCliente(cliente);
											biz.registrarReservaSamsung(cliente);
											// Se graba mail al cliente para posteriormente ser enviado
											String pag_form=rb.getString("conf.dir.html") + "" + rb.getString("mail.reservaSamsung.pathTemplate.html");
											TemplateLoader load = new TemplateLoader(pag_form);
								            ITemplate tem = load.getTemplate();
								            IValueSet top = new ValueSet();
								            top.setVariable("{CLIENTE_NOMBRE}", cliente.getNombre() + " " + cliente.getApellido());
								            top.setVariable("{MODELO_SAMSUNG}", cliente.getModeloSamsung());

								            String result = tem.toString(top);
											
							                MailDTO mail = new MailDTO();
							                mail.setFsm_subject(cliente.getNombre() + ", " + rb.getString("mail.reservaSamsung.subject"));
							                mail.setFsm_data(result);
							                mail.setFsm_destina(cliente.getEmail());
							                mail.setFsm_remite(rb.getString("mail.checkout.remite"));
							                biz.addMail(mail);
											oJsonResponse.put("cod","0");
										} else {
											oJsonResponse.put("cod","3");
										}
									} catch (NumberFormatException e) {
										oJsonResponse.put("cod","2");
									}
								}
							}
						}
					} else {
						oJsonResponse.put("cod","4");
					}
					break;

				default: 
					oJsonResponse.put("cod","2");
					break;
				}
	    		
	  			
	  			PrintWriter out = arg1.getWriter(); 
	  			arg1.setContentType("application/json");
				out.println(oJsonResponse.toString());
				out.close();
			}			
		} catch (Exception e) {
			oJsonResponse.put("cod","2");
		}
	}

	private ClienteSG6DTO formatear(ClienteSG6DTO cliente) {
		cliente.setNombre(remove1(cliente.getNombre()));
		cliente.setApellido(remove1(cliente.getApellido()));
		return cliente;
	}
	public static String remove1(String input) {
	    // Cadena de caracteres original a sustituir.
	    String original = "·‡‰ÈËÎÌÏÔÛÚˆ˙˘uÒ¡¿ƒ…»ÀÕÃœ”“÷⁄Ÿ‹—Á«";
	    // Cadena de caracteres ASCII que reemplazar·n los originales.
	    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
	    String output = input;
	    for (int i=0; i<original.length(); i++) {
	        // Reemplazamos los caracteres especiales.
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }//for i
	    return output;
	}//remove1
	

	private boolean validaFechaLimite(ParametroFoDTO fechaLimite) {
		// TODO ApÈndice de mÈtodo generado autom·ticamente
		boolean fechaCaducada = false;
		try{ 
		Date fechaActual = new Date();
	     SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
	     String fechaSistema=formateador.format(fechaActual);
	     fechaActual = formateador.parse(fechaSistema);
	     Date fechaM·xima = formateador.parse(fechaLimite.getNombre());
	     if(fechaM·xima.before(fechaActual)){
	    	 fechaCaducada = true;
	     }
		} catch (ParseException e){
			
		}
		return fechaCaducada;
	}

	/**
	 * 
	 * @param req
	 * @param cliente
	 * @return
	 */
	private ClienteSG6DTO validaDatosCliente(HttpServletRequest req) {
		ClienteSG6DTO cliente = new ClienteSG6DTO();
		boolean datosValidos = true;
		if(validaRutCliente(req)){
			cliente=setRutCliente(req);
		} else{	
			datosValidos=false;
		}
		String id_cliente = Utils.sanitizeFO(req.getParameter("cli_idHdn"));
		if(null != id_cliente && !Constantes.CADENA_VACIA.equals(id_cliente.trim())){
			cliente.setId(Integer.valueOf(id_cliente).intValue());
		} else {	
			datosValidos=false;
		}
		String nombre = Utils.sanitizeFO(req.getParameter("nombreHdn"));
		if(null != nombre && !Constantes.CADENA_VACIA.equals(nombre.trim())){
			cliente.setNombre(nombre);
		} else {	
			datosValidos=false;
		}
		String apellido = Utils.sanitizeFO(req.getParameter("apellidoHdn"));
		if(null != apellido && !Constantes.CADENA_VACIA.equals(apellido.trim())){
			cliente.setApellido(apellido);
		}else{
			datosValidos=false;
		}
		String telefono = Utils.sanitizeFO(req.getParameter("telefonoHdn"));
		if(null != telefono && !Constantes.CADENA_VACIA.equals(telefono.trim())){
			cliente.setTelefono(telefono);
		}else {
			datosValidos=false;
		}
		try{
			int id_modelo = Integer.parseInt(Utils.sanitizeFO(req.getParameter("id_modelohdn")));
			cliente.setId_modelo(id_modelo);
		} catch (NumberFormatException e) {
			datosValidos = false;
		}
		
		String email = Utils.sanitizeFO(req.getParameter("emailHdn"));
		if(null != email && !Constantes.CADENA_VACIA.equals(email.trim())){
			cliente.setEmail(email);
		} else {
			datosValidos=false;
		}
		if(!datosValidos){
			cliente = null;
		}
		return cliente;
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	private ClienteSG6DTO setRutCliente(HttpServletRequest req) {
		// TODO ApÈndice de mÈtodo generado autom·ticamente
		ClienteSG6DTO cliente = new ClienteSG6DTO();
		String rutHdn = Utils.sanitizeFO(req.getParameter("rutHdn"));
		String dvHdn = Utils.sanitizeFO(req.getParameter("dvHdn"));
		cliente.setRut(rutHdn);
		cliente.setDv(dvHdn);
		return cliente;
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	private boolean validaRutCliente(HttpServletRequest req) {
		// TODO ApÈndice de mÈtodo generado autom·ticamente
		//Valida Rut
		boolean datosValidos = true;
		String rutHdn = Utils.sanitizeFO(req.getParameter("rutHdn"));
		String dvHdn = Utils.sanitizeFO(req.getParameter("dvHdn"));
		if (!Utils.verificarRutFO(Integer.parseInt(rutHdn),(char)dvHdn.charAt(0))){
			datosValidos=false;
		}
		return datosValidos;
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	private int identificaCriterio(HttpServletRequest req) {
		int criterio=0;
		String descCriterio = Utils.sanitizeFO(req.getParameter("criteria"));
		
		if(null != descCriterio && "findByRut".equals(descCriterio.trim())){
			criterio = 1;
		} else if(
			null != descCriterio && "registrarPreventa".equals(descCriterio.trim())){
			criterio = 2;
		} 
		return criterio;
	}



	
	
}
