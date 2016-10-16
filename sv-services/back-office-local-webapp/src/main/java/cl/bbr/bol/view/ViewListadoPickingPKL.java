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
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.JornadaDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.RondaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Formulario que despliega un listado con las jornadas de picking
 * @author mleiva
 */
public class ViewListadoPickingPKL extends Command {

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
		
		long id_pedido = bizDelegate.getIdPedidoByRondaPKL(id_ronda);
		

		// 4.1 Listado Picking
		List listaPicking = new ArrayList();
		listaPicking = bizDelegate.getProductosRondaPKL(id_ronda);

		ArrayList pick = new ArrayList();
		
	    IValueSet fila_sector   = null; //Sector de Picking
	    IValueSet fila_seccion  = null; //Sección SAP
	    List productos = null;
	    List secciones = null;
	    List sectores  = new ArrayList();
	    String sector = new String();
	    String seccion = new String();
	    long id_sector = 0L;//ronda1.getId_sector();
	    long id_seccion= 0L;//ronda1.getId_seccion();
        boolean flag_seccion = false;
		for (int i = 0; i < listaPicking.size(); i++){
		    ProductosPedidoDTO ronda1 = new ProductosPedidoDTO();
		    ronda1 = (ProductosPedidoDTO) listaPicking.get(i);
		    sector  = ronda1.getSector();
		    seccion = ronda1.getSeccion();
		    
		    if (id_sector == 0 || id_sector != ronda1.getId_sector()){
		        id_sector = ronda1.getId_sector();
		        fila_sector = new ValueSet();
		        fila_sector.setVariable("{sector}", ronda1.getSector());
		        secciones = new ArrayList();
		    }
		    
	        if (id_seccion == 0 || id_seccion != ronda1.getId_seccion()){
	            id_seccion = ronda1.getId_seccion();
	            fila_seccion  = new ValueSet();
	            fila_seccion.setVariable("{seccion}", ronda1.getSeccion());
	            productos = new ArrayList();
	        }
	        
	        IValueSet fila_producto = new ValueSet();
			fila_producto.setVariable("{cod_sap}", String.valueOf(ronda1.getCod_producto()));
			fila_producto.setVariable("{descripcion}", ronda1.getDescripcion());
			fila_producto.setVariable("{cant_sol}", String.valueOf(ronda1.getCant_solic()));
			fila_producto.setVariable("{um}", ronda1.getUnid_medida());
            if ( ronda1.getObservacion() != null && !ronda1.getObservacion().equals("null") ) {
                fila_producto.setVariable("{observacion}", ronda1.getObservacion() );
			}else{
			    fila_producto.setVariable("{observacion}", "");
			}
            fila_producto.setVariable("{criterio_sustituto}", ronda1.getDescCriterio());
            productos.add(fila_producto);

            //Si la Seccion siguiente es diferente agrega el listado de Productos a la Seccion Actual
	        if ((i+1) < listaPicking.size() && id_seccion != ((ProductosPedidoDTO) listaPicking.get(i+1)).getId_seccion()){
	            fila_seccion.setDynamicValueSets("PRODUCTOS", productos);
	            secciones.add(fila_seccion);
	        }
		    
            //Si el Sector siguiente es diferente agrega el listado de Productos del Sector Actual
	        if ((i+1) < listaPicking.size() && id_sector != ((ProductosPedidoDTO) listaPicking.get(i+1)).getId_sector()){
	            fila_sector.setDynamicValueSets("SECCIONES", secciones);
	            sectores.add(fila_sector);
	        }else if((i+1) >= listaPicking.size()){
	            fila_seccion.setDynamicValueSets("PRODUCTOS", productos);
	            secciones.add(fila_seccion);

	            fila_sector.setDynamicValueSets("SECCIONES", secciones);
	            sectores.add(fila_sector);
	        }
		}
		
		top.setDynamicValueSets("SECTORES", sectores);
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
			}else {
				zonas += ", "+zon.getNombre();
			}
		}
		
		top.setVariable("{zonas_r}"		, zonas);
		// 5. Setea variables del template
		
		//Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(ronda.getF_creacion());
			
		//seteando calendar, segun la fecha, nueva manera de hacerlo
		Calendar cal1 = new GregorianCalendar();
		cal1.clear();
		cal1.set(Calendar.YEAR, Integer.parseInt(ronda.getF_creacion().substring(0,4)));
		cal1.set(Calendar.YEAR, Integer.parseInt(ronda.getF_creacion().substring(0,4)));
		cal1.set(Calendar.MONTH, Integer.parseInt(ronda.getF_creacion().substring(5,7))-1);
		cal1.set(Calendar.DAY_OF_MONTH, Integer.parseInt(ronda.getF_creacion().substring(8,10)));
		
		String strFecha = new SimpleDateFormat("yyyy-MM-dd").format(cal1.getTime());

		top.setVariable("{id_ronda}", param_id_ronda);
		top.setVariable("{id_pedido}" , id_pedido+"");
		top.setVariable("{fecha}"   , strFecha);
		if (ronda.getSector() == null){
			top.setVariable("{Sector}", Constantes.SECTOR_TIPO_PICKING_LIGHT_TXT);
		}else{
			top.setVariable("{Sector}", ronda.getSector());
		}
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("HH:mm:ss");
		Date fecha_impresion = new Date();
        String hora_imp = bartDateFormat.format(fecha_impresion);
		top.setVariable("{h_imp}"		, hora_imp);
		top.setVariable("{id_jornada}"	, ronda.getId_jpicking()+"");
		top.setVariable("{h_ini}"		, jor.getH_inicio());
		top.setVariable("{h_fin}"		, jor.getH_fin());
		
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("select_listado_de_picking", pick);

		// SE SETEA EN LA RONDA LA HORA DE IMPRESIÓN DEL LISTADO DE PICKING
		bizDelegate.setFechaImpListadoPKL(id_ronda);
		
		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}
}
