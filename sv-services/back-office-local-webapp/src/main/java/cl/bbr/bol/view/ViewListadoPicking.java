package cl.bbr.bol.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.CategoriaSapDTO;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RondaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Formulario que despliega un listado con las jornadas de picking
 * @author mleiva
 */
public class ViewListadoPicking extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		String param_id_ronda = "";
		long id_ronda = -1;

		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		// 2. Procesa parámetros del request

		logger.debug("Procesando parámetros...");
		if (req.getParameter("id_ronda") == null) {
			throw new ParametroObligatorioException("id_ronda");
		}
		param_id_ronda = req.getParameter("id_ronda");
		id_ronda = Long.parseLong(param_id_ronda);
		logger.debug("id_ronda = " + req.getParameter("id_ronda"));

		if (id_ronda == -1)
			throw new ParametroObligatorioException("id_ronda viene vacío");
		
		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		

		// 4.1 Listado Picking
		List listaPicking = new ArrayList();
		listaPicking = bizDelegate.getProductosRonda(id_ronda);

		ArrayList pick = new ArrayList();
		String seccion = new String();
		List list_secciones = bizDelegate.getSeccionesSap();
		for (int i = 0; i < listaPicking.size(); i++) {
			IValueSet fila = new ValueSet();
			ProductosPedidoDTO ronda1 = new ProductosPedidoDTO();
			ronda1 = (ProductosPedidoDTO) listaPicking.get(i);
			fila.setVariable("{id_pedido}", String.valueOf(ronda1.getId_pedido()));						
			seccion = ronda1.getId_catprod().substring(0,2);			
			for(int j=0;j <list_secciones.size();j++){
				CategoriaSapDTO sec1 = new CategoriaSapDTO();
				sec1 = (CategoriaSapDTO) list_secciones.get(j);
                if(sec1.getId_cat().equals(seccion)) {
                    if (sec1.getDescrip() != null) {
                        fila.setVariable("{seccion}", sec1.getDescrip().replaceAll("/","/ "));
                    } else {
                        fila.setVariable("{seccion}", "");
                    }
                }
            }
			fila.setVariable("{cod_sap}", String.valueOf(ronda1.getCod_producto()));
			fila.setVariable("{descripcion}", ronda1.getDescripcion());
			fila.setVariable("{cant_sol}", String.valueOf(ronda1.getCant_solic()));
			fila.setVariable("{um}", ronda1.getUnid_medida());
			String precio = String.valueOf(ronda1.getPrecio());
			int precioEntero = (int)ronda1.getPrecio();
			precio = String.valueOf(precioEntero);
			String precioEstandarizado="";
			if(precio != null && !"".equals(precio.trim())){
				int contador = 0;
				precioEstandarizado = "";
				for(int k=precio.length()-1;k>=0;k--){
					precioEstandarizado = String.valueOf(precio.charAt(k)) + precioEstandarizado;
					contador ++;
					if(contador == 3){
						contador=0;
						if(k!=0){
							precioEstandarizado= "."+precioEstandarizado;
						}
					}
				}
			}
			fila.setVariable("{precio}",String.valueOf(precioEstandarizado));
			fila.setVariable("{ean}",String.valueOf(ronda1.getCod_barra()));
//            if ( ronda1.getObservacion() != null && !ronda1.getObservacion().equals("null") ) {
//                fila.setVariable("{observacion}", ronda1.getObservacion() );
//			}else{
//				fila.setVariable("{observacion}", "");
//			}
			if(ronda1.getIdCriterio() == 4){
				fila.setVariable("{criterio_sustituto}", "<b><u>"+ronda1.getDescCriterio()+"</u></b>");
			}else{
				fila.setVariable("{criterio_sustituto}", ronda1.getDescCriterio());
			}
			pick.add(fila);
		}
		
		// 4.2 Obtenemos la ronda
		RondaDTO ronda = bizDelegate.getRondaById(id_ronda);	
		
		// 4.3 Obtenemos los datos de la jornada de picking en que está la ronda
		JornadaDTO jor = bizDelegate.getJornadaById(ronda.getId_jpicking());
		
		// 4.4 Obtiene zonas de la ronda
		String zonas = "";
		List listaZonas= new ArrayList(); 
		listaZonas = bizDelegate.getBuscaZonaByRonda(id_ronda);
		for(int j=0; j<listaZonas.size();j++){
			ZonaDTO zon = (ZonaDTO) listaZonas.get(j);
			if (j==0) {
				zonas += zon.getNombre();
			}
			else {
				zonas += ", "+zon.getNombre();
			}
		}
		
		top.setVariable("{zonas_r}"		, zonas);
		// 5. Setea variables del template
		
		//Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(ronda.getF_creacion());
		
		/*
		Calendar cal1 = new GregorianCalendar();
		cal1.setFirstDayOfWeek(Calendar.MONDAY);
		logger.debug("fecha: " + fecha.toString());
		cal1.setTime(fecha);
		cal1.getTime();
		cal1.set(Calendar.DAY_OF_WEEK, Calendar.DATE);*/
		
		//seteando calendar, segun la fecha, nueva manera de hacerlo
		Calendar cal1 = new GregorianCalendar();
		cal1.clear();
		cal1.set(Calendar.YEAR, Integer.parseInt(ronda.getF_creacion().substring(0,4)));
		cal1.set(Calendar.YEAR, Integer.parseInt(ronda.getF_creacion().substring(0,4)));
		cal1.set(Calendar.MONTH, Integer.parseInt(ronda.getF_creacion().substring(5,7))-1);
		cal1.set(Calendar.DAY_OF_MONTH, Integer.parseInt(ronda.getF_creacion().substring(8,10)));
		
		String strFecha = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime());

		top.setVariable("{id_ronda}"	, param_id_ronda);
		top.setVariable("{fecha}"		, strFecha);
		top.setVariable("{Sector}"		, ronda.getSector());
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("HH:mm:ss");
		Date fecha_impresion = new Date();
        String hora_imp = bartDateFormat.format(fecha_impresion);
		top.setVariable("{h_imp}"		, hora_imp);
		top.setVariable("{id_jornada}"	, ronda.getId_jpicking()+"");
		top.setVariable("{h_ini}"		, jor.getH_inicio());
		top.setVariable("{h_fin}"		, jor.getH_fin());
		
		
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("select_listado_de_picking", pick);

		
		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();

	}
}
