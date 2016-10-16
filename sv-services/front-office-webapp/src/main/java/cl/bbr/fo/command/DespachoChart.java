package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.common.model.SemanasEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.CalendarioDespachoDTO;

/**
 * Presenta el cuadro de despachos
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class DespachoChart extends Command {

	/**
	 * Presenta el cuadro de despachos
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		
		try {
			
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");			
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
				
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();	
			
			// Recupera pagina desde web.xml
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			this.getLogger().debug( "Template:"+pag_form );
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();
			
			IValueSet top = new ValueSet();
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			/*
			 * Recuperar datos de la sesión si existen
			 */
			long id_zona = 0;			
			if( session.getAttribute("ses_zona_id") != null )
				  id_zona = Long.parseLong(session.getAttribute("ses_zona_id").toString());
			else
				id_zona = Long.parseLong(arg0.getParameter("zona_id").toString());
			
			long cant_prods = Long.parseLong(rb.getString("despachochart.productos.promedio"));			
			if( arg0.getParameter("cant_prod") != null )
				cant_prods = Long.parseLong(arg0.getParameter("cant_prod")); 
			
			int pagina = 0;
			if( arg0.getParameter("pa") != null )
				pagina = Integer.parseInt(arg0.getParameter("pa"));
			if( pagina < 0 || pagina > 2 ) // Se eliminan intentos de revisión de calendario
				pagina = 0;
			
			Calendar fecha_actual = new GregorianCalendar();
			
			Calendar fecha_preview = new GregorianCalendar( fecha_actual.get(Calendar.YEAR), fecha_actual.get( Calendar.MONTH ), fecha_actual.get(Calendar.DAY_OF_MONTH)+pagina*7 );
			fecha_preview.setFirstDayOfWeek( Calendar.MONDAY );
			int year1 = fecha_preview.get(Calendar.YEAR);
			int woy1	 = fecha_preview.get(Calendar.WEEK_OF_YEAR) ;
			this.getLogger().debug( "-->" + year1 + "-" + woy1 );
			
			// Calcular la fecha de corte en futuro
			long diaspresentacion1 = fecha_preview.getTimeInMillis() + Long.parseLong(rb.getString("despachochart.diaspresentacion"))*24*60*60*1000;
           		
			// Obtenemos los objetos
			CalendarioDespachoDTO cal1 = biz.getCalendarioDespacho( woy1,year1,id_zona );

			List horarios1 = new ArrayList();
			horarios1 = cal1.getHorarios();
			
			List jornadas1 = new ArrayList();
			jornadas1 = cal1.getJornadas();	
			
			
			List datos = new ArrayList();
			
			boolean sem_sig = false;  
			
			//CICLO QUE VERIFICA SI UNA SEMANA ESTA COMPUESTA SOLO POR CANDADOS 
			for (int i=0; i<horarios1.size(); i++){
				HorarioDespachoEntity hor = (HorarioDespachoEntity)horarios1.get(i);				
				for (int j=0; j<jornadas1.size();j++){
					JornadaDespachoEntity jor = new JornadaDespachoEntity();
					
					jor =	(JornadaDespachoEntity)jornadas1.get(j);	
					String fecha_pick = jor.getFecha_picking()+" "+jor.getHoraIniPicking().toString();
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date datePicking = formatter.parse(fecha_pick);
					long tiempoDespacho = datePicking.getTime();
					long tiempoLimite = jor.getHrs_ofrecido_web()*60*60*1000L;
					long tiempoActual = fecha_actual.getTimeInMillis();					
					if ( hor.getId_hor_desp() == jor.getId_hor_desp() ){
						if ( !( ( cant_prods > ( jor.getCapac_picking() - jor.getCapac_picking_ocupada() ) )
								|| ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) <= 0 )
								|| ( (tiempoDespacho - tiempoActual) < tiempoLimite )
								|| ( jor.getFecha().getTime() > diaspresentacion1 )										
								)
							 ){
							sem_sig = true;//PRECIO
							break;//Quiebra primer ciclo for
						}
					}
				}
				if(sem_sig == true){
					break;//Quiebra el segundo ciclo for
				}
			}//FIN CICLO	
			if (sem_sig == false && arg0.getParameter("pa") == null){//Si la semana tiene solo candados aumenta en 1 la variable pagina
				pagina++;
				fecha_preview = new GregorianCalendar( fecha_actual.get(Calendar.YEAR), fecha_actual.get( Calendar.MONTH ), fecha_actual.get(Calendar.DAY_OF_MONTH)+pagina*7 );
				fecha_preview.setFirstDayOfWeek( Calendar.MONDAY );
				year1 = fecha_preview.get(Calendar.YEAR);
				woy1 = fecha_preview.get(Calendar.WEEK_OF_YEAR) ;
				this.getLogger().debug( "Se salta la semana -->" + year1 + "-" + woy1 );				
			}
			
			/*
			 * Inicio de Cuadro Despachos 
			 */
			
			// Obtener fecha para la página de presentación
			/*
			Calendar ahora = new GregorianCalendar( fecha_actual.get(Calendar.YEAR), fecha_actual.get( Calendar.MONTH ), fecha_actual.get(Calendar.DAY_OF_MONTH)+pagina*7 );
			ahora.setFirstDayOfWeek( Calendar.MONDAY );
			int year = ahora.get(Calendar.YEAR);
			int woy	 = ahora.get(Calendar.WEEK_OF_YEAR);
			*/
			
			// Calcular la fecha de corte en futuro
			long diaspresentacion = fecha_actual.getTimeInMillis() + Long.parseLong(rb.getString("despachochart.diaspresentacion"))*24*60*60*1000;
			CalendarioDespachoDTO cal = biz.getCalendarioDespacho( woy1,year1,id_zona );

			SemanasEntity sem1 = new SemanasEntity();
			sem1 = cal.getSemana();			
			
			List horarios = new ArrayList();
			horarios = cal.getHorarios();
			
			List jornadas = new ArrayList();
			jornadas = cal.getJornadas();
			
			// Setea variables del header de la tabla (fechas)
			Calendar fecha1 = new GregorianCalendar();
			fecha1.setFirstDayOfWeek( Calendar.MONDAY );
			fecha1.setTime(sem1.getF_ini());
			
			fecha1.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
			String StrFecha1 = new SimpleDateFormat( Formatos.DATE_CAL).format(fecha1.getTime());
			top.setVariable("{f_lu}"		,StrFecha1);

			fecha1.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
			String StrFecha2 = new SimpleDateFormat( Formatos.DATE_CAL).format(fecha1.getTime());
			top.setVariable("{f_ma}"		,StrFecha2);
			
			fecha1.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
			String StrFecha3 = new SimpleDateFormat( Formatos.DATE_CAL).format(fecha1.getTime());
			top.setVariable("{f_mi}"		,StrFecha3);
			
			fecha1.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
			String StrFecha4 = new SimpleDateFormat( Formatos.DATE_CAL).format(fecha1.getTime());
			top.setVariable("{f_ju}"		,StrFecha4);
			
			fecha1.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
			String StrFecha5 = new SimpleDateFormat( Formatos.DATE_CAL).format(fecha1.getTime());
			top.setVariable("{f_vi}"		,StrFecha5);

			fecha1.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
			String StrFecha6 = new SimpleDateFormat( Formatos.DATE_CAL).format(fecha1.getTime());
			top.setVariable("{f_sa}"		,StrFecha6);
			
			fecha1.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
			String StrFecha7 = new SimpleDateFormat( Formatos.DATE_CAL).format(fecha1.getTime());
			top.setVariable("{f_do}"		,StrFecha7);			
			
			// Iteramos listado de horarios
			this.getLogger().debug( "Horarios disponibles:"+horarios.size() );
			for (int i=0; i<horarios.size(); i++){
				IValueSet fila = new ValueSet();
				HorarioDespachoEntity hor = (HorarioDespachoEntity)horarios.get(i);
				fila.setVariable("{h_ini}"			, new SimpleDateFormat( Formatos.HOUR_CAL).format(hor.getH_ini()));
				fila.setVariable("{h_fin}"			, new SimpleDateFormat( Formatos.HOUR_CAL).format(hor.getH_fin()));
				fila.setVariable("{id_hor_desp}"	, String.valueOf(hor.getId_hor_desp()));
				
				// iteramos sobre las jornadas
				for (int j=0; j<jornadas.size();j++){
					
					JornadaDespachoEntity jor = new JornadaDespachoEntity();
						
					jor =	(JornadaDespachoEntity)jornadas.get(j);

					String fecha_pick = jor.getFecha_picking()+" "+jor.getHoraIniPicking().toString();
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date datePicking = formatter.parse(fecha_pick);
					long tiempoDespacho = datePicking.getTime();
					long tiempoLimite = jor.getHrs_ofrecido_web()*60*60*1000L;
					long tiempoActual = fecha_actual.getTimeInMillis();
					
					// jornadas que pertenecen al horario i
					if ( hor.getId_hor_desp() == jor.getId_hor_desp() ){						
						long fec_jor = jor.getFecha().getTime();
						long fec_act = fecha_actual.getTime().getTime();
						
						switch(jor.getDay_of_week()){
						case 1:
							if ( ( cant_prods > ( jor.getCapac_picking() - jor.getCapac_picking_ocupada() ) )
									|| ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) <= 0 )
									|| ( (tiempoDespacho - tiempoActual) < tiempoLimite )
									|| ( jor.getFecha().getTime() > diaspresentacion )										
									) {
								IValueSet reg_dia = new ValueSet();
								reg_dia.setVariable("{blanco}","");
								List list_dia = new ArrayList();
								list_dia.add(reg_dia);
								if ( (fec_jor - fec_act) < 0 ) {
									fila.setDynamicValueSets( "LUNES_PASADO", list_dia );
									this.getLogger().debug("Pasado");
								} else {
									fila.setDynamicValueSets( "LUNES_CANDADO", list_dia );
									this.getLogger().debug("Candado");
								}
							}
							else {
								IValueSet reg_dia = new ValueSet();
								reg_dia.setVariable("{id_jdespacho_lu}"	, String.valueOf( jor.getId_jdespacho() ) );
								reg_dia.setVariable("{id_jpicking_lu}"	, jor.getId_jpicking() + "" );
								reg_dia.setVariable("{precio_lu}"		, Formatos.formatoPrecioFO(jor.getTarifa_normal()) );
								reg_dia.setVariable("{fecha_lu}"		, StrFecha1+" "+hor.getH_ini()+" - "+hor.getH_fin() );
								List list_dia = new ArrayList();
								list_dia.add(reg_dia);
								fila.setDynamicValueSets( "LUNES_PRECIO", list_dia );
								this.getLogger().debug("Abierto");
							}
							
							break;
						case 2:
							if ( ( cant_prods > ( jor.getCapac_picking() - jor.getCapac_picking_ocupada() ) )
									|| ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) <= 0 )
									|| ( (tiempoDespacho - tiempoActual) < tiempoLimite )
									|| ( jor.getFecha().getTime() > diaspresentacion )
									) {
								IValueSet reg_dia = new ValueSet();
								reg_dia.setVariable("{blanco}","");
								List list_dia = new ArrayList();
								list_dia.add(reg_dia);								
								fila.setDynamicValueSets( "MARTES_CANDADO", list_dia );
								this.getLogger().debug("Candado");
							}
							else {
								IValueSet reg_dia = new ValueSet();
								reg_dia.setVariable("{id_jdespacho_ma}"	, String.valueOf( jor.getId_jdespacho() ) );
								reg_dia.setVariable("{id_jpicking_ma}"	, jor.getId_jpicking() + "" );
								reg_dia.setVariable("{precio_ma}"		, Formatos.formatoPrecioFO(jor.getTarifa_normal()) );
								reg_dia.setVariable("{fecha_ma}"		, StrFecha2+" "+hor.getH_ini()+" - "+hor.getH_fin() );
								List list_dia = new ArrayList();
								list_dia.add(reg_dia);
								fila.setDynamicValueSets( "MARTES_PRECIO", list_dia );
								this.getLogger().debug("Abierto");
							}
														
							break;
					
						case 3:
							if ( ( cant_prods > ( jor.getCapac_picking() - jor.getCapac_picking_ocupada() ) )
									|| ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) <= 0 )
									|| ( (tiempoDespacho - tiempoActual) < tiempoLimite )
									|| ( jor.getFecha().getTime() > diaspresentacion )
									) {
								IValueSet reg_dia = new ValueSet();
								reg_dia.setVariable("{blanco}","");
								List list_dia = new ArrayList();
								list_dia.add(reg_dia);								
								fila.setDynamicValueSets( "MIERCOLES_CANDADO", list_dia );
								this.getLogger().debug("Candado");
							}
							else {
								IValueSet reg_dia = new ValueSet();
								reg_dia.setVariable("{id_jdespacho_mi}"	, String.valueOf( jor.getId_jdespacho() ) );
								reg_dia.setVariable("{id_jpicking_mi}"	, jor.getId_jpicking() + "" );
								reg_dia.setVariable("{precio_mi}"		, Formatos.formatoPrecioFO(jor.getTarifa_normal()) );
								reg_dia.setVariable("{fecha_mi}"		, StrFecha3+" "+hor.getH_ini()+" - "+hor.getH_fin() );
								List list_dia = new ArrayList();
								list_dia.add(reg_dia);
								fila.setDynamicValueSets( "MIERCOLES_PRECIO", list_dia );
								this.getLogger().debug("Abierto");
							}
							
							break;
							
						case 4:
							if ( ( cant_prods > ( jor.getCapac_picking() - jor.getCapac_picking_ocupada() ) )
									|| ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) <= 0 )
									|| ( (tiempoDespacho - tiempoActual) < tiempoLimite )
									|| ( jor.getFecha().getTime() > diaspresentacion )
									) {
								IValueSet reg_dia = new ValueSet();
								reg_dia.setVariable("{blanco}","");
								List list_dia = new ArrayList();
								list_dia.add(reg_dia);								
								fila.setDynamicValueSets( "JUEVES_CANDADO", list_dia );
								this.getLogger().debug("Candado");
							}
							else {
								IValueSet reg_dia = new ValueSet();
								reg_dia.setVariable("{id_jdespacho_ju}"	, String.valueOf( jor.getId_jdespacho() ) );
								reg_dia.setVariable("{id_jpicking_ju}"	, jor.getId_jpicking() + "" );
								reg_dia.setVariable("{precio_ju}"		, Formatos.formatoPrecioFO(jor.getTarifa_normal()) );
								reg_dia.setVariable("{fecha_ju}"		, StrFecha4+" "+hor.getH_ini()+" - "+hor.getH_fin() );
								List list_dia = new ArrayList();
								list_dia.add(reg_dia);
								fila.setDynamicValueSets( "JUEVES_PRECIO", list_dia );		
								this.getLogger().debug("Abierto");
							}
							
							break;
							
						case 5:
							if ( ( cant_prods > ( jor.getCapac_picking() - jor.getCapac_picking_ocupada() ) )
									|| ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) <= 0 )
									|| ( (tiempoDespacho - tiempoActual) < tiempoLimite )
									|| ( jor.getFecha().getTime() > diaspresentacion )
									) {
								IValueSet reg_dia = new ValueSet();
								reg_dia.setVariable("{blanco}","");
								List list_dia = new ArrayList();
								list_dia.add(reg_dia);								
								fila.setDynamicValueSets( "VIERNES_CANDADO", list_dia );
								this.getLogger().debug("Candado");
							}
							else {
								IValueSet reg_dia = new ValueSet();
								reg_dia.setVariable("{id_jdespacho_vi}"	, String.valueOf( jor.getId_jdespacho() ) );
								reg_dia.setVariable("{id_jpicking_vi}"	, jor.getId_jpicking() + "" );
								reg_dia.setVariable("{precio_vi}"		, Formatos.formatoPrecioFO(jor.getTarifa_normal()) );
								reg_dia.setVariable("{fecha_vi}"		, StrFecha5+" "+hor.getH_ini()+" - "+hor.getH_fin() );
								List list_dia = new ArrayList();
								list_dia.add(reg_dia);
								fila.setDynamicValueSets( "VIERNES_PRECIO", list_dia );		
								this.getLogger().debug("Abierto");
							}
							
							break;
							
						case 6:
							if ( ( cant_prods > ( jor.getCapac_picking() - jor.getCapac_picking_ocupada() ) )
									|| ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) <= 0 )
									|| ( (tiempoDespacho - tiempoActual) < tiempoLimite )
									|| ( jor.getFecha().getTime() > diaspresentacion )
									) {
								IValueSet reg_dia = new ValueSet();
								reg_dia.setVariable("{blanco}","");
								List list_dia = new ArrayList();
								list_dia.add(reg_dia);								
								fila.setDynamicValueSets( "SABADO_CANDADO", list_dia );
								this.getLogger().debug("Candado");
							}
							else {
								IValueSet reg_dia = new ValueSet();
								reg_dia.setVariable("{id_jdespacho_sa}"	, String.valueOf( jor.getId_jdespacho() ) );
								reg_dia.setVariable("{id_jpicking_sa}"	, jor.getId_jpicking() + "" );
								reg_dia.setVariable("{precio_sa}"		, Formatos.formatoPrecioFO(jor.getTarifa_normal()) );
								reg_dia.setVariable("{fecha_sa}"		, StrFecha6+" "+hor.getH_ini()+" - "+hor.getH_fin() );
								List list_dia = new ArrayList();
								list_dia.add(reg_dia);
								fila.setDynamicValueSets( "SABADO_PRECIO", list_dia );			
								this.getLogger().debug("Abierto");
							}
							
							break;
							
						case 7:
							if ( ( cant_prods > ( jor.getCapac_picking() - jor.getCapac_picking_ocupada() ) )
									|| ( ( jor.getCapac_despacho() - jor.getCapac_despacho_ocupada() ) <= 0 ) 
									|| ( (tiempoDespacho - tiempoActual) < tiempoLimite )
									|| ( jor.getFecha().getTime() > diaspresentacion )
									) {
								IValueSet reg_dia = new ValueSet();
								reg_dia.setVariable("{blanco}","");
								List list_dia = new ArrayList();
								list_dia.add(reg_dia);								
								fila.setDynamicValueSets( "DOMINGO_CANDADO", list_dia );
								this.getLogger().debug("Candado");
							}
							else {
								IValueSet reg_dia = new ValueSet();
								reg_dia.setVariable("{id_jdespacho_do}"	, String.valueOf( jor.getId_jdespacho() ) );
								reg_dia.setVariable("{id_jpicking_do}"	, jor.getId_jpicking() + "" );
								reg_dia.setVariable("{precio_do}"		, Formatos.formatoPrecioFO(jor.getTarifa_normal()) );
								reg_dia.setVariable("{fecha_do}"		, StrFecha7+" "+hor.getH_ini()+" - "+hor.getH_fin() );
								List list_dia = new ArrayList();
								list_dia.add(reg_dia);
								fila.setDynamicValueSets( "DOMINGO_PRECIO", list_dia );
								this.getLogger().debug("Abierto");
							}
							
							break;
							
						}//end switch
					
					}//end if
								
					
				}//end for
								
				datos.add(fila);
				
			}
			
			top.setDynamicValueSets( "FILA_SEL", datos );
									
			// Botones avanzar y retroceder
			if( pagina == 0 ) { // Estamos al inicio no hay atrás
				top.setVariable( "{boton_atras}", rb.getString("despachochart.boton.atras.desactivo") );
				top.setVariable( "{boton_adelante}", rb.getString("despachochart.boton.adelante.activo") );
				top.setVariable( "{siguiente}", "1" );
				top.setVariable( "{atras}", "0" );				
			} else if( pagina == 1) {
				top.setVariable( "{boton_atras}", rb.getString("despachochart.boton.atras.activo") );
				top.setVariable( "{boton_adelante}", rb.getString("despachochart.boton.adelante.activo") );
				top.setVariable( "{siguiente}", "2" );
				top.setVariable( "{atras}", "0" );				
			} else {
				top.setVariable( "{boton_atras}", rb.getString("despachochart.boton.atras.activo") );
				top.setVariable( "{boton_adelante}", rb.getString("despachochart.boton.adelante.desactivo") );
				top.setVariable( "{siguiente}", "2" );
				top.setVariable( "{atras}", "1" );				
			}
			
			top.setVariable( "{cant_prod}", cant_prods + "" );
			top.setVariable( "{zona_id}", id_zona + "" );
			
			if( arg0.getParameter("sel") != null && arg0.getParameter("sel").compareTo("1") == 0 )
				top.setVariable( "{sel}", "1" );
			else
				top.setVariable( "{sel}", "0" );
			
			String result = tem.toString(top);
			
			out.print(result);
		}
		catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}
	}
}