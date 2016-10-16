package cl.bbr.fo.command;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.SystemException;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;

public class DespachosDisponibles extends Command{

	private static final long serialVersionUID = 8543295186324704974L;    

    protected void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try {     
        	
        	 // Recupera la sesión del usuario
            HttpSession session = request.getSession();  
            ResourceBundle rb = ResourceBundle.getBundle("fo"); 
            
            
            if(!"1".equals(rb.getString("ventana_info_disponibilidad_despacho"))){
            	return;
            }
            
            if (session.getAttribute("ses_cli_id") == null || "1".equals(session.getAttribute("ses_cli_id").toString())) {
        		response.sendRedirect(rb.getString("command.sin_permisos")); //Say good bye	
            }  	
        	
        	if (!(request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").toString().equals("XMLHttpRequest"))){
        		response.sendRedirect(rb.getString("command.sin_permisos")); //Say good bye	
        	}     
        	
        	//Veamos como nos comportamos
            String actionCDD =(request.getParameter("action") != null)? request.getParameter("action").toString().trim():"";
			List List_Acciones = new ArrayList();//Acciones permitidas desde el cliente
			List_Acciones.add("L");
			List_Acciones.add("RD");
			List_Acciones.add("CD");
			List_Acciones.add("RR");
			List_Acciones.add("CR"); 
			List_Acciones.add("INIT_ML");//Inicio Disponibilidad despacho...
			
			            
            if(!isActionCDDValid(List_Acciones, actionCDD)){
            	response.sendRedirect(rb.getString("command.sin_permisos")); //Say good bye
            }            
            
            //Comenzamos...        	
        	request.setCharacterEncoding("iso-8859-1");
        	response.setContentType("text/html; charset=iso-8859-1");
        	
        	// Se recupera la salida para el servlet
            PrintWriter out = response.getWriter();
            BizDelegate biz = new BizDelegate();              
            
            // Carga properties            
            long cantProds 					= Long.parseLong(rb.getString("despachochart.productos.promedio"));
            long hhWebCliConfiables 		= Long.parseLong(rb.getString("despachochart.hrscliconfiables"));
            int diasCalendario 				= Integer.parseInt(rb.getString("despachochart.dias.calendario"));
            int diasPresentacionCalendario 	= Integer.parseInt(rb.getString("despachochart.diaspresentacion"));
            
            // Recupera paginas desde web.xml
            String pagForm 				= rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_despachos");
            String pagFormCalendario 	= rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_cal_despachos");

                       
            long cliente_id 		= Long.parseLong((String)session.getAttribute("ses_cli_id"));            
            boolean esCliConfiable	=(session.getAttribute("ses_cli_confiable") != null && session.getAttribute("ses_cli_confiable").toString().equalsIgnoreCase("S"))?true:false;          
                                  
            //Obtiene fecha BD
            Date fechaActualSistema = biz.fechaActualBD();
                        
            //Cargo template principal
            TemplateLoader load = new TemplateLoader(pagForm);
            ITemplate tem 		= load.getTemplate();
            IValueSet top_main	= new ValueSet();
            
            //tomo id zona 
            long idZona = Long.parseLong(session.getAttribute("ses_zona_id").toString());
            
                 
            //Comenzamos Acciones            
            if(actionCDD.equals("L") || actionCDD.equals("INIT_ML") ){       
            	
                //Obtiene todas las direcciones del cliente
                List List_DireccionesDespacho = biz.clientegetAllDirecciones(cliente_id);
                
                //Obtiene todos los locales que tienen retiro
                List List_LocalesRetiro = biz.localesRetiro();
                        	
                String dir_id=(session.getAttribute("ses_dir_id") != null)?(String)session.getAttribute("ses_dir_id"):"";
                
            	top_main.setDynamicValueSets("DIR_DESPACHOS", getDireccionesDespacho4Template(List_DireccionesDespacho,dir_id));  
            	top_main.setDynamicValueSets("LOCALES_RETIRO",getLocalesRetiro4Template(List_LocalesRetiro,idZona));
            	
				ZonaDTO zona = biz.getZonaDespachoById(idZona);            
				
				//Traemos la info para el calendario sin solapas y que llena X dias
				String  cdd = "";
				  
				//Para marcar el radio segun seleccion previa
				top_main.setVariable("{cdd_is_checked_rd}", "");
				top_main.setVariable("{cdd_is_checked_rr}", "");
				top_main.setVariable("{cdd_is_combobox_cd}", "disabled='disabled'");
				top_main.setVariable("{cdd_is_combobox_cr}", "disabled='disabled'");
							
				if(session.getAttribute("cdd_is_checked_rd") != null && session.getAttribute("cdd_is_checked_rd").toString().equalsIgnoreCase("true")){
					top_main.setVariable("{cdd_is_checked_rd}", "checked='checked'");
					top_main.setVariable("{cdd_is_combobox_cd}", "");
					cdd = generaCalendarioDespacho(biz, zona, diasCalendario, cantProds, false,diasPresentacionCalendario, esCliConfiable, hhWebCliConfiables, fechaActualSistema,pagFormCalendario);
					
				}else if(session.getAttribute("cdd_is_checked_rr") != null && session.getAttribute("cdd_is_checked_rr").toString().equalsIgnoreCase("true")){
					top_main.setVariable("{cdd_is_checked_rr}", "checked='checked'");
					top_main.setVariable("{cdd_is_combobox_cr}", "");
					cdd = generaCalendarioDespacho(biz, zona, diasCalendario, cantProds, true,diasPresentacionCalendario, esCliConfiable, hhWebCliConfiables, fechaActualSistema,pagFormCalendario);
				
				}else{
					top_main.setVariable("{cdd_is_checked_rd}", "checked='checked'");
					top_main.setVariable("{cdd_is_combobox_cd}", "");
					cdd = generaCalendarioDespacho(biz, zona, diasCalendario, cantProds, false,diasPresentacionCalendario, esCliConfiable, hhWebCliConfiables, fechaActualSistema,pagFormCalendario);					
				}
				
				top_main.setVariable("{calendario_despachos_disponibles}", cdd);
				top_main.setVariable("{ContextPath}", request.getContextPath());
				String result = tem.toString(top_main);
				out.print(result);
            }
            
            else if(actionCDD.equals("RD") || actionCDD.equals("CD")){
            	
            	 if (request.getParameter("direccion_despacho") != null){
					long idDireccion = Long.parseLong(request.getParameter("direccion_despacho").toString());      
					List List_DireccionesDespacho = biz.clientegetAllDirecciones(cliente_id);
					DireccionesDTO oDireccion = getDireccionDespachoSeleccionada(List_DireccionesDespacho,idDireccion);	
					String  cdd = "";
					if(oDireccion != null){
						ZonaDTO zona = biz.getZonaDespachoById(oDireccion.getZona_id());
						
						session.setAttribute("cdd_is_checked_rd", String.valueOf("true"));
						session.setAttribute("cdd_is_checked_rr", String.valueOf("false"));
												
						session.setAttribute("ses_loc_id", String.valueOf( oDireccion.getLoc_cod() ));
						session.setAttribute("ses_dir_id", String.valueOf( idDireccion ));
						session.setAttribute("ses_dir_alias", oDireccion.getCalle());   
						session.setAttribute("ses_zona_id", String.valueOf( oDireccion.getZona_id() ));  
						
						//Traemos la info para el calendario sin solapas y que llena X dias
						cdd = generaCalendarioDespacho(biz, zona, diasCalendario, cantProds, false, diasPresentacionCalendario, esCliConfiable, hhWebCliConfiables, fechaActualSistema,pagFormCalendario);									
					}
					out.print(cdd); 

            	 }else{
            		 out.print("");            		 
            	 }
            }            
            
            else if(actionCDD.equals("RR") || actionCDD.equals("CR")){
            	 if (request.getParameter("retiro_local") != null){
					idZona = Long.parseLong(request.getParameter("retiro_local").toString());
					 
					boolean esRetiroLocal = biz.zonaEsRetiroLocal(idZona);
					String  cdd = "";
					if(esRetiroLocal){
						ZonaDTO zona = biz.getZonaDespachoById(idZona);
					 
						session.setAttribute("cdd_is_checked_rd", String.valueOf("false"));
						session.setAttribute("cdd_is_checked_rr", String.valueOf("true"));
						
						session.setAttribute("ses_loc_id", String.valueOf( zona.getId_local() ));
						session.setAttribute("ses_zona_id", String.valueOf( zona.getId_zona()));  
					 
						//Traemos la info para el calendario sin solapas y que llena X dias
						cdd = generaCalendarioDespacho(biz, zona, diasCalendario, cantProds, esRetiroLocal, diasPresentacionCalendario, esCliConfiable, hhWebCliConfiables, fechaActualSistema,pagFormCalendario);
					}
            		out.print(cdd);   
            	 }else{
            		 out.print("");            		 
            	 }            	
            }            
            
            else{
            	response.sendRedirect(rb.getString("command.sin_permisos")); //Say good bye	  
            }

        } catch (Exception e) {
        	logger.error("DespachosDisponibles",e);
        }
    }
    
    private String  generaCalendarioDespacho(BizDelegate biz, ZonaDTO zona, int diasCalendario, long cantProds,boolean esRetiroLocal,
            int diasPresentacionCalendario, boolean esCliConfiable, long tiempoLimiteVip,Date fechaActualSistema, String pagForm) throws SystemException, ParseException, IOException {
        
		 TemplateLoader load = new TemplateLoader(pagForm);
		 ITemplate tem = load.getTemplate();
          
        IValueSet top = new ValueSet();
        int ancho = 510/diasCalendario;
        Calendar fechaActual = new GregorianCalendar();
        fechaActual.setFirstDayOfWeek(Calendar.MONDAY);

        DateFormat formatter        = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long tiempoActual           = fechaActualSistema.getTime();
        long diasPresentacion       = fechaActual.getTimeInMillis() + diasPresentacionCalendario * 24 * 60 * 60 * 1000;

        List cabecera = getCabecera(diasCalendario, fechaActual, ancho);
        top.setDynamicValueSets("CABECERA", cabecera);
        
        List horarios = biz.getCalendarioDespachoByDias(diasCalendario, zona.getId_zona());
        
        // Llenamos las ventanas horarias
        List listHorarios = new ArrayList();
        for ( int i = 0; i < horarios.size(); i++ ) {
            HorarioDespachoEntity ventana = (HorarioDespachoEntity) horarios.get(i);
            IValueSet regHorario = new ValueSet();
            regHorario.setVariable("{ventana}", new SimpleDateFormat(Formatos.HOUR_CAL).format(ventana.getH_ini()) + " - " + new SimpleDateFormat(Formatos.HOUR_CAL).format(ventana.getH_fin()) );
            
            
            List listDias = new ArrayList();
            
            Calendar fecha = new GregorianCalendar();
            fecha.setFirstDayOfWeek(Calendar.MONDAY);
            for ( int j = 0; j < diasCalendario; j++ ) {            
                boolean tieneVentana = false;
                IValueSet dia = new ValueSet();
                
                for ( int k = 0; k < ventana.getJornadas().size(); k++ ) {
                    JornadaDespachoEntity jor = (JornadaDespachoEntity) ventana.getJornadas().get(k);
                    String strFecha = new SimpleDateFormat(Formatos.DATE_CAL).format(fecha.getTime());
                    if ( new SimpleDateFormat(Formatos.DATE_CAL).format(jor.getFecha()).equals( strFecha ) ) {
                        tieneVentana = true;                        

                        String fechaPick            = jor.getFecha_picking() + " " + jor.getHoraIniPicking().toString();
                        Date datePicking            = formatter.parse(fechaPick);                 
                        long tiempoDespacho         = datePicking.getTime();
                        long tiempoLimite           = jor.getHrs_ofrecido_web() * 60 * 60 * 1000L;                         

                        boolean pasadoOSinCapacidad = esPasadoOSinCapacidad(cantProds, jor, tiempoDespacho, tiempoActual, tiempoLimite, diasPresentacion);                    
                        boolean mostrarExpressVip = false;
                        
                        if ( esCliConfiable ) {
                            mostrarExpressVip = mostrarExpressVip(cantProds, jor, tiempoDespacho, tiempoActual, tiempoLimiteVip, zona);
                        }                        

                        if ( pasadoOSinCapacidad ) {                                
                            if ( mostrarExpressVip ) {
                                dia.setVariable("{view}", "<img src=\"/FO_IMGS/img/estructura/check.jpg\"/> ");                                
                            } else {
                                tieneVentana = false;
                            }                                
                        } else {
                        	if ( esRetiroLocal ) {                          
                        		dia.setVariable("{view}", "<img src=\"/FO_IMGS/img/estructura/check.jpg\"/>");
                            } else {
                            	dia.setVariable("{view}", "<img src=\"/FO_IMGS/img/estructura/check.jpg\"/>");
                            }
                        }                                
                    }
                }
                if ( !tieneVentana ) {
                    dia.setVariable("{view}", "<img src=\"/FO_IMGS/img/estructura/no_disponible.jpg\" />");
                }
                dia.setVariable("{ancho}",""+ancho);
                listDias.add(dia);
                fecha.add(Calendar.DAY_OF_YEAR, 1);
            }
            regHorario.setDynamicValueSets("DIAS", listDias);
            listHorarios.add(regHorario);
        }
        top.setDynamicValueSets("VENTANAS", listHorarios);
        return tem.toString(top);
    } 
    
    private List getCabecera(int diasCalendario, Calendar fechaActual, int ancho) {
        List listFechas = new ArrayList();
        for ( int j = 0; j < diasCalendario; j++ ) {
            IValueSet regDia = new ValueSet();
            regDia.setVariable("{dia}", new SimpleDateFormat(Formatos.EEEEEEEEE, new Locale("es","ES")).format(fechaActual.getTime()) );
            regDia.setVariable("{fc}", new SimpleDateFormat(Formatos.DATE_CAL).format(fechaActual.getTime() ) );
            regDia.setVariable("{fecha}", new SimpleDateFormat(Formatos.YYYYMMDD).format(fechaActual.getTime()) );
            regDia.setVariable("{ancho}", ""+ancho );
            listFechas.add(regDia);
            fechaActual.add(Calendar.DAY_OF_YEAR, 1);
        }
        return listFechas;
    }

    private boolean mostrarExpressVip(long cantProds, JornadaDespachoEntity jor, long tiempoDespacho, long tiempoActual, long tiempoLimiteVip, ZonaDTO zonadto) {
        if ( (cantProds < (jor.getCapac_picking() - jor.getCapac_picking_ocupada()))
                && ( zonadto.getEstado_tarifa_express() == 1 )
                && ( jor.getTarifa_express() > 0 )
                && ((jor.getCapac_despacho() - jor.getCapac_despacho_ocupada()) > 0)
                && ((tiempoDespacho - tiempoActual) > tiempoLimiteVip) ) {
            return true;
        }
        return false;
    }

    private boolean esPasadoOSinCapacidad(long cantProds, JornadaDespachoEntity jor, long tiempoDespacho, long tiempoActual, long tiempoLimite, long diasPresentacion) {
        if ((cantProds > (jor.getCapac_picking() - jor.getCapac_picking_ocupada()))
                || ((jor.getCapac_despacho() - jor.getCapac_despacho_ocupada()) <= 0)
                || ((tiempoDespacho - tiempoActual) < tiempoLimite)
                || (jor.getFecha().getTime() > diasPresentacion)) {
            return true;            
        }
        return false;
    } 
    
    //Valida que la accion pasada por el cliente sea valida.
    private boolean isActionCDDValid(List List_Acciones, String actionCD){   
    	boolean isActionCDValid = false;
        Iterator ita = List_Acciones.iterator();
        while(ita.hasNext()){
        	String a = (String) ita.next();    
        	if(actionCD.equals(a)) {
        		isActionCDValid = true;
        		break;
        	}
        }
        return isActionCDValid;
    }
    
    //Direcciones de despacho seleccionada
    private DireccionesDTO getDireccionDespachoSeleccionada(List List_DireccionesDespacho, long idDireccion){    	 
        
    	DireccionesDTO oDireccionesDTO = null;
        Iterator it = List_DireccionesDespacho.iterator();
        while(it.hasNext()) {
        	oDireccionesDTO = (DireccionesDTO) it.next();

        	if(idDireccion == oDireccionesDTO.getId()){
        		break;
        	}
           
        }
        return oDireccionesDTO;
    }
    
    //Direcciones de despacho para el template
    private List getDireccionesDespacho4Template(List List_DireccionesDespacho, String dir_id){    	 
        
        Iterator it = List_DireccionesDespacho.iterator();
        List List_Direcciones = new ArrayList();
        while(it.hasNext()) {
        	DireccionesDTO oDireccionesDTO = (DireccionesDTO) it.next();
        	IValueSet regDireccion = new ValueSet();
        	regDireccion.setVariable("{id_zona}", String.valueOf(oDireccionesDTO.getZona_id()));
        	regDireccion.setVariable("{dir_calle}", oDireccionesDTO.getCalle());
        	regDireccion.setVariable("{dir_numero}",oDireccionesDTO.getNumero() );
        	regDireccion.setVariable("{dir_comuna}", oDireccionesDTO.getCom_nombre());
        	regDireccion.setVariable("{dir_region}", oDireccionesDTO.getReg_nombre());
        	regDireccion.setVariable("{id_direccion}", String.valueOf(oDireccionesDTO.getId()));
        	
        	regDireccion.setVariable("{selected}", "");
        	String id_dir_usu= String.valueOf(oDireccionesDTO.getId());
        	if(dir_id.equals(id_dir_usu)){
        		regDireccion.setVariable("{selected}", "selected='selected'");
        	}
            List_Direcciones.add(regDireccion);
        }
        return List_Direcciones;
    }
    
   //Locales de retiro  para el template
    private List getLocalesRetiro4Template(List List_LocalesRetiro, long idZona){
    	
        Iterator itl = List_LocalesRetiro.iterator();
        List locales_retiro = new ArrayList();
        while(itl.hasNext()) {
            LocalDTO local = (LocalDTO) itl.next();            
            IValueSet fila = new ValueSet();
            fila.setVariable("{nom_local}", local.getNom_local().replaceAll("Jumbo", ""));
            fila.setVariable("{id_local}", local.getId_local()+"");
            fila.setVariable("{cod_local}", local.getCod_local());
            fila.setVariable("{direccion}", local.getDireccion());
            fila.setVariable("{id_zona_retiro}", local.getIdZonaRetiro()+"");
            
            fila.setVariable("{selected}", "");
            if(idZona == local.getIdZonaRetiro()){
            	//if (local.getId_local()==5){//Descomentar la linea superior para activar parametrizacion "jumbo al auto" y eliminar esta linea
            	fila.setVariable("{selected}", "selected='selected'");
        	}
            
            locales_retiro.add(fila);                         
        }
        return locales_retiro;
    }

}
