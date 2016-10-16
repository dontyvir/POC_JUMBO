package cl.bbr.bol.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.pedidos.dto.AvanceDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaCriteria;
import cl.bbr.jumbocl.pedidos.dto.MonitorJornadasDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega monitor de jornadas, si no hay alguno de los parametros seleccionados, entonces despliega valores por defecto con la fecha actual
 * @author jsepulveda
 */
public class ViewMonitorJornadas extends Command {

	
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		long   id_jornada        = -1;
		String param_fecha       = "";
		String param_id_jornada  = "";
		String fecha             = "";
		Date   hoy               = new Date();
		String anterior          = "";
		String siguiente         = "";
		String fecha_jor         = "";
		String local_tipo_picking= "";
		String direccion         = "";
		
		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);
		
		local_tipo_picking = usr.getLocal_tipo_picking();
		
		if (local_tipo_picking.equalsIgnoreCase("N")){//N: Picking Normal
		    direccion = "ViewJornada";
		}else{//L: Picking Light
		    direccion = "ViewJornadaPKL";
		}
		
		String MsjeError = getServletConfig().getInitParameter("MsjeError");
		logger.debug("MsjeError: " + MsjeError);
		
		// 2. Procesa parámetros del request
		logger.debug("req fecha: "+req.getParameter("fecha"));
		if ( req.getParameter("fecha") != null ){
			param_fecha = req.getParameter("fecha");
			logger.debug("param_fecha: " + param_fecha);
			//fecha = new SimpleDateFormat("yyyy-MM-dd").parse(param_fecha);
			logger.debug("Fecha: "+param_fecha);
		}

		if ( req.getParameter("id_jornada") != null ){
			param_id_jornada = req.getParameter("id_jornada");
			try {
				id_jornada = Long.parseLong(param_id_jornada);
			} catch ( NumberFormatException e ) {
				id_jornada = -1;
				logger.info("Parámetro id_jornada no se pudo transformar a long");
			}
			logger.debug("Logger Id Jornada: "+id_jornada);
		}

		
		logger.debug("id jornada" +id_jornada);
		logger.debug("param_fecha: "+param_fecha);
		
		// 3. Template
		View salida = new View(res);
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		// 4.  Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		
		// creamos los criterios
		JornadaCriteria criterio = new JornadaCriteria();
		Calendar cal_si = new GregorianCalendar(); 
		Calendar cal_an = new GregorianCalendar(); 
		
		if (id_jornada!=-1 && id_jornada!=0){  // si viene id_jornada
			criterio.setId_jornada(id_jornada);
		}else if ( !param_fecha.equals("") ){  // si viene una fecha
			criterio.setF_jornada(param_fecha);
			
			Date fecha_aux = new SimpleDateFormat("yyyy-MM-dd").parse(param_fecha);
			cal_si.setTimeInMillis(fecha_aux.getTime());
			cal_an.setTimeInMillis(fecha_aux.getTime());
			
			cal_si.add(Calendar.DATE, 1);
		    cal_an.add(Calendar.DATE, -1);		    
		    
		    siguiente 	= new SimpleDateFormat("yyyy-MM-dd").format(cal_si.getTime());
		    anterior 	= new SimpleDateFormat("yyyy-MM-dd").format(cal_an.getTime());
		    
		    logger.debug("fecha jornada:" +param_fecha);
		    logger.debug("fecha siguiente:" +siguiente);
		    logger.debug("fecha anterior:" +anterior);
		}else{ // caso por defecto
			fecha = new SimpleDateFormat("yyyy-MM-dd").format(hoy);
			criterio.setF_jornada( fecha );
			cal_si.setTimeInMillis(hoy.getTime());
			cal_an.setTimeInMillis(hoy.getTime());
			cal_si.add(Calendar.DATE, 1);
		    cal_an.add(Calendar.DATE, -1);		    
		    siguiente = new SimpleDateFormat("yyyy-MM-dd").format(cal_si.getTime());
		    anterior = new SimpleDateFormat("yyyy-MM-dd").format(cal_an.getTime());
		    logger.debug("fecha actual:" +param_fecha);
		    logger.debug("fecha siguiente:" +siguiente);
		    logger.debug("fecha anterior:" +anterior);
		}
		//setear los estados de pedidos
		criterio.setEst_pedido_no_mostrar(Constantes.ESTADOS_PEDIDO_PARA_PICK);
		//criterio.setEst_pedido_no_mostrar(Constantes.ESTADOS_PEDIDO_INGR_EN_VAL);
		
		boolean oParam = bizDelegate.valoresNegativos();
		
		// 4.1 Listado de Jornada		
		// Llama al BizDelegator
	//	if(id_jornada !=0){
			List listadoJornadas = bizDelegate.getJornadasPickingByCriteria(criterio,usr.getId_local());
			ArrayList datos = new ArrayList();
			
			//Esta es la lista de los pedidos especiales, son los que se le restara a la cantidad de productos total ya que no se consideran en la suma
			List listadoJornadasEspeciales = null;
			try {
				listadoJornadasEspeciales = bizDelegate.getJornadasPickingByCriteriaEspeciales(criterio,usr.getId_local());
			} catch(Exception ex) {
				ex.printStackTrace(System.out);
			}
			for (int i = 0; i < listadoJornadas.size(); i++) {
				IValueSet fila = new ValueSet();
				MonitorJornadasDTO jor1 = (MonitorJornadasDTO)listadoJornadas.get(i);
				MonitorJornadasDTO jornadaEspecial = null;
				
				fila.setVariable("{id_jornada}"   , String.valueOf(jor1.getId_jornada()));
				
				int cantidadProductosDiferencia = bizDelegate.calcularDiferenciaJornada(jor1.getId_jornada());
				
				//System.out.println("Para jornada "+jor1.getId_jornada()+" diferencia es "+canti);
				//Maxbell arreglo homologacion
//				int cantidadOpPasadosPorBodega = 0;
//				try {
//					cantidadOpPasadosPorBodega = bizDelegate.getCantidadOpPasadosPorBodega(jor1.getId_jornada());
//				} catch(Exception ex) {
//					ex.printStackTrace(System.out);
//				}
				
				long cantidadProductos = jor1.getCant_prods();
				long cantidadOP = jor1.getCant_op();
				
				for (int ie = 0;ie<listadoJornadasEspeciales.size();ie++) {
				try {
						jornadaEspecial = (MonitorJornadasDTO)listadoJornadasEspeciales.get(ie);
				} catch(Exception ex) {
						ex.printStackTrace(System.out);
					}
					if(jor1.getId_jornada()==jornadaEspecial.getId_jornada()) {
						
						try {
							cantidadProductos = cantidadProductos - jornadaEspecial.getCant_prods();
						} catch(Exception ex) {
							ex.printStackTrace(System.out);
						}
						
						try {
							cantidadOP = cantidadOP - jornadaEspecial.getCant_op();
						} catch(Exception ex) {
							ex.printStackTrace(System.out);
						}
						break;
					}
				}
					
				if (!oParam) {
					cantidadProductos -= cantidadProductosDiferencia;
				}
				fila.setVariable("{h_inicio}"     , jor1.getHinicio());
				fila.setVariable("{h_fin}"        , jor1.getHfin());
				fila.setVariable("{cant_op}"      , String.valueOf(cantidadOP));
				//fila.setVariable("{cant_prod}"    , String.valueOf(jor1.getCant_prods()));
				fila.setVariable("{cant_prod}"    , String.valueOf(cantidadProductos));
				fila.setVariable("{porc_av_prods}", String.valueOf(jor1.getPorc_av_prods()));
				fila.setVariable("{porc_av_op}"   , String.valueOf(jor1.getPorc_av_op()));
				fila.setVariable("{estado}"       , jor1.getEstado());
				fila.setVariable("{accion1}"      , "ver");
				fila.setVariable("{direccion1}"   , direccion);
				
				/**(+) INDRA 2012-12-12 (+)
	             * Alerta Revision Faltante
	             */
				boolean revision = bizDelegate.existeJornadaRevFaltante(jor1.getId_jornada());
				if(revision){
					 fila.setVariable("{imgEstado}","<img src='img/icon41.gif' width='9' height='9'>");
				}else{
					fila.setVariable("{imgEstado}","");
				}
				//(-) INDRA 2012-12-12 (-)
			
				if(id_jornada !=0){
					fecha_jor = jor1.getFecha();
					Date fecha_aux = new SimpleDateFormat("yyyy-MM-dd").parse(fecha_jor);
					cal_si.setTimeInMillis(fecha_aux.getTime());
					cal_an.setTimeInMillis(fecha_aux.getTime());
					
					cal_si.add(Calendar.DATE, 1);
				    cal_an.add(Calendar.DATE, -1);
				    
				    siguiente 	= new SimpleDateFormat("yyyy-MM-dd").format(cal_si.getTime());
				    anterior 	= new SimpleDateFormat("yyyy-MM-dd").format(cal_an.getTime());
				    
				    logger.debug("fecha jornada:"   + param_fecha);
				    logger.debug("fecha siguiente:" + siguiente);
				    logger.debug("fecha anterior:"  + anterior);
				}
				AvanceDTO AvanceJornada = bizDelegate.getAvanceJornada(jor1.getId_jornada());
				if (AvanceJornada != null){
					fila.setVariable("{cant_prod_bodega}", Formatos.formatoNumero(AvanceJornada.getCant_prod_en_bodega()));
					fila.setVariable("{porc_prod_bodega}", Formatos.formatoNumeroSinDecimales(AvanceJornada.getPorc_prod_en_bodega()) + " %");
				}else{
					fila.setVariable("{cant_prod_bodega}", "0");
					fila.setVariable("{porc_prod_bodega}", "0 %");
				}
				//************************
				datos.add(fila);
			}
			if (listadoJornadas.size()<=0){
				top.setVariable("{mensaje}",MsjeError);
			}else{
				top.setVariable("{mensaje}","");
			}
			// Setea variables bloques
			top.setDynamicValueSets("listado", datos);
		//}

		// 5. Setea variables del template
		top.setVariable("{id_jornada}", param_id_jornada);		
		top.setVariable("{fecha}"     , param_fecha);
		logger.debug("id_jornada --->>>" + param_id_jornada);
		if(param_fecha.equals("")){
			top.setVariable("{fecha_jornada}"	,fecha);
			top.setVariable("{jornadas}"		,"Jornadas del");			
			top.setVariable("{anterior_label}"  ,"<<< ");
			top.setVariable("{anterior}"  ,anterior);			
			top.setVariable("{siguiente_label}"  ," >>>");
			top.setVariable("{siguiente}"  ,siguiente);
		}else{
			top.setVariable("{fecha_jornada}"	,param_fecha);
			top.setVariable("{jornadas}"		,"Jornadas del");
			top.setVariable("{anterior_label}"  ,"<<< ");
			top.setVariable("{anterior}"  ,anterior);
			top.setVariable("{siguiente_label}" ," >>>");
			top.setVariable("{siguiente}"  ,siguiente);
		}
		if(!param_id_jornada.equals("")){
			top.setVariable("{fecha_jornada}"	,fecha_jor);
			top.setVariable("{jornadas}"		,"Jornadas del");			
			top.setVariable("{anterior_label}"  ,"<<< ");
			top.setVariable("{anterior}"  		,anterior);			
			top.setVariable("{siguiente_label}" ," >>>");
			top.setVariable("{siguiente}"  ,siguiente);
		}
		
		if(id_jornada == 0){
			top.setVariable("{mensaje}","El id de jornada debe ser mayor a 0");			
		}
		
		
		// 7. variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());
		
		// 8. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}
}
