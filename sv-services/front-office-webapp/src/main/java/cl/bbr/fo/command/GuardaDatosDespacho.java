package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
//import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
//import cl.bbr.promo.lib.dto.CuponDsctoDTO;

import cl.bbr.jumbocl.common.utils.Cifrador;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Guarda los datos del despacho
 * 
 * @author carriagada
 * 
 */
public class GuardaDatosDespacho extends Command {
	
	protected void execute(HttpServletRequest req, HttpServletResponse arg1) throws CommandException {
		try {
			
			ResourceBundle rb = ResourceBundle.getBundle("fo");
        	String keyFo = rb.getString("conf.fo.key");
        	List listDespachoSession = null;
        	
        	String isOk="N";
			// Recupera la sesión del usuario
			HttpSession session = req.getSession();			
            BizDelegate biz = new BizDelegate();
            //recupera datos del cliente invitado y los dejamos en sesion para usarlos en el resumen de compra
            if(session.getAttribute("ses_cli_rut").toString().equals("123123")){
            	//inicio limpieza variables de sesion 
            	session.setAttribute("rut1_inv", null);				//Rut
            	session.setAttribute("nombre_inv", null);			//Nombre
            	session.setAttribute("ape_pat_inv", null);		//Apellido
            	session.setAttribute("email_inv", null);			//Mail
            	session.setAttribute("fono_num_1_inv", null);	//Fono 1 
            	session.setAttribute("fono_cod_1_inv",null);	//Cod. Area Fono 1
            	session.setAttribute("fono_num_2_inv", null);	//Fono 2
            	session.setAttribute("fono_cod_2_inv", null);
            	session.setAttribute("tipo_calle_inv", null);		//Tipo de calle (Av, Calle, Pje)
            	session.setAttribute("calle_inv", null);				//Nombre Calle
            	session.setAttribute("numero_inv", null);				//Numero Domicilio
            	session.setAttribute("departamento_inv", null);	//Numero o letra Depto
            	session.setAttribute("region_inv", null);				//Region
            	session.setAttribute("comuna_inv", null);				//Comuna
            	session.setAttribute("alias_inv", null);		
            	//Fin Limpieza variables de sesion
            	session.setAttribute("rut1_inv", req.getParameter("rut1_inv"));				//Rut
            	session.setAttribute("nombre_inv", req.getParameter("nombre_inv"));			//Nombre
            	session.setAttribute("ape_pat_inv", req.getParameter("ape_pat_inv"));		//Apellido
            	session.setAttribute("email_inv", req.getParameter("email_inv"));			//Mail
            	session.setAttribute("fono_num_1_inv", req.getParameter("fono_num_1_inv"));	//Fono 1 
            	session.setAttribute("fono_cod_1_inv", req.getParameter("fono_cod_1_inv"));	//Cod. Area Fono 1
            	session.setAttribute("fono_num_2_inv", req.getParameter("fono_num_2_inv"));	//Fono 2
            	session.setAttribute("fono_cod_2_inv", req.getParameter("fono_cod_2_inv"));	//Cod. Area Fono 2	        	    
	            //comprobamos si viene con despacho a domicilio para sacar los datos de direccion
	            if ((req.getParameter("tipodesp").toString().equals("D"))){
	            	session.setAttribute("tipo_calle_inv", req.getParameter("tipo_calle_inv"));		//Tipo de calle (Av, Calle, Pje)
	            	session.setAttribute("calle_inv", req.getParameter("calle_inv"));				//Nombre Calle
	            	session.setAttribute("numero_inv", req.getParameter("numero_inv"));				//Numero Domicilio
	            	session.setAttribute("departamento_inv", req.getParameter("departamento_inv"));	//Numero o letra Depto
	            	session.setAttribute("region_inv", req.getParameter("region_inv"));				//Region
	            	session.setAttribute("comuna_inv", req.getParameter("comuna_inv"));				//Comuna
	            	session.setAttribute("alias_inv", req.getParameter("alias_inv"));				//Alias para guardar domicilio
	            }
	            
            }
           
            
            long idCliente = Long.parseLong(session.getAttribute("ses_cli_id").toString());
            String idSession = null;
            if (session.getAttribute("ses_invitado_id") != null)
                idSession = session.getAttribute("ses_invitado_id").toString();
            
            //Entra aqui cuando se despliega el calendario de desapacho en la pagina de pago. 
            if (req.getHeader("MyFOReferer") != null && "Pago".equals(req.getHeader("MyFOReferer"))) {
            	isOk = "N";
				PrintWriter out = arg1.getWriter(); 
				JSONObject oJsonResponse = new JSONObject(); 
            	boolean isValid=true;
            	
            	ArrayList param = new ArrayList();
            	param.add("jpicking");
            	param.add("jdespacho");
            	param.add("jprecio");
            	param.add("jfecha");
            	param.add("tipo_despacho");
            	param.add("horas_economico");
            	//param.add("jclave");
            	
            	Iterator it = param.iterator();
            	while(it.hasNext()){
            		String p = (String) it.next();
            		if(req.getParameter(p) == null){
            			isValid=false;
            			break;
            		}
            	}
            	
            	if(isValid){            		
            		            		
            		String jdespacho = req.getParameter("jdespacho").toString();
            		String jpicking = req.getParameter("jpicking").toString();
            		
            		session.setAttribute("jdespacho", jdespacho);
            		session.setAttribute("jpicking", jpicking);
            		String tipo_despacho = req.getParameter("tipo_despacho");
            		
            		String jfechaDecifrado ="";
            		String jprecioDecifrado="";
            		//String jdespacho="";
            		String claveLista[] = null;
            		String tipoDespacho="";
            		long jprecioOri=0;
            		
            		try{
			            //long jprecio = Long.parseLong(req.getParameter("jprecio"));
	            		//String jprecioCifrado = req.getParameter("jprecio");
	            		jprecioDecifrado = Cifrador.desencriptar(keyFo, req.getParameter("jprecio"));
	            		claveLista = Cifrador.desencriptar(keyFo, req.getParameter("jclave")).split(",");
	            		
	            		//String jfechaCifrado = req.getParameter("jfecha");
	            		jfechaDecifrado = Cifrador.desencriptar(keyFo, req.getParameter("jfecha"));
	            		tipoDespacho = claveLista[4];
	            		
	            		jprecioOri = Long.parseLong( GuardaDatosDespacho.replace(claveLista[0]));
	            		
	            		if(jprecioDecifrado==null || jprecioDecifrado.length()==0 || jfechaDecifrado==null  || jfechaDecifrado.length()==0  || tipoDespacho==null || tipoDespacho.length()==0){
			        		throw new Exception();
			        	}
	            	}catch(Exception err){
	            		
            		}
            		
            		listDespachoSession = (List) session.getAttribute("listDespachoSession");
                	long jprecioSession = 0;
                	String jfechaSession="";
                	String tdespachoSession="";
                	for(Iterator i = listDespachoSession.iterator(); i.hasNext(); ) {
            			String[] item = (String []) i.next();
            			//System.out.println("id jdespacho:"+item[0]+ ",id jpicking:"+item[1]+ ",monto:"+item[2]+ ",fecha:"+item[3]+ ",tipo:"+item[4]);        			
            			if(Long.parseLong(jpicking)==Long.parseLong(item[1])&& Long.parseLong(jdespacho)==Long.parseLong(item[0])  ){
            				jprecioSession =  Long.parseLong(item[2]);	
            				jfechaSession = item[3];
            				tdespachoSession = item[4];    
            				isOk="S";
            				break;
            			}
            		} 
                	  
                	if(isOk.equals("N")){
                		throw new Exception("No se encontraron datos de session");
                	}
                	//if(jprecioDecifrado==null || String.valueOf(jprecioOri)==null){
                		//jprecioDecifrado = String.valueOf(jprecioSession);                	
                	//}else{
                		if(null==jprecioDecifrado || null==String.valueOf(jprecioOri) || jprecioSession!=Long.parseLong(GuardaDatosDespacho.replace(jprecioDecifrado))  || jprecioSession!=jprecioOri ){
                    		jprecioDecifrado = String.valueOf(jprecioSession);
                    	}	
                		//}
                	
                	if(null==jfechaDecifrado || !jfechaSession.equals(jfechaDecifrado)){
                		jfechaDecifrado = jfechaSession;
                	}
                	if(null==tipoDespacho || !tdespachoSession.equals(tipoDespacho)){
                		tipo_despacho = tdespachoSession;
                	}
                	
            		session.setAttribute("jprecio", GuardaDatosDespacho.replace(jprecioDecifrado) + "");
            		session.setAttribute("jfecha", jfechaDecifrado);
            		session.setAttribute("tdespacho", tipo_despacho);
		            //session.setAttribute("jprecio", jprecioDecifrado + "");
            		//session.setAttribute("jfecha", req.getParameter("jfecha"));
		            //session.setAttribute("tdespacho", req.getParameter("tipo_despacho"));
		            session.setAttribute("ses_horas_economico", req.getParameter("horas_economico"));   
		            
		            oJsonResponse.put("status", "200");//OK 
		            
            	}else{
            		oJsonResponse.put("status", "500");//error       
            		oJsonResponse.put("msg", "Parametros invalidos.");//error 
            	}	
            	
	            arg1.setContentType("application/json");
				out.println(oJsonResponse.toString());
				out.close();
            	
            }else{		
                    
            	/* NOV 2015 */
            	isOk = "N";      		
            	String desjclaveCifrado = req.getParameter("des_jclave");
            	String destino = req.getParameter("destino").toString();
            	long jpicking = Long.parseLong(req.getParameter("des_jpicking"));
	            String jdespacho = req.getParameter("des_jdespacho").toString();
	            String jprecioCifrado = req.getParameter("des_jprecio");
	            String tdespacho = req.getParameter("des_tdespacho");
	            
	            String desjclaveDesencriptado="";
            	String jprecioDesencriptado="";
            	String jfecha="";
            	String claveLista[] = null;
            	String tipoDespacho="";
            	long jprecioOri=0;
            	long jprecio=0;
            	
            	try{
            		desjclaveDesencriptado= Cifrador.desencriptar(keyFo, desjclaveCifrado);
		        	jprecioDesencriptado= Cifrador.desencriptar(keyFo, jprecioCifrado);
		        	jfecha= Cifrador.desencriptar(keyFo, req.getParameter("des_jfecha"));    	        	
		        	claveLista = desjclaveDesencriptado.split(",");
		        	tipoDespacho = claveLista[4];
		        	
		        	jprecioDesencriptado = GuardaDatosDespacho.replace(jprecioDesencriptado);
    	        	jprecio = Long.parseLong(jprecioDesencriptado);	       
    	        	jprecioOri = Long.parseLong( GuardaDatosDespacho.replace(claveLista[0]));
    	        	
		        	if(jprecioDesencriptado==null || jprecioDesencriptado.length()==0 || jfecha==null  || jfecha.length()==0  || tipoDespacho==null || tipoDespacho.length()==0){
		        		throw new Exception();
		        	}
	            }catch(Exception err){
	            	//System.out.println("Obtener desde session");            		
        		}
            	
            	listDespachoSession = (List) session.getAttribute("listDespachoSession");
            	long jprecioSession = 0;
            	String jfechaSession="";
            	String tdespachoSession="";
            	for(Iterator i = listDespachoSession.iterator(); i.hasNext(); ) {
        			String[] item = (String []) i.next();
        			//System.out.println("id jdespacho:"+item[0]+ ",id jpicking:"+item[1]+ ",monto:"+item[2]+ ",fecha:"+item[3]+ ",tipo:"+item[4]);        			
        			if(jpicking==Long.parseLong(item[1])&& Long.parseLong(jdespacho)==Long.parseLong(item[0])  ){
        				jprecioSession =  Long.parseLong(item[2]);	
        				jfechaSession = item[3];
        				tdespachoSession = item[4];
        				isOk ="S";
        				break;
        			}
        		} 
            	if(isOk.equals("N")){
            		throw new Exception("No se encontraron datos de session");
            	}
            	if(null==String.valueOf(jprecio) || null==String.valueOf(jprecioOri) || jprecioSession!=jprecio || jprecioSession!=jprecioOri){
            		jprecio = jprecioSession;
            	}
            	if(null==jfecha ||  !jfechaSession.equals(jfecha)){
            		jfecha = jfechaSession;
            	}
            	if(null==tipoDespacho || !tdespachoSession.equals(tipoDespacho)){
            		tdespacho = tdespachoSession;
            	}
            	            	        
	        	//System.out.println("Des: des_jclave:"+desjclaveDesencriptado+", des_jprecio:"+jprecioDesencriptado+", des_jfecha:"+jfecha);
	        		        		        	       	
	        	/* NOV 2015 */
            	
	            //String destino = req.getParameter("destino").toString();
	            //long jpicking = Long.parsYeLong(req.getParameter("des_jpicking"));
	            //String jdespacho = req.getParameter("des_jdespacho").toString();
	            //long jprecio = Long.parseLong(req.getParameter("des_jprecio"));
	            
	            /*if(session.getAttribute("ses_colaborador").equals("true")){
	            	jprecio = 1;
	            }*/
	            //String jfecha = req.getParameter("des_jfecha");
	           
	            String horas_economico = req.getParameter("des_hh_economico");
	            long zona_economico = Long.parseLong(req.getParameter("des_zona_despacho"));
	            String telefono   = req.getParameter("tel_despacho");
	            String codigo_tel = req.getParameter("codtel_despacho");
	            int redireccion = Integer.parseInt(req.getParameter("redireccion").toString());
	            session.setAttribute("ses_forma_despacho", destino);
	            session.setAttribute("jpicking", jpicking + "");
	            session.setAttribute("jdespacho", jdespacho + "");
	            session.setAttribute("jprecio", jprecio + "");
	            session.setAttribute("jfecha", jfecha);
	            session.setAttribute("tdespacho", tdespacho);
	            session.setAttribute("ses_horas_economico", horas_economico);
	            session.setAttribute("ses_zona_id", zona_economico + "");
	            session.setAttribute("tel_despacho", codigo_tel);
	            session.setAttribute("ses_telefono", telefono);
	
	            
	            if (destino.equals("R")) {
	                long local_retiro = Long.parseLong(req.getParameter("des_localretiro"));
	                String autorizacion = req.getParameter("aut_despacho");
	                long rut_autorizado = Long.parseLong(req.getParameter("rutautorizado"));
	                String dv_autorizado = req.getParameter("dvautorizado");
	                String email = req.getParameter("email_despacho");
	                session.setAttribute("ses_loc_id_prod", session.getAttribute("ses_loc_id").toString());
	                session.setAttribute("ses_loc_id", local_retiro + "");
	                session.setAttribute("autorizacion", autorizacion);
	                session.setAttribute("rut_autorizado", rut_autorizado + "");
	                session.setAttribute("dv_autorizado", dv_autorizado);
	                session.setAttribute("ses_cli_email", email);
	                session.setAttribute("despacho_local","R");
	                if (redireccion == 0)
	                    biz.eliminaProdCarroNoDisp(idCliente,(String)req.getParameter("des_localretiro"),idSession);
	            } else if (destino.equals("D")) {
	            	 session.setAttribute("despacho_local",null);
	            	 if(session.getAttribute("ses_cli_rut").toString().equals("123123")){
	                    long tipo_calle = Long.parseLong(req.getParameter("tipocalle_invitado"));
	                    String calle = req.getParameter("calle_invitado");
	                    String numero = req.getParameter("numero_invitado");
	                    String departamento = req.getParameter("depto_invitado");
	                    long region = Long.parseLong(req.getParameter("region_invitado"));
	                    long comuna = Long.parseLong(req.getParameter("comuna_invitado"));
	                    String alias = req.getParameter("alias_invitado");
	
	                    String email = req.getParameter("email_despacho");
	                    session.setAttribute("tipo_calle", tipo_calle + "");
	                    session.setAttribute("calle", calle);
	                    session.setAttribute("numero", numero);
	                    session.setAttribute("departamento", departamento);
	                    session.setAttribute("region", region + "");
	                    session.setAttribute("comuna", comuna + "");
	                    session.setAttribute("ses_dir_alias", alias);
	                    session.setAttribute("ses_cli_email", email);
	                    session.setAttribute("ses_loc_id_prod", session.getAttribute("ses_loc_id").toString());
	                    if (redireccion == 0)
	                        biz.eliminaProdCarroNoDisp(idCliente,session.getAttribute("ses_loc_id").toString(),idSession);
	                } else {
	                    long id_dir = Long.parseLong(req.getParameter("id_direccion"));
	                    long id_local = Long.parseLong(req.getParameter("local_direccion"));
	                    String email = req.getParameter("email_despacho");
	                    
	                    biz.clienteChangeDatosPaso3(idCliente,email,codigo_tel,telefono);
	                    
	                    session.setAttribute("ses_dir_id", id_dir + "");
	                    session.setAttribute("ses_loc_id", id_local + "");
	                    session.setAttribute("ses_loc_id_prod", id_local + "");
	                    session.setAttribute("ses_cli_email", email);
	                    if (redireccion == 0)
	                        biz.eliminaProdCarroNoDisp(idCliente,(String)req.getParameter("local_direccion"),idSession);
	                }
	                
	                String autorizacion = req.getParameter("aut_despacho");
	                String observacion = req.getParameter("obs_despacho");
	                session.setAttribute("autorizacion", autorizacion);
	                session.setAttribute("observacion", observacion);
	            }
	            if (redireccion == 0)
	                arg1.sendRedirect(getServletConfig().getInitParameter("dis_ok"));
	            else
	                arg1.sendRedirect("MiCarro");
            }
            
		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException(e);
		}
	}
	
	public static String replace(String param) {
		param = param.replaceAll("-", "");
		param = param.replaceAll("[^0-9]", "");	   
	    Pattern pt = Pattern.compile("[^a-zA-Z0-9_-]");
	   // Pattern pt = Pattern.compile("[^0-9]");
	    Matcher match = pt.matcher(param);
	    while (match.find()) {
	        String s = match.group();
	        param = param.replaceAll("\\" + s, "");
	    }	    
	    return param;	    
	}
}