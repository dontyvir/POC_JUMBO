package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.ComunasRegionesTexto;
import cl.bbr.fo.util.Tracking_web;
import cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.PoligonoxComunaDTO;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;
import cl.bbr.jumbocl.common.utils.Utils;

/**
 * P?gina que entrega los productos para una categor?a
 * 
 * @author imoyano
 * 
 */
public class CategoryDisplay extends Command {
    

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {
		try {
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");
			
			// Recupera la sesi?n del usuario
			HttpSession session = arg0.getSession();
            
            BizDelegate biz = new BizDelegate();
            
			IValueSet top = new ValueSet();	
			
            //Pregunta si existe la sesion de invitado antes de agregar el producto
            //if ( session.getAttribute("ses_loc_id") == null && session.getAttribute("ses_dir_id") == null ) {
            if (session.getAttribute("ses_cli_id") == null || session.getAttribute("ses_cli_rut") == null ) {
				toMakeDonaldSession(session);				
				//Crea la sesion en la BD
				String id_sess= (String)session.getAttribute("ses_id");                	
				int invitado_id = biz.crearSesionInvitado(id_sess);
				session.setAttribute("ses_invitado_id", invitado_id + "");
				logger.info("Se inicia session invitado: " + session.getId() + " IP: ["+arg0.getRemoteAddr()+"]");
            }
            if ( !"1".equalsIgnoreCase( session.getAttribute("ses_cli_id").toString() )) {
                //Se almacena tracking en este sector
                //Tracking_web.saveTracking("Categorias", arg0);    
            }
            if( session.getAttribute("cod_error") != null ) {
                session.removeAttribute("cod_error");
            }
         // Seteo datos cliente para chaordic_meta
    		top.setVariable("{ses_cli_id}", (null == session.getAttribute("ses_cli_id") ? "1" : session.getAttribute("ses_cli_id")));
    		top.setVariable("{ses_cli_nombre}", (null == session.getAttribute("ses_cli_nombre") ? "Invitado" : session.getAttribute("ses_cli_nombre")));
    		top.setVariable("{ses_cli_email}", (null == session.getAttribute("ses_cli_email") || "".equals(session.getAttribute("ses_cli_email")) ? "invitado@invitado.cl" : session.getAttribute("ses_cli_email")));

			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();
			
			String pag_form = "";
			if ( arg0.getParameter("cai") == null || arg0.getParameter("tip").compareTo("I") == 0 ){
				// Recupera pagina desde web.xml y se inicia parser
				pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			} else { 
				// Recupera pagina desde web.xml y se inicia parser
				pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form_cat");
			}
			this.getLogger().debug( "Template:"+pag_form );
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

	

			//max correccion bug invitado
			if(session.getAttribute("ses_cli_nombre").toString().indexOf(" ") != -1) {
				top.setVariable("{nombre_cliente}", "Hola "+session.getAttribute("ses_cli_nombre").toString().substring(0,session.getAttribute("ses_cli_nombre").toString().indexOf(" ")));
			} else {
			top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
			}
			
			
			//Maxbell Caballero 2014/05/23 - Esta variable es para ver si viene desde el catalogo 
            if(arg0.getParameter("id_producto_catalogo") != null) {
            	
            	try {
            		top.setVariable("{id_producto_catalogo}", String.valueOf(Integer.parseInt(arg0.getParameter("id_producto_catalogo").toString())));
            	} catch(Exception ex) {
            		top.setVariable("{id_producto_catalogo}", "false");
            	}
            	
            	try {
            		top.setVariable("{num_prod}", String.valueOf(Integer.parseInt(arg0.getParameter("num_prod").toString())));
            	} catch(Exception ex) {
            		top.setVariable("{num_prod}", "1");
            	}
            	
//            	if(session.getAttribute("ses_cli_nombre") != null && session.getAttribute("ses_cli_nombre").toString().equalsIgnoreCase("invitado")) {
//            		top.setVariable("{invitado_de_catalogo}", "true");
//            	}
            } else {
            	top.setVariable("{id_producto_catalogo}", "false");
            }
			
            if(session.getAttribute("ses_dir_alias") != null){
            	top.setVariable("{lug_desp}", session.getAttribute("ses_dir_alias").toString());
            }else{
            	arg1.sendRedirect("/FO");
            }

			// B?squedas
//+20120320coh
			if (arg0.getParameter("patron") != null && arg0.getParameter("patron") != ""){
				top.setVariable("{patron_busqueda}", arg0.getParameter("patron"));
			}
			else
				top.setVariable("{patron_busqueda}", "");
//-20120320coh
			if (session.getAttribute("ses_patron") != null && session.getAttribute("ses_patron") != ""){
				List datos_patron = new ArrayList();
				List list_ses_patron = (List)(session.getAttribute("ses_patron"));
				
				IValueSet fila_pat = null;
				String aux = null;
				for( int i = 0; i < list_ses_patron.size() ; i++ ) {
					 fila_pat = new ValueSet();
					 aux = (String)list_ses_patron.get(i);
					 fila_pat.setVariable("{patron}", aux.trim()+"");
					 datos_patron.add(fila_pat);
				}
								
				List datos_p = new ArrayList(); 
				IValueSet fila = new ValueSet();
				fila.setDynamicValueSets("SESSION_PATRON",datos_patron);
				datos_p.add(fila);
				top.setDynamicValueSets("CON_PATRON", datos_p); 				
			}
			
			// Recepcion de variables de pagina
            long idCabecera = 0;
			long idIntermedia = 0;
			long idTerminal = 0;
            if (arg0.getParameter("patron") == null || arg0.getParameter("patron") == ""){
                
                CategoriaDTO cat = null;
                String cat_name = "";
            	
            	/*****************************************/
                /**********   CATEGORIA PADRE   **********/
                /*****************************************/
                if ( arg0.getParameter("cab") != null ){
                    idCabecera = Long.parseLong(arg0.getParameter("cab"));
                    
                    // busco nombre de cabecera
	                cat = biz.getCategoria(idCabecera);
	                cat_name = cat.getNombre();                    
                }else{
                    idCabecera = Long.parseLong(rb.getString("CatPadre"));
                }
                top.setVariable("{cabecera_id}", idCabecera + "");
                top.setVariable("{cabecera_nombre}", cat_name);
                /*****************************************/
                /*******   CATEGORIA  INTERMEDIA   *******/
                /*****************************************/
                cat_name = "";
    			if ( arg0.getParameter("cab") != null && 
                        arg0.getParameter("int") != null ){
                    idIntermedia = Long.parseLong(arg0.getParameter("int"));
                    
                    // busco nombre de categoría intermedia 
                    cat = biz.getCategoria(idIntermedia);
                    cat_name = cat.getNombre();
                    
    			}else if (arg0.getParameter("cab") == null){
                    idIntermedia = Long.parseLong(rb.getString("CatIntermedia"));
                }
                top.setVariable("{categoria_id}", idIntermedia + "");
                top.setVariable("{categoria_nombre}", cat_name);

                /*****************************************/
                /*********   CATEGORIA TERMINAL   ********/
                /*****************************************/
                cat_name = "";
    			if ( arg0.getParameter("cab") != null && 
                        arg0.getParameter("int") != null && 
                        arg0.getParameter("ter") != null  ) {
    				idTerminal = Long.parseLong(arg0.getParameter("ter"));
    				
    				if(idTerminal != -1){
    	    			// busco nombre categoría terminal
    					cat = biz.getCategoria(idTerminal);
    					cat_name = cat.getNombre();				
    				}
    			}else if (arg0.getParameter("cab") == null && 
                        arg0.getParameter("int") == null){
                    idTerminal = Long.parseLong(rb.getString("CatTerminal"));
                }
                top.setVariable("{subcategoria_id}", idTerminal + "");
                top.setVariable("{subcategoria_nombre}", cat_name);
            }
			
			this.getLogger().debug( "Cabecera:" + idCabecera + " Intermedia:" + idIntermedia + " Terminal:" + idTerminal );
			
			// --- INI - LAYER EVENTOS PERSONALIZADOS - PASO 2 ---
			/*EventoDTO evento = new EventoDTO();
			String codLayerFlash = UtilsEventos.codHtmlDeFlash(Long.parseLong(session.getAttribute("ses_cli_rut").toString()), EventosConstants.PASO_2);
			if ( codLayerFlash.length() > 0 ) {
			    evento = UtilsEventos.eventoMostrado( Long.parseLong(session.getAttribute("ses_cli_rut").toString()) );
			    top.setVariable("{titulo_evento}",	UtilsEventos.tituloMostrar( evento.getTitulo(), session.getAttribute("ses_cli_nombre_pila").toString() ));
				top.setVariable("{mostrar_evento}", "SI");
		    } else {
		        top.setVariable("{titulo_evento}",	"");
		        top.setVariable("{mostrar_evento}", "NO");
		    }
		    top.setVariable("{src_flash}", codLayerFlash);*/
	        // --- FIN - LAYER EVENTOS PERSONALIZADOS - PASO 2 ---
            
	        // --- INI - SUSTITUTOS
//            if ( biz.clienteTieneSustitutos( Long.parseLong(session.getAttribute("ses_cli_id").toString()) ) ) {
//                List mostrarSustitutos = new ArrayList(); 
//                IValueSet filaSust = new ValueSet();
//                filaSust.setVariable("{from}", "CategoryDisplay"); 
//                mostrarSustitutos.add(filaSust);
//                top.setDynamicValueSets("MOSTRAR_CRITERIOS", mostrarSustitutos); 
//            }
            // --- FIN - SUSTITUTOS
            
            // --- INI - TRACKING OP
//            if ( biz.clienteConOPsTracking( Long.parseLong(session.getAttribute("ses_cli_id").toString())  ) ) {
//                List mostrarTracking = new ArrayList(); 
//                IValueSet fila = new ValueSet();
//                fila.setVariable("{from}", "CategoryDisplay"); 
//                mostrarTracking.add(fila);
//                top.setDynamicValueSets("MOSTRAR_TRACKING", mostrarTracking); 
//            }
            // --- FIN - TRACKING OP
            
            if ( !biz.clienteHaComprado( Long.parseLong(session.getAttribute("ses_cli_id").toString())) ) {
                List mostrarBanner = new ArrayList(); 
                IValueSet dato = new ValueSet();
                dato.setVariable("{vacio}", ""); 
                mostrarBanner.add(dato);
                top.setDynamicValueSets("MOSTRAR_BANNER_DESPACHO", mostrarBanner);
            }
            
            // -- Calendario despacho o retiro
            String formaDespacho = "D"; //D:Despacho domicilio o R:Retiro Local
            if (session.getAttribute("ses_forma_despacho") != null) {
                formaDespacho = session.getAttribute("ses_forma_despacho").toString();
                List mostrar = new ArrayList();
                IValueSet fila = new ValueSet();
                fila.setVariable("{vacio}", "");
                mostrar.add(fila);
                if (formaDespacho.equalsIgnoreCase("R")) {
                    top.setDynamicValueSets("MUESTRA_CAL_RETIRAR", mostrar);
                } else {
                    top.setDynamicValueSets("MUESTRA_CAL_DESPACHO", mostrar);
                }
            }
            
            // INI - DATOS PARA EL CHAT
            top.setVariable("{nom_cliente}", session.getAttribute("ses_cli_nombre_pila").toString());
            top.setVariable("{ape_cliente}", session.getAttribute("ses_cli_apellido_pat").toString());
            top.setVariable("{dia_actual}", Utils.getFechaActualByPatron("E"));
            top.setVariable("{hora_actual}", Utils.getFechaActualByPatron("HHmm"));
            // FIN - DATOS PARA EL CHAT
            
            top.setVariable("{cli_id}", session.getAttribute("ses_cli_id").toString());
            
            List viewLogin = new ArrayList(); 
            IValueSet fila = new ValueSet();
            
            if ( "".equalsIgnoreCase( session.getAttribute("ses_comuna_cliente").toString() ) ) {
                fila.setVariable("{comuna_usuario}", NOMBRE_COMUNA_DONALD);
                fila.setVariable("{comuna_usuario_id}", "0");
            } else {
                String[] loc = session.getAttribute("ses_comuna_cliente").toString().split("-=-");
                fila.setVariable("{comuna_usuario}", "" + loc[2]);
                fila.setVariable("{comuna_usuario_id}", "" + loc[1]);
            }
            
            fila.setVariable("{display_calendario_despacho}", "none");
            if("1".equals(rb.getString("ventana_info_disponibilidad_despacho"))){
            	if (session.getAttribute("ses_cli_id") != null && !"1".equalsIgnoreCase( session.getAttribute("ses_cli_id").toString() )) {
            	  fila.setVariable("{display_calendario_despacho}", "block"); 
            	}
            }
            viewLogin.add(fila);
            
            if ( "1".equalsIgnoreCase( session.getAttribute("ses_cli_id").toString() )) {
                top.setDynamicValueSets("MOSTRAR_NO_LOGUEADO", viewLogin); 
            } else {
                if ((session.getAttribute("ses_cli_rut") != null)  && (session.getAttribute("ses_cli_rut").equals("123123")))
                    top.setDynamicValueSets("MOSTRAR_LOGUEADO_INVITADO", viewLogin);
                else
                    top.setDynamicValueSets("MOSTRAR_LOGUEADO", viewLogin);
            }     
            
            //Ventana Info DESPACHOS DISPONIBLES 
            if("1".equals(rb.getString("ventana_info_disponibilidad_despacho"))){
	            if (session.getAttribute("ses_cli_id") != null && !"1".equalsIgnoreCase( session.getAttribute("ses_cli_id").toString() )) {
		            if(session.getAttribute("show_calendario_despacho_Category") == null){		            	
			            session.setAttribute("show_calendario_despacho_Category","P");
			            top.setVariable("{display_calendario_despacho}", "block");	
			         }else{
			            top.setVariable("{display_calendario_despacho}", "none");	          	  
			        }	            
	            }else{            	
	            	top.setVariable("{display_calendario_despacho}", "none");
	            }
            }else{
            	top.setVariable("{display_calendario_despacho}", "none");
            }
          //Fin Ventana Info DESPACHOS DISPONIBLES            
            
          //Tags Analytics - Captura de estado de user
            if(session.getAttribute("ses_cli_nombre_pila").toString().equals("Invitado")){
                top.setVariable("{ta_mx_user}", "invitado");
            }
            else{
                top.setVariable("{ta_mx_user}", "registrado");
            }
            //Locales Retiro - framos
            ArrayList locales = new ArrayList();
            List localesRetiro = biz.localesRetiro();
            for (int i = 0; i < localesRetiro.size(); i++) {
            	IValueSet fila1 = new ValueSet();
            	LocalDTO local = (LocalDTO)localesRetiro.get(i);
            	PoligonoxComunaDTO pol = new PoligonoxComunaDTO();
            	long id_zona = local.getIdZonaRetiro();
            	JdbcPedidosDAO dao = new JdbcPedidosDAO();
            	List listaPoligonosZona = dao.getPoligonosXZona(id_zona);
            	for (int j = 0 ; j < listaPoligonosZona.size();j++){
            		pol = (PoligonoxComunaDTO)listaPoligonosZona.get(j);
            	}
            	fila1.setVariable("{comuna_local}", Long.toString(pol.getId_comuna()));
            	fila1.setVariable("{nombre_local}", local.getNom_local());
            	locales.add(fila1);
            }
            top.setDynamicValueSets("LOCALES_RETIRO",locales);		
            //Locales Retiro
			//Tags Analytics - Captura de Comuna y Regi?n en Texto
			/************   LISTADO DE REGIONES   ****************/
			  
			String ta_mx_loc = ComunasRegionesTexto.ComunaRegionTexto(session);
			if(ta_mx_loc.equals(""))
			ta_mx_loc="none-none";
			top.setVariable("{ta_mx_loc}", ta_mx_loc);
			  
			top.setVariable("{mx_content}", "Categorias");
			
			String result = tem.toString(top);
			out.print(result);			
			
		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}


	}



}