package cl.bbr.fo.command.mobi;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
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
import cl.bbr.fo.command.Command;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.exception.SystemException;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.model.DiaDespachoEconomico;
import cl.bbr.jumbocl.common.model.HorarioDespachoEntity;
import cl.bbr.jumbocl.common.model.JornadaDespachoEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.CalendarioDespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;

/**
 * Presenta el calendario de despacho
 * 
 * @author imoyano
 *  
 */
public class CalendarioDespacho extends Command {
    
    String fechaSeleccionada = "";

    protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
            throws Exception {

        try {            
            fechaSeleccionada = "";
            
            // Carga properties
            ResourceBundle rb = ResourceBundle.getBundle("fo");
            
            IValueSet top = new ValueSet();

            // Recupera la sesión del usuario
            HttpSession session = arg0.getSession();

            // Se recupera la salida para el servlet
            PrintWriter out = arg1.getWriter();
            
            BizDelegate biz = new BizDelegate();

            // Recupera pagina desde web.xml
            String pagForm = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
           
            this.getLogger().debug("Template:" + pagForm);
            TemplateLoader load = new TemplateLoader(pagForm);
            ITemplate tem = load.getTemplate();

            long idZona = 0;
            if (session.getAttribute("ses_zona_id") != null) {
                idZona = Long.parseLong(session.getAttribute("ses_zona_id").toString());
            }                        
            if ( arg0.getParameter("fecha_seleccionada") != null ) {
                fechaSeleccionada = arg0.getParameter("fecha_seleccionada");
            }
            
            String jpickingSel = "";
            if ( arg0.getParameter("jpicking") != null ) {
                jpickingSel = arg0.getParameter("jpicking");
            }
            
            List fechas = comboFechas(fechaSeleccionada);
            top.setDynamicValueSets("FECHAS_SELECCION", fechas);
            
            ZonaDTO zonadto = biz.getZonaDespachoById(idZona);
            
            long cantProds = Long.parseLong(rb.getString("despachochart.productos.promedio"));
            if (arg0.getParameter("cant_prod") != null && "".equalsIgnoreCase(arg0.getParameter("cant_prod"))) {
                cantProds = Long.parseLong(arg0.getParameter("cant_prod"));
            }
            
            List datosCalendario = generaCalendario(fechaSeleccionada, zonadto, cantProds, rb, jpickingSel);
            if ( datosCalendario.size() > 0 ) {            
                top.setDynamicValueSets("CALENDARIO", datosCalendario);
            } else {
                List vacia = new ArrayList();
                IValueSet fila = new ValueSet();
                fila.setVariable("{vacio}", "");
                vacia.add(fila);
                top.setDynamicValueSets("SIN_CALENDARIO", vacia);
            }
            top.setVariable("{cant_prod}",  cantProds + "");
            top.setVariable("{zona_id}",    idZona + "");

            if (arg0.getParameter("sel") != null && arg0.getParameter("sel").compareTo("1") == 0) {
                top.setVariable("{sel}", "1");
            } else {
                top.setVariable("{sel}", "0");
            }
            String result = tem.toString(top);
            out.print(result);
            
        } catch (Exception e) {
            this.getLogger().error(e);
            throw new CommandException(e);
        }
    }
    
    private List comboFechas(String fechaSel) {        
        List fechas = new ArrayList();
        
        ResourceBundle rb = ResourceBundle.getBundle("fo");
        Calendar fechaActual = new GregorianCalendar();
        fechaActual.setFirstDayOfWeek(Calendar.MONDAY);
        
        int diasPresentacion = Integer.parseInt(rb.getString("despachochart.diaspresentacion"));
        
        int hoy = dia(fechaActual.get(Calendar.DAY_OF_WEEK));
        
        
        diasPresentacion -= hoy;
       
        for (int i=0; i <= diasPresentacion; i++) {
            IValueSet fila = new ValueSet();
            String valor = new SimpleDateFormat(Formatos.DATE).format(fechaActual.getTime());
            
            
            if (i == 0) {            
                fila.setVariable("{dia_nombre}",  "Hoy " + new SimpleDateFormat(Formatos.DATE).format(fechaActual.getTime()) );
                fechaSeleccionada = valor;
            
            } else {
                fila.setVariable("{dia_nombre}",  diaStr( dia(fechaActual.get(Calendar.DAY_OF_WEEK)) ) + " " + new SimpleDateFormat(Formatos.DATE_CAL).format(fechaActual.getTime()) );
                
            }            
            fila.setVariable("{dia_value}", valor );
            
            if (fechaSel.equalsIgnoreCase( valor ) ) {
                fila.setVariable("{selected}", "selected" );
                fechaSeleccionada = valor;
                
            } else {
                fila.setVariable("{selected}", "" );
                
            }
            fechas.add(fila);            
            fechaActual.add(Calendar.DAY_OF_MONTH, 1);            
        }        
        return fechas;
    }

    /**
     * @param i
     * @return
     */
    private static int dia(int dia) {
        if (dia == 1) {
            return 7;
        } 
        return (dia - 1);        
    }
    
    private static String diaStr(int dia) {
        switch (dia) {
            case 1:
                return "Lunes";
            case 2:
                return "Martes";
            case 3:
                return "Miércoles";
            case 4:
                return "Jueves";
            case 5:
                return "Viernes";
            case 6:
                return "Sábado";
        }
        return "Domingo";
    }

    /**
     * @throws SystemException
     * @throws ParseException
     *  
     */
    private List generaCalendario(String fecha, ZonaDTO zonadto,
            long cantProds, ResourceBundle rb, String jpickingSel)
            throws SystemException, ParseException {
        
        BizDelegate biz = new BizDelegate();
        
        String fechaStr[] = fecha.split("-");
        
        List datos = new ArrayList();
        
        int dia     = Integer.parseInt( fechaStr[0] );
        int mes     = Integer.parseInt( fechaStr[1] );
        int anio    = Integer.parseInt( fechaStr[2] );
        
        Calendar fechaActual = new GregorianCalendar();
        fechaActual.setFirstDayOfWeek(Calendar.MONDAY);
        long diasPresentacion = fechaActual.getTimeInMillis() + Long.parseLong(rb.getString("despachochart.diaspresentacion")) * 24 * 60 * 60 * 1000;
        
        Calendar fechaSeleccionada = new GregorianCalendar(anio, mes-1 , dia);
        fechaSeleccionada.setFirstDayOfWeek(Calendar.MONDAY);
        int semana = fechaSeleccionada.get(Calendar.WEEK_OF_YEAR);
        
        String fcStr = new SimpleDateFormat(Formatos.YYYYMMDD).format(fechaSeleccionada.getTime());
        String fcStrShort = new SimpleDateFormat(Formatos.DATE_CAL).format(fechaSeleccionada.getTime());

        CalendarioDespachoDTO cal = biz.getCalendarioDespachoByFecha(fechaSeleccionada.getTime(), semana, anio, zonadto.getId_zona());

        List horarios = new ArrayList();
        horarios = cal.getHorarios();

        List jornadas = new ArrayList();
        jornadas = cal.getJornadas();
        
        boolean flagTarExpress  = false;
        boolean flagEntro       = false;
        int fcActual            = 0;
        int fcPicking           = 0;
        int fcDespacho          = 0;
        Calendar calAhora       = new GregorianCalendar();
        calAhora.setFirstDayOfWeek(Calendar.MONDAY);
        long tiempoLimiteCompraExpress = Constantes.HORAS_COMPRA_EXPRESS * Constantes.HORA_EN_MILI_SEG;
        
        String horIniEconomico = "";
        String horFinEconomico = "";
        
        DiaDespachoEconomico eco = new DiaDespachoEconomico();        
        
        // Iteramos listado de horarios
        for (int i = 0; i < horarios.size(); i++) {
            IValueSet fila = new ValueSet();
            HorarioDespachoEntity hor = (HorarioDespachoEntity) horarios.get(i);
            fila.setVariable("{h_ini}", new SimpleDateFormat(Formatos.HOUR_CAL).format(hor.getH_ini()));
            if (i == 0) {
                horIniEconomico = new SimpleDateFormat(Formatos.HOUR_CAL).format(hor.getH_ini());
            } 
            
            fila.setVariable("{h_fin}", new SimpleDateFormat(Formatos.HOUR_CAL).format(hor.getH_fin()));
            horFinEconomico = new SimpleDateFormat(Formatos.HOUR_CAL).format(hor.getH_fin());
            fila.setVariable("{id_hor_desp}", String.valueOf(hor.getId_hor_desp()));

            // iteramos sobre las jornadas
            for (int j = 0; j < jornadas.size(); j++) {
                JornadaDespachoEntity jor   = (JornadaDespachoEntity) jornadas.get(j);
                String fechaPick            = jor.getFecha_picking() + " " + jor.getHoraIniPicking().toString();
                DateFormat formatter        = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date datePicking            = formatter.parse(fechaPick);
                long tiempoDespacho         = datePicking.getTime();
                long tiempoLimite           = jor.getHrs_ofrecido_web() * 60 * 60 * 1000L;
                long tiempoActual           = fechaActual.getTimeInMillis();
                long horaPick               = jor.getHoraIniPicking().getHours()*Constantes.HORA_EN_MILI_SEG;
                long horaInicioValidacionExpress = Constantes.HORAS_INICIO_VALIDACION_EXPRESS * Constantes.HORA_EN_MILI_SEG;
                long tiempoPicking          = datePicking.getTime();
                
                GregorianCalendar horaPicking = new GregorianCalendar();
                horaPicking.setFirstDayOfWeek(Calendar.MONDAY);
                horaPicking.set(Calendar.HOUR_OF_DAY, jor.getHoraIniPicking().getHours());
                horaPicking.set(Calendar.MINUTE, jor.getHoraIniPicking().getMinutes());
                horaPicking.set(Calendar.SECOND, jor.getHoraIniPicking().getSeconds());
                horaPicking.add(Calendar.HOUR_OF_DAY, -(int)Constantes.HORAS_COMPRA_EXPRESS);
                
                // jornadas que pertenecen al horario i
                if (hor.getId_hor_desp() == jor.getId_hor_desp()) {
                    java.sql.Date fecJor = new java.sql.Date(jor.getFecha().getTime());
                    Calendar fecJorTime = Calendar.getInstance();
                    fecJorTime.setFirstDayOfWeek(Calendar.MONDAY);
                    fecJorTime.setTime(fecJor);
                    Calendar fecJorTimeAux = Calendar.getInstance();
                    fecJorTimeAux.setFirstDayOfWeek(Calendar.MONDAY);
                    fecJorTimeAux.set(Calendar.YEAR, fecJorTime.get(Calendar.YEAR));
                    fecJorTimeAux.set(Calendar.MONTH, fecJorTime.get(Calendar.MONTH));
                    fecJorTimeAux.set(Calendar.DAY_OF_MONTH, fecJorTime.get(Calendar.DAY_OF_MONTH));
             
                    fcActual    = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(calAhora.getTime()));
                    fcPicking   = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(jor.getFecha_picking()));
                    fcDespacho  = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(jor.getFecha()));
                    
                    flagTarExpress  = false;
  
                    if ( fcActual == fcDespacho && fcActual == fcPicking ) {
                        if ((zonadto.getEstado_tarifa_express() == 1)){
                            if (((horaPick - tiempoLimiteCompraExpress ) > horaInicioValidacionExpress) && 
                                  ((tiempoPicking - tiempoActual) > tiempoLimiteCompraExpress) &&
                                    (cantProds < (jor.getCapac_picking() - jor.getCapac_picking_ocupada())) &&
                                      ((jor.getCapac_despacho() - jor.getCapac_despacho_ocupada()) > 0) ){
                                
                                fila.setVariable("{id_jdespacho}", String.valueOf(jor.getId_jdespacho()));
                                fila.setVariable("{id_jpicking}", jor.getId_jpicking() + "");
                                fila.setVariable("{precio}", Formatos.formatoPrecioFO(jor.getTarifa_express()));
                                fila.setVariable("{fecha}", fcStrShort + " " + hor.getH_ini() + " - " + hor.getH_fin());
                                fila.setVariable("{tipo}", "E");
                                fila.setVariable("{disabled}", "");
                                fila.setVariable("{class_hora}", "horario");
                                fila.setVariable("{radio}", "radio");
                                fila.setVariable("{img_express}", "<img src=\"/FO_IMGS/images/mobi/paso3/express.gif\" border=\"0\" />");
                                flagTarExpress = true;
                                eco.setPrecio( jor.getTarifa_economica() );
                                flagEntro = true;
                                fila.setVariable("{seleccionado}", String.valueOf(jor.getId_jdespacho()));
                                if (jpickingSel.equalsIgnoreCase(String.valueOf(jor.getId_jdespacho()))) {
                                    fila.setVariable("{checked}", "checked");                                
                                } else {
                                    fila.setVariable("{checked}", "");
                                }
                                
                            }
                        }
                    }
                    if (!flagTarExpress) {
                        if ((cantProds > (jor.getCapac_picking() - jor.getCapac_picking_ocupada()))
                                || ((jor.getCapac_despacho() - jor.getCapac_despacho_ocupada()) <= 0)
                                || ((tiempoDespacho - tiempoActual) < tiempoLimite)
                                || (jor.getFecha().getTime() > diasPresentacion)) {
                            
                            // CANDADO - PASADO
                            fila.setVariable("{id_jdespacho}", "");
                            fila.setVariable("{id_jpicking}", "");
                            fila.setVariable("{precio}", "");
                            fila.setVariable("{fecha}", "");
                            fila.setVariable("{tipo}", "");
                            fila.setVariable("{disabled}", "disabled");
                            fila.setVariable("{radio}", "hidden");
                            fila.setVariable("{img_express}", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=\"/FO_IMGS/img/estructura/calendario/candado.gif\" alt=\"despacho no disponible\" title=\"despacho no disponible\" width=\"11\" height=\"14\" />");
                            fila.setVariable("{seleccionado}", "");
                            fila.setVariable("{class_hora}", "horario"); 
                            eco.setMostrar( false );
                            flagEntro = true;
                            fila.setVariable("{checked}", "");                                                    

                        } else {
                            fila.setVariable("{id_jdespacho}", String.valueOf(jor.getId_jdespacho()));
                            fila.setVariable("{id_jpicking}", jor.getId_jpicking() + "");
                            fila.setVariable("{precio}", Formatos.formatoPrecioFO(jor.getTarifa_normal()));
                            fila.setVariable("{fecha}", fcStrShort + " " + hor.getH_ini() + " - " + hor.getH_fin());
                            fila.setVariable("{tipo}", "N");
                            fila.setVariable("{disabled}", "");
                            fila.setVariable("{radio}", "radio");
                            fila.setVariable("{img_express}", "");
                            fila.setVariable("{class_hora}", "horario"); 
                            eco.setPrecio( jor.getTarifa_economica() );
                            flagEntro = true;
                            fila.setVariable("{seleccionado}", String.valueOf(jor.getId_jdespacho()));
                            if (jpickingSel.equalsIgnoreCase(String.valueOf(jor.getId_jdespacho()))) {
                                fila.setVariable("{checked}", "checked");                                
                            } else {
                                fila.setVariable("{checked}", "");
                            }
                            
                        }
                    }
                    if (fcActual == fcDespacho) {
                        eco.setDiaActual( true );   
                    }                        
                }
            }
            
            datos.add( fila );
        }
        
        if ( !eco.isDiaActual() && eco.isMostrar() && flagEntro && zonadto.getEstado_tarifa_economica() == 1 ) {
            IValueSet fila = new ValueSet();
            fila.setVariable("{h_ini}", horIniEconomico);
            fila.setVariable("{h_fin}", horFinEconomico);
            fila.setVariable("{id_jdespacho}", "0");
            fila.setVariable("{id_jpicking}", horIniEconomico + " - " + horFinEconomico);
            fila.setVariable("{precio}", Formatos.formatoPrecioFO(eco.getPrecio()));
            fila.setVariable("{fecha}", fcStr);
            fila.setVariable("{tipo}", "C");
            fila.setVariable("{disabled}", "");
            fila.setVariable("{radio}", "radio");
            fila.setVariable("{img_express}", "");
            fila.setVariable("{seleccionado}", fcStr);
            fila.setVariable("{class_hora}", "horario_econom"); 
            if (jpickingSel.equalsIgnoreCase(fcStr)) {
                fila.setVariable("{checked}", "checked");                                
            } else {
                fila.setVariable("{checked}", "");
            }
            
            datos.add( fila );
            
        }
        return datos;
    }

}
